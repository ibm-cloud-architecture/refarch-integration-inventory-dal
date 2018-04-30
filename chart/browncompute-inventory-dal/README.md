# Inventory Data Access Layer Web Service
This helm chart creates 3 instances for the Inventory Data Access Layer JAXWS service.
## Docker image
The docker image include Liberty server profile with the configuration to access a remote DB2 instance running on premise.

The database used for persisting inventory Items should be up and running and configured (see the github repository: https://github.com/ibm-cloud-architecture/refarch-integration-inventory-db2  for more information)

## configuration

For more details about this project see the github repository: https://github.com/ibm-cloud-architecture/refarch-integration-inventory-dal.
## License
The license is Apache version 2.o
