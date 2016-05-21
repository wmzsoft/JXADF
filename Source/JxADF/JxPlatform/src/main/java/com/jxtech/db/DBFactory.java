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
    private static final Map<String, String> dbQueryImpl = new HashMap<String, String>();
    private static final Map<String, String> dbEditImpl = new HashMap<String, String>();
    private static final Map<String, String> dbColumnImpl = new HashMap<String, String>();
    private static final Map<String, String> dbAttributeImpl = new HashMap<String, String>();
    // 默认的属性实现类
    private static String defaultAttributeImpl;
    // 实例
    private static final Map<String, DbColumn> dbColumnInstance = new HashMap<String, DbColumn>();
    public static final String CACHE_PREX = "db.jboset.";
    private static DbColumn defaultDbColumn;
    private static final Logger LOG = LoggerFactory.getLogger(DBFactory.class);

    static {
        dbQueryImpl.put(JxDataSourceUtil.ORACLE, System.getProperty(JxDataSourceUtil.DBQUERYIMPL, "com.jxtech.db.impl.oracle.DataQueryImpl"));
        dbEditImpl.put(JxDataSourceUtil.ORACLE, System.getProperty(JxDataSourceUtil.DBEDITIMPL, "com.jxtech.db.impl.oracle.DataEditImpl"));
        dbColumnImpl.put(JxDataSourceUtil.ORACLE, System.getProperty(JxDataSourceUtil.DBCOLUMNIMPL, "com.jxtech.db.impl.oracle.DbColumnImpl"));
        dbAttributeImpl.put(JxDataSourceUtil.ORACLE, System.getProperty(JxDataSourceUtil.DBATTRIBTEIMPL, "com.jxtech.jbo.base.impl.JxAttributeImpl"));

        dbQueryImpl.put(JxDataSourceUtil.MYSQL, System.getProperty(JxDataSourceUtil.DBQUERYIMPL, "com.jxtech.db.impl.mysql.DataQueryImpl"));
        dbEditImpl.put(JxDataSourceUtil.MYSQL, System.getProperty(JxDataSourceUtil.DBEDITIMPL, "com.jxtech.db.impl.mysql.DataEditImpl"));
        dbColumnImpl.put(JxDataSourceUtil.MYSQL, System.getProperty(JxDataSourceUtil.DBCOLUMNIMPL, "com.jxtech.db.impl.mysql.DbColumnImpl"));
        dbAttributeImpl.put(JxDataSourceUtil.MYSQL, System.getProperty(JxDataSourceUtil.DBATTRIBTEIMPL, "com.jxtech.jbo.base.impl.mysql.JxAttributeImpl"));

        dbQueryImpl.put(JxDataSourceUtil.MSSQLSERVER, System.getProperty(JxDataSourceUtil.DBQUERYIMPL, "com.jxtech.db.impl.mssqlserver.DataQueryImpl"));
        dbEditImpl.put(JxDataSourceUtil.MSSQLSERVER, System.getProperty(JxDataSourceUtil.DBEDITIMPL, "com.jxtech.db.impl.mssqlserver.DataEditImpl"));
        dbColumnImpl.put(JxDataSourceUtil.MSSQLSERVER, System.getProperty(JxDataSourceUtil.DBCOLUMNIMPL, "com.jxtech.db.impl.mssqlserver.DbColumnImpl"));
        dbAttributeImpl.put(JxDataSourceUtil.MSSQLSERVER, System.getProperty(JxDataSourceUtil.DBATTRIBTEIMPL, "com.jxtech.jbo.base.impl.mssqlserver.JxAttributeImpl"));

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
        String cn = System.getProperty(JxDataSourceUtil.DBQUERYIMPL, "com.jxtech.db.impl.oracle.DataQueryImpl");
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
        DbColumn dc = dbColumnInstance.get(dbtype);
        if (dc != null) {
            return dc;
        }
        String cn = dbColumnImpl.get(dbtype);
        if (!StrUtil.isNull(cn)) {
            Object obj = ClassUtil.getInstance(cn);
            if (obj instanceof DbColumn) {
                dc = (DbColumn) obj;
                dbColumnInstance.put(dbtype, dc);
                return dc;
            }
        }
        return getDefaultDbColumn();
    }

    public static DbColumn getDefaultDbColumn() {
        if (defaultDbColumn == null) {
            String cl = System.getProperty(JxDataSourceUtil.DBCOLUMNIMPL, null);
            if (StrUtil.isNull(cl)) {
                String dbtype = JxDataSourceUtil.getSysDatabaseType();// 获得默认的数据类型
                cl = dbColumnImpl.get(dbtype);
            }
            if (!StrUtil.isNull(cl)) {
                Object obj = ClassUtil.getInstance(cl);
                if (obj instanceof DbColumn) {
                    defaultDbColumn = (DbColumn) obj;
                }
            }
            if (defaultDbColumn == null) {
                defaultDbColumn = new com.jxtech.db.impl.oracle.DbColumnImpl();
                LOG.warn("Please config " + JxDataSourceUtil.DBCOLUMNIMPL);
            }
        }
        return defaultDbColumn;
    }

    public static void setDefaultDbColumn(DbColumn defaultDbColumn) {
        DBFactory.defaultDbColumn = defaultDbColumn;
    }

    /**
     * 返回默认的属性定义实现类
     * @return
     */
    public static String getDefaultAttributeImpl() {
        if (!StrUtil.isNull(defaultAttributeImpl)) {
            return defaultAttributeImpl;
        }
        defaultAttributeImpl = System.getProperty(JxDataSourceUtil.DBATTRIBTEIMPL, null);
        if (!StrUtil.isNull(defaultAttributeImpl)) {
            return defaultAttributeImpl;
        }
        String dbtype = JxDataSourceUtil.getSysDatabaseType();// 获得默认的数据类型
        defaultAttributeImpl = dbAttributeImpl.get(dbtype);
        if (!StrUtil.isNull(defaultAttributeImpl)) {
            return defaultAttributeImpl;
        }
        defaultAttributeImpl = dbAttributeImpl.get(JxDataSourceUtil.ORACLE);
        return defaultAttributeImpl;
    }
}
