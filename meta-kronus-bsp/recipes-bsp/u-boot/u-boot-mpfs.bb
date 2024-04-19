require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

DEPENDS += "bc-native dtc-native u-boot-mkenvimage-native hss-payload-generator-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

PV = "2023.07+git${SRCPV}"
SRCREV = "049a256e42b72a65ac3329fbe3085b47bdf20cbc"
SRC_URI = "git://github.com/polarfire-soc/u-boot.git;protocol=https;nobranch=1  \
           file://${HSS_PAYLOAD}.yaml \
           "

SRC_URI:append:mpfs-beaglev-fire = "file://${UBOOT_ENV}.cmd \
                                    file://${MACHINE}.cfg \
                                    file://uEnv.txt \
                                   "

ENV_SOURCE ?= "uEnv"
ENV_FILENAME ?= "uboot.env"

do_compile:append() {
    if [ -f "${WORKDIR}/${ENV_SOURCE}.txt" ]; then
        mkenvimage ${MKENVIMAGE_EXTRA_ARGS} -s ${UBOOT_ENV_SIZE} ${WORKDIR}/${ENV_SOURCE}.txt -o ${ENV_FILENAME}
    fi
}

do_deploy:append() {
    if [ -e  ${B}/${ENV_FILENAME} ]; then
        install -Dm 0644 ${B}/${ENV_FILENAME} ${DEPLOYDIR}
    fi

    hss-payload-generator -c ${WORKDIR}/${HSS_PAYLOAD}.yaml -v ${DEPLOYDIR}/payload.bin
}

COMPATIBLE_MACHINE = "mpfs-beaglev-fire"
