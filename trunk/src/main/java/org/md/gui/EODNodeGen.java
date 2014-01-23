package org.md.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.md.retriever.stock.YahooEODStock;
import org.md.util.Time;

/**
 *
 * @author remembermewhy
 */
public class EODNodeGen {

    private static final Logger log = Logger.getLogger(EODNodeGen.class);

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

    private static List<YahooEODStock> unifyList(List<String> dates, List<YahooEODStock> toBeUnified) {
        List<YahooEODStock> result = new ArrayList<>(dates.size());

        for (String date : dates) {
            result.add(new YahooEODStock(toBeUnified.get(0).getTicker(), date, 0.0, 0.0, 0.0, 0.0, 0, 0.0));
        }

        for (YahooEODStock yeod : result) {
            if (toBeUnified.contains(yeod)) {
                for (YahooEODStock stock : toBeUnified) {
                    if (stock.equals(yeod)) {
                        yeod.copy(stock);
                    }
                }
            }
        }

        int size = result.size();
        for (int i = 0; i < size; i++) {
            YahooEODStock stock = result.get(i);
            if ((i > 0) && (stock.getClose() == 0)) {
                String date = stock.getDate();
                stock.copy(result.get(i - 1));
                stock.setDate(date);
            }
        }

        return result;
    }

    public static Node GenerateSingleTickerNode(List<YahooEODStock> eods) {
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Close Price");

        Collections.sort(eods);

        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);

        XYChart.Series seriesClose = new XYChart.Series();
        seriesClose.setName(eods.get(0).getTicker());

        List<YahooEODStock> tmp = new ArrayList<>(30);
        int start = eods.size() - 1 - 30;

        if (start < 0) {
            tmp = eods;
        } else {
            for (int i = start; i < eods.size(); i++) {
                tmp.add(eods.get(i));
            }
        }

        for (YahooEODStock yeods : tmp) {
            log.debug(yeods);
            seriesClose.getData().add(new XYChart.Data(yeods.getDate(), yeods.getClose()));
        }

        lineChart.prefHeight(Double.MAX_VALUE);
        lineChart.prefWidth(Double.MAX_VALUE);

        lineChart.getData().addAll(seriesClose);

        return (Node) lineChart;
    }

    public static Node GenerateTickerNode(List<List<YahooEODStock>> eods) {

        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Close Price");

        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);

        List<List<YahooEODStock>> shortList = new ArrayList<>();

        for (List<YahooEODStock> eod : eods) {
            Collections.sort(eod);
        }

        for (List<YahooEODStock> eod : eods) {
            List<YahooEODStock> tmp = new ArrayList<>(30);
            int start = eod.size() - 1 - 30;

            if (start < 0) {
                tmp = eod;
            } else {
                for (int i = start; i < eod.size(); i++) {
                    tmp.add(eod.get(i));
                }
            }
            shortList.add(tmp);
        }

        String minDate = "";

        List<String> dateList = null;
        try {
//            minDate = firstDate(eods);
            minDate = firstDate(shortList);
            dateList = new Time().listOfDate(minDate);
        } catch (ParseException ex) {
            log.error(ex);
//            Logger.getLogger(EODNodeGen.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<List<YahooEODStock>> unifiedEOD = new ArrayList<>();

//        for (List<YahooEODStock> list : eods) {
        for (List<YahooEODStock> list : shortList) {
            List<YahooEODStock> tmp = unifyList(dateList, list);
            unifiedEOD.add(tmp);
        }

        for (List<YahooEODStock> list : unifiedEOD) {
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
