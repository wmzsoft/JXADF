package com.jxtech.app.attachment;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

import java.io.OutputStream;

/**
 * 平台基础表信息-附件内容-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public interface AttachmentContentSetIFace extends JboSetIFace {
    public void getBlob(String uid, OutputStream os) throws JxException;

}
