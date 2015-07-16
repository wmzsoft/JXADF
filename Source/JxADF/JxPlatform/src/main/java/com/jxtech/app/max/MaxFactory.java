package com.jxtech.app.max;

import com.jxtech.app.pubuser.PubUserSetIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;

/**
 * 平台基础表信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 * 
 */
public class MaxFactory {

    public static MaxSequenceSetIFace getMaxSequenceSetIFace() throws JxException {
        return (MaxSequenceSetIFace) JboUtil.getJboSet("MAXSEQUENCE");
    }

    public static PubUserSetIFace getPubUserSetIface() throws JxException {
        return (PubUserSetIFace) JboUtil.getJboSet("PUB_USER");
    }
}
