pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                sh './scripts/build.sh'
            }
        }
        stage('integration test') {
            steps {
                sh './scripts/tests.sh'
            }
        }
        stage('deploy') {
            steps {
             timeout(time: 3, unit: 'MINUTES') {
                sh './scripts/deployToICP.sh'
              }
            }
        }
    }
}
