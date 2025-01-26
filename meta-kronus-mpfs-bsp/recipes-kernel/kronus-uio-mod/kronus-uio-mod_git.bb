SUMMARY = "Kronus userspace I/O Linux kernel module"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/kronusproject/kronus-uio.git;protocol=https;branch=main"

PV = "0.1.0+git${SRCPV}"

S = "${WORKDIR}/git"

RPROVIDES:${PN} += "kernel-module-kronus-uio"

KERNEL_MODULE_AUTOLOAD += "kronus_uio"

COMPATIBLE_MACHINE = "(mpfs-beaglev-fire|mpfs-disco-kit)"
