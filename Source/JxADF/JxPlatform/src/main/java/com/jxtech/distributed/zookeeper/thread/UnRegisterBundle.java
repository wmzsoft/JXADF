package com.jxtech.distributed.zookeeper.thread;

import java.util.concurrent.Callable;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.BundleHelper;
import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.util.StrUtil;

/**
 * 取消插件注册
 * 
 * @author wmzsoft@gmail.com
 *
 */
public class UnRegisterBundle implements Callable<Boolean> {
    private static final Logger LOG = LoggerFactory.getLogger(UnRegisterBundle.class);
    private Bundle bundle;

    public UnRegisterBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public Boolean call() throws Exception {
        if (bundle == null) {
            return false;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return false;
        }
        String path = BundleHelper.getPath(bundle);
        path = StrUtil.contact(path, "/", Configuration.getInstance().getHomepage());
        try {
            Stat stat = zk.exists(path, false);
            if (stat != null) {
                zk.delete(path, stat.getVersion());// 删除节点
            }
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            pool.returnObject(zk);
        }
        return false;
    }

}
