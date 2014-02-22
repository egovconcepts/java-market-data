package org.md.retriever.stock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author remembermewhy
 */
public class YahooEODStock implements Comparable<YahooEODStock>,Cloneable {

    private static final Logger Log = Logger.getLogger(YahooEODStock.class);

    private String ticker;

    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;
    private double adj;

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

    public void copy(YahooEODStock stock){
        this.setTicker(stock.ticker);
        this.setDate(stock.date);
        this.setOpen(stock.open);
        this.setHigh(stock.high);
        this.setLow(stock.low);
        this.setClose(stock.close);
        this.setVolume(stock.volume);
        this.setAdj(stock.adj);
    }

    @Override
    public String toString() {
        return ticker + " -- Date : " + date + " -- high : " + high + " - low : " + low + " - volume : " + volume;
    }

    private DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean equals(Object o) {
        YahooEODStock t = (YahooEODStock) o;
        Date thisDate = null;
        Date tDate = null;
        boolean result = false;
        try {
            thisDate = EODDateFormat.parse(date);
            tDate = EODDateFormat.parse(t.date);
            result = thisDate.compareTo(tDate) == 0;
        } catch (ParseException ex) {
            Log.error("Date parsing issue", ex);
        }
        return result;
    }

    @Override
    public int compareTo(YahooEODStock t) {
        Date thisDate = null;
        Date tDate = null;
        int result = 0;

        try {
            thisDate = EODDateFormat.parse(date);
            tDate = EODDateFormat.parse(t.date);
            result = thisDate.compareTo(tDate);
        } catch (ParseException ex) {
            Log.error("Date parsing issue", ex);
        }

        return result;
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

    /**
     * @param ticker the ticker to set
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     * @param low the low to set
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     * @param close the close to set
     */
    public void setClose(double close) {
        this.close = close;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * @param adj the adj to set
     */
    public void setAdj(double adj) {
        this.adj = adj;
    }

    /**
     * @param EODDateFormat the EODDateFormat to set
     */
    public void setEODDateFormat(DateFormat EODDateFormat) {
        this.EODDateFormat = EODDateFormat;
    }

}
