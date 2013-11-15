package org.rememberme.retreiver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
            loInputYahooDatas = new ArrayList<>();
            url = new URL(
                    "http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2d1t1");

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        url.openStream()));
            } catch (FileNotFoundException fnfe) {
                log.error("File not downloaded from yahoo going to sleep");
                Thread.sleep(5000);
                continue;
            }

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

    public InputYahooData retreiveStock(String yahooTicker) throws MalformedURLException, IOException, FileNotFoundException {
        URL url = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2d1t1");
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        InputYahooData data = null;
        while ((inputLine = in.readLine()) != null) {
            data = new InputYahooData(inputLine, System.currentTimeMillis());
        }
        return data;
    }

    private void serializeStock(InputYahooData yahooData) {
        Stock stock = null;
        boolean serializedFlag = false;

        if (stockManager.countNumberOfComma(yahooData.getYahooString()) != 7) {

            log.debug("Not processed because the number of comma <> 7");
            return;

        } else {
            
            log.debug("build from stream");
            stock = stockManager.generateStock(yahooData.getYahooString());
            serializedFlag = true;
        }

        boolean toBeAddedStock = stockManager.addStockInDB(stock);
        
        if (toBeAddedStock) {
            connector.insert_market_data(stock, yahooData.getTimestamp());
            log.info("Add " + stock);
        } else {
            log.debug("AddNot " + stock);
        }
        
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

//        if (args.length != 1) {
//            log.info("usage : database");
//            return;
//        }
//        String server = args[0];
//        String port = args[1];
//        String dbName = args[2];
//        String tableName = args[3];
//        String dblogin = args[4];
//        String dbPwd = args[5];
//        Connector connector = new Connector(server, port, dbName, tableName, dblogin, dbPwd);
        Connector connector = new Connector();
        dr.setConnector(connector);

        dr.init();
    }
}
