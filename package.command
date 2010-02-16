:
PDIR=`dirname $0`
cd "${PDIR}"
ant -f antJarCreation.xml 
cp -v target/cnWebServer.jar /usr/cmtc/app/jars/
cp -v jsp/*.jsp /usr/cmtc/app/jsp/
cp -rv www/* /usr/cmtc/app/www/
echo ================ done packaging rdms_gui ======================
