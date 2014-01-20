package org.md.retreiver.stock;

/**
 *
 * @author remembermewhy
 */
public class SingleStockDef {

    private String ticker;
    private String definition;

    public SingleStockDef() {
    }
    
    public SingleStockDef(String ticker, String definition) {
        this.ticker = ticker;
        this.definition = definition;
    }

    @Override
    public String toString() {
        return ticker + " " + definition;
    }

    /**
     * @return the ticker
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * @param ticker the ticker to set
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /**
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    
    
    
}
