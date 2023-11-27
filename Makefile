# Copyright 2023 Bahman Movaqar
#
# This file is part of BJForth.
#
# BJForth is free software: you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# BJForth is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License
# along with BJForth. If not, see <https://www.gnu.org/licenses/>.
####################################################################################################

SHELL := /usr/bin/env bash -e -o pipefail
.DEFAULT_GOAL := test

####################################################################################################

export ROOT := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))

####################################################################################################

gradle.gradle ?= ./gradlew
gradle.options ?= --console plain
gradle.command = $(gradle.gradle) $(gradle.options)

####################################################################################################

.PHONY : gradle-options(%)

gradle.options(%) :
	$(eval gradle.options += $(*))

####################################################################################################

.PHONY : gradle(%)

gradle(%) :
	$(gradle.command) $(*)

####################################################################################################

.PHONY : test

test : gradle( check )

####################################################################################################

.PHONY : format

format : gradle( spotlessApply )

####################################################################################################

.PHONY : compile

compile : gradle( classes )
