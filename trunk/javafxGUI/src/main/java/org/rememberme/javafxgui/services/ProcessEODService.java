package org.rememberme.javafxgui.services;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.rememberme.retriever.DataRetriever;

/**
 *
 * @author remembermewhy
 */
public class ProcessEODService extends Service{

    private SimpleObjectProperty<DataRetriever> retriever = new SimpleObjectProperty<DataRetriever>();

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
