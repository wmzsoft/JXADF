package com.jxtech.protocol.jxtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * 定义自己的协议
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.01
 * 
 */
public class Handler extends URLStreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        if (u == null) {
            return null;
        }
        if ("file".equalsIgnoreCase(u.getHost())) {
            StringBuffer sb = new StringBuffer();
            String fs = u.getFile();
            sb.append("file:///");
            int idx = fs.indexOf('/', 1);
            if (idx > 0) {
                String prop = fs.substring(1, idx);
                sb.append(System.getProperty(prop, prop));
                sb.append(fs.substring(idx));
            } else {
                LOG.debug("路径可能存在问题：" + u.toString());
                sb.append(fs);
            }
            // LOG.debug(sb.toString());
            URL nu = new URL(sb.toString());
            return nu.openConnection();
        }
        return null;
    }

}
