package com.jxtech.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServerUtil.class);

    /**
     * 返回主机名以及所有的IP地址
     * 
     * @return
     */
    public List<String> getServers() {
        List<String> list = new ArrayList<String>();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String name = addr.getHostName();// 主机名
            list.add(name);
            InetAddress[] addrs = InetAddress.getAllByName(name);
            if (addrs != null && addrs.length > 0) {
                for (int i = 0; i < addrs.length; i++) {
                    list.add(addrs[i].getHostAddress());// IP地址
                }
            }
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage());
        }
        return list;
    }

}
