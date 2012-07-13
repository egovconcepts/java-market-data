package org.rememberme.yahoo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

public class Connector {

	Connection conn = null;
	String url = "jdbc:mysql://peipito.no-ip.org:3306/";
	String dbName = "YAHOO_MARKET_DATA";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "yahoo";
	String password = "yahoo";
	
	PreparedStatement insertStock;
	
	public Connector() {
	}

	public void init() throws SQLException{
		System.out.println("MySQL Connect Example.");
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			System.out.println("Connected to the database");
//			conn.close();
//			System.out.println("Disconnected from database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String request = "insert into YAHOO_MARKET_DATA (DATE,TIME,BBID,BQTY,AQTY,BAK) values (?,?,?,?,?,?)";
		insertStock = conn.prepareStatement(request);
	}
	
	public void insert_market_data(Stock stock){
		java.util.Date d = new java.util.Date();
		try {
			insertStock.setDate(1, new Date(d.getTime()));
			insertStock.setTime(2, new Time(d.getTime()));
			insertStock.setDouble(3, stock.getBbid());
			insertStock.setLong(4, stock.getQbid());
			insertStock.setLong(5,stock.getQask());
			insertStock.setDouble(6, stock.getBask());
			insertStock.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
