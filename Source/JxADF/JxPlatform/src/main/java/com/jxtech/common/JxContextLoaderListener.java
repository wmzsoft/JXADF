package com.jxtech.common;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLStreamHandlerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.protocol.JxURLStreamHandlerFactory;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;

/**
 * @author wmzsoft
 * @date 2013.03.07
 */
public class JxContextLoaderListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(JxContextLoaderListener.class);
    private static final String JAVA_PROTOCOL = "java.protocol.handler.pkgs";
    private static final String JX_PROTOCOL = "jx.java.protocol.handler.pkgs";
    private static final String EXT_DIRS = "java.ext.dirs";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean ok = true;
        // DirContextURLStreamHandlerFactory.addUserFactory(new JxURLStreamHandlerFactory());
        // 处理Tomcat的协议加载，其它中间件，需要将jx-protocols-1.0放到java.ext.dirs 目录中
        String fn = "org.apache.naming.resources.DirContextURLStreamHandlerFactory";
        Class<?> cs = null;
        try {
            cs = Class.forName(fn);
            Method m = cs.getMethod("addUserFactory", new Class<?>[] { URLStreamHandlerFactory.class });
            try {
                m.invoke(cs, new Object[] { new JxURLStreamHandlerFactory() });
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOG.error(e.getMessage());
                ok = false;
            }
        } catch (ClassNotFoundException ex) {
            LOG.error(ex.getMessage());
            ok = false;
        } catch (NoSuchMethodException ne) {
            LOG.error(ne.getMessage());
            ok = false;
        }
        try {
            ServletContext sc = event.getServletContext();
            String realpath = sc.getRealPath("/");
            System.setProperty(SysPropertyUtil.WEB_REALPATH, realpath);
            if (!ok) {
                String extdir = System.getProperty(EXT_DIRS);
                if (extdir == null || extdir.indexOf(realpath) < 0) {
                    System.setProperty(EXT_DIRS, StrUtil.contact(extdir, File.pathSeparator, realpath, "WEB-INF", File.separator, "ext"));
                }
                LOG.info("\r\njava.ext.dirs=" + extdir + "\r\nsee:http://wiki.osgi.help/pages/viewpage.action?pageId=22578355");
            }
            // 处理协议
            String myprotocol = System.getProperty(JX_PROTOCOL, "com.jxtech.protocol");
            String pcs = System.getProperty(JAVA_PROTOCOL);
            if (!StrUtil.isNull(pcs)) {
                if (pcs.indexOf(myprotocol) < 0) {
                    System.setProperty(JAVA_PROTOCOL, StrUtil.contact(myprotocol, "|", pcs));
                    LOG.debug("protocol=" + System.getProperty(JAVA_PROTOCOL));
                }
            } else {
                System.setProperty(JAVA_PROTOCOL, myprotocol);
            }
            // 处理Context的信息
            String cp = sc.getServletContextName();// 需在web.xml文件中添加<display-name>/jxweb</display-name>
            if (StrUtil.isNull(cp)) {
                int len = realpath.length();
                if (len > 3) {
                    int start = realpath.lastIndexOf(File.separatorChar);
                    int end = len;
                    if (start == (len - 1)) {
                        start = realpath.lastIndexOf(File.separatorChar, len - 2);
                        end = len - 1;
                    }
                    cp = realpath.substring(start, end);
                } else {
                    cp = "/jxweb";
                }
            }
            System.setProperty(SysPropertyUtil.WEB_CONTEXT, cp);
            // 加载OSGi配置信息
            loadOsgiProperty(event, event.getServletContext());
            // 加载其它配置信息
            SysPropertyUtil.loadSystemProperty();
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
