package org.md.gui.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.log4j.Logger;
import org.md.gui.EODApplication;
import org.md.gui.SingleStockStage;

/**
 *
 * @author remembermewhy
 */
public class LookupStockHandler implements EventHandler<ActionEvent> {

    private static final Logger log = Logger.getLogger(LookupStockHandler.class);
    EODApplication historicalDataGUI;

    public LookupStockHandler(EODApplication historicalDataGUI) {
        this.historicalDataGUI = historicalDataGUI;
    }

    @Override
    public void handle(ActionEvent t) {
        try {
            SingleStockStage singleStockView = new SingleStockStage(historicalDataGUI);
            singleStockView.setDataRetriever(historicalDataGUI.getDr());
        } catch (Exception ex) {
            log.error("Error loading the new Stock", ex);
        }
    }

}
