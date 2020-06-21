package com.example.tvtracker.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    public static String toDateString(String dateString){
        SimpleDateFormat comingFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sendingFormat = new SimpleDateFormat("MMM d, yyyy (EEE)");
        try {
            Date date =  comingFormat.parse(dateString);
            return sendingFormat.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String daysDifferenceFromCurrentDate(String epDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date episodeDate =  format.parse(epDate);
            Date currentDate = new Date();
            long diff = episodeDate.getTime() - currentDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return Long.toString(diffDays);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean compareDates(String date1, String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date episodeDate =  format.parse(date1);
            Date newEpisodeDate = format.parse(date2);
            long diff = newEpisodeDate.getTime() - episodeDate.getTime();
            if(diff > 0) {
                return true;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }
}
