package com.jxtech.app.securityrestrict;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/27
 */
public class SecurityrestrictSet extends JboSet {
    /**
     * 
     */
    private static final long serialVersionUID = -5591453005301266894L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Securityrestrict(this);
        return currentJbo;
    }
}
