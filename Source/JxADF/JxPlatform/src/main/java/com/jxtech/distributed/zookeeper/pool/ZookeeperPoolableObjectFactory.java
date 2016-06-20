package com.jxtech.distributed.zookeeper.pool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.zookeeper.common.ConnectionWatcher;

/**
 * Zookeeper实例对象池，由于一个Zookeeper实例持有一个Socket连接，所以将Zookeeper实例池化避免实例化过程中的消耗
 * 
 * @author wmzsoft@gmail.com
 */
public class ZookeeperPoolableObjectFactory implements PoolableObjectFactory {
    // private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperPoolableObjectFactory.class);

    /** 配置信息对象 */
    private Configuration config;

    /**
     * 构造方法
     * 
     * @param config
     */
    public ZookeeperPoolableObjectFactory(Configuration config) {
        this.config = config;
    }

    @Override
    public ZooKeeper makeObject() throws Exception {
        // 返回一个新的zk实例
        ConnectionWatcher cw = new ConnectionWatcher();
        // 连接服务端
        String servers = config.getServers();
        int timeout = config.getTimeOut();
        return cw.connection(servers, timeout);
    }

    @Override
    public void destroyObject(Object obj) throws Exception {
        if (obj instanceof ZooKeeper) {
            ((ZooKeeper) obj).close();
        }
    }

    @Override
    public boolean validateObject(Object obj) {
        if (obj instanceof ZooKeeper) {
            ZooKeeper zk = (ZooKeeper) obj;
            return zk.getState() == States.CONNECTED;
        }
        return false;
    }

    @Override
    public void activateObject(Object obj) throws Exception {
    }

    @Override
    public void passivateObject(Object obj) throws Exception {
    }

}
