For devolopment use maven to create liquibase-scripts and update dev-db:
mvn liquibase:diff@diff_dac
mvn liquibase:update@update_dac
mvn liquibase:update@update_dev

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



