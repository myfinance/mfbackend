use liquibase-script to work with liquibase commands like 
liquibase 
 --driver=org.postgresql.Driver 
 --classpath=".\postgresql-9.4-1203-jdbc42.jar" 
 --changeLogFile=db.changelogdiff1.xml 
 --url="jdbc:postgresql://localhost:5432/dac_lb_db" 
 --username=postgres 
 --password=**** 
 diffChangeLog 
 --referenceUrl="jdbc:postgresql://localhost:5432/dac_pd_db" 
 --referenceUsername=dac 
 --referencePassword=**** 
 --referenceDriver=org.postgresql.Driver
 
use dac_liquibase-scripts to work with less parameters. Everthing else is set to the dac envirnment(Postgres-db ...)
liquibase.cmd 
-m "diffChangeLog" 
-url "localhost:5432/dac_lb_db" 
-u postgres 
-p xxxxx 
-refurl 
localhost:5432/dac_pd_db 
-ru dac 
-rp xxxxx


