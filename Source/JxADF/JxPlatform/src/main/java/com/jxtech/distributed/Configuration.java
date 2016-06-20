package com.jxtech.distributed;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxvars.JxVars;
import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.util.StrUtil;

/**
 * 配置信息实现类，用于读取和设置系统配置属性
 * 
 * @author wmzsoft@gmail.com
 */
public class Configuration {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    public static final String NODE_SEP = "/";
    /** 单例对象 */
    private static Configuration instance;

    // zookeeper 的服务器地址
    private String servers;
    // zookeeper 池最大空闲数
    private int maxIdle;
    // zookeeper 池的初始化大小
    private int initIdleCapacity;
    // zookeeper 客户端连接最长时间，毫秒
    private int timeOut;
    private boolean deploy;// 是否要进行分布式部署

    // 操作session的线程池大小
    private int sessionPoolSize;
    // 共享Session的溢出时间，单位分钟,get方法获得的是毫秒
    private long sessionTimeOut;

    // bundle的前缀
    private String baseBundle;
    // Session的前缀
    private String baseSession;

    // 当前服务的地址
    private String homepage;

    /**
     * 构造方法
     */
    protected Configuration() {
        JxVars var = JxVarsFactory.getInstance();
        deploy = var.getBoolean(Distributed.DEPLOY, false);
        servers = var.getValue(Distributed.SERVERS);
        timeOut = (int) var.getLong(Distributed.TIMEOUT, 5000);
        maxIdle = (int) var.getLong(Distributed.MAX_IDLE, 50);
        initIdleCapacity = (int) var.getLong(Distributed.INIT_IDLE_CAPACITY, 80);

        sessionPoolSize = (int) var.getLong(Distributed.SESSION_SHARE_SIZE, 100);
        if (sessionPoolSize < 0) {
            sessionPoolSize = 10;
        }
        // 读出来之后，直接转换为毫秒
        sessionTimeOut = var.getLong(Distributed.SESSION_SHARE_TIMEOUT, 30) * 60 * 1000;

        baseBundle = var.getValue(Distributed.BASE_BUNDLE, "/jxwebBundle");
        baseSession = var.getValue(Distributed.BASE_SESSION, "/jxwebSession");
        homepage = var.getValue(Distributed.HOMEPAGE);
        if (StrUtil.isNull(homepage)) {
            homepage = var.getValue("HOMEPAGE", "http://127.0.0.1/jxweb");
            if (deploy) {
                LOG.warn("please set " + Distributed.HOMEPAGE + ",current value is " + homepage);
            }
        }
        try {
            homepage = URLEncoder.encode(homepage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage() + "\r\nhomepage=" + homepage);
        }
    }

    /**
     * 返回实例的方法
     * 
     * @return
     */
    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Configuration [config=" + "]";
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getSessionPoolSize() {
        return sessionPoolSize;
    }

    public void setSessionPoolSize(int sessionPoolSize) {
        this.sessionPoolSize = sessionPoolSize;
    }

    public long getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(long sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getInitIdleCapacity() {
        return initIdleCapacity;
    }

    public void setInitIdleCapacity(int initIdleCapacity) {
        this.initIdleCapacity = initIdleCapacity;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isDeploy() {
        return deploy;
    }

    public void setDeploy(boolean deploy) {
        this.deploy = deploy;
    }

    public String getBaseBundle() {
        return baseBundle;
    }

    public void setBaseBundle(String baseBundle) {
        this.baseBundle = baseBundle;
    }

    public String getBaseSession() {
        return baseSession;
    }

    public void setBaseSession(String baseSession) {
        this.baseSession = baseSession;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
