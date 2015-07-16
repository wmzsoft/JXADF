package com.jxtech.tag.head;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class HeadTag extends JxBaseUITag {

    private static final long serialVersionUID = 1L;
    protected String skin;
    protected String headtype;// 头类型，TOP：最外层的菜单框

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Head(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Head head = (Head) component;
        head.setSkin(skin);
        head.setHeadtype(headtype);
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getHeadtype() {
        return headtype;
    }

    public void setHeadtype(String headtype) {
        this.headtype = headtype;
    }

}
