package org.md.retriever;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.md.retriever.stock.InputYahooEODHistorical;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.stock.YahooEODStock;
import org.md.retriever.stockmanager.EODStockManager;

/**
 * Retrieve Stock Information from the Yahoo Finance WebSite.
 *
 * @author remembermewhy.
 */
public class DataRetriever {

    private static Logger log = Logger.getLogger("DataRetreiver");

    private Connector connector;

    public DataRetriever() {
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
    }

    /**
     * Retrieve a stock Definition from the Yahoo Website.
     * Using the URL : http://finance.yahoo.com/d/quotes.csv?s=TICKER&f=sn
     * @param ticker
     * @return 
     */
    public SingleStockDef retrieveStockDef(String ticker) {
        
        SingleStockDef def = new SingleStockDef();
        URL url;
        BufferedReader in = null;
        try {
            log.info("requesting http://finance.yahoo.com/d/quotes.csv?s="+ticker+"&f=sn");
            url = new URL("http://finance.yahoo.com/d/quotes.csv?s="+ticker+"&f=sn");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = in.readLine();

            log.info("received " + inputLine);

            String[] splitted = inputLine.split(",");
            String description = splitted[1];
            
            def.setTicker(ticker);
            def.setDefinition(description.replaceAll("\"", ""));
            
        } catch (MalformedURLException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                log.error(ex);
            }
        }

        return def;
    }

    /**
     * 
     * 
     * @param stockDefs
     * @throws IOException 
     */
    public void processEODStockData(List<SingleStockDef> stockDefs) throws IOException {

        List<InputYahooEODHistorical> inputHistoricals = new ArrayList<>();
        List<EODStockManager> stockManagers = new ArrayList<>();

        List<String> tickers = new ArrayList<>(stockDefs.size());

        for (SingleStockDef ssd : stockDefs) {
            tickers.add(ssd.getTicker());
        }

//      Load the EOD from YAHOO    
        for (String ticker : tickers) {
            InputYahooEODHistorical historical = retrieveHistoricalEODStockData(ticker);
            if (historical != null) {
                inputHistoricals.add(historical);
            }
        }

//      Transform EOD into intermediary objects. ( generate a stockManager by ticker ).
        for (InputYahooEODHistorical historical : inputHistoricals) {
            log.info("generate Stock for : " + historical);
            List<String> EODs = historical.getEODs();
            EODStockManager stockManager = new EODStockManager(historical.getTicker());
            for (String eod : EODs) {
                stockManager.addStock(eod);
            }
            stockManagers.add(stockManager);
        }

//      Insert data into the database.
        for (EODStockManager stockManager : stockManagers) {
            log.info("serialize stock : " + stockManager.getTicker());
            List<YahooEODStock> stocks = stockManager.getStocks();
            for (YahooEODStock stock : stocks) {
                connector.insertEOD(stock);
            }
        }
            log.info("EOD data retreived ... ");
    }

    /**
     * Method to be call to start the EOD retrieval and serialization of stock.
     * data.
     *
     * @throws java.io.IOException
     */
    @Deprecated
    public void processEODStockData() throws IOException {
        List<SingleStockDef> stockDefs = connector.loadStockDefDB();
        processEODStockData(stockDefs);
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
        List<SingleStockDef> stockDefs = connector.loadStockDefDB();

        List<String> tickers = new ArrayList<>(stockDefs.size());

        for (SingleStockDef def : stockDefs) {
            tickers.add(def.getTicker());
        }

        if (tickers == null) {
            throw new RuntimeException("No Stock to be loaded from DB");
        }

        return tickersFromList(tickers);
    }

}
