
if [[ "$PWD" = */scripts ]]; then
 cd ..
fi
. ./scripts/setenv.sh

rc=$(helm ls --all $progname --tls)
if [[ ! -z "$rc" ]]; then
  rc=$(helm del --purge $progname --tls)
  if [[ "$rc" = *deleted ]]; then
    helm install --name $progname chart/$helmchart --namespace=$namespace --tls
  fi
else
  helm install --name $progname chart/$helmchart --namespace=$namespace --tls
fi
