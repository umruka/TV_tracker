package com.watermelon.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {
    public static String getDateString(String dateString) {
        SimpleDateFormat comingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sendingFormat = new SimpleDateFormat("MMM d, yyyy (EEE)");
        try {
            Date date = comingFormat.parse(dateString);
            return sendingFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String daysDifferenceFromCurrentDate(String epDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String timeText;
            Date episodeDate = format.parse(epDate);
            Date currentDate = new Date();
            long diff = episodeDate.getTime() - currentDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            long diffHours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            if (diffDays < 0) {
                timeText = Long.toString(diffDays).substring(1) + " day and " + Long.toString(diffHours).substring(1);
                return "released " + timeText + " hours ago";
            } else if (diffDays == 0) {
                if (diffHours == 0) {
                    return "less than a hour left";
                } else if (diffHours < 0) {
                    timeText = Long.toString(diffHours);
                    return "released " + timeText.substring(1) + " hours ago";
                }
                timeText = Long.toString(diffHours);
                return timeText + " hours left";
            }
            timeText = Long.toString(diffDays);
            return timeText + " days left";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean compareDates(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date episodeDate = format.parse(date1);
            Date newEpisodeDate = format.parse(date2);
            long diff = newEpisodeDate.getTime() - episodeDate.getTime();
            if (diff > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String toDays(String runtime) {
        int runtimeDays = Integer.parseInt(runtime);
        String hoursText = " hours ";
        String minutesText =  " minutes ";
        int hours = runtimeDays / 3600;
        int temp = (int)  runtimeDays - hours * 3600;
        int mins = temp / 60;
        temp = temp - mins * 60;
        int secs = temp;
        if (hours > 24) {
            int days = hours / 24;
            hours = hours - 24;
            if (days < 10) {
                return days + " day " + hours + hoursText + mins + minutesText;
            } else {
                return days + " days " + hours + hoursText + mins + minutesText + secs;
            }
        }
            return hours + hoursText + mins + minutesText;
        }
    }
