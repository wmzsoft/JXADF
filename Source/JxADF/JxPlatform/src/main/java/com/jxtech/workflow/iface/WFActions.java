package com.jxtech.workflow.iface;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 工作流中自定义操作类
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.10
 * 
 */
public interface WFActions {
    /**
     * 执行操作
     * 
     * @param form jbo的表单信息
     * @throws JxException
     */
    public void execute(JboIFace form) throws JxException;
}
