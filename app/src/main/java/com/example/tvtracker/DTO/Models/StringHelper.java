package com.example.tvtracker.DTO.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StringHelper {
    public static String addZero(int count){
        return count < 10 ? "0" + count : String.valueOf(count);
    }
}
