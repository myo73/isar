#!/bin/bash
#
# This software is a part of ISAR.
# Copyright (C) 2015-2017 ilbers GmbH
# Copyright (c) 2018 Siemens AG

set -e

# Go to build directory
cd $1

# Add target architecture
dpkg --add-architecture $2

# To avoid Perl locale warnings:
export LC_ALL=C
export LANG=C
export LANGUAGE=C

# If autotools files have been created, update their timestamp to
# prevent them from being regenerated
for i in configure aclocal.m4 Makefile.am Makefile.in; do
    if [ -f "${i}" ]; then
        touch "${i}"
    fi
done

# Build the package
dpkg-buildpackage -a$2
