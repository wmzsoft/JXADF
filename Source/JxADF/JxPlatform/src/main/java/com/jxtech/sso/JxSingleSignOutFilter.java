package com.jxtech.sso;

import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.auth.PermissionIFace;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 *
 */
public class JxSingleSignOutFilter extends AbstractConfigurationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JxSingleSignOutFilter.class);
    private static final SingleSignOutHandler handler = new SingleSignOutHandler();

    public void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        }
        handler.init();
    }

    public void setArtifactParameterName(final String name) {
        handler.setArtifactParameterName(name);
    }

    public void setLogoutParameterName(final String name) {
        handler.setLogoutParameterName(name);
    }

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        handler.setSessionMappingStorage(storage);
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        // by wmzsoft.201301.27 以下5行
        PermissionIFace perm = PermissionFactory.getPermissionInstance();
        String currentURL = request.getServletPath();
        if (perm.isIgoreSecurity(currentURL)) {
            filterChain.doFilter(servletRequest, servletResponse);// 如果匹配，就表示忽略此滤镜，到下一个滤镜吧
            return;
        }

        if (handler.isTokenRequest(request)) {
            handler.recordSession(request);
        } else if (handler.isLogoutRequest(request)) {
            handler.destroySession(request);
            HttpSession session = request.getSession(false);
            if (session !=null){
                session.invalidate();
            }
            // Do not continue up filter chain
            return;
        } else {
            LOG.info("Ignoring URI " + request.getRequestURI());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        // nothing to do
    }

    protected static SingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }
}
