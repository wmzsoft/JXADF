package com.jxtech.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;

/**
 * Spring 中的参数配置，可以读取
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 */
public class JxParams implements Serializable {

    private static final long serialVersionUID = 988179958465814835L;
    private String userInfoUrl;// 获得用户信息的URL地址。
    private String loginUrl;// 本系统的登录地址。
    private String reportUrl;// 报表地址
    private String doclink;// 文件访问连接地址
    private String docpath;// 文件存储地址
    private boolean jndi;
    private boolean constantConnection;// 是否采用常连接
    private Map<String, String> fileTypes;// 文件类型、缩略图的显示地址,为空代表直接显示
    private String authenticate;// 认证实现类
    private String permission;// 权限验证实现类

    private String obpmUser;// oracle bpm 管理用户
    private String obpmPass;// Oracle bpm 管理用户密码
    private String obpmJndi;// oracle bpm 访问地址

    // private static final Logger LOG = LoggerFactory.getLogger(JxParams.class);
    private static JxParams instance;

    private JxParams() {

    }

    public static JxParams getInstance() {
        if (instance == null) {
            instance = new JxParams();
            instance.setUserInfoUrl(System.getProperty("jx.comtop.userinfourl"));
            instance.setLoginUrl(System.getProperty("jx.loginurl"));
            instance.setReportUrl(System.getProperty("jx.report.url"));
            instance.setDoclink(System.getProperty("jx.doclink"));
            instance.setDocpath(System.getProperty("jx.docpath"));
            instance.setJndi("true".equalsIgnoreCase(System.getProperty("jx.db.jndi", "false")));
            instance.setConstantConnection("true".equalsIgnoreCase(System.getProperty("jx.db.constantConnection", "false")));
            instance.setAuthenticate(System.getProperty("jx.authenticate.class"));
            instance.setPermission(System.getProperty("jx.permission.class"));
            instance.setObpmJndi(System.getProperty("jx.obpm.jndi"));
            instance.setObpmUser(System.getProperty("jx.obpm.user"));
            instance.setObpmPass(System.getProperty("jx.obpm.pass"));
            String ftypes = System.getProperty("jx.fileTypes");
            if (!StrUtil.isNull(ftypes)) {
                instance.fileTypes = new HashMap<String,String>();
                Map<String, Object> fs = JsonUtil.json2map(ftypes);
                Iterator<Entry<String, Object>> iter = fs.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
                    instance.fileTypes.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return instance;
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
