require recipes-kernel/linux/linux-mpfs_6.6.bb

SRC_URI:append = " \
    https://cdn.kernel.org/pub/linux/kernel/projects/rt/6.6/older/patch-6.6.74-rt48.patch.xz;name=preempt-rt-patch \
    file://preempt-rt.cfg \
    "
SRC_URI[preempt-rt-patch.sha256sum] = "f89df2a6b461687e7ca8d50e35a305b07e9277944ae7fe62375b58ea525debe6"