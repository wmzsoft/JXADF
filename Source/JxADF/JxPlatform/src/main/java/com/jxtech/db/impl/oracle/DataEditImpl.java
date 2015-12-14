package com.jxtech.db.impl.oracle;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.CLOB;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboValue;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxSequenceDao;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;

/**
 * 数据持久化
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class DataEditImpl extends com.jxtech.db.impl.DataEditImpl {
    private static final Logger LOG = LoggerFactory.getLogger(DataEditImpl.class);

    public int execute(Connection conn, String msql, Object[] columns, Object[] values) throws JxException {
        if (conn == null || StrUtil.isNull(msql)) {
            return -1;
        }
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();// 用来做调试的。
        int i = 0;
        try {
            int size = values.length;
            ps = conn.prepareStatement(msql);
            for (i = 0; i < size; i++) {
                JxAttribute attr = (JxAttribute) columns[i];
                int type = attr.getSqlType();
                if (StrUtil.isObjectNull(values[i])) {
                    sb.append(attr.getAttributeName() + "[" + i + "," + attr.getMaxType() + "," + type + "]=null\r\n");
                    ps.setNull(i + 1, type);
                } else {
                    sb.append(attr.getAttributeName() + "[" + i + "," + attr.getMaxType() + "," + type + "]=" + values[i] + "\r\n");
                    if ((type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP)) {
                        java.sql.Timestamp d = DateUtil.toSqlTimestamp(values[i]);
                        ps.setObject(i + 1, d, type);
                    } else {
                        ps.setObject(i + 1, values[i], type);
                    }
                }
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            LOG.error(msql + "\r\ni=" + i + ",length=" + values.length + "\r\n" + sb.toString());
            throw new JxException(e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    LOG.error(ex.getMessage());
                }
            }
        }
    }

    /**
     * 插入数据，执行insert语句
     * 
     * @param conn
     * @param jbo
     * @return
     */
    public int insert(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            return -1;
        }
        Map<String, Object> data = jbo.getData();
        String tablename = jbo.getJboSet().getEntityname();
        String keyName = jbo.getUidName();
        String keyValue = jbo.getUidValue();
        String seqname = JxSequenceDao.getSequence(tablename, keyName);
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
        Map<String, Object> clobs = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            JxAttribute attr = jas.get(key);
            if (key.indexOf(".") > 0 || attr == null || !attr.isPersistent()) {
                continue;
            }
            if (key.equalsIgnoreCase(keyName) && seqname != null && StrUtil.isNull(keyValue)) {
                msql.append(keyName).append(",");
                long seq = jbo.getNewSequence();
                jbo.setObject(jbo.getUidName(), seq);
                // vsql = vsql + seqname.trim() + ".nextval,";
                vsql.append(" ?,");
                column.add(attr);
                values.add(seq);
            } else {
                msql.append(key).append(",");
                if (Types.CLOB == attr.getSqlType()) {
                    vsql.append(" empty_clob(),");
                    if (entry.getValue() != null) {
                        clobs.put(key, entry.getValue());
                    }
                } else {
                    vsql.append(" ?,");
                    column.add(attr);
                    values.add(entry.getValue());
                }
            }
        }
        if (!StrUtil.isNull(keyName) && !data.containsKey(keyName)) {
            msql.append(keyName).append(",");
            // vsql = vsql + seqname.trim() + ".nextval,";
            long seq = jbo.getNewSequence();
            jbo.setObject(jbo.getUidName(), seq);
            JxAttribute uidAttribute = jbo.getJxAttribute(jbo.getUidName());
            vsql.append(" ?,");
            column.add(uidAttribute);
            values.add(seq);
        }
        msql = StrUtil.deleteLastChar(msql);
        vsql = StrUtil.deleteLastChar(vsql);
        msql.append(vsql).append(")");
        int res = execute(conn, msql.toString(), column.toArray(), values.toArray());
        updateClob(conn, jbo, clobs);
        JxLog jxlog = JxLogFactory.getJxLog(jbo.getJboSet().getAppname(), jbo.getJboName());
        if (jxlog != null) {
            jxlog.info(jbo, "INSERT");
        }
        return res;
    }

    /**
     * 更新数据
     * 
     * @param conn
     * @param jbo
     * @return
     * @throws JxException
     */
    public int update(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            LOG.info("传入的参数为空。");
            return -1;
        }
        String jboname = jbo.getJboSet().getEntityname();
        Map<String, Object> data = jbo.getData();
        if (data == null || !jbo.isModify()) {
            LOG.info("没有要保存的数据。" + jboname);
            return 0;
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

        Map<String, Object> clobs = new HashMap<String, Object>();// 需要修改的大字段

        Map<String, JxAttribute> jas = jbo.getJxAttributes();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();// 属性名
            JxAttribute attr = jas.get(key);
            if (key.indexOf(".") > 0 || attr == null || !attr.isPersistent()) {
                continue;
            }
            JboValue value = jbo.getValue(key, false);
            if (value != null && value.isModify()) {// 只处理修改过的字段，不是更新全部字段
                if (!keyName.equalsIgnoreCase(key)) {
                    if (Types.CLOB == attr.getSqlType()) {// 是 clob类型
                        sql.append(key).append(" = empty_clob(),");
                        Object ov = entry.getValue();
                        if (ov != null) {
                            clobs.put(key, entry.getValue());
                        }
                    } else {
                        sql.append(key).append(" = ?,");
                        values.add(entry.getValue());
                        columns.add(attr);
                    }
                }
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(keyName).append("= ?");
        values.add(keyValue);
        columns.add(jas.get(keyName));
        int res = execute(conn, sql.toString(), columns.toArray(), values.toArray());
        updateClob(conn, jbo, clobs);
        JxLog jxlog = JxLogFactory.getJxLog(jbo.getJboSet().getAppname(), jboname);
        if (jxlog != null) {
            jxlog.info(jbo, "UPDATE");
        }
        return res;
    }

    public long insertBlob(Connection conn, String tbName, String uidName, String uidValue, String blobName, InputStream inputs, StringBuffer md5) throws JxException {
        if (conn == null || StrUtil.isNull(tbName) || StrUtil.isNull(uidName) || StrUtil.isNull(uidValue)) {
            return -1;
        }
        if (inputs == null) {
            return -2;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 1.查询是否存在,
        StringBuilder msql = new StringBuilder();
        msql.append(" select ").append(uidName);
        msql.append(" from ").append(tbName);
        msql.append(" where ").append(uidName).append("=? ");
        long filesize = 0L;
        try {
            ps = conn.prepareStatement(msql.toString());
            ps.setString(1, uidValue);
            rs = ps.executeQuery();
            if (rs.next()) {
                // 存在记录，处理一下

            } else {
                rs.close();
                ps.close();
                // 2.插入一个空的东东
                msql.delete(0, msql.length());
                msql.append("Insert into ").append(tbName);
                msql.append("(").append(uidName);
                msql.append(",").append(blobName);
                msql.append(") values(?,empty_blob() )");
                ps = conn.prepareStatement(msql.toString());
                ps.setString(1, uidValue);
                ps.executeUpdate();
                ps.close();
            }
            // 3.执行更新
            msql.delete(0, msql.length());
            msql.append(" select ").append(blobName);
            msql.append(" from ").append(tbName);
            msql.append(" where ").append(uidName);
            msql.append("=?  for update");
            ps = conn.prepareStatement(msql.toString());
            ps.setString(1, uidValue);
            rs = ps.executeQuery();
            if (rs.next()) {
                oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(1);
                OutputStream out = blob.setBinaryStream(0);

                byte[] b = new byte[blob.getBufferSize()];
                int len = 0;

                while ((len = inputs.read(b)) != -1) {
                    out.write(b, 0, len);
                    filesize += len;
                }
                out.flush();
                out.close();
                return filesize;
            }
        } catch (Exception e) {
            LOG.error(msql + "," + uidValue);
            return -3;
        } finally {
            JxDataSourceUtil.closeResultSet(rs);
            JxDataSourceUtil.closeStatement(ps);
        }
        return filesize;
    }

    /**
     * 先将要更新的CLOB字段清空，不然长度小于原长度时，末尾的信息不能清除。
     * 
     * @param conn
     * @param jbo
     * @param clobs
     * @return
     * @throws JxException
     */
    public int updateClobEmpty(Connection conn, JboIFace jbo, Map<String, Object> clobs) throws JxException {
        if (conn == null || jbo == null || clobs == null) {
            return 0;
        }
        int size = clobs.size();
        if (size < 1) {
            // 没有需要更新的字段
            return 0;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Update ");
        msql.append(jbo.getJboSet().getEntityname());
        msql.append(" set ");
        int i = 0;
        for (Map.Entry<String, Object> entry : clobs.entrySet()) {
            msql.append(entry.getKey());
            msql.append("=empty_clob()");
            i++;
            if (i < size) {
                msql.append(",");
            }
        }
        msql.append(" where ");
        msql.append(jbo.getUidName());
        msql.append("=?");
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(msql.toString());
            ps.setString(1, jbo.getUidValue());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n" + msql.toString(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
        return 0;
    }

    /**
     * 更新CLOB字段信息
     * 
     * @param conn
     * @param jbo
     * @param clobs <字段名,字段值>
     * @return
     * @throws JxException
     */
    public int updateClob(Connection conn, JboIFace jbo, Map<String, Object> clobs) throws JxException {
        if (conn == null || jbo == null || clobs == null) {
            return 0;
        }
        int size = clobs.size();
        if (size < 1) {
            return 0;// 没有数据
        }
        // updateClobEmpty(conn, jbo, clobs);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String msql = " select * from " + jbo.getJboSet().getEntityname() + " where " + jbo.getUidName() + "=?  for update";
        try {
            ps = conn.prepareStatement(msql);
            ps.setString(1, jbo.getUidValue());
            rs = ps.executeQuery();
            size = 0;
            if (rs.next()) {
                for (Map.Entry<String, Object> entry : clobs.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Clob objclob = rs.getClob(key);
                    CLOB clob = (CLOB) objclob;

                    /*
                     * CLOB clob = null; if("weblogic.jdbc.wrapper.Clob_oracle_sql_CLOB".equals(objclob.getClass().getName())){ ///如果是WebLogic的驱动 Method method = objclob.getClass().getMethod("getVendorObj",new Class[]{}); clob = (oracle.sql.CLOB)method.invoke(objclob); } if("oracle.sql.CLOB".equals(objclob.getClass().getName())){ ////如果是Oracle的驱动 clob = (oracle.sql.CLOB)objclob; }
                     */
                    // oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(key);

                    @SuppressWarnings("deprecation")
                    Writer writer = clob.getCharacterOutputStream();
                    if (value instanceof CLOB) {
                        CLOB cv = (CLOB) value;
                        try {
                            int len = (int) cv.getLength();
                            writer.write(cv.getSubString(1, len));
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        }
                    } else {
                        writer.write((String) value);
                    }
                    writer.flush();
                    // clob.close();
                    IOUtils.closeQuietly(writer);
                }
                size++;
            }
            LOG.debug("保存了" + size + "个CLOB");
            return size;
        } catch (Exception e) {
            LOG.error(msql + "\r\n" + e.getMessage());
            throw new JxException("保存数据失败：" + e.getMessage());
        } finally {
            JxDataSourceUtil.closeResultSet(rs);
            JxDataSourceUtil.closeStatement(ps);
        }
    }
}
