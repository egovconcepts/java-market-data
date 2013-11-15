CREATE TABLE STOCK (
  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  NAME varchar(100) NOT NULL,
  YAHOO_NAME varchar(50) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping structure for table YAHOO_TEST.YAHOO_MARKET_DATA
CREATE TABLE YAHOO_MARKET_DATA (
  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  DATE date NOT NULL,
  TIME time NOT NULL,
  STOCK_NAME varchar(30) DEFAULT NULL,
  STOCK_DES varchar(100) DEFAULT NULL,
  BQTY int NOT NULL,
  BBID double NOT NULL,
  BASK double DEFAULT NULL,
  AQTY int NOT NULL,
  LAST_TRADE_DATE date DEFAULT NULL,
  LAST_TRADE_TIME time DEFAULT NULL,
  PRIMARY KEY (ID)
);