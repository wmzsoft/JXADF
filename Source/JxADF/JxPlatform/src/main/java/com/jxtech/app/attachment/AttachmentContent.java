package com.jxtech.app.attachment;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 平台基础表信息-附件内容-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */

public class AttachmentContent extends Jbo implements AttachmentContentIFace {

    private static final long serialVersionUID = 6354804880552868138L;

    public AttachmentContent(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

}
