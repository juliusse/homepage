SBT=./sbt.make
SBT_ARGS=-Dsbt.log.noformat=true

INSTALL_DIR=$(DESTDIR)/usr/share/js-homepage/frontend

VERSION_FILE=src/main/resources/app.version
VERSION_STRING=application.version=

VERSION=$(shell grep "$(VERSION_STRING)" $(VERSION_FILE) | sed -e "s/$(VERSION_STRING)\"\(.*\)\"/\1/")

version:
	@echo $(VERSION)

check_version:
	@test ! ".$(VERSION)" = "." || exit 1

build:
	chmod +x $(SBT)
	$(SBT) $(SBT_ARGS) clean jacoco:cover dist
	
install: check_version
	mkdir -p $(INSTALL_DIR)
	cd target/universal/ && unzip -o js-homepage-frontend-$(VERSION).zip && \
		mv js-homepage-frontend-$(VERSION)/* $(INSTALL_DIR)/

clean:
	#clean is called by dpkg-buildpackage with fakeroot, does not work

.PHONY: build install clean version
