package org.rememberme.javafxgui.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author remembermewhy
 */
public class StockModel {

    private final SimpleStringProperty ticker;
    private final SimpleStringProperty definition;

    public StockModel(SimpleStringProperty ticker, SimpleStringProperty def) {
        this.ticker = ticker;
        this.definition = def;
    }

    public String getTicker(){
        return ticker.get();
    }
            
    public void setTicker(String ticker){
        this.ticker.set(ticker);
    }
    
    public String getDefinition(){
        return definition.get();
    }
    
    public void setDefinition(String definition){
        this.definition.set(definition);
    }
    
}
