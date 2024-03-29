package org.md.gui;

import org.md.gui.eodview.EODTableView;
import org.md.gui.services.EODChartTask;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import org.md.gui.model.StockDefModel;
import org.md.gui.model.YahooEODStockModel;
import org.md.gui.services.LoadEODTask;
import org.md.gui.services.LoadStockDefService;
import org.md.gui.services.ProcessEODSingleStockService;
import org.md.retriever.stock.SingleStockDef;

/**
 *
 * @author remembermewhy
 */
public class StockDefListBorderPane extends BorderPane {

    private static final Logger log = Logger.getLogger(StockDefListBorderPane.class);
    private final EODApplication hdgui;
    private final LoadStockDefService loadStockService;

    public StockDefListBorderPane(EODApplication hdgui) {
        this.hdgui = hdgui;
        loadStockService = new LoadStockDefService(hdgui.connector);
    }

    private final TableView stockListTable = new TableView();
    private final Button buttonGraph = new Button("Show Graph");

    public void init() {

        buttonGraph.setMaxWidth(Double.MAX_VALUE);
        stockListTable.setMaxHeight(Double.MAX_VALUE);
        stockListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        buttonGraph.setOnMouseClicked(new ButtonGraphEvent());

        TableColumn tickerColumn = new TableColumn("Ticker");
        TableColumn defColumn = new TableColumn("Definition");
        stockListTable.getColumns().addAll(tickerColumn, defColumn);

        tickerColumn.setCellValueFactory(
                new PropertyValueFactory<>("ticker")
        );

        defColumn.setCellValueFactory(
                new PropertyValueFactory<>("definition")
        );

        stockListTable.itemsProperty().bind(loadStockService.valueProperty());
        loadStockService.start();

        setCenter(stockListTable);
        setBottom(buttonGraph);

    }

    public void reload() {
        loadStockService.reset();
        loadStockService.start();
    }

    public void addStock(SingleStockDef def) {
        ProcessEODSingleStockService service = new ProcessEODSingleStockService(hdgui.connector, hdgui.dr);
        service.setSingleStockDef(def);
        try {
            hdgui.connector.addStockDef(def.getTicker(), def.getDefinition());
            reload();
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

                int steps = 0;

                final CategoryAxis xAxis = new CategoryAxis();
                xAxis.setLabel("Date");
                final NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Close Price");

                //creating the chart
                final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
                Task<ObservableList<XYChart.Series<String, Number>>> task = new EODChartTask(stockModels, hdgui.connector);
                lineChart.dataProperty().bind(task.valueProperty());
                new Thread(task).start();

                String tickers = "";
                for (StockDefModel model : stockModels) {
                    tickers = tickers + " " + model.getTicker();
                }

                BorderPane graphePane = new BorderPane();
                TabPane pane = new TabPane();

                ProgressBar progressBar = new ProgressBar();
                progressBar.progressProperty().bind(task.progressProperty());
                progressBar.setPrefWidth(Double.MAX_VALUE);

                Text text = new Text();
                text.textProperty().bind(task.messageProperty());

                StackPane stackpane = new StackPane(progressBar, text);

                Tab graphTab = new Tab("Graph");
                graphTab.setContent(lineChart);
                pane.getTabs().add(graphTab);
                SingleSelectionModel<Tab> model = pane.getSelectionModel();
                model.select(graphTab);

                EODTableView view = new EODTableView();
                Task<ObservableList<YahooEODStockModel>> loadEODTask = new LoadEODTask(stockModels, hdgui.connector);
                view.itemsProperty().bind(loadEODTask.valueProperty());
                Thread thread = new Thread(loadEODTask);
                thread.start();

                ProgressBar progressBar1 = new ProgressBar();
                progressBar1.progressProperty().bind(loadEODTask.progressProperty());
                progressBar1.setPrefWidth(Double.MAX_VALUE);

                VBox vBox = new VBox();
                vBox.getChildren().addAll(stackpane, progressBar1);

                Tab dataTab = new Tab("Datas");
                dataTab.setContent(view);
                pane.getTabs().add(dataTab);

                graphePane.setCenter(pane);
                graphePane.setTop(vBox);

                hdgui.addTab(tickers, graphePane);

            }
        }

    }

}
