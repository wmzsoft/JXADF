package com.jxtech.app.max;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 平台基础表信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 * 
 */
public interface MaxSequenceSetIFace extends JboSetIFace {

    /**
     * 产生序列号
     * 
     * @param tbName 表名
     * @param name 字段名
     * @return 返回序列值
     */
    public long generateNewSequence(String tbName, String name) throws JxException;

    public String getSequeceName(String tbName, String name) throws JxException;

    public String getSequeceName(String tbName, String name, String defaultSeq) throws JxException;
}
