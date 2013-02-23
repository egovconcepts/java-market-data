package org.rememberme.yahoo;

import static org.junit.Assert.*;

import org.junit.Test;

public class StockManagerTest {

//	@Test
	public void testAddStock() {
		StockManager manager = new StockManager();
		assertEquals(0, manager.getPreviousStocks().size());
		
		Stock stock = new Stock();
		stock.parse("\"AC.PA\",\"ACCOR\",26.955,517,800,26.975");
		boolean isNewStock = manager.addStockInDB(stock);
		
		assertEquals(1, manager.getPreviousStocks().size());
		assertTrue(isNewStock);
		
		stock = new Stock();
		stock.parse("\"AC.PA\",\"ACCOR\",26.955,517,800,26.975");
		isNewStock = manager.addStockInDB(stock);
		
		assertFalse(isNewStock);
		
		assertEquals(1, manager.getPreviousStocks().size());
	}

}
