set -e
set -x

#export display to run firefox headless
#export DISPLAY=:99

# remove files form last build
rm -rf jenkins/builds/*
rm -rf jenkins/jacoco/*
mkdir -p jenkins/builds
mkdir -p jenkins/jacoco

# build
dpkg-buildpackage -uc -us -b

#move debian package
mv ../js-homhepage_*.deb jenkins/builds/


# move frontend files
mv base_frontend/target/universal/js-homepage-frontend-*.zip jenkins/builds/
mv base_frontend/target/scala-2.10/jacoco/jacoco.exec jenkins/jacoco/frontend.exec

# remove MVC-classes that should be ignored
rm -rf base_frontend/target/*/classes/system
rm -rf base_frontend/target/*/classes/Routes*.class
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/views
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/javascript
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/ref
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/routes*
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/Reverse*.class
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/system/javascript
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/system/ref
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/system/routes*
rm -rf base_frontend/target/*/classes/com/rohdeschwarz/sit/base/frontend/controllers/system/Reverse*.class