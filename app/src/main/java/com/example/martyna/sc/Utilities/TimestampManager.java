package com.example.martyna.sc.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Martyna on 2016-01-19.
 */
public class TimestampManager {

    public String toTime(long timestamp) {
        Date date = new Date (timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return sdf.format(date);
    }

    public long oneHourBack (long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        return oneHourBack.getTime();
    }

    public String toDate(long timestamp) {
        Date date = new Date (timestamp);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }


}
