do_install:append() {
    echo "%wheel ALL=(ALL:ALL) ALL" > ${D}${sysconfdir}/sudoers.d/00_wheel
    chmod 0440 ${D}${sysconfdir}/sudoers.d/00_wheel
}

CONFFILES:${PN}-lib += "${sysconfdir}/sudoers.d/00_wheel"