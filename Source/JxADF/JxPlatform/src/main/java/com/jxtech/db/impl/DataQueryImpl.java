package com.jxtech.db.impl;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.DataQuery;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.DataMap;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;

/**
 * @author wmzsoft@gmail.com
 * @date 2015.03
 */
public abstract class DataQueryImpl implements DataQuery {

    private static final Logger LOG = LoggerFactory.getLogger(DataQuery.class);

    private String dataSourceName;// 数据源名称

    public List<Map<String, Object>> query(Connection conn, String tablename, DataQueryInfo queryinfo) throws JxException {
        return null;
    }

    public List<Map<String, Object>> query(String tablename, DataQueryInfo qbe) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            return query(conn, tablename, qbe);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param conn
     * @param tablename
     * @param queryInfo
     * @return
     */
    public List<Map<String, Object>> queryAllPage(Connection conn, String tablename, DataQueryInfo queryInfo) throws JxException {
        int pageNum = queryInfo.getPageNum();
        int pageSize = queryInfo.getPageSize();
        queryInfo.setPageNum(0);
        queryInfo.setPageSize(0);
        List<Map<String, Object>> list = query(conn, tablename, queryInfo);
        queryInfo.setPageNum(pageNum);
        queryInfo.setPageSize(pageSize);
        return list;
    }

    public List<Map<String, Object>> queryAllPage(String tablename, DataQueryInfo queryInfo) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        try {
            return this.queryAllPage(conn, tablename, queryInfo);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * 执行数据库的sql函数
     *
     * @param conn
     * @param tablename
     * @param columnName
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public Object sqlFun(Connection conn, String fun, String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        if (conn == null || StrUtil.isNull(tablename) || StrUtil.isNull(columnName)) {
            LOG.warn("没有传入正确的参数Connection或tablename=" + tablename);
            return -1;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select ").append(fun).append("(").append(columnName);
        msql.append(") as m from ");
        boolean ist = false;
        if (tablename.trim().indexOf(' ') > 0) {
            msql.append("(");
            ist = true;
        }
        msql.append(tablename);
        if (ist) {
            msql.append(") n");
        }
        if (!StrUtil.isNull(whereCause)) {
            msql.append(" where ").append(whereCause);
        }
        QueryRunner qr = new QueryRunner(true);
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            pstat = conn.prepareStatement(msql.toString());
            qr.fillStatement(pstat, params);
            rs = pstat.executeQuery();
            if (rs.next()) {
                return rs.getObject("m");
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage() + "\r\n" + msql.toString() + StrUtil.objectToString(params));
            throw new JxException(e.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstat);
        }
        return null;
    }

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
    public List<Map<String, Object>> getResultSet(Connection conn, String msql, Object[] params) throws JxException {
        if (StrUtil.isNull(msql)) {
            return null;
        }
        QueryRunner qr = new QueryRunner(true);
        try {
            return qr.query(conn, msql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOG.error(e.getMessage() + "\r\n" + msql);
        }
        return null;
    }

    /**
     * 通过SQL脚本、参数得到结果集
     *
     * @param msql
     * @param params
     * @return
     * @throws JxException
     */
    public List<Map<String, Object>> getResultSet(String msql, Object[] params) throws JxException {
        if (StrUtil.isNull(msql)) {
            return null;
        }
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        try {
            return this.getResultSet(conn, msql, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

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
    public int count(Connection conn, String tablename, String whereCause, Object[] params) throws JxException {
        Object value = sqlFun(conn, "count", tablename, "*", whereCause, params);
        if (value != null) {
            try {
                return Integer.parseInt(String.valueOf(value));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public int count(String tablename, String whereCause, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            return count(conn, tablename, whereCause, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public int count(JboSetIFace jboset) throws JxException {
        if (jboset == null) {
            return 0;
        }
        String tablename = jboset.getEntityname();
        if (StrUtil.isNull(tablename) || jboset.getJboname().startsWith("SQL_")) {
            tablename = jboset.getSql();
        }
        return count(tablename, jboset.getQueryInfo());
    }

    /**
     * 计算count
     *
     * @param tablename
     * @param qbe
     * @return
     */
    public int count(String tablename, DataQueryInfo qbe) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        try {
            String groupby = qbe.getGroupby();
            if (StrUtil.isNull(groupby)) {
                return count(conn, tablename, qbe.getWhereAllCause(), qbe.getWhereAllParams());
            } else {
                StringBuilder msql = new StringBuilder();
                msql.append("Select ");
                String select = qbe.getSelectColumn();
                if (StrUtil.isNull(select)) {
                    msql.append(" * ");
                } else {
                    msql.append(select);
                }
                msql.append(" from ").append(tablename);
                String cause = qbe.getWhereAllCause();
                if (!StrUtil.isNull(cause)) {
                    msql.append(" where ").append(cause);
                }
                msql.append(" group by ").append(groupby);
                return count(conn, msql.toString(), null, qbe.getWhereAllParams());
            }
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

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
    public boolean exist(Connection conn, String tableName, String columnName, Object columnValue) throws JxException {
        if (columnValue == null) {
            return false;
        }
        String where = columnName + "=?";
        int c = count(conn, tableName, where, new Object[] { columnValue });
        return (c > 0);
    }

    public boolean exist(String tableName, String columnName, Object columnValue) throws JxException {
        if (columnValue == null) {
            return false;
        }
        String where = columnName + "=?";
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        int c = 0;
        try {
            c = count(conn, tableName, where, new Object[] { columnValue });
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return (c > 0);
    }

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
    public Object max(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        return sqlFun(conn, "max", tablename, columnName, whereCause, params);
    }

    public Object max(String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(getDataSourceName());
        try {
            return max(conn, tablename, columnName, whereCause, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

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
    public Object min(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        return sqlFun(conn, "min", tablename, columnName, whereCause, params);
    }

    public Object min(String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        try {
            return min(conn, tablename, columnName, whereCause, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

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
    public double sum(Connection conn, String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        Object value = sqlFun(conn, "sum", tablename, columnName, whereCause, params);
        if (value == null) {
            return 0.0D;
        } else {
            try {
                return Double.parseDouble(String.valueOf(value));
            } catch (Exception e) {
                return 0.0D;
            }
        }
    }

    public double sum(String tablename, String columnName, String whereCause, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(dataSourceName);
        try {
            return sum(conn, tablename, columnName, whereCause, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public <T> T getResult(Connection conn, ResultSetHandler<T> rsh, String msql, Object[] params) throws JxException {
        QueryRunner qr = new QueryRunner(true);
        try {
            return qr.query(conn, msql, rsh, params);
        } catch (SQLException e) {
            LOG.error(e.getMessage() + "\r\n" + msql + "," + StrUtil.objectToString(params));
        }
        return null;
    }

    public <T> T getResult(ResultSetHandler<T> rsh, String msql, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        if (conn == null) {
            throw new JxException("Connection is null.");
        }
        try {
            return this.getResult(conn, rsh, msql, params);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    protected List<Map<String, Object>> toDataMapList(List<Map<String, Object>> list) {
        if (list == null) {
            return null;
        }
        List<Map<String, Object>> nlist = new ArrayList<Map<String, Object>>();
        int lsize = list.size();
        for (int i = 0; i < lsize; i++) {
            Map<String, Object> map = list.get(i);
            Map<String, Object> nmap = new DataMap<String, Object>();
            nmap.putAll(map);
            nlist.add(nmap);
        }
        return nlist;
    }

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
    public String toJson(Connection conn, String msql, Object[] params, String[] columns, String jboname) throws JxException {
        return toJson(conn, msql, params, columns, false, jboname, 0, 0, 0);
    }

    public String toJson(String msql, Object[] params, String[] columns, String jboname) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(null);
        try {
            return toJson(conn, msql, params, columns, jboname);
        } finally {
            DbUtils.closeQuietly(conn);
        }

    }

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
    public String toJson(Connection conn, String msql, Object[] params, String[] columns, boolean head, String jboname, int count, int pageSize, int pageNum) throws JxException {
        List<Map<String, Object>> list = getResultSet(conn, msql, params);
        return JsonUtil.toJson(list, columns, head, jboname, count, pageSize, pageNum);
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    protected boolean isIgnoreLog(String tableName) {
        if (tableName == null) {
            return true;
        }
        return ",PUB_USER,PUB_DEPARTMENT,".contains(tableName.toUpperCase());
    }

    public long getSequence(String sequenceName, boolean isNext) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            return getSequence(conn, sequenceName, isNext);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public void getBlob(String tableName, String blobColumnName, String uidName, String uidValue, OutputStream os) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            getBlob(conn, tableName, blobColumnName, uidName, uidValue, os);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    @Override
    public String date2String(Object date) {
        return null;
    }

    @Override
    public String datetime2String(Object datetime) {
        return null;
    }

    @Override
    public String date2Year(String str) {
        return null;
    }

    public String date2YearMonth(String str) {
        return null;
    };

    @Override
    public String date2Month(String str) {
        return null;
    }

    @Override
    public String column2substr(String str, int start, int length) {
        return null;
    }

}
