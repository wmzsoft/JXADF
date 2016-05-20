package com.jxtech.app.pubdepartment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/22
 */
public class PubDepartmentSet extends JboSet {
    private static final long serialVersionUID = 6399268545462601488L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new PubDepartment(this);
        return currentJbo;
    }

    @Override
    public boolean canCache() throws JxException {
        return true;
    }

}
