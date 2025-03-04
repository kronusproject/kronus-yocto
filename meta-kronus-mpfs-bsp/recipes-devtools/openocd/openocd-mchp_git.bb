SUMMARY = "Free and Open On-Chip Debugging, In-System Programming and Boundary-Scan Testing"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=599d2d1ee7fc84c0467b3d19801db870"
DEPENDS = "libusb-compat"
RDEPENDS:${PN} = "libusb1"

SRC_URI = " \
    git://github.com/microchip-fpga/openocd.git;protocol=https;name=openocd;branch=microchip-pic64gx-curiosity \
    git://repo.or.cz/r/git2cl.git;protocol=http;destsuffix=tools/git2cl;name=git2cl;branch=master \
    git://github.com/msteveb/jimtcl.git;protocol=https;destsuffix=git/jimtcl;name=jimtcl;branch=master \
    git://repo.or.cz/r/libjaylink.git;protocol=http;destsuffix=git/src/jtag/drivers/libjaylink;name=libjaylink;branch=master \
"

SRCREV_FORMAT = "openocd"
SRCREV_openocd = "85d0785e5f0b715c1c3e13058745d61f177556b6"
SRCREV_git2cl = "8373c9f74993e218a08819cbcdbab3f3564bbeba"
SRCREV_jimtcl = "1933e5457b9512d39ebbe11ed32578aada149f49"
SRCREV_libjaylink = "0d23921a05d5d427332a142d154c213d0c306eb1"

PV = "0.12+git"
S = "${WORKDIR}/git"

inherit pkgconfig autotools-brokensep gettext

BBCLASSEXTEND += "native nativesdk"

EXTRA_OECONF = " \
    --enable-microchip-fp6 \
    --disable-microchip-efp6 \
    --disable-microchip-fp5 \
    --disable-ftdi \
    --disable-stlink \
    --disable-ti-icdi \
    --disable-ulink \
    --disable-angie \
    --disable-usb-blaster-2 \
    --disable-ft232r \
    --disable-vsllink \
    --disable-xds110 \
    --disable-cmsis-dap-v2 \
    --disable-osbdm \
    --disable-opendous \
    --disable-armjtagew \
    --disable-rlink \
    --disable-usbprog \
    --disable-esp-usb-jtag \
    --disable-cmsis-dap \
    --disable-nulink \
    --disable-kitprog \
    --disable-usb-blaster \
    --disable-presto \
    --disable-openjtag \
    --disable-buspirate \
    --disable-jlink \
    --disable-parport \
    --disable-jtag_vpi \
    --disable-vdebug \
    --disable-jtag_dpi \
    --disable-amtjtagaccel \
    --disable-bcm2835gpio \
    --disable-imx_gpio \
    --disable-am335xgpio \
    --disable-ep93xx \
    --disable-at91rm9200 \
    --disable-gw16012 \
    --disable-sysfsgpio \
    --disable-xlnx-pcie-xvc \
    --disable-doxygen-html \
    --disable-werror \
    "

do_configure() {
    ./bootstrap nosubmodule
    install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.guess ${S}/jimtcl/autosetup
    install -m 0755 ${STAGING_DATADIR_NATIVE}/gnu-config/config.sub ${S}/jimtcl/autosetup
    oe_runconf ${EXTRA_OECONF}
}

do_install() {
    oe_runmake DESTDIR=${D} install
    if [ -e "${D}${infodir}" ]; then
      rm -Rf ${D}${infodir}
    fi
    if [ -e "${D}${mandir}" ]; then
      rm -Rf ${D}${mandir}
    fi
    if [ -e "${D}${bindir}/.debug" ]; then
      rm -Rf ${D}${bindir}/.debug
    fi
}

FILES:${PN} = " \
  ${datadir}/openocd/* \
  ${bindir}/openocd \
  "

# Can't be built with ccache
CCACHE_DISABLE = "1"
