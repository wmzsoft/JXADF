package com.jxtech.common;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 处理Spring的Bean
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */
public class BeanUtil {
    private static final Logger LOG = LoggerFactory.getLogger(BeanUtil.class);
    // private static String xmlfile = null;
    public static ApplicationContext ctx = null;

    public static Object getBean(String beanname) {
        if (beanname == null) {
            LOG.warn("beanname is null.");
            return null;
        }
        Object bean = null;
        if (ctx != null) {
            bean = ctx.getBean(beanname);
        } else {
            LOG.info("ApplicationContext is null.");
        }
        if (bean == null) {
            bean = getBeanOfDwr(beanname);
        }
        return bean;
    }

    /**
     * 供DWR框架调用获得Bean
     * 
     * @param beanName
     * @return
     */
    public static Object getBeanOfDwr(String beanName) {
        try {
            ServerContext ctx = ServerContextFactory.get();
            if (ctx == null) {
                LOG.warn("ServerContext[ServerContextFactory.get()] is null.");
                return null;
            }
            Container container = ctx.getContainer();
            return container.getBean(beanName);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static JxParams getJxParams() {
        return JxParams.getInstance();
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

}
