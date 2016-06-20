package com.jxtech.distributed.zookeeper.thread;

import java.util.concurrent.Callable;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.BundleHelper;
import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.util.StrUtil;

/**
 * 注册插件
 * 
 * @author wmzsoft@gmail.com
 *
 */
public class RegisterBundle implements Callable<Boolean> {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterBundle.class);
    private Bundle bundle;

    public RegisterBundle(Bundle bundle) {
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
        Configuration config = Configuration.getInstance();
        String path = BundleHelper.getPath(bundle);
        try {
            Stat stat = zk.exists(path, false);
            String url = BundleHelper.getData(bundle);
            if (stat == null) {
                zk.create(path, url.getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            path = StrUtil.contact(path, "/", config.getHomepage());
            stat = zk.exists(path, false);
            if (stat == null) {
                // 创建临时的homepage路径
                zk.create(path, url.getBytes("UTF-8"), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            } else {
                zk.setData(path, url.getBytes("UTF-8"), stat.getVersion());
            }
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return false;
    }

}
