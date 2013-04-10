package org.rememberme.retreiver;

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
            log.debug(Thread.currentThread().getName() + " " + inputString.getYahooString());
            add(inputString);
        }

        private void add(InputYahooData inputYahooData) {
            Stock stock = null;

            if (countNumberOfComma(inputYahooData.getYahooString()) != 7) {
                log.debug("Not processed because the number of comma <> 7");
                return;

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

    }
}
