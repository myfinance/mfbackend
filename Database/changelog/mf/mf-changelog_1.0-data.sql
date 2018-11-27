
--changeset hf:mf-data.1.0.1


INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(1, 'Giro', 'CashAccount');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(2, 'MoneyAtCall', 'CashAccount');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(3, 'TimeDeposit', 'CashAccount');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(4, 'BuildingsavingAccount', 'CashAccount');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(5, 'Budget', 'CashAccount');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(6, 'tenant', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(7, 'BudgetGroupPortfolio', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(8, 'AccountPortfolio', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(9, 'artificialPortfolio', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(10, 'BudgetGroup', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(11, 'Depot', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(12, 'Buildingsaving', 'Portfolio');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(13, 'Currency', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(14, 'Equity', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(15, 'Fonds', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(16, 'ETF', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(17, 'Index', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(18, 'Bond', 'Security');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(20, 'LifeInsurance', 'LifeInsurance');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(21, 'DepreciationObject', 'DepreciationObject');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(22, 'RealEstate', 'RealEstate');
INSERT INTO mf_instrumenttype("instrumenttypeid", "typename", "typegroup")
VALUES(23, 'Loan', 'Loan');

INSERT INTO "mf_source"("sourceid", "description", "isactive")
VALUES(1, 'MAN', false);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(2, 'ALPHAVANTAGEEQ', 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&apikey=Q6RLS6PGB55105EP', true);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(3, 'ALPHAVANTAGEFX', 'https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=', '&to_currency=EUR&apikey=Q6RLS6PGB55105EP', true);
INSERT INTO "mf_source"("sourceid", "description", "urlprefix", "urlpostfix", "isactive")
VALUES(4, 'ALPHAVANTAGEEQ', 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=', '&outputsize=full&apikey=Q6RLS6PGB55105EP', false);



