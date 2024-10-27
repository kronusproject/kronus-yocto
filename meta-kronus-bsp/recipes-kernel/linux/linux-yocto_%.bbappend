FILESEXTRAPATHS:prepend:qemuriscv64 := "${THISDIR}/files:"

SRC_URI:append:qemuriscv64 = " \
    file://can.cfg \
    "
