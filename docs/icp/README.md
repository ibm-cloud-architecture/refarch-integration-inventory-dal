# Deploy the Data Access Layer to IBM Cloud Private


## Table of Contents
* [Topology](#topology)
* [Access WSDL](#access-wsdl)
* [Delete the Helm Chart](#delete-the-helm-chart)
* [Upgrade Docker Image](#upgrade-docker-image)
  + [1. Build Docker Image](#1-build-docker-image)
  + [2. Push the image to ICP's Docker Registry](#2-push-the-image-to-icps-docker-registry)
  + [3. Pass the Docker image to `helm install`](#3-pass-the-docker-image-to-helm-install)
* [Testing the access to the application](#testing-the-access-to-the-application)

## Topology
The ICP topology looks like the image below:
![](dal-on-icp.png)

The data access layer is accessing the DB2 running on premise using the JDBC protocol, and is exposed to the external world via Ingress rules so a SOAP request to the URL http://dal.brown.case/inventory/ws will return item(s) from the database.

## Setup Helm
The chart consists of the following files:
* [Chart.yaml](../../chart/browncompute-inventory-dal/Chart.yaml) - Contains information about the chart.
* [values.yaml](../../chart/browncompute-inventory-dal/values.yaml) - Default configuration for the chart.
* [templates](../../chart/browncompute-inventory-dal/templates) - Contains all of the templates for Kubernetes YAML files.
  + [_helpers.tpl](../../chart/browncompute-inventory-dal/templates/_helpers.tpl) - Contains helper functions for the templates.
  + [deployment.yaml](../../chart/browncompute-inventory-dal/templates/deployment.yaml) - Contains the Kubernetes [Deployment](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) template.
  + [ingress.yaml](../../chart/browncompute-inventory-dal/templates/ingress.yaml) - Contains the Kubernetes [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/#what-is-ingress) template.
  + [NOTES.txt](../../chart/browncompute-inventory-dal/templates/NOTES.txt) - A plain text file containing short usage notes.
  + [service.yaml](../../chart/browncompute-inventory-dal/templates/service.yaml) - Contains the Kubernetes [Service](https://kubernetes.io/docs/concepts/services-networking/service/) template.

## Access WSDL
The `helm install browncompute-inventory-dal --name browncompute-dal --namespace browncompute` command, output includes instructions for accessing the WSDL through a browser, which look as follows:
```bash
export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services dal-browncompute-inventory-dal);
export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}");
echo http://$NODE_IP:$NODE_PORT;
```

Copy and run the produced snippet in your terminal, open a new browser window, and paste the URL to access the WSDL.

If you cleared the `helm install` output by accident, you can get it back with the following command:
```bash
$ helm status browncompute-dal --tls
```

## Delete the Helm Chart
To delete the helm chart, use the following command:
```bash
$ helm delete browncompute-dal --purge --tls
```

The `--purge` flag makes sure that the `browncompute-dal` release name is reusable for a fresh install if you decide to use the same release name again.

## Upgrade Docker Image
If you would like to install the chart using your own Docker image, you need to do the following:
1. Build your own docker using the provided `Dockerfile`.
2. Push the image to ICP's Docker registry.
3. Pass the Docker image located in ICP as a parameter to the `helm install` command.

### 1. Build Docker Image
To build the docker image, run the following command
```bash
$ docker build -t ibmcase/browncompute-inventory-dal:latest .
# and tag for the private repository running on ICP master node
$ docker tag  ibmcase/browncompute-inventory-dal:latest super-cluster.icp:8500/ibmcase/browncompute-inventory-dal:latest
```

Where:
* `super-cluster.icp` is the ICP cluster name, which you can change upon installing ICP:
  + Make sure than an entry for `super-cluster.icp` (or whatever name you chose) exists in your local `/etc/hosts` file. i.e.
    ```bash
    127.0.0.1       localhost
    255.255.255.255 broadcasthost
    ::1             localhost
    172.16.40.xxx        super-cluster.icp icp
    ```
* `8500` is the port number for ICP's Docker Registry.
* `ibmcase` is the Docker Registry Namespace where the Docker image will be located, which you can change.
* `browncompute-inventory-dal` is the actual Docker image name, which you can change.
* `latest` is the Docker image tag.

### 2. Push the image to ICP's Docker Registry
To push the image to ICP's Docker Registry, run the following command:
```bash
# Login to ICP Docker Registry
$ docker login super-cluster.icp:8500

# Push Image
$ docker push super-cluster.icp:8500/ibmcase/browncompute-inventory-dal:latest
```

You can see the image in the `Images` section under `Catalog` in the ICP Dashboard.

We are prefer now push the image to dockerhub and reference the image in the helm chart. As our code is public using public dockerhub repository is perfectly fine.
Here are the steps:
```
$ docker login
$ docker push ibmcase/browncompute-inventory-dal
```

### 3. Pass the Docker image to `helm install`
To use the new docker image in a fresh `helm install`, just pass it as an argument as follows:
```bash
$ helm install browncompute-inventory-dal --name browncompute-dal \
  --set image.repository=super-cluster.icp:8500/ibmcase/browncompute-inventory-dal \
  --set image.tag=latest \
  --tls;
```
As an alternate using docker hub image:
```bash
$ helm install browncompute-inventory-dal --name browncompute-dal \
  --set image.repository=ibmcase/browncompute-inventory-dal \
  --set image.tag=latest \
  --tls;
```

If you want to upgrade an existing helm release with a new image version deployed on docker hub, use the following command:
```bash
$ helm upgrade <HELM_RELEASE_NAME> \
  --set image.repository=ibmcase/browncompute-inventory-dal \
  --set image.tag=latest \
  --tls;
```

Where:
* `<HELM_RELEASE_NAME>` is the helm release name, which is `browncompute-dal` in this case.

If you want to upgrade the deployment image directly without using `helm`, you can do so as follows:
```bash
# Set the new image
$ kubectl set image <HELM_RELEASE_NAME>-browncompute-inventory-dal \
  browncompute-inventory-dal=ibmcase/browncompute-inventory-dal:latest;

# Watch the upgrade status
$ kubectl rollout status <HELM_RELEASE_NAME>-browncompute-inventory-dal;
```

The advantage of using this approach is that this command preserves a history of the deployment image updates. This is useful for image rollbacks in case the new images contain breaking changes.

## Troubleshooting
If you have issue in deployment review our centralized [Troubleshooting note](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/icp/troubleshooting.md)

## Testing the access to the application
In order to access the exposed WSDL via curl, run the command below:
```bash
$ curl -k http://<ICP_IP_ADDRESS>:<DAL_NODE_PORT>/inventory/ws?wsdl
```

Where:
* `<ICP_IP_ADDRESS>` is the IP address of your ICP cluster's Proxy Node.
  + You can obtain this by following the steps [here](#access-wsdl).
* `<DAL_NODE_PORT>` is the `NodePort` that the `helm install` command created for you.
  + You can obtain this by following the steps [here](#access-wsdl).

Now lest test a simple database retrieval using a SOAP envelope, which we provided in [`src/test/scripts/item13408.xml`](src/test/scripts/item13408.xml) to retrieve item `13408`.

```bash
$ curl -v -k --header "Content-Type: text/xml;charset=UTF-8" --data @src/test/scripts/item13408.xml http://<ICP_IP_ADDRESS>:<DAL_NODE_PORT>/inventory/ws
```

If successful, you should get an XML output similar to the one below (which has been formatted for readability):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <a:itemByIdResponse xmlns:a="http://ws.inventory/">
         <return>
            <id>13408</id>
            <name>Calculator</name>
            <description>The IBM 608 calculator (shown at left) was the first completely transistorized computer available for commercial installation. Announced in April 1955, the 608 began the transition of IBM s line of small and intermediate electronic calculators from vacuum tube to transistor operation. It contained more than 3,000 transistors -- tiny germanium devices no bigger than a paper clip -- and magnetic cores -- doughnut-shaped objects slightly larger than a pinhead, in the first known use of transistors and cores together in a computer. The magnetic cores could remember information indefinitely and recall it in a few millionths of a second, and made up the machine s internal storage or memory.&lt;br&gt;The 608 s transistors made possible a 50 percent reduction in physical size and a 90 percent reduction in power requirements over comparable vacuum tube models. The machine could perform 4,500 additions a second, a computing speed 2.5 times faster than IBM s Type 607 calculator introduced only two years before. It could multiply two 9-digit numbers and derive the 18-digit product in 11 one-thousandths of a second, and divide an 18-digit number by a nine-digit number to produce the nine-digit quotient in just 13 one-thousandths of a second. The associated IBM 535 card read punch (shown at right) was used for both input and output, and was designed to permit a card to be calculated and the results punched while passing through the machine at the rate of 155 cards per minute.&lt;br&gt;In 1957, customers could purchase the 608 for $83,210 (or rent it for $1,760 a month) and the 535 for $44,838 (or rent it for $715 a month). The 608 was withdrawn from marketing in April 1959.</description>
            <price>5199.99</price>
            <imgAlt>608 Calculator</imgAlt>
            <img>/api/image/608-calculator.jpg</img>
            <quantity>1</quantity>
         </return>
      </a:itemByIdResponse>
   </soap:Body>
</soap:Envelope>
```
