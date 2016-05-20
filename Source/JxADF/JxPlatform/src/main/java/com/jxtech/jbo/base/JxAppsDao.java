package com.jxtech.jbo.base;

import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.App;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 处理Maxapps表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxAppsDao {
    private static final Logger LOG = LoggerFactory.getLogger(JxAppsDao.class);
    public static final String CACHE_PREX = "APP.";

    public static JxApps getApp(String appName) throws JxException {
        if (StrUtil.isNull(appName)) {
            App app = JxSession.getApp();
            if (app != null) {
                appName = app.getAppName();
            }
        }
        if (StrUtil.isNull(appName)) {
            LOG.info("app name is null.");
            return null;
        }
        return query(appName);
    }

    private static JxApps query(String appName) throws JxException {
        if (StrUtil.isNull(appName)) {
            return null;
        }
        // 从缓存中，检查是否以前读取过基本信息
        String cachekey = StrUtil.contact(CACHE_PREX, appName.toUpperCase());
        Object obj = CacheUtil.getBase(cachekey);
        if (obj instanceof JxApps) {
            return (JxApps) obj;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        String msql = "Select * From MAXAPPS where app = ?";
        List<JxApps> list = dq.getResult(new BeanListHandler<JxApps>(JxApps.class), msql, new Object[] { appName.toUpperCase() });
        if (list != null && !list.isEmpty()) {
            JxApps app = list.get(0);
            CacheUtil.putBaseCache(cachekey, app);
            return app;
        } else {
            LOG.info("app[" + appName + "] is null，请在Maxapps中进行配置，谢谢。");
        }
        return null;
    }

    public static JxApps getAppByMaintbname(String tablename) throws JxException {
        if (StrUtil.isNull(tablename)) {
            return null;
        }
        // 从缓存中，检查是否以前读取过基本信息
        String cachekey = StrUtil.contact(CACHE_PREX, "t.", tablename.toUpperCase());
        Object obj = CacheUtil.getBase(cachekey);
        if (obj instanceof JxApps) {
            return (JxApps) obj;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        String msql = "Select * From MAXAPPS where maintbname = ?";
        List<JxApps> list = dq.getResult(new BeanListHandler<JxApps>(JxApps.class), msql, new Object[] { tablename.toUpperCase() });
        if (list != null && !list.isEmpty()) {
            JxApps app = list.get(0);
            CacheUtil.putBaseCache(cachekey, app);
            return app;
        }
        return null;

    }
}
