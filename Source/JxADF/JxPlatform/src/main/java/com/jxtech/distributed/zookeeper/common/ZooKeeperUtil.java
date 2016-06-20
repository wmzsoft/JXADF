package com.jxtech.distributed.zookeeper.common;

import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.Configuration;
import com.jxtech.util.StrUtil;

/**
 * ZooKeeper工具类
 * @author wmzsoft@gmail.com
 * @date 2016.06
 *
 */
public class ZooKeeperUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperUtil.class);

    /**
     * 删除节点以及所有子节点
     * 
     * @param zk
     * @param path
     */
    public static void delete(ZooKeeper zk, String path) {
        try {
            List<String> list = zk.getChildren(path, false);
            if (list != null && !list.isEmpty()) {
                Iterator<String> iter = list.iterator();
                while (iter.hasNext()) {
                    String sp = StrUtil.contact(path, Configuration.NODE_SEP, iter.next());
                    zk.delete(sp, -1);// 所有版本全部删除。
                }
            }
            zk.delete(path, -1);// 删除父节点
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
