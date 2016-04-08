package com.jxtech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.i18n.JxLangResourcesUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 处理日期格式
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class DateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "HH:mm:ss";
    public static String YYYY_MM_FORMAT = "yyyy-MM";

    public static String DATE_PATTERN = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

    public static String YYYY_MM_DD = "^\\d{4}-\\d{1,2}\\-\\d{1,2}";
    public static String YYYY_MM = "^\\d{4}-\\d{1,2}";
    public static String YYYYsMsD = "^\\d{4}\\/\\d{1,2}\\/\\d{1,2}";
    public static String YYYYMMDD = "^\\d{4}\\d{1,2}\\d{1,2}";
    public static String YYYYMM = "^\\d{4}\\d{1,2}";
    public static String YY_MM_DD = "^\\d{2}-\\d{1,2}\\-\\d{1,2}";
    public static String YYsMsD = "^\\d{2}\\/\\d{1,2}\\/\\d{1,2}";
    public static String YYMMDD = "^\\d{2}\\d{1,2}\\d{1,2}";

    public static String YY_MM_DD_HH24_MI_SS = "^\\d{4}-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static String YY_MM_DD_HH24_MI = "^\\d{4}-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}:\\d{1,2}$";
    public static String YY_MM_DD_HH24 = "^\\d{4}-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}$";
    public static String YYYYsMsD_HH24_MI_SS = "^\\d{4}\\/\\d{1,2}\\/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static String YYYYsMsD_HH24_MI = "^\\d{4}\\/\\d{1,2}\\/\\d{1,2}\\s\\d{1,2}:\\d{1,2}$";
    public static String YYYYsMsD_HH24 = "^\\d{4}\\/\\d{1,2}\\/\\d{1,2}\\s\\d{1,2}$";
    public static String YYYYMMDD_HH24_MI_SS = "^\\d{4}\\d{1,2}\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static String YYYYMMDD_HH24_MI = "^\\d{4}\\d{1,2}\\d{1,2}\\s\\d{1,2}:\\d{1,2}$";
    public static String YYYYMMDD_HH24 = "^\\d{4}\\d{1,2}\\d{1,2}\\s\\d{1,2}$";
    public static String HH24_MI_SS = "^\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static String HH24_MI = "^\\d{1,2}:\\d{1,2}$";
    // 正则表达式格式：2015-01-01T00:00:00 00:00
    public static String UTC = "^\\d{4}-\\d{1,2}\\-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\s\\d{1,2}:\\d{1,2}$";

    public static String dateToString(Object date, String format) {
        java.util.Date d = null;
        if (date instanceof Long) {
            d = new Date();
            d.setTime(((Long) date).longValue());
        } else if (date instanceof java.lang.String) {
            // 可能会出现String格式，出现String格式将其转换
            d = stringToDate((String) date);
        } else if (date instanceof java.sql.Timestamp) {
            d = toJavaDate((java.sql.Timestamp) date);
        } else if (date instanceof java.util.Date) {
            d = (java.util.Date) date;
        }
        if (d == null) {
            // d = null;// d = new Date(); by dcc
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(d);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将日期转换为：yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String dateTimeToString(Object date) {
        return dateToString(date, DATE_TIME_FORMAT);
    }

    /**
     * 将日期转换为：yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public static String dateToString(Object date) {
        return dateToString(date, DATE_FORMAT);
    }

    public static String monthToString(Object date) {
        return dateToString(date, YYYY_MM_FORMAT);
    }

    /**
     * 将日期转换为：HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String timeToString(Object date) {
        return dateToString(date, TIME_FORMAT);
    }

    /**
     * 将日期转换为Oracle SQL脚本，格式：TO_DATE(date,'YYYY-MM-DD')
     * 
     * @param date
     * @return
     */
    public static String oracleToDate(Object date) {
        String ds = dateToString(date);
        return "TO_DATE('" + ds + "','YYYY-MM-DD')";
    }

    /**
     * 将日期转换为mysql sql脚本，格式：str_to_date(date,'%Y-%m-%d')
     * @param date
     * @return
     */
    public static String mysqlToDate(Object date) {
        String ds = dateToString(date);
        return "str_to_date('" + ds + "','%Y-%m-%d')";
    }
    
    /**
     * 将datetime转换为MySQL sql脚本，格式：str_to_date(date,'%Y-%m-%d %H:%i:%s')
     * @param date
     * @return
     */
    public static String mysqlToDateTime(Object date){
        String ds = dateTimeToString(date);
        return "str_to_date('" + ds + "','%Y-%m-%d %H:%i:%s')"; 
    }
    
    /**
     * 将日期转换为sqlserver 脚本，格式：cast(ds as date)
     * @param date
     * @return
     */
    public static String sqlserverToDate(Object date){
        String ds = dateToString(date);
        return "cast('"+ds+"' as date)";
    }
    
    /**
     * 将datetime转换为sqlserver脚本，格式：cast(ds as datetime)
     * @param date
     * @return
     */
    public static String sqlserverToDateTime(Object date){
        String ds = dateTimeToString(date);
        return "cast('"+ds+"' as datetime)";
    }

    /**
     * 将日期转换为Oracle SQL脚本，格式：TO_DATE(date,'YYYY-MM-DD HH24:MI:SS')
     * 
     * @param date
     * @return
     */
    public static String oracleToDateTime(Object date) {
        String ds = dateTimeToString(date);
        return "TO_DATE('" + ds + "','YYYY-MM-DD HH24:MI:SS')";
    }

    /**
     * 将日期转换为Oracle SQL脚本，格式：TO_DATE(date,'HH24:MI:SS')
     * 
     * @param date
     * @return
     */
    public static String oracleToTime(Object date) {
        String ds = timeToString(date);
        return "TO_DATE('" + ds + "','HH24:MI:SS')";
    }

    /**
     * 获得当前时间
     * 
     * @return
     */
    public static java.util.Date now() {
        return new Date();
    }

    public static java.sql.Timestamp sqlDateTime() {
        return sqlDateTime(null);
    }

    /**
     * 将字符串转为日期，自动分辨格式
     * 
     * @param date
     * @return
     */
    public static java.util.Date stringToDate(String date) {
        if (StrUtil.isNull(date)) {
            return null;
        }
        date = date.trim();
        try {
            if (StrUtil.isFormat(date, YYYY_MM_DD)) {
                return parseToDate(date, "yyyy-MM-dd");
            } else if (StrUtil.isFormat(date, YYYY_MM)) {
                return parseToDate(date, "yyyy-MM");
            } else if (StrUtil.isFormat(date, YYYYMM)) {
                return parseToDate(date, "yyyyMM");
            } else if (StrUtil.isFormat(date, YYYYMMDD)) {
                return parseToDate(date, "yyyyMMdd");
            } else if (StrUtil.isFormat(date, YYYYsMsD)) {
                return parseToDate(date, "yyyy/MM/dd");
            } else if (StrUtil.isFormat(date, YY_MM_DD_HH24_MI_SS)) {
                return parseToDate(date, "yyyy-MM-dd HH:mm:ss");
            } else if (StrUtil.isFormat(date, YY_MM_DD_HH24_MI)) {
                return parseToDate(date, "yyyy-MM-dd HH:mm");
            } else if (StrUtil.isFormat(date, YY_MM_DD_HH24)) {
                return parseToDate(date, "yyyy-MM-dd HH");
            } else if (StrUtil.isFormat(date, YYYYsMsD_HH24_MI_SS)) {
                return parseToDate(date, "yyyy/MM/dd HH:mm:ss");
            } else if (StrUtil.isFormat(date, YYYYsMsD_HH24_MI)) {
                return parseToDate(date, "yyyy/MM/dd HH:mm");
            } else if (StrUtil.isFormat(date, YYYYsMsD_HH24)) {
                return parseToDate(date, "yyyy/MM/dd HH");
            } else if (StrUtil.isFormat(date, YYYYMMDD_HH24_MI_SS)) {
                return parseToDate(date, "yyyyMMdd HH:mm:ss");
            } else if (StrUtil.isFormat(date, YYYYMMDD_HH24_MI)) {
                return parseToDate(date, "yyyyMMdd HH:mm");
            } else if (StrUtil.isFormat(date, YYYYMMDD_HH24)) {
                return parseToDate(date, "yyyyMMdd HH");
            } else if (StrUtil.isFormat(date, HH24_MI_SS)) {
                return parseToDate(date, "HH:mm:ss");
            } else if (StrUtil.isFormat(date, HH24_MI)) {
                return parseToDate(date, "HH:mm");
            } else if ("&SYSDATE&".equalsIgnoreCase(date)) {
                return new Date();
            } else if (StrUtil.isFormat(date, UTC)) {
                String xd = date.substring(0, date.lastIndexOf(' '));
                xd = xd.replace('T', ' ');
                return parseToDate(xd, "yyyy-MM-dd HH:mm:ss");
            } else {
                LOG.debug("日期格式不正确：" + date);
            }
        } catch (ParseException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * 将 date 转换为SQL的时间格式
     * 
     * @param date
     * @return
     */
    public static java.sql.Timestamp toSqlTimestamp(Object date) {
        if (date == null) {
            return null;
        }
        if (date instanceof String) {
            return stringToSqlDate((String) date);
        } else if (date instanceof java.util.Date) {
            return sqlDateTime((java.util.Date) date);
        }
        return null;
    }

    public static java.sql.Timestamp stringToSqlDate(String date) {
        java.util.Date d = stringToDate(date);
        if (d == null) {
            return null;
        }
        return sqlDateTime(d);
    }

    public static java.sql.Timestamp sqlDateTime(java.util.Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        }
        return new java.sql.Timestamp(now().getTime());
    }

    /**
     * 当前时间，加上多少微秒
     * 
     * @param date
     *            为空表示当前时间
     * @param milliseconds
     *            可正可负
     * @return
     */
    public static java.sql.Timestamp sqlDateTimeAdd(java.util.Date date, long milliseconds) {
        long ct;
        if (date == null) {
            ct = System.currentTimeMillis();
        } else {
            ct = date.getTime();
        }
        return new java.sql.Timestamp(ct + milliseconds);
    }

    /**
     * 某个时间，加上多少天
     * 
     * @param date
     * @param day
     * @return
     */
    public static java.sql.Timestamp sqlDateTimeAddDay(java.util.Date date, int day) {
        long ms = day * 86400000;
        return sqlDateTimeAdd(date, ms);
    }

    public static java.sql.Date sqlDate() {
        return sqlDate(null);
    }

    public static java.sql.Date sqlDate(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        }
        return new java.sql.Date(now().getTime());
    }

    public static java.sql.Time sqlTime() {
        return sqlTime(null);
    }

    public static java.sql.Time sqlTime(java.util.Date date) {
        if (date != null) {
            return new java.sql.Time(date.getTime());
        }
        return new java.sql.Time(now().getTime());
    }

    /**
     * 获得某月第一天第一秒 java.sql.Timestamp
     * 
     * @param date
     * @return
     */
    public static java.sql.Timestamp sqlFirtDayOfMonth(Object date) {
        java.util.Date d = toJavaDate(date);
        if (date == null) {
            d = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return toSqlTimestamp(c.getTime());
    }

    /**
     * 获得某月的最后一天最后一秒 java.sql.Timestamp
     * 
     * @param date
     * @return
     */
    public static java.sql.Timestamp sqlLastDayOfMonth(Object date) {
        java.util.Date d = toJavaDate(date);
        if (date == null) {
            d = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MILLISECOND, -1);
        return toSqlTimestamp(c.getTime());
    }

    /**
     * 将时间转换为long
     * 
     * @param date
     * @param format
     * @return
     */
    public static long dateToLong(Object date, String format) {
        if (date == null) {
            return 0;
        }
        if (date instanceof Date) {
            return ((Date) date).getTime();
        }
        Date d = stringToDate((String) date);
        if (d != null) {
            return d.getTime();
        }
        return 0;
    }

    public static long dateToLong(Object date) {
        return dateToLong(date, DATE_FORMAT);
    }

    public static long datetimeToLong(Object date) {
        return dateToLong(date, DATE_TIME_FORMAT);
    }

    public static long timeToLong(Object date) {
        return dateToLong(date, TIME_FORMAT);
    }

    /**
     * 将长型转换为日期
     * 
     * @param date
     * @return
     */
    public static String longToDateTime(long date) {
        Date dt = new Date(date);
        return dateTimeToString(dt);
    }

    /**
     * 使用指定格式格式化日期。为空则使用默认的格式。yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @param pattern
     * @return 传入的参数为空时返回null
     * @throws ParseException
     */
    public static Date parseToDate(String strDate, String pattern) throws ParseException {
        Date date = null;
        if (StrUtil.isNull(pattern)) {
            return parseToDate(strDate);
        }
        if (StrUtil.isNull(strDate)) {
            return null;
        }
        try {
            if (!StrUtil.isNull(strDate)) {
                if (strDate.lastIndexOf(".") > 0) {// 处理字符串后面带.0的时间
                    StringBuilder sb = new StringBuilder(strDate);
                    sb.delete(sb.lastIndexOf("."), sb.length());
                    strDate = sb.toString();
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            date = formatter.parse(strDate);
        } catch (ParseException ex) {
            LOG.error("日期转换错误.输入日期='" + strDate + "'.转换格式='" + pattern + "'.", ex);
            throw ex;
        }
        return date;
    }

    /**
     * 使用默认时间格式格式化。yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return 传入参数为空时，返回null
     * @throws ParseException
     */
    public static Date parseToDate(String strDate) throws ParseException {
        Date date = null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            if (!StrUtil.isNull(strDate)) {
                if (strDate.lastIndexOf(".") > 0) {// 处理字符串后面带.0的时间
                    StringBuffer sb = new StringBuffer(strDate);
                    sb.delete(sb.lastIndexOf("."), sb.length());
                    strDate = sb.toString();
                }
                date = formatter.parse(strDate);
                return date;
            }
        } catch (ParseException ex) {
            LOG.error("日期转换错误.输入日期='" + strDate + "'.转换格式='" + pattern + "'.", ex);
            throw ex;
        }
        return null;
    }

    /**
     * 时间比较大小。yyyy-MM-dd
     * 
     * @param strSmallDate
     *            被减数
     * @param strLargeDate
     *            减数
     * @return 传入参数为空时，返回null
     * @throws ParseException
     */
    public static Long compareDate(String strSmallDate, String strLargeDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            if (!StrUtil.isNull(strSmallDate) && !StrUtil.isNull(strLargeDate)) {
                d1 = df.parse(strLargeDate);
                d2 = df.parse(strSmallDate);
                Long diff = d1.getTime() - d2.getTime();
                Long days = diff / (1000 * 60 * 60 * 24);
                return days;
            }
        } catch (ParseException e) {
            LOG.error("日期转换错误.输入日期='" + strSmallDate + "'.转换格式='" + strLargeDate + "'." + e);
        }
        return null;
    }

    /**
     * 返回两个时间差，如果某值为空，则默认为当前时间
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static double subDays(Object date1, Object date2) {
        Date d1 = DateUtil.toSqlTimestamp(date1);
        if (d1 == null) {
            d1 = now();
        }
        Date d2 = DateUtil.toSqlTimestamp(date2);
        if (d2 == null) {
            d2 = now();
        }
        long dif = d1.getTime() - d2.getTime();
        return (dif / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取年
     * 
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        if (date == null) {
            c.setTime(new Date());
        } else {
            c.setTime(date);
        }
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取月份
     * 
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        if (date == null) {
            c.setTime(new Date());
        } else {
            c.setTime(date);
        }
        int month = c.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取date事件是date年的第几周
     * 
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        GregorianCalendar g = new GregorianCalendar();
        if (date == null) {
            g.setTime(new Date());
        } else {
            g.setTime(date);
        }
        /* 获得周数 */
        return g.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取季度
     * 
     * @param month
     * @return
     */
    public static int getQuarter(int month) {
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else if (month >= 10 && month <= 12) {
            quarter = 4;
        }
        return quarter;
    }

    /**
     * 得到本日的前几个月日期 by dcc
     */
    public static String getDateBeforeMonth(int i) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, i);
        return dateToString(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 得到某年某周的第一天
     * 
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
        return cal.getTime();
    }

    /**
     * @param date
     * @param w
     * @return
     */
    public static Date getWeekStart(Date date, int w) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 1);
        c.add(Calendar.WEEK_OF_MONTH, w);
        return c.getTime();
    }

    public static Date getWeekEnd(Date date, int w) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 7);
        c.add(Calendar.WEEK_OF_MONTH, w);
        return c.getTime();
    }

    /**
     * 
     * @param date
     *            基础时间
     * @param year
     *            延长的年数
     * @return
     */
    public static Date getDateAfterYear(Date date, int year) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + year);
            Date afterdate = c.getTime();
            return afterdate;
        } catch (Exception e) {
            LOG.error("getDateAfterYear(Date date,int i)日期传入有误'" + date);
        }
        return null;
    }

    /**
     * 将java.sql.Timestamp转换为：java.util.Date
     * 
     * @param time
     * @return
     */
    public static java.util.Date toJavaDate(Object time) {
        if (time == null) {
            return null;
        }
        if (time instanceof String) {
            return stringToDate((String) time);
        } else if (time instanceof java.util.Date) {
            return (java.util.Date) time;
        } else if (time instanceof java.sql.Timestamp) {
            return new java.util.Date(((java.sql.Timestamp) time).getTime());
        } else if (time instanceof java.sql.Date) {
            return new java.util.Date(((java.sql.Date) time).getTime());
        }
        return null;
    }

    /**
     * 
     * @param date
     *            基础时间
     * @param month
     *            延长月数
     * @return
     */
    public static Date getDateAfterMonth(Date date, int month) {
        Date afterdate = null;
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, month);
            afterdate = c.getTime();
            return afterdate;
        } catch (Exception e) {
            LOG.error("getDateAfterMonth(Date date,int i)日期传入有误'" + date);
        }
        return afterdate;
    }

    /**
     * 日期加减
     * 
     * @param date
     * @param day
     * @return
     */
    public static Date addDate(Date date, int day) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, day);
            return c.getTime();
        } catch (Exception e) {
            LOG.error("addDate(Date date,int i)日期传入有误'" + date);
            return null;
        }
    }

    /**
     * 根据年，周数取的指定的周数范围内的 当前星期数的日期
     * 
     * @param year
     * @param weekNum
     * @return
     */
    public static Date getDateByYearAndWeekNum(int year, int weekNum) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, weekNum);
        return c.getTime();
    }

    /**
     * 将多少秒转换为：2天5小时6分钟这样的格式
     * 
     * @param time
     *            单位秒
     * @return
     */
    public static String formatDayHourMins(long time) {
        if (time < 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int day = (int) (time / (24 * 60 * 60));
        if (day > 0) {
            sb.append(day).append(JxLangResourcesUtil.getString("dateUtil.day")).append(" ");
        }
        int hour = (int) ((time - day * 24 * 60 * 60) / (60 * 60));
        sb.append(hour).append(JxLangResourcesUtil.getString("dateUtil.hour")).append(" ");
        int min = (int) ((time - day * 24 * 60 * 60 - hour * 60 * 60) / 60);
        sb.append(min).append(JxLangResourcesUtil.getString("dateUtil.minutes"));
        return sb.toString();
    }
}
