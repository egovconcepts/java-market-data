package org.rememberme.retreiver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Connector {

    private static final Logger log = Logger.getLogger(Connector.class);
    
    private Connection conn = null;
    private String url;
    private String dbName;
    private String tableName;
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String userName;
    private String password;
    private PreparedStatement insertStock;
    private PreparedStatement insertRawData;
    private Statement statement;
    

//    public Connector() {
//    }
    public Connector( //            String dbServer,
            //            String dbPort,
            //            String dbName,
            //            String tableName
            //            String userName,
            //            String password
            ) {
        super();
        this.url = "jdbc:derby:YAHOO;create=true";
//        this.dbName = dbName;
//        this.userName = userName;
//        this.password = password;
//        this.tableName = tableName;
    }

    public void init() throws SQLException {
        log.info("Connect to " + url);
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);
            log.info("Connected to the database");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String generateStockTable = "CREATE TABLE STOCK (\n"
                + "  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + "  NAME varchar(100) NOT NULL,\n"
                + "  YAHOO_NAME varchar(50) NOT NULL,\n"
                + "  PRIMARY KEY (ID))";

        String generateMarketDataTable = "CREATE TABLE YAHOO_MARKET_DATA (\n"
                + "  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + "  DATE date NOT NULL,\n"
                + "  TIME time NOT NULL,\n"
                + "  STOCK_NAME varchar(30) DEFAULT NULL,\n"
                + "  STOCK_DES varchar(100) DEFAULT NULL,\n"
                + "  BQTY int NOT NULL,\n"
                + "  BBID double NOT NULL,\n"
                + "  BASK double DEFAULT NULL,\n"
                + "  AQTY int NOT NULL,\n"
                + "  LAST_TRADE_DATE date DEFAULT NULL,\n"
                + "  LAST_TRADE_TIME time DEFAULT NULL,\n"
                + "  PRIMARY KEY (ID))";

        String addStock = "INSERT INTO STOCK (NAME, YAHOO_NAME) VALUES"
                + "('Accor Paris', 'AC.PA'),"
                + "('GOOGLE Inc.', 'GOOG')";
        
        statement = conn.createStatement();
        
        try {
            statement.execute(generateStockTable);
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        
        try {
            statement.execute(generateMarketDataTable);
        } catch (SQLException sqle) {
            System.out.println(sqle );
        }
        
        try {
//            statement.execute(addStock);
            statement.execute(allStock);
        } catch (SQLException sqle) {
            System.out.println(sqle );
        }
        

        String request = "insert into YAHOO_MARKET_DATA (DATE,TIME,STOCK_NAME,STOCK_DES,BBID,BQTY,AQTY,BASK,LAST_TRADE_DATE,LAST_TRADE_TIME) values (?,?,?,?,?,?,?,?,?,?)";
        insertStock = conn.prepareStatement(request);

    }

    public List<String> loadStockDB() {
        String request = "SELECT YAHOO_NAME FROM STOCK";
        List<String> result = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                String yahooTicker = rs.getString("YAHOO_NAME");
                result.add(yahooTicker);
            }
        } catch (SQLException e) {
            return null;
        }
        return result;
    }

    private final DateFormat lastTradeDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final DateFormat lastTradeTimeFormat = new SimpleDateFormat("HH:mm");

    public void insert_market_data(Stock stock, long timeStamp) {
        java.util.Date d = new java.util.Date(timeStamp);
        try {
            insertStock.setDate(1, new Date(d.getTime()));
            insertStock.setTime(2, new Time(d.getTime()));
            insertStock.setString(3, stock.getName());
            insertStock.setString(4, stock.getDescription());
            insertStock.setDouble(5, stock.getBbid());
            insertStock.setLong(6, stock.getQbid());
            insertStock.setLong(7, stock.getQask());
            insertStock.setDouble(8, stock.getBask());

            try {
                insertStock.setDate(9, new java.sql.Date(lastTradeDateFormat.parse(stock.getLastTradeDate()).getTime()));
            } catch (ParseException ex) {
                insertStock.setDate(9, null);
            }

            try {
                insertStock.setTime(10, new java.sql.Time(lastTradeTimeFormat.parse(removeAmPm(stock.getLastTradeTime())).getTime()));
            } catch (ParseException ex) {
                insertStock.setDate(10, null);
            }
            insertStock.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String removeAmPm(String time) {
        if (time.contains("pm")) {
            time = time.replaceAll("pm", "");
            String[] tmp = time.split(":");
            int t = Integer.parseInt(tmp[0]) + 12;
            return t + ":" + tmp[1];
        } else {
            return time.replaceAll("am", "");
        }
    }

    public Connection getConn() {
        return conn;
    }
    
    private String allStock = "INSERT INTO STOCK (NAME, YAHOO_NAME) VALUES\n" +
"	('Accor Paris', 'AC.PA'),\n" +
"	('Air Liquide', '419919.PA'),\n" +
"	('Alcatel-Lucent', 'ALU.PA'),\n" +
"	('Alstom', 'ALO.PA'),\n" +
"	('ArcelorMittal', 'MT.PA'),\n" +
"	('AXA', 'CS.PA'),\n" +
"	('BNP Paribas', 'BNP.PA'),\n" +
"	('Bouygues', 'EN.PA'),\n" +
"	('Capgemini', 'CAP.PA'),\n" +
"	('Carrefour', 'CA.PA'),\n" +
"	('Crédit Agricole', 'ACA.PA'),\n" +
"	('EADS', 'EAD.PA'),\n" +
"	('EDF', 'EDF.PA'),\n" +
"	('Essilor', 'EI.PA'),\n" +
"	('France Télécom', 'FTE.PA'),\n" +
"	('GDF Suez', 'GSZ.PA'),\n" +
"	('Groupe Danone', 'BN.PA'),\n" +
"	('L''Oréal', 'OR.PA'),\n" +
"	('Lafarge', 'LG.PA'),\n" +
"	('Legrand', 'LR.PA'),\n" +
"	('LVMH', 'MC.PA'),\n" +
"	('Michelin', 'ML.PA'),\n" +
"	('Pernod Ricard', 'RI.PA'),\n" +
"	('PPR', 'PP.PA'),\n" +
"	('Publicis', 'PUB.PA'),\n" +
"	('Renault', 'RNO.PA'),\n" +
"	('Safran', 'SAF.PA'),\n" +
"	('Saint-Gobain', 'SGO.PA'),\n" +
"	('Sanofi', 'SAN.PA'),\n" +
"	('Schneider Electric', 'SU.PA'),\n" +
"	('Société Générale', 'GLE.PA'),\n" +
"	('Solvay', 'SOLB.BR'),\n" +
"	('STMicroelectronics', 'STM.PA'),\n" +
"	('Technip', 'TEC.PA'),\n" +
"	('Total', 'FP.PA'),\n" +
"	('Unibail-Rodamco', 'UL.PA'),\n" +
"	('Vallourec', 'VK.PA'),\n" +
"	('Veolia Environnement', 'VIE.PA'),\n" +
"	('Vinci', 'DG.PA'),\n" +
"	('Vivendi', 'VIV.PA'),\n" +
"	('Adidas', 'ADS.DE'),\n" +
"	('Allianz', 'ALV.DE'),\n" +
"	('BASF', 'BAS.DE'),\n" +
"	('Bayer', 'BAYN.DE'),\n" +
"	('Beiersdorf', 'BEI.DE'),\n" +
"	('BMW', 'BMW.DE'),\n" +
"	('Commerzbank', 'CBK.DE'),\n" +
"	('Daimler', 'DAI.DE'),\n" +
"	('Deutsche Bank', 'DBK.DE'),\n" +
"	('Deutsche Börse', 'DB1.DE'),\n" +
"	('Deutsche Lufthansa', 'LHA.DE'),\n" +
"	('Deutsche Post', 'DPW.DE'),\n" +
"	('Deutsche Telekom', 'DTE.DE'),\n" +
"	('E.ON', 'EOAN.DE'),\n" +
"	('Fresenius', 'FRE.DE'),\n" +
"	('Fresenius Medical Care', 'FME.DE'),\n" +
"	('HeidelbergCement', 'HEI.DE'),\n" +
"	('Henkel', 'HEN3.DE'),\n" +
"	('Infineon Technologies', 'IFX.DE'),\n" +
"	('K+S', 'SDF.DE'),\n" +
"	('Linde', 'LIN.DE'),\n" +
"	('MAN', 'MAN.DE'),\n" +
"	('Merck', 'MRK.DE'),\n" +
"	('Metro', 'MEO.DE'),\n" +
"	('Munich Re', 'MUV2.DE'),\n" +
"	('RWE', 'RWE.DE'),\n" +
"	('SAP', 'SAP.DE'),\n" +
"	('Siemens', 'SIE.DE'),\n" +
"	('ThyssenKrupp', 'TKA.DE'),\n" +
"	('Volkswagen Group', 'VOW3.DE'),\n" +
"	('GOOGLE Inc.', 'GOOG'),\n" +
"	('Activision Blizzard, Inc.', 'ATVI'),\n" +
"	('ACTIVISION BLIZZARD', 'ATVI'),\n" +
"	('ADOBE SYSTEMS INC', 'ADBE'),\n" +
"	('AKAMAI TECHNOLOGIES', 'AKAM'),\n" +
"	('ALEXION PHARM INC', 'ALXN'),\n" +
"	('ALTERA CORP', 'ALTR'),\n" +
"	('AMAZON.COM INC', 'AMZN'),\n" +
"	('AMGEN', 'AMGN'),\n" +
"	('ANALOG DEVICES CMN', 'ADI'),\n" +
"	('APPLE INC', 'AAPL'),\n" +
"	('APPLIED MATERIALS', 'AMAT'),\n" +
"	('AUTODESK INC', 'ADSK'),\n" +
"	('AUTOMATIC DATA PROCS', 'ADP'),\n" +
"	('AVAGO TECHNOLOGIES L', 'AVGO'),\n" +
"	('BAIDU, INC.', 'BIDU'),\n" +
"	('BED BATH & BEYOND', 'BBBY'),\n" +
"	('BIOGEN IDEC INC', 'BIIB'),\n" +
"	('BMC SOFTWARE INC', 'BMC'),\n" +
"	('BROADCOM CORP', 'BRCM'),\n" +
"	('C.H. ROBINSON WW', 'CHRW'),\n" +
"	('CA INC', 'CA'),\n" +
"	('CATAMARAN CORPORATIO', 'CTRX'),\n" +
"	('CELGENE CORP', 'CELG'),\n" +
"	('CERNER CORP', 'CERN'),\n" +
"	('CHECK POINT SOFTWARE', 'CHKP'),\n" +
"	('CISCO SYSTEMS INC', 'CSCO'),\n" +
"	('CITRIX SYSTEMS INC', 'CTXS'),\n" +
"	('COGNIZANT TECH SOL', 'CTSH'),\n" +
"	('COMCAST CORP A', 'CMCSA'),\n" +
"	('COSTCO WHOLESALE', 'COST'),\n" +
"	('DELL INC', 'DELL'),\n" +
"	('DENTSPLY INTL INC', 'XRAY'),\n" +
"	('DIRECTV', 'DTV'),\n" +
"	('DISCOVERY COMM A', 'DISCA'),\n" +
"	('DOLLAR TREE INC', 'DLTR'),\n" +
"	('EBAY INC.', 'EBAY'),\n" +
"	('EQUINIX, INC.', 'EQIX'),\n" +
"	('EXPEDIA, INC. NEW', 'EXPE'),\n" +
"	('EXPEDITORS INTL', 'EXPD'),\n" +
"	('EXPRESS SCRIPTS', 'ESRX'),\n" +
"	('F5 NETWORKS, INC.', 'FFIV'),\n" +
"	('FACEBOOK INC', 'FB'),\n" +
"	('FASTENAL CO', 'FAST'),\n" +
"	('FISERV, INC.', 'FISV'),\n" +
"	('FOSSIL INC', 'FOSL'),\n" +
"	('GARMIN LTD', 'GRMN'),\n" +
"	('GILEAD SCIENCES, INC', 'GILD'),\n" +
"	('GOOGLE INC.', 'GOOG'),\n" +
"	('HENRY SCHEIN, INC.', 'HSIC'),\n" +
"	('INTEL CORP', 'INTC'),\n" +
"	('INTUIT INC', 'INTU'),\n" +
"	('INTUITIVE SURG, INC.', 'ISRG'),\n" +
"	('K L A-TENCOR CORP', 'KLAC'),\n" +
"	('LIBERTY GLOBAL CL-A', 'LBTYA'),\n" +
"	('LIBERTY INTATV SRS A', 'LINTA'),\n" +
"	('LIBERTY MEDIA CORP A', 'LMCA'),\n" +
"	('LIFE TECHNOLOGIES', 'LIFE'),\n" +
"	('LINEAR TECHNOLOGY', 'LLTC'),\n" +
"	('MATTEL INC', 'MAT'),\n" +
"	('MAXIM INTEGRATED', 'MXIM'),\n" +
"	('MICROCHIP TECHNOLOGY', 'MCHP'),\n" +
"	('MICRON TECHNOLOGY', 'MU'),\n" +
"	('MICROSOFT CORP', 'MSFT'),\n" +
"	('MONDELEZ INTL CMN A', 'MDLZ'),\n" +
"	('MONSTER BEVERAGE CP', 'MNST'),\n" +
"	('MYLAN INC', 'MYL'),\n" +
"	('NETAPP, INC.', 'NTAP'),\n" +
"	('NEWS CORP CL A', 'NWSA'),\n" +
"	('NUANCE COMMUNICATNS', 'NUAN'),\n" +
"	('NVIDIA CORPORATION', 'NVDA'),\n" +
"	('O''REILLY AUTOMOTIVE', 'ORLY'),\n" +
"	('ORACLE CORPORATION', 'ORCL'),\n" +
"	('PACCAR INC.', 'PCAR'),\n" +
"	('PAYCHEX, INC.', 'PAYX'),\n" +
"	('PERRIGO COMPANY', 'PRGO'),\n" +
"	('PRICELINE.COM INC', 'PCLN'),\n" +
"	('QUALCOMM INC', 'QCOM'),\n" +
"	('RANDGOLD RES LTD', 'GOLD'),\n" +
"	('REGENERON PHARMACEUT', 'REGN'),\n" +
"	('ROSS STORES, INC.', 'ROST'),\n" +
"	('SANDISK CORPORATION', 'SNDK'),\n" +
"	('SBA COMMUNICATIONS', 'SBAC'),\n" +
"	('SEAGATE TECHNOLOGY', 'STX'),\n" +
"	('SEARS HLDGS CORP', 'SHLD'),\n" +
"	('SIGMA ALDRICH CORP', 'SIAL'),\n" +
"	('SIRIUS XM RADIO INC.', 'SIRI'),\n" +
"	('STAPLES, INC.', 'SPLS'),\n" +
"	('STARBUCKS CORP', 'SBUX'),\n" +
"	('STERICYCLE, INC.', 'SRCL'),\n" +
"	('SYMANTEC CORPORATION', 'SYMC'),\n" +
"	('TEXAS INSTRUMENTS', 'TXN'),\n" +
"	('VERISK ANALYTICS INC', 'VRSK'),\n" +
"	('VERTEX PHARMACEUTIC', 'VRTX'),\n" +
"	('VIACOM INC CL B', 'VIAB'),\n" +
"	('VIRGIN MEDIA INC', 'VMED'),\n" +
"	('VODAFONE GROUP PLC', 'VOD'),\n" +
"	('WESTERN DIGITAL CP', 'WDC'),\n" +
"	('WHOLE FOODS MARKET', 'WFM'),\n" +
"	('WYNN RESORTS LIMITED', 'WYNN'),\n" +
"	('XILINX, INC.', 'XLNX'),\n" +
"	('YAHOO! INC.', 'YHOO'),\n" +
"	('XILINX, INC.', 'XLNX')";
    
}
