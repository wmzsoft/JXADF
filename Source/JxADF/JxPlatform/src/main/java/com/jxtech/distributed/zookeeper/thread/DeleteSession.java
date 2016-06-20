package com.jxtech.distributed.zookeeper.thread;

import java.util.concurrent.Callable;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.zookeeper.common.ZooKeeperUtil;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.util.StrUtil;

/**
 * 删除Session
 * 
 * @author wmzsoft@gmail.com
 *
 */
public class DeleteSession implements Callable<Boolean> {
    private static Logger LOG = LoggerFactory.getLogger(DeleteSession.class);
    private String sessionid;

    public DeleteSession(String sessionid) {
        this.sessionid = sessionid;
    }

    @Override
    public Boolean call() throws Exception {
        if (StrUtil.isNull(sessionid)) {
            return true;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return false;
        }
        Configuration config = Configuration.getInstance();
        String path = StrUtil.contact(config.getBaseSession(), Configuration.NODE_SEP, sessionid);
        try {
            if (zk.exists(path, false) != null) {
                ZooKeeperUtil.delete(zk, path);
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return true;
    }

}
