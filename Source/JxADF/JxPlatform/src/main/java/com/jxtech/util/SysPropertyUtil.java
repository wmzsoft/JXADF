package com.jxtech.util;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * 处理本系统中要保存在Porperties中的内容
 * 
 * @author wmzsoft@gmail.com
 * @date 014.12
 * 
 */
public class SysPropertyUtil {
    public static final String WEB_CONFIG_FILE_NAME = "jxtech.properties";
    public static final String WEB_REALPATH = "jxtech.web.realpath";
    public static final String WEB_CONTEXT = "jxweb.context";
    public static final String UPLOAD_FILE_PATH = "upload";
    public static final String WEB_CONFIG_BASE = "base.properties";
    public static final String HOMEPAGE = "HOMEPAGE";
    private static Logger LOG = LoggerFactory.getLogger(SysPropertyUtil.class);

    public static void loadSystemProperty() {
        loadSystemProperty(getSystemPropertiesFileName());
        loadSystemProperty(getBaseFileName());
        //LOG.debug(System.getProperties().toString());
    }

    /**
     * 加载系统属性
     * 
     * @param filename
     */
    public static void loadSystemProperty(String filename) {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            System.getProperties().load(is);
            LOG.debug("Load " + filename);
        } catch (Exception ex) {
            LOG.warn("Load " + filename + " properties failed.\r\n" + ex.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 保存系统配置信息到jxtech.properties文件中
     * 
     * @param key
     * @param value
     */
    public static void storeSystemProperties(String key, String value) {
        String filename = getSystemPropertiesFileName();
        storeSystemProperties(filename, key, value);
    }

    /**
     * 保存信息到base.properties文件中
     * @param key
     * @param value
     */
    public static void storeBaseProperties(String key, String value) {
        String filename = getBaseFileName();
        storeSystemProperties(filename, key, value);
    }
    
    public static void storeSystemProperties(String filename, String key, String value) {
        Properties prop = new Properties();
        FileOutputStream fos = null;
        InputStream inStream = null;
        try {
            try {
                inStream = new FileInputStream(filename);
                prop.load(inStream);
            } catch (FileNotFoundException e) {
                LOG.error(e.getMessage());
            }
            if (value!=null){
                System.setProperty(key, value);
                fos = new FileOutputStream(filename);
                prop.setProperty(key, value);
                prop.store(fos, "Copyright (c) jxtech.net,homepage: http://osgia.com");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n" + filename, e);
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(fos);
        }
    }

    private static String getBaseFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty(WEB_REALPATH));
        sb.append("WEB-INF");
        sb.append(File.separator);
        sb.append("conf");
        sb.append(File.separator);
        sb.append(WEB_CONFIG_BASE);
        return sb.toString();
    }

    private static String getSystemPropertiesFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty(WEB_REALPATH));
        sb.append("WEB-INF");
        sb.append(File.separator);
        sb.append("conf");
        sb.append(File.separator);
        sb.append(WEB_CONFIG_FILE_NAME);
        return sb.toString();
    }

    /**
     * 获得WEB的Context
     * 
     * @return
     */
    public static String getBase() {
        return System.getProperty(WEB_CONTEXT, "/jxweb");
    }

    /**
     * 获得主页地址，例：http://osgi.jxtech.net/jxweb
     * 
     * @return
     */
    public static String getHomepage() {
        return System.getProperty(HOMEPAGE, "http://osgi.jxtech.net/jxweb");
    }

    public static int getPropertyOfInt(String key, int def) {
        String value = System.getProperty(key);
        if (!StrUtil.isNull(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    public static boolean getPropertyOfBool(String key, boolean def) {
        String value = System.getProperty(key);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("Y".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
            return true;
        } else if (!StrUtil.isNull(value)) {
            return false;
        }
        return def;
    }
    

}
