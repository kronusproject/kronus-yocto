DESCRIPTION = "Kronus development image"

IMAGE_FEATURES += "\
    ssh-server-openssh \
    tools-debug \
    tools-sdk \
    tools-profile \
    package-management \
    doc-pkgs \
    dev-pkgs \
    "

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-machine-base \
    packagegroup-kronus-cmdline \
    dtc \
    cmake \
    ninja \
    tmux \
    htop \
    libgpiod-tools \
    can-utils \
    mtd-utils \
    i2c-tools \
    ${@bb.utils.contains('COMBINED_FEATURES', 'usbhost', 'usbutils', '', d)} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

inherit core-image
