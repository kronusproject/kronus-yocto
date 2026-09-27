# Upgrade targets (checked 2026-09-27)

Where Kronus is today versus the newest upstream versions, taken from upstream git tags and recipes.

## Summary

| Component | Kronus today | Newest upstream | Notes |
|---|---|---|---|
| Yocto release | Scarthgap 5.0.8 | **Wrynose 6.0.3** (LTS to April 2030) | Scarthgap is at 5.0.20, with LTS extended to April 2028 |
| BitBake | 2.8.x | **2.18.0** (yocto-6.0.3) | Scarthgap 5.0.20 ships BitBake 2.8.1 |
| Kernel | linux4microchip+fpga-2025.03 (6.6.75) | **linux4microchip-2026.04.2 (6.18.35)** | meta-mchp still pins 2026.04.1 (6.18.17, `7fbe4f6`) |
| RT kernel | separate `linux-mpfs-rt` 6.6.74-rt48 | same kernel + `CONFIG_PREEMPT_RT=y` | PREEMPT_RT is in mainline, so no external patch is needed |
| U-Boot | u-boot-mchp 2023.07 @ linux4microchip+fpga-2025.03 | **u-boot-2025.07-mchp @ `9fa52b8`** (tagged linux4microchip-2026.04) | A `u-boot-2026.07-mchp` branch exists, but meta-mchp doesn't use it yet |
| HSS | fork of hart-software-services (master) | **v2026.04.1** (payload generator 0.99.53) | Rebase the Kronus fork onto this |
| OpenSBI | built into HSS | **1.2** (vendored in HSS v2026.04.1) | On MPFS, OpenSBI only comes from HSS. meta-mchp masks the OpenSBI recipe |
| Libero | 2024.2 | **2025.2** (named in the HSS v2026.04.1 README) | |
| Firmware toolchain | SoftConsole 2022.2 | **xPack GNU RISC-V Embedded GCC** | HSS now marks SoftConsole as deprecated |

## Findings

**Microchip's Yocto BSP has moved.** `polarfire-soc/meta-polarfire-soc-yocto-bsp` has been archived (last commit 2026-04-21). Its replacement is `linux4microchip/meta-mchp` (layer `meta-mchp-polarfire-soc`). meta-mchp has **only a `scarthgap` branch**: every layer sets `LAYERSERIES_COMPAT = "scarthgap"`, and its kas file is on oe-core yocto-5.0.19. So Microchip doesn't yet support Wrynose for PolarFire SoC. Kronus uses its own `meta-kronus-mpfs-bsp` rather than Microchip's layer, so this doesn't block a Wrynose move. It does mean the Kronus layer is the only one that will be tested on Wrynose.

**The "+fpga" kernel line has ended.** The last `+fpga` tag is `linux4microchip+fpga-2025.10.1` (6.12.48), and there is no `linux-6.18-mchp+fpga` branch. From 2026.04 on, PolarFire SoC is built from the unified `linux-6.18-mchp` branch under `linux4microchip-YYYY.MM` tags. That branch includes the parts Kronus uses: `mpfs_defconfig`, `mpfs-beaglev-fire-fabric.dtsi`, `mpfs-disco-kit-fabric.dtsi`, `mpfs-sys-controller`, `mpfs-auto-update`, `uio_pdrv_genirq` and `xilinx_dma`. The recipe's `SRC_URI`/`SRCREV` need the new tag name, and `linux-mpfs_6.6.bb` becomes `linux-mpfs_6.18.bb`.

**PREEMPT_RT.** `arch/riscv/Kconfig` selects `ARCH_SUPPORTS_RT` in both 6.12 (+fpga-2025.10.1) and 6.18 (2026.04.2). So on 6.18 the RT build can drop the -rt patch and the separate 6.6.74 base. It becomes a config fragment on the normal kernel recipe.

**Yocto 6.0 Wrynose** was released 2026-05-14. It ships Linux 6.18 LTS, GCC 15.2, glibc 2.43 and LLVM 22.1. `bitbake-setup` and `bitbake-config-build` replace the old poky checkout (the poky repo has no 6.x tags). Distros not based on Poky now default to systemd, and the host needs about 32 GB of RAM and 140 GB of disk. meta-openembedded and meta-sifive both have `wrynose` branches, so the `unmatched` target can move too.

## Recommended path

1. **Kernel/boot stack on Scarthgap first.** Move oe-core/bitbake to yocto-5.0.20 and switch to linux4microchip-2026.04.2 (6.18). Use U-Boot 2025.07-mchp @ `9fa52b8`, rebase the HSS fork onto v2026.04.1, and turn RT into `PREEMPT_RT` on the same kernel. This is the combination Microchip validates in meta-mchp, so failures here are Kronus-specific. Scarthgap is supported until April 2028.
2. **Then Yocto 6.0 Wrynose (6.0.3 / bitbake 2.18.0).** This step is layer-only: bump `LAYERSERIES_COMPAT`, apply the 5.1 to 6.0 migration notes, move kas repos to `wrynose` and choose an init system. The kernel doesn't change, because Wrynose's own default is also 6.18.
3. **kronus-system:** Libero 2025.2 and the xPack toolchain for HSS, alongside step 1.

## Open risks

- `kronus-uio-mod` is on `AUTOREV`. It needs pinning, and its build needs checking against 6.18 kernel APIs.
- The hand-copied `*-fabric.dtsi` files need diffing against the 2026.04 upstream versions.
- The 2023.07 to 2025.07 U-Boot jump changes the environment and boot script. Microchip's per-board env and `.cfg` files in meta-mchp are the reference.
- Unverified: the exact Wrynose `linux-yocto` versions and the full 6.0 migration-note impact on the Kronus layers. Both need checking during step 2.

## Sources

- linux4microchip/linux, u-boot-mchp and meta-mchp tags and branches, and polarfire-soc/hart-software-services v2026.04.1 (`git ls-remote` and file reads, 2026-09-27)
- openembedded/bitbake and openembedded-core tags `yocto-6.0.3` and `yocto-5.0.20`
- [Yocto release process / LTS dates](https://docs.yoctoproject.org/current/ref-manual/release-process.html)
- [Yocto Project 6.0 "Wrynose" is here](https://www.yoctoproject.org/blog/2026/05/13/yocto-project-6-0-wrynose-is-here/)
- [CNX Software: Yocto 6.0 Wrynose released with Linux 6.18 LTS](https://www.cnx-software.com/2026/05/14/yocto-project-6-0-wrynose-released-with-linux-6-18-lts/)
