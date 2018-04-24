#!/bin/bash
if [[ "$PWD" = */scripts ]]; then
  cd ..
fi
. ./scripts/setenv.sh
v=$(grep -o 'v\([0-9]\+.\)\{2\}\([0-9]\+\)' $dalserv)

./scripts/exp  admin docker login -u admin $k8cluster:8500
docker push $k8cluster:8500/$namespace/$progname:$v

rc=$(helm ls --all bc-dal --tls | grep bc-$progname)
if [[ ! -z "$rc" ]]; then
  rc=$(helm del --purge bc-$progname --tls)
  if [[ "$rc" = *deleted ]]; then
    helm install --name bc-$progname chart/browncompute-dal --namespace=$namespace --tls
  fi
else
  helm install --name bc-$progname chart/browncompute-dal --namespace=$namespace --tls
fi
