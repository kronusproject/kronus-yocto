DESCRIPTION = "MPFS Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KCONFIG_MODE = "--alldefconfig"

KBUILD_DEFCONFIG ?= "mpfs_defconfig"

KERNEL_EXTRA_FEATURES ?= ""

require recipes-kernel/linux/linux-yocto.inc

SRCREV = "linux4microchip+fpga-2025.03"
SRC_URI = " \
    git://github.com/linux4microchip/linux.git;protocol=https;nobranch=1 \
    file://kronus.cfg \
    "

SRC_URI:append:mpfs-beaglev-fire = " \
    file://mpfs_cmdline.cfg \
    file://usb_gadget.cfg \
    file://mpfs-beaglev-fire.dts \
    file://mpfs-beaglev-fire-fabric.dtsi \
    "

SRC_URI:append:mpfs-disco-kit = " \
    file://mpfs_cmdline.cfg \
    file://mpfs-disco-kit.dts \
    file://mpfs-disco-kit-fabric.dtsi \
    "

LINUX_VERSION ?= "6.6.75"
LINUX_VERSION_EXTENSION = ""
KERNEL_VERSION_SANITY_SKIP = "1"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE = "(mpfs-beaglev-fire|mpfs-disco-kit)"

do_configure:append() {
    cp ${WORKDIR}/${MACHINE}.dts ${S}/arch/riscv/boot/dts/microchip/
    cp ${WORKDIR}/${MACHINE}-fabric.dtsi ${S}/arch/riscv/boot/dts/microchip/
}