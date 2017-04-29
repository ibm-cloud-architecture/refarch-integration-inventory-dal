pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('deploy') {
            steps {
                sh './updateAppServer.sh'
            }
        }
    }
}
