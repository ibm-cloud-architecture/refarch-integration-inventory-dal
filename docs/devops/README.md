# Data Access Layer - Continuous Integration with Jenkins
Before reading this document, make sure you setup a CICD Jenkins server in ICP by following the steps [here](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/devops/README.md#jenkins-on-ibm-cloud-private-icp).

## Table of Contents
* [Jenkinsfile Explained](#jenkinsfile-explained)
* [Setup the Pipeline](#setup-the-pipeline)
* [Run the Pipeline](#run-the-pipeline)

## Jenkinsfile Explained
We recommend that a CICD pipeline is defined and stored in source control. That way, you can keep track of CICD pipelines changes as well. In our case, we are going to use a [`Jenkinsfile`](https://jenkins.io/doc/book/pipeline/jenkinsfile/), which does just that.

More specifically, our `Jenkinsfile` (which you can access [here](../../Jenkinsfile)) leverages the pipeline templates from the Jenkins [Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin), which lets you define custom containers to run each pipeline stage. These containers are used to create a custom Kubernetes pod, which becomes the Jenkins agent that runs the pipeline. For more details, checkout the [Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin) page.

Our [Jenkinsfile](Jenkinsfile) is broken down as follows:
* **Define two2containers**
    + [`docker`](../../Jenkinsfile#L14)
    + [`kubectl`](../../Jenkinsfile#L15)
* **3 pipeline stages**
    + The [`1st stage`](../../Jenkinsfile#L21) uses the `docker` container to build the [docker image](Dockerfile). The included [Multi-staged Dockerfile](https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds) itself contains 2 stages:
        - The [`first stage`](../../Dockerfile#L1) performs a `gradle build`, which produces the `jar` and `war` files.
        - The [`second stage`](../../Dockerfile#L12) creates the `WebSphere Liberty profile` server and puts the `jar` and `war` from the previous files in the WPL server directories.
    + The [`2nd stage`](../../Jenkinsfile#L30) uses the `docker` container to push the docker image to the ICP Docker Registry.
    + The [`3rd stage`](../../Jenkinsfile#L47) uses the `kubectl` container to check for an existing Kubernetes Deployment and, if any exists, update the docker image.

It is important to note that the Jenkinsfile declares in the first stage [`a config map`](../../Dockerfile#L11) for the docker registry URL and [`a secret`](../../Dockerfile#L10) for the admin user to authenticate to the docker registry. The folder names (registry-account and registry-secret) are declared in the yaml files ([configmap.yaml](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/devops/registry/configmap.yaml) and [secret.yaml](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/devops/registry/secret.yaml) in brown main repository.

## Setup the Pipeline
Recall that the Jenkins URL is defined in the NodePort of the Jenkins master deployment configuration:
```
$  kubectl get services -l component=jenkins-jenkins-master  -n browncompute
NAME            TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)          AGE
jenkins         NodePort    10.10.10.4    <none>        8080:32277/TCP   15d
```

See the generic instructions [to understand how to set a pipeline](https://github.com/ibm-cloud-architecture/refarch-integration/tree/master/docs/devops/README.md#setup-the-pipeline) and for the pipeline configuration, use the following git repository details:  

+ **Repository URL:** `https://github.com/ibm-cloud-architecture/refarch-integration-inventory-dal.git`
+ **Branch:** `master`
+ **Script Path:** `Jenkinsfile`

Which can be summarized in the figure below:
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/3_setup_pipeline.png)

You should now have a fully setup pipeline.

## Run the pipeline
Use the 'build now' link.
