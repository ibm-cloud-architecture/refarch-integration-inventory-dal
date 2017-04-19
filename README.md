---
---
# Goals
The goal of the project is to define a SOAP interface for the Inventory datasource and implement the data access object as JPA entities.

# Technology
The application is packaged as a war file to be deployed to a lightweight server like IBM WebSphere Liberty profile.
The code uses JPA and JAXWS APIs.

# Code Explanation
The SOAP service is defined in the class inventory.ws.InventoryService and uses JAXWS annotations to define the service and the operations
```
@WebService
public class InventoryService {
  ...

  @WebMethod(operationName="items")
	public Collection<Item> getItems(){
```

The Item class is a Data Transfer Object to deliver a simple view of the item from the database.
 
# Preparing the project
The project was developed with [Eclipse Neon](http://www.eclipse.org/neon) with the following plugins added to the base eclipse:
* Websphere Developer Tool for Liberty: using the Marketplace and searching Websphere developer
* Gradle eclipse plugin

Install gradle CLI on your computer, for that see the installation instruction at [gradle](http://gradle.org)

# Build
The build is supported by gradle. The project folder has a wrapper so running
> ./gradlew build
should compile and build a war under build/libs

# Deploy
The script ./deployToWlp.sh copy the created war to a local wlp, modify the path to reflect your local environment
