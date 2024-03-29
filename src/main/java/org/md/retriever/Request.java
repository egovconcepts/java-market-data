package org.md.retriever;

/**
 *
 * @author remembermewhy
 */
public class Request {

    public static String EOD_Name(final String ticker) {
        return Connector.PREFIX_EOD + "" + ticker;
    }
    
    public static String CLEAN_EOD = "DELETE FROM EOD";
    
    public static String INSERT_HISTORICAL_DATA ="insert into EOD(TICKER,DATE,OPENPRICE,HIGHPRICE,LOWPRICE,CLOSEPRICE,VOLUME,ADJ)"
            + "values(?,?,?,?,?,?,?,?)";
    
    public static String ADD_GOOG_STOCK = "INSERT INTO STOCK (TICKER, DESCRIPTION) VALUES"
            + "('GOOG','GOOGLE Inc.' )";
    
    public static String ADD_STOCK_DEF = "INSERT INTO STOCK (TICKER, DESCRIPTION) VALUES"
            + "(?, ?)";
    
    public static String GENERATE_STOCK_TABLE = "CREATE TABLE STOCK (\n"
            + "  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
            + "  TICKER varchar(100) NOT NULL,\n"
            + "  DESCRIPTION varchar(50) NOT NULL,\n"
            + "  PRIMARY KEY (ID))";

    public static String GENERATE_EOD_MD_TABLE = "CREATE TABLE EOD ("
                + "  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + "  TICKER VARCHAR(10) NOT NULL,"
                + "  DATE DATE NOT NULL,"
                + "  OPENPRICE DOUBLE,"
                + "  HIGHPRICE DOUBLE,"
                + "  LOWPRICE DOUBLE,"
                + "  CLOSEPRICE DOUBLE,"
                + "  VOLUME INT,"
                + "  ADJ DOUBLE,"
                + "  PRIMARY KEY (ID)"
                + " )";
    /**
     * This is not actually all stock ;). just a big list of stock.
     */
    public static String ALL_STOCK = "INSERT INTO STOCK (NAME, YAHOO_NAME) VALUES\n"
            + "	('Accor Paris', 'AC.PA'),\n"
            + "	('Air Liquide', '419919.PA'),\n"
            + "	('Alcatel-Lucent', 'ALU.PA'),\n"
            + "	('Alstom', 'ALO.PA'),\n"
            + "	('ArcelorMittal', 'MT.PA'),\n"
            + "	('AXA', 'CS.PA'),\n"
            + "	('BNP Paribas', 'BNP.PA'),\n"
            + "	('Bouygues', 'EN.PA'),\n"
            + "	('Capgemini', 'CAP.PA'),\n"
            + "	('Carrefour', 'CA.PA'),\n"
            + "	('Crédit Agricole', 'ACA.PA'),\n"
            + "	('EADS', 'EAD.PA'),\n"
            + "	('EDF', 'EDF.PA'),\n"
            + "	('Essilor', 'EI.PA'),\n"
            + "	('France Télécom', 'FTE.PA'),\n"
            + "	('GDF Suez', 'GSZ.PA'),\n"
            + "	('Groupe Danone', 'BN.PA'),\n"
            + "	('L''Oréal', 'OR.PA'),\n"
            + "	('Lafarge', 'LG.PA'),\n"
            + "	('Legrand', 'LR.PA'),\n"
            + "	('LVMH', 'MC.PA'),\n"
            + "	('Michelin', 'ML.PA'),\n"
            + "	('Pernod Ricard', 'RI.PA'),\n"
            + "	('PPR', 'PP.PA'),\n"
            + "	('Publicis', 'PUB.PA'),\n"
            + "	('Renault', 'RNO.PA'),\n"
            + "	('Safran', 'SAF.PA'),\n"
            + "	('Saint-Gobain', 'SGO.PA'),\n"
            + "	('Sanofi', 'SAN.PA'),\n"
            + "	('Schneider Electric', 'SU.PA'),\n"
            + "	('Société Générale', 'GLE.PA'),\n"
            + "	('Solvay', 'SOLB.BR'),\n"
            + "	('STMicroelectronics', 'STM.PA'),\n"
            + "	('Technip', 'TEC.PA'),\n"
            + "	('Total', 'FP.PA'),\n"
            + "	('Unibail-Rodamco', 'UL.PA'),\n"
            + "	('Vallourec', 'VK.PA'),\n"
            + "	('Veolia Environnement', 'VIE.PA'),\n"
            + "	('Vinci', 'DG.PA'),\n"
            + "	('Vivendi', 'VIV.PA'),\n"
            + "	('Adidas', 'ADS.DE'),\n"
            + "	('Allianz', 'ALV.DE'),\n"
            + "	('BASF', 'BAS.DE'),\n"
            + "	('Bayer', 'BAYN.DE'),\n"
            + "	('Beiersdorf', 'BEI.DE'),\n"
            + "	('BMW', 'BMW.DE'),\n"
            + "	('Commerzbank', 'CBK.DE'),\n"
            + "	('Daimler', 'DAI.DE'),\n"
            + "	('Deutsche Bank', 'DBK.DE'),\n"
            + "	('Deutsche Börse', 'DB1.DE'),\n"
            + "	('Deutsche Lufthansa', 'LHA.DE'),\n"
            + "	('Deutsche Post', 'DPW.DE'),\n"
            + "	('Deutsche Telekom', 'DTE.DE'),\n"
            + "	('E.ON', 'EOAN.DE'),\n"
            + "	('Fresenius', 'FRE.DE'),\n"
            + "	('Fresenius Medical Care', 'FME.DE'),\n"
            + "	('HeidelbergCement', 'HEI.DE'),\n"
            + "	('Henkel', 'HEN3.DE'),\n"
            + "	('Infineon Technologies', 'IFX.DE'),\n"
            + "	('K+S', 'SDF.DE'),\n"
            + "	('Linde', 'LIN.DE'),\n"
            + "	('MAN', 'MAN.DE'),\n"
            + "	('Merck', 'MRK.DE'),\n"
            + "	('Metro', 'MEO.DE'),\n"
            + "	('Munich Re', 'MUV2.DE'),\n"
            + "	('RWE', 'RWE.DE'),\n"
            + "	('SAP', 'SAP.DE'),\n"
            + "	('Siemens', 'SIE.DE'),\n"
            + "	('ThyssenKrupp', 'TKA.DE'),\n"
            + "	('Volkswagen Group', 'VOW3.DE'),\n"
            + "	('GOOGLE Inc.', 'GOOG'),\n"
            + "	('Activision Blizzard, Inc.', 'ATVI'),\n"
            + "	('ACTIVISION BLIZZARD', 'ATVI'),\n"
            + "	('ADOBE SYSTEMS INC', 'ADBE'),\n"
            + "	('AKAMAI TECHNOLOGIES', 'AKAM'),\n"
            + "	('ALEXION PHARM INC', 'ALXN'),\n"
            + "	('ALTERA CORP', 'ALTR'),\n"
            + "	('AMAZON.COM INC', 'AMZN'),\n"
            + "	('AMGEN', 'AMGN'),\n"
            + "	('ANALOG DEVICES CMN', 'ADI'),\n"
            + "	('APPLE INC', 'AAPL'),\n"
            + "	('APPLIED MATERIALS', 'AMAT'),\n"
            + "	('AUTODESK INC', 'ADSK'),\n"
            + "	('AUTOMATIC DATA PROCS', 'ADP'),\n"
            + "	('AVAGO TECHNOLOGIES L', 'AVGO'),\n"
            + "	('BAIDU, INC.', 'BIDU'),\n"
            + "	('BED BATH & BEYOND', 'BBBY'),\n"
            + "	('BIOGEN IDEC INC', 'BIIB'),\n"
            + "	('BMC SOFTWARE INC', 'BMC'),\n"
            + "	('BROADCOM CORP', 'BRCM'),\n"
            + "	('C.H. ROBINSON WW', 'CHRW'),\n"
            + "	('CA INC', 'CA'),\n"
            + "	('CATAMARAN CORPORATIO', 'CTRX'),\n"
            + "	('CELGENE CORP', 'CELG'),\n"
            + "	('CERNER CORP', 'CERN'),\n"
            + "	('CHECK POINT SOFTWARE', 'CHKP'),\n"
            + "	('CISCO SYSTEMS INC', 'CSCO'),\n"
            + "	('CITRIX SYSTEMS INC', 'CTXS'),\n"
            + "	('COGNIZANT TECH SOL', 'CTSH'),\n"
            + "	('COMCAST CORP A', 'CMCSA'),\n"
            + "	('COSTCO WHOLESALE', 'COST'),\n"
            + "	('DELL INC', 'DELL'),\n"
            + "	('DENTSPLY INTL INC', 'XRAY'),\n"
            + "	('DIRECTV', 'DTV'),\n"
            + "	('DISCOVERY COMM A', 'DISCA'),\n"
            + "	('DOLLAR TREE INC', 'DLTR'),\n"
            + "	('EBAY INC.', 'EBAY'),\n"
            + "	('EQUINIX, INC.', 'EQIX'),\n"
            + "	('EXPEDIA, INC. NEW', 'EXPE'),\n"
            + "	('EXPEDITORS INTL', 'EXPD'),\n"
            + "	('EXPRESS SCRIPTS', 'ESRX'),\n"
            + "	('F5 NETWORKS, INC.', 'FFIV'),\n"
            + "	('FACEBOOK INC', 'FB'),\n"
            + "	('FASTENAL CO', 'FAST'),\n"
            + "	('FISERV, INC.', 'FISV'),\n"
            + "	('FOSSIL INC', 'FOSL'),\n"
            + "	('GARMIN LTD', 'GRMN'),\n"
            + "	('GILEAD SCIENCES, INC', 'GILD'),\n"
            + "	('GOOGLE INC.', 'GOOG'),\n"
            + "	('HENRY SCHEIN, INC.', 'HSIC'),\n"
            + "	('INTEL CORP', 'INTC'),\n"
            + "	('INTUIT INC', 'INTU'),\n"
            + "	('INTUITIVE SURG, INC.', 'ISRG'),\n"
            + "	('K L A-TENCOR CORP', 'KLAC'),\n"
            + "	('LIBERTY GLOBAL CL-A', 'LBTYA'),\n"
            + "	('LIBERTY INTATV SRS A', 'LINTA'),\n"
            + "	('LIBERTY MEDIA CORP A', 'LMCA'),\n"
            + "	('LIFE TECHNOLOGIES', 'LIFE'),\n"
            + "	('LINEAR TECHNOLOGY', 'LLTC'),\n"
            + "	('MATTEL INC', 'MAT'),\n"
            + "	('MAXIM INTEGRATED', 'MXIM'),\n"
            + "	('MICROCHIP TECHNOLOGY', 'MCHP'),\n"
            + "	('MICRON TECHNOLOGY', 'MU'),\n"
            + "	('MICROSOFT CORP', 'MSFT'),\n"
            + "	('MONDELEZ INTL CMN A', 'MDLZ'),\n"
            + "	('MONSTER BEVERAGE CP', 'MNST'),\n"
            + "	('MYLAN INC', 'MYL'),\n"
            + "	('NETAPP, INC.', 'NTAP'),\n"
            + "	('NEWS CORP CL A', 'NWSA'),\n"
            + "	('NUANCE COMMUNICATNS', 'NUAN'),\n"
            + "	('NVIDIA CORPORATION', 'NVDA'),\n"
            + "	('O''REILLY AUTOMOTIVE', 'ORLY'),\n"
            + "	('ORACLE CORPORATION', 'ORCL'),\n"
            + "	('PACCAR INC.', 'PCAR'),\n"
            + "	('PAYCHEX, INC.', 'PAYX'),\n"
            + "	('PERRIGO COMPANY', 'PRGO'),\n"
            + "	('PRICELINE.COM INC', 'PCLN'),\n"
            + "	('QUALCOMM INC', 'QCOM'),\n"
            + "	('RANDGOLD RES LTD', 'GOLD'),\n"
            + "	('REGENERON PHARMACEUT', 'REGN'),\n"
            + "	('ROSS STORES, INC.', 'ROST'),\n"
            + "	('SANDISK CORPORATION', 'SNDK'),\n"
            + "	('SBA COMMUNICATIONS', 'SBAC'),\n"
            + "	('SEAGATE TECHNOLOGY', 'STX'),\n"
            + "	('SEARS HLDGS CORP', 'SHLD'),\n"
            + "	('SIGMA ALDRICH CORP', 'SIAL'),\n"
            + "	('SIRIUS XM RADIO INC.', 'SIRI'),\n"
            + "	('STAPLES, INC.', 'SPLS'),\n"
            + "	('STARBUCKS CORP', 'SBUX'),\n"
            + "	('STERICYCLE, INC.', 'SRCL'),\n"
            + "	('SYMANTEC CORPORATION', 'SYMC'),\n"
            + "	('TEXAS INSTRUMENTS', 'TXN'),\n"
            + "	('VERISK ANALYTICS INC', 'VRSK'),\n"
            + "	('VERTEX PHARMACEUTIC', 'VRTX'),\n"
            + "	('VIACOM INC CL B', 'VIAB'),\n"
            + "	('VIRGIN MEDIA INC', 'VMED'),\n"
            + "	('VODAFONE GROUP PLC', 'VOD'),\n"
            + "	('WESTERN DIGITAL CP', 'WDC'),\n"
            + "	('WHOLE FOODS MARKET', 'WFM'),\n"
            + "	('WYNN RESORTS LIMITED', 'WYNN'),\n"
            + "	('XILINX, INC.', 'XLNX'),\n"
            + "	('YAHOO! INC.', 'YHOO'),\n"
            + "	('XILINX, INC.', 'XLNX')";
}
