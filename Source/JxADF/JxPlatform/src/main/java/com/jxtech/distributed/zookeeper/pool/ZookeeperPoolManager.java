package com.jxtech.distributed.zookeeper.pool;

import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.Configuration;

/**
 * ZK实例池管理器
 * 
 * @author wmzsoft@gmail.com
 */
public class ZookeeperPoolManager {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperPoolManager.class);

    /** 单例 */
    protected static ZookeeperPoolManager instance;

    private ObjectPool pool;

    /**
     * 构造方法
     */
    private ZookeeperPoolManager() {
        init();
    }

    /**
     * 返回单例的对象
     * 
     * @return
     */
    public static ZookeeperPoolManager getInstance() {
        if (instance == null) {
            instance = new ZookeeperPoolManager();
        }
        return instance;
    }

    /**
     * 初始化方法zookeeper连接池
     * 
     * @param config
     */
    private void init() {
        if (pool != null) {
            LOG.debug("pool is already init");
            return;
        }
        Configuration config = Configuration.getInstance();
        if (!config.isDeploy()) {
            LOG.info("Can't init , deploy = false.");
            return;
        }
        PoolableObjectFactory factory = new ZookeeperPoolableObjectFactory(config);
        // 初始化ZK对象池
        int maxIdle = config.getMaxIdle();
        int initIdleCapacity = config.getInitIdleCapacity();
        pool = new StackObjectPool(factory, maxIdle, initIdleCapacity);
        // 初始化池
        for (int i = 0; i < initIdleCapacity; i++) {
            try {
                pool.addObject();
            } catch (IllegalStateException | UnsupportedOperationException ex) {
                LOG.error(ex.getMessage(), ex);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 将ZK对象从对象池中取出
     * 
     * @return
     */
    public ZooKeeper borrowObject() {
        if (pool != null) {
            try {
                return (ZooKeeper) pool.borrowObject();
            } catch (NoSuchElementException | IllegalStateException ex) {
                LOG.error(ex.getMessage(), ex);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 将ZK实例返回对象池
     * 
     * @param zk
     */
    public void returnObject(ZooKeeper zk) {
        if (pool != null && zk != null) {
            try {
                pool.returnObject(zk);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 关闭对象池
     */
    public void close() {
        if (pool != null) {
            try {
                pool.close();
                LOG.info("关闭ZK对象池完成");
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }
}
