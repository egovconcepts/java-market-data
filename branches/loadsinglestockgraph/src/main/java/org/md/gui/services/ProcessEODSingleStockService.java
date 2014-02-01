package org.md.gui.services;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.md.retriever.stock.SingleStockDef;
import org.md.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class ProcessEODSingleStockService extends Service {

    private final SimpleObjectProperty<DataRetriever> retriever = new SimpleObjectProperty<>();
    private SingleStockDef singleStockDef;

    public void setRetriever(DataRetriever dr) {
        this.retriever.set(dr);
    }

    public void setSingleStockDef(SingleStockDef singleStockDef) {
        this.singleStockDef = singleStockDef;
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                List<SingleStockDef> ssds = new ArrayList<>();
                ssds.add(singleStockDef);
                retriever.get().processEODStockData(ssds);
                return null;
            }
        };
    }
    
}
