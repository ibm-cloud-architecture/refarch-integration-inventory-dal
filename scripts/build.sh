#!/bin/bash
# Program to compile, build war, build the docker images
# modify the version number for the different element 
progname=dal
dalserv=src/main/java/inventory/ws/DALService.java
k8cluster=green2-cluster.icp
nspace=browncompute

# Get default version if the version is not the first argument
prev=$(grep -o 'v\([0-9]\+.\)\{2\}\([0-9]\+\)' $dalserv)

if [[ $# -gt 0 ]]; then
	v=v$1
else
  v=$prev
fi
echo $v

# Update version in java code
sed -i -e s/$prev/$v/ $dalserv


# Compile Java Code
./gradlew build

# Build docker
# docker build -t ibmcase/$progname .
docker tag ibmcase/$progname $k8cluster:8500/$nspace/$progname:$v

docker images
exp  admin docker login -u admin $k8cluster:8500
docker push $k8cluster:8500/$nspace/$progname:$v

## modify helm version
cd ./chart/browncompute-dal
a=$(grep 'version' Chart.yaml)
sed -i -e 's/"$a"/version: "$v"/' Chart.yaml
## same for the tag in values.yaml
sed -i -e 's/tag: "$prev"/tag: "$v"/' values.yaml
sed -i -e 's/green2-cluster.icp/$k8cluster/' values.yaml
cd ..
#helm install --name bc-$progname browncompute-dal --namespace=$nspace
