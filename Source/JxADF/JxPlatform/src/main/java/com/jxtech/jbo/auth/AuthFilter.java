package com.jxtech.jbo.auth;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.usermetadata.DefaultMetadata;
import com.jxtech.app.usermetadata.MetaData;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.BrowserUtils;
import com.jxtech.util.StrUtil;

/**
 * 登录滤镜
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class AuthFilter implements Filter {
    public static final String LOGIN_URL = "jx.loginurl";
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        boolean isLogin = false;
        String uid = JxSession.getUserId();
        if (!StrUtil.isNull(uid)) {
            // 已登录过了
            isLogin = true;
        } else {
            String ssoUserId = req.getRemoteUser();
            if (StrUtil.isNull(ssoUserId)) {
                // 看看是否有内部认证机制
                String jxsessionid = request.getParameter(JxSessionID.ID);
                ssoUserId = JxSessionID.getUserId(jxsessionid);
            }
            if (!StrUtil.isNull(ssoUserId)) {
                // SSO已登录了。
                isLogin = JxSession.loginBySsoUser(ssoUserId);
                if (isLogin) {
                    // 配置浏览器类型
                    String renderer = req.getParameter("renderer");
                    if (StrUtil.isNull(renderer)) {
                        if (BrowserUtils.isPhone(req)) {
                            // 如果是手机端，强行使用Bootstrap
                            req.getSession().setAttribute(JxSession.RENDERER, "bootstrap");
                        }
                    }
                    // uid = ssoUserId;
                    JxUserInfo userInfo = JxSession.getJxUserInfo();
                    if (null != userInfo) {
                        String lang = request.getParameter("locale");
                        if (lang != null) {
                            userInfo.setLangcode(lang);
                        } else {
                            userInfo.setLangcode("zh_CN");
                        }
                        // 设定IP、机器名等信息
                        userInfo.setLoginIp(getIpAddr(req));
                        userInfo.setLoginMachine(req.getRemoteHost());
                    }
                }
            }
        }
        if (!isLogin) {
            PermissionIFace perm = PermissionFactory.getPermissionInstance();
            String url = req.getServletPath();
            // 没有登录 ，判断是否需要权限检查。
            if (perm.isIgoreSecurity(url)) {
                // 不检查权限，且没有登录，则自动添加匿名登录信息
                String langCode = req.getParameter("locale");
                if (StrUtil.isNull(langCode)) {
                    langCode = request.getLocale().toString();
                    JxSession.loadAnonymity(langCode, true);
                } else {
                    JxSession.loadAnonymity(langCode, false);
                }
                chain.doFilter(request, response);
            } else {
                // 跳转到登录页面
                String loginurl = getLoginUrl(req);
                ((HttpServletResponse) response).sendRedirect(loginurl);
            }
        } else {
            // 检查权限
            // 这里不使用URL来进行权限判断，直接通过标签处理来判断权限 by wmzsoft.2015.07
            chain.doFilter(request, response);
            // if (perm.isIgoreSecurity(url)) {
            // chain.doFilter(request, response);
            // } else {
            // url = req.getRequestURL().toString();
            // String qs = req.getQueryString();
            // if (!StrUtil.isNull(qs)) {
            // url = url + "?" + qs;
            // }
            // if (perm.hasFunctions(url)) {
            // chain.doFilter(request, response);
            // } else {
            // String s = DefaultMetadata.getInstance().get(MetaData.NOT_PERMISSION);
            // ((HttpServletResponse) response).sendRedirect(req.getContextPath() + s);
            // }
            // }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * 获得登录地址
     *
     * @param request
     * @return
     */
    private String getLoginUrl(HttpServletRequest request) {
        // String loginurl = System.getProperty(LOGIN_URL,"/login/login.jsp");
        String loginurl = DefaultMetadata.getInstance().get(MetaData.LOGIN);
        StringBuilder url = new StringBuilder();
        String path = request.getContextPath();
        url.append(path);
        url.append(loginurl);
        if (url.indexOf("?") > 0) {
            url.append("&");
        } else {
            url.append("?");
        }
        try {
            String oldurl = request.getRequestURL().toString();
            String qs = request.getQueryString();
            if (!StrUtil.isNull(qs)) {
                oldurl = oldurl + "?" + qs;
            }
            url.append("oldurl=");
            url.append(URLEncoder.encode(oldurl, "UTF-8"));
            url.append("&loginurl=");
            url.append(URLEncoder.encode(loginurl, "UTF-8"));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return url.toString();
    }

    /**
     * 获得客户端访问的IP地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
        }
        return ip;
    }
}
