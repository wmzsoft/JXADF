package com.jxtech.app.maxapps;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxappsMDataSet extends JboSet {

    private static final long serialVersionUID = -1887155083419525878L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxappsMData(this);
        return currentJbo;
    }

}
