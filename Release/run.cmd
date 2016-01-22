REM /*das configfile ist optional. ist es nicht angegeben wird das jar-interne verwendet*/
java -jar ../bootstrapper/target/bootstrapper-1.0-SNAPSHOT.jar --spring.config.location=application.properties 
REM /*das commandline argument überschreibt hier den inhalt des propüertyfiles -> name=spring statt holger2*/
REM java -jar ../Bootstrapper/target/Bootstrapper-1.0-SNAPSHOT.jar --spring.config.location=application.properties --name="Spring"