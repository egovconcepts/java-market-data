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
	private StockManager stockManager;
	
	public DataRetreiver() {
		BasicConfigurator.configure();
		stockManager = new StockManager();
	}

	public void init() throws SQLException, IOException, InterruptedException{
		connector.init();
		
		while (true) {
			Thread.sleep(1000);
			
			url = new URL(
		"http://finance.yahoo.com/d/quotes.csv?s="+cac40()+"&f=snb3b6a5b2");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				Stock stock = new Stock();
				stock.parse(inputLine);
				boolean isNewStock = stockManager.addStock(stock);
				if(isNewStock) {
					connector.insert_market_data(stock);
					log.info("NEW " + stock);
				}else{
					log.info("OLD " + stock);
				}
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
		
		if(args.length < 5){
			log.info("usage : server port dbname dblogin dbpwd");
			return ;
		}
		
		String server = args[0];
		String port = args[1];
		String dbName = args[2];
		String tableName = args[3];
		String dblogin = args[4];
		String dbPwd = args[5];
		
		Connector connector = new Connector(server,port,dbName,tableName,dblogin,dbPwd);
		dr.setConnector(connector);
		
		
		log.info(server + " " + port + " " + dbName + " " + dblogin + " " + dbPwd);
		
		dr.init();
	}
	
	private String cac40(){
//		http://en.wikipedia.org/wiki/CAC_40
		StringBuilder sb = new StringBuilder();
		sb.append("AC.PA").append("+") 				// Accor Paris
			.append("419919.PA").append("+")		// Air Liquide Paris
			.append("ALU.PA").append("+")			// Alcatel Lucent
			.append("ALO.PA").append("+")			// Alstom SA
			.append("MT.PA").append("+")			// Arcelor Mittal
			.append("CS.PA").append("+")			// Axa
			.append("BNP.PA").append("+")			// BNP
			.append("EN.PA").append("+")			// Bouygue
			.append("CAP.PA").append("+")			// Cap Gemini
			.append("CA.PA").append("+")			// Carrefour
			.append("ACA.PA").append("+")			
			.append("EAD.PA").append("+")			
			.append("EDF.PA").append("+")			
			.append("EI.PA").append("+")			
			.append("FTE.PA").append("+")			
			.append("GSZ.PA").append("+")			
			.append("BN.PA").append("+")			
			.append("OR.PA").append("+")			
			.append("LG.PA").append("+")			
			.append("LR.PA").append("+")			
			.append("MC.PA").append("+")			
			.append("ML.PA").append("+")			
			.append("ML.PA").append("+")			
			.append("RI.PA").append("+")			
			.append("UG.PA").append("+")			
			.append("PP.PA").append("+")			
			.append("PUB.PA").append("+")			
			.append("RNO.PA").append("+")			
			.append("SAF.PA").append("+")			
			.append("SGO.PA").append("+")			
			.append("SAN.PA").append("+")			
			.append("SU.PA").append("+")			
			.append("GLE.PA").append("+")			
			.append("STM.PA").append("+")			
			.append("TEC.PA").append("+")			
			.append("FP.PA").append("+")			
			.append("UL.PA").append("+")			
			.append("VK.PA").append("+")			
			.append("VIE.PA").append("+")			
			.append("DG.PA").append("+")			
			.append("VIV.PA").append("+");
//			.append("^FCHI");			
			
		return sb.toString();
	}
}
