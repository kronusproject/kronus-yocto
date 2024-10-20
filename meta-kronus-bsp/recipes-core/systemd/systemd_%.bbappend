FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://50-root.conf"

PACKAGECONFIG:append = " openssl repart"

do_install:append() {
    install -d ${D}${sysconfdir}/repart.d/
    install -m 0644 ${WORKDIR}/50-root.conf ${D}${sysconfdir}/repart.d/
}

FILES:${PN} += "${sysconfdir}/repart.d/*"

SRC_URI:append:mpfs-beaglev-fire = " \
    file://60-can.network \
    file://60-gadget.network \
    "

do_install:append:mpfs-beaglev-fire() {
    install -D -m 0644 ${WORKDIR}/60-gadget.network ${D}${systemd_unitdir}/network/
    install -D -m 0644 ${WORKDIR}/60-can.network ${D}${systemd_unitdir}/network/
}

FILES:${PN}:append:mpfs-beaglev-fire = "\
    ${systemd_unitdir}/network/ \
"
