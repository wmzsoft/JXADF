package com.jxtech.tag.attachment;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 附件标签
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.12
 * 
 */
public class AttachmentTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentTag.class);

    private static final long serialVersionUID = -3672746179845621700L;
    protected String datasrc;// =TOP_ATTACHMEN.JOB_TYPE_CODE,为空则为主表名
    protected String jboname;// 为空，则为主表的附件
    protected String mystyle;// 样式，默认值为TD
    protected JboIFace jbo;// 当前记录
    protected String display; // 显示模式
    protected String needed; // 是否需要上传附件
    protected String vfolder; // == TOP_ATTACHMENT.DATA_FROM 为空为主表，否则为主表中某个字段或者某个类型的附件

    private String type;
    private String cols;
    private String imgwidth;
    private String imgheight;
    private String imgtype;
    private String operate;
    private String label;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Attachment(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        if (!super.isRenderer("attachment")) {
            return;
        }
        Attachment att = (Attachment) component;
        att.setDatasrc(datasrc);
        att.setMystyle(mystyle);
        // 附件
        JboSetIFace attJboSet = null;
        try {
            attJboSet = JboUtil.getJboSet(jboname);
        } catch (JxException e1) {
            LOG.error(e1.getMessage(), e1);
        }
        attJboSet.setRequired("true".equalsIgnoreCase(needed) ? true : false);
        att.setAttrJboSet(attJboSet);

        // 附件依赖于其他应用，主app为当前应用的app，不能从attachment来获取
        /*
         * Tag tag = findAncestorWithClass(getParent(), BodyTag.class); App myapp = null; if (tag != null) { BodyTag body = (BodyTag) tag; String appName = body.getAppName(); myapp = JxSession.getApp(appName, body.getAppType()); } else { myapp = JxSession.getApp(); }
         */

        App myapp = null;
        try {
            myapp = JxSession.getApp();
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        if (myapp != null) {
            try {
                jbo = myapp.getJbo();
                if (jbo != null) {
                    att.setAcount(jbo.getAttachmentCount(vfolder));
                }
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
            att.setJbo(jbo);

            jbo.addAttachment(vfolder, attJboSet);
            readonly = attJboSet.isReadonly() ? "true" : "false";
            needed = attJboSet.isRequired() ? "true" : "false";
        }
        att.setType(type);
        att.setImgheight(imgheight);
        att.setImgwidth(imgwidth);
        att.setImgtype(imgtype);
        att.setCols(cols);
        att.setDisplay(display);
        att.setReadonly(readonly);
        att.setNeeded(needed);
        att.setVfolder(vfolder);
        att.setOperate(operate);
        att.setLabel(label);
    }

    public String getDatasrc() {
        return datasrc;
    }

    public void setDatasrc(String datasrc) {
        this.datasrc = datasrc;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getImgwidth() {
        return imgwidth;
    }

    public void setImgwidth(String imgwidth) {
        this.imgwidth = imgwidth;
    }

    public String getImgheight() {
        return imgheight;
    }

    public void setImgheight(String imgheight) {
        this.imgheight = imgheight;
    }

    public String getImgtype() {
        return imgtype;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getNeed() {
        return this.needed;
    }

    public void setNeeded(String needed) {
        this.needed = needed;
    }

    public String getVfolder() {
        return this.vfolder;
    }

    public void setVfolder(String vfolder) {
        this.vfolder = vfolder;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

}
