package com.jxtech.app.attachment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

import java.io.InputStream;
import java.util.List;

/**
 * 平台基础表信息-附件基本信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public interface AttachmentSetIFace extends JboSetIFace {

    public long addAttachment(String code, String ownerid, String filename, String filetype, InputStream inputs, JxUserInfo userinfo, String vfloder) throws JxException;

    public long addAttachment(String code, String ownerid, String filename, String filetype, long filesize, String md5, String filefullname, JxUserInfo userinfo, String vfolder) throws JxException;

    public JboIFace findAttachment(String attachmentJbo, String object_id) throws JxException;

    public List<JboIFace> findAttachments(String attachmentJbo, String object_id) throws JxException;
}
