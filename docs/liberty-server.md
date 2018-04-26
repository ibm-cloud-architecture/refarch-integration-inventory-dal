# The Liberty server configuration
The goal of this article is to give you some details on our server configuration. The source definition for `server.xml` can be found under `src/main/liberty/config` folder.

## Download and install
* Download the Liberty server runtime from [WAS dev](https://developer.ibm.com/wasdev/downloads/download-latest-stable-websphere-liberty-runtime/) or from Open liberty the new open source version of it [here](https://www.openliberty.io/downloads/)
*The home directory is $WLP_HOME*

* Install additional features like the Admin Center [optional] using the command:
```bash
$ ${WLP_HOME}/bin/installUtility install adminCenter-1.0
```

* Create a server named appServer
```bash
$ ${WLP_HOME}/bin/server create appServer
```
* Modify the server.xml or copy the one from this project `src/main/liberty/config` folder.
The followings were done in the server.xml
### Datasource configuration

To expose SOAP interface and use JPA as a data access layer we need to add the following features in the server.xml
```xml
<feature>jpa-2.0</feature>
<feature>jaxws-2.2</feature>
```

The following commands were done:
```bash
$ cd wlp/bin
$ ./installUtility install jaxws-2.2
$ ./installUtility install jpa-2.0
```

To avoid changing configuration and parameters we set the HTTP and HTTPS ports as:
```xml
<httpEndpoint httpPort="9080" httpsPort="9443"
    id="defaultHttpEndpoint" />
```

As the project is using a single DB2 database instance the data source is defined to use db2 jdbc driver and to control the connection pool size so failover and testing can be done on capacity.
```xml
<library id="DB2JCC4Lib">
   <fileset dir="${shared.config.dir}/lib" includes="db2jcc4.jar"/>
</library>
<dataSource id="db2" jndiName="jdbc/invdbds" type="javax.sql.ConnectionPoolDataSource">
     <connectionManager maxPoolSize="8" minPoolSize="2"
                       connectionTimeout="5s" agedTimeout="10m"/>
     <jdbcDriver libraryRef="DB2JCC4Lib"/>
     <properties.db2.jcc databaseName="INVDB" portNumber="50000" serverName="172.16.254.23"
           currentLockTimeout="30s" user="DB2INST1" password="xxxxx"/>
   </dataSource>
```
You will need to adapt the IP address, the db2 userid and password.

## Security
The firewall was disabled using the following:
```bash
# See the status of firewall:
$ systemctl status firewalld;
$ service firewalld stop;
$ systemctl disable firewalld;
```

## Development environment setup
As root user git was installed as well as JDK 1.8. (The JDK was extracted under home/admin/IBM)
```bash
$ yum install git;
$ yum list installed java\*;
```

The admin account was modified by adding the following exports into .bashrc file
```bash
$ export JAVA_HOME=~/IBM/jdk1.8.0_131;
$ export PATH=$JAVA_HOME/bin:$PATH;
```

The first time you setup the server, you need to run the following commands to update the `server.xml` and add the DB2 JDBC driver jar to the liberty shared config folder.

```bash
$ cp ./src/main/liberty/config/server.xml ~/IBM/wlp/usr/servers/appServer;
$ cp ./lib/db2jcc4.jar ~/IBM/wlp/usr/shared/config/lib/db2jcc4.jar;
```

## Server start and test
Under the ~/IBM/wlp/bin do ``./server start appServer --clean ` then locally point to the URL http://localhost:9080/inventory/ws?wsdl to access the web app SOAP operation definition.