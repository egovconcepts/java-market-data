package org.rememberme.javafxgui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.rememberme.retreiver.stock.YahooEODStock;
import org.rememberme.util.Time;

/**
 *
 * @author remembermewhy
 */
public class EODNodeGen {

    private static final DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static String firstDate(List<List<YahooEODStock>> eods) throws ParseException {

        for (List<YahooEODStock> eod : eods) {
            Collections.sort(eod);
        }

        Date minDate = EODDateFormat.parse(eods.get(0).get(0).getDate());

        for (List<YahooEODStock> eod : eods) {
            Date date = EODDateFormat.parse(eod.get(0).getDate());
            minDate = minDate.getTime() < date.getTime() ? minDate : date;
        }

        return EODDateFormat.format(minDate);
    }

    private static List<YahooEODStock> unifyList(List<String> dates, List<YahooEODStock> toBeUnified){
        List<YahooEODStock> result = new ArrayList<>(dates.size());
        
        for(String date:dates){
            result.add(new YahooEODStock(toBeUnified.get(0).getTicker(), date, 0.0, 0.0, 0.0, 0.0, 0, 0.0));
        }
        
        for(YahooEODStock yeod:result){
            if(toBeUnified.contains(yeod)){
                for(YahooEODStock stock:toBeUnified){
                    if(stock.equals(yeod)){
                        yeod.copy(stock);
                    }
                }
            }
        }
        
        int size = result.size();
        for(int i = 0;i<size;i++){
            YahooEODStock stock = result.get(i);
            if((i>0)&&(stock.getClose()==0)){
                String date = stock.getDate();
                stock.copy(result.get(i-1));
                stock.setDate(date);
            }
        }
        
        return result;
    }
    
    public static Node GenerateTickerNode(List<List<YahooEODStock>> eods) {

        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Close Price");

        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);


        for (List<YahooEODStock> eod : eods) {
            Collections.sort(eod);
        }
        
        
        String minDate = "";
        List<String> dateList = null;
        try {
            minDate = firstDate(eods);
            dateList = new Time().listOfDate(minDate);
        } catch (ParseException ex) {
            Logger.getLogger(EODNodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<List<YahooEODStock>> unfiedEOD = new ArrayList<>();
        
        for(List<YahooEODStock> list: eods){
            List<YahooEODStock> tmp = unifyList(dateList, list);
            unfiedEOD.add(tmp);
        }
        
        for(List<YahooEODStock> list: unfiedEOD){
            XYChart.Series seriesClose = new XYChart.Series();
            seriesClose.setName(list.get(0).getTicker());
            for (YahooEODStock yeods : list) {
                seriesClose.getData().add(new XYChart.Data(yeods.getDate(), yeods.getClose()));
            }
            lineChart.getData().addAll(seriesClose);
        }
        
        lineChart.prefHeight(Double.MAX_VALUE);
        lineChart.prefWidth(Double.MAX_VALUE);

        return (Node) lineChart;
    }
}
