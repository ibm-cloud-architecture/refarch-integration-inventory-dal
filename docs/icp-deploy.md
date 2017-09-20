# Deploy the Data Access Layer to IBM Cloud Private

We propose to package the code as a docker image, build a helm chart and then publish it to an ICP instance.
For explanation about why the commands below are performed, read the detail on [deploying Case inc Web app on ICP](https://github.com/ibm-cloud-architecture/refarch-caseinc-app/blob/master/docs/run-icp.md)

## Pre-requisites
If you did not configure your ICP environment with SSH certificates, ... please read [this note](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/icp-deploy.md#common-installation-tasks)

## Build
This project includes a docker file to build a docker image.

You can build the image to your local repository using the commands:
```
# first compile and build the App war file
$ gradlew build
# build the image
$ docker build -t case/dal .
$ docker images
#... should see case/dal image
```
Then tag your local image with the name of the remote server where the docker registry resides, and the namespace to use. (*master.cfc:8500* is the remote server and *default* is the namespace)
```
$ docker tag case/dal master.cfc:8500/default/dal:v0.0.1
$ docker images
# you should get something like:
REPOSITORY                    TAG                 IMAGE ID            CREATED              SIZE
master.cfc:8500/default/dal   v0.0.1              906ad6fc851e        About a minute ago   474MB
case/dal                      latest              906ad6fc851e        About a minute ago   474MB
websphere-liberty             webProfile7         905fc63e8e9b        
```

## Push docker image to ICP private docker repository

```
docker login master.cfc:8500
User: admin
```
Push the image
```
docker push master.cfc:8500/default/dal:v0.0.1
```

## Build the helm package

The casedal chart was already created with a 'helm create casedal' command.
### Chart.yaml
Set the version and name it will be use in deployment.yaml. Each time you deploy a new version of your app you can just change the version number. The values in the char.yaml are used in the templates.

### values.yaml
Specify in this file where to get the docker image:
```yaml
image:
  repository: master.cfc:8500/default/casedal
  tag: v0.0.1
  pullPolicy: IfNotPresent
```

Try to align the number of helm package with docker image tag.

## Build the application package with helm
```
$ cd chart
$ helm lint casedal
# if you do not have issue ...
$ helm package casedal
```

## Deploy the helm package

### Use helm commmand
* Use helm install command to install a chart archive directly to kubernetes cluster. Be sure to be connected to your cluster server.
```
$ helm install casedal-0.0.1.tgz
```
You should get a new name for the deployed application.
### Use helm upgrade
To update, rollout, a new version of the code, after packaging the docker images and uploaded it to ICP internal docker repository, use the **upgrade**

```
helm upgrade casedal
```

### Verify the app is deployed
```
helm ls --all default-casedal

# remove the app
helm del --purge default-casedal
```
