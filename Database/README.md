# how to update you databse

##update the documentation

use powerdesigner for this and open the Workspace in /powerdesigner/myfinance
commit the workspace and make a screenshot of the diagram in case someone has no powerdesigner to read this.
replace the picture in bundles/web/doc/src/main/asciidoc/_include/images

## create liquibase files

1. [optional] in case you have major changes powerdesigner can help:
a. create or update a database for each schema (dac_pd_db, mf_pd_db) with powerdesigner (see powerdesigner documentation)
b. if not already done create a database with the old liquibase-scripts for each schema 
(mvn liquibase:update@update_dac -> dac_lb_db, mvn liquibase:update@update_mf -> mf_lb_db)
c. run mvn liquibase:diff@diff_dac and mvn liquibase:diff@diff_mf to create the liquibase files
d. move the liquibasefiles to the changelog folder and rename it with the next version
-> this a lot of manual work, error prone and you need a local database -> do this only if the changes are the big that you save a lot of work to update the DB with Powerdesigner

2. create (or review and update the just created)  liquibase file in the changelog with the next version

3. update the changelog-master

4. validate the new file 
/distributions/mf-docker-images/target/docker-prep/mfdb/database/liquibase --changeLogFile=../../../../../../Database/changelog/mf/mf-changelog_1.1.xml --url="jdbc:postgresql://localhost:30030/marketdata" --username=postgres --password=xxx validate

5. run build.sh to redeploy the local dev-db (mvn clean install and kubectl delete job.batch/mfupgrade have to run there!)

6. check the logs: kubectl logs pod/mfupgrade-xxxxx (get full containername: kubectl get all )

7. update the hibernate-files. Small changes can be done manually. For big changes you can use mvn antrun:run@dachbm2java to create the files
but attention you have to check all generated files anyway   

create domain objects for the dev-db with mvn antrun:run@dachbm2java

In Test and Prod use the scripts in ./scripts to update the database. 
These scripts will be added to the package in the Database folder.
The liquibase-script is meant to work with the plain 
liquibase 
 --driver=org.postgresql.Driver 
 --classpath=".\postgresql-9.4-1203-jdbc42.jar" 
 --changeLogFile=changelog/changelog-master.xml 
 --url="jdbc:postgresql://localhost:5432/dac_lb_db" 
 --username=postgres 
 --password=**** 
 update
 

 
The dac_liquibase-script is meant to work with less parameters. Everthing else is set to the dac envirnment(Postgres-db ...)
dac_liquibase.cmd 
-url "localhost:5432/dac_lb_db" 
-u postgres 
-p xxxxx 



