# This software is a part of ISAR.
# Copyright (C) 2015-2016 ilbers GmbH

inherit dpkg-base

# Add dependency from buildchroot creation
do_build[depends] = "buildchroot:do_build"

ROOTFS_DIR = "${BUILDCHROOT_DIR}"

# Build package from sources using build script
dpkg_runbuild() {
    E="${@ bb.utils.export_proxies(d)}"
    sudo -E chroot ${ROOTFS_DIR} /build.sh ${PP}/${PPS}
}
