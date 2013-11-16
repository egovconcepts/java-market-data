package org.rememberme.retreiver.stock;

import java.util.Date;

/**
 * 
 * @author remembermewhy
 */
public class YahooRTStock implements Comparable<YahooRTStock> {

    private String name;
    private String description;
    private double bbid;
    private long qbid;
    private long qask;
    private double bask;
    private Date d;
    private String lastTradeDate;
    private String lastTradeTime;

    public YahooRTStock() {
    }

    public YahooRTStock(String name, String description, double bbid, long qbid, long qask, double bask, String lastTradeDate, String lastTradeTime) {
        this.name = name;
        this.description = description;
        this.bbid = bbid;
        this.qbid = qbid;
        this.qask = qask;
        this.bask = bask;
        this.lastTradeDate = lastTradeDate;
        this.lastTradeTime = lastTradeTime;
    }

    @Override
    public String toString() {
        return name + " " + description + " " + qbid + " " + bbid + " " + bask + " " + qask + " " + lastTradeDate + " " + lastTradeTime;
    }

    public double getBask() {
        return bask;
    }

    public double getBbid() {
        return bbid;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public long getQask() {
        return qask;
    }

    public long getQbid() {
        return qbid;
    }

    public Date getD() {
        return d;
    }

    public String getLastTradeDate() {
        return lastTradeDate;
    }

    public String getLastTradeTime() {
        return lastTradeTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBbid(double bbid) {
        this.bbid = bbid;
    }

    public void setQbid(long qbid) {
        this.qbid = qbid;
    }

    public void setQask(long qask) {
        this.qask = qask;
    }

    public void setBask(double bask) {
        this.bask = bask;
    }

    public void setD(Date d) {
        this.d = d;
    }

    public void setLastTradeDate(String lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public void setLastTradeTime(String lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    @Override
    public boolean equals(Object o) {
        YahooRTStock stock = (YahooRTStock) o;

        if (!name.equalsIgnoreCase(stock.name)) {
            return false;
        }
        if (bbid != stock.bbid) {
            return false;
        }
        if (qbid != stock.qbid) {
            return false;
        }
        if (qask != stock.qask) {
            return false;
        }
        if (bask != stock.bask) {
            return false;
        }

        if (!lastTradeDate.equalsIgnoreCase(stock.lastTradeDate)) {
            return false;
        }

        if (!lastTradeTime.equalsIgnoreCase(stock.lastTradeTime)) {
            return false;
        }

        return true;
    }

    public int compareTo(YahooRTStock o) {

        if (!name.equalsIgnoreCase(o.name)) {
            return name.compareTo(o.name);
        }

        if (bbid < o.bbid) {
            return -1;
        } else if (bbid > o.bbid) {
            return 1;
        }

        if (qbid < o.qbid) {
            return -1;
        } else if (qbid > o.qbid) {
            return 1;
        }

        if (qask < o.qask) {
            return -1;
        } else if (qask > o.qask) {
            return 1;
        }

        if (bask < o.bask) {
            return -1;
        } else if (bask > o.bask) {
            return 1;
        }

//        TODO change to compare the difference of date
        if (!lastTradeDate.equalsIgnoreCase(o.lastTradeDate)) {
            return lastTradeDate.compareTo(o.lastTradeDate);
        }

//        TODO change to compare the difference of time
        if (!lastTradeTime.equalsIgnoreCase(o.lastTradeTime)) {
            return lastTradeTime.compareTo(o.lastTradeTime);
        }

        return 0;
    }
}
