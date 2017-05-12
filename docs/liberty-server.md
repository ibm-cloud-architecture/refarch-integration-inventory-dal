--
--
# The Liberty server configuration
The goal of this article is to give you some details on the server configuration. The source definition for server.xml can be found under src/main/liberty/config folder.

## Download and install
Download the server library from [WAS dev](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime/)

Install additional features like the Admin Center [optional] using the command:
```
$WLP_HOME/bin/installUtility install adminCenter-1.0
```
Create a server named appServer
```
$WLP_HOME/bin/server create appServer
```
Modify the server.xml or copy the one from this project src/main/liberty/config folder

## Datasource configuration

To expose SOAP interface and use JPA as a data access layer we need to add the following features
```
<feature>jpa-2.0</feature>
<feature>jaxws-2.2</feature>
```

To avoid changing configuration and parameters we set static port definition, for the HTTP end point as
```
<httpEndpoint httpPort="9080" httpsPort="9443"
    id="defaultHttpEndpoint" />
```
As the project is using a single DB2 database instance the data source is defined to use db2 jdbc driver and to control the connection pool size so failover and testing can be done on capacity.
```
<library id="DB2JCC4Lib">
   <fileset dir="${shared.config.dir}/lib" includes="db2jcc4.jar"/>
</library>
<dataSource id="db2" jndiName="jdbc/invdbds" type="javax.sql.ConnectionPoolDataSource">
     <connectionManager maxPoolSize="8" minPoolSize="2"
                       connectionTimeout="5s" agedTimeout="10m"/>
     <jdbcDriver libraryRef="DB2JCC4Lib"/>
     <properties.db2.jcc databaseName="INVDB" portNumber="50000" serverName="172.16.254.23"
           currentLockTimeout="30s" user="DB2INST1" password="Brown01"/>
   </dataSource>
```
You will need to adapt the IP address and password.
