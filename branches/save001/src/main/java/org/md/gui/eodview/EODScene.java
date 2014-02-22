package org.md.gui.eodview;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.md.gui.EODApplication;

/**
 *
 * @author remembermewhy
 */
public class EODScene extends Scene {

    final private TabPane pane;
    final private EODApplication hdgui; 

    public EODScene(EODApplication hdgui, Parent root) {
        super(root);
        this.pane = new TabPane();
        this.hdgui = hdgui;
    }
    
    protected void addPane(Node node, String title){
        Tab tab = new Tab(title);
        tab.setContent(node);
        pane.getTabs().add(tab);
    }

}
