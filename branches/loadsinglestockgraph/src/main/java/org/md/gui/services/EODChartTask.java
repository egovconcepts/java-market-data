package org.md.gui.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Logger;
import org.md.gui.model.StockDefModel;
import org.md.retriever.Connector;
import org.md.retriever.stock.YahooEODStock;
import org.md.util.Time;

/**
 *
 * @author remembermewhy
 */
public class EODChartTask extends Task<ObservableList<XYChart.Series<String, Number>>> {

    private static final Logger log = Logger.getLogger(EODChartTask.class);
    private static final DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static int STEPS = 50;

    private final List<StockDefModel> stockModels;
    private final Connector connector;

    public EODChartTask(List<StockDefModel> stockModels, Connector connector) {
        this.stockModels = stockModels;
        this.connector = connector;
    }

    @Override
    protected ObservableList<XYChart.Series<String, Number>> call() throws Exception {

        List<List<YahooEODStock>> eods = new ArrayList<>();
        for (StockDefModel model : stockModels) {
            List<YahooEODStock> eod = connector.loadEOD(model.getTicker());
            eods.add(eod);
        }

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

        List<XYChart.Series<String, Number>> series = new ArrayList<>();

        if (shortList.size() == 1) {
            XYChart.Series<String, Number> serieClose = new XYChart.Series<>();
            List<YahooEODStock> stocks = shortList.get(0);
            serieClose.setName(stocks.get(0).getTicker());

            for (YahooEODStock stock : stocks) {
                serieClose.getData().add(new XYChart.Data(stock.getDate(), stock.getClose()));
            }
            series.add(serieClose);

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

            for (List<YahooEODStock> list : unifiedEOD) {
                XYChart.Series<String, Number> serieClose = new XYChart.Series<>();
                serieClose.setName(list.get(0).getTicker());
                for (YahooEODStock yeods : list) {
                    serieClose.getData().add(new XYChart.Data(yeods.getDate(), yeods.getClose()));
                }
                series.add(serieClose);
            }
        }

        return FXCollections.observableArrayList(series);
    }

    /**
     * Return the most previous date of differents list of objects.
     * @param eods
     * @return
     * @throws ParseException 
     */
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

}
