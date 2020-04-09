pipeline {
 agent none

 environment{
   SERVICE_NAME = "mfbackend"
   ORGANIZATION_NAME = "myfinance"
   DOCKERHUB_USER = "holgerfischer"
   VERSION = "0.13.${BUILD_ID}"
   K8N_IP = "192.168.100.73"
   REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-${SERVICE_NAME}:${VERSION}"
   DB_REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-mfpostgres:${VERSION}"
   MFUPDATE_REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-mfdbupdate:${VERSION}"
   MVN_REPO = "http://${K8N_IP}:31001/repository/maven-snapshots/"
   DOCKER_REPO = "${K8N_IP}:31003/repository/mydockerrepo/"
 }

 stages{
   stage('preperation'){
    agent {
        docker {
            image 'maven:3.6.3-jdk-8' 
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
        }
    }      
     steps {
       sh '''mvn clean deploy -DtargetRepository=${MVN_REPO} -Dnointtest'''
     }
   }
   stage('build and push Images'){
    agent {
        docker {
            image 'docker' 
        }
    }        
     steps {
       sh 'docker image build -t ${DB_REPOSITORY_TAG} ./distributions/mf-docker-images/docker/mfpostgres/'
       sh 'docker image build -t ${MFUPDATE_REPOSITORY_TAG} ./distributions/mf-docker-images/target/docker-prep/mfdb/'
       sh 'docker image build -t ${REPOSITORY_TAG} ./distributions/mf-docker-images/target/docker-prep/myfinance/'

       sh 'docker tag ${REPOSITORY_TAG} ${DOCKER_REPO}${REPOSITORY_TAG}'
       sh 'docker tag ${MFUPDATE_REPOSITORY_TAG} ${DOCKER_REPO}${MFUPDATE_REPOSITORY_TAG}'
       sh 'docker tag ${DB_REPOSITORY_TAG} ${DOCKER_REPO}${DB_REPOSITORY_TAG}'
       
       sh 'docker push ${DOCKER_REPO}${REPOSITORY_TAG}'
       sh 'docker push ${DOCKER_REPO}${MFUPDATE_REPOSITORY_TAG}'
       sh 'docker push ${DOCKER_REPO}${DB_REPOSITORY_TAG}'
     }
   }

   stage('deploy to cluster'){
     agent any
     steps {
       sh 'envsubst < deploy.yaml | kubectl apply -f -'
     }
   }
 }
}
