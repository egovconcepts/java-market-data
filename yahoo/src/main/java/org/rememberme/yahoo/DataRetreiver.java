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

	public static void main(String[] args) throws IOException,
			InterruptedException, SQLException {
		BasicConfigurator.configure();
		
		Connector connector = new Connector();
		connector.init();
		
		while (true) {
			Thread.sleep(2000);

			URL oracle = new URL(
					"http://finance.yahoo.com/d/quotes.csv?s=GOOG&f=snb2a5b6b3");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null){
//				log.info("received " + inputLine);
				Stock stock = new Stock();
				stock.parse(inputLine);
				log.info(stock);
				connector.insert_market_data(stock);
			}
			
			in.close();

		}
	}
}
