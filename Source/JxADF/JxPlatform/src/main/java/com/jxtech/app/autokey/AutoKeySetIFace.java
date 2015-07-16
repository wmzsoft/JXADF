package com.jxtech.app.autokey;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */

public interface AutoKeySetIFace extends JboSetIFace {

    /**
     * 生成自动增长编号
     * 
     * @param keyname
     * @param orgid
     * @param siteid
     * @return
     * @throws JxException
     */
    public String generateKeyValue(String keyname, String orgid, String siteid) throws JxException;
}
