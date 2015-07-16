package com.jxtech.tag.link;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
@StrutsTag(name = "Link", tldTagClass = "com.jxtech.tag.appbar.LinkTag", description = "Link")
public class Link extends JxBaseUIBean {
    private String urlDataAttribute;
    private String txtDataAttribute;
    private Object urlDataValue;
    private Object txtDataValue;
    private String txt;
    private String url;
    private String title;
    private String titleDataAttribute;
    private String titleDataValue;
    private String mxevent;
    private JboIFace jbo;

    public Link(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "link/" + getRenderer() + "link-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "link/" + getRenderer() + "link";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (urlDataAttribute != null) {
            addParameter("urlDataAttribute", findString(urlDataAttribute).toUpperCase());
        }
        if (txtDataAttribute != null) {
            addParameter("txtDataAttribute", findString(txtDataAttribute).toUpperCase());
        }
        if (titleDataAttribute != null) {
            addParameter("titleDataAttribute", findString(titleDataAttribute).toUpperCase());
        }
        if (jbo != null) {
            addParameter("jbo", jbo);
        }
        addParameter("urlDataValue", urlDataValue);
        addParameter("txtDataValue", txtDataValue);
        addParameter("titleDataValue", titleDataValue);
        if (url != null) {
            addParameter("url", findString(url));
        }
        if (txt != null) {
            addParameter("txt", findString(txt));
        }
        if (title != null) {
            addParameter("title", findString(title));
        }
        if (mxevent != null) {
            addParameter("mxevent", findString(mxevent));
        }
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public void setUrlDataAttribute(String urlDataAttribute) {
        this.urlDataAttribute = urlDataAttribute;
    }

    public void setTxtDataAttribute(String txtDataAttribute) {
        this.txtDataAttribute = txtDataAttribute;
    }

    public void setUrlDataValue(Object urlDataValue) {
        this.urlDataValue = urlDataValue;
    }

    public void setTxtDataValue(Object txtDataValue) {
        this.txtDataValue = txtDataValue;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleDataAttribute(String titleDataAttribute) {
        this.titleDataAttribute = titleDataAttribute;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
