--liquibase formatted sql

--changeset hf:dac-data.1.0.1
INSERT INTO "dac_environmentconfiguration"("environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES('dev', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
INSERT INTO "dac_environmentconfiguration"("environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES('md', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
--changeset hf:dac-data.1.0.2
INSERT INTO "dac_restauthorization"("restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
  VALUES('marketdataprovider', 'environment', 'READ', '.*', 'admin', 'mydescription', 'all', 's');
