FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append := " \
    file://root/dot.bashrc \
    file://user/dot.bashrc \
    "

do_install:append () {
    install -m 0644 ${S}/share/dot.profile ${D}${ROOT_HOME}/.profile
    install -m 0644 ${S}/root/dot.bashrc ${D}${ROOT_HOME}/.bashrc

    # Upstream recipe installs these with mode 0755
    install -m 0644 ${S}/share/dot.profile ${D}${sysconfdir}/skel/.profile
    install -m 0644 ${S}/user/dot.bashrc ${D}${sysconfdir}/skel/.bashrc
}