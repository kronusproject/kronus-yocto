require recipes-core/images/kronus-image.bb

DESCRIPTION = "Kronus development image"

IMAGE_FEATURES += "\
    tools-debug \
    tools-sdk \
    tools-profile \
    package-management \
    doc-pkgs \
    dev-pkgs \
    "

IMAGE_INSTALL += "\
    dtc \
    cmake \
    ninja \
    "
