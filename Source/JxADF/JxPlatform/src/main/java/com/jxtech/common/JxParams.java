package com.jxtech.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jxtech.app.jxvars.JxVars;
import com.jxtech.app.jxvars.JxVarsFactory;
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

    private static class SingletonHolder {
        private static final JxParams INSTANCE = new JxParams();
    }

    private JxParams() {
        JxVars vars = JxVarsFactory.getInstance();
        this.userInfoUrl = vars.getValue("jx.comtop.userinfourl");
        this.loginUrl = vars.getValue("jx.loginurl");
        this.reportUrl = vars.getValue("jx.report.url");
        this.doclink = vars.getValue("jx.doclink");
        this.docpath = vars.getValue("jx.docpath");
        this.jndi = "true".equalsIgnoreCase(vars.getValue("jx.db.jndi", "false"));
        this.constantConnection = "true".equalsIgnoreCase(vars.getValue("jx.db.constantConnection", "false"));
        this.authenticate = vars.getValue("jx.authenticate.class");
        this.permission = vars.getValue("jx.permission.class");
        this.obpmJndi = vars.getValue("jx.obpm.jndi");
        this.obpmUser = vars.getValue("jx.obpm.user");
        this.obpmPass = vars.getValue("jx.obpm.pass");
        String ftypes = System.getProperty("jx.fileTypes");
        if (!StrUtil.isNull(ftypes)) {
            this.fileTypes = new HashMap<String, String>();
            Map<String, Object> fs = JsonUtil.json2map(ftypes);
            for (Map.Entry<String, Object> entry : fs.entrySet()) {
                fileTypes.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    public static JxParams getInstance() {
        return SingletonHolder.INSTANCE;
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
