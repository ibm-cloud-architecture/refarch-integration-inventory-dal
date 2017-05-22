# Inventory Data Access Layer
This project is part of the 'IBM Integration Reference Architecture' suite, available at [https://github.com/ibm-cloud-architecture/refarch-integration](https://github.com/ibm-cloud-architecture/refarch-integration)
## Goals
The goal of this project is to define a SOAP interface for the Inventory datasource and implement the data access object as JPA entities The operations are visibles in the wsdl saved [here](docs/ws.wsdl). This wsdl is used for documentation purpose but it can also be imported in API Connect or IBM Integration Bus for interface mapping.

## Technology
The application is packaged as a war file to be deployed to a lightweight server like [IBM WebSphere Liberty profile](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime).
The code uses JPA 2.0 and JAXWS 2.2 APIs.

The server configuration defines the features needed and the datasource configuration. See the explanation [here](docs/liberty-server.md) about Liberty server.xml configuration.

## Code Explanation
The source code is organized to follow maven and gradle conventions: src/main/java or src/test/java...

The SOAP service is defined in the class inventory.ws.InventoryService and uses JAXWS annotations to define the service and the operations
```
@WebService
public class InventoryService {
  ...

  @WebMethod(operationName="items")
	public Collection<Item> getItems() throws DALException {
```
The Item class is a Data Transfer Object [Pattern explanation](https://martinfowler.com/eaaCatalog/dataTransferObject.html) to deliver a simple view of the item entity persisted in the database.

For information about the DB2 Inventory schema refer to the [Database guthub repository](https://github.com/ibm-cloud-architecture/refarch-integration-inventory-db2)

The Item from the data base table ITEMS was mapped to the inventory.model.ItemEntity class which uses a set of JPA annotations to map to the DB2 tables.

```
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

```

The unit tests are using an embedded derby to validate the service and data access object layer without dependencies to external DB. In production the data base is DB2.
Therefore two persistence.xml are defined one for testing ( src/test/resources) and one for production to be packaged into the war (src/java/resources).


## Preparing the project
The project was developed with [Eclipse Neon](http://www.eclipse.org/neon) with the following plugins added to the base eclipse:
* Websphere Developer Tool for Liberty: using the Marketplace and searching Websphere developer
* Gradle eclipse plugin

Install gradle CLI on your computer so you can build, unit test and assemble war.  For that see the installation instruction at [gradle](http://gradle.org)

## Preparing your App server
Install the WebSphere Liberty profile by downloading it from [WAS dev](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime). See configuration explanation [details](docs/liberty-server.md)

## Build
The build is supported by gradle. The project folder has a gradle wrapper so running
> ./gradlew build
should compile, unit tests and build a war under build/libs
Here is an example of execution trace
```
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
## Test Driven development
The service and data access object classes were developed by starting by the tests. The first test to validate the access to data and the CRUD operation at the service API level.


## Deploy
The script ./deployToWlp.sh copy the created war to a local wlp, modify the path in this script to reflect your local environment if you do not have your WebSphere Liberty profile under ~/IBM/wlp.
The server name was appServer.

## Access deployed wsdl
Using a web broswer to the localhost should display the wsdl: [http://localhost:9080/inventory/ws?wsdl](http://localhost:9080/inventory/ws?wsdl)
