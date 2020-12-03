# hmodule to update the hibernate domainobjects

update the hibernate-files. Small changes can be done manually. 
For big changes you can use mvn antrun:run@dachbm2java to create the files
but attention you have to check all generated files anyway   

create domain objects for the dev-db with mvn antrun:run@dachbm2java
