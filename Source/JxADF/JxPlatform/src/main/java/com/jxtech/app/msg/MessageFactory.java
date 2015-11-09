package com.jxtech.app.msg;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息工厂类
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class MessageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(MessageFactory.class);
    // 实现类<名称，类名>
    private static Map<String, String> impls = new HashMap<String, String>();

    /**
     * 发送消息
     * 
     * @param body 消息体
     * @return
     * @throws JxException
     */
    public static boolean sendMessage(MessageBody body) throws JxException {
        if (body == null) {
            return false;
        }
        if (impls.size() < 1) {
            LOG.warn("你未安装消息插件。");
            LOG.info(body.toString());
            return false;
        }
        boolean rst = true;
        for (Map.Entry<String, String> entry : impls.entrySet()) {
            Object obj = ClassUtil.getInstance(entry.getValue());
            if (obj != null && obj instanceof Message) {
                rst = rst & ((Message) obj).sendMessage(body);
            }
        }
        return rst;
    }

    public static void putMessageImpl(String key, String value) {
        if (StrUtil.isNull(value)) {
            impls.remove(key);
        } else {
            impls.put(key, value);
        }
    }
}
