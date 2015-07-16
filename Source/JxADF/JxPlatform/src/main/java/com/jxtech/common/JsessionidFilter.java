package com.jxtech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 处理jsessionid自动添加的问题
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JsessionidFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(JsessionidFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // clear session if session id in URL
        if (httpRequest.isRequestedSessionIdFromURL()) {
            HttpSession session = httpRequest.getSession();
            if (session != null) {
                LOG.debug("session.invalidate");
                session.invalidate();
            }
        }

        // wrap response to remove URL encoding
        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
            @Override
            public String encodeRedirectUrl(String url) {
                LOG.debug(url);
                return url;
            }

            @Override
            public String encodeRedirectURL(String url) {
                LOG.debug(url);
                return url;
            }

            @Override
            public String encodeUrl(String url) {
                LOG.debug(url);
                return url;
            }

            @Override
            public String encodeURL(String url) {
                LOG.debug(url);
                return url;
            }
        };
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
