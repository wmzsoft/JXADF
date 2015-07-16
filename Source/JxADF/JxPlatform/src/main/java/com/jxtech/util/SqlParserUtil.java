package com.jxtech.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL解析工具
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 */
public class SqlParserUtil {

    /**
     * 逗号
     */
    public static final String Comma = ",";

    /**
     * 解析选择的列
     * 
     */
    public static String parseCols(String sql) {
        String regex = "(select)(.+)(from)";
        return getMatchedString(regex, sql);
    }

    /**
     * 解析选择的表
     * 
     */
    public static String parseTables(String sql) {
        String regex = "";
        if (isContains(sql, "\\s+where\\s+")) {
            regex = "(from)(.+)(where)";
        } else {
            regex = "(from)(.+)($)";
        }
        return getMatchedString(regex, sql);
    }

    /**
     * 解析查找条件
     * 
     */
    public static String parseConditions(String sql) {
        String regex = "";

        if (isContains(sql, "\\s+where\\s+")) {
            // 包括Where，有条件

            if (isContains(sql, "group\\s+by")) {
                // 条件在where和group by之间
                regex = "(where)(.+)(group\\s+by)";
            } else if (isContains(sql, "order\\s+by")) {
                // 条件在where和order by之间
                regex = "(where)(.+)(order\\s+by)";
            } else {
                // 条件在where到字符串末尾
                regex = "(where)(.+)($)";
            }
        } else {
            // 不包括where则条件无从谈起，返回即可
            return null;
        }

        return getMatchedString(regex, sql);
    }

    /**
     * 解析GroupBy的字段
     * 
     */
    public static String parseGroupCols(String sql) {
        String regex = "";

        if (isContains(sql, "group\\s+by")) {
            // 包括GroupBy，有分组字段

            if (isContains(sql, "order\\s+by")) {
                // group by 后有order by
                regex = "(group\\s+by)(.+)(order\\s+by)";
            } else {
                // group by 后无order by
                regex = "(group\\s+by)(.+)($)";
            }
        } else {
            // 不包括GroupBy则分组字段无从谈起，返回即可
            return null;
        }

        return getMatchedString(regex, sql);
    }

    /**
     * 解析OrderBy的字段
     * 
     */
    public static String parseOrderCols(String sql) {
        String regex = "";

        if (isContains(sql, "order\\s+by")) {
            // 包括GroupBy，有分组字段
            regex = "(order\\s+by)(.+)($)";
        } else {
            // 不包括GroupBy则分组字段无从谈起，返回即可
            return null;
        }

        return getMatchedString(regex, sql);
    }

    /**
     * 从文本text中找到regex首次匹配的字符串，不区分大小写
     * 
     * @param regex ： 正则表达式
     * @param text ：欲查找的字符串
     * @return regex首次匹配的字符串，如未匹配返回空
     */
    public static String getMatchedString(String regex, String text) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    /**
     * 看word是否在lineText中存在，支持正则表达式
     * 
     * @param lineText
     * @param regex
     * @return
     */
    public static boolean isContains(String lineText, String regex) {
        if (lineText == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(lineText);
        return matcher.find();
    }

    /**
     * 获得字段名，即可以使用rs.get(columnName)中的字段名
     * 
     * @param sql
     * @return
     */
    public static String[] getColumns(String sql) {
        if (isSelectSql(sql)) {
            return getColumnsOfSelect(sql);
        } else if (isInsert(sql)) {
            return getColumnsOfInsert(sql);
        } else {
            return null;
        }
    }

    public static String[] getColumnsOfSelect(String sql) {
        String cols = parseCols(sql);
        if (cols == null) {
            return null;
        }
        cols = cols.trim();
        if ("*".equals(cols)) {
            return null;
        }
        List<String> columns = new ArrayList<String>();
        cols = cols + ",";
        int len = cols.length();
        String name = "";// 字段名
        int lp = 0;// 左括号数
        int rp = 0;// 右括号数
        for (int i = 0; i < len; i++) {
            char c = cols.charAt(i);
            name = name + c;
            if (c == '(') {
                lp++;
            } else if (c == ')') {
                rp++;
            } else if (c == ',') {
                if (lp == rp) {
                    lp = 0;
                    rp = 0;
                    name = name.substring(0, name.length() - 1).trim();
                    int pos = name.lastIndexOf(" ");
                    if (pos < name.lastIndexOf(')')) {
                        pos = name.lastIndexOf(')');
                    }
                    if (pos > 0) {
                        String tmp = name.substring(pos + 1, name.length()).trim();
                        if (!StrUtil.isNull(tmp)) {
                            name = tmp;
                        }
                    }
                    columns.add(name);
                    name = "";
                }
            }
        }
        String[] columnName = new String[columns.size()];
        for (int i = 0; i < columnName.length; i++) {
            columnName[i] = columns.get(i);
        }
        return columnName;
    }

    public static String[] getColumnsOfInsert(String sql) {
        if (sql == null) {
            return null;
        }
        int start = StrUtil.indexOf(sql, "(?i)\\s+into\\s+(.|\\n)+\\(");
        if (start < 0) {
            return null;
        }
        start = sql.indexOf('(', start);
        int end = StrUtil.indexOf(sql, "(?i)\\)\\s*values\\s*\\(");
        if (end < 0) {
            end = StrUtil.indexOf(sql, "(?i)\\)(\\s|\\()*select\\s+");
        }
        if (end > start) {
            String cols = sql.substring(start + 1, end);
            return cols.split(",");
        }
        return null;
    }

    /**
     * 获得Insert into 中的Values值
     * 
     * @param sql
     * @return
     */
    public static String[] getValues(String sql) {
        if (!isInsert(sql)) {
            return null;
        }
        int start = StrUtil.indexOf(sql, "(?i)\\)\\s*values\\s*\\(");
        int end = -1;
        String vals = null;
        if (start > 0) {
            start = sql.indexOf('(', start);
            end = sql.lastIndexOf(')');
            vals = sql.substring(start + 1, end);
        } else {
            start = StrUtil.indexOf(sql, "(?i)(\\s|\\()*select\\s+");
            if (start > 0) {
                start = StrUtil.indexOf(sql, "(?i)t", start) + 1;
                end = StrUtil.indexOf(sql, "(?i)\\s+from\\s+", start);
                if (end > start) {
                    vals = sql.substring(start, end);
                }
            }
        }
        if (vals != null) {
            List<String> values = new ArrayList<String>();
            start = 0;
            StringBuilder sb = new StringBuilder(vals.trim());
            sb.append(',');
            end = sb.indexOf(",");
            while (end > start) {
                String fv = sb.substring(start, end).trim();
                while ((StrUtil.howManyStr(fv, "'") % 2) != 0) {
                    end = sb.indexOf(",", end + 1);
                    if (end < 0) {
                        break;
                    }
                    fv = sb.substring(start, end).trim();
                }
                values.add(fv);
                if (end < 0) {
                    break;
                }
                start = end + 1;
                end = sb.indexOf(",", start);
            }
            int len = values.size();
            if (len > 0) {
                String[] v = new String[len];
                for (int i = 0; i < len; i++) {
                    v[i] = values.get(i);
                }
                return v;
            }
        }
        return null;
    }

    /**
     * 判断是否是Select语句
     * 
     * @param str
     * @return
     */
    public static boolean isSelectSql(String str) {
        return isContains(str, "^(?i)\\s*select\\s+(.|\\n)+\\s+from\\s+\\w+");
    }

    public static boolean isInsert(String s) {
        return isContains(s, "^(?i)\\s*INSERT\\s+INTO\\s+(.|\\n)+\\s*(values|select)");
    }

}
