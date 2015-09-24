package com.jxtech.common;

import java.io.InputStream;
import java.net.URLStreamHandlerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.protocol.JxURLStreamHandlerFactory;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;

/**
 * @author wmzsoft
 * @date 2013.03.07
 */
public class JxContextLoaderListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(JxContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            // URL.setURLStreamHandlerFactory(new JxURLStreamHandlerFactory());
            //DirContextURLStreamHandlerFactory.addUserFactory(new JxURLStreamHandlerFactory());
            // 处理Tomcat的协议加载，其它中间件，需要将jx-protocols-1.0放到java.ext.dirs 目录中
            ClassUtil.getStaticMethod("org.apache.naming.resources.DirContextURLStreamHandlerFactory", "addUserFactory", new Class<?>[] { URLStreamHandlerFactory.class }, new Object[] { new JxURLStreamHandlerFactory() });
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n加载jxtech协议失败，请将jxtech-protocols-1.0.jar放到java.ext.dirs目录中", e);
        }
        try {
            ServletContext sc = event.getServletContext();
            System.setProperty(SysPropertyUtil.WEB_REALPATH, sc.getRealPath("/"));
            String cp = sc.getServletContextName();// 需在web.xml文件中添加<display-name>/jxweb</display-name>
            if (StrUtil.isNull(cp)) {
                String rp = sc.getResource("/").getPath();
                if (rp.endsWith("/")) {
                    rp = rp.substring(0, rp.length() - 1);
                }
                cp = rp.substring(rp.lastIndexOf("/"));
            }
            System.setProperty(SysPropertyUtil.WEB_CONTEXT, cp);
            loadOsgiProperty(event, event.getServletContext());
            SysPropertyUtil.loadSystemProperty();
            // 处理协议
            String myprotocol = System.getProperty("jx.java.protocol.handler.pkgs");
            if (!StrUtil.isNull(myprotocol)) {
                String pcs = System.getProperty("java.protocol.handler.pkgs");
                if (!StrUtil.isNull(pcs)) {
                    if (pcs.indexOf(myprotocol) < 0) {
                        System.setProperty("java.protocol.handler.pkgs", StrUtil.contact(myprotocol, "|", pcs));
                        LOG.debug("protocol=" + System.getProperty("java.protocol.handler.pkgs"));
                    }
                } else {
                    System.setProperty("java.protocol.handler.pkgs", myprotocol);
                }
            }
            // 初始化缓存
            CacheUtil.initCache();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        CacheUtil.shutdown();
    }

    /**
     * 加载OSGi属性
     * 
     * @param event
     * @param servletContext
     */
    protected void loadOsgiProperty(ServletContextEvent event, ServletContext servletContext) {
        String configPropsFileValue = servletContext.getInitParameter("osgi.config.properties");
        if (configPropsFileValue == null || "".equals(configPropsFileValue)) {
            return;
        }
        InputStream is = null;
        try {
            is = servletContext.getResourceAsStream(configPropsFileValue);
            System.getProperties().load(is);
        } catch (Exception ex) {
            LOG.error("Load osgi properties failed.\r\n" + ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
