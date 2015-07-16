package com.jxtech.servlet;

import com.jxtech.i18n.JxLangResourcesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化上下文，包括安装数据库等
 *
 * @author wmzsoft@gmail.com
 */
public class InitContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(InitContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent paramServletContextEvent) {
        //初始化国际化支持
        JxLangResourcesUtil.init();
        LOG.debug(JxLangResourcesUtil.getString("servlet.InitContextListener.initlang"));
        LOG.debug(JxLangResourcesUtil.getString("servlet.InitContextListener.initsys"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
        LOG.debug("servlet.InitContextListener.exitsys");
    }

}
