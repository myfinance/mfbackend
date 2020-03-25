pipeline {
 agent any

 environment{
   SERVICE_NAME = "mfbackend"
   ORGANIZATION_NAME = "myfinance"
   DOCKERHUB_USER = "holgerfischer"
   VERSION = "0.13.${BUILD_ID}"
   REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-${SERVICE_NAME}:${VERSION}"
 }

 stages{
   stage('preperation'){
     steps {
       cleanWs()
       git credentialsId: 'github', url: "https://github.com/myfinance/mfbackend.git"
     }
   }
   stage('build'){
     steps {
       sh '''mvn clean deploy -Dnointtest'''
     }
   }
   //stage('build and push Image'){
   //  steps {
   //    sh 'docker image build -t ${REPOSITORY_TAG} ./distributions/mf-docker-images/target/myfinance'
   //  }
   //}

   //stage('deploy to cluster'){
   //  steps {
   //    sh 'envsubst < ${workspace}/deploy.yaml | kubectl apply -f -
   //  }
   //}
 }
}
