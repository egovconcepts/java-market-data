package org.md.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.DataRetriever;

/**
 * Check for the existence of a stock from the Yahoo WebSite
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

    private final EODApplication hdgui;

    public SingleStockStage(EODApplication hdgui1) {
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

        check.setOnAction((ActionEvent t) -> {
            SingleStockDef ssd = dataRetriever.retrieveStockDef(ticker.getText());
            def.setDefinition(ssd.getDefinition());
            def.setTicker(ssd.getTicker());
            tickerText.setText(ssd.getTicker());
            definitionText.setText(ssd.getDefinition().replaceAll("\"", ""));
        });

        ok.setOnAction((ActionEvent t) -> {
            RetreiveStockStage stage = new RetreiveStockStage(def,hdgui);
            stage.initOwner(hdgui.getStage());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setWidth(SingleStockStage.this.getWidth());
            stage.setHeight(SingleStockStage.this.getHeight());
            stage.show();
            stage.init();
            SingleStockStage.this.close();
        });
        
        cancel.setOnAction((ActionEvent t) -> {
            SingleStockStage.this.close();
        });
        
    }

    /**
     * @param dr the dataRetriever to set
     */
    public void setDataRetriever(DataRetriever dr) {
        this.dataRetriever = dr;
    }

}
