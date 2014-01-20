package org.md.gui.services;

import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.md.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class ProcessEODService extends Service{

    private final SimpleObjectProperty<DataRetriever> retriever = new SimpleObjectProperty<>();

    public void setRetriever(DataRetriever dr) {
        this.retriever.set(dr);
    }
    
    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                retriever.get().processEODStockData();
                return null;
            }
        };
    }
}
