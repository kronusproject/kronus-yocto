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
    packagegroup-kronus-cmdline \
    kernel-modules \
    dtc \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

inherit core-image
