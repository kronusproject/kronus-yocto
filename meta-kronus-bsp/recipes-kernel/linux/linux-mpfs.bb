DESCRIPTION = "MPFS Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KCONFIG_MODE = "--alldefconfig"

KBUILD_DEFCONFIG ?= "mpfs_defconfig"

KERNEL_EXTRA_FEATURES ?= ""
KERNEL_FEATURES:remove = "features/debug/printk.scc"
KERNEL_FEATURES:remove = "features/kernel-sample/kernel-sample.scc"

require recipes-kernel/linux/linux-yocto.inc

# linux-6.1-mchp+fpga branch
SRCREV = "060758d16686da594dcef5d5ecf33c5328736857"
SRC_URI = "git://github.com/linux4microchip/linux.git;protocol=https;nobranch=1 \
           file://0001-perf-cpumap-Make-counter-as-unsigned-ints.patch \
          "

SRC_URI:append:mpfs-beaglev-fire = " \
    file://0002-PCIe-Change-controller-and-bridge-base-address.patch \
    file://0003-GPIO-Add-Microchip-CoreGPIO-driver.patch \
    file://0004-ADC-Add-Microchip-MCP356X-driver.patch \
    file://0005-Microchip-QSPI-Add-regular-transfers.patch \
    file://0007-MMC-SPI-Hack-to-support-non-DMA-capable-SPI-ctrl.patch \
    file://mpfs_cmdline.cfg \
    "

LINUX_VERSION ?= "6.1"
LINUX_VERSION_EXTENSION = ""
KERNEL_VERSION_SANITY_SKIP = "1"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "mpfs-beaglev-fire"