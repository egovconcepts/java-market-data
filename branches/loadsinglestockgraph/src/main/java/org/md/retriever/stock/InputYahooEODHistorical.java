package org.md.retriever.stock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author remembermewhy
 */
public class InputYahooEODHistorical {

    private String ticker;
    private List<String> EODs;
    
    public InputYahooEODHistorical(String ticker) {
        this.ticker = ticker;
        EODs = new ArrayList<>();
    }
    
    public void addInput(String input){
        EODs.add(input);
    }

    public List<String> getEODs() {
        return EODs;
    }

    public String getTicker() {
        return ticker;
    }
    
}
