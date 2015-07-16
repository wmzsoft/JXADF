package com.jxtech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class NumUtil {
    private static final Logger LOG = LoggerFactory.getLogger(NumUtil.class);

    /**
     * 返回 a / b 向上取整
     * 
     * @param a
     * @param b
     * @param defaultb 如果b=0,则取defaultb作为默认值
     * @return
     */
    public static int upRounded(int a, int b, int defaultb) {
        if (b == 0 && defaultb == 0) {
            return 0;
        }
        int c = b;
        if (b == 0) {
            c = defaultb;
        }
        int x = a / c;
        if (a % c != 0) {
            x = x + 1;
        }
        return x;
    }

    /**
     * 获得页数
     * 
     * @param a 记录数
     * @param b 页面大小
     * @return
     */
    public static int getPageSize(int a, String b) {
        int c = 0;
        try {
            c = Integer.parseInt(b);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return upRounded(a, c, 20);
    }

    /**
     * value1与value2进行比较，比较操作符为operation
     * 
     * @param value1
     * @param value2
     * @param operation
     * @return
     */
    public static boolean compare(double value1, double value2, String operation) {
        if ("=".equals(operation) || "==".equals(operation)) {
            return (value1 == value2);
        } else if (">".equals(operation)) {
            return (value1 > value2);
        } else if (">=".equals(operation)) {
            return (value1 >= value2);
        } else if ("<".equals(operation)) {
            return (value1 < value2);
        } else if ("<=".equals(operation)) {
            return value1 <= value2;
        } else if ("!=".equals(operation) || "<>".equals(operation)) {
            return value1 != value2;
        } else {
            DecimalFormat df = new DecimalFormat("#");
            return StrUtil.compareStr(df.format(value1), df.format(value2), operation);
        }
    }

    public static long parseLong(String value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long parseLong(String value) {
        return parseLong(value, 0);
    }

    /**
     * 取几位小数
     * 
     * @param x 处理值
     * @param pos 位数
     * @return
     */
    public static double round(double x, int pos) {
        BigDecimal bg = new BigDecimal(x);
        return bg.setScale(pos, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将数字格式化
     * 
     * @param value
     * @param formatter
     * @return
     */
    public static String format(double value, String formatter) {
        NumberFormat nf;
        if (StrUtil.isNull(formatter)) {
            String tv = String.valueOf(value);
            if (tv.endsWith(".0")) {
                return tv.substring(0, tv.length() - 2);
            } else if (tv.indexOf('E')>0) {
                nf = new DecimalFormat("#");
                return nf.format(value);
            }else{
                return tv;
            }
        } else if ("$".equalsIgnoreCase(formatter)) {
            nf = new DecimalFormat("$,##0.00");
            return nf.format(value);
        } else if ("￥".equals(formatter) || "RMB".equalsIgnoreCase(formatter)) {
            nf = new DecimalFormat("￥,##0.00");
            return nf.format(value);
        } else if ("%".equals(formatter) || "%".equalsIgnoreCase(formatter) || "percent".equalsIgnoreCase(formatter)) {
            nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(1);
            return nf.format(value);
        } else if ("currency".equalsIgnoreCase(formatter)) {
            nf = NumberFormat.getCurrencyInstance();
            return nf.format(value);
        } else {
            nf = new DecimalFormat(formatter);
            return nf.format(value);
        }
    }
}
