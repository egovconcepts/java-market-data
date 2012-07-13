package org.rememberme.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DataRetreiver {

	private static Logger log = Logger.getLogger("DataRetreiver");

	private URL url;
	private Connector connector;
	
	public DataRetreiver() {
		BasicConfigurator.configure();
	}

	public void init() throws SQLException, IOException, InterruptedException{
//		Connector connector = new Connector();
		connector.init();
		
		while (true) {
			Thread.sleep(2000);
			
			url = new URL(
					"http://finance.yahoo.com/d/quotes.csv?s=GOOG&f=snb2a5b6b3");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				Stock stock = new Stock();
				stock.parse(inputLine);
				log.info(stock);
				connector.insert_market_data(stock);
			}
			
			in.close();
		}
		
	}
	
	public void setConnector(Connector connector) {
		this.connector = connector;
	}
	
	public static void main(String[] args) throws IOException,
			InterruptedException, SQLException {
		DataRetreiver dr = new DataRetreiver();
		
		String server = args[0];
		String port = args[1];
		String dbName = args[2];
		String dblogin = args[3];
		String dbPwd = args[4];
		
		Connector connector = new Connector(server,port,dbName,dblogin,dbPwd);
		dr.setConnector(connector);
		
		
		log.info(server + " " + port + " " + dbName + " " + dblogin + " " + dbPwd);
		
		dr.init();
	}
}
