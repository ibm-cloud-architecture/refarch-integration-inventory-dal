# curl -X POST --header "Content-Type: text/xml;charset=UTF-8" --header "/ws.inventory/DALService/itemById" --data @soap-env.xml http://green.csplab.local/inventory/ws
curl -X POST --header "Content-Type: text/xml;charset=UTF-8" --header "/ws.inventory/DALService/itemById" --data @soap-env.xml http://169.45.191.91:9080/inventory/ws

