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
             timeout(time: 30, unit: 'SECONDS') {
                sh './deployToWlp.sh'
              }
            }
        }
    }
}
