package org.rememberme.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fred
 */
public class DataSerializerManager implements Runnable {

    private BlockingQueue<InputYahooData> concurrentInputStrings;
    private StockManager stockManager;
    private ExecutorService service;
    private Connector connector;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("DataSerializerManager");
    
    public DataSerializerManager(BlockingQueue<InputYahooData> concurrentInputStrings) {
        this.concurrentInputStrings = concurrentInputStrings;
        stockManager = new StockManager();
        int cores = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(cores - 1); // one left for the main thread ...
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void run() {
        while (true) {
            try {
                InputYahooData inputString = concurrentInputStrings.take();
                service.execute(new Handler(inputString,stockManager,connector));
            } catch (InterruptedException ex) {
                Logger.getLogger(DataSerializerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static class Handler implements Runnable {

        private InputYahooData inputString;
        private StockManager stockManager;
        private Connector connector;

        public Handler(InputYahooData inputYahooData,StockManager stockManager,Connector connector) {
            this.inputString = inputYahooData;
            this.stockManager = stockManager;
            this.connector = connector;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " " + inputString.getYahooString());
            add(inputString);
        }

        private void add(InputYahooData inputYahooData) {
            Stock stock = null;
            if (countNumberOfComma(inputYahooData.getYahooString()) != 7) {
                log.debug("buildFromScratch");
                String ticker = inputYahooData.getYahooString().split(",")[0];

                try {
                    stock = buildStockFromScratch(ticker);
                } catch (IOException ioe) {
                    log.debug("buildFromScratch of " + ticker + " failed ");
                }

            } else {
                log.debug("build from stream");
                stock = new Stock();
                stock.parse(inputYahooData.getYahooString());
            }

            boolean toBeAddedStock = stockManager.addStockInDB(stock);
            if (toBeAddedStock) {
                connector.insert_market_data(stock, inputYahooData.getTimestamp());
                log.info("Add " + stock);
            } else {
                log.debug("AddNot " + stock);
            }
        }

        private int countNumberOfComma(String inputStream) {
            String[] st = inputStream.split(",");
            return st.length - 1;
        }

        private Stock buildStockFromScratch(String yahooTicker) throws MalformedURLException, IOException {
            String name = yahooTicker;
            String description = grabIndividualInfo(yahooTicker, "n");

            String bbidStr = grabIndividualInfo(yahooTicker, "b3");
            bbidStr = bbidStr.replaceAll(",", "");
            double bbid = 0;
            if (!"N/A".equalsIgnoreCase(bbidStr)) {
                bbid = Double.parseDouble(bbidStr);
            }

            String qbidStr = grabIndividualInfo(yahooTicker, "b6");
            qbidStr = qbidStr.replaceAll(",", "");
            long qbid = 0;
            if (!"N/A".equalsIgnoreCase(qbidStr)) {
                qbid = Long.parseLong(qbidStr);
            }

            String qaskStr = grabIndividualInfo(yahooTicker, "a5");
            qaskStr = qaskStr.replaceAll(",", "");
            long qask = 0;
            if (!"N/A".equalsIgnoreCase(qaskStr)) {
                qask = Long.parseLong(qaskStr);
            }

            String baskStr = grabIndividualInfo(yahooTicker, "b2");
            baskStr = baskStr.replaceAll(",", "");
            double bask = 0;
            if (!"N/A".equalsIgnoreCase(baskStr)) {
                bask = Double.parseDouble(baskStr);
            }

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
            in.close();

            return inputLine;
        }
    }
}
