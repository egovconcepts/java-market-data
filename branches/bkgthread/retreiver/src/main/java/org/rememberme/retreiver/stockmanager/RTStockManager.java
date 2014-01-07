package org.rememberme.retreiver.stockmanager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.rememberme.retreiver.stock.YahooRTStock;

/**
 * 
 * @author remembermewhy
 */
public class RTStockManager {

    private List<YahooRTStock> previousStocks;

    public RTStockManager() {
        previousStocks = new ArrayList<>();
    }

    /*
     * @return true if the stock should be serialized.
     */
    public synchronized boolean addStockInDB(YahooRTStock stock) {
        boolean result = true;
        if (previousStocks.contains(stock)) {
            result = false;
        } else {

            YahooRTStock previous = null;
            for (YahooRTStock st : previousStocks) {
                if (0 == compareStockByName.compare(stock, st)) {
                    previous = st;
                }
            }

            if (previous == null) {
                result = false;
            } else {
                previousStocks.remove(previous);
            }

            previousStocks.add(stock);
        }
        return result;
    }
    private Comparator<YahooRTStock> compareStockByName = new CompareStockByName();

    public int countNumberOfComma(String inputStream) {
        String[] st = inputStream.split(",");
        return st.length - 1;
    }

    public YahooRTStock generateStock(String yahooInput) {
        YahooRTStock stock = new YahooRTStock();
        String[] splitted = yahooInput.split(",");
        stock.setName(splitted[0]);
        stock.setDescription(splitted[1]);
        stock.setName(stock.getName().replaceAll("\"", ""));
        stock.setDescription(stock.getDescription().replaceAll("\"", ""));
        if ("N/A".equalsIgnoreCase(splitted[2])) {
            stock.setBbid(0);
        } else {
            stock.setBbid(Double.parseDouble(splitted[2]));
        }
        if ("N/A".equalsIgnoreCase(splitted[3])) {
            stock.setQbid(0);
        } else {
            stock.setQbid(Integer.parseInt(splitted[3]));
        }
        if ("N/A".equalsIgnoreCase(splitted[4])) {
            stock.setQask(0);
        } else {
            stock.setQask(Integer.parseInt(splitted[4]));
        }
        if ("N/A".equalsIgnoreCase(splitted[5])) {
            stock.setBask(0);
        } else {
            stock.setBask(Double.parseDouble(splitted[5]));
        }
        stock.setLastTradeDate(splitted[6].replaceAll("\"", ""));
        stock.setLastTradeTime(splitted[7].replaceAll("\"", ""));
        return stock;
    }

    private class CompareStockByName implements Comparator<YahooRTStock> {

        public int compare(YahooRTStock o1, YahooRTStock o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public List<YahooRTStock> getPreviousStocks() {
        return previousStocks;
    }
}
