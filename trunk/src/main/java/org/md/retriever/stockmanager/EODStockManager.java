package org.md.retriever.stockmanager;

import java.util.ArrayList;
import java.util.List;
import org.md.retriever.stock.YahooEODStock;

/**
 * @author remembermewhy
 */
public class EODStockManager {

    final String ticker;
    private final List<YahooEODStock> stocks;

    public EODStockManager(final String ticker) {
        this.ticker = ticker;
        stocks = new ArrayList<>();
    }

    public void addStock(YahooEODStock stock) {
        stocks.add(stock);
    }

    public void addStock(String yahooInput) {
        YahooEODStock stock = generateStock(yahooInput);
        stocks.add(stock);
    }

    /**
     * Generate a Stock from the network String.
     *
     * @param yahooInput
     * @return
     */
    public YahooEODStock generateStock(String yahooInput) {
        String[] splitted = yahooInput.split(",");

        String date = splitted[0];
        double open = Double.valueOf(splitted[1]);
        double high = Double.valueOf(splitted[2]);
        double low = Double.valueOf(splitted[3]);
        double close = Double.valueOf(splitted[4]);
        int volume = Integer.valueOf(splitted[5]);
        double adj = Double.valueOf(splitted[6]);

        YahooEODStock stock = new YahooEODStock(ticker, date, open, high, low, close, volume, adj);

        return stock;
    }

    public List<YahooEODStock> getStocks() {
        return stocks;
    }

    public String getTicker() {
        return ticker;
    }

}
