FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://50-root.conf \
    file://60-can.network \
    file://60-gadget.network \
    file://65-gadget-dhcp-server.network \
    "

PACKAGECONFIG:append = "openssl repart"

do_install:append() {
    install -d ${D}${sysconfdir}/repart.d/
    install -m 0644 ${WORKDIR}/50-root.conf ${D}${sysconfdir}/repart.d/

    if ${@bb.utils.contains('COMBINED_FEATURES', 'can', 'true', 'false', d)}; then
        install -D -m 0644 ${WORKDIR}/60-can.network ${D}${systemd_unitdir}/network/
    fi

    if ${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', 'true', 'false', d)}; then
        install -D -m 0644 ${WORKDIR}/60-gadget.network ${D}${systemd_unitdir}/network/
        install -D -m 0644 ${WORKDIR}/65-gadget-dhcp-server.network ${D}${systemd_unitdir}/network/
    fi
}

FILES:${PN} += " \
    ${sysconfdir}/repart.d/* \
    ${systemd_unitdir}/network/* \
    "
