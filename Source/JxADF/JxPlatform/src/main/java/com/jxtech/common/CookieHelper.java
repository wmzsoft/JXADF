package com.jxtech.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.Configuration;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.StrUtil;

/**
 * 操作Cookie的助手类，处理分布式SessionID
 * 
 * @author wmzsoft@gmail.com
 */
public class CookieHelper {
    protected static Logger LOG = LoggerFactory.getLogger(CookieHelper.class);

    /**
     * 将Session ID写到客户端的Cookie中
     * 
     * @param id
     *            Session ID
     * @param response
     *            HTTP响应
     * @return
     */
    public static Cookie writeSessionIdToNewCookie(String id, HttpServletResponse response, int expiry) {
        Cookie cookie = new Cookie(JxUserInfo.DISTRIBUTED_SESSION_ID, id);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
        return cookie;
    }

    /**
     * 将Session ID写到客户端的Cookie中
     * 
     * @param id
     *            Session ID
     * @param response
     *            HTTP响应
     * @return
     */
    public static Cookie writeSessionIdToCookie(String id, HttpServletRequest request, HttpServletResponse response, int expiry) {
        // 先查找
        Cookie cookie = findCookie(JxUserInfo.DISTRIBUTED_SESSION_ID, request);
        // 如果不存在，则新建一个
        if (cookie == null) {
            return writeSessionIdToNewCookie(id, response, expiry);
        } else if (cookie.getMaxAge() > 0) {
            // 直接改写cookie的值
            cookie.setValue(id);
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
        }
        return cookie;
    }

    public static Cookie writeSession(HttpServletRequest request, HttpServletResponse response) {
        if (request == null || response == null) {
            return null;
        }
        Configuration config = Configuration.getInstance();
        if (!config.isDeploy()) {
            return null;
        }
        // 先查找
        Cookie cookie = findCookie(JxUserInfo.DISTRIBUTED_SESSION_ID, request);
        int expiry = (int) config.getSessionTimeOut() / 1000;
        // 如果不存在，则新建一个
        if (cookie == null) {
            String id = StrUtil.getUUID();
            return writeSessionIdToNewCookie(id, response, expiry);
        } else if (cookie.getMaxAge() > 0) {
            // 直接改写cookie的值
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
        }
        return cookie;
    }

    /**
     * 查询指定名称的Cookie值
     * 
     * @param name
     *            cookie名称
     * @param request
     *            HTTP请求
     * @return
     */
    public static String findCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = findCookie(name, request);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 查询指定名称的Cookie
     * 
     * @param name
     *            cookie名称
     * @param request
     *            HTTP请求
     * @return
     */
    public static Cookie findCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || name == null) {
            return null;
        }
        // 迭代查找
        for (int i = 0, n = cookies.length; i < n; i++) {
            if (cookies[i].getName().equalsIgnoreCase(name)) {
                return cookies[i];
            }
        }
        return null;
    }

    /**
     * 查询所有名字相同的cookie
     * 
     * @param name
     * @param request
     * @return
     */
    public static List<Cookie> findCookies(String name, HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || name == null) {
            return null;
        }
        List<Cookie> list = new ArrayList<Cookie>();
        // 迭代查找
        for (int i = 0, n = cookies.length; i < n; i++) {
            if (cookies[i].getName().equalsIgnoreCase(name)) {
                list.add(cookies[i]);
            }
        }
        return list;
    }

    /**
     * 在Cookie中查找Session ID
     * 
     * @param request
     *            HTTP请求
     * @return
     */
    public static String findSessionId(HttpServletRequest request) {
        return findCookieValue(JxUserInfo.DISTRIBUTED_SESSION_ID, request);
    }

    public static String getDSessionId(JxUserInfo userinfo, HttpServletRequest request, HttpServletResponse response, boolean isCreate) {
        if (request == null) {
            request = JxSession.getRequest();
        }
        if (response == null) {
            response = JxSession.getResponse();
        }
        String dsessionid = null;
        if (userinfo != null) {
            Map<String, String> map = userinfo.getMetadata();
            dsessionid = map.get(JxUserInfo.DISTRIBUTED_SESSION_ID);
        }
        if (StrUtil.isNull(dsessionid)) {
            dsessionid = CookieHelper.findSessionId(request);
            if (StrUtil.isNull(dsessionid) && request != null && response != null && isCreate) {
                // 创建SessionID
                dsessionid = StrUtil.getUUID();
                long exp = Configuration.getInstance().getSessionTimeOut();
                writeSessionIdToNewCookie(dsessionid, response, (int) exp / 1000);
            }
        }
        return dsessionid;
    }

}
