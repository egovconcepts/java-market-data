package org.md.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.net.ssl.SSLEngineResult;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class SingleStockStage extends Stage {

    private DataRetriever dataRetriever = null;

    GridPane layout = new GridPane();
    final TextField ticker = new TextField();
    final Button check = new Button("Check");
    final Button ok = new Button("OK");
    final Button cancel = new Button("Cancel");

    final Text tickerText = new Text();
    final Text definitionText = new Text();

    private final HistoricalDataGUI hdgui;

    public SingleStockStage(HistoricalDataGUI hdgui1) {
        this.hdgui = hdgui1;
        final SingleStockDef def = new SingleStockDef();

        layout.setHgap(10);
        layout.setVgap(30);
        layout.setPadding(new Insets(15, 12, 15, 12));

        ticker.setPrefWidth(300);

        layout.add(ticker, 0, 0, 2, 1);
        layout.add(check, 2, 0);
        layout.add(tickerText, 0, 1);
        layout.add(definitionText, 1, 1);
        layout.add(ok, 2, 1);
        layout.add(cancel, 3, 1);

        Scene scene = new Scene(layout);

        this.setScene(scene);
        this.setTitle("Lookup Stock");
        this.show();

        check.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                SingleStockDef def1 = dataRetriever.retrieveStockDef(ticker.getText());
                def.setTicker(def1.getTicker());
                def.setDefinition(def1.getDefinition());

                tickerText.setText(def.getTicker());
                definitionText.setText(def.getDefinition().replaceAll("\"", ""));

            }
        });

        ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                hdgui.addStock(def);
                SingleStockStage.this.close();
//                System.exit(0);
            }
        });
    }

    /**
     * @param dr the dataRetriever to set
     */
    public void setDataRetriever(DataRetriever dr) {
        this.dataRetriever = dr;
    }

}
