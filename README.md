---
---
# Goals
The goal of the project is to define SOAP interface for the inventory and implement the data access object as JPA for the inventory DB.

# Technology
The application is packaged as a war file to be deployed to a lightweight server like IBM WebSphere Liberty profile.
The code uses JPA and JAXWS api.

# Preparing the project
The project was developed with [Eclipse Neon](http://www.eclipse.org/neon) with the following plugins added to the base eclipse:
* Websphere Developer Tool for Liberty: using the Marketplace and searching Websphere developer
* Gradle eclipse plugin

Install gradle CLI on your computer see installation (http://gradle.org)

# Build
The build is supported by gradle. The project folder has a wrapper so running
> ./gradlew build
should compile and build a war.