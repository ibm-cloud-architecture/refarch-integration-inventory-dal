# Inventory Data Access Layer
This project is part of the 'IBM Hybrid Integration Reference Architecture' solution, available at https://github.com/ibm-cloud-architecture/refarch-integration.

The goal of this project is to implement a set of SOA service operations to manage a product inventory, suppliers and stock. This is on purpose, that we centralize those three components inside the same application to represent an older application design done in the 2000s.
In 2017, most likely, we will have separated those three entities into three microservices.

Updated 04/27/2018.

## Table of Contents
* [Goals](#goals)
* [Technology](#technology)
* [Pre-Requisites](#pre-requisites)
* [Code Explanation](#code-explanation)
* [Build & Deployment Options](#build--deployment-options)
  + [Option 1: Local WebSphere Liberty Profile](#option-1-local-websphere-liberty-profile)
    - [Install & Configure Eclipse](#install--configure-eclipse)
    - [Install Java JDK](#install-java-jdk)
    - [Install WebSphere Liberty profile](#install-websphere-liberty-profile)
    - [Gradle Build](#gradle-build)
    - [Deploy to Local IBM WebSphere Liberty Profile](#deploy-to-local-ibm-websphere-liberty-profile)
    - [Access the WSDL](#access-the-wsdl)
  + [Option 2: Docker](#option-2-docker)
  + [Option 3: IBM Cloud Private](#option-3-ibm-cloud-private)
* [Continuous Integration](#continuous-integration)
* [Test Driven Development](#test-driven-development)
* [Conclusion](#conclusion)
* [Contribute](#contribute)

## Goals
The goal of this project is to define a SOAP interface for the Inventory datasource and implement the data access object as JPA entities. The operations are visible in the wsdl saved [here](docs/ws.wsdl). This wsdl is used for documentation purpose but it can also be imported in API Connect or IBM Integration Bus to define new API product or implement interface mapping flow. The WSDL can be visible by using a web browser to the following URL:
* when deploy on premise liberty server: http://172.16.254.44:9080/inventory/ws?WSDL
* when deploy on ICP, you need to have a name resolution for the dal.brown.case (map the ICP proxy IP address to this hostname in your /etc/hosts) then the URL is http://dal.brown.case/inventory/ws?WSDL

## Technology
The application is packaged as a war file to be deployed to a lightweight server like [IBM WebSphere Liberty profile](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime).
The code uses JPA 2.0 and JAXWS 2.2 APIs (See [JAXWS summary note](docs/jaxws.md)).

The server configuration defines the features needed and the data source configurations. To get understanding on our server configuration see the explanation [here](docs/liberty-server.md) but as we delivered dockerfile and server.xml for you there is nothing really to do.

The data model is simple as illustrated below:  

![](docs/datamodel.png)

* Item is the main product description and may include sub components.
* The Inventory specifies how many items are in a given site
* A Supplier delivers items and is-a Party.

## Pre-Requisites
You will need the following Command-line Interfaces (CLIs) to be able to build and deploy the app:
* [docker](https://docs.docker.com) (Docker Container Runtime & CLI) - Follow the instructions [here](https://docs.docker.com/install/) to install it on your platform.
* [kubectl](https://kubernetes.io/docs/user-guide/kubectl-overview/) (Kubernetes CLI) - Follow the instructions [here](https://kubernetes.io/docs/tasks/tools/install-kubectl/) to install it on your platform.
* [helm](https://github.com/kubernetes/helm) (Kubernetes package manager) - Follow the instructions [here](https://github.com/kubernetes/helm/blob/master/docs/install.md) to install it on your platform.
  + If using `IBM Cloud Private` version `2.1.0.2` or newer, we recommend you follow these [instructions](https://www.ibm.com/support/knowledgecenter/SSBS6K_2.1.0.2/app_center/create_helm_cli.html) to install `helm`.

## Code Explanation
The source code is organized to follow maven and gradle conventions: src/main/java or src/test/java...

The SOAP service is defined in the class inventory.ws.DALService and uses [JAXWS](docs/jaxws.md) annotations to define the service and the operations exposed as part of the unique application WSDL.
```
@WebService
public class DALService {
  ...

  @WebMethod(operationName="items")
	public Collection<Item> getItems() throws DALException {
```

The Item and Supplier classes are [Data Transfer Object Pattern](https://martinfowler.com/eaaCatalog/dataTransferObject.html) classes to deliver a simple view of the entities persisted in the database to do not expose all the attributes persisted in the DB.

For information about the DB2 Inventory schema refers to the [Database github repository](https://github.com/ibm-cloud-architecture/refarch-integration-inventory-db2)

The Item from the data base table ITEMS was mapped to the inventory.model.ItemEntity class which uses a set of JPA annotations to map to the DB2 tables.

```java
Entity(name="Item")
@Table(name="ITEMS")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class ItemEntity implements Serializable {
  ...
  @Id
  @GeneratedValue (strategy=GenerationType.SEQUENCE)
  @Column(nullable=false)
  protected long id;

  @Column(nullable=false, length=1500)
  private String description;
  @Column(nullable=false, length=100)
  protected String name;
  @Column(precision=8, scale=2)
  private double price;
  @Column(length=100)
  private String img;
  @Column(name="IMG_ALT", length=75)
  private String imgAlt;
  @Column(precision=5)
  private int quantity;
  private Timestamp updateDate;
  private Timestamp creationDate;
  ...
}
```

The same approach is done for the Inventory and the Supplier tables.

The JAXWS annotations are centralized in one class, which may not be the best approach. 
Here is an extract of this class showing the getItems implementation with the delegate to the providre class which supports the business logic implementation.

```Java
@WebService
public class DALService {
@WebMethod(operationName = "getItems")
	public Collection<Item> getItems() throws DALException{
		return processList(inventoryProvider.getItems());
	}
}
```
The processList function is here to do data transformation between entities and DTO.

The unit tests are using an embedded derby to validate the service and data access object layer without dependency to external DB. In production the data base is DB2.
Therefore two persistence.xml are defined: one for testing ( src/test/resources) and one for production to be packaged into the war (src/java/resources).

## Build & Deployment Options
To build & deploy the application, you can use 1 of the following 3 approaches:
1. Deploy to local `IBM WebSphere Liberty Profile` app server (least preferred).
2. Use the provided `Dockerfile` and deploy to local docker engine (easiest).
3. Use the provided `Helm Chart` and deploy to `IBM Cloud Private cluster`.

### Option 1: Local WebSphere Liberty Profile
#### Install & Configure Eclipse
The project was developed with [Eclipse Neon](http://www.eclipse.org/neon) with the following plug-ins added to the base eclipse:
* Websphere Developer Tool for Liberty: using the Eclipse Marketplace and search for WebSphere developer tool, then use the Eclipse way to install plugins.
* Gradle eclipse plug-in

Install gradle CLI on your computer so you can build, unit test and assemble war.  For that see the installation instructions at [gradle](http://gradle.org)

#### Install Java JDK
* Install Java JDK, preferably Oracle one. For example for a Ubuntu build server we did the following commands:
```bash
$ sudo update-ca-certificates -f
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer
$ sudo apt-get install oracle-java8-set-default
```

#### Install WebSphere Liberty profile
Install the WebSphere Liberty profile by downloading it from [WAS dev](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime). See our configuration explanations [here](docs/liberty-server.md).

#### Gradle Build
The build is supported by `gradle` & the project includes the [gradlew](gradlew) wrapper. To run the gradle build (which compiles the code, runs the [unit tests](src/test/java/dal/ut), and builds the war files under `build/libs`) run the following command:
```bash
$ ./gradlew build
```

If successful, you should see an execution trace similar to the following:
```bash
Starting a Gradle Daemon (subsequent builds will be faster)
:compileJava
:openjpaEnhance
36  inventory  INFO   [Daemon worker] openjpa.Tool - Enhancer running on type "class inventory.model.ItemEntity".
:processResources UP-TO-DATE
:classes
:war
:assemble
:compileTestJava
:processTestResources UP-TO-DATE
:testClasses
:test
```

#### Deploy to Local IBM WebSphere Liberty Profile
The script [scripts/deployToWlp.sh](scripts/deployToWlp.sh) copies the created war to your local WLP, modify the path in this script to reflect your local environment if you do not have your WebSphere Liberty profile under ~/IBM/wlp.
The server name was *appServer*.

#### Access the WSDL
To access the WSDL, open a web browser and enter the following link:
- http://localhost:9080/inventory/ws?wsdl

### Option 2: Docker
The included [Multi-staged Dockerfile](https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds) contains 2 stages:
1. The first stage performs a `gradle build`, which produces the `jar` and `war` files.
2. The second stage creates the `WebSphere Liberty profile` server and puts the `jar` and `war` from the previous files in the WPL server directories.

To build & run the [Dockerfile](Dockerfile) in your local Docker runtime, use the following commands:
```bash
# Build the container image
$ docker build -t ibmcase/browncompute-inventory-dal .

# Start the container
$ docker run -p 9080:9080 ibmcase/browncompute-inventory-dal
```

To access the WSDL, open a web browser and enter the following link:
- http://localhost:9080/inventory/ws?wsdl

### Option 3: IBM Cloud Private
To deploy to IBM Cloud Private [this note](./docs/icp/README.md) will go into details on how we did it on last ICP version.

## Continuous Integration
We recommend setting up a Continuous Integration Continuous Delivery (CICD) server to automate the build and deploy of new app updates. To setup a CICD Jenkins server, follow the steps [here](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/devops/README.md#jenkins-on-ibm-cloud-private-icp).

Once your Jenkins server is fully setup, follow the steps [here](./docs/devops/README.md) to setup a CICD pipeline for `browncompute-inventory-dal`.

## Test Driven Development
The service and data access object classes were developed by starting from the tests. Since Ken Bent wrote his book: "Test Driven Development by Example" in 2002, the practice is used in thousand of projects. At ILOG then IBM, we adopted this practice since 2003. So without redoing a how to do TDD, we just want to summarize some of the practices we used in this project.

The first tests were to validate the access to data and to validate CRUD operations happy path. The tests use the service Data Access Object to validate we can create, update, read and delete items.

Here is an example of tests:
```java
public class TestInventoryDB {

	static DALService serv;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 serv = new DALService();
	}
  ....
  @Test
	public void testLoadAllItems() {
		Collection<Item> items =null;
		try {
			items = serv.getItems();
		} catch (DALException e) {
			e.printStackTrace();
			fail("Exception");
		}
		Assert.assertNotNull(items);
		Assert.assertTrue(items.size() >= 1);
	}
  ...
}
```
The tests are using a Derby embedded database so it is easier to start and execute tests in isolation. The AfterClass method is deleting the locally created `INVDB` instance. See the `BaseTest` class.

When tests are executed by `gradlew` the reports are in the build/reports folder.

As the logic was separated into three classes: front end to support JaxWS annotation, service class to do service orchestration and to implement the business logic (Porvider classes) and DAO to support integration with data source, the unit tests should be at the DAO and service level.

In [another project]() we are presenting how to use Mockito to isolate back end component to test the business logic layer.
   
## Conclusion
The SOA service operations defined in this project are not exposed to Bluemix application or to microservice directly. When using ESB pattern, integration flows will be developed to address interface mapping, protocol mapping, or different quality of service configuration.

The project  [Inventory Flow - Integration Bus](https://github.com/ibm-cloud-architecture/refarch-integration-esb) provides the implementation of SOAP to REST interface mapping flow.

Also we define an `Inventory` API product in IBM API Connect. See project [API](https://github.com/ibm-cloud-architecture/refarch-integration-api).

## Contribute
We welcome contribution. Contribution is not only PRs and code, but any help with docs or helping other developers to solve issues are very appreciated! Thanks in advance!

To standardize on contribution guidance see [main brown compute repository](https://github.com/ibm-cloud-architecture/refarch-integration).
