SUMMARY = "Kronus full-featured Linux system"
DESCRIPTION = "Package group bringing in packages needed for a full-featured Linux system"

inherit packagegroup

VIRTUAL-RUNTIME_base-utils = "packagegroup-kronus-base-utils"
VIRTUAL-RUNTIME_base-utils-hwclock = "util-linux-hwclock"
VIRTUAL-RUNTIME_base-utils-syslog = ""
VIRTUAL-RUNTIME_vim = "vim"

PACKAGES = "\
    packagegroup-kronus-cmdline \
    packagegroup-kronus-cmdline-utils \
    packagegroup-kronus-cmdline-extended \
    packagegroup-kronus-cmdline-dev-utils \
    packagegroup-kronus-cmdline-multiuser \
    packagegroup-kronus-cmdline-initscripts \
    packagegroup-kronus-cmdline-sys-services \
    "

RDEPENDS:packagegroup-kronus-cmdline = "\
    packagegroup-kronus-cmdline-utils \
    packagegroup-kronus-cmdline-extended \
    packagegroup-kronus-cmdline-dev-utils \
    packagegroup-kronus-cmdline-multiuser \
    "

RDEPENDS:packagegroup-kronus-cmdline-utils = "\
    acl \
    attr \
    bc \
    makedevs \
    "

RDEPENDS:packagegroup-kronus-cmdline-extended = "\
    ethtool \
    iptables \
    module-init-tools \
    openssl \
    vim \
    vim-vimrc \
    "

RDEPENDS:packagegroup-kronus-cmdline-dev-utils = "\
    git \
    make \
    "

RDEPENDS:packagegroup-kronus-cmdline-multiuser = "\
    bzip2 \
    cracklib \
    gzip \
    shadow \
    sudo \
    "
