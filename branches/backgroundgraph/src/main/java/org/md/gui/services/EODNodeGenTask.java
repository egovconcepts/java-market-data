package org.md.gui.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Logger;
import org.md.retriever.stock.YahooEODStock;
import org.md.util.Time;

/**
 *
 * @author remembermewhy
 */
public class EODNodeGenTask extends Task<Node> {

    private static final Logger log = Logger.getLogger(EODNodeGenTask.class);
    private static final DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private static int STEPS = 360;
    
    @Override
    protected Node call() throws Exception {

        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Close Price");

        //creating the chart
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        final List<List<YahooEODStock>> shortList = new ArrayList<>();

        for (List<YahooEODStock> eod : eods) {
            Collections.sort(eod);
        }

        for (List<YahooEODStock> eod : eods) {
            List<YahooEODStock> tmp = new ArrayList<>(STEPS);
            int start = eod.size() - 1 - STEPS;

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

        updateProgress(1, 3);

        if (shortList.size() == 1) {
            XYChart.Series seriesClose = new XYChart.Series();
            List<YahooEODStock> stocks = shortList.get(0);
            seriesClose.setName(stocks.get(0).getTicker());

            for (YahooEODStock stock : stocks) {
                seriesClose.getData().add(new XYChart.Data(stock.getDate(), stock.getClose()));
            }
            lineChart.getData().addAll(seriesClose);

        } else {

            List<String> dateList = null;
            try {
                minDate = firstDate(shortList);
                dateList = new Time().listOfDate(minDate);
            } catch (ParseException ex) {
                log.error(ex);
            }

            List<List<YahooEODStock>> unifiedEOD = new ArrayList<>();

            for (List<YahooEODStock> list : shortList) {
                List<YahooEODStock> tmp = unifyList(dateList, list);
                unifiedEOD.add(tmp);
            }
            updateProgress(2, 3);

            for (List<YahooEODStock> list : unifiedEOD) {
                XYChart.Series seriesClose = new XYChart.Series();
                seriesClose.setName(list.get(0).getTicker());
                for (YahooEODStock yeods : list) {
                    seriesClose.getData().add(new XYChart.Data(yeods.getDate(), yeods.getClose()));
                }
                lineChart.getData().addAll(seriesClose);
            }
        }
        
        return (Node) lineChart;
    }

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

    List<List<YahooEODStock>> eods = new ArrayList<>();

    public void setEods(List<List<YahooEODStock>> eods) {
        this.eods = eods;
    }

    
}
