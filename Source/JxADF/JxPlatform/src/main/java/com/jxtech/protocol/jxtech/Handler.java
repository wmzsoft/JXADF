package com.jxtech.protocol.jxtech;

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

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        if (u == null) {
            return null;
        }
        if ("file".equalsIgnoreCase(u.getHost())) {
            StringBuilder sb = new StringBuilder();
            String fs = u.getFile();
            sb.append("file:///");
            int idx = fs.indexOf('/', 1);
            if (idx > 0) {
                String prop = fs.substring(1, idx);
                sb.append(System.getProperty(prop, prop));
                sb.append(fs.substring(idx));
            } else {
                // 这里最好不要使用LOG管理，因为可能需要打包放到java.ext.dir目录中
                System.out.println("路径可能存在问题：" + u.toString() + "\r\n" + fs);
                sb.append(fs);
            }
            // LOG.debug(sb.toString());
            URL nu = new URL(sb.toString());
            return nu.openConnection();
        }
        return null;
    }

}
