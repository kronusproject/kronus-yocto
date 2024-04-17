Kronus Yocto
============

## Build

```shell
./scipts/kas-container build scripts/kas/qemuriscv64.yml
```

```shell
KAS_CONTAINER_ENGINE=podman ./scripts/kas-container --runtime-args "--pids-limit=65536" build scripts/kas/qemuriscv64.yml
```

## Emulator (QEMU)

```shell
./scripts/kas-container --runtime-args "--network=host" shell -c 'runqemu nographic slirp bootparams="quiet systemd.show_status=yes"' scripts/kas/qemuriscv64.yml
```
