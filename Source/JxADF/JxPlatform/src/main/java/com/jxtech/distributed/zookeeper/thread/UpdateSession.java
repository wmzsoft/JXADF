package com.jxtech.distributed.zookeeper.thread;

import java.util.concurrent.Callable;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.DistributedSession;
import com.jxtech.distributed.zookeeper.common.ZooKeeperUtil;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.util.StrUtil;

/**
 * 更新Session的最后访问时间
 * 
 * @author Administrator
 *
 */
public class UpdateSession implements Callable<DistributedSession> {
    private static Logger LOG = LoggerFactory.getLogger(UpdateSession.class);
    private String sessionid;
    private boolean isCreate;
    private String userid;

    public UpdateSession(String sessionid, String userid, boolean isCreate) {
        this.sessionid = sessionid;
        this.isCreate = isCreate;
        this.userid = userid;
    }

    @Override
    public DistributedSession call() throws Exception {
        if (StrUtil.isNull(sessionid)) {
            return null;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        Configuration config = Configuration.getInstance();
        String path = StrUtil.contact(config.getBaseSession(), Configuration.NODE_SEP, sessionid);
        try {
            Stat stat = zk.exists(path, false);
            if (stat != null) {
                byte[] data = zk.getData(path, false, stat);
                Object obj = SerializationUtils.deserialize(data);
                if (obj instanceof DistributedSession) {
                    DistributedSession ds = (DistributedSession) obj;
                    long timeout = config.getSessionTimeOut();
                    if (!isCreate && timeout > 0 && System.currentTimeMillis() - ds.getLastAccessTime() > timeout) {
                        // 过期了
                        ZooKeeperUtil.delete(zk, path);
                    } else {
                        // 更新当前信息
                        ds.setLastAccessTime(System.currentTimeMillis());
                        zk.setData(path, SerializationUtils.serialize(ds), stat.getVersion());
                    }
                    return ds;
                }
            } else if (!StrUtil.isNull(userid) && isCreate) {
                DistributedSession dsession = new DistributedSession(userid);
                byte[] b = SerializationUtils.serialize(dsession);
                zk.create(path, b, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                return dsession;
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return null;
    }

}
