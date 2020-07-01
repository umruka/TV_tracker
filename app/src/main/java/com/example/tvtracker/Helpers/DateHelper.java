package com.example.tvtracker.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    public static String getDateString(String dateString){
        SimpleDateFormat comingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String timeText;
            Date episodeDate =  format.parse(epDate);
            Date currentDate = new Date();
            long diff = episodeDate.getTime() - currentDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            long diffHours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            if(diffDays == 0) {
                timeText = Long.toString(diffHours);
                if (diffHours == 0) {
                    return "less than a hour left";
                }
                return timeText + " hours left";
            }else if(diffDays < 0) {
                    timeText = Long.toString(diffHours);
                    return "released before " + timeText.substring(1) + " hours";
            }
            timeText = Long.toString(diffDays);
            return timeText + " days left";
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean compareDates(String date1, String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
