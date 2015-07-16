package com.jxtech.tag.attachment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 附件标签
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.12
 */
@StrutsTag(name = "attachment", tldTagClass = "com.jxtech.tag.attachment.AttachmentTag", description = "attachment")
public class Attachment extends JxBaseUIBean {

    protected String datasrc;
    protected String jboname;// 为空，则为主表的附件
    protected String mystyle;// 样式，默认值为TD
    protected JboIFace jbo;// 当前记录
    protected String name;
    private int acount;// 附件记录数
    protected String display; // 显示类型，默认为icon
    protected String needed;// 是否必须
    protected String vfolder;// 虚拟文件目录（字段映射文件）
    protected String label;

    private String type;
    private String cols;
    private String imgwidth;
    private String imgheight;
    private String imgtype;
    private JboSetIFace attJboSet;
    private String operate;

    public Attachment(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("attachment", false);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("attachment", false);
    }

    @Override
    public void evaluateParams() {
        if (datasrc != null) {
            addParameter("datasrc", findString(datasrc));
        }
        if (jboname != null) {
            addParameter("jboname", findString(jboname));
        }
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle));
        }
        if (type != null) {
            addParameter("type", findString(type).toUpperCase());
        }
        if (cols != null) {
            addParameter("cols", findValue(cols, Integer.class));
        }
        if (imgwidth != null) {
            addParameter("imgwidth", findString(imgwidth));
        }
        if (imgheight != null) {
            addParameter("imgheight", findString(imgheight));
        }
        if (imgtype != null) {
            addParameter("imgtype", findString(imgtype));
        }
        if (display != null) {
            addParameter("display", findString(display));
        }
        if (readonly != null) {
            addParameter("readonly", findString(readonly));
        }
        if (needed != null) {
            addParameter("needed", findString(needed));
        }
        if (vfolder != null) {
            addParameter("vfolder", findString(vfolder));
        }
        if (operate != null) {
            addParameter("operate", findString(operate).toUpperCase());
        }
        if (name != null) {
            addParameter("name", findString(name));
        }
        if (label != null) {
            addParameter("label", findString(label));
        }
        addParameter("attJboSet", attJboSet);
        addParameter("acount", acount);
        addParameter("jbo", jbo);
        super.evaluateParams();
    }

    public void setDatasrc(String datasrc) {
        this.datasrc = datasrc;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public void setImgwidth(String imgwidth) {
        this.imgwidth = imgwidth;
    }

    public void setImgheight(String imgheight) {
        this.imgheight = imgheight;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setAttrJboSet(JboSetIFace attJboSet) {
        this.attJboSet = attJboSet;
    }

    public void setNeeded(String needed) {
        this.needed = needed;
    }

    public void setVfolder(String vfolder) {
        this.vfolder = vfolder;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }
}
