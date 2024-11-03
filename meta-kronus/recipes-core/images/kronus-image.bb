DESCRIPTION = "Kronus image"

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-distro-base \
    packagegroup-machine-base \
    packagegroup-kronus-cmdline \
    tmux \
    htop \
    libgpiod-tools \
    can-utils \
    mtd-utils \
    i2c-tools \
    ${@bb.utils.contains('COMBINED_FEATURES', 'usbhost', 'usbutils', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'pci', 'pciutils', '', d)} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

inherit core-image
