
--changeset hf:mf-data.1.0.1

INSERT INTO "mf_source"("sourceid", "description", "isactive")
VALUES(1, 'MAN', false);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(2, 'ALPHAVANTAGEEQ', 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&apikey=Q6RLS6PGB55105EP', true);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(3, 'ALPHAVANTAGEFX', 'https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=', '&to_currency=EUR&apikey=Q6RLS6PGB55105EP', true);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(4, 'ALPHAVANTAGEEQ', 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&outputsize=full&apikey=Q6RLS6PGB55105EP', false);



