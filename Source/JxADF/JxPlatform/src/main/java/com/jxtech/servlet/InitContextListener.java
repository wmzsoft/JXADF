package com.jxtech.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化上下文，包括安装数据库等
 *
 * @author wmzsoft@gmail.com
 */
public class InitContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(InitContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent paramServletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
        LOG.debug("servlet.InitContextListener.exitsys");
    }

}
