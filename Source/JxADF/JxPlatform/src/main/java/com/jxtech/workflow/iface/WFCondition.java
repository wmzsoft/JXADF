package com.jxtech.workflow.iface;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 工作流中自定义条件类
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.10
 * 
 */
public interface WFCondition {

    /**
     * 是否为真
     * @param form 表单信息
     * @param node 条件节点的信息(WFCONDITION)Jbo
     * @return
     * @throws JxException
     */
    public boolean isOk(JboIFace form, JboIFace node) throws JxException;
}
