package org.rememberme.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.sql.SQLException;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.rememberme.retreiver.Connector;

/**
 *
 * @author remembermewhy
 */
public class Main extends JFrame {

    private static Logger log = Logger.getLogger("Main");
    private Connector connector;

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Main() throws HeadlessException {
    }

    public void init() {
        JFrame frame = new JFrame("YAHOO DB Gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        StockListPanel stockPanel = new StockListPanel(connector);
        stockPanel.init();
        
        frame.getContentPane().add(stockPanel, BorderLayout.CENTER);
        frame.pack();
        
        frame.setVisible(true);
    }

    public static void main(String[] args) throws SQLException {

//        String server = args[0];
//        String port = args[1];
//        String dbName = args[2];
//        String tableName = args[3];
//        String dblogin = args[4];
//        String dbPwd = args[5];

        Main main = new Main();

        Connector connector = new Connector();
        connector.init();
//        log.info(server + " " + port + " " + dbName + " " + dblogin + " " + dbPwd);
        main.setConnector(connector);
        main.init();
        
    }
}
