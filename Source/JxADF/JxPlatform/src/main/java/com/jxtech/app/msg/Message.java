package com.jxtech.app.msg;

import com.jxtech.jbo.util.JxException;

/**
 * 平台的消息处理机制
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public interface Message {
    /**
     * 发送消息 
     * @param body 消息内容
     * @return
     * @throws JxException
     */
    public boolean sendMessage(MessageBody body) throws JxException;
}
