package org.rememberme.retreiver.stock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author remembermewhy
 */
public class YahooEODStock implements Comparable<YahooEODStock> {

    private static final Logger Log = Logger.getLogger(YahooEODStock.class);

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

    @Override
    public String toString() {
        return ticker + " -- Date : " + date + " -- high : " + high + " - low : " + low + " - volume : " + volume;
    }

    private final DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

}
