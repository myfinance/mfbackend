--liquibase formatted sql

--changeset hf:dac-data.1.0.1
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(1, 'dev', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(2, 'md', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
--changeset hf:dac-data.1.0.2
INSERT INTO "dac_restauthorization"("restauthorizationid", "restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
  VALUES(1, 'marketdataprovider', 'environment', 'READ', '.*', 'admin', 'mydescription', 'all', 's');
