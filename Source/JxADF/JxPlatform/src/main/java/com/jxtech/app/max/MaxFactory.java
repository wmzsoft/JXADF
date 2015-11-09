package com.jxtech.app.max;

import com.jxtech.app.pubuser.PubUserSetIFace;
import com.jxtech.jbo.JboSetIFace;
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
        JboSetIFace jboset = JboUtil.getJboSet("MAXSEQUENCE");
        if (jboset instanceof MaxSequenceSetIFace) {
            return (MaxSequenceSetIFace) jboset;
        }
        return null;
    }

    public static PubUserSetIFace getPubUserSetIface() throws JxException {
        JboSetIFace jboset = JboUtil.getJboSet("PUB_USER");
        if (jboset instanceof PubUserSetIFace) {
            return (PubUserSetIFace) jboset;
        }
        return null;
    }
}
