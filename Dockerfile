FROM websphere-liberty:webProfile7
MAINTAINER https://github.com/ibm-cloud-architecture - IBM - Jerome Boyer
RUN installUtility install --acceptLicense jpa-2.0 jaxws-2.2 adminCenter-1.0
RUN mkdir /dalapp
WORKDIR /dalapp
COPY . /dalapp
RUN cd /dalapp
ADD src/main/liberty/config/server.xml /opt/ibm/wlp/usr/servers/defaultServer
RUN mkdir /opt/ibm/wlp/usr/shared/config/lib
ADD ./lib/db2jcc4.jar /opt/ibm/wlp/usr/shared/config/lib
ADD ./build/libs/refarch-integration-inventory-dal.war /opt/ibm/wlp/usr/servers/defaultServer/apps
EXPOSE 9080 9443
ENTRYPOINT ["/opt/ibm/wlp/bin/server", "run", "defaultServer"]
