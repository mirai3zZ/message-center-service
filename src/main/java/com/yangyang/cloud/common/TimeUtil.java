package com.yangyang.cloud.common;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间转换公共方法
 */
public class TimeUtil {

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 字符串转date
     */
    public static Date dateFormat(String time) {
        if (!StringUtils.hasText(time)) {
            return new Date();
        }
        try {
            return threadLocal.get().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 时间戳转date
     */
    public Date unixTime2Date(long timeStamp) {
        try {
            return threadLocal.get().parse(threadLocal.get().format(timeStamp * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * date转时间戳 单位 秒
     */
    public static long dateString2UnixTimeInSeconds(String time) {
        try {
            Date parse = threadLocal.get().parse(time);
            return parse.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String data2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String data2StringNoYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    public static String data2StringNoSecond(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String dateTime2String(Date date) {
        return threadLocal.get().format(date);
    }

    public static Date string2DateTime(String dateStr) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前日期前{beforDate}天
     */
    public static String getDataStrBeforeN(int beforDate) {
        return threadLocal.get().format(getDataBeforeN(beforDate));
    }

    /**
     * 获取当前日期前{beforDate}天
     */
    public static Date getDataBeforeN(int beforDate) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, beforDate * -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前日期前{beforDate}天
     */
    public static Date getDateBeforeN(Date date, int beforDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, beforDate * -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取前月的第一天
     */
    public static String getFirstDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setStartTime(calendar);
        return threadLocal.get().format(calendar.getTime());
    }

    /**
     * 获取前月的最后一天
     */
    public static String getLastDayOfMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        //设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        setEndTime(calendar);
        return format.format(calendar.getTime());
    }

    /**
     * 获取前周的第一天
     */
    public static String getFirstDayOfWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.MONDAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        setStartTime(calendar);
        return format.format(calendar.getTime());
    }

    /**
     * getToDayStartTime
     *
     * @return
     */
    public static String getToDayStartTime(Date date) {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setStartTime(calendar);
        return threadLocal.get().format(calendar.getTime());
    }

    /**
     * getToDayEndTime
     *
     * @return
     */
    public static String getToDayEndTime(Date date) {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setEndTime(calendar);
        return threadLocal.get().format(calendar.getTime());
    }

    /**
     * 获取前周的最后一天
     */
    public static String getLastDayOfWeek() {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.MONDAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        setEndTime(calendar);
        return threadLocal.get().format(calendar.getTime());
    }

    /**
     * set start time hour minute second
     *
     * @param calendar
     */
    public static void setStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    /**
     * set end time hour minute second
     *
     * @param calendar
     */
    public static void setEndTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
    }

    /**
     * 秒转时分
     *
     * @param seconds
     * @return
     */
    public static String timeSecondConvert(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds - hours * 3600) / 60;
        int lastSeconds = seconds - hours * 3600 - minutes * 60;
        return hours + "小时" + minutes + "分钟" + lastSeconds + "秒";
    }

    /**
     * 获取两个时间间隔秒
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int timeInterval(Date startTime, Date endTime) {
        return (int) (Math.abs(endTime.getTime() - startTime.getTime()) / 1000);
    }

    /**
     * 指定日期内时间间隔日期列表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Date> getTimeIntervalList(String startTime, String endTime) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(dateFormat(startTime));
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(dateFormat(endTime));
        List<Date> dateList = new ArrayList<>();
        while (calendarStart.before(calendarEnd)) {
            dateList.add(calendarStart.getTime());
            calendarStart.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

    public static String longTimeToDay(Long ms){
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        return sb.toString();
    }
}
