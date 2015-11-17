package com.jxtech.db.util;

import com.jxtech.app.usermetadata.DefaultMetadata;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健新数据源管理
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class JxDataSourceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JxDataSourceUtil.class);
    private static DataSource sysDataSource;// 系统数据源
    private static String sysDatabaseType = null;// 系统数据库类型
    public static final String SYSTEM_DATASOURCE_NAME = "JXADF_SYSTEM";
    public static final String ORACLE = "ORACLE";
    public static final String MYSQL = "MYSQL";
    public static final String MSSQLSERVER = "MSSQLSERVER";
    public static final String DB2 = "DB2";
    public static final String POSTGRESQL = "POSTGRESQL";
    private static boolean dbconntected=true;//系统数据库连接是否OK
    // 数据源缓存
    private static Map<String, DataSource> dsCache = new HashMap<String, DataSource>();

    /**
     * 获得数据库连接
     * 
     * @param sourceName 数据源名称，为空返回系统数据源
     * @return
     * @throws JxException
     */
    /**
     * 获得数据库连接
     * 
     * @param sourceName 数据源名称，为空返回系统数据源
     * @return
     * @throws JxException
     */
    public static Connection getConnection(String sourceName) throws JxException {
        return getConnection(sourceName, true);
    }

    /**
     * 获得数据库连接
     * 
     * @param sourceName
     * @param iscache 是否使用缓存
     * @return
     * @throws JxException
     */
    public static Connection getConnection(String sourceName, boolean iscache) throws JxException {
        if (StrUtil.isNull(sourceName) || SYSTEM_DATASOURCE_NAME.equalsIgnoreCase(sourceName)) {
            return getSystemConnection();
        } else {
            if (iscache) {
                DataSource cache = dsCache.get(sourceName);
                if (cache != null) {
                    try {
                        return cache.getConnection();
                    } catch (SQLException e) {
                        LOG.error(e.getMessage(), e);
                        dsCache.remove(sourceName);
                    }
                }
            }
            JboSetIFace js = JboUtil.getJboSet("JxDataSyncSource");
            if (js != null) {
                DataQueryInfo dqi = js.getQueryInfo();
                dqi.setWhereCause("Name=?");
                dqi.setWhereParams(new Object[] { sourceName });
                List<JboIFace> list = js.query();
                if (list != null && !list.isEmpty()) {
                    return getConnectionOfJbo(list.get(0));
                }
            }
        }
        return getSystemConnection();
    }

    /**
     * 根据数据源的名称获得数据源的类型
     * 
     * @param datasourceName
     * @return
     */
    public static String getDatasourceType(String datasourceName) {
        if (StrUtil.isNull(datasourceName)) {
            return sysDatabaseType;// 返回系统数据源的类型
        }
        Connection conn = null;
        try {
            conn = getConnection(datasourceName, true);
            return getDbTypeByDriver(conn.getMetaData().getDriverName());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            close(conn);
        }
        return null;
    }

    /**
     * 获得数据源
     * 
     * @param syncSource
     * @return
     * @throws JxException
     */
    public static DataSource getDataSource(JboIFace syncSource) throws JxException {
        if (syncSource == null) {
            LOG.debug("没找到数据源配置信息");
            return null;
        }
        DataSource dataSource = new BasicDataSource();
        BasicDataSource bds = (BasicDataSource) dataSource;
        bds.setDriverClassName(syncSource.getString("dbDriver"));
        bds.setUrl(syncSource.getString("dbUrl"));
        bds.setUsername(syncSource.getString("dbUser"));
        bds.setPassword(syncSource.getString("dbPassword"));
        int is = (int) syncSource.getLong("initNum");
        if (is > 0) {
            bds.setInitialSize(is);
        }
        int ms = (int) syncSource.getLong("maxNum");
        if (ms > is) {
            bds.setMaxActive(ms);
        }
        return dataSource;
    }

    /**
     * 获得数据库连接
     * 
     * @param syncSource
     * @return
     * @throws JxException
     */
    public static Connection getConnectionOfJbo(JboIFace syncSource) throws JxException {
        DataSource ds = getDataSource(syncSource);
        if (ds != null) {
            try {
                dsCache.put(syncSource.getString("Name"), ds);
                return ds.getConnection();
            } catch (SQLException e) {
                dsCache.remove(syncSource.getString("Name"));
                LOG.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 获得系统配置的数据库连接池
     * 
     * @return
     * @throws JxException
     */
    public static Connection getSystemConnection() throws JxException {
        if (sysDataSource == null) {
            String jndi = System.getProperty("jx.db.jndi");
            if ("true".equalsIgnoreCase(jndi)) {
                try {
                    Context ic = new InitialContext();
                    String jndiName = System.getProperty("jx.db.jndiName");
                    sysDataSource = (DataSource) ic.lookup("java:comp/env/" + jndiName);
                } catch (NamingException e) {
                    LOG.error(e.getMessage(), e);
                }
            } else {
                sysDataSource = getLocalDataSource();
            }
            if (sysDataSource == null) {
                DefaultMetadata.getInstance().toInsall();
                return null;
            }
        }
        try {
            Connection conn = sysDataSource.getConnection();
            if (sysDatabaseType == null) {
                String driv = conn.getMetaData().getDriverName();
                sysDatabaseType = getDbTypeByDriver(driv);
            }
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            LOG.error("获得数据库链接出错：" + e.getMessage());
            dbconntected=false;
            DefaultMetadata.getInstance().toInsall();
        }
        return null;
    }

    /**
     * 根据驱动程序名称获得数据源类型
     * 
     * @param drivier
     * @return
     */
    public static String getDbTypeByDriver(String drivier) {
        if (StrUtil.isNull(drivier)) {
            return System.getProperty("jx.db.type", ORACLE);
        } else if (StrUtil.indexOf(drivier, "(?i)ORACLE") >= 0) {
            return ORACLE;
        } else if (StrUtil.indexOf(drivier, "(?i)MYSQL") >= 0) {
            return MYSQL;
        } else if (StrUtil.indexOf(drivier, "(?i)sql\\s*server") >= 0) {
            return MSSQLSERVER;
        } else if (StrUtil.indexOf(drivier, "(?i)db2") >= 0) {
            return DB2;
        } else if (StrUtil.indexOf(drivier, "(?i)POSTGRESQL") >= 0) {
            return POSTGRESQL;
        } else {
            return System.getProperty("jx.db.type", ORACLE);
        }
    }

    public static BasicDataSource getLocalDataSource() {
        DataSource dataSource = new BasicDataSource();
        BasicDataSource bds = (BasicDataSource) dataSource;
        bds.setDriverClassName(System.getProperty("jx.db.driver"));
        bds.setUrl(System.getProperty("jx.db.url"));
        String un = System.getProperty("jx.db.user");
        if (!StrUtil.isNull(un)) {
            bds.setUsername(un);
        }
        String pwd = System.getProperty("jx.db.password");
        if (!StrUtil.isNull(pwd)) {
            bds.setPassword(pwd);
        }
        int is = SysPropertyUtil.getPropertyOfInt("jx.db.initialSize", 0);
        if (is > 0) {
            bds.setInitialSize(is);
        }
        int ma = SysPropertyUtil.getPropertyOfInt("jx.db.maxActive", 0);
        if (ma > is) {
            bds.setMaxActive(ma);
        }
        int mi = SysPropertyUtil.getPropertyOfInt("jx.db.maxIdle", 0);
        if (mi > 0) {
            bds.setMaxIdle(mi);
        }
        return bds;
    }

    /**
     * 仅用于TestCase
     */
    public static void setTestLocalDataSource() {
        sysDataSource = getLocalDataSource();
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public static boolean commit(Connection conn) {
        try {
            conn.commit();
            return true;
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    public static boolean rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static BasicDataSource getDataSource(String driver, String dburl, String username, String password) {
        DataSource dataSource = new BasicDataSource();
        BasicDataSource bds = (BasicDataSource) dataSource;
        bds.setDriverClassName(driver);
        bds.setUrl(dburl);
        bds.setUsername(username);
        bds.setPassword(password);
        return bds;
    }

    public static Connection getConnection(String driver, String dburl, String username, String password) throws SQLException {
        DataSource ds = getDataSource(driver, dburl, username, password);
        if (ds == null) {
            return null;
        }
        return ds.getConnection();
    }

    public static DataSource getSysDataSource() {
        return sysDataSource;
    }

    public static void setSysDataSource(DataSource sysDataSource) {
        JxDataSourceUtil.sysDataSource = sysDataSource;
    }

    public static String getSysDatabaseType() {
        return sysDatabaseType;
    }

    public static void setSysDatabaseType(String datatype) {
        sysDatabaseType = datatype;
    }

    /**
     * 系统数据源是否是Oracle
     * 
     * @return
     */
    public static boolean isDbOfSystemOracle() {
        if (ORACLE.equals(sysDatabaseType) || sysDatabaseType == null) {
            return true;
        }
        return false;
    }

    /**
     * 系统数据源是否为MySQL
     * 
     * @return
     */
    public static boolean isDbOfSystemMySql() {
        return MYSQL.equals(sysDatabaseType);
    }

    /**
     * 系统数据源是否是MS SQL Server
     * 
     * @return
     */
    public static boolean isDbOfSystemMsSqlServer() {
        return MSSQLSERVER.equals(sysDatabaseType);
    }

    public static boolean isDbconntected() {
        return dbconntected;
    }
}
