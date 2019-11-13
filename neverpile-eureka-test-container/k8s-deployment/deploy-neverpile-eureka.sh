#!/usr/bin/env bash

set -e ;
set -u ;

_main(){

    # optional kubeconfig file passed as argument "--kubeconfig /path/to/file"
#    local -r _KUBE_CONFIG_FILE="${1:-''}" ;

    echo "create namespace 'neverpile' and add credentials for TLS and Private Docker Registry" ;
    kubectl apply -f ./000-namespace.yaml ;
    ~/bin/namespace-setup.sh neverpile ;

#    echo "deploy neverpile stack" ;
#    kubectl deploy -f . ;

    echo "---deploy elasticsearch---" ;
    kubectl apply -f ./101-elasticsearch-configmap.yaml ;
    kubectl apply -f ./102-elasticsearch-rbac.yaml ;
    kubectl apply -f ./110-elasticsearch-deployment.yaml ;
    kubectl apply -f ./120-elasticsearch-service.yaml ;
    sleep 5 ;

    echo "---deploy cassandra---" ;
    kubectl apply -f ./210-cassandra-deployment.yaml ;
    kubectl apply -f ./220-cassandra-service.yaml ;
    sleep 5 ;

    echo "---deploy spring-boot-admin---" ;
    kubectl apply -f ./310-spring-boot-admin-deployment.yaml ;
    kubectl apply -f ./320-spring-boot-admin-service.yaml ;
    kubectl apply -f ./350-spring-boot-admin-ingress.yaml ;
    sleep 5 ;

    echo "---deploy neverpile---" ;
    kubectl apply -f ./502-neverpile-rbac.yaml ;
    kubectl apply -f ./510-neverpile-deployment.yaml_inactive ;
    kubectl apply -f ./520-neverpile-service.yaml ;
    kubectl apply -f ./550-neverpile-ingress.yaml ;
    sleep 5 ;

    echo "---deploy web-client---" ;
    kubectl apply -f ./610-web-client-deployment.yaml ;
    kubectl apply -f ./620-web-client-service.yaml ;
    kubectl apply -f ./650-web-client-ingress.yaml ;
    sleep 5 ;

} ;

_main "${@}" ;
