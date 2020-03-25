pipeline {
 agent any

 stages{
   stage('preperation'){
     steps {
       git credentialsId: 'github', url: "https://github.com/myfinance/mfbackend.git"
     }
   }
   stage('build'){
     steps {
       sh '''mvn clean install -Dnointtest'''
     }
   }
 }
}
