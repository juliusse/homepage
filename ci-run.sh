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
mv frontend/target/scala-*/jacoco/jacoco.exec jenkins/jacoco/frontend.exec
