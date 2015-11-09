package com.jxtech.app.maxapps;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class Maxapps extends Jbo {
    private static final long serialVersionUID = 7579847321003732420L;

    public Maxapps(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        if (!isToBeAdd()) {
            setReadonly("APP", true);
        }
        super.afterLoad();
    }

    @Override
    public String[] getDeleteChildren() throws JxException {
        return new String[] { "MAXAPPSWFINFOAPPP", "SECURITYRESTRICTAPPP", "MAXAPPSMENU" };
    }

}
