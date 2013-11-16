package org.rememberme.retreiver.stock;

/**
 *
 * @author remembermewhy
 */
public class YahooEODStock {

    private final String ticker;
    
    private final String date;	
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final int volume;
    private final double adj;

    public YahooEODStock(String ticker, String date, double open, double high, 
            double low, double close, int volume, double adj) {
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adj = adj;
    }

    public String getTicker() {
        return ticker;
    }
    
    public String getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public int getVolume() {
        return volume;
    }

    public double getAdj() {
        return adj;
    }
    
}
