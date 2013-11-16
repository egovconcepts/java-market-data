package org.rememberme.yahoo;

import static org.junit.Assert.*;

import org.rememberme.retreiver.stock.YahooRTStock;
import org.rememberme.retreiver.stockmanager.RTStockManager;

public class StockManagerTest {

//	@Test
	public void testAddStock() {
		RTStockManager manager = new RTStockManager();
		assertEquals(0, manager.getPreviousStocks().size());
		
		YahooRTStock stock = manager.generateStock("\"AC.PA\",\"ACCOR\",26.955,517,800,26.975");
		boolean isNewStock = manager.addStockInDB(stock);
		
		assertEquals(1, manager.getPreviousStocks().size());
		assertTrue(isNewStock);
		
		stock = manager.generateStock("\"AC.PA\",\"ACCOR\",26.955,517,800,26.975");
		isNewStock = manager.addStockInDB(stock);
		
		assertFalse(isNewStock);
		
		assertEquals(1, manager.getPreviousStocks().size());
	}

}
