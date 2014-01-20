package org.md.retriever;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.md.retreiver.stock.SingleStockDef;
import org.md.retreiver.stock.YahooEODStock;

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
    private PreparedStatement insertEODtock;
    private PreparedStatement insertStockDef;
//    private PreparedStatement loadEODStock;

    private Statement statement;

    public void init() throws SQLException, ClassNotFoundException {
        log.info("Connect to " + url);
        Class.forName(driver);
        connection = DriverManager.getConnection(url);
        log.info("Connected to the database");
        statement = connection.createStatement();

        try {
            insertStockDef = connection.prepareStatement(Request.ADD_STOCK_DEF);
        } catch (SQLSyntaxErrorException sqlsee) {
            generateStockTable();
            generateEODMarketDataTable();
            insertStockDef = connection.prepareStatement(Request.ADD_STOCK_DEF);
        }

        insertEODtock = connection.prepareStatement(Request.INSERT_HISTORICAL_DATA);
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
            log.error("sql exception");
        }
    }

    /**
     * Insert a random stock in the database.
     *
     * insert a new stock definition
     *
     * @param ticker
     * @param def
     * @throws java.sql.SQLException
     */
    public final void addStockDef(String ticker, String def) throws SQLException {
        insertStockDef.setString(1, ticker);
        insertStockDef.setString(2, def);
        insertStockDef.execute();
    }

    /**
     * Execute the ADD_GOOG_STOCK query.
     */
    public final void addGOOGStock() {
        try {
            statement.execute(Request.ADD_GOOG_STOCK);
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
        }
    }

    /**
     * Create an EOD database. generateRTMarketDataTable
     *
     */
    public final void generateEODMarketDataTable() {

        try {
            statement.execute(Request.GENERATE_EOD_MD_TABLE);
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }

    }

    public final void cleanEODDB() {
        try {
            log.info("clean EOD database");
            statement.execute(Request.CLEAN_EOD);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Load Stocks from the database.
     *
     * @return
     */
    public List<SingleStockDef> loadStockDB() {
        String request = "SELECT * FROM STOCK";
        List<SingleStockDef> result = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                String ticker = rs.getString("TICKER");
                String definition = rs.getString("DESCRIPTION");
                SingleStockDef ssd = new SingleStockDef(ticker, definition);
                result.add(ssd);
            }
        } catch (SQLException sqle) {
            return null;
        }
        return result;
    }

    /**
     *
     * @param ticker
     * @return
     * @throws SQLException
     */
    public List<YahooEODStock> LOAD_HISTORICAL_STOCK(final String ticker) throws SQLException {
        List<YahooEODStock> historicalStock = new ArrayList<>();

        String request = "SELECT * FROM EOD WHERE TICKER LIKE '" + ticker+"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(request);

        while (rs.next()) {
            Date date = rs.getDate("Date");
            double open = rs.getDouble("OPENPRICE");
            double high = rs.getDouble("HIGHPRICE");
            double low = rs.getDouble("LOWPRICE");
            double close = rs.getDouble("CLOSEPRICE");
            int volume = rs.getInt("VOLUME");
            double adj = rs.getDouble("ADJ");

            String dateStr = EODDateFormat.format(date);

            YahooEODStock stock = new YahooEODStock(ticker, dateStr, open, high, low, close, volume, adj);
            log.debug("load EOD : " + stock);
            historicalStock.add(stock);
        }

        return historicalStock;
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


    public Connection getConnection() {
        return connection;
    }

}
