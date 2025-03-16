FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://add-tmp451-chip.patch \
    "

SYSTEMD_AUTO_ENABLE:${PN}-fancontrol:hifive-premier-p550 = "enable"
