#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
./gradlew build
docker build -f Dockerfile-run -t ibmcase/browncompute-inventory-dal .

read -p 'Push to dockerhub?: (yes)/no' rep
if [ -z "$rep" ]
then
      echo "Pushing..."
      docker login
      docker push ibmcase/browncompute-inventory-dal:latest
fi
