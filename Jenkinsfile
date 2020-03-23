pipeline {
 agent any

 stages{
   stage('preperation'){
     steps {
       git credentialsId: 'bitbucket', url: "https://holgerfischer@bitbucket.org/holgerfischer/dac.git"
     }
   }
   stage('build'){
     steps {
       sh '''mvn clean install -Dnointtest'''
     }
   }
 }
}
