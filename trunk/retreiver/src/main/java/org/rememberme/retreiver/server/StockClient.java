package org.rememberme.retreiver.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import org.rememberme.retreiver.Stock;

/**
 * not thread safe ..
 * @author rememberme
 *
 */
public class StockClient extends Observable implements Client<Stock>{

	
	private Socket socket;
	private List<Stock> stocks;
	
	private OutputStream outputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	
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
		try {
			outputStream = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Stock stock) {
		if(stocks.contains(stock)){
			if(socket!=null && socket.isConnected()){
				try {
					objectOutputStream.writeObject(stock);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
