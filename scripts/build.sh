#!/bin/bash -v
# Program to compile, build war, build the docker images,
# and modify the version number for the different element
if [[ "$PWD" = */scripts ]]; then
  cd ..
fi
. scripts/setenv.sh

# Get current version if the version is not the first argument of this command line
prev=$(grep -o 'v\([0-9]\+.\)\{2\}\([0-9]\+\)' $dalserv)

if [[ $# -gt 0 ]]; then
	v=v$1
else
  v=$prev
fi
echo 'old version: ' $prev ' new version to use:' $v

# Update version in java code
sed -i -e s/$prev/$v/ $dalserv
rm $dalserv-e

# Compile Java Code
./gradlew -Dorg.gradle.daemon=false build

# Build docker
docker build -t ibmcase/$progname .
docker tag ibmcase/$progname $k8cluster:8500/$namespace/$progname:$v

## modify helm version
cd ./chart/browncompute-dal
a=$(grep 'version' Chart.yaml)
sed -i -e "s/$a/version: ${v:1}/" Chart.yaml
## same for the tag in values.yaml
sed -i -e "s/tag: $prev/tag: $v/" values.yaml
sed -i -e "s/green2-cluster.icp/$k8cluster/" values.yaml
rm values.yaml-e
rm Chart.yaml-e
cd ..
