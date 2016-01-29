package com.bvhloc.datetimemachine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatetimeHelper {

    public Calendar getCalendar(String dateString, SimpleDateFormat dateFormat) {
        try {

            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public String getString(Calendar calendar, SimpleDateFormat dateFormat){

        Date date = calendar.getTime();
        return dateFormat.format(date);
    }
}
