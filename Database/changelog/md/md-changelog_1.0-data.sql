
--changeset hf:mf-data.1.0.1

INSERT INTO "mf_instrumenttype"("instrumenttypeid", "description")
VALUES(1, 'Security');

INSERT INTO "mf_securitytyp"("securitytypeid", "description")
VALUES(1, 'Equity');
INSERT INTO "mf_securitytyp"("securitytypeid", "description")
VALUES(2, 'Currency');

INSERT INTO "mf_source"("sourceid", "description", "prio", "isactive")
VALUES(1, 'MAN', 1, false);
INSERT INTO "mf_source"("sourceid", "description", "prio", "urlprefix", "urlpostfix", "isactive")
VALUES(2, 'ALPHAVANTAGEEQ', 2, 'http://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&apikey=Q6RLS6PGB55105EP', true);
INSERT INTO "mf_source"("sourceid", "description", "prio", "urlprefix", "urlpostfix", "isactive")
VALUES(3, 'ALPHAVANTAGEFX', 2, 'http://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=', '&to_currency=EUR&apikey=Q6RLS6PGB55105EP', true);



