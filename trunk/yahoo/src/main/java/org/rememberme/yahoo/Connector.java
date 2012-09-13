package org.rememberme.yahoo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

import org.apache.log4j.Logger;

public class Connector {

	Connection conn = null;
	String url;
	String dbName;
	String tableName;
	String driver = "com.mysql.jdbc.Driver";
	String userName;
	String password;
	
	PreparedStatement insertStock;
	
	private static Logger log = Logger.getLogger(Connector.class);
	
	public Connector() {
	}

	public Connector(String dbServer, 
			String dbPort,
			String dbName, 
			String tableName,
			String userName, 
			String password) {
		super();
		this.url = "jdbc:mysql://" + dbServer + ":" +dbPort + "/";
		this.dbName = dbName;
		this.userName = userName;
		this.password = password;
		this.tableName = tableName;
	}

	public void init() throws SQLException{
		log.info("Connect to " + url);
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			log.info("Connected to the database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String request = "insert into "+ tableName +" (DATE,TIME,STOCK_NAME,STOCK_DES,BBID,BQTY,AQTY,BASK) values (?,?,?,?,?,?,?,?)";
		insertStock = conn.prepareStatement(request);
	}
	
	
	public void insert_market_data(Stock stock){
		java.util.Date d = new java.util.Date();
		try {
			insertStock.setDate(1, new Date(d.getTime()));
			insertStock.setTime(2, new Time(d.getTime()));
			insertStock.setString(3, stock.getName());
			insertStock.setString(4, stock.getDescription());
			insertStock.setDouble(5, stock.getBbid());
			insertStock.setLong(6, stock.getQbid());
			insertStock.setLong(7,stock.getQask());
			insertStock.setDouble(8, stock.getBask());
			insertStock.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
