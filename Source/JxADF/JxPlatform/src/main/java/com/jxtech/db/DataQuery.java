package com.jxtech.db;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import org.apache.commons.dbutils.ResultSetHandler;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 数据查询接口
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public interface DataQuery {

    /**
     * 查询数据
     * 
     * @param conn
     *            数据库连接
     * @param tablename
     *            表名
     * @param queryinfo
     *            查询对象
     * @return
     */
    public List<Map<String, Object>> query(Connection conn, String tablename, DataQueryInfo queryinfo) throws JxException;

    public List<Map<String, Object>> query(String tablename, DataQueryInfo queryinfo) throws JxException;

    public List<Map<String, Object>> queryAllPage(String tablename, DataQueryInfo queryInfo) throws JxException;

    /**
     * 执行数据库的sql函数
     * 
     * @param conn
     *            数据库连接
     * @param fun
     *            函数名，如：sum、count等
     * @param tablename
     *            表名
     * @param columnName
     *            字段名
     * @param whereCause
     *            查询条件
     * @param params
     *            参数值
     * @return
     * @throws JxException
     */
    public Object sqlFun(Connection conn, String fun, String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    /**
     * 统计记录数
     * 
     * @param conn
     *            数据库连接
     * @param tablename
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public int count(Connection conn, String tablename, String whereCause, Object[] params) throws JxException;

    public int count(String tablename, String whereCause, Object[] params) throws JxException;

    public int count(String tablename, DataQueryInfo qbe) throws JxException;

    public int count(JboSetIFace jboset) throws JxException;

    /**
     * 返回最大值
     * 
     * @param conn
     * @param tablename
     * @param columnName
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public Object max(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    public Object max(String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    /**
     * 返回最小值
     * 
     * @param conn
     * @param tablename
     * @param columnName
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public Object min(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    public Object min(String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    /**
     * 执行SQL语句中的SUM函数
     * 
     * @param conn
     * @param tablename
     * @param columnName
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public double sum(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    public double sum(String tablename, String columnName, String whereCause, Object[] params) throws JxException;

    /**
     * 通过SQL脚本、参数得到结果集
     * 
     * @param conn
     *            数据库连接
     * @param msql
     *            SQL脚本
     * @param params
     *            参数值
     * @return
     */
    public List<Map<String, Object>> getResultSet(Connection conn, String msql, Object[] params) throws JxException;

    /**
     * 通过SQL脚本、参数得到结果集
     * 
     * @param msql
     * @param params
     * @return
     * @throws JxException
     */
    public List<Map<String, Object>> getResultSet(String msql, Object[] params) throws JxException;

    public <T> T getResult(Connection conn, ResultSetHandler<T> rsh, String msql, Object[] params) throws JxException;

    public <T> T getResult(ResultSetHandler<T> rsh, String msql, Object[] params) throws JxException;

    /**
     * 获得序列值
     * 
     * @param sequenceName
     * @param isNext
     * @return
     */
    public long getSequence(Connection conn, String sequenceName, boolean isNext) throws JxException;

    public long getSequence(String sequenceName, boolean isNext) throws JxException;

    /**
     * 将SQL语句直接转换为JSON格式。
     * 
     * @param conn
     *            数据库连接
     * @param msql
     * @param params
     * @param columns
     * @return
     * @throws JxException
     */
    public String toJson(Connection conn, String msql, Object[] params, String[] columns, String jboname) throws JxException;

    public String toJson(String msql, Object[] params, String[] columns, String jboname) throws JxException;

    /**
     * 将SQL语句直接转换为JSON格式。
     * 
     * @param conn
     *            数据库连接
     * @param msql
     * @param params
     * @param columns
     * @param head
     * @param jboname
     * @param count
     * @param pageSize
     * @param pageNum
     * @return
     * @throws JxException
     */
    public String toJson(Connection conn, String msql, Object[] params, String[] columns, boolean head, String jboname, int count, int pageSize, int pageNum) throws JxException;

    /**
     * 检查是否存在某条记录信息
     * 
     * @param conn
     * @param tableName
     * @param columnName
     * @param columnValue
     * @return
     * @throws JxException
     */
    public boolean exist(Connection conn, String tableName, String columnName, Object columnValue) throws JxException;

    public boolean exist(String tableName, String columnName, Object columnValue) throws JxException;

    public void getBlob(String tableName, String blobColumnName, String uidName, String uidValue, OutputStream os) throws JxException;

    public void getBlob(Connection conn, String tableName, String blobColumnName, String uidName, String uidValue, OutputStream os) throws JxException;

    public String getDataSourceName();

    public void setDataSourceName(String dataSourceName);

    /**
     * 将日期格式转换为字符串，用作查询条件
     * 
     * @param date
     * @return
     */
    public String date2String(Object date);

    /**
     * 将Datetime转换为字符串，用作查询条件
     * 
     * @param datetime
     * @return
     */
    public String datetime2String(Object datetime);
    /**
     * 获取数据库日期字段的年份，用作查询条件
     * @param str
     * @return
     */
    public String date2Year(String str);
    /**
     * 获取数据库日期字段的年月，用作查询条件格式为yyyy-mm
     * @param str
     * @return
     */
    public String date2YearMonth(String str);
    /**
     * 获取数据库日期字段的月份，用作查询条件格式为mm
     * @param str
     * @return
     */
    public String date2Month(String str);
    /**
     * 将数据库字段按照指定的要求进行截取，用作查询条件
     * @param str
     * @param start
     * @param length
     * @return
     */
    public String column2substr(String str,int start,int length);
}
