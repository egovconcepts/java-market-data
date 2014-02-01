package org.md.gui.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;
import org.md.retriever.Connector;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.DataRetriever;
import org.md.retriever.stock.InputYahooEODHistorical;
import org.md.retriever.stock.YahooEODStock;
import org.md.retriever.stockmanager.EODStockManager;

/**
 *
 * @author remembermewhy
 */
public class ProcessEODSingleStockService extends Service {

    private static Logger log = Logger.getLogger(ProcessEODSingleStockService.class);

//    private final SimpleObjectProperty<DataRetriever> retriever = new SimpleObjectProperty<>();
    private final DataRetriever retriever;
    private final Connector connector;
    private SingleStockDef singleStockDef;

    public ProcessEODSingleStockService(Connector connector,
            DataRetriever retriever1){
        this.connector = connector;
        this.retriever = retriever1;
    }
    
    public void setSingleStockDef(SingleStockDef singleStockDef) {
        this.singleStockDef = singleStockDef;
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                updateMessage("Start");

                String ticker = singleStockDef.getTicker();

                EODStockManager stockManager = new EODStockManager(ticker);
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
                    updateMessage(inputLine);
                }

                int size = historical.getEODs().size() * 2;
                int progress = 0;
                updateProgress(0, size);
                updateMessage("Building internal representation");

                List<String> EODs = historical.getEODs();

                for (String eod : EODs) {
                    stockManager.addStock(eod);
                    updateProgress(++progress, size);
                }

                List<YahooEODStock> stocks = stockManager.getStocks();
                for (YahooEODStock stock : stocks) {
                    updateMessage("Serializing : " + ticker + " " + stock.getClose() + " " + stock.getDate());
                    updateProgress(++progress, size);
                    connector.insertEOD(stock);
                }

//                InputYahooEODHistorical historical = retrieveHistoricalEODStockData(singleStockDef.getTicker());
//                List<SingleStockDef> ssds = new ArrayList<>();
//                ssds.add(singleStockDef);
//                retriever.get().processEODStockData(ssds);
                updateMessage("End of process");
                return null;
            }
        };
    }

}
