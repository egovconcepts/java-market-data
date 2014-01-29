package org.md.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.md.gui.model.YahooEODStockModel;

/**
 *
 * @author remembermewhy
 */
public class EODView extends TableView<YahooEODStockModel> {

    private final TableColumn date = new TableColumn("Date");
    private final TableColumn open = new TableColumn("Open");
    private final TableColumn high = new TableColumn("High");
    private final TableColumn low = new TableColumn("Low");
    private final TableColumn close = new TableColumn("Close");
    private final TableColumn volume = new TableColumn("Volume");
    private final TableColumn adj = new TableColumn("Adj");
    private final TableColumn ticker = new TableColumn("Ticker");

    public EODView() {
        this.getColumns().addAll(date, open, high, low, close, volume, adj,ticker);
        
        date.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,String>("date"));
        open.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Double>("open"));
        high.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Double>("high"));
        low.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Double>("low"));
        close.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Double>("close"));
        volume.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Integer>("volume"));
        adj.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,Integer>("adj"));
        ticker.setCellValueFactory(new PropertyValueFactory<YahooEODStockModel,String>("ticker"));
        
    }
}
