package org.rememberme.gui.component;

/**
 *
 * @author remembermewhy
 */
public class SimpleStockGUI {

    private String name;
    private String description;
    private int id;

    public SimpleStockGUI(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
