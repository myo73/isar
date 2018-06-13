# This software is a part of ISAR.
# Copyright (C) 2015-2018 ilbers GmbH

inherit dpkg-base

# Add dependency from buildchroot creation
do_build[depends] = "buildchroot-cross:do_build"

ROOTFS_DIR="${BUILDCHROOT_CROSS_DIR}"

# Build package from sources using build script
dpkg_runbuild() {
    E="${@ bb.utils.export_proxies(d)}"
    sudo -E chroot ${ROOTFS_DIR} /build.sh ${PP}/${PPS} ${DISTRO_ARCH}
}
