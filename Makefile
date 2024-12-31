# Copyright 2023 Bahman Movaqar
#
# This file is part of bjForth.
#
# bjForth is free software: you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# bjForth is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License
# along with bjForth. If not, see <https://www.gnu.org/licenses/>.
####################################################################################################

SHELL := /usr/bin/env bash
.DEFAULT_GOAL := test

####################################################################################################

export ROOT := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
export root.docs = $(ROOT)docs/
export root.build = $(ROOT)bjforth/build/libs/
export root.forth = $(ROOT)bjforth/src/main/forth/

####################################################################################################

.PHONY : bmakelib/bmakelib.mk
include  bmakelib/bmakelib.mk

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

.PHONY : e2e-test

e2e-test : bmakelib.error-if-blank( VERSION )
e2e-test : package
	@bjforth/src/test/e2e/e2e-test-runner.pl \
	  $(root.build)/bjForth-$(VERSION).jar \
	  bjforth/src/test/e2e/e2e-expected-output.txt \
	  bjforth/src/test/e2e/e2e-tests.forth \
	  bjforth/src/main/forth/bjForth.forth

####################################################################################################

.PHONY : format

format : gradle( spotlessApply )

####################################################################################################

.PHONY : compile

compile : gradle( classes )

####################################################################################################

.PHONY : clean

clean : gradle( clean )
clean:
	-@rm -rf build bjforth/build

####################################################################################################

.PHONY : package

package : bmakelib.error-if-blank( VERSION )
package : gradle( shadowJar )
package : package-path := $(ROOT)bjforth/build/libs/bjForth-$(VERSION).tar.gz
package :
	cp $(root.forth)*.forth $(root.build)
	mkdir -p $(root.build)docs
	cp $(root.docs)*.md $(root.build)docs
	cd $(root.build) && tar -cf bjForth-$(VERSION).tar *

####################################################################################################

.PHONY : docs

docs :
	$(ROOT)docs/extract.pl $(ROOT)bjforth/src/main/forth/bjForth.forth $(ROOT)docs/bjForth.forth.md

####################################################################################################

.PHONY : run

run : bmakelib.error-if-blank( VERSION )
run : gradle( shadowJar )
	@cat $(root.forth)bjForth.forth - \
	| java \
	  --add-opens=java.base/java.lang=ALL-UNNAMED \
	  --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
	  --add-opens=java.base/java.io=ALL-UNNAMED \
	  --add-opens=java.base/java.util=ALL-UNNAMED \
	  --add-opens=java.base/java.util.stream=ALL-UNNAMED \
	  -jar $(root.build)bjForth-$(VERSION).jar
  
