package com.jxtech.db.impl.mysql;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.06
 *
 */
public class DataQueryImpl extends com.jxtech.db.impl.DataQueryImpl {
    private static final Logger LOG = LoggerFactory.getLogger(DataQueryImpl.class);

    @Override
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
        msql.append("Select ");
        String select = queryinfo.getSelectColumn();
        if (StrUtil.isNull(select)) {
            msql.append(" * ");
        } else {
            msql.append(select);
        }
        msql.append(" from ");
        boolean ist = false;
        if (tablename.trim().indexOf(' ') > 0) {
            msql.append("(");
            ist = true;
        }
        msql.append(tablename);
        if (ist) {
            msql.append(") mysql_n");
        }

        String cause = queryinfo.getWhereAllCause();
        if (!StrUtil.isNull(cause)) {
            msql.append(" where ");
            msql.append(cause);
        }
        String groupby = queryinfo.getGroupby();
        if (!StrUtil.isNull(groupby)) {
            msql.append(" group by ").append(groupby);
        }
        String orderby = queryinfo.getOrderby();
        if (!StrUtil.isNull(orderby)) {
            msql.append("  order by  ");
            msql.append(orderby);
        }
        Object[] params = queryinfo.getWhereAllParams();
        int pageNum = queryinfo.getPageNum();
        int pageSize = queryinfo.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            int start = pageSize * (pageNum - 1);
            msql.append(" LIMIT ").append(start).append(",").append(pageSize);
        }
        if (isIgnoreLog(tablename)) {
            LOG.debug("\r\n" + msql.toString() + "\r\n" + StrUtil.objectToString(params));
        }
        QueryRunner qr = new QueryRunner();
        try {
            // 解码特殊字段名

            List<Map<String, Object>> list = qr.query(conn, msql.toString(), new MapListHandler(), params);
            JxLog jxlog = JxLogFactory.getJxLog(queryinfo.getAppname(), tablename);
            if (jxlog != null) {
                jxlog.debug(msql + "\r\n" + StrUtil.objectToString(params), "QUERY");
            }
           return toDataMapList(list);
        } catch (SQLException e) {
            LOG.error(e.getMessage() + "\r\n" + tablename + "\r\n" + msql + "\r\n" + StrUtil.objectToString(params));
        }
        return null;
    }

    @Override
    public long getSequence(Connection conn, String sequenceName, boolean isNext) throws JxException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
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
        InputStream in = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(msql.toString());
            pst.setString(1, uidValue);
            rs = pst.executeQuery();
            if (rs.next()) {
                in = rs.getBinaryStream(blobColumnName);
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

    @Override
    public String date2String(Object date) {
        return DateUtil.mysqlToDate(date);
    }

    @Override
    public String datetime2String(Object datetime) {
        return DateUtil.mysqlToDateTime(datetime);
    }

    @Override
    public String date2Year(String str) {
        return "DATE_FORMAT(" + str + ",'%Y')";
    }

    @Override
    public String date2YearMonth(String str) {
        return "DATE_FORMAT(" + str + ",'%Y-%m')";
    }
    
    @Override
    public String date2Month(String str) {
        return "DATE_FORMAT("+str+",'%m')";
    }
    
    @Override
    public String column2substr(String str,int start,int length){
        return "substring("+ str+","+ start+","+  length+")";
    }

}
