package org.rememberme.retreiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DataRetreiver {

    private static Logger log = Logger.getLogger("DataRetreiver");
    private URL url;
    private Connector connector;
    StockManager stockManager;

    public DataRetreiver() {
        BasicConfigurator.configure();
    }

    public void init() throws SQLException, IOException, InterruptedException {
        connector.init();
        stockManager = new StockManager();
        String yahooTicker = loadYahooTicker();
        List<InputYahooData> loInputYahooDatas;

        while (true) {
            loInputYahooDatas = new LinkedList<>();
            url = new URL(
                    "http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2d1t1");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                loInputYahooDatas.add(new InputYahooData(inputLine, System.currentTimeMillis()));
            }

            in.close();

            for (InputYahooData istr : loInputYahooDatas) {
                serializeStock(istr);
            }

            Thread.sleep(1000);
        }
    }

    private void serializeStock(InputYahooData yahooData) {
        Stock stock = null;
        boolean serializedFlag = false;

        if (countNumberOfComma(yahooData.getYahooString()) != 7) {
            
            log.debug("Not processed because the number of comma <> 7");
            return;

        } else {
            log.debug("build from stream");
            stock = new Stock();
            stock.parse(yahooData.getYahooString());
            serializedFlag = true;
        }

        boolean toBeAddedStock = stockManager.addStockInDB(stock);
        if (toBeAddedStock) {
            connector.insert_market_data(stock, yahooData.getTimestamp());
            connector.insert_raw_data(yahooData.getYahooString(),yahooData.getTimestamp(),serializedFlag);
            log.info("Add " + stock);
        } else {
            log.debug("AddNot " + stock);
        }
    }

    private int countNumberOfComma(String inputStream) {
        String[] st = inputStream.split(",");
        return st.length - 1;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    private String loadYahooTicker() {
        List<String> yahooTickers = connector.loadStockDB();

        if (yahooTickers == null) {
            throw new RuntimeException("No Stock to be loaded from DB");
        }

        StringBuilder sb = new StringBuilder();
        for (String yahooTicker : yahooTickers) {
            sb.append(yahooTicker).append("+");
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException,
            InterruptedException, SQLException {
        DataRetreiver dr = new DataRetreiver();

        if (args.length != 6) {
            log.info("usage : server port dbname tableName dblogin dbpwd");
            return;
        }

        String server = args[0];
        String port = args[1];
        String dbName = args[2];
        String tableName = args[3];
        String dblogin = args[4];
        String dbPwd = args[5];

        Connector connector = new Connector(server, port, dbName, tableName, dblogin, dbPwd);
        dr.setConnector(connector);

        log.info(server + " " + port + " " + dbName + " " + dblogin + " " + dbPwd);

        dr.init();
    }
}
