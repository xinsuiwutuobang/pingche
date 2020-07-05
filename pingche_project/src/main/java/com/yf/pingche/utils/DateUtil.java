package com.yf.pingche.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 文件名称：DateUtils.java
 * 描述：时间工具类
 * Author:xingyh
 * Create Date:2016/7/22 15:49
 */
public class DateUtil {
    public final static int ONE_DAY_MILLS = 86400000; //一天的毫秒数
    public static final int ONE_YEAR = -365;
    public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public final static String DATETIME_MILLI_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String PATTERN_OBLIQUE = "yyyy/MM/dd HH:mm:ss";

    public final static String MM = "MM";

    public final static String YYYYMMDD_HHMM = "yyyyMMddHHmm";

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";

    public final static String HHMMSS = "HH:mm:ss";
    public final static String HHMM = "HH:mm";

    public final static String DEFAULT_PATTERN_CONN = "yyyy-MM-dd-HH-mm-ss";

    public static String YYYYMMDD = "yyyyMMdd";

    public final static String DD = "dd";
    public static String yyyy_MM = "yyyy-MM";

    public final static String YYYYMM = "yyyyMM";
    public static String YYYY_MM = "yyyy-MM";

    public static String YYYYMMDDHH = "yyyy-MM-dd-HH";

    public static String YYYYMMDD_POINT_HH = "yyyy-MM-dd.HH";

    public static String YYMMDD = "yyMMdd";
    public final static String TIME_START_SUFFIX = " 00:00:00";
    public final static String TIME_END_SUFFIX = " 23:59:59";

    public static String yyyyMM = "yyyyMM";

    public static String yyyy = "yyyy";


    public static Date getDateFromStr(String dateStr, String format) {
        Date date = parseDate(dateStr, format);
        return date;
    }

    /**
     * 时间格式转化
     *
     * @param strDate
     * @param format
     * @return
     */
    public static final Date parseDate(String strDate, String format) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format,Locale.CHINA);
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return date;
    }

    /**
     * 格式化date类型的时间
     *
     * @param date
     * @param format
     * @return
     */
    public static Date formatDate(Date date, String format) {
        SimpleDateFormat inDf = new SimpleDateFormat(format);
        SimpleDateFormat outDf = new SimpleDateFormat(format);
        String reDate = "";
        try {
            reDate = inDf.format(date);
            return outDf.parse(reDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long formatDate(String dateTimeStr, String startOrEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);

        if (dateTimeStr != null && !"".equals(dateTimeStr)) {
            if ("start".equals(startOrEnd)) {
                if (!dateTimeStr.trim().contains(" ")) {
                    dateTimeStr = dateTimeStr + " 00:00:00";
                }
            }
            if ("end".equals(startOrEnd)) {
                if (!dateTimeStr.trim().contains(" ")) {
                    dateTimeStr = dateTimeStr + " 23:59:59";
                }
            }
            try {
                Date dateTime = sdf.parse(dateTimeStr);
                return dateTime.getTime();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }

    /**
     * 返回String类型的时间格式
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String getDateStr(Date date, String formatStr) {
        if (StringUtils.isBlank(formatStr)) {
            formatStr = DEFAULT_PATTERN;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        return dateFormat.format(date);
    }

    /**
     * 获取当前系统时间,返回Date类型
     *
     * @return
     */
    public static Date getCurrentDate() {
        return getCurrentDate(DEFAULT_PATTERN);
    }

    /**
     * 获取系统时间 ,返回String类型
     *
     * @return
     */
    public static String getCurrentTime() {
        return getDateStr(getCurrentDate(DEFAULT_PATTERN), DEFAULT_PATTERN);
    }

    /**
     * 获取当前系统时间
     *
     * @param format
     * @return
     */
    public static Date getCurrentDate(String format) {
        return formatDate(new Date(), format);
    }

    /**
     * 将毫秒转成秒的时间戳
     *
     * @param time1
     * @return
     */
    public static Integer millToSecs(long time1) {
        if (time1 < 0) {
            return -1;
        }
        time1 = time1 / 1000;
        if (time1 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return new Long(time1).intValue();
        }
    }

    /**
     * 校验时间格式是否正确
     *
     * @param date
     * @param format
     * @return
     */
    public static boolean checkDateType(String date, String format) {
        SimpleDateFormat s = new SimpleDateFormat(format);
        boolean b = false;
        try {
            Date parDate = s.parse(date);
            b = date.equals(s.format(parDate));
        } catch (ParseException p) {
        }
        return b;
    }


    /**
     * 根据时戳获得时间格式文本
     *
     * @param timestamp
     * @return 时间格式文本 （yyyy-MM-dd HH:mm:ss）
     */
    public static String GetDateByStamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_PATTERN);
        return format.format(date);
    }

    /**
     * 获得当前时间
     *
     * @param sdfStr
     * @return
     */
    public static String getCurrentTime(String sdfStr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(sdfStr);
        return format.format(date);
    }


    /**
     * 获取开始时间到结束时间的秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getcountSeconds(Date startDate, Date endDate) {
        double t1 = startDate.getTime();
        double t2 = endDate.getTime();
        return (int) Math.ceil(Math.abs(t1 - t2) / 1000);
    }
    /**
     * 获取当前时间到今天结束的秒数
     *
     * @return
     */
    public static int getRemainSecondsOneDay(){
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return (int)ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
    }
    /**
     * 获得本月第一天
     *
     * @return
     */
    public static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
        gregorianCalendar.setTime(theDate);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        String first_day = df.format(gregorianCalendar.getTime());
        return first_day;
    }

    /**
     * 获得本月最后一天
     *
     * @return
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
        gregorianCalendar.setTime(theDate);
        gregorianCalendar.set(Calendar.DATE, 1);
        gregorianCalendar.roll(Calendar.DATE, -1);
        String last_day = df.format(gregorianCalendar.getTime());
        return last_day;
    }

    /**
     * 将字符串时间格式转换成默认的日期格式
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date formatString(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        return sdf.parse(time);
    }

    /**
     * 描述: 获取前几天或后几天时间 如：前一天 -1，后一天 1
     * 作者: wangmingli@zhangyue.com
     * 时间: 2013-4-2 下午08:24:50
     */
    public static Date getFutureOrPastDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 返回过去或者未来的日期
     *
     * @param day 过去或者未来的天数，正数：未来${day}天，负数：过去${day}天
     * @return
     */
    public static Date changeDay(int day) {
        return changeDay(new Date(), day);
    }

    /**
     * 返回过去或者未来的日期
     *
     * @param day 过去或者未来的天数，正数：未来${day}天，负数：过去${day}天
     * @return
     */
    public static Date changeDay(Date d, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 设置 增加或者减少
     *
     * @param d
     * @param minute
     * @return
     */
    public static Date changeMinute(Date d, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 获取未来的时间 ，目前系统时间加上分钟数
     *
     * @param minutes
     * @return
     */
    public static Date getFutureDate(int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, minutes);
        return nowTime.getTime();
    }

    /**
     * 比较时间是否在 开始--结束 的有效期内
     */
    public static boolean judgeTime(String startTime, String endTime) {
        long time = System.currentTimeMillis();
        try {
            if (formatString(startTime).getTime() < time
                    && formatString(endTime).getTime() > time) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较时间是否在 开始--结束 的有效期内 时间单位 时间戳 秒
     */
    public static boolean judgeTimeMin(String startTime, String endTime) {
        long time = System.currentTimeMillis() / 1000;
        try {
            if (new BigDecimal(startTime).compareTo(new BigDecimal(time)) <= 0
                    && new BigDecimal(endTime).compareTo(new BigDecimal(time)) >= 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 描述: 计算时间之间的天数差
     */
    public static int getcountDays(String stratDate, String endDate) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(formatString(stratDate));
        long t1 = c.getTime().getTime();
        c.setTime(formatString(endDate));
        long t2 = c.getTime().getTime();
        return (int) java.lang.Math.abs((t1 / 1000 - t2 / 1000) / 3600 / 24);
    }


    /**
     * 获取某一个时间对应的年、月 、天、时、分、秒数据
     * @param date
     * @param params
     * @return
     */
    public static int getDateData(Date date,int params){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(params);
    }


    public static Date formatString(String time, String fromatStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(fromatStr);
        return sdf.parse(time);
    }


    /**
     * @param dateEarly 对比开始日期
     * @param dateLate  对比结束日期
     * @return 返回两个日期相差天数，不包含开始日期。例如，开始日期20120614,结束日期20120625，返回11
     * @throws ParseException
     */
    public static int getDateInterval(String dateEarly, String dateLate, String formater) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formater);
        try {
            if (null == dateLate || null == dateEarly) {
                return -1;
            }
            return (int) Math
                    .floor((dateFormat.parse(dateLate).getTime() - dateFormat.parse(dateEarly)
                            .getTime()) / 1000 / 86400);
        } catch (ParseException e) {
            return -1;
        }
    }

    /**
     * 获得前n天
     *
     * @param date
     * @return Date
     */
    public static Date getNextNDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }


    /**
     * 获取未来或者过去n个月
     *
     * @param month
     * @return
     */
    public static Date getFutureOrPastMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 获取未来或者过去n个月
     *
     * @param month
     * @return
     */
    public static Date getFutureOrPastMonthByDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 获取未来或者过去n年
     *
     * @param year
     * @return
     */
    public static Date getFutureOrPastYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * Timestamp转成指定的String 类型
     *
     * @param ts
     * @param strFormat
     * @return
     */
    public static String dateToString(Timestamp ts, String strFormat) {
        DateFormat df = new SimpleDateFormat(strFormat);
        String tsStr = df.format(ts);
        return tsStr;
    }

    /**
     * 获取未来或者过去的时间
     *
     * @return
     */
    public static Date getFutureOrPast(int number, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(type, number);
        return calendar.getTime();
    }

    /**
     * 获取未来或者过去n个月
     *
     * @return
     */
    public static Date getFutureOrPastByDate(Date date, int number, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, number);
        return calendar.getTime();
    }
    /**
     * 根据当前日期算周一日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }
    /**
     * 根据当前日期算周日日期
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    public static Integer getHHSSSecond(String time) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR,Integer.parseInt(time.substring(0,2)));
        now.set(Calendar.MINUTE,Integer.valueOf(time.substring(3)));
        now.set(Calendar.SECOND,0);
        Long second = now.getTime().getTime() / 1000;
        return second.intValue();
    }

    public static Date parseDateByHHss(String time) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR,Integer.parseInt(time.substring(0,2)));
        now.set(Calendar.MINUTE,Integer.valueOf(time.substring(3)));
        now.set(Calendar.SECOND,0);
        return now.getTime();
    }

    public static void main(String[] args) {
        String aa = "17:33";
        Date date = parseDate(aa, DateUtil.HHMM);
        System.out.println(date);
    }
}
