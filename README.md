Kronus Yocto
============

## Setup

```shell
sudo cp *.rules /etc/udev/rules.d/
sudo udevadm control --reload
```

## Build

```shell
./scipts/kas-container build kas/qemuriscv64.yml
```

```shell
KAS_CONTAINER_ENGINE=podman ./scripts/kas-container --runtime-args "--pids-limit=65536" build kas/qemuriscv64.yml
```

## Emulator (QEMU)

```shell
./scripts/kas-container --runtime-args "--network=host" shell -c 'runqemu nographic slirp bootparams="quiet systemd.show_status=yes"' kas/qemuriscv64.yml
```

### CAN

```shell
ip link add dev can0 type vcan
ip link set can0 up
ip -d link show can0
```

```shell
./scripts/kas-container --runtime-args "--network=host" shell -c 'runqemu nographic slirp bootparams="quiet systemd.show_status=yes" qemuparams="-object can-bus,id=canbus0 -object can-host-socketcan,id=canhost0,if=can0,canbus=canbus0 -device kvaser_pci,canbus=canbus0"' kas/qemuriscv64.yml
```

```shell
./scripts/kas-container --runtime-args "--network=host" shell -c 'runqemu nographic slirp bootparams="quiet systemd.show_status=yes" qemuparams="-object can-bus,id=canbus0-bus -object can-host-socketcan,if=can0,canbus=canbus0-bus,id=canbus0-socketcan -device ctucan_pci,canbus0=canbus0-bus,canbus1=canbus0-bus"' kas/qemuriscv64.yml
```
