package com.jxtech.protocol;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * 自定义协议工厂
 * 
 * @author Administrator
 * 
 */
public class JxURLStreamHandlerFactory implements URLStreamHandlerFactory {

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("jxtech".equalsIgnoreCase(protocol)) {
            return new com.jxtech.protocol.jxtech.Handler();
        }
        return null;
    }

}
