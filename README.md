Kronus Yocto
============

## Build

```shell
./scipts/kas-container build scripts/kas/kronus.yml
```

```shell
./scripts/kas-container --runtime-args "--pids-limit=65536" build scripts/kas/kronus.yml
```

## Emulator (QEMU)

```shell
./scripts/kas-container --runtime-args "--network=host" shell -c 'runqemu nographic slirp bootparams="quiet systemd.show_status=yes"' scripts/kas/kronus.yml
```
