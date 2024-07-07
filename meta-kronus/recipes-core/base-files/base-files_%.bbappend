FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_install:append () {
    # Upstream recipe installs these with mode 0755
    install -m 0644 ${S}/share/dot.profile ${D}${sysconfdir}/skel/.profile
    install -m 0644 ${S}/share/dot.bashrc ${D}${sysconfdir}/skel/.bashrc
}