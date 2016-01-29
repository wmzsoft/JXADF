package com.jxtech.util;

import com.jxtech.jbo.base.KeyValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wmzsoft@gmail.com
 */
public class StrUtil {
    private static final Logger LOG = LoggerFactory.getLogger(StrUtil.class);

    public static boolean isNull(String str) {
        if (str == null)
            return true;
        if ("null".equalsIgnoreCase(str)) {
            return true;
        }
        if ("".equals(str))
            return true;
        return false;
    }

    public static boolean isNullOfIgnoreCaseBlank(String str) {
        if (str == null)
            return true;
        if ("null".equalsIgnoreCase(str.trim())) {
            return true;
        }
        if ("".equalsIgnoreCase(str.trim()))
            return true;
        return false;
    }

    public static boolean isObjectNull(Object value) {
        if (value == null)
            return true;
        if (value instanceof String) {
            return isNull((String) value);
        }
        return false;
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return true;
            } else {
                // return s2.equals(s1);
                return false;
            }
        } else {
            return s1.equals(s2);
        }
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return true;
            } else {
                // return s2.equalsIgnoreCase(s1);
                return false;
            }
        } else {
            if (s2 == null) {
                return false;
            } else {
                return s1.equalsIgnoreCase(s2);
            }

        }
    }

    public static int compareTo(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return 0;
            } else {
                // return s2.compareTo(s1);
                return -1;
            }
        } else {
            if (s2 == null)
                return 1;
            return s1.compareTo(s2);
        }
    }

    public static int compareToIgnoreCase(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return 0;
            } else {
                // return s2.compareToIgnoreCase(s1);
                return -1;
            }
        } else {
            if (s2 == null) {
                return 1;
            } else {
                return s1.compareToIgnoreCase(s2);
            }

        }
    }

    public static boolean startsWith(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return true;
            } else {
                // return s2.startsWith(s1);
                return false;
            }
        } else {
            if (s2 == null) {
                return false;
            } else {
                return s1.startsWith(s2);
            }

        }
    }

    public static boolean endsWith(String s1, String s2) {
        if (s1 == null) {
            if (s2 == null) {
                return true;
            } else {
                // return s2.endsWith(s1);
                return false;
            }
        } else {
            if (s2 == null) {
                return false;
            } else {
                return s1.endsWith(s2);
            }

        }
    }

    public static boolean contains(String s1, String s2) {
        if (s1 == null) {
            return (s2 == null);
        } else {
            return s1.contains(s2);
        }
    }

    public static int parseInt(String i, int defaultvalue) {
        if (isNull(i)) {
            return defaultvalue;
        }
        try {
            return Integer.parseInt(getSplitFirst(i));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return defaultvalue;
    }

    public static long parseLong(String i, long defaultvalue) {
        if (isNull(i)) {
            return defaultvalue;
        }
        try {
            return Long.parseLong(getSplitFirst(i));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return defaultvalue;
    }

    public static double parseDouble(String i, long defaultvalue) {
        if (isNull(i)) {
            return defaultvalue;
        }
        try {
            return Double.parseDouble(i);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return defaultvalue;
    }

    public static String getSplitFirst(String source) {
        return getSplitFirst(source, ",");
    }

    public static String getSplitFirst(String source, String flag) {
        if (source == null) {
            return null;
        }
        if (flag == null) {
            return source;
        }
        String[] s = source.split(flag);
        return s[0];
    }

    public static String parseIntToString(String i, int defaultvalue) {
        return String.valueOf(parseInt(i, defaultvalue));
    }

    public static String removeLastChar(String source) {
        if (source == null) {
            return null;
        }
        String t = source.trim();
        int len = t.length();
        if (len > 1) {
            return t.substring(0, len - 1);
        }
        return t;
    }

    public static StringBuilder deleteLastChar(StringBuilder sb) {
        if (sb == null) {
            return null;
        }
        int len = sb.length();
        int idx = len - 1;
        while (idx > 0 && sb.charAt(idx) == ' ') {
            idx--;
        }
        if (idx > 0) {
            sb.delete(idx, len);
        }
        return sb;
    }

    public static String objectToString(Object[] obj) {
        if (obj == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null) {
                sb.append("null,");
                continue;
            }
            try {
                sb.append((String) obj[i] + ",");
            } catch (Exception e) {
                sb.append(obj[i].toString() + ",");
            }
        }
        return sb.toString();
    }

    /**
     * 把json格式的字符串拆分为一个map数组.
     * <p>
     * 注意：<br/>
     * json中的数据都不需要带''或""
     * </p>
     * 
     * @param json 输入为json格式的字符串,最好不要带有"{}"
     * @return
     */
    public static List<KeyValue> toList(String json) {
        if (isNull(json) || "{}".equals(json)) {
            return null;
        }
        // 以防传入的json格式带有json的大括号
        if ("{".equals(json.substring(0, 1))) {
            json = json.substring(1);
        }
        if ("}".equals(json.substring(json.length() - 1))) {
            json = json.substring(0, json.length() - 1);
        }
        String[] kvs = json.split(",");
        List<KeyValue> list = new ArrayList<KeyValue>();
        for (String kv : kvs) {
            if (isNull(kv)) {
                continue;
            }
            String[] kvArray = kv.split(":");
            if (kvArray.length != 2) {
                return null;
                // throw new IllegalArgumentException("进行Keyvalue转换的json输入格式有误！json input="+kv);
            }
            // key为空忽略
            if (isNull(kvArray[0])) {
                continue;
            }
            KeyValue kvObj = new KeyValue(kvArray[0], kvArray[1]);
            list.add(kvObj);
        }
        return list;
    }

    /**
     * value1与value2进行比较，比较操作符为operation
     * 
     * @param value1
     * @param value2
     * @param operation
     * @return
     */
    public static boolean compare(Object value1, Object value2, String operation) {
        if (value1 == null || value2 == null) {
            if ("=".equals(operation) || "==".equals(operation)) {
                // value1 如果是由el表达式解析，结果为"null"
                return (("null".equals(value1) || value1 == null) && value2 == null);
            } else if ("!=".equals(operation) || "<>".equals(operation)) {
                return !(value1 == null && value2 == null);
            } else if (">".equals(operation)) {
                return (value1 != null && value2 == null);
            } else if (">=".equals(operation)) {
                return ((value1 != null && value2 == null) || (value1 == null && value2 == null));
            } else if ("<".equals(operation)) {
                return (value1 == null && value2 != null);
            } else if ("<=".equals(operation)) {
                return ((value1 == null && value2 != null) || (value1 == null && value2 == null));
            } else {
                return false;
            }
        }
        if ((value1 instanceof BigDecimal) && (value2 instanceof BigDecimal)) {
            BigDecimal bd1 = (BigDecimal) value1;
            BigDecimal bd2 = (BigDecimal) value2;
            return NumUtil.compareBigDecimal(bd1, bd2, operation);
        }
        if ((value1 instanceof String) || (value2 instanceof String)) {
            return compareStr(value1.toString(), value2.toString(), operation);
        }

        try {
            String s1 = value1.toString();
            BigDecimal bd1 = new BigDecimal(s1);
            String s2 = value2.toString();
            BigDecimal bd2 = new BigDecimal(s2);
            return NumUtil.compareBigDecimal(bd1, bd2, operation);
        } catch (Exception e) {
            // LOG.debug(e.getMessage());
        }
        return compareStr(value1.toString(), value2.toString(), operation);
    }


    public static boolean compareStr(String value1, String value2, String operation) {
        if ("==".equals(operation) || "=".equals(operation)) {
            return equals(value1, value2);
        } else if ("!=".equals(operation) || "<>".equals(operation)) {
            return !equals(value1, value2);
        } else if ("startWith".equalsIgnoreCase(operation) || "startsWith".equalsIgnoreCase(operation)) {
            return startsWith(value1, value2);
        } else if ("endWith".equalsIgnoreCase(operation) || "endsWith".equalsIgnoreCase(operation)) {
            return endsWith(value1, value2);
        } else if ("contains".equalsIgnoreCase(operation)) {
            return contains(value1, value2);
        } else {
            int x = value1.compareTo(value2);// 相等=0,大于>0，小于<0
            if (">".equals(operation)) {
                return (x > 0);
            } else if (">=".equals(operation)) {
                return (x >= 0);
            } else if ("<=".equals(operation)) {
                return (x <= 0);
            } else if ("<".equals(operation)) {
                return (x < 0);
            }
        }
        return false;
    }

    public static int indexOfignoreCase(String sourceStr, String express) {
        if (isNull(sourceStr) || isNull(express)) {
            return -1;
        }
        String ss = sourceStr.toUpperCase();
        String es = express.toUpperCase();
        return ss.indexOf(es);
    }

    public static String toString(Object object, String defaultvalue) {
        if (object == null) {
            return defaultvalue;
        }
        if (object instanceof String) {
            return (String) object;
        }
        return object.toString();
    }

    public static String toString(Object[] obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.length; i++) {
            sb.append(obj[i]);
            if (i < obj.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static String[] toArray(String str, String split) {
        if (isNull(str)) {
            return null;
        }
        if (isNull(split)) {
            split = ",";
        }
        return str.split(split);
    }

    public static String convertFirstUpper(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String convertFirstUpperLower(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static boolean isNumber(String str) {
        if (isNull(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] splitWord(String str) {
        if (isNull(str)) {
            return null;
        }
        return str.split("(?!')\\+(?!')|(?!')\\-(?!')|(?!')\\*(?!')|(?!')/(?!')|\\|\\|");
    }

    /**
     * 表达式是否符合某格式
     * 
     * @param express
     * @param formatter
     * @return
     */
    public static boolean isFormat(String express, String formatter) {
        Pattern pat = Pattern.compile(formatter);
        Matcher mat = pat.matcher(express);
        return mat.matches();
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 将UTF8编码转成正常点的
     * 
     * @param utfString
     * @return
     */
    public static String convertUtf8ToString(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }

    /**
     * 这个也是用来解决utf8编码问题的啊
     * 
     * @param utfstring
     * @return
     */
    public static String convertUtf8CharToString(String utfstring) {
        StringBuilder sb = new StringBuilder();
        if (!StrUtil.isNull(utfstring)) {
            char[] chars = utfstring.toCharArray();
            char c = chars[0];
            if (c == 65279) {
                for (int i = 1; i < chars.length; i++) {
                    sb.append(chars[i]);
                }
            } else {
                sb.append(utfstring);
            }
        }

        return sb.toString();
    }

    public static String md5(String plainText) {
        if (isNull(plainText)){
            return null;
        }
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String toJson(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串转换为Boolean类型
     * 
     * @param str
     * @param defaultValue,字符串为空时，返回的值
     * @return
     */
    public static boolean isTrue(String str, boolean defaultValue) {
        if (StrUtil.isNull(str)) {
            return defaultValue;
        } else if ("yes".equalsIgnoreCase(str) || "1".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str) || "y".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 因为页面合并列表的特殊性,可能出现三个特殊字符，导出的时候需要需要做特殊处理<br/>
     * 
     * 移除特殊字符 用于Excel的列头显示 目前支持移除所有的 " : "," ： ","*" 三个特殊字符 还有html标签
     * 
     * @param str 需要处理的字符
     * @return 处理之后的字符
     */
    public static String removeSpecialChar(String str) {
        if (isNull(str)) {
            return "";
        }
        return delHTMLTag(StringUtils.remove(StringUtils.remove(StringUtils.remove(str, ":"), "："), "*"));
    }

    /**
     * 移除html标签
     * 
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    public static int indexOf(String str, String regex) {
        if (str == null || regex == null) {
            return -1;
        }
        Matcher matcher = Pattern.compile(regex).matcher(str);
        if (matcher.find()) {
            return matcher.start();
        }
        return -1;
    }

    public static int indexOf(String str, String regex, int fromindex) {
        if (str == null || regex == null) {
            return -1;
        }
        if (fromindex > 0 && fromindex < str.length()) {
            String s = str.substring(fromindex);
            int idx = indexOf(s, regex);
            if (idx >= 0) {
                return idx + fromindex;
            }
        }
        return -1;
    }

    /**
     * 字符串Str中，有多少regex字符串
     * 
     * @param str
     * @param regex
     * @return
     */
    public static int howManyStr(String str, String regex) {
        if (str == null || regex == null) {
            return 0;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int c = 0;
        int pos = 0;
        while (matcher.find(pos)) {
            c++;
            pos = matcher.start() + 1;
        }
        return c;
    }

    /**
     * 将所有字符串连接在一起
     * 
     * @param s
     * @return
     */
    public static String contact(String... str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            if (str[i] != null) {
                sb.append(str[i]);
            }
        }
        return sb.toString();
    }
}
