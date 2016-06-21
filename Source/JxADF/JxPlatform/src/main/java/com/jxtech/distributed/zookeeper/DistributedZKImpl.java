package com.jxtech.distributed.zookeeper;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.jxtech.distributed.BundleHelper;
import com.jxtech.distributed.Configuration;
import com.jxtech.distributed.DistributedImpl;
import com.jxtech.distributed.DistributedSession;
import com.jxtech.distributed.zookeeper.common.ZooKeeperUtil;
import com.jxtech.distributed.zookeeper.pool.ZookeeperPoolManager;
import com.jxtech.distributed.zookeeper.thread.CleanInvalidBundle;
import com.jxtech.distributed.zookeeper.thread.DeleteSession;
import com.jxtech.distributed.zookeeper.thread.RegisterBundle;
import com.jxtech.distributed.zookeeper.thread.UnRegisterBundle;
import com.jxtech.distributed.zookeeper.thread.UpdateSession;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;

/**
 * 分布式 zookeeper实现
 * 
 * @author wmzsoft@gmail.com
 * @date 2016
 *
 */
public class DistributedZKImpl extends DistributedImpl {
    private static Logger LOG = LoggerFactory.getLogger(DistributedZKImpl.class);
    private static ExecutorService executor;

    /**
     * 注册 Data中的内容为JSON格式，[{"app":"autokey","url":"/autokey/index.action"}]
     */
    @Override
    public boolean register(Bundle bundle, boolean wait) throws JxException {
        if (executor == null) {
            return false;
        }
        // 放入线程中执行
        Future<Boolean> future = executor.submit(new RegisterBundle(bundle));
        if (wait) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean unRegister(Bundle bundle, boolean wait) throws JxException {
        if (executor == null) {
            return false;
        }
        // 放入线程中执行
        Future<Boolean> future = executor.submit(new UnRegisterBundle(bundle));
        if (wait) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error(e.getMessage());
            }
        }
        return false;
    }

    /**
     * 获得Bundle的URL地址
     * 
     * @param bundle
     * @param server
     * @return
     * @throws JxException
     */
    public String getUrl(Bundle bundle) throws JxException {
        if (bundle == null) {
            return null;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        String path = BundleHelper.getPath(bundle);
        Stat stat;
        try {
            stat = zk.exists(path, false);
            if (stat == null) {
                LOG.debug(path + " not exists");
            } else {
                byte[] b = zk.getData(path, false, stat);
                String s = new String(b);
                if (s.indexOf(',') < 0) {
                    return s.trim();
                } else {
                    String[] ss = s.split(",");
                    // 多个的情况下，随机选一个吧
                    Random random = new Random();
                    int idx = random.nextInt(ss.length);
                    if (ss[idx].trim().length() > 5) {
                        return ss[idx].trim();
                    }
                    // 如果获得的随机URL不正确，就按顺序找吧
                    for (int i = 0; i < ss.length; i++) {
                        if (ss[i].trim().length() > 5) {
                            return ss[i].trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            pool.returnObject(zk);
        }
        return null;
    }

    /**
     * 获得所有的插件分布情况
     * 
     * @param server
     * @return
     * @throws JxException
     */
    public List<String> getAllBundle() throws JxException {
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        try {
            return zk.getChildren(Configuration.getInstance().getBaseBundle(), true);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return null;
    }

    /**
     * 加载所有的应用以及URL信息
     * 
     * @param list
     * @param server
     * @return {autokey=[{"APP"="autokey","URL"="/autokey/index.action","HOME"="http://127.0.0.1/jxweb"}]}
     * @throws JxException
     */
    public Map<String, List<Map<String, Object>>> getApps() throws JxException {
        List<String> list = getAllBundle();
        if (list == null || list.isEmpty()) {
            return null;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        Iterator<String> iter = list.iterator();
        Map<String, List<Map<String, Object>>> apps = new HashMap<String, List<Map<String, Object>>>();
        String baseb = Configuration.getInstance().getBaseBundle();
        while (iter.hasNext()) {
            String it = iter.next(); // ex : com.jxtech.autokey.1.0.0
            String path = StrUtil.contact(baseb, "/", it); // ex : /jxweb/com.jxtech.autokey.1.0.0
            try {
                Stat stat = zk.exists(path, false);
                if (stat != null) {
                    // 获得子节点
                    List<String> children = zk.getChildren(path, false);// 获得插件的服务器地址列表
                    if (children == null || children.isEmpty()) {
                        continue;
                    }
                    int ccount = children.size();
                    Random random = new Random();
                    // cpath=/jxweb/com.jxtech.autokey.1.0.0/http%3A%2F%2F127.0.0.1%2Fjxweb
                    String home = children.get(random.nextInt(ccount));// 这里只随机取一个即可
                    String cpath = StrUtil.contact(path, "/", home);
                    byte[] b = zk.getData(cpath, true, stat);
                    String json = new String(b);// [{"APP":"autokey","URL":"/autokey/index.action"}]
                    List<Map<String, Object>> applist = JsonUtil.fromJson(json);
                    if (applist != null && !applist.isEmpty()) {
                        String app = String.valueOf(applist.get(0).get("APP"));
                        if (!StrUtil.isNull(app)) {
                            home = URLDecoder.decode(home, "UTF-8");
                            Iterator<Map<String, Object>> aiter = applist.iterator();
                            while (aiter.hasNext()) {
                                Map<String, Object> ait = aiter.next();
                                ait.put("HOME", home);
                            }
                            apps.put(app.toUpperCase(), applist);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        pool.returnObject(zk);
        return apps;
    }

    /**
     * 根据当前URL，得到分布式插件的URL
     * 
     * @param url
     * @return
     * @throws JxException
     */
    public String getDistributedUrl(String url) throws JxException {
        if (StrUtil.isNull(url)) {
            return null;
        }
        if (url.startsWith("app.action")) {
            return url;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return url;
        }
        String context = Configuration.getInstance().getBaseBundle();
        Stat stat;
        try {
            stat = zk.exists(context, false);
            if (stat == null) {
                return url;
            }
            List<String> list = zk.getChildren(context, false);
            if (list == null || list.isEmpty()) {
                return url;
            }
            Iterator<String> iter = list.iterator();
            while (iter.hasNext()) {
                String val = iter.next();// com.jxtech.autokey.1.0.0
                /// jxweb/com.jxtech.autokey.1.0.0
                String path = StrUtil.contact(context, "/", val);
                stat = zk.exists(path, false);
                if (stat != null) {
                    // 判断是否是此插件
                    byte[] b = zk.getData(path, false, stat);
                    String bs = new String(b);
                    List<Map<String, Object>> apps = JsonUtil.fromJson(bs);
                    if (apps == null || apps.isEmpty()) {
                        continue;
                    }
                    Iterator<Map<String, Object>> appiter = apps.iterator();
                    while (appiter.hasNext()) {
                        Map<String, Object> map = appiter.next();
                        String surl = String.valueOf(map.get("URL"));
                        if (!StrUtil.isNull(surl) && url.indexOf(surl) >= 0) {
                            // 找到了
                            List<String> applist = zk.getChildren(path, false);
                            if (applist != null && !applist.isEmpty()) {
                                // 随机返回一个地址吧
                                Iterator<String> iters = applist.iterator();
                                while (iters.hasNext()) {
                                    String home = URLDecoder.decode(iters.next(), "UTF-8");
                                    if (url.indexOf(home) < 0) {
                                        int pos = url.indexOf(surl);
                                        String hurl = url.substring(pos);
                                        // 得到分布式地址
                                        return StrUtil.contact(home, hurl);
                                    }
                                }
                                return url;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }

        return url;
    }

    /**
     * 将信息保存在分布式服务器中
     * 
     * @param userinfo
     * @param dsessionid
     * @return
     */
    public boolean saveJxUserInfo(JxUserInfo userinfo, String dsessionid) {
        if (userinfo == null) {
            return false;
        }
        if (StrUtil.isNull(dsessionid)) {
            return false;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return false;
        }
        Configuration config = Configuration.getInstance();
        String path = StrUtil.contact(config.getBaseSession(), Configuration.NODE_SEP, dsessionid);
        try {
            // SessionID的路径
            Stat stat = zk.exists(path, false);
            byte[] sdata = SerializationUtils.serialize(new DistributedSession(userinfo.getUserid()));
            if (stat == null) {
                // 创建路径
                zk.create(path, sdata, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                // 修改信息
                zk.setData(path, sdata, stat.getVersion());
            }
            // Session 中的用户信息路径
            String spath = StrUtil.contact(path, Configuration.NODE_SEP, JxSession.USER_INFO);
            stat = zk.exists(spath, false);
            byte[] udata = SerializationUtils.serialize(userinfo);
            if (stat == null) {
                zk.create(spath, udata, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            } else {
                zk.setData(spath, udata, stat.getVersion());
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return false;
    }

    /**
     * 更新Session的访问时间
     * 
     * @param sessionid
     * @param userid
     * @param isCreate
     * @param wait
     *            是否等带结果
     * @return
     */
    public DistributedSession updateSessionTime(String sessionid, String userid, boolean isCreate, boolean wait) {
        if (executor == null) {
            return null;
        }
        // 放入线程中执行
        Future<DistributedSession> future = executor.submit(new UpdateSession(sessionid, userid, isCreate));
        if (wait) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 从分布式服务器中删除用户信息
     * 
     * @param dsessionid
     * @param wait
     *            是否等待执行结果
     * @return
     */
    public boolean delJxUserInfo(String dsessionid, boolean wait) {
        if (executor == null) {
            return false;
        }
        // 放入线程中执行
        Future<Boolean> future = executor.submit(new DeleteSession(dsessionid));
        if (wait) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error(e.getMessage());
            }
        }
        return false;
    }

    /**
     * 获得用户ID
     * 
     * @param dsessionid
     * @return
     */
    public String getJxUserId(String dsessionid) {
        if (StrUtil.isNull(dsessionid)) {
            return null;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        Configuration config = Configuration.getInstance();
        String path = StrUtil.contact(config.getBaseSession(), Configuration.NODE_SEP, dsessionid);
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                return null;
            }
            byte[] ds = zk.getData(path, false, stat);
            Object obj = SerializationUtils.deserialize(ds);
            if (obj instanceof DistributedSession) {
                // 检查是否过期
                DistributedSession dsession = (DistributedSession) obj;
                long timeout = config.getSessionTimeOut();
                if (timeout > 0 && System.currentTimeMillis() - dsession.getLastAccessTime() > timeout) {
                    LOG.info(dsession.getUserid() + " expired");
                    ZooKeeperUtil.delete(zk, path);
                    return null;
                } else {
                    return dsession.getUserid();
                }
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage());
        } finally {
            pool.returnObject(zk);
        }
        return null;
    }

    /**
     * 获得用户信息
     * 
     * @param dsessionid
     * @return
     */
    public JxUserInfo getJxUserInfo(String dsessionid) {
        if (StrUtil.isNull(dsessionid)) {
            return null;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return null;
        }
        Configuration config = Configuration.getInstance();
        String path = StrUtil.contact(config.getBaseSession(), Configuration.NODE_SEP, dsessionid);
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                return null;
            }
            byte[] ds = zk.getData(path, false, stat);
            Object obj = SerializationUtils.deserialize(ds);
            if (obj instanceof DistributedSession) {
                // 检查是否过期
                DistributedSession dsession = (DistributedSession) obj;
                long timeout = config.getSessionTimeOut();
                if (timeout > 0 && System.currentTimeMillis() - dsession.getLastAccessTime() > timeout) {
                    LOG.info(dsession.getUserid() + " expired");
                    ZooKeeperUtil.delete(zk, path);
                    return null;
                }
                String spath = StrUtil.contact(path, Configuration.NODE_SEP, JxSession.USER_INFO);
                stat = zk.exists(spath, false);
                if (stat != null) {
                    byte[] data = zk.getData(spath, false, stat);
                    Object val = SerializationUtils.deserialize(data);
                    if (val instanceof JxUserInfo) {
                        return (JxUserInfo) val;
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage());
        } finally {
            pool.returnObject(zk);
        }
        return null;
    }

    /**
     * 移出过期的Session
     */
    public void removeTimeoutSession() {
        Configuration config = Configuration.getInstance();
        long timeout = config.getSessionTimeOut();
        if (timeout <= 0) {
            return;
        }
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            return;
        }
        String path = config.getBaseSession();
        try {
            Stat stat = zk.exists(path, false);
            if (stat != null) {
                List<String> list = zk.getChildren(path, false);
                if (list != null && !list.isEmpty()) {
                    Iterator<String> iter = list.iterator();
                    while (iter.hasNext()) {
                        String it = StrUtil.contact(path, Configuration.NODE_SEP, iter.next());
                        stat = zk.exists(it, false);
                        if (stat != null) {
                            byte[] b = zk.getData(it, false, stat);
                            Object obj = SerializationUtils.deserialize(b);
                            if (obj instanceof DistributedSession) {
                                DistributedSession ds = (DistributedSession) obj;
                                if (System.currentTimeMillis() - ds.getLastAccessTime() > timeout) {
                                    // 超时了，删除吧
                                    ZooKeeperUtil.delete(zk, it);
                                }
                            } else {
                                ZooKeeperUtil.delete(zk, it);
                            }
                        }
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error(e.getMessage());
        } finally {
            pool.returnObject(zk);
        }
    }

    @Override
    public boolean init() throws JxException {
        // 初始化连接池
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        Configuration config = Configuration.getInstance();
        ZooKeeper zk = pool.borrowObject();
        if (zk == null) {
            if (config.isDeploy()) {
                LOG.info("init failed failed.");
            }
            return false;
        }
        try {
            // 创建为bundle的根节点
            String bundleb = config.getBaseBundle();
            byte[] b = "http://osgia.com".getBytes();
            if (zk.exists(bundleb, false) == null) {
                zk.create(bundleb, b, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // 创建为session的根节点
            String bundles = config.getBaseSession();
            if (zk.exists(bundles, false) == null) {
                zk.create(bundles, b, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            executor = Executors.newFixedThreadPool(config.getSessionPoolSize());
            // 删除无效的数据
            Future<Boolean> future = executor.submit(new CleanInvalidBundle());
            future.get();// 等待执行完毕。
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            pool.returnObject(zk);
        }
        return true;
    }

    @Override
    public void destory() throws JxException {
        // 结束线程池
        if (executor != null) {
            try {
                executor.shutdown();
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                        LOG.error("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                LOG.error(ie.getMessage(), ie);
                // (Re-)Cancel if current thread also interrupted
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
        // 结束zookeeper池
        ZookeeperPoolManager pool = ZookeeperPoolManager.getInstance();
        pool.close();
    }

}
