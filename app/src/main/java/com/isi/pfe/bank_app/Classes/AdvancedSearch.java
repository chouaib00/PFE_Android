package com.isi.pfe.bank_app.Classes;

import java.util.Calendar;

public class AdvancedSearch {
    public static int day_Max,month_Max,year_Max,day_min,month_min,year_min;
    public static String category;
    public static boolean sent,received;


    public AdvancedSearch() {
        year_min = -1;
        initDateMax();
        category = "ALL";
        sent = true;
        received = true;
    }

    public static void initDateMin() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
      //  calendar.add(Calendar.MONTH,-1);
        year_min = calendar.get(Calendar.YEAR);
        month_min = calendar.get(Calendar.MONTH);
        day_min = calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static void initDateMax() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        year_Max = calendar.get(Calendar.YEAR);
        month_Max = calendar.get(Calendar.MONTH);
        day_Max = calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static String getMaxDate() {
        return year_Max+"-"+(month_Max+1)+"-"+day_Max;
    }
    public static String getMinDate() {
        if (year_min == -1) return "";
        return year_min+"-"+(month_min+1)+"-"+day_min;
    }

    public static String getType() {
        if ( sent && received ) return "ALL";
        if ( sent ) return "sent";
        if ( received) return "received";
        return "ALL";
    }

}