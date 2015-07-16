package com.jxtech.sso;

import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.auth.PermissionIFace;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zhouyu on 2014/12/8. modify by wmzsoft on 2014.12
 */
public class JxAuthenticationFilter extends AbstractCasFilter {

    private static Logger LOG = LoggerFactory.getLogger(JxAuthenticationFilter.class);
    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        // 以下一行为于2014.12添加。
        System.getProperties().put("CAS_SERVER_URL", this.getPropertyFromInitParams(filterConfig, "casServerLoginUrl", (String) null));

        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));

            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    throw new ServletException(e);
                }
            }
        }
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        // by wmzsoft.201301.27 以下5行
        PermissionIFace perm = PermissionFactory.getPermissionInstance();
        String currentURL = request.getServletPath();
        if (perm.isIgoreSecurity(currentURL)) {
            filterChain.doFilter(request, response);// 如果匹配，就表示忽略此滤镜，到下一个滤镜吧
            return;
        }

        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;

        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }
        final String modifiedServiceUrl;
        LOG.debug("no ticket and no assertion found");
        if (this.gateway) {
            LOG.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }
        LOG.debug("Constructed service url: " + modifiedServiceUrl);
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
        LOG.debug("redirecting to \"" + urlToRedirectTo + "\"");
        response.sendRedirect(urlToRedirectTo);
    }

    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }

}
