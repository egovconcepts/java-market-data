package org.md.gui.services;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.md.gui.model.StockDefModel;
import org.md.gui.model.YahooEODStockModel;
import org.md.retriever.Connector;
import org.md.retriever.stock.YahooEODStock;

/**
 *
 * @author remembermewhy
 */
public class LoadEODTask extends Task<ObservableList<YahooEODStockModel>> {

    private final List<StockDefModel> stockModels;
    private final Connector connector;

    public LoadEODTask(List<StockDefModel> stockModels, Connector connector) {
        this.stockModels = stockModels;
        this.connector = connector;
    }
    
    @Override
    protected ObservableList<YahooEODStockModel> call() throws Exception {

        List<List<YahooEODStock>> eods = new ArrayList<>();
        for (StockDefModel model : stockModels) {
            List<YahooEODStock> eod = connector.loadEOD(model.getTicker());
            eods.add(eod);
        }

        List<YahooEODStockModel> models = new ArrayList<>();
        for (List<YahooEODStock> stocks : eods) {
            for (YahooEODStock stock : stocks) {
                YahooEODStockModel tmp = new YahooEODStockModel(stock);
                models.add(tmp);
            }
        }

        return FXCollections.observableArrayList(models);
    }

}
