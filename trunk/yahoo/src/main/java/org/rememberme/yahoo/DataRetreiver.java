package org.rememberme.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DataRetreiver {

    private static Logger log = Logger.getLogger("DataRetreiver");
    private URL url;
    private Connector connector;
    private StockManager stockManager;

    public DataRetreiver() {
        BasicConfigurator.configure();
        stockManager = new StockManager();
    }
    private String yahooTicker;

    public void init() throws SQLException, IOException, InterruptedException {
        connector.init();

        yahooTicker = loadYahooTicker();

        while (true) {
//			Thread.sleep(1000);
            url = new URL(
                    "http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2d1t1");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info(inputLine);
//                log.info(countNumberOfComma(inputLine));
                Stock stock = null;
                if (countNumberOfComma(inputLine) != 7) {
                    String ticker = inputLine.split(",")[0];

                    try {
                        stock = buildStockFromScratch(ticker);
                    } catch (IOException ioe) {
                        continue;
                    }

                } else {
                    stock = new Stock();
                    stock.parse(inputLine);
                }

                boolean isNewStock = stockManager.addStock(stock);
                if (isNewStock) {
                    connector.insert_market_data(stock);
                    log.info("NEW " + stock);
                } else {
                    log.debug("OLD " + stock);
                }
            }
            in.close();
        }
    }

    private Stock buildStockFromScratch(String yahooTicker) throws MalformedURLException, IOException {
//        log.error(yahooTicker);
//        "http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2");
        String name = yahooTicker;
        String description = grabIndividualInfo(yahooTicker, "n");
//        log.info("name " + name);

        String bbidStr = grabIndividualInfo(yahooTicker, "b3");
        bbidStr = bbidStr.replaceAll(",", "");
        double bbid = 0;
        if (!"N/A".equalsIgnoreCase(bbidStr)) {
            bbid = Double.parseDouble(bbidStr);
        }
//        log.info("bid " + bbid);

        String qbidStr = grabIndividualInfo(yahooTicker, "b6");
        qbidStr = qbidStr.replaceAll(",", "");
        long qbid = 0;
        if (!"N/A".equalsIgnoreCase(qbidStr)) {
            qbid = Long.parseLong(qbidStr);
        }

//        log.info("bidSize " + qbid);
        String qaskStr = grabIndividualInfo(yahooTicker, "a5");
        qaskStr = qaskStr.replaceAll(",", "");
        long qask = 0;
        if (!"N/A".equalsIgnoreCase(qaskStr)) {
            qask = Long.parseLong(qaskStr);
        }
//        log.info("askSize " + qask);

        String baskStr = grabIndividualInfo(yahooTicker, "b2");
        baskStr = baskStr.replaceAll(",", "");
        double bask = 0;
        if (!"N/A".equalsIgnoreCase(baskStr)) {
            bask = Double.parseDouble(baskStr);
        }
//        log.info("askSize " + bask);

        String lastTradeDate = grabIndividualInfo(yahooTicker, "d1");
        String lastTradeTime = grabIndividualInfo(yahooTicker, "t1");

        name = name.replaceAll("\"", "");
        description = description.replaceAll("\"", "");
        lastTradeDate = lastTradeDate.replaceAll("\"", "");
        lastTradeTime = lastTradeTime.replaceAll("\"", "");

        Stock stock = new Stock(name, description, bbid, qbid, qask, bask, lastTradeDate, lastTradeTime);
        return stock;
    }

    private String grabIndividualInfo(String ticker, String yahooSpecialTag) throws MalformedURLException, IOException {
        URL _url = new URL(
                "http://finance.yahoo.com/d/quotes.csv?s=" + ticker + "&f=" + yahooSpecialTag);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                _url.openStream()));

        String inputLine = in.readLine();
//        log.info(" -- " + inputLine);
        in.close();

//        return inputLine.split(",")[0];
        return inputLine;
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

        if (args.length < 5) {
            log.info("usage : server port dbname dblogin dbpwd");
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
