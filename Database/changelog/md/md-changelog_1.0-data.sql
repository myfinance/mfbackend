--liquibase formatted sql

--changeset hf:md-data.1.0.1
INSERT INTO "md_instrument"("id", "description", "hometradingcurrencyid", "instrumenttypeid", "isin", "sourceid", "ticker", "treelastchanged", "wkn")
  VALUES(1, 'testinstrument1', 0, 0, 'testisin0001', 0, 'testticker1', '2017-01-10 15:32:40.562', 'testwkn1');
INSERT INTO "md_instrument"("id", "description", "hometradingcurrencyid", "instrumenttypeid", "isin", "sourceid", "ticker", "treelastchanged", "wkn")
  VALUES(2, 'testinstrument2', 0, 0, 'testisin0002', 0, 'testticker2', '2017-01-10 15:32:40.562', 'testwkn2');
INSERT INTO "md_product"("id", "description", "image_url", "price", "product_id", "version")
  VALUES(1, 'test', NULL, 1.00, 'testid', 1);
INSERT INTO "md_product"("id", "description", "image_url", "price", "product_id", "version")
  VALUES(2, 'test2', NULL, 2.50, 'testid2', 1);

