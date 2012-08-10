package org.rememberme.yahoo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.rememberme.yahoo.client.StockClient;
import org.rememberme.yahoo.client.Client;

public class Server implements Runnable {

	List<Client> clients;
	private ServerSocket serverSocket = null;

	public Server() {
		clients = new ArrayList<Client>(100);

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
				
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

		}

	}

}
