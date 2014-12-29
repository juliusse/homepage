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
mv ../js-homepage_*.deb jenkins/builds/

# move frontend files
mv frontend/target/universal/js-homepage-frontend-*.zip jenkins/builds/
mv frontend/target/scala-2.10/jacoco/jacoco.exec jenkins/jacoco/frontend.exec

# remove MVC-classes that should be ignored
rm -rf frontend/target/*/classes/system
rm -rf frontend/target/*/classes/Routes*.class
rm -rf frontend/target/*/classes/info/seltenheim/homepage/views
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/javascript
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/ref
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/routes*
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/Reverse*.class
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/system/javascript
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/system/ref
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/system/routes*
rm -rf frontend/target/*/classes/info/seltenheim/homepage/controllers/system/Reverse*.class