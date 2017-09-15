# Deploy the Data Access Layer to IBM Cloud Private

We propose to package the code as a docker image, build a helm chart and then publish it to an ICP instance.
For detail on why commands below are performed, read the detail on [deploying Case inc Web app on ICP](https://github.com/ibm-cloud-architecture/refarch-caseinc-app/blob/master/docs/run-icp.md)

## Build
This project includes a docker file to build a docker image.

You can build the image to your local repository using the command:
```
# first build the App
$ gradlew build
$ docker build -t case/dal .
$ docker images
```
Then tag your local image with the name of the remote server where the docker registry resides, and the namespace to use. (*master.cfc:8500* is the remote server and *default* is the namespace)
```
$ docker tag case/dal master.cfc:8500/default/dal:v0.0.1
$ docker images
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

The casedal chart was created.
### Chart.yaml
Set the version and name it will be use in deployment.yaml. Each time you deploy a new version of your app you can just change the version number. The values in the char.yaml are used in the templates.

### values.yaml
Specify in this file the docker image name and tag
```yaml
image:
  repository: mycluster:8500/default/casedal
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
* Use helm install command to install a chart archive directly to kubernetes cluster
```
$ helm install casedal-0.0.1.tgz
```

### Use helm upgrade
```
helm upgrade casedal
```

### Verify the app is deployed
```
helm ls --all default-casedal

# remove the app
helm del --purge default-casedal
```
