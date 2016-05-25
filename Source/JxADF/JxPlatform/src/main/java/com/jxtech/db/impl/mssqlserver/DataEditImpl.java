package com.jxtech.db.impl.mssqlserver;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboValue;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class DataEditImpl extends com.jxtech.db.impl.DataEditImpl {
    private static final Logger LOG = LoggerFactory.getLogger(DataEditImpl.class);

    @Override
    public int execute(Connection conn, String msql, Object[] columns, Object[] values) throws JxException {
        return (int) execute(conn, msql, columns, values, false);
    }

    public long execute(Connection conn, String msql, Object[] columns, Object[] values, boolean insert) throws JxException {
        if (conn == null || StrUtil.isNull(msql)) {
            return -1;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            int size = values.length;
            if (insert) {
                ps = conn.prepareStatement(msql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(msql);
            }
            for (i = 0; i < size; i++) {
                JxAttribute attr = (JxAttribute) columns[i];
                int type = attr.getSqlType();
                if (StrUtil.isObjectNull(values[i])) {
                    ps.setNull(i + 1, type);
                } else {
                    if ((type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP)) {
                        java.sql.Timestamp d = DateUtil.toSqlTimestamp(values[i]);
                        ps.setObject(i + 1, d, type);
                    } else if (type==Types.CLOB){
                        ps.setString(i+1, (String)values[i]);
                    } else {
                        ps.setObject(i + 1, values[i], type);
                    }
                }
            }
            int u = ps.executeUpdate();
            if (u > 0 && insert) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return u;
        } catch (Exception e) {
            LOG.error(msql + "\r\ni=" + i + ",length=" + values.length + "\r\n" + columnValue2String(columns, values));
            throw new JxException(e.getMessage());
        } finally {
            JxDataSourceUtil.closeResultSet(rs);
            JxDataSourceUtil.closeStatement(ps);
        }
    }

    @Override
    public int update(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            LOG.info("传入的参数为空。");
            return -1;
        }
        String jboname = jbo.getJboSet().getEntityname();
        Map<String, Object> data = jbo.getData();
        if (data == null || !jbo.isModify()) {
            LOG.info("没有要保存的数据。" + jboname);
        }
        String keyName = jbo.getUidName();
        if (keyName == null) {
            LOG.info("没有找到主关键字：" + jboname);
            return -1;
        }
        Object keyValue = data.get(keyName);
        if (keyValue == null) {
            LOG.warn("传入的值不正确。");
            return -2;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("Update ").append(jboname).append(" Set ");
        List<Object> values = new ArrayList<Object>();
        List<JxAttribute> columns = new ArrayList<JxAttribute>();

        Map<String, JxAttribute> jas = jbo.getJxAttributes();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();// 属性名
            if (key.indexOf('.') > 0) {
                continue;
            }
            JxAttribute attr = jas.get(key);
            if (attr == null || !attr.isPersistent()) {
                continue;
            }
            JboValue value = jbo.getValue(key, false);
            if (value != null && value.isModify()) {// 只处理修改过的字段，不是更新全部字段
                if (!keyName.equalsIgnoreCase(key)) {
                    sql.append(key).append(" = ?,");
                    values.add(entry.getValue());
                    columns.add(attr);
                }
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(keyName).append("= ?");
        values.add(keyValue);
        columns.add(jas.get(keyName));
        int res = execute(conn, sql.toString(), columns.toArray(), values.toArray());
        // updateClob(conn, jbo, clobs);
        JxLog jxlog = JxLogFactory.getJxLog(jbo.getJboSet().getAppname(), jboname);
        if (jxlog != null) {
            jxlog.info(jbo, "UPDATE");
        }
        return res;
    }

    @Override
    public int insert(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            return -1;
        }
        Map<String, Object> data = jbo.getData();
        String tablename = jbo.getJboSet().getEntityname();
        String keyName = jbo.getUidName();
        // String keyValue = jbo.getUidValue();
        if (!checkValid(jbo)) {
            LOG.warn("数据有效性检查失败，保存不成功。");
            return -2;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Insert Into ").append(tablename).append("(");
        StringBuilder vsql = new StringBuilder();
        vsql.append(" ) values (");
        Map<String, JxAttribute> jas = jbo.getJxAttributes();
        List<Object> values = new ArrayList<Object>();
        List<JxAttribute> column = new ArrayList<JxAttribute>();// 字段
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (key.indexOf('.') > 0) {
                continue;
            }
            JxAttribute attr = jas.get(key);
            if (attr == null || !attr.isPersistent()) {
                continue;
            }
            if (key.equalsIgnoreCase(keyName)) {
                continue;
            } else {
                msql.append(key).append(",");
                vsql.append(" ?,");
                column.add(attr);
                values.add(entry.getValue());
            }
        }
        msql = StrUtil.deleteLastChar(msql);
        vsql = StrUtil.deleteLastChar(vsql);
        msql.append(vsql).append(")");
        long res = execute(conn, msql.toString(), column.toArray(), values.toArray(), true);
        // 补充一下序列的值
        if (res > 0) {
            jbo.setObject(jbo.getUidName(), res);
        }
        JxLog jxlog = JxLogFactory.getJxLog(jbo.getJboSet().getAppname(), jbo.getJboName());
        if (jxlog != null) {
            jxlog.info(jbo, "INSERT");
        }
        if (res > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public long insertBlob(Connection conn, String tbName, String uidName, String uidValue, String blobName, InputStream inputs, StringBuilder md5) throws JxException {
        // TODO Auto-generated method stub
        return 0;
    }

}
