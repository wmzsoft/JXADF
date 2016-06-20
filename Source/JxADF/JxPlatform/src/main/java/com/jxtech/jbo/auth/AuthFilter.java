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
import com.jxtech.util.UrlUtil;

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
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        boolean isLogin = false;
        boolean isClusterLogin = false;// 是否需要到集群中的其它服务器中验证
        String uid = JxSession.getUserId();
        if (!StrUtil.isNull(uid)) {
            // 已登录过了
            isLogin = true;
        } else {
            String ssoUserId = req.getRemoteUser();
            if (StrUtil.isNull(ssoUserId)) {
                // 看看是否有内部认证机制
                ssoUserId = JxSession.getUserid(req);
                if (StrUtil.isNull(ssoUserId)) {
                    String referer = request.getParameter("referer");
                    String jsessionid = request.getParameter("jsessionid");
                    ssoUserId = JxSession.getRemoteUser(referer, jsessionid);
                    if (StrUtil.isNull(referer) && StrUtil.isNull(jsessionid)) {
                        isClusterLogin = true;
                    }
                }
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
                    uid = ssoUserId;
                    JxUserInfo userInfo = JxSession.getJxUserInfo();
                    if (null != userInfo) {
                        String lang = request.getParameter("locale");
                        if (lang != null) {
                            userInfo.setLangcode(lang);
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
                String loginurl = getLoginUrl(req, isClusterLogin);
                ((HttpServletResponse) response).sendRedirect(loginurl);
            }
        } else {
            // 检查权限
            // 这里不使用URL来进行权限判断，直接通过标签处理来判断权限 by wmzsoft.2015.07
            chain.doFilter(request, response);
        }
    }

    /**
     * 获得登录地址
     *
     * @param request
     * @return
     */
    private String getLoginUrl(HttpServletRequest request, boolean clauser) {
        String referer = request.getHeader("Referer");
        StringBuilder loginurl = new StringBuilder();
        String baseurl = UrlUtil.getBaseUrl(referer);
        if (!StrUtil.isNull(baseurl) && clauser) {
            // 查看是否为集群或分布式过来的地址，是否需要到自动验证处
            // 首先检查是否为本系统先
            String active = UrlUtil.getUrlContent2(baseurl + "/login/active.jsp", null, 1000);// 最多1秒中
            if (!StrUtil.isNull(active)) {
                loginurl.append(baseurl);
                loginurl.append("/login/sso.jsp");
            }else{
                loginurl.append(request.getContextPath());
                loginurl.append(DefaultMetadata.getInstance().get(MetaData.LOGIN));
            }
        } else {
            loginurl.append(request.getContextPath());
            loginurl.append(DefaultMetadata.getInstance().get(MetaData.LOGIN));
        }
        StringBuilder url = new StringBuilder();
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
                oldurl = StrUtil.contact(oldurl, "?", qs);
            }
            url.append("oldurl=");
            url.append(URLEncoder.encode(oldurl, "UTF-8"));
            url.append("&referer=");
            url.append(URLEncoder.encode(referer, "UTF-8"));
            url.append("&loginurl=");
            url.append(URLEncoder.encode(loginurl.toString(), "UTF-8"));
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
