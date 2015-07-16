package com.jxtech.app.msg;

import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 这个是消息的简单实现类
 * @author wmzsoft@gmail.com
 * @date 2014.12
 *
 */
public class MessageImpl implements Message {
    private static final Logger LOG = LoggerFactory.getLogger(MessageImpl.class);

    @Override
    public boolean sendMessage(MessageBody body) throws JxException {
        LOG.info(body.toString());
        return true;
    }

}
