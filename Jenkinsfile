podTemplate(label: 'mypod',
    volumes: [
        hostPathVolume(hostPath: '/etc/docker/certs.d', mountPath: '/etc/docker/certs.d'),
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
        secretVolume(secretName: 'registry-account', mountPath: '/var/run/secrets/registry-account'),
        configMapVolume(configMapName: 'registry-config', mountPath: '/var/run/configs/registry-config')
    ],
    containers: [
        containerTemplate(name: 'gradle' , image: 'gradle:jre8', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker' , image: 'docker:17.06.1-ce', ttyEnabled: true, command: 'cat')/*,
        containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:v2.8.2', ttyEnabled: true, command: 'cat',
            envVars: [
              containerEnvVar(key: 'TILLER_NAMESPACE', value: tillerNamespace)
        ])*/
  ]) {

    node('mypod') {
        checkout scm
        container('gradle') {
            stage('Compile Code') {
                sh 'gradle build'
                //sh 'echo hola'
                //sh """
                //#!/bin/bash
                //gradlew -Dorg.gradle.daemon=false build
                //gradle build
                //"""
            }
        }
        container('docker') {
            stage('Build Docker Image') {
                sh """
                #!/bin/bash
                NAMESPACE=`cat /var/run/configs/registry-config/namespace`
                REGISTRY=`cat /var/run/configs/registry-config/registry`

                docker build -t \${REGISTRY}/\${NAMESPACE}/browncompute-dal:${env.BUILD_NUMBER} .
                """
            }
            stage('Push Docker Image to Registry') {
                sh """
                #!/bin/bash
                NAMESPACE=`cat /var/run/configs/registry-config/namespace`
                REGISTRY=`cat /var/run/configs/registry-config/registry`

                set +x
                DOCKER_USER=`cat /var/run/secrets/registry-account/username`
                DOCKER_PASSWORD=`cat /var/run/secrets/registry-account/password`
                docker login -u=\${DOCKER_USER} -p=\${DOCKER_PASSWORD} \${REGISTRY}
                set -x

                docker push \${REGISTRY}/\${NAMESPACE}/browncompute-dal:${env.BUILD_NUMBER}
                """
            }
        }
        /*
        container('helm') {
            stage('Deploy Helm Chart') {
                sh """
                #!/bin/bash
                set +e
                NAMESPACE=`cat /var/run/configs/registry-config/namespace`
                REGISTRY=`cat /var/run/configs/registry-config/registry`
                DEPLOYMENT=`kubectl --namespace=\${NAMESPACE} get deployments -l app=bluecompute,micro=web-bff -o name`

                kubectl --namespace=\${NAMESPACE} get \${DEPLOYMENT}

                if [ \${?} -ne "0" ]; then
                    # No deployment to update
                    helm init --skip-refresh
                    helm install --namespace=\${NAMESPACE} chart/browncompute-dal --set image.repository=\${REGISTRY}/\${NAMESPACE}/browncompute-dal --image.tag=\${env.BUILD_NUMBER} --set ingress.enabled=false --tls
                else                
                    # Update Deployment
                    kubectl --namespace=\${NAMESPACE} set image \${DEPLOYMENT} web=\${REGISTRY}/\${NAMESPACE}/browncompute-dal:${env.BUILD_NUMBER}
                    kubectl --namespace=\${NAMESPACE} rollout status \${DEPLOYMENT}
                """
                fi
            }
        }
        container('kubectl') {
            stage('Update Docker Image') {
                sh """
                #!/bin/bash
                set +e
                NAMESPACE=`cat /var/run/configs/registry-config/namespace`
                REGISTRY=`cat /var/run/configs/registry-config/registry`
                DEPLOYMENT=`kubectl --namespace=\${NAMESPACE} get deployments -l app=bluecompute,micro=web-bff -o name`

                kubectl --namespace=\${NAMESPACE} get \${DEPLOYMENT}

                if [ \${?} -ne "0" ]; then
                    # No deployment to update
                else                
                    # Update Deployment
                    kubectl --namespace=\${NAMESPACE} set image \${DEPLOYMENT} web=\${REGISTRY}/\${NAMESPACE}/browncompute-dal:${env.BUILD_NUMBER}
                    kubectl --namespace=\${NAMESPACE} rollout status \${DEPLOYMENT}
                """
                fi
            }
        }*/
    }
}
