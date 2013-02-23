package org.rememberme.yahoo-md-retreiver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StockManager {

    private List<Stock> previousStocks;

    public StockManager() {
        previousStocks = new ArrayList<>();
    }

    /*
     * @return true if the stock should be serialized.
     */
    public synchronized boolean addStockInDB(Stock stock) {
        boolean result = true;
        if (previousStocks.contains(stock)) {
            result = false;
        } else {

            Stock previous = null;
            for (Stock st : previousStocks) {
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
    private Comparator<Stock> compareStockByName = new CompareStockByName();

    private class CompareStockByName implements Comparator<Stock> {

        public int compare(Stock o1, Stock o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public List<Stock> getPreviousStocks() {
        return previousStocks;
    }
}
