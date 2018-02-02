--liquibase formatted sql

--changeset hf:md-data.1.2.1

INSERT INTO "mf_importtype"("importtypeid", "description")
VALUES(1, 'RestImportJSON');

INSERT INTO "mf_instrumenttype"("instrumenttypeid", "description")
VALUES(1, 'Security');

INSERT INTO "mf_securitytyp"("securitytypeid", "description")
VALUES(1, 'Equity');

INSERT INTO "mf_source"("description", "prio", "urlprefix", "urlpostfix", "importtypeid")
VALUES('alphavantage_daily_EQ', 1, 'http://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&apikey=Q6RLS6PGB55105EP', 1);
INSERT INTO "mf_source"("description", "prio", "urlprefix", "urlpostfix", "importtypeid")
VALUES('alphavantage_daily_FX', 1, 'http://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE', '&apikey=Q6RLS6PGB55105EP', 1);
