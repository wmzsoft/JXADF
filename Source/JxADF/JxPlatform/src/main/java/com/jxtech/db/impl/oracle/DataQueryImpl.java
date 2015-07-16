package com.jxtech.db.impl.oracle;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.db.DBFactory;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 数据查询
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class DataQueryImpl extends com.jxtech.db.impl.DataQueryImpl {
    private static final Logger LOG = LoggerFactory.getLogger(DataQueryImpl.class);

    /**
     * 查询数据
     * 
     * @param conn 数据库连接
     * @param tablename 表名
     * @param queryinfo 查询对象
     * @return
     */
    public List<Map<String, Object>> query(Connection conn, String tablename, DataQueryInfo queryinfo) throws JxException {
        List<Map<String, Object>> vals = super.query(conn, tablename, queryinfo);
        if (vals != null) {
            return vals;
        }
        if (conn == null || StrUtil.isNull(tablename)) {
            LOG.warn("没有传入正确的参数Connection或tablename=" + tablename);
            return null;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select * from (");
        msql.append(tablename);
        msql.append(")");
        String cause = queryinfo.getWhereAllCause();
        if (!StrUtil.isNull(cause)) {
            msql.append(" where ");
            msql.append(cause);
        }
        if (!StrUtil.isNull(queryinfo.getOrderby())) {
            msql.append("  order by  ");
            msql.append(queryinfo.getOrderby());
        }
        Object[] params = queryinfo.getWhereAllParams();
        int pageNum = queryinfo.getPageNum();
        int pageSize = queryinfo.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            int end = pageSize * pageNum;
            int start = pageSize * (pageNum - 1);
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (");
            if (start > 0) {
                start += 1;
            }
            sql.append(msql).append(") A WHERE ROWNUM <= ").append(end).append(" ) WHERE RN >= ").append(start);
            msql = sql;
        }
        if (isIgnoreLog(tablename)) {
            LOG.debug("\r\n" + msql + "\r\n" + StrUtil.objectToString(params));
        }
        QueryRunner qr = new QueryRunner();
        try {
            List<Map<String, Object>> list = qr.query(conn, msql.toString(), new MapListHandler(), params);
            JxLog jxlog = JxLogFactory.getJxLog(queryinfo.getAppname(), tablename);
            if (jxlog != null) {
                jxlog.debug(msql + "\r\n" + StrUtil.objectToString(params), "QUERY");
            }
            vals = toDataMapList(list);
            String ckey = StrUtil.contact(DBFactory.CACHE_PREX, tablename, ".", String.valueOf(queryinfo.getQueryId(true)));
            CacheUtil.putJboCache(ckey, vals);
            return vals;
        } catch (SQLException e) {
            LOG.error(e.getMessage() + "\r\n" + tablename + "\r\n" + msql + "\r\n" + StrUtil.objectToString(params));
        }
        return null;
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
        if (columnValue instanceof String) {
            if (((String) columnValue).toLowerCase().indexOf("nextval") > 0) {
                return false;
            }
        }
        String where = columnName + "=?";
        int c = count(conn, tableName, where, new Object[] { columnValue });
        return (c > 0);
    }

    /**
     * 获得序列值
     * 
     * @param sequenceName
     * @param isNext
     * @return
     */
    public long getSequence(Connection conn, String sequenceName, boolean isNext) throws JxException {
        if (StrUtil.isNull(sequenceName)) {
            return 0;
        }
        String val = ".Nextval";
        if (!isNext) {
            val = ".currval";
        }
        String msql = "select " + sequenceName + val + " as val from dual";
        QueryRunner qr = new QueryRunner();
        try {
            Map<String, Object> rs = qr.query(conn, msql, new MapHandler());
            if (rs != null) {
                return Long.parseLong(String.valueOf(rs.get("val")));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n" + msql);
            // LOG.error(e.getMessage());
        }
        return 0;
    }

    public void getBlob(Connection conn, String tableName, String blobColumnName, String uidName, String uidValue, OutputStream os) throws JxException {
        if (StrUtil.isNull(tableName) || StrUtil.isNull(blobColumnName) || os == null) {
            throw new JxException("输入的参数为空，不能查询。");
        }
        if (StrUtil.isNull(uidName) || StrUtil.isNull(uidValue)) {
            throw new JxException("uidName or uidValue is null.");
        }
        if (conn == null) {
            throw new JxException("获取数据库连接失败。");
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select ").append(blobColumnName);
        msql.append(" from ").append(tableName);
        msql.append(" Where ").append(uidName).append("=?");
        PreparedStatement pst = null;
        BufferedInputStream in = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(msql.toString());
            pst.setString(1, uidValue);
            rs = pst.executeQuery();
            if (rs.next()) {
                oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);
                in = new BufferedInputStream(blob.getBinaryStream());
                byte[] buf = new byte[1024];
                int hasRead = 0;
                while ((hasRead = in.read(buf)) > 0) {
                    os.write(buf, 0, hasRead);
                }
                in.close();
                conn.commit();
                conn.setAutoCommit(true);
            } else {
                conn.commit();
                conn.setAutoCommit(true);
                throw new JxException("没有找到对应的记录：ID=" + uidValue);
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚失败。" + e1.getMessage());
            }
            LOG.error(e.getMessage());
        } finally {
            JxDataSourceUtil.closeStatement(pst);
            JxDataSourceUtil.closeResultSet(rs);
        }
    }

}
