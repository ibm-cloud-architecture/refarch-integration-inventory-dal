# Data Access Layer - Continuous Interation with Jenkins

This project includes a *jenkinsfile* which defines a build stage to call a shell to execute *gradlew* to compile and package the war file, built under the folder *build/libs*. The second step of the pipeline for this project is to deploy the war to the target Liberty App Server.
```
pipeline {
    agent { docker 'gradle' }
    stages {
        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
    },
    stage('deploy') {
        steps {
         timeout(time: 3, unit: 'MINUTES') {
            sh './deployToWlp.sh'
          }
        }
    }
}
```

3. The war file is deployed to the App Server, via remote copy.

The deployToWlp.bsh script is under the DAL project but it is using an interesting trick: the exp script to inject password
```
exp admin01 scp ./build/libs/*.war admin@172.16.254.44:~/IBM/wlp/usr/servers/appServer/apps
```

*exp* is a shell that uses the [expect](http://expect.sourceforge.net/) tool to automate interaction with application like ssh or scp. The script is defined in this project. It gets the password as first argument and then the command to execute.
```
#!/usr/bin/expect
set timeout 20
set cmd [lrange $argv 1 end]
set p [lindex $argv 0]
eval spawn $cmd
expect "Pipeline"
send "Yes\r";
expect "assword:"
send "$p\r";
interact
```
The *"Pipeline"* keyword is used when there is a question about certificate to accept.
