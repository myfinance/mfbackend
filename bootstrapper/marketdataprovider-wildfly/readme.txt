add in Jboss/Wildfly standalone.xml:
            <datasources>
                <datasource jta="true" jndi-name="java:jboss/datasources/H2DS" pool-name="h2" enabled="true" use-java-context="true">
                    <connection-url>jdbc:h2:file:./h2dev;AUTO_SERVER=TRUE</connection-url>
                    <driver>h2</driver>
                </datasource>
                <datasource jta="true" jndi-name="java:jboss/datasources/postgres" pool-name="postgres" enabled="true" use-java-context="true">
                    <connection-url>jdbc:postgresql://localhost:5432/marketdata</connection-url>
                    <driver>postgresql-jdbc4</driver>
                    <security>
                        <user-name>****</user-name>
                        <password>*****</password>
                    </security>
                </datasource>
                                <drivers>
                                    <driver name="h2" module="com.h2database.h2">
                                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                                    </driver>
                                    <driver name="postgresql-jdbc4" module="org.postgresql">
                                        <driver-class>org.postgresql.Driver</driver-class>
                                    </driver>
                                </drivers>
                            </datasources>

add jdbc driver to {jboss/Wildfly}\modules\org\postgresql\main
add module.xml to {jboss/Wildfly}\modules\org\postgresql\main:

<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-9.1-901.jdbc4.jar"/>
  </resources>
</module>