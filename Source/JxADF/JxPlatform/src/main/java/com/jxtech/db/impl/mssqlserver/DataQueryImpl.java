package com.jxtech.db.impl.mssqlserver;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;

/**
 * @author wmzsoft@gmail.com
 * @date 2015.07
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
        int pageNum = queryinfo.getPageNum();
        int pageSize = queryinfo.getPageSize();
        int end = 10;
        int start = 0;
        if (pageNum > 0) {
            if (pageSize <= 0) {
                pageSize = 20;
            }
            end = pageSize * pageNum;
            start = pageSize * (pageNum - 1);
        }
        String cause = queryinfo.getWhereAllCause();
        Object[] params = queryinfo.getWhereAllParams();
        if (pageNum <= 1) {
            // 只查询第一页，或全部
            msql.append("Select ");
            if (pageNum == 1) {
                msql.append(" TOP ").append(end);
            }
            msql.append(" * from ");

            boolean ist = false;
            if (tablename.trim().indexOf(' ') > 0) {
                msql.append("(");
                ist = true;
            }
            msql.append(tablename);
            if (ist) {
                msql.append(") n");
            }

            if (!StrUtil.isNull(cause)) {
                msql.append(" where ");
                msql.append(cause);
            }
            if (!StrUtil.isNull(queryinfo.getOrderby())) {
                msql.append("  order by  ");
                msql.append(queryinfo.getOrderby());
            }
        } else {
            // 查询第二页以后的内容
            msql.append("Select t1.* from ");
            msql.append(tablename).append(" t1 ,(select TOP ");
            msql.append(end).append(" row_number() OVER (ORDER BY ");
            String uidName = queryinfo.getJboset().getUidName();
            if (StrUtil.isNull(queryinfo.getOrderby())) {
                msql.append(uidName);
            } else {
                msql.append(queryinfo.getOrderby());
            }
            msql.append(") n,").append(uidName);
            msql.append(" from ").append(tablename);
            if (!StrUtil.isNull(cause)) {
                msql.append(" where ");
                msql.append(cause);
            }
            msql.append(" ) t2");
            msql.append(" where t1.").append(uidName).append("=t2.").append(uidName);
            msql.append(" AND t2.n > ").append(start).append(" ORDER BY t2.n ASC");
        }
        if (isIgnoreLog(tablename)) {
            LOG.debug("\r\n" + msql.toString() + "\r\n" + StrUtil.objectToString(params));
        }
        QueryRunner qr = new QueryRunner(true);
        try {
            List<Map<String, Object>> list = qr.query(conn, msql.toString(), new MapListHandler(), params);
            JxLog jxlog = JxLogFactory.getJxLog(queryinfo.getAppname(), tablename);
            if (jxlog != null) {
                jxlog.debug(msql + "\r\n" + StrUtil.objectToString(params), "QUERY");
            }
            vals = toDataMapList(list);
            return vals;
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
        // TODO Auto-generated method stub

    }
    
    @Override
    public String date2String(Object date) {     
        return DateUtil.sqlserverToDate(date);
    }

    @Override
    public String datetime2String(Object datetime) {
        return DateUtil.sqlserverToDateTime(datetime);
    }

    @Override
    public String date2Year(String str) {
        return "DATEPART(yy,"+str+")" ;
    }

}
