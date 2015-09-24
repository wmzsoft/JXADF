package com.jxtech.jbo;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class AppSet {
    private static final Logger LOG = LoggerFactory.getLogger(AppSet.class);
    public static final String APP_TYPE_LIST = "LIST";// 列表应用程序
    public static final String APP_TYPE_MAIN = "MAIN";// 主表应用程序
    public static final String APP_TYPE_DEFAULT = APP_TYPE_MAIN; // 应用程序默认类型
    // 应用程序列表
    private List<String> list;// 做为List是方便移出最早的一个
    private Map<String, App> applist;// 应用程序列表
    // 当前App应用
    private App app;

    public final static int MAX_APPS = 10;// 最大同时支持多少个应用程序

    public void putApp(String appName, String appType) throws JxException {
        if (StrUtil.isNull(appName)) {
            return;
        }
        appName = appName.toUpperCase();
        if (appType == null) {
            appType = APP_TYPE_DEFAULT;
        } else {
            appType = appType.toUpperCase();
        }
        if (applist == null) {
            applist = new HashMap<String, App>();
        }
        if (list == null) {
            list = new ArrayList<String>();
        }
        String key = appName + "." + appType;
        // 1、检查是否存在，如果存在，则不做操作，否则新建
        if (applist.containsKey(key)) {
            app = applist.get(key);
            return;
        }
        // 2.不存在，新建APP
        app = new App(appName);
        app.setAppType(appType);
        list.add(0, key);// 插入到第一个
        applist.put(key, app);
        int size = list.size();
        // 3. 超出最大值，则移出最后一个
        if (size > MAX_APPS) {
            key = list.get(size - 1);
            list.remove(size - 1);
            applist.remove(key);
        }
    }

    public void removeApp(String appName, String appType) {
        if (applist == null) {
            return;
        }
        String key = appName + "." + appType;
        key = key.toUpperCase();
        // 1、检查是否存在，如果存在，则不做操作，否则新建
        if (applist.containsKey(key)) {
            applist.remove(key);
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (StrUtil.equals(key, list.get(i))) {
                        list.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public App getApp() throws JxException {
        if (app == null && applist != null && list != null) {
            if (applist.size() > 0 && list.size() > 0) {
                app = applist.get(list.get(0));
            }
        }
        if (app != null) {
            if (app.getJboset() == null) {
                app = new App(app.getAppName());
                applist.put(app.getAppNameType(), app);
            }
        }
        return app;
    }

    public String getAppName() throws JxException {
        if (app == null && applist != null && list != null) {
            if (applist.size() > 0 && list.size() > 0) {
                app = applist.get(list.get(0));
            }
        }
        if (app != null) {
            return app.getAppName();
        }
        return null;
    }

    public App getApp(String appName, String appType) throws JxException {
        if (StrUtil.isNull(appName) || applist == null) {
            return app;
        }
        appName = appName.toUpperCase();
        if (appType == null) {
            appType = APP_TYPE_DEFAULT;
        } else {
            appType = appType.toUpperCase();
        }
        String key = appName + "." + appType;
        App a = applist.get(key);
        if (a != null) {
            if (a.getJboset() == null) {
                a = new App(a.getAppName());
                applist.put(key, a);
            }
        }
        return a;
    }

    public App getApp(String appNameType) throws JxException {
        if (StrUtil.isNull(appNameType) || applist == null) {
            if (app == null) {
                getAllAppName();
            }
            return app;
        }
        App a = applist.get(appNameType.toUpperCase());
        if (a != null) {
            if (a.getJboset() == null) {
                a = new App(a.getAppName());
                applist.put(appNameType.toUpperCase(), a);
            }
        } else if (list != null) {
            if (appNameType.indexOf('.') < 0) {
                // 只传了APPName，没有传入Type
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String key = list.get(i).toUpperCase();
                    if (key.startsWith(appNameType.toUpperCase() + ".")) {
                        return applist.get(key);
                    }
                }
            }
        }
        return a;
    }

    /**
     * 将应用程序名字打出来看看先,主要用来调试。
     * 
     * @return
     */
    public String getAllAppName() {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            sb.append("\r\nlist=");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                sb.append(list.get(i) + ",");
            }
        }
        if (applist != null) {
            sb.append("\r\napplist=");
            for (Map.Entry<String, App> entry : applist.entrySet()) {
                sb.append(entry.getKey() + ",");
            }
        }
        LOG.debug("当前列表中的应用：\r\n" + sb.toString());
        return sb.toString();
    }

}
