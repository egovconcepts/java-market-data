package org.rememberme.yahoo.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.rememberme.yahoo.Stock;


/**
 * not thread safe ..
 * @author rememberme
 *
 */
public class StockClient implements Client {

	private Socket socket;
	private List<Stock> stocks;
	
	
	public StockClient() {
		stocks = new LinkedList<Stock>();
	}
	
	public void cleanStocks(){
		this.stocks.clear();
	}
	
	public void removeStock(Stock stock){
		stocks.remove(stock);
	}
	
	public void addOneStock(Stock stock){
		this.stocks.add(stock);
	}
	
	public void addMultipleStocks(List<Stock> stocks) {
		this.stocks.addAll(stocks);
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
