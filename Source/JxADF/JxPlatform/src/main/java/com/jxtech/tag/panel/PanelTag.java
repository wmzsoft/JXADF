package com.jxtech.tag.panel;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PanelTag extends JxBaseUITag {

    private static final long serialVersionUID = -3112779127857377464L;
    private String type;// 类型：info,note,waring,error,gray,blue,green,yellow...
    private String title;
    private String content;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Panel(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Panel panel = (Panel) component;
        panel.setType(type);
        panel.setTitle(title);
        panel.setContent(content);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
