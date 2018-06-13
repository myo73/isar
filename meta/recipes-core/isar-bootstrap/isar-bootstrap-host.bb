# Minimal host Debian root file system
#
# This software is a part of ISAR.
# Copyright (c) Siemens AG, 2018
#
# SPDX-License-Identifier: MIT

Description = "Minimal host Debian root file system"

include isar-bootstrap.inc
inherit isar-bootstrap-helper

do_bootstrap[stamp-extra-info] = "${DISTRO}-${DISTRO_ARCH}"
do_bootstrap[vardeps] += "DISTRO_APT_SOURCES"
do_bootstrap[vardeps] += "DISTRO_APT_PREMIRRORS"
do_bootstrap() {
    if [ -e "${ROOTFSDIR}" ]; then
       sudo umount -l "${ROOTFSDIR}/dev" || true
       sudo umount -l "${ROOTFSDIR}/proc" || true
       sudo rm -rf "${ROOTFSDIR}"
    fi
    E="${@bb.utils.export_proxies(d)}"
    sudo -E "${DEBOOTSTRAP}" --verbose \
                             --variant=minbase \
                             --include=locales \
                             ${@get_distro_components_argument(d)} \
                             ${DEBOOTSTRAP_KEYRING} \
                             "${@get_distro_suite(d)}" \
                             "${ROOTFSDIR}" \
                             "${@get_distro_source(d)}"
}
addtask bootstrap before do_build after do_generate_keyring

do_deploy[stamp-extra-info] = "${DISTRO}-${HOST_ARCH}"
do_deploy[dirs] = "${DEPLOY_DIR_IMAGE}"
do_deploy() {
    ln -Tfsr "${ROOTFSDIR}" "${DEPLOY_DIR_IMAGE}/isar-bootstrap-${DISTRO}-${HOST_ARCH}"
}
addtask deploy before do_build after do_apt_update

CLEANFUNCS = "clean_deploy"
clean_deploy() {
     rm -f "${DEPLOY_DIR_IMAGE}/${PN}-${DISTRO}-${HOST_ARCH}"
}
