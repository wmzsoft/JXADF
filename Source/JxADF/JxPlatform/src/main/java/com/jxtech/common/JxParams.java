package com.jxtech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring 中的参数配置，可以读取
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 */
public class JxParams implements ApplicationContextAware {

    private String userInfoUrl;// 获得用户信息的URL地址。
    private String loginUrl;// 本系统的登录地址。
    private String reportUrl;// 报表地址
    private String comtopUrl;// 康拓普地址
    private String doclink;// 文件访问连接地址
    private String docpath;// 文件存储地址
    private boolean jndi;
    private boolean constantConnection;// 是否采用常连接
    private Map<String, String> fileTypes;// 文件类型、缩略图的显示地址,为空代表直接显示
    private List<String> sqlFiles;// 需要安装的SQL文件列表
    private String authenticate;// 认证实现类
    private String permission;// 权限验证实现类

    private String obpmUser;// oracle bpm 管理用户
    private String obpmPass;// Oracle bpm 管理用户密码
    private String obpmJndi;// oracle bpm 访问地址

    private static final Logger LOG = LoggerFactory.getLogger(JxParams.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.ctx = applicationContext;
    }

    public void init() {
        HashMap<String, String> env = MiscTool.getAppServerAttrs(true);
        MiscTool.IP = env.get("ip");
        LOG.debug(">>JxPlatform 初始化 OK！");
    }

    public boolean isJndi() {
        return jndi;
    }

    public void setJndi(boolean jndi) {
        this.jndi = jndi;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getComtopUrl() {
        return comtopUrl;
    }

    public void setComtopUrl(String comtopUrl) {
        this.comtopUrl = comtopUrl;
    }

    public String getDoclink() {
        return doclink;
    }

    public void setDoclink(String doclink) {
        this.doclink = doclink;
    }

    public String getDocpath() {
        return docpath;
    }

    public void setDocpath(String docpath) {
        this.docpath = docpath;
    }

    public boolean isConstantConnection() {
        return constantConnection;
    }

    public void setConstantConnection(boolean constantConnection) {
        this.constantConnection = constantConnection;
    }

    public Map<String, String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(Map<String, String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public List<String> getSqlFiles() {
        return sqlFiles;
    }

    public void setSqlFiles(List<String> sqlFiles) {
        this.sqlFiles = sqlFiles;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public String getObpmUser() {
        return obpmUser;
    }

    public void setObpmUser(String obpmUser) {
        this.obpmUser = obpmUser;
    }

    public String getObpmPass() {
        return obpmPass;
    }

    public void setObpmPass(String obpmPass) {
        this.obpmPass = obpmPass;
    }

    public String getObpmJndi() {
        return obpmJndi;
    }

    public void setObpmJndi(String obpmJndi) {
        this.obpmJndi = obpmJndi;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
