package org.rememberme.retreiver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.rememberme.retreiver.Stock;

public class Server implements Runnable {

	ClientManager<StockClient, Stock> clientManager = null; 
	private ServerSocket serverSocket = null;

	public Server() {
		clientManager = new ClientManager<StockClient, Stock>();
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}
	}

	public void run() {
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				StockClient client = new StockClient();
				client.setSocket(clientSocket);
				clientManager.addClient(client);
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		}
	}
}
