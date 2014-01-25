package org.md.gui;

import org.md.gui.event.LookupStockHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.md.gui.model.StockDefModel;
import org.md.gui.model.YahooEODStockModel;
import org.md.gui.services.ProcessEODSingleStockService;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.stock.YahooEODStock;
import org.md.retriever.Connector;
import org.md.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class HistoricalDataGUI extends Application {

    private static final Logger log = Logger.getLogger(HistoricalDataGUI.class);
    final TabPane pane = new TabPane();
    private StockDefListBorder leftborder;
    
    Connector connector;
    DataRetriever dr;

    public HistoricalDataGUI() {

    }

    public void addStock(SingleStockDef def){
        leftborder.addStock(def);
    }
    
    public void addTab(String title, Node node) {
        Tab tab = new Tab(title);
        tab.setContent(node);
        pane.getTabs().add(tab);
        SingleSelectionModel<Tab> model = pane.getSelectionModel();
        model.select(tab);
    }

    @Override
    public void start(Stage stage) throws Exception {

        BasicConfigurator.configure(); // Log4J configurator

        connector = new Connector();
        dr = new DataRetriever();
        dr.setConnector(connector);
        dr.init();

        // --- Generate the Menu
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");

        MenuItem newStock = new MenuItem("New Stock");
        newStock.setOnAction(new LookupStockHandler(this));
        menuFile.getItems().addAll(newStock);

        menuBar.getMenus().addAll(menuFile);
        
        // Main Panel
        final BorderPane borderPane = new BorderPane();

        // Stock Definition Panel
        leftborder = new StockDefListBorder(this);
        leftborder.init();

        // Graph Panel
        final BorderPane centerPane = new BorderPane();

        
        borderPane.setTop(menuBar);
        borderPane.setLeft(leftborder);
        borderPane.setCenter(pane);

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setTitle("Stock List");
        stage.setScene(scene);
        stage.show();

//        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent t) {
//                StockDefModel stockModel = (StockDefModel) table.getSelectionModel().getSelectedItem();
//                if (stockModel == null) {
//                    return;
//                }
//
//                log.info(stockModel.getTicker() + " " + stockModel.getDefinition());
//
//                if (t.getButton() == MouseButton.SECONDARY) {
//                    EODView view = new EODView();
//
//                    try {
//
//                        List<YahooEODStock> stocks = connector.loadEOD(stockModel.getTicker());
//                        Collections.sort(stocks);
//
//                        List<YahooEODStockModel> models = new ArrayList<>(stocks.size());
//                        for (YahooEODStock stock : stocks) {
//                            YahooEODStockModel tmp = new YahooEODStockModel(stock);
//                            models.add(tmp);
//                        }
//
//                        ObservableList<YahooEODStockModel> data = FXCollections.observableArrayList(models);
//                        view.setItems(data);
//
//                        Tab tab = new Tab(stockModel.getTicker());
//                        tab.setContent(view);
//                        pane.getTabs().add(tab);
//                        SingleSelectionModel<Tab> model = pane.getSelectionModel();
//                        model.select(tab);
//
//                    } catch (SQLException ex) {
//                        java.util.logging.Logger.getLogger(HistoricalDataGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        });

    }

    public DataRetriever getDr() {
        return dr;
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
