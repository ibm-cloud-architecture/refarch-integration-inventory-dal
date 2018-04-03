exp admin01 scp ./build/libs/*.war admin@172.16.254.44:~/IBM/wlp/usr/servers/appServer/apps
sleep 10
exp admin01 ssh admin@172.16.254.44 IBM/wlp/bin/server stop appServer
sleep 5
exp admin01 ssh admin@172.16.254.44 IBM/wlp/bin/server start appServer
