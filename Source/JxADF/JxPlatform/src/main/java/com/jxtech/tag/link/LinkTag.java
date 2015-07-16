package com.jxtech.tag.link;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.form.FormTag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class LinkTag extends JxBaseUITag {
    private String urlDataAttribute;
    private String txtDataAttribute;
    private String titleDataAttribute;
    private String titleDataValue;
    private Object urlDataValue;
    private Object txtDataValue;
    private String txt;
    private String url;
    private String title;
    private JboIFace jbo;
    private String mxevent;
    private static final long serialVersionUID = -3834274862684813426L;
    private static final Logger LOG = LoggerFactory.getLogger(LinkTag.class);

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Link(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Link link = (Link) component;
        link.setUrlDataAttribute(urlDataAttribute);
        link.setTxtDataAttribute(txtDataAttribute);
        link.setTitleDataAttribute(titleDataAttribute);
        link.setTxt(txt);
        link.setUrl(url);
        link.setTitle(title);
        link.setMxevent(mxevent);
        Tag tag = findAncestorWithClass(this, FormTag.class);
        try {
            if (tag != null) {
                FormTag ft = ((FormTag) tag);
                jbo = ft.getJbo();
                if (jbo != null) {
                    link.setJbo(jbo);
                    if (!StrUtil.isNull(urlDataAttribute)) {
                        urlDataValue = jbo.getString(urlDataAttribute);
                        link.setUrlDataValue(urlDataValue);
                    }
                    if (!StrUtil.isNull(txtDataAttribute)) {
                        txtDataValue = jbo.getString(txtDataAttribute);
                        link.setTxtDataValue(txtDataValue);
                    }
                    if (!StrUtil.isNull(titleDataAttribute)) {
                        titleDataValue = jbo.getString(titleDataAttribute);
                        link.setTitleDataAttribute(titleDataValue);
                    }
                }
            }
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getUrlDataAttribute() {
        return urlDataAttribute;
    }

    public void setUrlDataAttribute(String urlDataAttribute) {
        this.urlDataAttribute = urlDataAttribute;
    }

    public String getTxtDataAttribute() {
        return txtDataAttribute;
    }

    public void setTxtDataAttribute(String txtDataAttribute) {
        this.txtDataAttribute = txtDataAttribute;
    }

    public Object getUrlDataValue() {
        return urlDataValue;
    }

    public void setUrlDataValue(Object urlDataValue) {
        this.urlDataValue = urlDataValue;
    }

    public Object getTxtDataValue() {
        return txtDataValue;
    }

    public void setTxtDataValue(Object txtDataValue) {
        this.txtDataValue = txtDataValue;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getTitleDataAttribute() {
        return titleDataAttribute;
    }

    public void setTitleDataAttribute(String titleDataAttribute) {
        this.titleDataAttribute = titleDataAttribute;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleDataValue() {
        return titleDataValue;
    }

    public void setTitleDataValue(String titleDataValue) {
        this.titleDataValue = titleDataValue;
    }

}
