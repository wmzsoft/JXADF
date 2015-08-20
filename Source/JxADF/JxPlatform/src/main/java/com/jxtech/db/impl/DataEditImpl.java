package com.jxtech.db.impl;

import com.jxtech.app.jxlog.JxLog;
import com.jxtech.app.jxlog.JxLogFactory;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataEdit;
import com.jxtech.db.DataQuery;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public abstract class DataEditImpl implements DataEdit {
    private static final Logger LOG = LoggerFactory.getLogger(DataEdit.class);
    private String dataSourceName;// 数据源名称

    /**
     * 执行Insert 之前，检查数据必填项是否填写完毕。
     * 
     * @param jbo
     * @return
     */
    public boolean checkValid(JboIFace jbo) throws JxException {
        if (jbo == null) {
            return false;
        }
        Map<String, JxAttribute> jxattr = jbo.getJxAttributes();
        String jboname = jbo.getJboName();
        String keyName = jbo.getUidName();
        Map<String, Object> data = jbo.getData();
        for (Map.Entry<String, JxAttribute> entry : jxattr.entrySet()) {
            String key = entry.getKey();
            if ((keyName != null && key.equalsIgnoreCase(keyName))) {
                continue;
            }
            JxAttribute attr = entry.getValue();
            if (attr == null) {
                continue;
            } else if (attr.getDefaultValue() != null) {
                if (data.get(key) == null) {
                    data.put(key, attr.getDefaultValue());
                }
            } else if (attr.isRequired() && key.indexOf(".") == -1) {
                if (null == data.get(key)) {
                    LOG.debug("表名.字段名（" + jboname + "." + key + "）为必填项。");
                    throw new JxException("必须填写:" + attr.getObjectName() + " : " + attr.getTitle() + " : " + attr.getAttributeName());
                }
            }
        }
        return true;
    }

    /**
     * true if the first result is a ResultSet object; false if the first result is an update count or there is no result
     */
    public boolean execute(Connection conn, String msql, Object[] params) throws JxException {
        if (conn == null || StrUtil.isNull(msql)) {
            throw new JxException("conn or msql is null.");
        }
        if (params == null) {
            Statement st = null;
            try {
                st = conn.createStatement();
                return st.execute(msql);
            } catch (SQLException e) {
                LOG.error(e.getMessage() + "\r\n" + msql);
                throw new JxException(e.getLocalizedMessage());
            } finally {
                JxDataSourceUtil.closeStatement(st);
            }
        } else {
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(msql);
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                return ps.execute();
            } catch (SQLException e) {
                LOG.error(e.getMessage() + "\r\n" + msql);
                throw new JxException(e.getLocalizedMessage());
            } finally {
                JxDataSourceUtil.closeStatement(ps);
            }
        }
    }

    public int delete(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            LOG.info("传入的参数为空。");
            return -1;
        }
        String tabname = jbo.getJboSet().getEntityname();
        String msql = "Delete from " + tabname + " where " + jbo.getUidName() + "=?";
        Object[] columns = new Object[] { jbo.getJxAttributes().get(jbo.getUidName()) };
        JxLog jxlog = JxLogFactory.getJxLog(jbo.getJboSet().getAppname(), tabname);
        if (jxlog != null) {
            jxlog.info(jbo, "DELETE");
        }
        return execute(conn, msql, columns, new Object[] { jbo.getUidValue() });
    }

    /**
     * 保存数据
     * 
     * @param conn
     * @param jbo
     * @return
     */
    public boolean save(Connection conn, JboIFace jbo) throws JxException {
        if (conn == null || jbo == null) {
            LOG.warn("保存数据失败，conn is null or jbo is null.");
            return false;
        }
        Map<String, Object> data = jbo.getData();
        if (data == null) {
            LOG.info("没有要保存的数据");
            return false;
        }
        int res;
        if (jbo.isToBeAdd() && jbo.isToBeDel()) {
            return true;
        }
        String tableName = jbo.getJboSet().getEntityname();
        if (jbo.isToBeDel() && !jbo.isToBeAdd()) {
            if (delete(conn, jbo) > 0) {
                removeCache(tableName);
                return true;
            } else {
                return false;
            }
        }
        if (!jbo.isModify() && !jbo.isToBeAdd()) {
            return true;
        }
        String keyName = jbo.getUidName();
        if (StrUtil.isNull(keyName)) {
            throw new JxException("表" + tableName + "没有指定关键字，请与管理员联系。");
        }
        Object keyValue = data.get(keyName);
        JboSetIFace js = jbo.getJboSet();
        DataQuery dq = DBFactory.getDataQuery(js.getDbtype(), js.getDataSourceName());
        // 设定记录信息
        java.sql.Timestamp ts = DateUtil.sqlDateTime();
        JxUserInfo user = JxSession.getJxUserInfo();
        if (user != null && jbo != null) {
            jbo.setString("MODIFIER_ID", user.getUserid());
            jbo.setString("MODIFIER", user.getDisplayname());
            jbo.setObject("MODIFY_TIME", ts);
            jbo.setObject("MODIFY_DATE", ts);
            jbo.setString("CHANGEBY", user.getUserid());
            jbo.setObject("CHANGEDATE", ts);
        }
        if (dq.exist(conn, tableName, keyName, keyValue)) {
            // 更改记录，修改人、修改时间要设置为现在
            res = update(conn, jbo);
        } else {
            res = insert(conn, jbo);
        }
        if (res > 0) {
            removeCache(tableName);
            return true;
        } else {
            LOG.info("没有保存到数据，" + tableName + "." + keyName + "=" + keyValue);
            return false;
        }
    }

    public void removeCache(String tablename) {
        String key = StrUtil.contact(DBFactory.CACHE_PREX, tablename, ".");
        CacheUtil.removeJboOfStartWith(key);
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
