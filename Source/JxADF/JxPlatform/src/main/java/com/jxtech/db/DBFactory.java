package com.jxtech.db;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

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
    public static Map<String, String> dbColumnImpl = new HashMap<String, String>();
    public static final String CACHE_PREX = "db.jboset.";
    private static DbColumn defaultDbColumn;
    private static Logger LOG = LoggerFactory.getLogger(DBFactory.class);

    static {
        dbQueryImpl.put(JxDataSourceUtil.ORACLE, "com.jxtech.db.impl.oracle.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.ORACLE, "com.jxtech.db.impl.oracle.DataEditImpl");
        dbColumnImpl.put(JxDataSourceUtil.ORACLE, "com.jxtech.db.impl.oracle.DbColumnImpl");

        dbQueryImpl.put(JxDataSourceUtil.MYSQL, "com.jxtech.db.impl.mysql.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.MYSQL, "com.jxtech.db.impl.mysql.DataEditImpl");
        dbColumnImpl.put(JxDataSourceUtil.MYSQL, "com.jxtech.db.impl.mysql.DbColumnImpl");

        dbQueryImpl.put(JxDataSourceUtil.MSSQLSERVER, "com.jxtech.db.impl.mssqlserver.DataQueryImpl");
        dbEditImpl.put(JxDataSourceUtil.MSSQLSERVER, "com.jxtech.db.impl.mssqlserver.DataEditImpl");
        dbColumnImpl.put(JxDataSourceUtil.MSSQLSERVER, "com.jxtech.db.impl.mssqlserver.DbColumnImpl");
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

    public static DbColumn getDbColumn(String dbtype) {
        if (StrUtil.isNull(dbtype)) {
            return getDefaultDbColumn();
        }
        String cn = dbColumnImpl.get(dbtype);
        if (!StrUtil.isNull(cn)) {
            Object obj = ClassUtil.getInstance(cn);
            if (obj instanceof DbColumn) {
                return (DbColumn) obj;
            }
        }
        return getDefaultDbColumn();
    }

    public static DbColumn getDefaultDbColumn() {
        if (defaultDbColumn == null) {
            String cl = System.getProperty("jx.db.column.class", null);
            if (!StrUtil.isNull(cl)) {
                Object obj = ClassUtil.getInstance(cl);
                if (obj instanceof DbColumn) {
                    defaultDbColumn = (DbColumn) obj;
                }
            }
            if (defaultDbColumn == null) {
                if (JxDataSourceUtil.isDbOfSystemOracle()) {
                    defaultDbColumn = new com.jxtech.db.impl.oracle.DbColumnImpl();
                } else if (JxDataSourceUtil.isDbOfSystemMsSqlServer()) {
                    defaultDbColumn = new com.jxtech.db.impl.mssqlserver.DbColumnImpl();
                } else if (JxDataSourceUtil.isDbOfSystemMySql()) {
                    defaultDbColumn = new com.jxtech.db.impl.mysql.DbColumnImpl();
                } else {
                    defaultDbColumn = new com.jxtech.db.impl.DbColumnImpl();
                    LOG.warn("请配置默认的数据字段信息类，jx.db.column.class");
                }
            }
        }
        return defaultDbColumn;
    }

    public static void setDefaultDbColumn(DbColumn defaultDbColumn) {
        DBFactory.defaultDbColumn = defaultDbColumn;
    }

}
