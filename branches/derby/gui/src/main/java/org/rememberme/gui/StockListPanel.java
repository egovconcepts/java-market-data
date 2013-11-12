package org.rememberme.gui;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import org.rememberme.gui.component.SimpleStockGUI;
import org.rememberme.gui.utils.RequestDB;
import org.rememberme.retreiver.Connector;

/**
 *
 * @author remembermewhy
 */
public class StockListPanel extends JPanel {

    private static Logger log = Logger.getLogger("StockList");
    private Connector connector = null;
    private List<SimpleStockGUI> stocks = null;
    private JTable stockTable = null;
    private JScrollPane scrollPane = null;
    private RequestDB requestDB;

    public StockListPanel(Connector connector) {
        this.connector = connector;
        this.requestDB = new RequestDB(connector);
    }

    public void init() {
        stocks = requestDB.retreiveStockList();
        Object[] columnNames = {"ID","YAHOO_NAME","NAME"};
        Object[][] data = new Object[stocks.size()][3];
        int i = 0;
        
        for (SimpleStockGUI stock : stocks) {
            data[i][0] = stock.getId();
            data[i][1] = stock.getName();
            data[i][2] = stock.getDescription();
            i++;
        }
        
        stockTable = new JTable(data, columnNames);

        scrollPane = new JScrollPane(stockTable);
        stockTable.setFillsViewportHeight(true);
        
        this.add(scrollPane);
    }
    
}
