DESCRIPTION = "Daemon used for communication between MCU and SoC via UART on HiFive Premier P550"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit systemd

SYSTEMD_SERVICE:${PN} = "es-bmcd.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

SRC_URI = "file://es-bmc-daemon"

S = "${WORKDIR}/es-bmc-daemon"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
	oe_runmake
}

do_install() {
	install -m 755 -D ${S}/es-bmcd -t ${D}/usr/bin
	install -m 755 -D ${S}/es-bmcd.service -t ${D}/${systemd_system_unitdir}
	install -m 755 ${S}/es-bmcd.sh ${D}/usr/bin/
}

FILES:${PN} += "${systemd_system_unitdir} /usr/bin"
