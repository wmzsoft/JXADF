package com.jxtech.app.usermetadata;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 定义皮肤的基本信息,对外访问
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class MetaData {
    private static final Logger LOG = LoggerFactory.getLogger(MetaData.class);
    // 登录之后的第一个默认页面。
    public static final String HOME_PAGE = "HOMEPAGE";
    // 移动登录的第一个默认页面
    public static final String HOME_MOBILE_PAGE = "HOMEMOBILEPAGE";
    // 定义用户皮肤路径
    public static final String SKIN = "SKIN";
    // 定义主页打开的默认APP应用
    public static final String HOME_PAGE_APP = "HOMEPAGEAPP";
    // 定义退出程序
    public static final String LOGOUT = "LOGOUT";
    // 定义登录页面
    public static final String LOGIN = "LOGIN";
    // 定义无权限的页面
    public static final String NOT_PERMISSION = "NOT_PERMISSION";

    /**
     * 获得用户个性化数据
     * 
     * @return
     */
    public static Map<String, String> getUserMetadata() {
        JxUserInfo userinfo = JxSession.getJxUserInfo();
        if (userinfo != null) {
            return userinfo.getMetadata();
        }
        return null;
    }

    /**
     * 保存用户个性化数据
     * 
     * @param key
     * @param value
     * @throws JxException
     */
    public static void putUserMetadata(String key, String value) throws JxException {
        UserMetadataSetIFace umsi = (UserMetadataSetIFace) JboUtil.getJboSet("USERMETADATA");
        try {
            umsi.saveUserMetadata(key, value);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 获得用户个性化数据
     * 
     * @param key
     * @return
     */
    public static String getUserMetadata(String key) {
        Map<String, String> meta = getUserMetadata();
        String val = null;
        if (meta != null) {
            val = meta.get(key);
        }
        if (StrUtil.isNull(val)) {
            val = DefaultMetadata.getInstance().get(key);
        }
        return val;
    }

    /**
     * 获得用户自己定义的默认首页
     * 
     * @return
     */
    public static String getHomePage() {
        if (JxSession.isMobile()) {
            return getUserMetadata(HOME_MOBILE_PAGE);
        } else {
            return getUserMetadata(HOME_PAGE);
        }
    }

    /**
     * 获得用户自己选择的皮肤
     * 
     * @return
     */
    public static String getSkin() {
        return getUserMetadata(SKIN);
    }

    public static String getHomePageApp() {
        return getUserMetadata(HOME_PAGE_APP);
    }
}
