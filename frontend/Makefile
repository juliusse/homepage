SBT=./sbt.make
SBT_ARGS=-Dsbt.log.noformat=true

INSTALL_DIR=$(DESTDIR)/usr/share/js-homepage/frontend

VERSION_FILE=conf/app.version
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
	mkdir -p $(INSTALL_DIR)/js-homepage/frontend
	cd target/universal/ && unzip -o js-homepage-frontend-$(VERSION).zip && \
		mv js-homepage-frontend-$(VERSION)/lib $(INSTALL_DIR)/frontend/ && \
		mkdir -p $(INSTALL_DIR)/frontend/conf && \
		cp js-homepage-frontend-$(VERSION)/conf/logger.xml $(INSTALL_DIR)/frontend/conf

clean:
	#clean is called by dpkg-buildpackage with fakeroot, does not work

.PHONY: build install clean version
