#!/bin/bash
h=$(hostname)
if [[ "$h" == "brownlibertyappserver.localdomain" ]]; then
    cp ../build/libs/*.war ~/IBM/wlp/usr/servers/appServer/apps
    sleep 10
    ~/IBM/wlp/bin/server stop appServer
    sleep 5
    ~/IBM/wlp/bin/server start appServer
else
   ./exp admin01 scp ../build/libs/*.war admin@172.16.249.79:~/IBM/wlp/usr/servers/appServer/apps
   sleep 10
   ./exp admin01 ssh admin@172.16.249.79 ~/IBM/wlp/bin/server stop appServer
   sleep 5
   ./exp admin01 ssh admin@172.16.249.79 ~/IBM/wlp/bin/server start appServer
fi


