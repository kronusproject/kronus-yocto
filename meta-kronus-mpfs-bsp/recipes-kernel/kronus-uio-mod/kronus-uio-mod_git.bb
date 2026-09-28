SUMMARY = "Kronus userspace I/O Linux kernel module"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

# kronus-uio main as of 2024-10-05 ("feat: Update IRQ control")
SRCREV = "427738ddbf0cd3c7cc64a57cccc94a24ce8a7015"
SRC_URI = "git://github.com/kronusproject/kronus-uio.git;protocol=https;branch=main"

PV = "0.1.0+git${SRCPV}"

S = "${WORKDIR}/git"

RPROVIDES:${PN} += "kernel-module-kronus-uio"

KERNEL_MODULE_AUTOLOAD += "kronus_uio"

COMPATIBLE_MACHINE = "(mpfs-beaglev-fire|mpfs-disco-kit)"
