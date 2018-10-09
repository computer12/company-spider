package com.ssta.company.spider.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    public static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        LocalDateTime.now();
        return DATETIMEFORMATTER.format(LocalDateTime.now());
    }

    public static Date getFirstTime(String mh) {
        String time = LocalDate.now().toString() + " ";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date first = new Date();
        try {
            first = sdf.parse(time + mh);
            if (first.before(new Date())) {
                time = LocalDate.now().plusDays(1).toString() + " " + mh;
                first = sdf.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return first;
    }
}
