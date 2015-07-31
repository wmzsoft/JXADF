package com.jxtech.common;

import com.jxtech.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.11
 */
public class JxResource {
    // 添加到单个应用中的JS、CSS、HTML内容
    public static final String APP_REQUEST_BODY = "app_request_body";
    // 添加到单个应用中Head部份的JS、CSS、HTML内容
    public static final String APP_REQUEST_HEAD = "app_request_head";

    // 定义需要添加到整个应用框架中的内容：JS、CSS、HTML,Map<插件名，JS代码>
    private static Map<String, String> homeContent = new HashMap<String, String>();
    // 定义需要添加到每个应用程序中的JS、CSS、HTML内容,Map<插件名，JS代码>
    private static Map<String, String> appBody = new HashMap<String, String>();
    // 定义需要添加到每个应用程序的Head中的JS、CSS、HTML内容。,Map<插件名，JS代码>
    private static Map<String, String> appHead = new HashMap<String, String>();
    // 定义系统有的皮肤<描述,显示到界面的信息>，例：<default，<span onclick='switch("default")>默认</span>>
    private static Map<String, String> skins = new HashMap<String, String>();
    // 特定应用的JS、CSS、HTML内容。Map<应用程序名,Map<插件名,JS代码>>
    private static Map<String, Map<String, String>> specialAppBody = new HashMap<String, Map<String, String>>();
    // 定义当前系统加载的活动的Bundle，Map<Bundle-SymbolicName.Bundle-Version,Map<Bundle-Key,Bundle-Value>>
    private static Map<String, Map<String, Object>> myBundles = new HashMap<String, Map<String, Object>>();

    /**
     * 每个应用程序中需要要添加的内容
     * 
     * @param key
     * @param value
     */
    public static void putAppBody(String key, String value) {
        if (value == null) {
            appBody.remove(key);
        } else {
            appBody.put(key, value);
        }
    }

    /**
     * 单个应用中需要添加的内容
     * 
     * @param request
     * @param key
     * @param value
     */
    public static void putAppBody(HttpServletRequest request, String key, String value) {
        putContent(request, APP_REQUEST_BODY, key, value);
    }

    /**
     * 获得每个应用的Body中需要添加的代码信息
     * 
     * @param request
     * @return
     */
    public static String getAppBody(HttpServletRequest request, String appNameType) {
        // 获得当前页面需要添加的信息
        Map<String, String> appReq = getContent(request, APP_REQUEST_BODY);
        // 获得所有页面需要添加的信息
        if (appBody != null) {
            appReq.putAll(appBody);
        }
        // 添加特定应用需要添加的内容。
        if (!StrUtil.isNull(appNameType) && specialAppBody != null) {
            Map<String, String> special = specialAppBody.get(appNameType);
            if (special != null) {
                appReq.putAll(special);
            }
        }
        // 转换为字符串
        return valueToString(appReq);
    }

    /**
     * 整个应用框架需要添加的内容
     * 
     * @return
     */
    public static String getHomeContent() {
        return valueToString(homeContent);
    }

    /**
     * 整个应用框架需要添加的内容
     * 
     * @param key
     * @param value
     */
    public static void putHomeContent(String key, String value) {
        if (value != null) {
            homeContent.put(key, value);
        } else {
            homeContent.remove(key);
        }
    }

    public static Map<String, String> getHomeConents() {
        return homeContent;
    }

    /**
     * 每个应用程序中Head部份需要添加的内容
     * 
     * @param key
     * @param value
     */
    public static void putAppHead(String key, String value) {
        if (value == null) {
            appHead.remove(key);
        } else {
            appHead.put(key, value);
        }
    }

    /**
     * 单个应用程序Head部份需要添加的内容
     * 
     * @param request
     * @param key
     * @param value
     */
    public static void putAppHead(HttpServletRequest request, String key, String value) {
        putContent(request, APP_REQUEST_HEAD, key, value);
    }

    /**
     * 获得应用程序Head部份需要添加的内容。
     * 
     * @param request
     * @return
     */
    public static String getAppHead(HttpServletRequest request) {
        // 获得当前页面需要添加的信息
        Map<String, String> appReq = getContent(request, APP_REQUEST_HEAD);
        // 获得所有页面需要添加的信息
        if (appHead != null) {
            appReq.putAll(appHead);
        }
        // 转换为字符串
        return valueToString(appReq);
    }

    /**
     * 在特定的应用程序中插入一段JS、CSS、HTML代码。 可以支持多个插件往这里插入JS代码。
     * 
     * @param app ：应用程序名.应用程序类型
     * @param key : 关键字，一般为插件的名字
     * @param value
     */
    public static void putSpecialAppBody(String appNameType, String key, String value) {
        Map<String, String> keys = specialAppBody.get(appNameType);
        if (keys == null) {
            keys = new HashMap<String, String>();
        }
        if (value == null) {
            keys.remove(key);
        } else {
            keys.put(key, value);
        }
        specialAppBody.put(appNameType, keys);
    }

    /**
     * 是否加载了某个变量
     * 
     * @param request
     * @param key
     * @return
     */
    public static boolean isLoadKey(HttpServletRequest request, String key) {
        if (request == null || StrUtil.isNull(key)) {
            return false;
        }
        return (request.getAttribute(key) != null);
    }

    /**
     * 设置某个变量为TRUE
     * 
     * @param request
     * @param key
     */
    public static void setLoadKey(HttpServletRequest request, String key) {
        if (request == null || StrUtil.isNull(key)) {
            return;
        }
        request.setAttribute(key, "true");
    }

    public static Map<String, String> getSkins() {
        return skins;
    }

    public static String skins2String() {
        StringBuilder sb = new StringBuilder();
        if (skins != null) {
            for (Map.Entry<String, String> entry : skins.entrySet()) {
                sb.append(entry.getValue());
            }
        }
        if (sb.toString().indexOf("skinTo") > 0) {
            sb.append("<li><div class='skin-classics'  onclick='skinTo(\"/app.action?app=home\")'><span>经典</span></div></li>");
        }
        return sb.toString();
    }

    public static void putSkin(String key, String value) {
        if (value == null) {
            skins.remove(key);
        } else {
            skins.put(key, value);
        }
    }

    /**
     * 将MAP中的Value转为字符串
     * 
     * @param content
     * @return
     */
    private static String valueToString(Map<String, String> content) {
        if (content == null) {
            return "";
        }
        StringBuilder html = new StringBuilder();
        for (Map.Entry<String, String> enty : content.entrySet()) {
            html.append(enty.getValue());
        }
        return html.toString();
    }

    /**
     * 获得Request对象中的posKey的值
     * 
     * @param request
     * @param posKey
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, String> getContent(HttpServletRequest request, String posKey) {
        if (request == null) {
            return new HashMap<String, String>();
        }
        Object obj = request.getAttribute(posKey);
        if (obj != null && (obj instanceof Map)) {
            return (Map<String, String>) obj;
        } else {
            return new HashMap<String, String>();
        }
    }

    /**
     * 修改Request中PosKey的值
     * 
     * @param request
     * @param posKey
     * @param key
     * @param value
     */
    private static void putContent(HttpServletRequest request, String posKey, String key, String value) {
        if (StrUtil.isNull(key)) {
            return;
        }
        Map<String, String> content = getContent(request, posKey);
        if (value == null) {
            content.remove(key);
        } else {
            content.put(key, value);
        }
        request.setAttribute(posKey, content);
    }

    public static Map<String, Map<String, Object>> getMyBundles() {
        return myBundles;
    }

    public static void setMyBundles(Map<String, Map<String, Object>> myBundles) {
        JxResource.myBundles = myBundles;
    }

}
