package com.jxtech.app.jxlog;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 操作日志、痕迹保留
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 */
public interface JxLog {
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    public void debug(String message, String action, String uid) throws JxException;

    public void debug(String message, String action) throws JxException;

    public void debug(JboIFace jbo, String action) throws JxException;

    public void info(String message, String action, String uid) throws JxException;

    public void info(String message, String action) throws JxException;

    public void info(JboIFace jbo, String action) throws JxException;

    public void warn(String message, String action, String uid) throws JxException;

    public void warn(String message, String action) throws JxException;

    public void warn(JboIFace jbo, String action) throws JxException;

    public void error(String message, String action, String uid) throws JxException;

    public void error(String message, String action) throws JxException;

    public void error(JboIFace jbo, String action) throws JxException;

    public String getApp();

    public void setApp(String app);

    public String getJboname();

    public void setJboname(String jboname);
}
