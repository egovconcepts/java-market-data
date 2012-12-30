-- --------------------------------------------------------
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2012-12-30 12:31:18
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for YAHOO_TEST
DROP DATABASE IF EXISTS `YAHOO_TEST`;
CREATE DATABASE IF NOT EXISTS `YAHOO_TEST` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `YAHOO_TEST`;


-- Dumping structure for table YAHOO_TEST.STOCK
DROP TABLE IF EXISTS `STOCK`;
CREATE TABLE IF NOT EXISTS `STOCK` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `YAHOO_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table YAHOO_TEST.YAHOO_MARKET_DATA
DROP TABLE IF EXISTS `YAHOO_MARKET_DATA`;
CREATE TABLE IF NOT EXISTS `YAHOO_MARKET_DATA` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DATE` date NOT NULL,
  `TIME` time NOT NULL,
  `STOCK_NAME` varchar(30) DEFAULT NULL,
  `STOCK_DES` varchar(100) DEFAULT NULL,
  `BQTY` mediumtext NOT NULL,
  `BBID` double NOT NULL,
  `BASK` double DEFAULT NULL,
  `AQTY` mediumtext NOT NULL,
  `LAST_TRADE_DATE` date DEFAULT NULL,
  `LAST_TRADE_TIME` time DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
