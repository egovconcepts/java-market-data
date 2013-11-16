package org.rememberme.retreiver;

import org.rememberme.retreiver.stock.YahooRTStock;
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
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.rememberme.retreiver.stock.YahooEODStock;
import org.rememberme.util.Utils;

/**
 *
 * @author remembermewhy
 */
public class Connector {

    private static final Logger log = Logger.getLogger(Connector.class);
    public static final String PREFIX_EOD = "EOD_";

    private Connection connection = null;
    private final String url = "jdbc:derby:YAHOO;create=true";
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    private PreparedStatement insertRTStock;
    public PreparedStatement insertEODtock;

    private Statement statement;

    public void init() throws SQLException, ClassNotFoundException {
        log.info("Connect to " + url);
        Class.forName(driver);
        connection = DriverManager.getConnection(url);
        log.info("Connected to the database");
        statement = connection.createStatement();
//        insertRTStock = connection.prepareStatement(Request.INSERT_RT_DATA);
//        insertEODtock = connection.prepareStatement(Request.INSERT_HISTORICAL_DATA);
    }

    /**
     * Execute random query.
     *
     * @param query
     */
    public final void executeQuery(final String query) {
        try {
            statement.execute(query);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Execute the ADD_STOCK query.
     */
    public final void addStock() {
        try {
            statement.execute(Request.ADD_STOCK);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Create the stock table.
     */
    public final void generateStockTable() {
        try {
            statement.execute(Request.GENERATE_STOCK_TABLE);
        } catch (SQLException sqle) {
            log.error("Stock table already exist");
//            sqle.printStackTrace();
        }
    }

    /**
     * Create the market data table.
     */
    public final void generateRTMarketDataTable() {
        try {
            statement.execute(Request.GENERATE_RT_MD_TABLE);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Create an EOD database.
     *
     * @param ticker
     */
    public final void CREATE_EOD_DB() {

        final String request = "CREATE TABLE EOD ("
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

        try {
            statement.execute(request);
        } catch (SQLException sqle) {
            System.out.println(sqle);
//            sqle.printStackTrace();
        }
        try {
            insertEODtock = connection.prepareStatement(Request.INSERT_HISTORICAL_DATA);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    /**
     * Load Stocks from the database.
     *
     * @return
     */
    public List<String> loadStockDB() {
        String request = "SELECT YAHOO_NAME FROM STOCK";
        List<String> result = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                String ticker = rs.getString("YAHOO_NAME");
                result.add(ticker);
            }
        } catch (SQLException sqle) {
            return null;
        }
        return result;
    }

    private final DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void insertMarketData(YahooEODStock stock) {
        try {
            insertEODtock.setString(1, stock.getTicker());

            try {
                insertEODtock.setDate(2, new java.sql.Date(EODDateFormat.parse(stock.getDate()).getTime()));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

            insertEODtock.setDouble(3, stock.getOpen());
            insertEODtock.setDouble(4, stock.getHigh());
            insertEODtock.setDouble(5, stock.getLow());
            insertEODtock.setDouble(6, stock.getClose());
            insertEODtock.setInt(7, stock.getVolume());
            insertEODtock.setDouble(8, stock.getAdj());

            insertEODtock.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    private final DateFormat lastTradeDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final DateFormat lastTradeTimeFormat = new SimpleDateFormat("HH:mm");

    /**
     *
     * @param stock
     * @param timeStamp
     */
    public void insert_market_data(YahooRTStock stock, long timeStamp) {
        java.util.Date d = new java.util.Date(timeStamp);
        try {
            insertRTStock.setDate(1, new Date(d.getTime()));
            insertRTStock.setTime(2, new Time(d.getTime()));
            insertRTStock.setString(3, stock.getName());
            insertRTStock.setString(4, stock.getDescription());
            insertRTStock.setDouble(5, stock.getBbid());
            insertRTStock.setLong(6, stock.getQbid());
            insertRTStock.setLong(7, stock.getQask());
            insertRTStock.setDouble(8, stock.getBask());

            try {
                insertRTStock.setDate(9, new java.sql.Date(lastTradeDateFormat.parse(stock.getLastTradeDate()).getTime()));
            } catch (ParseException ex) {
                insertRTStock.setDate(9, null);
            }

            try {
                insertRTStock.setTime(10, new java.sql.Time(lastTradeTimeFormat.parse(Utils.removeAmPm(stock.getLastTradeTime())).getTime()));
            } catch (ParseException ex) {
                insertRTStock.setDate(10, null);
            }
            insertRTStock.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
