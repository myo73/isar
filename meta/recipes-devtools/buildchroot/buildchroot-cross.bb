# Root filesystem for packages cross-building
#
# This software is a part of ISAR.
# Copyright (C) 2015-2016 ilbers GmbH

DESCRIPTION = "Isar development cross-filesystem"

LICENSE = "gpl-2.0"
LIC_FILES_CHKSUM = "file://${LAYERDIR_isar}/licenses/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

FILESPATH_prepend := "${THISDIR}/files:"
SRC_URI = "file://configscript.sh \
           file://build-cross.sh"
PV = "1.0"

inherit isar-bootstrap-helper

BUILDCHROOT_CROSS_PREINSTALL ?= "gcc-multilib \
                                 make \
                                 build-essential \
                                 debhelper \
                                 autotools-dev \
                                 dpkg \
                                 locales \
                                 docbook-to-man \
                                 apt \
                                 automake \
                                 devscripts \
                                 equivs"

# TODO: make this inclusion depending on the target arch
BUILDCHROOT_CROSS_PREINSTALL += "binutils"

WORKDIR = "${TMPDIR}/work/${DISTRO}-${DISTRO_ARCH}/${PN}"

do_build[stamp-extra-info] = "${DISTRO}-${DISTRO_ARCH}"
do_build[root_cleandirs] = "${BUILDCHROOT_CROSS_DIR} \
                            ${BUILDCHROOT_CROSS_DIR}/isar-apt \
                            ${BUILDCHROOT_CROSS_DIR}/downloads \
                            ${BUILDCHROOT_CROSS_DIR}/home/builder"
do_build[depends] = "isar-apt:do_cache_config isar-bootstrap-host:do_deploy"

do_build() {
    setup_root_file_system --host-arch "${BUILDCHROOT_CROSS_DIR}" ${BUILDCHROOT_CROSS_PREINSTALL}

    # Install package builder script
    sudo chmod -R a+rw "${BUILDCHROOT_CROSS_DIR}/home/builder"
    sudo install -m 755 ${WORKDIR}/build-cross.sh ${BUILDCHROOT_CROSS_DIR}/build.sh

    # Configure root filesystem
    sudo install -m 755 ${WORKDIR}/configscript.sh ${BUILDCHROOT_CROSS_DIR}
    sudo chroot ${BUILDCHROOT_CROSS_DIR} /configscript.sh
}
