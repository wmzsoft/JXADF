package com.jxtech.app.jxlog;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft@gmail.com
 * @2014.12
 * 
 */
public class JxLogImpl implements JxLog {
    private static final Logger LOG = LoggerFactory.getLogger(JxLogImpl.class);
    private String app;
    private String jboname;

    public JxLogImpl() {

    }

    public JxLogImpl(String app, String jboname) {
        this.app = app;
        this.jboname = jboname;
    }

    protected void save(int level, String message, String action, String uid) throws JxException {
        if (level <= JxLog.DEBUG) {
            LOG.debug(getMessage(message, action, uid));
        } else if (level <= JxLog.INFO) {
            LOG.info(this.getMessage(message, action, uid));
        } else if (level <= JxLog.WARN) {
            LOG.warn(this.getMessage(message, action, uid));
        } else if (level <= JxLog.ERROR) {
            LOG.error(this.getMessage(message, action, uid));
        }
    }

    protected String getMessage(String message, String action, String uid) throws JxException {
        StringBuilder buf = new StringBuilder();
        if (app != null) {
            buf.append("\r\napp=" + app);
        }
        if (action != null) {
            buf.append("\r\naction=" + action);
        }
        if (jboname != null) {
            buf.append("\r\nname=" + jboname);
        }
        if (uid != null) {
            buf.append("\r\nuid=" + uid);
        }
        JxUserInfo userinfo = JxSession.getJxUserInfo();
        if (userinfo != null) {
            String usid = userinfo.getUserid();
            if (usid != null) {
                buf.append("\r\nuser=" + usid);
            }
        }
        if (!StrUtil.isNull(message)) {
            buf.append("\r\n" + message);
        }
        return buf.toString();
    }

    @Override
    public void debug(String message, String action, String uid) throws JxException {
        save(JxLog.DEBUG, message, action, uid);
    }

    @Override
    public void debug(String message, String action) throws JxException {
        debug(message, action, null);
    }

    @Override
    public void debug(JboIFace jbo, String action) throws JxException {
        if (jbo != null) {
            app = jbo.getJboSet().getAppname();
            jboname = jbo.getJboName();
            debug(jbo.toJson(), action, jbo.getUidValue());
        }
    }

    @Override
    public void info(String message, String action, String uid) throws JxException {
        save(JxLog.INFO, message, action, uid);
    }

    @Override
    public void info(String message, String action) throws JxException {
        info(message, action, null);
    }

    @Override
    public void info(JboIFace jbo, String action) throws JxException {
        if (jbo != null) {
            app = jbo.getJboSet().getAppname();
            jboname = jbo.getJboName();
            info(jbo.toJson(), action, jbo.getUidValue());
        }
    }

    @Override
    public void warn(String message, String action, String uid) throws JxException {
        save(JxLog.WARN, message, action, uid);
    }

    @Override
    public void warn(String message, String action) throws JxException {
        warn(message, action, null);
    }

    @Override
    public void warn(JboIFace jbo, String action) throws JxException {
        if (jbo != null) {
            app = jbo.getJboSet().getAppname();
            jboname = jbo.getJboName();
            warn(jbo.toJson(), action, jbo.getUidValue());
        }
    }

    @Override
    public void error(String message, String uid, String action) throws JxException {
        save(JxLog.ERROR, message, action, uid);
    }

    @Override
    public void error(String message, String action) throws JxException {
        error(message, action, null);
    }

    @Override
    public void error(JboIFace jbo, String action) throws JxException {
        if (jbo != null) {
            app = jbo.getJboSet().getAppname();
            jboname = jbo.getJboName();
            error(jbo.toJson(), action, jbo.getUidValue());
        }
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getJboname() {
        return jboname;
    }

}
