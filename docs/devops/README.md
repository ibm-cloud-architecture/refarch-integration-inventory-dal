# Data Access Layer - Continuous Integration with Jenkins
Before reading this document, make sure you setup a CICD Jenkins server in ICP by following the steps [here](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/devops/README.md#jenkins-on-ibm-cloud-private-icp).

## Table of Contents
* [Jenkinsfile Explained](#jenkinsfile-explained)
* [Setup the Pipeline](#setup-the-pipeline)
    + [1. Create a Sample Job](#1-create-a-sample-job)
    + [2. Select Pipeline Type](#2-select-pipeline-type)
    + [3. Setup Sample Pipeline](#3-setup-sample-pipeline)
* [Run the Pipeline](#run-the-pipeline)
    + [1. Launch Pipeline Build](#1-launch-pipeline-build)
    + [2. Open Pipeline Console Output](#2-open-pipeline-console-output)
    + [3. Monitor Console Output](#3-monitor-console-output)

## Jenkinsfile Explained
We recommend that a CICD pipeline is defined and stored in source control. That way, you can keep track of CICD pipelines changes as well. In our case, we are going to use a [`Jenkinsfile`](https://jenkins.io/doc/book/pipeline/jenkinsfile/), which does just that.

More specifically, our `Jenkinsfile` (which you can access [here](Jenkinsfile)) leverages the pipeline templates from the Jenkins [Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin), which lets you define custom containers to run each pipeline stage. These containers are use to create a custom Kubernetes pod, which becomes the Jenkins agent that runs the pipeline. For more details, checkout the [Kubernetes Plugin](https://github.com/jenkinsci/kubernetes-plugin) page.

Our [Jenkinsfile](Jenkinsfile) is broken down as follows:
* **2 containers**
    + [`docker`](Jenkinsfile#L14)
    + [`kubectl`](Jenkinsfile#L15)
* **3 pipeline stages**
    + The [`1st stage`](Jenkinsfile#L21) uses the `docker` container to build the [docker image](Dockerfile). The included [Multi-staged Dockerfile](https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds) itself contains 2 stages:
        - The [`first stage`](Dockerfile#L1) performs a `gradle build`, which produces the `jar` and `war` files.
        - The [`second stage`](Dockerfile#L12) creates the `WebSphere Liberty profile` server and puts the `jar` and `war` from the previous files in the WPL server directories.
    + The [`2nd stage`](Jenkinsfile#L30) uses the `docker` container to push the docker image to the ICP Docker Registry.
    + The [`3rd stage`](Jenkinsfile#L47) uses the `kubectl` container to check for an existing Kubernetes Deployment and, if any exists, update the docker image.

## Setup the Pipeline
To setup a pipeline, open Jenkins in the browser and follow the steps below:

### 1. Create a Sample Job
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/1_create_job.png)

### 2. Select Pipeline Type
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/2_select_pipeline_type.png)

### 3. Setup Sample Pipeline
Use the following git repository details:
+ **Repository URL:** `https://github.com/ibm-cloud-architecture/refarch-integration-inventory-dal.git`
+ **Branch:** `master`
+ **Script Path:** `Jenkinsfile`
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/3_setup_pipeline.png)

You should now have a fully setup pipeline.

## Run the Pipeline
To run the pipeline, open Jenkins in the browser and follow the steps below:

### 1. Launch Pipeline Build
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/4_launch_build.png)

### 2. Open Pipeline Console Output
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/5_open_console_output.png)

### 3. Monitor Console Output
![Create a Sample Job](https://raw.githubusercontent.com/ibm-cloud-architecture/refarch-cloudnative-devops-kubernetes/master/static/imgs/6_see_console_output.png)

If the pipeline finishes successfully, then Congratulations! You have successfully setup a fully working CICD pipeline.
