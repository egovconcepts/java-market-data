package org.rememberme.javafxgui;

import java.util.Collections;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.rememberme.retreiver.stock.YahooEODStock;

/**
 * On a single ticker graph.
 * 
 * @author remembermewhy
 */
public class SingleTickerGraph extends Stage {

    private List<YahooEODStock> eod;
    private String ticker;

    public SingleTickerGraph(String ticker,List<YahooEODStock> eod) {
        this.eod = eod;
        Collections.sort(eod);
        build();
    }

    private void build() {
        setTitle(ticker);
        
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
//        xAxis.set
        
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        xAxis.setAutoRanging(true);
        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);

//        lineChart.setTitle("Stock Monitoring, " + );
        //defining a series
        XYChart.Series seriesLow = new XYChart.Series();
        seriesLow.setName("Low");
        XYChart.Series seriesHigh = new XYChart.Series();
        seriesHigh.setName("High");
        //populating the series with data

        int i = 0;
        for (YahooEODStock yeods : eod) {
            i++;
            seriesLow.getData().add(new XYChart.Data(yeods.getDate(), yeods.getLow()));
            seriesHigh.getData().add(new XYChart.Data(yeods.getDate(), yeods.getHigh()));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(seriesLow,seriesHigh);
//        lineChart.getData().add(seriesHigh);

        this.setScene(scene);
//        stage.show();
    }

}
