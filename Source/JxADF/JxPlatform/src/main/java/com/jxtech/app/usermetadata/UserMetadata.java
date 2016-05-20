package com.jxtech.app.usermetadata;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 用户个性化表
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class UserMetadata extends Jbo {
    private static final long serialVersionUID = -5214014891519388555L;

    public UserMetadata(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean canCache() throws JxException {
        return !isToBeAdd();
    }
}
