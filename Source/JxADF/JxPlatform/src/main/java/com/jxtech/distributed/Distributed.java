package com.jxtech.distributed;

import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;

import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 分布式接口类
 * 
 * @author wmzsoft@gmail.com
 * @date 2016
 *
 */
public interface Distributed {
    public static final String DEPLOY = "distributed.deploy";
    public static final String SERVERS = "distributed.server";
    public static final String HOMEPAGE = "distributed.homepage";
    public static final String TIMEOUT = "distributed.timeout";
    public static final String MAX_IDLE = "distributed.maxIdle";
    public static final String INIT_IDLE_CAPACITY = "distributed.initIdleCapacity";
    public static final String BASE_BUNDLE = "distributed.base.bundle";
    public static final String BASE_SESSION = "distributed.base.session";
    public static final String SESSION_SHARE_SIZE = "distributed.session.share.pool.size";
    public static final String SESSION_SHARE_TIMEOUT = "distributed.session.share.timeout";

    /**
     * 初始化分布式环境
     * 
     * @param config
     * @return
     * @throws JxException
     */
    public boolean init() throws JxException;

    /**
     * 结束分布式环境的相关信息
     * 
     * @throws JxException
     */
    public void destory() throws JxException;

    /**
     * 注册bundle到服务器中，需要重载此方法，编写实现类,注册 Data中的内容为JSON格式，[{"app":"autokey","url":"/autokey/index.action"}]
     * 
     * @param bundle
     * @param wait
     *            是否等待返回结果
     * @return
     * @throws JxException
     */
    public boolean register(Bundle bundle, boolean wait) throws JxException;

    /**
     * 从注册表中删除，需要重载此方法，编写实现类
     * 
     * @param bundle
     * @param wait
     *            是否等待返回结果
     * @return
     * @throws JxException
     */
    public boolean unRegister(Bundle bundle, boolean wait) throws JxException;

    /**
     * 获得Bundle的URL地址
     * 
     * @param bundle
     * @return
     * @throws JxException
     */
    public String getUrl(Bundle bundle) throws JxException;

    /**
     * 加载所有的应用以及URL信息
     * 
     * @param params
     * @return {autokey=[{"APP"="autokey","URL"="/autokey/index.action","HOME"="http://127.0.0.1/jxweb"}]}
     * @throws JxException
     */
    public Map<String, List<Map<String, Object>>> getApps() throws JxException;

    /**
     * 根据当前URL，得到分布式插件的URL
     * 
     * @param url
     * @return
     * @throws JxException
     */
    public String getDistributedUrl(String url) throws JxException;

    /**
     * 获得用户信息
     * 
     * @param dsessionid
     * @return
     */
    public JxUserInfo getJxUserInfo(String dsessionid);

    /**
     * 获得用户ID
     * 
     * @param dsessionid
     * @return
     */
    public String getJxUserId(String dsessionid);

    /**
     * 从分布式服务器中删除用户信息
     * 
     * @param dsessionid
     * @param wait
     *            是否等待执行结果
     * @return
     */
    public boolean delJxUserInfo(String dsessionid, boolean wait);

    /**
     * 更新Session的访问时间
     * 
     * @param sessionid
     * @param userid
     * @param isCreate
     * @param wait
     *            是否等带结果
     * @return
     */
    public DistributedSession updateSessionTime(String sessionid, String userid, boolean isCreate, boolean wait);

    /**
     * 将信息保存在分布式服务器中
     * 
     * @param userinfo
     * @param dsessionid
     * @return
     */
    public boolean saveJxUserInfo(JxUserInfo userinfo, String dsessionid);

    /**
     * 移出过期的Session
     */
    public void removeTimeoutSession();
}
