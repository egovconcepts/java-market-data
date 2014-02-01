package org.md.gui.services;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.md.gui.model.StockDefModel;
import org.md.retriever.Connector;
import org.md.retriever.stock.SingleStockDef;

/**
 *
 * @author remembermewhy
 */
public class LoadStockDefService extends Service<ObservableList<StockDefModel>> {

    private final Connector connector;

    public LoadStockDefService(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected Task<ObservableList<StockDefModel>> createTask() {

        return new Task<ObservableList<StockDefModel>>() {

            @Override
            protected ObservableList<StockDefModel> call() throws Exception {
                List<StockDefModel> data = new ArrayList<>();
                List<SingleStockDef> stockList = connector.loadStockDefDB();
                for (SingleStockDef stockDef : stockList) {
                    data.add(new StockDefModel(new SimpleStringProperty(stockDef.getTicker()), new SimpleStringProperty(stockDef.getDefinition())));
                }

                return FXCollections.observableArrayList(data);
            }
        };
        
    }

}
