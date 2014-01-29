package org.md.gui;

import org.md.gui.services.EODNodeGenTask;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;
import org.md.gui.model.StockDefModel;
import org.md.gui.model.YahooEODStockModel;
import org.md.gui.services.LoadEODTask;
import org.md.gui.services.LoadStockDefTask;
import org.md.gui.services.ProcessEODSingleStockService;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.stock.YahooEODStock;

/**
 *
 * @author remembermewhy
 */
public class StockDefListBorder extends BorderPane {

    private static final Logger log = Logger.getLogger(StockDefListBorder.class);
    private final HistoricalDataGUI hdgui;

    public StockDefListBorder(HistoricalDataGUI hdgui) {
        this.hdgui = hdgui;
    }

    private final TableView stockListTable = new TableView();
    private final ObservableList<StockDefModel> data = FXCollections.observableArrayList();
    
    private final Button buttonGraph = new Button("Show Graph");

    public void init() {

        buttonGraph.setMaxWidth(Double.MAX_VALUE);
        stockListTable.setMaxHeight(Double.MAX_VALUE);
        stockListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        buttonGraph.setOnMouseClicked(new ButtonGraphEvent());
        
        TableColumn tickerColumn = new TableColumn("Ticker");
        TableColumn defColumn = new TableColumn("Definition");
        stockListTable.getColumns().addAll(tickerColumn,defColumn);
        
        tickerColumn.setCellValueFactory(
                new PropertyValueFactory<StockDefModel, String>("ticker")
        );
        
        defColumn.setCellValueFactory(
                new PropertyValueFactory<StockDefModel, String>("definition")
        );

        Task<ObservableList<StockDefModel>> loadStockTask = new LoadStockDefTask(hdgui.connector);
        stockListTable.itemsProperty().bind(loadStockTask.valueProperty());
        Thread thread = new Thread(loadStockTask);
        thread.start();
        
        setCenter(stockListTable);
        setBottom(buttonGraph);
        
    }

    public void addStock(SingleStockDef def) {
        ProcessEODSingleStockService service = new ProcessEODSingleStockService();
        service.setRetriever(hdgui.dr);
        service.setSingleStockDef(def);
        try {
            hdgui.connector.addStockDef(def.getTicker(), def.getDefinition());
            data.add(new StockDefModel(def.getTicker(), def.getDefinition()));
            service.start();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private class ButtonGraphEvent implements EventHandler<MouseEvent> {

        public ButtonGraphEvent() {
        }

        @Override
        public void handle(MouseEvent t) {

            List<StockDefModel> stockModels = (List<StockDefModel>) stockListTable.getSelectionModel().getSelectedItems();

            if (stockModels != null && stockModels.size() > 0) {

                try {

                    for (StockDefModel model : stockModels) {
                        log.debug("Load graph and data for " + model.getTicker() + " " + model.getDefinition());
                    }

                    List<List<YahooEODStock>> eods = new ArrayList<>();
                    for (StockDefModel model : stockModels) {
                        List<YahooEODStock> eod = hdgui.connector.loadEOD(model.getTicker());
                        eods.add(eod);
                    }

                    Node node = null;
                    EODNodeGenTask task = new EODNodeGenTask();
                    task.setEods(eods);
                    new Thread(task).start();

                    try {
//                        node = task.call();
//                        node = (Node)task.get();
                    } catch (Exception ex) {
                        log.error(null, ex);
                    }

                    String tickers = "";
                    for (StockDefModel model : stockModels) {
                        tickers = tickers + " " + model.getTicker();
                    }

                    TabPane pane = new TabPane();
                    Tab graphTab = new Tab("Graph");
                    graphTab.setContent(node);
                    pane.getTabs().add(graphTab);
                    SingleSelectionModel<Tab> model = pane.getSelectionModel();
                    model.select(graphTab);

                    EODView view = new EODView();
                    Task<ObservableList<YahooEODStockModel>> loadEODTask = new LoadEODTask(stockModels, hdgui.connector);
                    view.itemsProperty().bind(loadEODTask.valueProperty());
                    
                    Thread thread = new Thread(loadEODTask);
                    thread.start();
                    
                    Tab dataTab = new Tab("Datas");
                    dataTab.setContent(view);
                    pane.getTabs().add(dataTab);

                    hdgui.addTab(tickers, pane);

                } catch (SQLException sqle) {
                    log.error("Error loading stocks ");
                }

            }
        }

    }

}
