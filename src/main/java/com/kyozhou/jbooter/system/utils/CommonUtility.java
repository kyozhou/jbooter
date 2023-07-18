package com.kyozhou.jbooter.system.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class CommonUtility {

    public static String md5(String inputString) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(inputString.getBytes());
        String md5String = new BigInteger(1, md.digest()).toString(16);
        while (md5String.length() < 32) {
            md5String = "0" + md5String;
        }
        return md5String;
    }

    public static Integer random(Integer from, Integer to) {
        return (int)(Math.random() * (to - from) + from);
    }

    public static Long getCurrentTimestamp() {
        return Instant.now().toEpochMilli() / 1000;
    }

    public static Long getTimestampByString(String timeString) {
        Timestamp timestamp = Timestamp.valueOf(timeString + " 00:00:00");  // 2011-05-09 11:49:45.0
        return timestamp.getTime() / 1000;
    }

    public static String getDayToday() {
        DateFormat format = new SimpleDateFormat("Y-MM-dd");
        Date date = new Date();
        return format.format(date);
    }

    public static String getStringFromTimestamp(Long timestampLong, String formatString) {
        DateFormat format = new SimpleDateFormat(formatString);
        Date date = new Date(timestampLong);
        return format.format(date);
    }

    public static LinkedList<String> getDayListByRange(String dayStart, String dayEnd) {
        LinkedList<String> dayList = new LinkedList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateStart = format.parse(dayStart);
            Date dateEnd = format.parse(dayEnd);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateStart);
            Integer maxDays = 60;
            while (calendar.getTime().compareTo(dateEnd) <= 0 && maxDays >= 0) {
                dayList.add(format.format(calendar.getTime()));
                calendar.add(calendar.DATE, 1);
                maxDays--;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayList;
    }



}
