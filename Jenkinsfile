pipeline {
 agent none

 environment{
   SERVICE_NAME = "mfbackend"
   ORGANIZATION_NAME = "myfinance"
   DOCKERHUB_USER = "holgerfischer"
   VERSION = "0.13.${BUILD_ID}"
   REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-${SERVICE_NAME}:${VERSION}"
   DB_REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-mfpostgres:${VERSION}"
   MFUPDATE_REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-mfdbupdate:${VERSION}"
 }

 stages{
   stage('preperation'){
    agent {
        docker {
            image 'maven:3.6.3-jdk-8' 
            args '-p 3000:3000' 
        }
    }      
     steps {
       cleanWs()
       git credentialsId: 'github', url: "https://github.com/myfinance/mfbackend.git"
     }
   }
   stage('build'){
    agent {
        docker {
            image 'maven:3.6.3-jdk-8' 
            args '-p 3000:3000' 
        }
    }      
     steps {
       sh '''mvn clean install -Dnointtest'''
     }
   }
   stage('build and push Images'){
    agent {
        docker {
            image 'docker' 
            args '-p 3000:3000' 
        }
    }        
     steps {
       sh 'docker image build -t ${DB_REPOSITORY_TAG} ./distributions/mf-docker-images/docker/mfpostgres/'
       sh 'docker image build -t ${MFUPDATE_REPOSITORY_TAG} ./distributions/mf-docker-images/target/docker-prep/mfdb/'
       sh 'docker image build -t ${REPOSITORY_TAG} ./distributions/mf-docker-images/target/docker-prep/myfinance/'
     }
   }

   stage('deploy to cluster'){
    agent {
        docker {
            image 'mykubectl' 
            args '-p 3000:3000' 
        }
    }        
     steps {
       //sh 'envsubst < ${workspace}/deploy.yaml | kubectl apply -f deploy.yaml'
       sh 'kubectl apply -f deploy.yaml'
     }
   }
 }
}
