package com.vuclip.abtesthttp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-6
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
//    static final private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static final private SimpleDateFormat dateFormat2 = new SimpleDateFormat("M/d/yy h:ma");
    static final private SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static SimpleDateFormat MYSQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat RSS_DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
    public static SimpleDateFormat ATOM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
    static final private SimpleDateFormat monthDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static final private SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
    
    static private HashMap<String,SimpleDateFormat> dateFormatMap = new HashMap<String,SimpleDateFormat>();

    public synchronized static final String dateFormat2(java.util.Date date) {
        return dateFormat2.format(date);
    }

    public synchronized static final String dateFormat2(long time) {
        return dateFormat2.format(new java.util.Date(time));
    }

    public synchronized static final String dateFormat(java.util.Date date) {
        return MYSQL_DATE_FORMAT.format(date);
    }

    public synchronized static final String dateFormat(long time) {
        return MYSQL_DATE_FORMAT.format(new java.util.Date(time));
    }

    public synchronized static final long dateParse(String date) throws ParseException {
        return MYSQL_DATE_FORMAT.parse(date).getTime();
    }

    public synchronized static final String monthDateFormat(long time) {
        return monthDateFormat.format(new java.util.Date(time));
    }

    public synchronized static final String monthFormat(long time) {
        return monthFormat.format(new java.util.Date(time));
    }
    public synchronized static final long dateParse2(String date) throws ParseException {
        return dateFormat3.parse(date).getTime();
    }
    public synchronized static final String dateFormat3(long time) {
        return dateFormat3.format(new java.util.Date(time));
    }
    
    public synchronized static final String dateFormat(java.util.Date date,String pattern) {
        
        SimpleDateFormat dateformat = dateFormatMap.get(pattern);
        if(dateformat==null){
            dateformat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateformat);
        }
        return dateformat.format(date);
    }

    public synchronized static final String dateFormat(long time,String pattern) {
        SimpleDateFormat dateformat = dateFormatMap.get(pattern);
        if(dateformat==null){
            dateformat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateformat);
        }
        return dateformat.format(new java.util.Date(time));
    }

    public synchronized static final long dateParse(String date,String pattern) throws ParseException {
        SimpleDateFormat dateformat = dateFormatMap.get(pattern);
        if(dateformat==null){
            dateformat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateformat);
        }
        return dateformat.parse(date).getTime();
    }
    
    public static String logTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss:",
                Locale.US);
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    public static List<String> getDaysBetwSDateAndEDate(int startDate,int endDate){
        List<String> list = new ArrayList<String>();
        String startDateStr = String.valueOf(startDate);
        String endDateStr = String.valueOf(endDate);
        int startYear = 0,startMon=0,startDay=0,endYear=0,endMon=0,endDay=0;
        if(startDateStr.length()==8&&endDateStr.length()==8){
            startYear = Integer.parseInt(startDateStr.substring(0,4));
            startMon = Integer.parseInt(startDateStr.substring(4, 6));
            startDay = Integer.parseInt(startDateStr.substring(6, 8));
            endYear = Integer.parseInt(endDateStr.substring(0, 4));
            endMon = Integer.parseInt(endDateStr.substring(4, 6));
            endDay = Integer.parseInt(endDateStr.substring(6, 8));
            if(startYear==endYear){
                if(startMon==endMon){
                    for(int i=startDay;i<=endDay;i++){
                        list.add(String.valueOf(startYear).concat("-").concat(startMon<10?("0"+startMon):String.valueOf(startMon)).concat("-").concat(i<10?("0"+i):String.valueOf(i)));
                    }
                }else{
                    for(int i = startMon;i<=endMon;i++){
                        int startAlldays = getDays(startYear,i);
                        if(i==startMon){
                            for(int j=startDay;j<=startAlldays;j++){
                                list.add(String.valueOf(startYear).concat("-").concat(i < 10 ? ("0" + i) : String.valueOf(i)).concat("-").concat(j<10?("0"+j):String.valueOf(j)));
                            }
                        }else if(i==endMon){
                            for(int j=1;j<=endDay;j++){
                                list.add(String.valueOf(startYear).concat("-").concat(i<10?("0"+i):String.valueOf(i)).concat("-").concat(j<10?("0"+j):String.valueOf(j)));
                            }
                        }else{
                            for(int j=1;j<=startAlldays;j++){
                                list.add(String.valueOf(startYear).concat("-").concat(i<10?("0"+i):String.valueOf(i)).concat("-").concat(j<10?("0"+j):String.valueOf(j)));
                            }
                        }
                    }

                }
            }
        }

        return list;
    }
    public static int getDays(int year,int month){
        Calendar time=Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR,year);
        time.set(Calendar.MONTH,month-1);
        int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day;
    }
}
