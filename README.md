DAC - Distributed Application Container

## Goals - What does this do ##

A scalable, extendable Web-Application using via OSGi.
Using the following technologies:


- Jersey 2 providing JAX-RS REST services
- Narayana (JBoss Transaction Manager) providing JTA transactions
- Hibernate providing JPA persistence
- Karaf as hosting container

This setup also illustrates how to deploy the same functionality in an Spring-Boot or Wildfly App Server.

OSGi-wise I use

- The newest and bestest OSGi-spec
- Blueprint using blueprint-maven-plugin
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

e.G. 
mvn clean install -Dnoclientgen -Dnointtest -Dnoangular

!attention! if you run integrationtests or clientgeneration under linux, you have to deploy the docker images first. so run mvn clean deploy instead of install

### get started ###

to start MYFinancein Docker: docker stack deploy -c distributions/myfinance-full-packaging/target/docker-compose.yml myfinance

if Database is fresh: add via gui or api-Docs: currency eur and usd
then add equities for example DE0005140008 deutsche Bank
                              US5949181045 Microsoft
                              add symbols MSFT, DBK
                              start import prices
