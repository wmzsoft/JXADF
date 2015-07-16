package com.jxtech.app.attachment;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 平台基础表信息-附件基本信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */

public class Attachment extends Jbo {

    private static final long serialVersionUID = 3048746715468928281L;

    public Attachment(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public String[] getDeleteChildren() throws JxException {
        return new String[] { "TOP_ATTACHMENT_OBJECT_RELATION", "TOP_ATTACHMENT_CONTENT" };
    }

}
