package com.jxtech.app.attachment;


import com.jxtech.db.DBFactory;
import com.jxtech.db.DataEdit;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

/**
 * 平台基础表信息-附件基本信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public class AttachmentSet extends JboSet implements AttachmentSetIFace {

    private static final long serialVersionUID = 2624071627979734323L;
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentSet.class);

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Attachment(this);
        return currentJbo;
    }

    @Override
    public long addAttachment(String code, String ownerid, String filename, String filetype, InputStream inputs, JxUserInfo userinfo, String vfolder) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            StringBuilder md5b = new StringBuilder();
            // long filesize = FileUtil.getMd5OfFile(inputs, md5b);
            String attachmentid = String.valueOf(getJboInstance().getNewSequence());
            DataEdit de = DBFactory.getDataEdit(this.getDbtype(),this.getDataSourceName());
            long filesize = de.insertBlob(conn, "TOP_ATTACHMENT_CONTENT", "attachment_id", attachmentid, "attachment_content", inputs, md5b);
            if (filesize > 0) {
                return addAttachment(conn, attachmentid, code, ownerid, filename, filetype, filesize, null, null, userinfo, vfolder);
            }
        } finally {
            JxDataSourceUtil.close(conn);
        }
        return -1;
    }

    @Override
    public long addAttachment(String code, String ownerid, String filename, String filetype, long filesize, String md5, String filefullname, JxUserInfo userinfo, String vfolder) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            String attachmentid = String.valueOf(getJboInstance().getNewSequence());
            return addAttachment(conn, attachmentid, code, ownerid, filename, filetype, filesize, md5, filefullname, userinfo, vfolder);
        } finally {
            JxDataSourceUtil.close(conn);
        }
    }

    protected long addAttachment(Connection conn, String attachmentid, String code, String ownerid, String filename, String filetype, long filesize, String md5, String filefullname, JxUserInfo userinfo, String vfolder) {
        String userid = null;
        String username = null;
        if (userinfo != null) {
            userid = userinfo.getUserid();
            username = userinfo.getDisplayname();
        }
        try {
            boolean b = false;
            if (filesize > 0) {
                JboIFace am = add();
                am.setString("job_type_code", code);
                am.setString("file_name", filename);
                am.setString("file_type", filetype);
                am.setObject("file_size", filesize);
                am.setString("md5_id", md5);
                am.setString("state", "2");
                am.setString("CREATOR_ID", userid);
                am.setString("CREATOR_NAME", username);
                am.setString("UPDATE_USER_ID", userid);
                am.setString("UPDATE_USER_NAME", username);
                am.setString("attachment_id", attachmentid);
                am.setString("file_dir", filefullname);
                am.setString("data_from", vfolder);
                b = save(conn);
                if (b) {
                    JboSetIFace aors = JboUtil.getJboSet("TOP_ATTACHMENT_OBJECT_RELATION");
                    JboIFace aor = aors.add();
                    aor.setString("object_id", ownerid);
                    aor.setString("object_name", code);
                    aor.setString("attachment_id", attachmentid);
                    aor.setObject("state", 1);
                    aor.setObject("optimistic_lock_version", 1);
                    b = aor.save(conn);
                }
            }
            if (b) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return filesize;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public JboIFace findAttachment(String attachmentJbo, String object_id) throws JxException {
        JboIFace attachment = null;
        try {
            // 根据文档模版id获取附件关系表
            StringBuilder buffer = new StringBuilder();
            buffer.append(" STATE='2' and JOB_TYPE_CODE=?");
            buffer.append(" and DATA_FROM=? and ATTACHMENT_ID in( select t.ATTACHMENT_ID from TOP_ATTACHMENT_OBJECT_RELATION t");
            buffer.append(" where t.OBJECT_NAME=? and t.OBJECT_ID=? and t.STATE='1')");
            Object[] params = new Object[] { attachmentJbo, attachmentJbo, attachmentJbo, object_id };
            JboSetIFace jboJboSet = JboUtil.getJboSet("TOP_ATTACHMENT");
            DataQueryInfo dao = new DataQueryInfo();
            dao.setWhereCause(buffer.toString());
            dao.setWhereParams(params);
            jboJboSet.setQueryInfo(dao);
            List<JboIFace> attachmentJboList = jboJboSet.queryAll();
            if (!attachmentJboList.isEmpty()) {
                attachment = attachmentJboList.get(0);
            }
        } catch (Exception e) {
            attachment = null;
            LOG.error(e.getMessage());
            return attachment;
        }
        return attachment;
    }

    /*返回应用的有效的附件列表*/
    @Override
    public List<JboIFace> findAttachments(String attachmentJbo, String object_id) throws JxException {
        try {
            // 根据文档模版id获取附件关系表
            StringBuilder buffer = new StringBuilder();
            buffer.append(" STATE='2' and JOB_TYPE_CODE=?");
            buffer.append(" and DATA_FROM=? and ATTACHMENT_ID in( select t.ATTACHMENT_ID from TOP_ATTACHMENT_OBJECT_RELATION t");
            buffer.append(" where t.OBJECT_NAME=? and t.OBJECT_ID=? and t.STATE='1')");
            Object[] params = new Object[] { attachmentJbo, attachmentJbo, attachmentJbo, object_id };
            JboSetIFace jboJboSet = JboUtil.getJboSet("TOP_ATTACHMENT");
            DataQueryInfo dao = new DataQueryInfo();
            dao.setWhereCause(buffer.toString());
            dao.setWhereParams(params);
            jboJboSet.setQueryInfo(dao);
            List<JboIFace> attachmentJboList = jboJboSet.queryAll();
            if (!attachmentJboList.isEmpty()) {
                return attachmentJboList;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
