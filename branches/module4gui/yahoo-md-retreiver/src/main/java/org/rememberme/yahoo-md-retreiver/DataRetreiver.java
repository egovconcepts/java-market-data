package org.rememberme.yahoo-md-retreiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DataRetreiver {

    private static Logger log = Logger.getLogger("DataRetreiver");
    private URL url;
    private Connector connector;
    private BlockingQueue<InputYahooData> concurrentInputStrings;
    private DataSerializerManager dataSerializerManager;

    public DataRetreiver() {
        BasicConfigurator.configure();
        concurrentInputStrings = new LinkedBlockingQueue<>();
        dataSerializerManager = new DataSerializerManager(concurrentInputStrings);
    }

    public void init() throws SQLException, IOException, InterruptedException {
        connector.init();
        dataSerializerManager.setConnector(connector);
        new Thread(dataSerializerManager).start();
        String yahooTicker = loadYahooTicker();
        List<InputYahooData> loInputYahooDatas;

        while (true) {
            loInputYahooDatas = new LinkedList<>();
            url = new URL(
                    "http://finance.yahoo.com/d/quotes.csv?s=" + yahooTicker + "&f=snb3b6a5b2d1t1");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                loInputYahooDatas.add(new InputYahooData(inputLine, System.currentTimeMillis()));
            }

            in.close();

            for (InputYahooData istr : loInputYahooDatas) {
                concurrentInputStrings.put(istr);
            }

            Thread.sleep(1000);
        }
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    private String loadYahooTicker() {
        List<String> yahooTickers = connector.loadStockDB();

        if (yahooTickers == null) {
            throw new RuntimeException("No Stock to be loaded from DB");
        }

        StringBuilder sb = new StringBuilder();
        for (String yahooTicker : yahooTickers) {
            sb.append(yahooTicker).append("+");
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException,
            InterruptedException, SQLException {
        DataRetreiver dr = new DataRetreiver();

        if (args.length != 6 ) {
            log.info("usage : server port dbname tableName dblogin dbpwd");
            return;
        }
        
        String server = args[0];
        String port = args[1];
        String dbName = args[2];
        String tableName = args[3];
        String dblogin = args[4];
        String dbPwd = args[5];

        Connector connector = new Connector(server, port, dbName, tableName, dblogin, dbPwd);
        dr.setConnector(connector);

        log.info(server + " " + port + " " + dbName + " " + dblogin + " " + dbPwd);

        dr.init();
    }
}
