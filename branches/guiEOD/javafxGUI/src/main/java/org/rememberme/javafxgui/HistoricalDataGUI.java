package org.rememberme.javafxgui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.rememberme.javafxgui.model.StockModel;
import org.rememberme.retreiver.stock.YahooEODStock;
import org.rememberme.retriever.Connector;
import org.rememberme.retriever.DataRetriever;
import org.rememberme.retriever.Request;

/**
 *
 *
 * @author remembermewhy
 */
public class HistoricalDataGUI extends Application {

    private static final Logger Log = Logger.getLogger(HistoricalDataGUI.class);

    private final TableView table = new TableView();
    final ObservableList<StockModel> data = FXCollections.observableArrayList();
    Connector connector;
    DataRetriever dr;

    public HistoricalDataGUI() {
    }

    @Override
    public void start(Stage stage) throws Exception {

        BasicConfigurator.configure();
        connector = new Connector();
        dr = new DataRetriever();
        dr.setConnector(connector);
        dr.init();

        
        connector.generateStockTable();
//        connector.executeQuery(Request.ADD_GOOG_STOCK);
        connector.executeQuery(Request.ALL_STOCK);
        connector.generateEODMarketDataTable();

        // --- Menu File
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem add = new MenuItem("Clean EOD DB");

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                connector.cleanEODDB();
            }
        });

        menuFile.getItems().addAll(add);
        MenuItem load = new MenuItem("Load EOD DB");

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    dr.processEODStockData();
                } catch (IOException ex) {
                    Log.error(ex);
                }
            }
        });

        menuFile.getItems().addAll(load);
        menuBar.getMenus().addAll(menuFile);

        List<List<String>> stockList = connector.loadStockDB();

        for (List<String> stockDef : stockList) {
            data.add(new StockModel(new SimpleStringProperty(stockDef.get(0)), new SimpleStringProperty(stockDef.get(1))));
        }

//        Group root = new Group();
        final BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,800,600);
        stage.setTitle("Stock List");

        final Label label = new Label("Stock View");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setFont(new Font("Arial", 20));

        final Button buttonGraph = new Button("Show Graph");
        buttonGraph.setMaxWidth(Double.MAX_VALUE);

        TableColumn tickerColumn = new TableColumn("Ticker");
        table.getColumns().addAll(tickerColumn);

        tickerColumn.setCellValueFactory(
                new PropertyValueFactory<StockModel, String>("ticker")
        );

        TableColumn defColumn = new TableColumn("Definition");
        table.getColumns().addAll(defColumn);

        defColumn.setCellValueFactory(
                new PropertyValueFactory<StockModel, String>("definition")
        );

        table.setItems(data);

        BorderPane leftborder = new BorderPane();

        leftborder.setTop(label);
        leftborder.setCenter(table);
        leftborder.setBottom(buttonGraph);

        table.setMaxHeight(Double.MAX_VALUE);

        final BorderPane centerPane = new BorderPane();

        borderPane.setTop(menuBar);
        borderPane.setLeft(leftborder);
//        borderPane.setCenter(centerPane);
        List<YahooEODStock> eod = connector.LOAD_HISTORICAL_STOCK("GOOG");
        Node node = SingleTickerNodeGen.GenerateSingleTickerNode("GOOG", eod);
        final TabPane pane = new TabPane();
        Tab tab = new Tab("GOOG");
        tab.setContent(node);
        pane.getTabs().add(tab);
        borderPane.setCenter(pane);
//        centerPane.sets
//        centerPane.setCenter(node);
//        borderPane.setCenter(node);

//        ((Group) scene.getRoot()).getChildren().add(node);

//        root.set`
        
//        Scene scene2 = new Scene(node,800,600);
        stage.setScene(scene);
        stage.show();

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                StockModel stockModel = (StockModel) table.getSelectionModel().getSelectedItem();
                if (stockModel != null) {
                    System.out.println(stockModel.getTicker() + " " + stockModel.getDefinition());
                }
            }
        });

        buttonGraph.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                StockModel stockModel = (StockModel) table.getSelectionModel().getSelectedItem();
                if (stockModel != null) {
                    String ticker = stockModel.getTicker();
                    Log.info("Load graph for " + ticker + " " + stockModel.getDefinition());
                    try {
                        List<YahooEODStock> eod = connector.LOAD_HISTORICAL_STOCK(ticker);
                        Node node = SingleTickerNodeGen.GenerateSingleTickerNode(ticker, eod);
                        Tab tab = new Tab(ticker);
                        tab.setContent(node);
                        pane.getTabs().add(tab);
//                        borderPane.setCenter(node);
                    } catch (SQLException sqle) {
                        Log.error("Error loading stock " + stockModel.getTicker(), sqle);
                    }

                }
            }
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
