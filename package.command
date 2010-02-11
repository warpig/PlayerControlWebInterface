:
PDIR=`dirname $0`
cd "${PDIR}"
find . -name \.DS_Store -exec rm {} \;
mvn package
echo ================ done packaging rdms_gui ======================
echo Press return to close...
read NOTHING
