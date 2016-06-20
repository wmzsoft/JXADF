package com.jxtech.distributed;

import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.util.ClassUtil;

/**
 * 分布式 工厂类
 * 
 * @author wmzsoft@gmail.com
 * @date 2016
 *
 */
public class DistributedFactory {

    /**
     * 获得分布式接口
     * 
     * @return
     */
    public static Distributed getDistributed() {
        // 得到实现类
        String cn = JxVarsFactory.getInstance().getValue("distributed.impl.class", "com.jxtech.distributed.zookeeper.DistributedZKImpl");
        Object obj = ClassUtil.getInstance(cn);
        if (obj instanceof Distributed) {
            return (Distributed) obj;
        }
        return null;
    }
}
