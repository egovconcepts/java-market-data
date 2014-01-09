package org.rememberme.retreiver.stock;

/**
 *
 * @author remembermewy
 */
public class InputYahooRTData {
    
    private String yahooString;
    private long timestamp;

    public InputYahooRTData(String yahooString, long timestamp) {
        this.yahooString = yahooString;
        this.timestamp = timestamp;
    }

    public String getYahooString() {
        return yahooString;
    }

    public void setYahooString(String yahooString) {
        this.yahooString = yahooString;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    
    
    
    
}
