package org.rememberme.retreiver.server;

import java.util.ArrayList;
import java.util.List;

public class ClientManager<E extends Client<M>,M> {

	List<E> clients = null;
	
	public ClientManager() {
		clients = new ArrayList<E>();
	}
	
	public synchronized void addClient(E e){
		clients.add(e);
	}
	
	public synchronized void removeClient(E e){
		clients.remove(e);
	}
	
	public synchronized void updateAllClients(M m){
		for(E e : clients){
			e.update(m);
		}
	}
	
}
