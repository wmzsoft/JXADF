package com.jxtech.distributed;

/**
 * 分布式Session的基本信息
 * 
 * @author Administrator
 *
 */
public class DistributedSession implements java.io.Serializable {
    private static final long serialVersionUID = 1451992384346533701L;
    private String userid;
    private long createTime;// 创建时间
    private long lastAccessTime;// 上次访问时间

    public DistributedSession() {
        createTime = System.currentTimeMillis();
        lastAccessTime = this.createTime;
    }

    public DistributedSession(String userid) {
        createTime = System.currentTimeMillis();
        lastAccessTime = this.createTime;
        this.userid = userid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
