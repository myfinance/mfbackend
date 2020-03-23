pipeline {
 agent any

 stages{
   stage('preperation'){
     steps {
       git credentialsId: 'bitbucket', url: "https://holgerfischer@bitbucket.org/holgerfischer"
     }
   }
   stage('build'){
     steps {
       sh '''mvn clean install'''
     }
   }
 }
}
