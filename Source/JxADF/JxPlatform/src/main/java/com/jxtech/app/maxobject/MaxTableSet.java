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
public class MaxTableSet extends JboSet {

    /**
     * 
     */
    private static final long serialVersionUID = 3978864131798677947L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxTable(this);
        return currentJbo;
    }

}
