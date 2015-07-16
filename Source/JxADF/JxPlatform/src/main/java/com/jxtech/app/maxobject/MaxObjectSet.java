package com.jxtech.app.maxobject;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxObjectSet extends JboSet {

    private static final long serialVersionUID = -4260395941096305913L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxObject(this);
        return currentJbo;
    }

}
