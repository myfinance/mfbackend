pipeline {
 agent none

 environment{
   SERVICE_NAME = "mfbackend"
   ORGANIZATION_NAME = "myfinance"
   DOCKERHUB_USER = "holgerfischer"

   //Snapshot Version
   VERSION = "0.17.0-alpha.${BUILD_ID}"
   //Release Version
   //VERSION = "0.16.0"

   K8N_IP = "192.168.100.73"
   REPOSITORY_TAG = "${DOCKERHUB_USER}/${ORGANIZATION_NAME}-${SERVICE_NAME}:${VERSION}"
   NEXUS_URL = "${K8N_IP}:31001"
   MVN_REPO = "http://${NEXUS_URL}/repository/maven-releases/"
   DOCKER_REPO = "${K8N_IP}:31003/repository/mydockerrepo/"
   TARGET_HELM_REPO = "http://${NEXUS_URL}/repository/myhelmrepo/"
   SONAR = "https://sonarcloud.io"
   DEV_NAMESPACE = "mfdev"
   TEST_NAMESPACE = "mftest"
 }
 
 stages{
   stage('preperation'){
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
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
            image 'maven:3.6.3-jdk-11'
        }
    }      
     steps {
       sh '''mvn versions:set -DnewVersion=${VERSION}'''
       sh '''mvn clean deploy -DtargetRepository=${MVN_REPO} -Pjacoco -Dsonar.jacoco.reportPaths=./jacoco-ut.exec -Dsonar.jacoco.itReportPath=./jacoco-it.exec org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=myfinance_mfbackend -Dsonar.organization=myfinance -Dsonar.host.url=${SONAR} -Dsonar.login=d48202ac4b86df8e851d5429983b0205916352ca'''

     }
   }
   stage('build and push Images'){
    agent {
        docker {
            image 'docker' 
        }
    }        
     steps {
       sh 'docker image build -t ${REPOSITORY_TAG} ./distributions/mf-docker-images/target/docker-prep/myfinance/'

       sh 'docker tag ${REPOSITORY_TAG} ${DOCKER_REPO}${REPOSITORY_TAG}'

       sh 'docker push ${DOCKER_REPO}${REPOSITORY_TAG}'
     }
   }

   stage('deploy to cluster'){
     agent any
     steps {
       //sh 'kubectl delete job.batch/mfupgrade'
       //sh 'envsubst < deploy.yaml | kubectl apply -f -'
       sh 'envsubst < ./distributions/helm/mfbackend/Chart_template.yaml > ./distributions/helm/mfbackend/Chart.yaml'
       sh 'helm upgrade -i --cleanup-on-fail mfbackend ./distributions/helm/mfbackend/  -n ${DEV_NAMESPACE} --set stage=dev --set repository=${DOCKER_REPO}${DOCKERHUB_USER}/${ORGANIZATION_NAME}-'
       sh 'helm upgrade -i --cleanup-on-fail mfbackend ./distributions/helm/mfbackend/  -n ${TEST_NAMESPACE} --set stage=test --set mfbackend.mf_http_port_ext=30031 --set mfbackend.mf_https_port_ext=30032 --set repository=${DOCKER_REPO}${DOCKERHUB_USER}/${ORGANIZATION_NAME}-'
       sh 'helm package distributions/helm/mfbackend -u -d helmcharts/'
       sh 'curl ${TARGET_HELM_REPO} --upload-file helmcharts/mfbackend-${VERSION}.tgz -v'
     }
   }

   stage('test'){
    agent {
        docker {
            image 'maven:3.6.3-jdk-11'
        }
    }
     steps {
       sh '''mvn clean install -f test/pom.xml -DNEXUS_URL=${NEXUS_URL}'''
     }
   }
 }
}
