package org.rememberme.yahoo-md-retreiver;


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
import org.rememberme.yahoo.Stock;

public class Connector {

    private Connection conn = null;
    private String url;
    private String dbName;
    private String tableName;
    private final String driver = "com.mysql.jdbc.Driver";
    private String userName;
    private String password;
    private PreparedStatement insertStock;
    private static final Logger log = Logger.getLogger(Connector.class);

    public Connector() {
    }

    public Connector(String dbServer,
            String dbPort,
            String dbName,
            String tableName,
            String userName,
            String password) {
        super();
        this.url = "jdbc:mysql://" + dbServer + ":" + dbPort + "/";
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
        this.tableName = tableName;
    }

    public void init() throws SQLException {
        log.info("Connect to " + url);
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager
                    .getConnection(url + dbName, userName, password);
            log.info("Connected to the database");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String request = "insert into " + tableName + " (DATE,TIME,STOCK_NAME,STOCK_DES,BBID,BQTY,AQTY,BASK,LAST_TRADE_DATE,LAST_TRADE_TIME) values (?,?,?,?,?,?,?,?,?,?)";
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
}
