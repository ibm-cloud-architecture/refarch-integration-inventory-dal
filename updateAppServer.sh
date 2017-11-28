#!/bin/bash
# This script is used only if you have WebSphere Liberty install locally
cp ./src/main/liberty/config/server.xml ~/IBM/wlp/usr/servers/appServer
cp ./lib/db2jcc4.jar ~/IBM/wlp/usr/shared/config/lib/db2jcc4.jar
