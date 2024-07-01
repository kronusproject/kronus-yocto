// SPDX-License-Identifier: GPL-2.0-only
/*
 * Kronus userspace I/O platform driver
 *
 * Based in part on drivers/uio/uio_pdrv_genirq.c by Magnus Damm,
 * Copyright (C) 2008 Magnus Damm.
 */

#include <linux/platform_device.h>
#include <linux/uio_driver.h>
#include <linux/spinlock.h>
#include <linux/module.h>
#include <linux/interrupt.h>
#include <linux/slab.h>
#include <linux/delay.h>

#include <linux/of.h>
#include <linux/of_platform.h>
#include <linux/of_address.h>

#define DRIVER_NAME "kronus-uio"

#define KRONUS_OFFS (0x100)
#define KRONUS_STAT_REG (KRONUS_OFFS + 0x00)
#define KRONUS_CTRL_REG (KRONUS_OFFS + 0x10)
#define KRONUS_REQ_REG (KRONUS_OFFS + 0x20)
#define KRONUS_RSP_REG (KRONUS_OFFS + 0x30)

#define KRONUS_CTRL_IRQ_MASK (0x00)
#define KRONUS_CTRL_RESET (0x01)
#define KRONUS_CTRL_ENABLE (0x10)

struct kronus_uio_platdata {
	struct uio_info *info;
	spinlock_t lock;
	void __iomem *base;
	struct platform_device *pdev;
};

static irqreturn_t kronus_uio_handler(int irq, struct uio_info *info)
{
	struct kronus_uio_platdata *priv = info->priv;

	spin_lock(&priv->lock);
	iowrite32(KRONUS_CTRL_IRQ_MASK, priv->base + KRONUS_CTRL_REG);
	spin_unlock(&priv->lock);

	return IRQ_HANDLED;
}

static int kronus_uio_irqcontrol(struct uio_info *info, s32 irq_on)
{
	struct kronus_uio_platdata *priv = info->priv;
	unsigned long flags;

	spin_lock_irqsave(&priv->lock, flags);
	if (irq_on) {
		iowrite32(KRONUS_CTRL_ENABLE, priv->base + KRONUS_CTRL_REG);
	} else {
		iowrite32(KRONUS_CTRL_IRQ_MASK, priv->base + KRONUS_CTRL_REG);
	}
	spin_unlock_irqrestore(&priv->lock, flags);

	return 0;
}

#if 0
static void kronus_uio_reset(struct platform_device *pdev)
{
	struct uio_info *info = dev_get_platdata(&pdev->dev);
	struct kronus_uio_platdata *priv = info->priv;

	iowrite32(KRONUS_CTRL_RESET, priv->base + KRONUS_CTRL_REG);
	udelay(1);
	iowrite32(KRONUS_CTRL_ENABLE, priv->base + KRONUS_CTRL_REG);
}
#endif

static int kronus_uio_probe(struct platform_device *pdev)
{
	struct uio_info *info = dev_get_platdata(&pdev->dev);
	struct device_node *node = pdev->dev.of_node;
	struct kronus_uio_platdata *priv;
	struct uio_mem *mem;
	int ret = -EINVAL;

	if (node) {
		const char *name;

		info = devm_kzalloc(&pdev->dev, sizeof(*info), GFP_KERNEL);
		if (!info) {
			dev_err(&pdev->dev, "failed to kmalloc\n");
			return -ENOMEM;
		}

		if (!of_property_read_string(node, "linux,uio-name", &name))
			info->name = devm_kstrdup(&pdev->dev, name, GFP_KERNEL);
		else
			info->name = devm_kasprintf(&pdev->dev, GFP_KERNEL,
						    "%pOFn", node);

		info->version = "devicetree";
	}

	if (!info || !info->name || !info->version) {
		dev_err(&pdev->dev, "missing platform data\n");
		return ret;
	}

	if (info->handler || info->irqcontrol ||
	    info->irq_flags & IRQF_SHARED) {
		dev_err(&pdev->dev, "interrupt configuration error\n");
		return ret;
	}

	priv = devm_kzalloc(&pdev->dev, sizeof(*priv), GFP_KERNEL);
	if (!priv) {
		dev_err(&pdev->dev, "unable to kmalloc\n");
		return -ENOMEM;
	}

	priv->info = info;
	spin_lock_init(&priv->lock);
	priv->pdev = pdev;

	if (!info->irq) {
		ret = platform_get_irq_optional(pdev, 0);
		info->irq = ret;
		if (ret == -ENXIO)
			info->irq = UIO_IRQ_NONE;
		else if (ret == -EPROBE_DEFER)
			return ret;
		else if (ret < 0) {
			dev_err(&pdev->dev, "failed to get IRQ\n");
			return ret;
		}
	}

	mem = &info->mem[0];

	for (int i = 0; i < pdev->num_resources; ++i) {
		struct resource *res = &pdev->resource[i];

		if (res->flags != IORESOURCE_MEM)
			continue;

		mem->memtype = UIO_MEM_PHYS;
		mem->addr = res->start & PAGE_MASK;
		mem->offs = res->start & ~PAGE_MASK;
		mem->size = (mem->offs + resource_size(res) + PAGE_SIZE - 1) &
			    PAGE_MASK;
		mem->name = res->name;

		++mem;
		mem->size = 0;

		priv->base = devm_platform_ioremap_resource(pdev, i);
		if (IS_ERR(priv->base)) {
			dev_err(&pdev->dev, "failed to map memory\n");
			return PTR_ERR(priv->base);
		}

		break;
	}

	if (!priv->base) {
		dev_err(&pdev->dev, "missing memory map\n");
		return -EINVAL;
	}

	info->handler = kronus_uio_handler;
	info->irqcontrol = kronus_uio_irqcontrol;
	info->open = NULL;
	info->release = NULL;
	info->priv = priv;

	ret = devm_uio_register_device(&pdev->dev, priv->info);
	if (ret) {
		dev_err(&pdev->dev, "failed to register device: %s\n",
			info->name);
	} else {
		dev_info(&pdev->dev, "registered device: %s\n", info->name);
	}

	return ret;
}

#ifdef CONFIG_OF
static struct of_device_id kronus_uio_match[] = {
	{ .compatible = "kronus-uio" },
	{ /* Sentinel */ },
};
#endif

static struct platform_driver kronus_uio = {
	.probe = kronus_uio_probe,
	.driver = {
		.name = DRIVER_NAME,
		.pm = NULL,
		.of_match_table = of_match_ptr(kronus_uio_match),
	},
};

module_platform_driver(kronus_uio);

MODULE_AUTHOR("Brett Witherspoon");
MODULE_DESCRIPTION("Kronus userspace I/O platform driver");
MODULE_LICENSE("GPL v2");
MODULE_ALIAS("platform:" DRIVER_NAME);
