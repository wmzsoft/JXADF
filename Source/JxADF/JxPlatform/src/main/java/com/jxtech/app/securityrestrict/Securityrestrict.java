package com.jxtech.app.securityrestrict;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/27
 */
public class Securityrestrict extends Jbo {
    /**
     * 
     */
    private static final long serialVersionUID = 3229451811889860966L;

    public Securityrestrict(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        if (!super.setDefaultValue()) {
            return false;
        }
        JboIFace parent = getParent();
        if (parent != null) {
            if ("PUB_ROLE".equalsIgnoreCase(parent.getJboName())) {
                setObject("GROUPNAME", parent.getObject("ROLE_ID"));
            }
        }
        return true;
    }

}
