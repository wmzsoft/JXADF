package com.jxtech.app.dashboard.plugin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenxiaomin@jxtech.net on 2015/1/14.
 */
public class DashboardLetPluginManager {
    private static final Logger LOG = LoggerFactory.getLogger(DashboardLetPluginManager.class);
    private static Map<String, IDashboardLetPlugin> DASHBOARD_LET_PLUGINS = new HashMap<String, IDashboardLetPlugin>();

    /**
     * 注册插件
     * 
     * @param name
     * @param plugin
     */
    public static void regiestLetPlugin(String name, IDashboardLetPlugin plugin) {
        String pluginName = name.toUpperCase();
        if (DASHBOARD_LET_PLUGINS.containsKey(pluginName)) {
            LOG.debug("名称为 : " + pluginName + " 的插件已经存在，使用新的覆盖老插件！");
        }
        DASHBOARD_LET_PLUGINS.put(pluginName, plugin);
    }

    /**
     * 移出插件
     * @param name
     */
    public static void unRegiestLetPlugin(String name) {
        if (name != null) {
            LOG.debug("移出插件：" + name);
            DASHBOARD_LET_PLUGINS.remove(name);
        }
    }

    /**
     * 获取插件集合
     * 
     * @return
     */
    public static Map<String, IDashboardLetPlugin> getDashboardLetPlugins() {
        return DASHBOARD_LET_PLUGINS;
    }
}
