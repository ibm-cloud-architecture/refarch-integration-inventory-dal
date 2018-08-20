#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
gradlew build
docker build -f Dockerfile-run -t ibmcase/browncompute-inventory-dal .