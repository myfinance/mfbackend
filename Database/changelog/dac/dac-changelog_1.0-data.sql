--liquibase formatted sql

--changeset hf:dac-data.1.0.1
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(1, 'dev', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(2, 'md', 'mdb', 'db', 'DEVPOSTGRES', NULL, NULL);
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(3, 'dev', 'adb', 'db', 'DEVPOSTGRES', NULL, NULL);
INSERT INTO "dac_serviceconfiguration"("serviceconfigurationid", "environment", "target", "envtype", "identifier", "jdbcurl", "jndiurl")
  VALUES(23, 'md', 'adb', 'db', 'DEVPOSTGRES', NULL, NULL);
--changeset hf:dac-data.1.0.2
INSERT INTO "dac_restauthorization"("restauthorizationid", "restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
VALUES(1, 'myfinance', 'environment', 'READ', '.*', 'read', 'mydescription', 'all', 's');
INSERT INTO "dac_restauthorization"("restauthorizationid", "restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
VALUES(2, 'myfinance', 'environment', 'WRITE', '.*', 'write', 'mydescription', 'all', 's');
INSERT INTO "dac_restauthorization"("restauthorizationid", "restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
VALUES(3, 'myfinance', 'environment', 'EXECUTE', '.*', 'admin', 'mydescription', 'all', 's');
INSERT INTO "dac_restauthorization"("restauthorizationid", "restapp", "resource", "restoptype", "restidpattern", "permissions", "description", "operations", "users")
VALUES(4, 'myfinance', 'runner', 'EXECUTE', '.*', 'exec', 'mydescription', 'all', 's');
