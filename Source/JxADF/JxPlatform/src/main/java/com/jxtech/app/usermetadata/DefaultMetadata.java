package com.jxtech.app.usermetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义了默认的属性，这个是全局唯一的。
 * 
 * @author Administrator
 * 
 */
public class DefaultMetadata {
    private static DefaultMetadata instance;
    private Map<String, String> metaData = null;
    public static final String LOGIN_URL = "/login/login.jsp";
    public static final String HOME_MOBILE_URL = "/app.action?app=home";
    public static final String HOME_URL = "/app.action?app=home";
    public static final String HOME_APP_URL = "/app.action?app=inbox&type=list";

    private DefaultMetadata() {
        metaData = new HashMap<String, String>();
        metaData.put(MetaData.HOME_PAGE, HOME_URL);
        metaData.put(MetaData.SKIN, "default");
        metaData.put(MetaData.HOME_PAGE_APP, HOME_APP_URL);
        metaData.put(MetaData.LOGOUT, "/login/logout.jsp");
        metaData.put(MetaData.LOGIN, LOGIN_URL);
        metaData.put(MetaData.NOT_PERMISSION, "/system/notpermission.jsp");
    }

    public static DefaultMetadata getInstance() {
        if (instance == null) {
            instance = new DefaultMetadata();
        }
        return instance;
    }

    public void toInsall() {
        metaData.put(MetaData.LOGIN, "/install/index.action");
        metaData.put(MetaData.HOME_PAGE, "/install/index.action");
    }

    public String get(String key) {
        return metaData.get(key);
    }

    public void put(String key, String value) {
        metaData.put(key, value);
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }
}
