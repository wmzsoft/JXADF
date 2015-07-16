package com.jxtech.tag.selectlist;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 列表选择，两个列表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
@StrutsTag(name = "SelectList", tldTagClass = "com.jxtech.tag.select.SelectListTag", description = "Select")
public class SelectList extends JxBaseUIBean {
    protected String jboname;// 表名
    protected String wherecause;// 条件
    protected String orderby;// 排序字段
    protected String ulClass;// UI标签的样式
    protected String liClass;// li标签的样式
    protected String dataattribute;// 属性名
    protected String displayName;// 显示名称
    protected String displayValue;// 显示值
    protected JboSetIFace jboset;

    public SelectList(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "selectlist/selectlist-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "selectlist/selectlist";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jboname != null) {
            this.addParameter("jboname", findString(jboname));
        }
        if (wherecause != null) {
            this.addParameter("wherecause", findString(wherecause));
        }
        if (orderby != null) {
            this.addParameter("orderby", findString(orderby));
        }
        if (ulClass != null) {
            this.addParameter("ulClass", findString(ulClass));
        }
        if (liClass != null) {
            this.addParameter("liClass", findString(liClass));
        }
        if (dataattribute != null) {
            this.addParameter("dataattribute", findString(dataattribute));
        }
        if (displayName != null) {
            this.addParameter("displayName", findString(displayName));
        }
        if (displayValue != null) {
            this.addParameter("displayValue", findString(displayValue));
        }
        addParameter("jboset", jboset);
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setLiClass(String liClass) {
        this.liClass = liClass;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setUlClass(String ulClass) {
        this.ulClass = ulClass;
    }

}
