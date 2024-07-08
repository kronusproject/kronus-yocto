require recipes-kernel/linux/linux-mpfs_6.6.bb

SRC_URI:append = " \
    https://cdn.kernel.org/pub/linux/kernel/projects/rt/6.6/patch-6.6.35-rt34.patch.xz;name=preempt-rt-patch \
    file://0001-preempt-rt-arch-riscv-kernel-cpufeature-fix-no-return-value.patch \
    file://preempt-rt.cfg \
    "
SRC_URI[preempt-rt-patch.sha256sum] = "fe75783c01f2ac3ed45f634b07cf005a3a4778495dc018cce3b9b961d3743266"