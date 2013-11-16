package org.rememberme.retriever;

import org.rememberme.retreiver.stock.InputYahooRTData;
import org.rememberme.retreiver.stock.YahooRTStock;
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
import org.rememberme.retreiver.stock.InputYahooEODHistorical;
import org.rememberme.retreiver.stock.YahooEODStock;
import org.rememberme.retreiver.stockmanager.EODStockManager;
import org.rememberme.retreiver.stockmanager.RTStockManager;

/**
 *
 * @author remembermewhy
 */
public class DataRetreiver {

    private static Logger log = Logger.getLogger("DataRetreiver");

    private Connector connector;
    private RTStockManager rstStockManager;

    public DataRetreiver() {
        BasicConfigurator.configure();
    }

    /**
     * Initialize the class.
     *
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public void init() throws SQLException, IOException, InterruptedException, ClassNotFoundException {
        connector.init();
        rstStockManager = new RTStockManager();
    }

    /**
     * Method to be call to start the EOD retrieval and serialization of stock
     * data.
     *
     * @throws java.io.IOException
     */
    public void processEODStockData() throws IOException {
        List<String> tickers = connector.loadStockDB();
        List<InputYahooEODHistorical> inputHistoricals = new ArrayList<>();
        List<EODStockManager> stockManagers = new ArrayList<>();

        for (String ticker : tickers) {
            InputYahooEODHistorical historical = retrieveHistoricalEODStockData(ticker);
            if (historical != null) {
                inputHistoricals.add(historical);
            }
        }

        for (InputYahooEODHistorical historical : inputHistoricals) {
            log.info("generate Stock for : " + historical);
            List<String> EODs = historical.getEODs();
            EODStockManager stockManager = new EODStockManager(historical.getTicker());
            for (String eod : EODs) {
                stockManager.addStock(eod);
            }
            stockManagers.add(stockManager);
        }

        for (EODStockManager stockManager : stockManagers) {
            log.info("serialize stock : " + stockManager.getTicker());
//            connector.CREATE_EOD_DB(stockManager.getTicker());
            List<YahooEODStock> stocks = stockManager.getStocks();
            for (YahooEODStock stock : stocks) {
                connector.insertMarketData(stock);
            }
        }

    }

    /**
     * Method to be call to start the real-time retrieval of stock data.
     * retrieve and serialize stock in real time.
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public void processRTStockData() throws InterruptedException, IOException {

        List<String> tickerList = connector.loadStockDB();
        List<InputYahooRTData> loInputYahooDatas = null;

        while (true) {

            try {
                loInputYahooDatas = retreiveRTStockData(tickerList);
            } catch (FileNotFoundException fnfe) {
                log.error("File not downloaded from yahoo -- going to sleep");
                Thread.sleep(5000);
                continue;
            }

            for (InputYahooRTData istr : loInputYahooDatas) {
                serializeRTStock(istr);
            }

            Thread.sleep(1000);
        }
    }

    /**
     * Retrieve the real-time information for multiple stock. This is an
     * intermediary step and should not be called outside this class.
     *
     * @param tickersList the list of ticker to be downloaded.
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
    private List<InputYahooRTData> retreiveRTStockData(List<String> tickersList)
            throws MalformedURLException, IOException, FileNotFoundException {

        String tickers = tickersFromList(tickersList);
        List<InputYahooRTData> loInputYahooDatas = new ArrayList<>();

        URL url = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + tickers + "&f=snb3b6a5b2d1t1");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            loInputYahooDatas.add(new InputYahooRTData(inputLine, System.currentTimeMillis()));
        }

        in.close();

        return loInputYahooDatas;
    }

    /**
     * Retrieve the real-time information for one single stock. Primarily used
     * for the GUI. visualize the information.
     *
     * @param ticker
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public InputYahooRTData retrieveRTStockData(String ticker)
            throws MalformedURLException, IOException, FileNotFoundException {

        URL url = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + ticker + "&f=snb3b6a5b2d1t1");
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        InputYahooRTData data = null;
        while ((inputLine = in.readLine()) != null) {
            data = new InputYahooRTData(inputLine, System.currentTimeMillis());
        }
        return data;
    }

    /**
     * Retrieve Historical EOD data for a single stock.
     *
     * @param ticker
     * @return the historical raw data.
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public InputYahooEODHistorical retrieveHistoricalEODStockData(String ticker)
            throws MalformedURLException, IOException {
        log.info("retrieve historical md for " + ticker);
        InputYahooEODHistorical historical = new InputYahooEODHistorical(ticker);

        URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + ticker);

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (java.io.FileNotFoundException fnfe) {
            log.info(fnfe);
            return null;
        }

        String inputLine;

        // the first line is the header.
        in.readLine();

        while ((inputLine = in.readLine()) != null) {
            historical.addInput(inputLine);
        }

        return historical;
    }

    /**
     * Serialize the real-time Information to the database
     *
     * @param yahooRawData
     */
    private void serializeRTStock(InputYahooRTData yahooRawData) {
        YahooRTStock stock = null;
        boolean serializedFlag = false;

        if (rstStockManager.countNumberOfComma(yahooRawData.getYahooString()) != 7) {

            log.debug("Not processed because the number of comma <> 7");
            return;

        } else {

            log.debug("build from stream");
            stock = rstStockManager.generateStock(yahooRawData.getYahooString());
            serializedFlag = true;
        }

        boolean toBeAddedStock = rstStockManager.addStockInDB(stock);

        if (toBeAddedStock) {
            connector.insert_market_data(stock, yahooRawData.getTimestamp());
            log.info("Add " + stock);
        } else {
            log.debug("AddNot " + stock);
        }

    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    /**
     * Generate a unique tickers String from a List of tickers.
     *
     * @param tickers
     * @return String, list of ticker.
     */
    private String tickersFromList(List<String> tickers) {
        StringBuilder sb = new StringBuilder();
        for (String yahooTicker : tickers) {
            sb.append(yahooTicker).append("+");
        }
        return sb.toString();
    }

    /**
     * Load tickers from the stock list in the DB and generate string of tickers
     *
     * @return
     */
    private String loadYahooTicker() {
        List<String> yahooTickers = connector.loadStockDB();

        if (yahooTickers == null) {
            throw new RuntimeException("No Stock to be loaded from DB");
        }
        return tickersFromList(yahooTickers);
    }

    public static void main(String[] args) throws IOException,
            InterruptedException, SQLException, ClassNotFoundException {

        DataRetreiver dr = new DataRetreiver();
        Connector connector = new Connector();
        dr.setConnector(connector);
        dr.init();
        connector.generateStockTable();
        connector.CREATE_EOD_DB();

//        connector.addStock();
        connector.executeQuery(Request.ALL_STOCK);
//        dr.processRTStockData();
        dr.processEODStockData();
    }
}
