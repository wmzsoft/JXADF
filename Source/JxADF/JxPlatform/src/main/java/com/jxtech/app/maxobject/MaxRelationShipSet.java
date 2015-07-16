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
public class MaxRelationShipSet extends JboSet {

    /**
     * 
     */
    private static final long serialVersionUID = 3803723646984824957L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxRelationShip(this);
        return currentJbo;
    }

}
