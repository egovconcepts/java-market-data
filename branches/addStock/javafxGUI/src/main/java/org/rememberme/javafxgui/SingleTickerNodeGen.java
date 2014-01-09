package org.rememberme.javafxgui;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.rememberme.retreiver.stock.YahooEODStock;

/**
 *
 * @author remembermewhy
 */
public class SingleTickerNodeGen {

    public static Node GenerateSingleTickerNode(String ticker, final List<YahooEODStock> eod) {
        
        Collections.sort(eod);
        
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelRotation(45);
        System.out.println("Tick Label Gep" + xAxis.getTickLabelGap());
        xAxis.setTickLabelGap(15);
        System.out.println("Tick Label Gep" + xAxis.getTickLabelGap());
        
        xAxis.setLabel("Date");
        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setAutoRanging(true);
        
        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);

        XYChart.Series seriesLow = new XYChart.Series();
        seriesLow.setName("Close");
//        XYChart.Series seriesHigh = new XYChart.Series();
//        seriesHigh.setName("High");

        for (YahooEODStock yeods : eod) {
            seriesLow.getData().add(new XYChart.Data(yeods.getDate(), yeods.getClose()));
//            seriesHigh.getData().add(new XYChart.Data(yeods.getDate(), yeods.getHigh()));
        }
        
        lineChart.getData().addAll(seriesLow);
        lineChart.prefHeight(Double.MAX_VALUE);
        lineChart.prefWidth(Double.MAX_VALUE);
        System.out.println(lineChart.isResizable());
        
        return (Node)lineChart;
    }
}
