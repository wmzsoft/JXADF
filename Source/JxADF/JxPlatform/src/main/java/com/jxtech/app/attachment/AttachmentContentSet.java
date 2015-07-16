package com.jxtech.app.attachment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.JxException;

import java.io.OutputStream;

/**
 * 平台基础表信息-附件内容-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public class AttachmentContentSet extends JboSet implements AttachmentContentSetIFace {

    /**
     * 
     */
    private static final long serialVersionUID = 9047659472604946260L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        this.currentJbo = new AttachmentContent(this);
        return this.currentJbo;
    }

    @Override
    public void getBlob(String uid, OutputStream os) throws JxException {
        super.getBlob("attachment_content", uid, os);
    }

}
