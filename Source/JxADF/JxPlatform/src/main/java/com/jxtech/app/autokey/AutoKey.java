package com.jxtech.app.autokey;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */
public class AutoKey extends Jbo implements AutoKeyIFace {

    /**
     * 
     */
    private static final long serialVersionUID = -8946757743216221745L;

    public AutoKey(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

}
