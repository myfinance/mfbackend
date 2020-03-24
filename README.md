
## Goals - What does this do ##

This project is an example-project to evaluate new technologies. In this repository I've tried to use a karafcontainer for the backend. 

Using the following technologies:


- Jersey 2 providing JAX-RS REST services
- Narayana (JBoss Transaction Manager) providing JTA transactions
- Hibernate providing JPA persistence
- Karaf as hosting container

This setup also illustrates how to deploy the same functionality in an Spring-Boot or Wildfly App Server.

OSGi-wise I use

- The newest and bestest OSGi-spec
- dedclarativ Service
- bnd via the bnd-maven-plugin

Bonus Features: 
JTA/JPA without persistence.xml

## Why ##

Java and the JVM is here to stay. There may be better languages, but Java is often reality.

I sincerely believe that for an organisation that wants to build maintainable, testable and extendable applications in a JVM environment, learning to use and apply OSGi is a potentially revolutionary step.

OSGi is, however, hard to learn. It is very well specified and as such documented by itself, but as with many mosty 'enterprisey' technologies, the scope is much too broad and deep to be grasped by reading and imitating some stackoverflow and blog entries. There is also lots if legacy code and outdated approaches out there - OSGi is quite old!

I also don't believe in (too) opinionated and proprietary setups. Bndtools, Enroute, Eclipse, PDE might be great tools - but if they are mandatory for my build I won't use them.

It boils down to what I know, and what I believe is trivially introduced in any Java Shop - maven and its plugin architecture.

Maven is a great build tool, and together with bnd it is, for me where I stand right now, the future of Java development.

## Project Organization ##

Our project is organized in a two-level folder hierarchy. On each level there is a README explaining the current level.

On this highest level of organisation we have our reactor-pom (declaring all modules needed for a build) and four folders in which we organize our modules:

- Features
- Bootstrapper
- Bundle

### Bundles ###

The core functionality of our application, separated across multiple bundles - high cohesion, low coupling apply here.
Bundles depend on/relate to each other and thirdparty-libraries never (ideally) directly, but via Api-Jars.

### Bootstrapper ###

How we run our functionality. We need to start the OSGi Framework, collect and load our bundles, configure our application and somehow connect with the world.

### Features ###

These modules just collect bundles which are necessary to provide certain high-level features - like persistence, rest services, consoles to control our apps at runtime.
This is conceptionally related to Karaf-Features.

### Felix Commands ###

DAC uses Felix-GOGO-commands to start batch-jobs via ssh. Often it is necessary to autowire a lot of components depending on the batch job parameters. 
Blueprint is a static DI and can not do this, so google guice is used.


### Build ###

requirements:

node-js must be installed. then npm install -g @angular/cli
for win only: 7zip hast to installed and in path

profiles: 
- deploy-win if you like to run tests with karaf in a windows environment - activated on windows
- docker if you like to run tests with karaf with the help of docker - activated on linux
- clientgen to regenerate the Rest-Api-Client - cost some time 
    but you should do it for a release-build to be sure you haven't forgotten some changes in the api -activate by default
- noclientgen activated if property noclientgen is set (-Dnoclientgen) this deactivates clientgen. this profile is necessary due to build order issues
 (module depended on profile clientgen will always build after the default modules) 
- inttest to run integration tests with karaf. activatre by default. to deactivate set property -Dnointtest
- angularbuild to build angular client (takes a lot of time) activated by default to deactivate set property -Dnoangular
- paxam to run pax exam
- jacoco  to run test coverage for sonarqube. Attention you have to set the absolute path to the parent target directory: -Pjacoco -Dsonar.jacoco.reportPaths=C:/devenv/repos/dac/target/jacoco-ut.exec -Dsonar.jacoco.itReportPath=C:/devenv/repos/dac/target/jacoco-it.exec 

e.G. 
mvn clean install -Dnoclientgen -Dnointtest -Dnoangular -Pjacoco -Dsonar.jacoco.reportPaths=C:/devenv/repos/dac/target/jacoco-ut.exec -Dsonar.jacoco.itReportPath=C:/devenv/repos/dac/target/jacoco-it.exec 
Sonarqube load: mvn -Pjacoco -Dsonar.jacoco.reportPaths=C:/devenv/repos/dac/target/jacoco-ut.exec -Dsonar.jacoco.itReportPath=C:/devenv/repos/dac/target/jacoco-it.exec  sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=bf889454ae88499d420851b077952ae71fd1740f
or at the buildserver(see ansible): mvn -Pjacoco sonar:sonar -Dsonar.jacoco.reportPaths=~/repos/dac/target/jacoco-ut.exec -Dsonar.jacoco.itReportPath=~/repos/dac/target/jacoco-it.exec -Dsonar.host.url=http://localhost:9000 -Dsonar.login=afd01fc97608394267c0af112064b12ffcbc702a

!attention! if you run integrationtests or clientgeneration under linux, you have to deploy the docker images first. so run mvn clean deploy instead of install

### DdevEnv ###

In der Dev-Umgebung muss export DAC_LOGIN_INFO=$HOME/dac.res gesetzt sein und eine postgres instanz entsprechend eingerichtet sein

### get started ###

install ansible on centos:
yum install ansible
create .vault_prod in homedir with the vault-passwort - to use the encrypted passwords which are checked-in the repository you nee the password from my keepass-file ;) if you can not get it recreate all secrets with your vaul-password  ansible-vault encrypt_string --vault-id prod@~/.vault_prod 'thepasswaord' --name 'variable-name'
update the inventory-file with your IPs to a kubernetes(kuberneteshost) and a development server(devenv) (both minimal CentOS setups) doc/install/ansible/environments/prod
login via ssh from ansible-host to myfinance-server to create private key
copy playbook from doc/install/ansible to ansible host or mount an nfs-share with the playbooks (add a row in the file /etc/fstab <code><ip>://<path> /mnt/data nfs rw 0 0</code>) Achtung dazu muss auch nfs-utils installiert sein mit sudo yum install nfs-utils
prepare passwordless communication from ansible host to myfinanceserver "ssh-keygen -t rsa" ssh-copy-id "user@<your_ip>"
at least python has to be installed at the ansible client 

configure kubernetes and devenv-server: ansible-playbook site.yml --vault-id prod@~/.vault_prod

/mnt/data is mounted on both server (kubernetes and devenv). clone the repo to /mnt/data/repo (from devenv. kubernetes has no git installed): git clone https://holgerfischer@bitbucket.org/holgerfischer/dac.git

to install jenkins run the following command in folder /mnt/data/repo/dac/buildserver
./installJenkins.sh

to install the application manually run the following command in folder /mnt/data/repo/dac/doc/install/kubernetes (optional. should be done via buildserver)
kubectl apply -f .


if Database is fresh: add via gui or api-Docs: currency EUR and USD
then add equities for example DE0005140008 deutsche Bank
                              US5949181045 Microsoft
                              add symbols MSFT, DBK
                              start import prices
                              
### mfshell ###                              

admin shell to start jobs etc on myfinance. works only in intranet 
(only http request at the moment - ssl not working and it is not recommended to puplish the http port outside the intranet)
shouldn't ne necessary to use outside the scheduler but just in case:
docker run -v "mfshellconfig:/mfshell/envconfig" holgerfischer/myfinance:0.12.0-SNAPSHOT-mfshell //replace the version with the latest
if default ip http://192.168.100.71:8182/dac/rest is wrong then change dac.res in the mapped volume mfshellconfig

### psql ###


bei kubernetes mit: "kubectl -it exec <podname> sh" auf den Dockercontainer gehen und dort psql postgresql://postgres:postgres@localhost:5432/marketdata

### dump and restore ###

you can stop the docker services and copy the myfinancedata-volume to the new location or an nfs share.
to restore this volume create an empty volume myfinancedata and copy the saved volume-data to it

just in case, if this process is not working due to an hardwarecrash e.G., a job is scheduled that dumps the database every day and on startup
If you want to create a fresh dump: just restart the scheduling-service or the complete stack:
docker stack rm myfinance
docker stack deploy -c distributions/myfinance-full-packaging/target/docker-compose.yml myfinance

to restore a dump:
// jobs are imutable, so you have to delete the old job if it exists:
kubectl delete job.batch/mfrestore
//edit the dump-filename in  doc/install/kubernetes/tools/dumprestore.yaml
kubectl apply -f dumprestore.yaml

 
