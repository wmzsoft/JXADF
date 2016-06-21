package com.jxtech.distributed.zookeeper.thread;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.util.StrUtil;

/**
 * 清理无效的Bundle
 * 
 * @author wmzsoft@gmail.com
 * @date 2016.06
 *
 */
public class CleanInvalidBundle implements Callable<Boolean> {
    private static final Logger LOG = LoggerFactory.getLogger(CleanInvalidBundle.class);

    @Override
    public Boolean call() throws Exception {
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return false;
        }
        Configuration config = Configuration.getInstance();
        String path = config.getBaseBundle();
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                return true;
            }
            List<String> list = zk.getChildren(path, false);
            if (list == null || list.isEmpty()) {
                return true;
            }
            Iterator<String> iter = list.iterator();
            long now = System.currentTimeMillis();
            long day = 24 * 60 * 60 * 1000;// 1天的耗秒数
            while (iter.hasNext()) {
                String bpath = StrUtil.contact(path, "/", iter.next());
                stat = zk.exists(bpath, false);
                if (stat != null) {
                    List<String> clist = zk.getChildren(bpath, false);
                    if ((clist == null || clist.isEmpty()) && (now - stat.getMtime() > day)) {
                        // 超过一天没有修改，则删除此节点
                        zk.delete(bpath, stat.getVersion());
                    }
                }
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
