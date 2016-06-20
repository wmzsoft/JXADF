package com.jxtech.distributed.zookeeper.common;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接ZooKeeper服务器的watcher实现
 * 
 * @author wmzsoft@gmail.com
 * 
 */
public class ConnectionWatcher implements Watcher {

    private static final int SESSION_TIMEOUT = 5000;
    private CountDownLatch signal = new CountDownLatch(1);
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 连接ZK客户端
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public ZooKeeper connection(String servers) {
        return connection(servers, SESSION_TIMEOUT);
    }

    /**
     * 连接ZK客户端
     * 
     * @param servers
     * @param sessionTimeout
     * @return
     */
    public ZooKeeper connection(String servers, int sessionTimeout) {
        ZooKeeper zk;
        try {
            zk = new ZooKeeper(servers, sessionTimeout, this);
            signal.await();
            return zk;
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    public void process(WatchedEvent event) {
        KeeperState state = event.getState();
        if (state == KeeperState.SyncConnected) {
            signal.countDown();
        }
    }
}
