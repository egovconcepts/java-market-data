package org.rememberme.javafxgui;

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
import org.rememberme.retreiver.stock.SingleStockDef;
import org.rememberme.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class SingleStockView extends Application {

    private DataRetriever dataRetriever = null;

    GridPane layout = new GridPane();
    final TextField ticker = new TextField();
    final Button check = new Button("Check");
    final Button ok = new Button("OK");
    final Button cancel = new Button("Cancel");

    final Text tickerText = new Text();
    final Text definitionText = new Text();

    private final HistoricalDataGUI hdgui;
    
    public SingleStockView(HistoricalDataGUI hdgui1) {
        this.hdgui = hdgui1;
    }
    
    @Override
    public void start(Stage stage) throws Exception {

        final SingleStockDef def = new SingleStockDef();
        
//        definitionText.setVisible(false);
        layout.setHgap(10);
        layout.setVgap(30);
        layout.setPadding(new Insets(15, 12, 15, 12));

        ticker.setPrefWidth(300);

        layout.add(ticker, 1, 0);
        layout.add(check, 2, 0);
        layout.add(tickerText, 1, 1);
        layout.add(definitionText, 2, 1);
        layout.add(ok, 3, 1);
        layout.add(cancel, 4, 1);

        Scene scene = new Scene(layout);

        stage.setScene(scene);
        stage.setTitle("Lookup Stock");
        stage.show();

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
