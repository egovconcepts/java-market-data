package org.rememberme.yahoo;

import java.util.Date;

public class Stock {

	private String name;
	private String description;
	private double bbid;
	private long qbid;
	private long qask;
	private double bask;
	private Date d;

	public Stock() {
	}
	
	public Stock(String name, String description, double bbid, long qbid,
			long qask, double bask) {
		super();
		this.name = name;
		this.description = description;
		this.bbid = bbid;
		this.qbid = qbid;
		this.qask = qask;
		this.bask = bask;
	}
	
	public void parse(String str){
		String [] splitted = str.split(",");
		name = splitted[0];
		description = splitted[1];
		bbid = Double.parseDouble(splitted[2]);
		qbid = Integer.parseInt(splitted[3]);
		qask = Integer.parseInt(splitted[4]);
		bask = Double.parseDouble(splitted[5]);
	}

	@Override
	public String toString() {
		return name + " " + description + " " + bbid + " " + qbid + " " + qask + " " + bask; 
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
}
