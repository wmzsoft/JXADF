package com.jxtech.db;

import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class DBFactory {

    /**
     * 数据库在本平台中的实现,数据库名=实现类名，例：ORACLE = com.jxtech.db.impl.oracle.DataQueryImpl
     */
    public static Map<String, String> dbQueryImpl = new HashMap<String, String>();
    public static Map<String, String> dbEditImpl = new HashMap<String, String>();
    public static final String CACHE_PREX = "db.jboset.";

    static {
        dbQueryImpl.put(JxDataSourceUtil.ORACLE, "com.jxtech.db.impl.oracle.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.ORACLE, "com.jxtech.db.impl.oracle.DataEditImpl");
        dbQueryImpl.put(JxDataSourceUtil.MYSQL, "com.jxtech.db.impl.mysql.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.MYSQL, "com.jxtech.db.impl.mysql.DataEditImpl");
        dbQueryImpl.put(JxDataSourceUtil.MSSQLSERVER, "com.jxtech.db.impl.mssqlserver.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.MSSQLSERVER, "com.jxtech.db.impl.mssqlserver.DataEditImpl");
    }

    public static void putDbQueryImplClass(String name, String classname) {
        dbQueryImpl.put(name, classname);
    }

    public static void putDbEditImplClass(String name, String classname) {
        dbEditImpl.put(name, classname);
    }

    /**
     * 获得
     * 
     * @param dbtype
     * @return
     */
    public static DataQuery getDataQuery(String dbtype, String dataSourceName) {
        if (StrUtil.isNull(dbtype)) {
            dbtype = JxDataSourceUtil.getDatasourceType(dataSourceName);
        }
        if (StrUtil.isNull(dbQueryImpl.get(dbtype))) {
            return getDefaultDataQuery(dataSourceName);
        } else {
            Object obj = ClassUtil.getInstance(dbQueryImpl.get(dbtype));
            if (obj instanceof DataQuery) {
                DataQuery dq = (DataQuery) obj;
                dq.setDataSourceName(dataSourceName);
                return dq;
            }
        }
        return getDefaultDataQuery(dataSourceName);
    }

    public static DataQuery getDefaultDataQuery(String dataSourceName) {
        String cn = System.getProperty("jx.db.query.class", "com.jxtech.db.impl.oracle.DataQueryImpl");
        Object obj = ClassUtil.getInstance(cn);
        if (obj instanceof DataQuery) {
            DataQuery dq = (DataQuery) obj;
            dq.setDataSourceName(dataSourceName);
            return dq;
        }
        return null;
    }

    public static DataEdit getDataEdit(String dbtype, String dataSourceName) {
        if (StrUtil.isNull(dbtype)) {
            dbtype = JxDataSourceUtil.getDatasourceType(dataSourceName);
        }
        if (StrUtil.isNull(dbEditImpl.get(dbtype))) {
            return getDefaultDataEdit(dataSourceName);
        } else {
            Object obj = ClassUtil.getInstance(dbEditImpl.get(dbtype));
            if (obj instanceof DataEdit) {
                DataEdit de = (DataEdit) obj;
                de.setDataSourceName(dataSourceName);
                return de;
            }
        }
        return getDefaultDataEdit(dataSourceName);
    }

    public static DataEdit getDefaultDataEdit(String dataSourceName) {
        String cn = System.getProperty("jx.db.edit.class", "com.jxtech.db.impl.oracle.DataEditImpl");
        Object obj = ClassUtil.getInstance(cn);
        if (obj instanceof DataEdit) {
            DataEdit de = (DataEdit) obj;
            de.setDataSourceName(dataSourceName);
            return de;
        }
        return null;
    }

}
