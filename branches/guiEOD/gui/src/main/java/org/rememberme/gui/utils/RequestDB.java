package org.rememberme.gui.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.rememberme.gui.component.SimpleStockGUI;
import org.rememberme.retriever.Connector;

/**
 *
 * @author remembermewhy
 */
public class RequestDB {

    private Connector connector = null;
    Connection con = null;

    public RequestDB(Connector connector) {
        this.connector = connector;
        this.con = connector.getConnection();
    }

    public List<SimpleStockGUI> retreiveStockList() {
        List<SimpleStockGUI> result = new ArrayList<>();
        String request = "Select * from STOCK";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                String yahooTicker = rs.getString("YAHOO_NAME");
                String yahooDescription = rs.getString("NAME");
                String id = rs.getString("ID");
                result.add(new SimpleStockGUI(yahooTicker, yahooDescription, Integer.parseInt(id)));
            }
        } catch (SQLException e) {
            return null;
        }
        return result;
    }
}
