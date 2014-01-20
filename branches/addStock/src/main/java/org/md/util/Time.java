package org.md.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author remembermewhy
 */
public class Time {

    public List<String> listOfDate(String firstDay) throws ParseException {
        List<String> result = new ArrayList<>();

        Date date = EODDateFormat.parse(firstDay);
        Calendar c = new GregorianCalendar();
        c.setLenient(true);
        c.setTime(date);

        Calendar rightNow = Calendar.getInstance();

        while (!isSameDay(c, rightNow)) {
            if (isLastDayOfYear(c)) {
                c.roll(Calendar.YEAR, 1);
                c.set(Calendar.MONTH, Calendar.JANUARY);
                c.set(Calendar.DAY_OF_YEAR, 1);
            } else {
                c.roll(Calendar.DAY_OF_YEAR, 1);
            }
            if ((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                    || (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                continue;
            }

            result.add(EODDateFormat.format(c.getTime()));
        }

        return result;
    }

    private boolean isSameDay(Calendar a, Calendar b) {

        boolean result = (a.get(Calendar.YEAR) == b.get(Calendar.YEAR))
                && (a.get(Calendar.MONTH) == b.get(Calendar.MONTH))
                && (a.get(Calendar.DATE) == b.get(Calendar.DATE));

        return result;
    }

    private boolean isLastDayOfYear(Calendar calendar) {
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER
                && calendar.get(Calendar.DAY_OF_MONTH) == 31;
    }

    DateFormat EODDateFormat = new SimpleDateFormat("yyyy-MM-dd");

}
