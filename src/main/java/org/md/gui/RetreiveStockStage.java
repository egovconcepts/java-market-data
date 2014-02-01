package org.md.gui;

import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.md.gui.services.ProcessEODSingleStockService;
import org.md.retriever.stock.SingleStockDef;

/**
 *
 * @author remembermewhy
 */
public class RetreiveStockStage extends Stage {

    private static final Logger log = Logger.getLogger(RetreiveStockStage.class);
    private final HistoricalDataGUI hdgui;
    private final SingleStockDef ssd;
    private final ProcessEODSingleStockService service;
    private final ProgressBar progressBar = new ProgressBar();
    private final Text text = new Text();

    RetreiveStockStage(SingleStockDef def, HistoricalDataGUI hdgui) {
        this.hdgui = hdgui;
        this.ssd = def;
        this.service = new ProcessEODSingleStockService(hdgui.connector, hdgui.dr);
        VBox box = new VBox();
        box.setSpacing(20);
        box.setPadding(new Insets(20, 20, 20, 20));

        progressBar.setPrefWidth(Double.MAX_VALUE);
        box.getChildren().addAll(progressBar, text);
        this.setScene(new Scene(box));
        progressBar.progressProperty().bind(service.progressProperty());
        text.textProperty().bind(service.messageProperty());
    }

    public void init() {
        service.setSingleStockDef(ssd);

        service.stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
                switch (newValue) {
                    case SUCCEEDED:
                        RetreiveStockStage.this.close();
                        break;
                    case FAILED:
                        RetreiveStockStage.this.close();
                        break;
                    case CANCELLED:
                        RetreiveStockStage.this.close();
                        break;
                }
            }

        });

        try {
            hdgui.connector.addStockDef(ssd.getTicker(), ssd.getDefinition());
            hdgui.reloadStock();
            service.start();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

}
