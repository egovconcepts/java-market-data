package org.md.gui.eodview;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.md.gui.model.YahooEODStockModel;

/**
 *
 * @author remembermewhy
 */
public class EODTableView extends TableView<YahooEODStockModel> {

    private final TableColumn date = new TableColumn("Date");
    private final TableColumn open = new TableColumn("Open");
    private final TableColumn high = new TableColumn("High");
    private final TableColumn low = new TableColumn("Low");
    private final TableColumn close = new TableColumn("Close");
    private final TableColumn volume = new TableColumn("Volume");
    private final TableColumn adj = new TableColumn("Adj");
    private final TableColumn ticker = new TableColumn("Ticker");

    public EODTableView() {
        this.getColumns().addAll(date, open, high, low, close, volume, adj,ticker);
        
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        open.setCellValueFactory(new PropertyValueFactory<>("open"));
        high.setCellValueFactory(new PropertyValueFactory<>("high"));
        low.setCellValueFactory(new PropertyValueFactory<>("low"));
        close.setCellValueFactory(new PropertyValueFactory<>("close"));
        volume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        adj.setCellValueFactory(new PropertyValueFactory<>("adj"));
        ticker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        
    }
}
