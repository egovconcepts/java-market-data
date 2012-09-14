package org.rememberme.yahoo;

import java.util.Date;

public class Stock implements Comparable<Stock>{

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
		
//		remove the " at the beginning and at the end of the name and the description fields.
		
		name = name.replaceAll("\"", "");
		description = description.replaceAll("\"", "");
		
		if("N/A".equalsIgnoreCase(splitted[2]))
			bbid = 0;
		else 
			bbid = Double.parseDouble(splitted[2]);
		
		if("N/A".equalsIgnoreCase(splitted[3]))
			qbid = 0;
		else 
			qbid = Integer.parseInt(splitted[3]);
		
		if("N/A".equalsIgnoreCase(splitted[4]))
			qask = 0;
		else
			qask = Integer.parseInt(splitted[4]);
		
		if("N/A".equalsIgnoreCase(splitted[5]))
			bask = 0;
		else
			bask = Double.parseDouble(splitted[5]);
	}
	
	@Override
	public String toString() {
		return name + " " + description + " " + qbid  + " " + bbid + " " + bask + " " + qask ; 
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

	@Override
	public boolean equals(Object o) {
		Stock stock = (Stock) o;
		
		if(!name.equalsIgnoreCase(stock.name)) return false;
		if(bbid != stock.bbid) return false;
		if(qbid != stock.qbid) return false;
		if(qask != stock.qask) return false;
		if(bask != stock.bask) return false;
		
		return true;
	}
	
	public int compareTo(Stock o) {
		
		if(!name.equalsIgnoreCase(o.name))
			return name.compareTo(o.name);
		
		if(bbid < o.bbid) 		return -1;
		else if(bbid > o.bbid) 	return 1;

		if(qbid < o.qbid)		return -1;
		else if (qbid > o.qbid) return 1;
		
		if(qask < o.qask)		return -1;
		else if (qask > o.qask) return 1;
		
		if(bask < o.bask) 		return -1;
		else if(bask > o.bask)	return 1;
		
		return 0;
	}
}
