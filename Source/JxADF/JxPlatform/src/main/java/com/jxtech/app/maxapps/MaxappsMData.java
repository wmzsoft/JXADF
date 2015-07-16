package com.jxtech.app.maxapps;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.04
 * 
 */
public class MaxappsMData extends Jbo {

    private static final long serialVersionUID = -8035648646276155969L;

    public MaxappsMData(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        super.setDefaultValue();
        JboIFace parent = getParent();
        if (parent != null) {
            if ("MAXAPPS".equalsIgnoreCase(parent.getJboName())) {
                this.setString("APP", parent.getString("APP"));
            }
        }
        return true;
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!isToBeAdd()) {
            this.setReadonly("key", true);
        }
    }

}
