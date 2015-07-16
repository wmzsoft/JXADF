package com.jxtech.tag.panel;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.09
 */

@StrutsTag(name = "Panel", tldTagClass = "com.jxtech.tag.panel.PanelTag", description = "Panel")
public class Panel extends JxBaseUIBean {
    private String type;// 类型：info,note,waring,error,gray,blue,green,yellow...
    private String title;
    private String content;

    public Panel(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "panel/" + getRenderer() + "panel-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "panel/" + getRenderer() + "panel";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("type", findString(type));
        addParameter("title", findString(title));
        addParameter("content", findString(content));
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
