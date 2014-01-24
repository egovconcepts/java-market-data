package org.md.gui.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.md.retriever.stock.YahooEODStock;

/**
 *
 * @author remembermewhy
 */
public class YahooEODStockModel {

    private final SimpleStringProperty ticker = new SimpleStringProperty();

    private final SimpleStringProperty date = new SimpleStringProperty();
    private final SimpleDoubleProperty open = new SimpleDoubleProperty();
    private final SimpleDoubleProperty high = new SimpleDoubleProperty();
    private final SimpleDoubleProperty low = new SimpleDoubleProperty();
    private final SimpleDoubleProperty close = new SimpleDoubleProperty();
    private final SimpleIntegerProperty volume = new SimpleIntegerProperty();
    private final SimpleDoubleProperty adj = new SimpleDoubleProperty();

    public YahooEODStockModel(String ticker, String date, double open,
            double high, double low, double close,
            int volume, double adj) {
        this.ticker.set(ticker);
        this.date.set(date);
        this.open.set(open);
        this.high.set(high);
        this.low.set(low);
        this.close.set(close);
        this.volume.set(volume);
        this.adj.set(adj);
    }
    
    public YahooEODStockModel(YahooEODStock stock) {
        this.ticker.set(stock.getTicker());
        this.date.set(stock.getDate());
        this.open.set(stock.getOpen());
        this.high.set(stock.getHigh());
        this.low.set(stock.getLow());
        this.close.set(stock.getClose());
        this.volume.set(stock.getVolume());
        this.adj.set(stock.getAdj());
    }

    /**
     * @return the ticker
     */
    public String getTicker() {
        return ticker.get();
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date.get();
    }

    /**
     * @return the open
     */
    public Double getOpen() {
        return open.get();
    }

    /**
     * @return the high
     */
    public Double getHigh() {
        return high.get();
    }

    /**
     * @return the low
     */
    public Double getLow() {
        return low.get();
    }

    /**
     * @return the close
     */
    public Double getClose() {
        return close.get();
    }

    /**
     * @return the volume
     */
    public Integer getVolume() {
        return volume.get();
    }

    /**
     * @return the adj
     */
    public Double getAdj() {
        return adj.get();
    }
    
}
