package com.jxtech.tag.json;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class JsonColTag extends JxBaseUITag {

    private static final long serialVersionUID = -7516370814761397742L;
    private String dataattribute;// 字段名
    private String dataname;// 数据显示名称，默认为dataattribute

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JsonCol(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        JsonCol col = (JsonCol) component;
        col.setDataattribute(dataattribute);
        col.setDataname(dataname);
    }

    @Override
    public int doStartTag() throws JspException {
        int flag = super.doStartTag();
        Tag tag = getParent();
        if (tag != null && tag instanceof JsonTag) {
            JsonCol col = (JsonCol) component;
            ((JsonTag) tag).getJsonCol().add(col);
        }
        return flag;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

}
