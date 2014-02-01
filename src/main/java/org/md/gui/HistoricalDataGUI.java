package org.md.gui;

import org.md.gui.event.LookupStockHandler;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.Connector;
import org.md.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class HistoricalDataGUI extends Application {

    private static final Logger log = Logger.getLogger(HistoricalDataGUI.class);
    private final BorderPane centerPane = new BorderPane();
    private final TabPane tabPane = new TabPane();
    private StockDefListBorderPane leftborder;

    Connector connector;
    DataRetriever dr;

    public HistoricalDataGUI() {

    }

    public void addStock(SingleStockDef def) {
        leftborder.addStock(def);
    }

    public void addTab(String title, Node node) {
        Tab tab = new Tab(title);
        tab.setContent(node);
        tabPane.getTabs().add(tab);
        SingleSelectionModel<Tab> model = tabPane.getSelectionModel();
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

        
        StackPane pane = new StackPane();
        centerPane.setCenter(tabPane);
        
        // Main Panel
        final BorderPane borderPane = new BorderPane();

        // Stock Definition Panel
        leftborder = new StockDefListBorderPane(this);
        leftborder.init();
        
        borderPane.setTop(menuBar);
        borderPane.setLeft(leftborder);
        borderPane.setCenter(centerPane);
        
        Scene scene = new Scene(borderPane, 1000, 600);
        stage.setTitle("Stock List");
        stage.setScene(scene);
        stage.show();

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
