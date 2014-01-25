package org.md.gui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;
import org.md.gui.model.StockDefModel;
import org.md.gui.model.YahooEODStockModel;
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
        stockListTable.getColumns().addAll(tickerColumn);
        tickerColumn.setCellValueFactory(
                new PropertyValueFactory<StockDefModel, String>("ticker")
        );

        TableColumn defColumn = new TableColumn("Definition");
        stockListTable.getColumns().addAll(defColumn);
        defColumn.setCellValueFactory(
                new PropertyValueFactory<StockDefModel, String>("definition")
        );

        List<SingleStockDef> stockList = hdgui.connector.loadStockDefDB();
        for (SingleStockDef stockDef : stockList) {
            data.add(new StockDefModel(new SimpleStringProperty(stockDef.getTicker()), new SimpleStringProperty(stockDef.getDefinition())));
        }

        stockListTable.setItems(data);

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

                    if (eods.size() == 1) {
                        node = EODNodeGen.GenerateSingleTickerNode(eods.get(0));
                    } else {
                        node = EODNodeGen.GenerateTickerNode(eods);
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
                    List<YahooEODStockModel> models = new ArrayList<>();
                    for (List<YahooEODStock> stocks : eods) {
                        for (YahooEODStock stock : stocks) {
                            YahooEODStockModel tmp = new YahooEODStockModel(stock);
                            models.add(tmp);
                        }
                    }

                    ObservableList<YahooEODStockModel> data = FXCollections.observableArrayList(models);
                    view.setItems(data);

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
