set -x

#refresh export dirs
rm -rR jenkins/builds
mkdir -p jenkins/builds

export xsbt="java -Xmx1024M -Xss10M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=1024M  -jar sbtwrapper/sbt-launch.jar -Dsbt.log.noformat=true"
$xsbt clean test dist

# move file to builds folder
mv target/universal/js_homepage-*.zip jenkins/builds
