package com.jxtech.jbo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.util.StrUtil;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.text.*;
import java.util.*;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxFormat {
    private static final Logger LOG = LoggerFactory.getLogger(JxFormat.class);

    public static final int ALN = 0;
    public static final int UPPER = 1;
    public static final int LOWER = 2;
    public static final int DATE = 3;
    public static final int DATETIME = 4;
    public static final int TIME = 5;
    public static final int INTEGER = 6;
    public static final int SMALLINT = 7;
    public static final int FLOAT = 8;
    public static final int DECIMAL = 9;
    public static final int DURATION = 10;
    public static final int AMOUNT = 11;
    public static final int YORN = 12;
    public static final int GL = 13;
    public static final int LONGALN = 14;
    public static final int CRYPTO = 15;
    public static final int CRYPTOX = 16;
    public static final int CLOB = 17;
    public static final int BLOB = 18;
    public static final int BIGINT = 19;
    public static final int NUMBER = 20;
    public static final int UDTYPE = 99;
    public static final String[] TYPEASSTRING = { "ALN", "UPPER", "LOWER", "DATE", "DATETIME", "TIME", "INTEGER", "SMALLINT", "FLOAT", "DECIMAL", "DURATION", "AMOUNT", "YORN", "GL", "LONGALN", "CRYPTO", "CRYPTOX", "CLOB", "BLOB", "BIGINT", "NUMBER" };
    public static final int UNLIMITEDPLACES = 8;
    public static final String TIMESEP = ":";
    public static final String DATESEP = "/";
    public static final char YEARCHAR = 'y';
    public static final char MONTHCHAR = 'M';
    public static final char DAYCHAR = 'd';
    public static final char AMCHAR = 'a';
    public static final char MINCHAR = 'm';
    public static final char HOUR24CHAR = 'H';
    public static final char HOUR12CHAR = 'h';
    public static final char SECONDCHAR = 's';
    public static final String SEPERATORS = " /-.:";
    public static final double MINSPERHOUR = 60.0D;
    public static final String SPECIALSAVECHARS = "=: \t\r\n\f#!";
    public static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static final HashMap<String, String> settingProps = new HashMap<String, String>();

    public static int stringToInt(String s) throws JxException {
        return stringToInt(s, Locale.getDefault());
    }

    public static int stringToInt(String s, Locale l) throws JxException {
        if (s == null) {
            // Object[] params = { s };
            Object[] params = { "Transformation parameter value is empty" };
            throw new JxException("system", "invalidnumberint", params);
        }

        if (isZero(s, l)) {
            return 0;
        }
        s = fixRussianInputString(s, l);

        NumberFormat fmt = NumberFormat.getInstance(l);
        fmt.setParseIntegerOnly(true);
        Number num = null;
        try {
            ParsePosition parsePosition = new ParsePosition(0);
            num = fmt.parse(s, parsePosition);

            if (parsePosition.getIndex() != s.length()) {
                throw new ParseException("parseerror", 0);
            }
        } catch (ParseException e) {
            Object[] params = { s };
            throw new JxException("system", "invalidnumberint", params);
        }

        return num.intValue();
    }

    public static long stringToLong(String s) throws JxException {
        return stringToLong(s, Locale.getDefault());
    }

    public static long stringToLong(String s, Locale l) throws JxException {
        if ((s == null) || ((s.indexOf(44) != -1) && (s.length() < 5))) {
            Object[] params = { s };
            throw new JxException("system", "invalidnumberint", params);
        }

        if (isZero(s, l)) {
            return 0L;
        }
        s = fixRussianInputString(s, l);

        NumberFormat fmt = NumberFormat.getInstance(l);
        fmt.setParseIntegerOnly(true);
        Number num = null;
        try {
            ParsePosition parsePosition = new ParsePosition(0);
            num = fmt.parse(s, parsePosition);

            if (parsePosition.getIndex() != s.length()) {
                throw new ParseException("parseerror", 0);
            }

        } catch (ParseException e) {
            Object[] params = { s };
            throw new JxException("system", "invalidnumberint", params);
        }

        if (num.longValue() == 9223372036854775807L) {
            DecimalFormatSymbols symb = new DecimalFormatSymbols(l);
            char tc = symb.getGroupingSeparator();
            String outStr = s;
            if (s.indexOf(tc) > -1) {
                StringBuilder r = new StringBuilder(s.length());
                r.setLength(s.length());
                int current = 0;
                for (int i = 0; i < s.length(); i++) {
                    char cur = s.charAt(i);
                    if (cur != tc)
                        r.setCharAt(current++, cur);
                }
                outStr = r.toString();
            }
            String rets = String.valueOf(num.longValue());
            if ((outStr != null) && (!outStr.trim().equalsIgnoreCase(rets))) {
                Object[] params = { s };
                throw new JxException("system", "invalidnumberint", params);
            }

        }

        return num.longValue();
    }

    private static boolean isZero(String s, Locale l) {
        DecimalFormatSymbols symb = new DecimalFormatSymbols(l);

        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) != symb.getDecimalSeparator()) && (s.charAt(i) != symb.getGroupingSeparator()) && (s.charAt(i) != '0') && (s.charAt(i) != symb.getMinusSign())) {
                return false;
            }
        }
        return true;
    }

    public static String intToString(int i) {
        return intToString(i, Locale.getDefault());
    }

    public static String intToString(int i, Locale l) {
        NumberFormat fmt = NumberFormat.getInstance(l);
        return fmt.format(i);
    }

    public static String longToString(long i) {
        return longToString(i, Locale.getDefault());
    }

    public static String longToString(long i, Locale l) {
        NumberFormat fmt = NumberFormat.getInstance(l);
        return fmt.format(i);
    }

    public static double stringToAmount(String s) throws JxException {
        return stringToAmount(s, Locale.getDefault());
    }

    public static double stringToAmount(String s, Locale l) throws JxException {
        if (s == null) {
            // Object[] params = { s };
            Object[] params = { "Transformation parameter value is empty" };
            throw new JxException("system", "invalidnumberintdec", params);
        }

        if (isZero(s, l)) {
            return 0.0D;
        }
        try {
            return stringToDouble(s, l);
        } catch (JxException e2) {
            Object[] params = { s };
            throw new JxException("system", "invalidnumberintdec", params);
        }
    }

    public static String amountToString(double d) {
        return amountToString(d, 8, Locale.getDefault());
    }

    public static String amountToString(double d, int places) {
        return amountToString(d, places, Locale.getDefault());
    }

    public static String amountToString(double d, int places, Locale l) {
        return doubleToString(d, places, l);
    }

    public static double stringToDouble(String s) throws JxException {
        return stringToDouble(s, Locale.getDefault());
    }

    public static double stringToDouble(String s, Locale l) throws JxException {
        if (s == null) {
            // Object[] params = { s };
            Object[] params = { "Transformation parameter value is empty" };
            throw new JxException("system", "invalidnumberintdec", params);
        }

        if (isZero(s, l)) {
            return 0.0D;
        }
        s = fixRussianInputString(s, l);

        NumberFormat fmt = NumberFormat.getInstance(l);
        Number num = null;
        try {
            ParsePosition parsePosition = new ParsePosition(0);
            num = fmt.parse(s, parsePosition);

            if (parsePosition.getIndex() != s.length()) {
                throw new ParseException("parseerror", 0);
            }
        } catch (ParseException e) {
            Object[] params = { s };
            throw new JxException("system", "invalidnumberintdec", params);
        }

        return num.doubleValue();
    }

    public static String doubleToString(double d) {
        return doubleToString(d, 8, Locale.getDefault());
    }

    public static String doubleToString(double d, Locale l) {
        return doubleToString(d, 8, l);
    }

    public static String doubleToString(double d, int places) {
        return doubleToString(d, places, Locale.getDefault());
    }

    public static String doubleToString(double d, int places, Locale l) {
        return doubleToString(d, places, l, true);
    }

    public static String doubleToString(double d, int places, Locale l, boolean groupingInFormat) {
        NumberFormat fmt = NumberFormat.getInstance(l);
        fmt.setMaximumFractionDigits(places);

        fmt.setGroupingUsed(groupingInFormat);

        if (places != 8) {
            fmt.setMinimumFractionDigits(places);
        }

        String dVal = "" + d;
        BigDecimal bd = new BigDecimal(dVal);
        bd = bd.setScale(places, 4);
        double x = bd.doubleValue();

        return fmt.format(x);
    }

    public static double durationToDouble(String s) throws JxException {
        return durationToDouble(s, Locale.getDefault());
    }

    public static double durationToDouble(String s, Locale l) throws JxException {
        int hours = 0;
        int mins = 0;
        boolean minus = false;
        try {
            char DECIMALSEP = new DecimalFormatSymbols(l).getDecimalSeparator();

            if (s.indexOf(':') == -1) {
                return stringToDouble(s, l);
            }

            if (s.indexOf(DECIMALSEP) != -1) {
                throw new JxException("system", "invalidduration", new Object[] { s });
            }

            if (s.startsWith("-")) {
                s = s.substring(1);
                minus = true;
            }

            StringTokenizer st = new StringTokenizer(s, ":");
            if (st.hasMoreTokens()) {
                hours = stringToInt(st.nextToken(), l);
                if (st.hasMoreTokens()) {
                    mins = stringToInt(st.nextToken(), l);
                } else if (s.startsWith(":")) {
                    mins = hours;
                    hours = 0;
                }
            }
        } catch (JxException e) {
            throw new JxException("system", "invalidduration", new Object[] { s });
        }

        double dl = 0.0D;
        if (minus) {
            double tmpDl = hours + mins / 60.0D;
            String tmpStr = "-" + new Double(tmpDl).toString();
            dl = new Double(tmpStr).doubleValue();
        } else {
            dl = hours + mins / 60.0D;
        }
        return dl;
    }

    public static String doubleToDuration(double d) {
        return doubleToDuration(d, Locale.getDefault());
    }

    public static String doubleToDuration(double d, Locale l) {
        boolean minus = false;

        if (d < 0.0D) {
            d = Math.abs(d);
            minus = true;
        }

        int hours = (int) d;
        int mins = (int) Math.round((d - hours) * 60.0D);

        if (mins == 60) {
            hours++;
            mins = 0;
        }

        String retStr = null;
        if (mins < 10)
            retStr = StrUtil.contact(String.valueOf(hours), ":0", String.valueOf(mins));
        else {
            retStr = StrUtil.contact(String.valueOf(hours), ":" + String.valueOf(mins));
        }
        if (minus) {
            retStr = "-" + retStr;
        }
        return retStr;
    }

    public static boolean stringToBoolean(String s) throws JxException {
        return stringToBoolean(s, Locale.getDefault());
    }

    public static boolean stringToBoolean(String s, Locale l) throws JxException {
        if (s == null) {
            throw new JxException("system", "invalidyorn");
        }

        if ("true".equalsIgnoreCase(s))
            return true;
        if ("false".equalsIgnoreCase(s)) {
            return false;
        }
        if (s.equalsIgnoreCase(getStoreYesValue()))
            return true;
        if (s.equalsIgnoreCase(getStoreNoValue())) {
            return false;
        }
        if (s.equalsIgnoreCase(getDisplayYesValue(l)))
            return true;
        if (s.equalsIgnoreCase(getDisplayNoValue(l))) {
            return false;
        }

        throw new JxException("system", "invalidyorn");
    }

    public static String booleanToString(boolean b) {
        return booleanToString(b, Locale.getDefault());
    }

    public static String booleanToString(boolean b, Locale l) {
        if (b) {
            return getDisplayYesValue(l);
        }
        return getDisplayNoValue(l);
    }

    public static int booleanToInt(boolean b) {
        if (b) {
            return 1;
        }
        return 0;
    }

    public static double booleanToDouble(boolean b) {
        if (b) {
            return 1.0D;
        }
        return 0.0D;
    }

    public static long booleanToLong(boolean b) {
        if (b) {
            return 1L;
        }
        return 0L;
    }

    public static Date stringToDate(String s) throws JxException {
        return stringToDate(s, Locale.getDefault(), TimeZone.getDefault());
    }

    public static Date stringToDate(String s, Locale l) throws JxException {
        return stringToDate(s, l, TimeZone.getDefault());
    }

    public static Date stringToDate(String s, Locale l, TimeZone tz) throws JxException {
        if (s == null) {
            throw new JxException("system", "invaliddate");
        }

        try {
            String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());

            if ((dateFormat == null) || ("".equals(dateFormat))) {
                dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
            }

            SimpleDateFormat df = new SimpleDateFormat(dateFormat, l);

            int[] value = parseDateTime(s, l, df.toPattern());

            Calendar c = Calendar.getInstance(tz, l);
            c.setLenient(false);
            c.set(value[2], value[1] - 1, value[0], 0, 0, 0);

            return c.getTime();
        } catch (Exception e) {
            try {
                Date dateTime = stringToDateTime(s, l, tz);
                return getDateOnly(dateTime);
            } catch (JxException ex) {
            }
        }
        throw new JxException("system", "invaliddate");
    }

    public static Date stringToDate(String strDate, String strFormat) throws JxException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(strFormat);
        try {
            return dateFormatter.parse(strDate);
        } catch (Exception e) {
        }
        throw new JxException("system", "invaliddatetime");
    }

    public static Date sqlWindowsStringToDateTime(String s) throws JxException {
        if (s == null) {
            throw new JxException("system", "invaliddate");
        }
        try {
            Locale l = Locale.getDefault();
            TimeZone tz = TimeZone.getDefault();

            int[] value = parseDateTime(s.substring(0, 18), l, "yyyy-MM-dd-HH.mm.ss");

            Calendar c = Calendar.getInstance(l);
            c.setTimeZone(tz);

            c.setLenient(false);
            c.set(5, value[0]);
            c.set(2, value[1] - 1);
            c.set(1, value[2]);
            c.set(11, value[3]);
            c.set(12, value[4]);
            c.set(13, value[5]);
            c.set(14, 0);

            return c.getTime();
        } catch (Exception e) {
            throw new JxException("system", "invaliddatetime", e);
        }
    }

    public static Date jmigStringToDateTime(String s) throws JxException {
        if ((s == null) || (s.length() == 0)) {
            throw new JxException("system", "invaliddate");
        }
        if (s.charAt(0) == '-') {
            s = s.substring(1);
        }

        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = sdf.parse(s);
            if ((s.length() <= 19) || (s.charAt(s.length() - 1) == 'Z')) {
                return date;
            }

            char addSubtract = 'Z';
            String remainingString = s.substring(19);
            int indexOf = remainingString.indexOf(43);
            if (indexOf != -1) {
                addSubtract = '+';
                remainingString = remainingString.substring(indexOf + 1);
            } else if (remainingString.indexOf(45) != -1) {
                addSubtract = '-';
                indexOf = remainingString.indexOf(45);
                remainingString = remainingString.substring(indexOf + 1);
            } else {
                addSubtract = 'Z';
                remainingString = null;
            }

            if (addSubtract == 'Z') {
                return date;
            }
            long milliseconds = 0L;
            int remainingHours = Integer.parseInt(remainingString.substring(0, 2));
            int remainingMinutes = Integer.parseInt(remainingString.substring(3, 5));
            milliseconds = (remainingHours * 60 + remainingMinutes) * 60 * 1000L;

            if (addSubtract == '+') {
                milliseconds = -milliseconds;
            }
            date.setTime(date.getTime() + milliseconds);
            return date;
        } catch (Exception e) {
            throw new JxException("system", "invaliddatetime", e);
        }
    }

    public static String dateToString(Date d) {
        return dateToString(d, Locale.getDefault(), TimeZone.getDefault());
    }

    public static String dateToString(Date d, Locale l) {
        return dateToString(d, l, TimeZone.getDefault());
    }

    public static String dateToString(Date d, Locale l, TimeZone tz) {
        Calendar c = Calendar.getInstance(tz, l);
        c.setLenient(false);
        c.setTime(d);

        String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());

        if ((dateFormat == null) || ("".equals(dateFormat))) {
            dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat, l);

        df.setTimeZone(tz);

        return df.format(getDateOnly(c.getTime()));
    }

    public static Date stringToDateTime(String s) throws JxException {
        return stringToDateTime(s, Locale.getDefault(), TimeZone.getDefault());
    }

    public static Date stringToDateTime(String s, Locale l) throws JxException {
        return stringToDateTime(s, l, TimeZone.getDefault());
    }

    public static Date stringToDateTime(String s, Locale l, TimeZone tz) throws JxException {
        try {
            String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());
            String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());

            if ((dateFormat == null) || ("".equals(dateFormat))) {
                dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
            }

            if ((timeFormat == null) || ("".equals(timeFormat))) {
                timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
            }

            SimpleDateFormat df = new SimpleDateFormat(dateFormat + " " + timeFormat, l);

            int[] value = parseDateTime(s, l, df.toPattern());

            Calendar c = Calendar.getInstance(l);
            c.setTimeZone(tz);
            c.setLenient(false);
            c.set(5, value[0]);
            c.set(2, value[1] - 1);
            c.set(1, value[2]);
            c.set(11, value[3]);
            c.set(12, value[4]);
            c.set(13, value[5]);
            c.set(14, 0);

            return c.getTime();
        } catch (Exception e) {
            try {
                return jmigStringToDateTime(s);
            } catch (Exception e1) {
            }
        }
        throw new JxException("system", "invaliddatetime");
    }

    public static String dateTimeToParseString(Date d) {
        return dateTimeToParseString(d, Locale.getDefault(), TimeZone.getDefault());
    }

    public static String dateTimeToParseString(Date d, Locale l) {
        return dateTimeToParseString(d, l, TimeZone.getDefault());
    }

    public static String dateTimeToParseString(Date d, Locale l, TimeZone tz) {
        String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());
        String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());

        if ((dateFormat == null) || ("".equals(dateFormat))) {
            dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
        }

        if (dateFormat.indexOf("yyyy") == -1) {
            int ind = dateFormat.indexOf("yy");
            if (ind > -1) {
                StringBuilder strBuf = new StringBuilder(dateFormat);
                strBuf.insert(ind, "yy");
                dateFormat = strBuf.toString();
            }
        }

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat + " " + timeFormat, l);

        df.setTimeZone(tz);
        return df.format(d);
    }

    public static String dateTimeToString(Date d) {
        return dateTimeToString(d, Locale.getDefault(), TimeZone.getDefault());
    }

    public static String dateTimeToString(Date d, Locale l) {
        return dateTimeToString(d, l, TimeZone.getDefault());
    }

    public static String dateTimeToString(Date d, Locale l, TimeZone tz) {
        String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());
        String timeFormat = getSettingProp("setting.DISPLAYTIME", l.getLanguage());

        if ((dateFormat == null) || ("".equals(dateFormat))) {
            dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
        }

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(3, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat + " " + timeFormat, l);

        df.setTimeZone(tz);
        return df.format(d);
    }

    public static String dateTimeToSQLString(Date d) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(d);
    }

    public static String dateToSQLString(Date d) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(d);
    }

    public static String timeToSQLString(Date d) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(d);
    }

    public static Date stringToTime(String s) throws JxException {
        return stringToTime(s, Locale.getDefault(), TimeZone.getDefault());
    }

    public static Date stringToTime(String s, Locale l) throws JxException {
        return stringToTime(s, l, TimeZone.getDefault());
    }

    public static Date stringToTime(String s, Locale l, TimeZone tz) throws JxException {
        boolean isError = false;
        try {
            DecimalFormatSymbols symb = new DecimalFormatSymbols(l);
            Character minusSign = Character.valueOf(symb.getMinusSign());

            if (minusSign != null) {
                if (s.startsWith(minusSign.toString())) {
                    isError = true;
                    throw new JxException("system", "invalidtime");
                }

            }

            String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());

            if ((timeFormat == null) || ("".equals(timeFormat))) {
                timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
            }

            SimpleDateFormat df = new SimpleDateFormat(timeFormat, l);

            int[] value = parseDateTime(s, l, df.toPattern());

            if (value[3] > 24) {
                isError = true;
            }
            Calendar c = Calendar.getInstance(l);
            c.setTimeZone(tz);

            c.setLenient(false);
            c.set(5, 1);
            c.set(2, 0);
            c.set(1, 1970);
            c.set(11, value[3]);
            c.set(12, value[4]);
            c.set(13, value[5]);
            c.set(14, 0);

            return c.getTime();
        } catch (Exception e) {
            try {
                if (isError) {
                    throw new JxException("system", "invalidtime");
                }
                return stringToDateTime(s, l, tz);
            } catch (JxException ex) {
            }
        }
        throw new JxException("system", "invalidtime");
    }

    public static String timeToParsetring(Date d) {
        return timeToParseString(d, Locale.getDefault(), TimeZone.getDefault());
    }

    public static String timeToParseString(Date d, Locale l) {
        return timeToParseString(d, l, TimeZone.getDefault());
    }

    public static String timeToParseString(Date d, Locale l, TimeZone tz) {
        String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(timeFormat, l);

        df.setTimeZone(tz);
        return df.format(d);
    }

    public static String timeToString(Date d) {
        return timeToString(d, Locale.getDefault(), TimeZone.getDefault());
    }

    public static String timeToString(Date d, Locale l) {
        return timeToString(d, l, TimeZone.getDefault());
    }

    public static String timeToString(Date d, Locale l, TimeZone tz) {
        String timeFormat = getSettingProp("setting.DISPLAYTIME", l.getLanguage());

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(3, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(timeFormat, l);

        df.setTimeZone(tz);
        return df.format(d);
    }

    public static Date getDateOnly(Date date) {
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(date);
        g.set(11, 0);
        g.set(12, 0);
        g.set(13, 0);
        g.set(14, 0);

        return g.getTime();
    }

    public static int getMaxTypeAsInt(String type) {
        for (int i = 0; i < TYPEASSTRING.length; i++) {
            if (TYPEASSTRING[i].equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public static int[] parseDateTime(String s, Locale l, String pattern) throws JxException {
        boolean yearentered = false;
        boolean ampmValidate = false;

        int[] value = { 0, 0, 0, 0, 0, 0 };
        int tokencount = 0;

        String ampm = null;

        DateFormatSymbols df = new DateFormatSymbols(l);
        String[] pm = df.getAmPmStrings();

        for (int i = 0; i < pm.length; i++) {
            if ((s.indexOf(pm[i].toUpperCase()) > -1) || (s.indexOf(pm[i].toLowerCase()) > -1)) {
                ampm = pm[i];
            }
        }
        StringBuilder tmpStr = new StringBuilder(s);
        if (ampm != null) {
            int index = s.indexOf(ampm);
            if ((index > -1) && (index + ampm.length() < s.length()) && (tmpStr.charAt(index + 1) != ' ')) {
                tmpStr.insert(index + ampm.length(), ' ');
            }
        }
        StringTokenizer st = new StringTokenizer(tmpStr.toString(), " /-.:");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            try {
                int pos = tokenToPosition(tokencount, pattern);
                value[pos] = stringToInt(token, l);

                if (pos == 2)
                    yearentered = true;
            } catch (JxException e) {
                int am;
                if ((am = stringToAmPm(token, l)) != -1) {
                    ampmValidate = true;

                    if ((value[3] > 0) && (value[3] <= 12)) {
                        if (value[3] == 12)
                            value[3] -= 12;
                        value[3] += am * 12;
                    } else {
                        throw new JxException("system", "invaliddate");
                    }
                } else {
                    int m;
                    if ((m = stringToMonth(token, l)) != -1) {
                        if (value[1] != 0)
                            value[0] = value[1];
                        value[1] = m;
                    } else {
                        throw new JxException("system", "invaliddate");
                    }
                }
            }
            tokencount++;
        }

        if ((ampm != null) && (!ampmValidate)) {
            throw new JxException("system", "invaliddate");
        }

        Calendar c = Calendar.getInstance(l);
        c.setTime(new Date());

        if ((value[1] != 0) && (value[0] == 0) && (value[2] == 0)) {
            value[0] = value[1];
            value[1] = 0;
        } else if ((value[2] != 0) && (value[0] == 0) && (value[1] == 0)) {
            value[0] = value[2];
            value[2] = 0;
            yearentered = false;
        }

        if (value[1] == 0) {
            value[1] = (c.get(2) + 1);
        }

        if (value[0] == 0) {
            value[0] = c.get(5);
        }
        if (!yearentered) {
            value[2] = c.get(1);
        } else if (value[2] < 100) {
            int currYear = c.get(1) % 100;
            int currCentury = c.get(1) / 100 * 100;

            if ((currYear >= 0) && (currYear <= 49) && (value[2] >= 0) && (value[2] <= 49)) {
                value[2] += currCentury;
            } else if ((currYear >= 50) && (currYear <= 99) && (value[2] >= 0) && (value[2] <= 49)) {
                value[2] += currCentury + 100;
            } else if ((currYear >= 0) && (currYear <= 49) && (value[2] >= 50) && (value[2] <= 99)) {
                value[2] += currCentury - 100;
            } else if ((currYear >= 50) && (currYear <= 99) && (value[2] >= 50) && (value[2] <= 99)) {
                value[2] += currCentury;
            }
        } else if ((value[2] > 9999) || (value[2] <= 999)) {
            throw new JxException("system", "invaliddate");
        }
        return value;
    }

    private static int stringToAmPm(String s, Locale l) {
        DateFormatSymbols df = new DateFormatSymbols(l);

        s = s.toUpperCase();
        String[] pm = df.getAmPmStrings();

        for (int i = 0; i < pm.length; i++) {
            if (s.equals(pm[i].toUpperCase()))
                return i;
        }
        return -1;
    }

    private static int stringToMonth(String s, Locale l) {
        DateFormatSymbols df = new DateFormatSymbols(l);

        s = s.toUpperCase();
        String[] shortmnths = df.getShortMonths();
        for (int i = 0; i < shortmnths.length; i++) {
            if (s.equals(shortmnths[i].toUpperCase()))
                return i + 1;
        }
        String[] mnths = df.getMonths();
        for (int i = 0; i < mnths.length; i++) {
            if (s.equals(mnths[i].toUpperCase()))
                return i + 1;
        }
        return -1;
    }

    private static int tokenToPosition(int tokennum, String pattern) {
        StringTokenizer st = new StringTokenizer(pattern, " /-.:");
        int i = 0;

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if ((s.indexOf(100) != -1) && (i == tokennum))
                return 0;
            if ((s.indexOf(77) != -1) && (i == tokennum))
                return 1;
            if ((s.indexOf(121) != -1) && (i == tokennum))
                return 2;
            if ((s.indexOf(72) != -1) && (i == tokennum))
                return 3;
            if ((s.indexOf(104) != -1) && (i == tokennum))
                return 3;
            if ((s.indexOf(109) != -1) && (i == tokennum))
                return 4;
            if ((s.indexOf(115) != -1) && (i == tokennum))
                return 5;
            i++;
        }

        return -1;
    }

    public static boolean isTimePartEntered(String s, Locale l) {
        int[] value = { 0, 0, 0, 0, 0, 0 };
        int tokencount = 0;
        try {
            String pattern = getDateTimePattern(l);
            String ampm = null;

            DateFormatSymbols df = new DateFormatSymbols(l);
            String[] pm = df.getAmPmStrings();

            for (int i = 0; i < pm.length; i++) {
                if ((s.indexOf(pm[i].toUpperCase()) > -1) || (s.indexOf(pm[i].toLowerCase()) > -1)) {
                    ampm = pm[i];
                }
            }
            StringBuilder tmpStr = new StringBuilder(s);
            if (ampm != null) {
                int index = s.indexOf(ampm);
                if ((index > -1) && (tmpStr.charAt(index + 1) != ' ') && (index + ampm.length() < s.length())) {
                    tmpStr.insert(index + ampm.length(), ' ');
                }
            }
            StringTokenizer st = new StringTokenizer(tmpStr.toString(), " /-.:");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                try {
                    int pos = tokenToPosition(tokencount, pattern);
                    value[pos] = stringToInt(token, l);
                } catch (JxException e) {
                    int am;
                    if ((am = stringToAmPm(token, l)) != -1) {
                        if ((value[3] > 0) && (value[3] <= 12)) {
                            if (am == 1) {
                                if (value[3] == 12)
                                    value[3] -= 12;
                                value[3] += am * 12;
                            }
                        }
                    }
                }

                tokencount++;
            }

            if ((value[3] > 0) || (value[4] > 0) || (value[5] > 0)) {
                return true;
            }

        } catch (Exception ee) {
        }

        return false;
    }

    public static String getStoreYesValue() {
        return "1";
    }

    public static String getStoreNoValue() {
        return "0";
    }

    public static String getDisplayYesValue(Locale l) {
        return getSettingProp("setting.YES", l.getLanguage());
    }

    public static String getDisplayNoValue(Locale l) {
        return getSettingProp("setting.NO", l.getLanguage());
    }

    public static String convertToStoreYNValue(String val, Locale l) throws JxException {
        boolean isYes = stringToBoolean(val, l);
        if (isYes) {
            return getStoreYesValue();
        }
        return getStoreNoValue();
    }

    public static String clobToString(Clob c) {
        try {
            long pos = 1L;
            int len = (int) c.length();
            return c.getSubString(pos, len);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return "";
    }

    public static byte[] blobToBytes(Blob b) {
        byte[] array = null;
        try {
            long pos = 1L;
            int len = (int) (b.length());
            array = b.getBytes(pos, len);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return array;
    }

    public static String getDatePattern(Locale l) {
        String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());

        if ((dateFormat == null) || ("".equals(dateFormat))) {
            dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat, l);
        return df.toPattern();
    }

    public static String getTimePattern(Locale l) {
        String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(timeFormat, l);
        return df.toPattern();
    }

    public static String getDisplayTimePattern(Locale l) {
        String timeFormat = getSettingProp("setting.DISPLAYTIME", l.getLanguage());

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(3, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(timeFormat, l);
        return df.toPattern();
    }

    public static String getDateTimePattern(Locale l) {
        String timeFormat = getSettingProp("setting.PARSETIME", l.getLanguage());
        String dateFormat = getSettingProp("setting.DISPLAYDATE", l.getLanguage());

        if ((dateFormat == null) || ("".equals(dateFormat))) {
            dateFormat = ((SimpleDateFormat) DateFormat.getDateInstance(3, l)).toPattern();
        }

        if ((timeFormat == null) || ("".equals(timeFormat))) {
            timeFormat = ((SimpleDateFormat) DateFormat.getTimeInstance(2, l)).toPattern();
        }

        SimpleDateFormat df = new SimpleDateFormat(dateFormat + " " + timeFormat, l);
        return df.toPattern();
    }

    public static boolean supports24Hours(Locale l) {
        boolean is24hr = false;
        String pattern = getTimePattern(l);
        if ((pattern != null) && (pattern.trim().length() > 0)) {
            if (pattern.indexOf('H') > -1)
                is24hr = true;
        }
        return is24hr;
    }

    public static int dateTimePartToPosition(int tokennum, String pattern) {
        StringTokenizer st = new StringTokenizer(pattern, " /-.:");
        int i = 0;

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if ((s.indexOf(100) != -1) && (tokennum == 0))
                return i;
            if ((s.indexOf(77) != -1) && (tokennum == 1))
                return i;
            if ((s.indexOf(121) != -1) && (tokennum == 2))
                return i;
            if ((s.indexOf(72) != -1) && (tokennum == 3))
                return i;
            if ((s.indexOf(104) != -1) && (tokennum == 3))
                return i;
            if ((s.indexOf(109) != -1) && (tokennum == 4))
                return i;
            if ((s.indexOf(115) != -1) && (tokennum == 5))
                return i;
            i++;
        }

        return -1;
    }

    public static String stringToCodepoints(String theString, boolean escapeSpace, boolean escapeUnicode) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
            case ' ':
                if ((x == 0) || (escapeSpace))
                    outBuffer.append('\\');
                outBuffer.append(' ');
                break;
            case '\\':
                outBuffer.append('\\');
                outBuffer.append('\\');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            default:
                if ("=: \t\r\n\f#!".indexOf(aChar) != -1) {
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                } else {
                    if (escapeUnicode)
                        outBuffer.append("\\\\");
                    else
                        outBuffer.append("\\");
                    outBuffer.append('u');
                    outBuffer.append(toHex(aChar >> '\f' & 0xF));
                    outBuffer.append(toHex(aChar >> '\b' & 0xF));
                    outBuffer.append(toHex(aChar >> '\004' & 0xF));
                    outBuffer.append(toHex(aChar & 0xF));
                }
                break;
            }
        }
        return outBuffer.toString();
    }

    public static synchronized void setSettingProp(String propName, String propValue) {
        settingProps.put(propName, propValue);
    }

    private static synchronized String getSettingProp(String propName, String lang) {
        if ((lang != null) && (!"".equals(lang))) {
            String langKey = propName + "_" + lang.toUpperCase();
            if (settingProps.containsKey(langKey)) {
                return settingProps.get(langKey);
            }
        }
        return settingProps.get(propName);
    }

    public static char toHex(int nibble) {
        return HEXDIGITS[(nibble & 0xF)];
    }

    public static boolean isValidChar(String checkString) {
        char[] checkArray = checkString.toCharArray();

        boolean valid = false;

        for (int xx = 0; xx < checkArray.length; xx++) {
            valid = false;

            if (((checkArray[xx] >= 'A') && (checkArray[xx] <= 'Z')) || ((checkArray[xx] >= '0') && (checkArray[xx] <= '9')) || (checkArray[xx] == '_') || (checkArray[xx] == '$')) {
                if ((xx != 0) || ((checkArray[xx] >= 'A') && (checkArray[xx] <= 'Z'))) {
                    valid = true;
                }
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    private static String fixRussianInputString(String value, Locale l) {
        if ("ru".equals(l.getLanguage())) {
            value = value.trim();
            StringTokenizer tok = new StringTokenizer(value, " ");
            StringBuilder buf = new StringBuilder();
            while (tok.hasMoreTokens()) {
                buf.append(tok.nextToken());
            }
            value = buf.toString();
        }
        return value;
    }

    static String getMaxTypeAsString(int i) {
        if ((i > 0) && (i < TYPEASSTRING.length)) {
            return TYPEASSTRING[i];
        }
        return null;
    }
}
