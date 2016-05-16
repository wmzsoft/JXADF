package com.jxtech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

/**
 * User: jfy Date: 13-10-25 上午11:13
 */
public class MiscTool {
    private static Logger log = LoggerFactory.getLogger(MiscTool.class);

    public static final String TOMCAT = "tomcat";
    public static final String WEBLOGIC = "weblogic";
    private static HashMap<String, String> WLSAttr = null;
    private static String ip;

    public static String getAppServer() {
        if (isTomcat())
            return TOMCAT;
        else if (isWebLogic())
            return WEBLOGIC;
        return "unkown";
    }

    /**
     * 获取应用服务器相关参数
     * 
     * @return
     */
    public static HashMap<String, String> getAppServerAttrs(boolean isRefresh) {
        try {
            if (WLSAttr != null && !isRefresh)
                return WLSAttr;
            HashMap<String, String> res = new HashMap<String, String>();
            if (isTomcat()) {
                MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
                ObjectName oname = new ObjectName("Catalina:type=Connector,port=*");
                Set<ObjectName> names = mBeanServer.queryNames(oname, null);
                Integer port = 0;
                for (ObjectName o : names) {
                    String protocol = (String) mBeanServer.getAttribute(o, "protocol");
                    port = (Integer) mBeanServer.getAttribute(o, "port");
                    if ("HTTP/1.1".equals(protocol)) {
                        break;
                    }
                }
                ip = getIPAddress();
                res.put("name", TOMCAT);
                res.put("ip", ip);
                res.put("port", String.valueOf(port));
                res.put("defaultUrl", "http://" + ip + ":" + port);
                res.put("ipnet", ip);
                return res;
            } else if (isWebLogic()) {
                InitialContext ctxt = new InitialContext();
                MBeanServer connection = (MBeanServer) ctxt.lookup("java:comp/env/jmx/runtime");
                ObjectName rs = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
                ObjectName serverRt = (ObjectName) connection.getAttribute(rs, "ServerRuntime");
                ip = (String) connection.getAttribute(serverRt, "AdminServerHost");
                Integer port = (Integer) connection.getAttribute(serverRt, "AdminServerListenPort");
                String defaultUrl = (String) connection.getAttribute(serverRt, "DefaultURL");
                String ipnet = getIPAddress();
                res.put("name", WEBLOGIC);
                res.put("ip", ip);
                res.put("port", String.valueOf(port));
                res.put("defaultUrl", defaultUrl);
                res.put("ipnet", ipnet);
                WLSAttr = res;
            } else {
                throw new RuntimeException("不能识别当前应用服务器！");
            }
            return res;
        } catch (Exception ex) {
            log.info("检查" + getAppServer() + "地址失败:" + ex.getMessage());
        }
        return null;
    }

    /**
     * @return 本机主机名
     */
    public static String getHostName() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    /**
     * @return 本机IP 地址
     */
    public static String getIPAddress() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isTomcat() {
        return isClassAvailable("org.apache.jasper.servlet.JspServlet");
    }

    public static boolean isWebLogic() {
        return isClassAvailable("weblogic.servlet.JSPServlet");
    }

    /**
     * 检测虚拟机中某个Class是否存在
     * 
     * @param s
     * @return
     */
    public static boolean isClassAvailable(String s) {
        try {
            Class.forName(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getIp() {
        return ip;
    }
}
