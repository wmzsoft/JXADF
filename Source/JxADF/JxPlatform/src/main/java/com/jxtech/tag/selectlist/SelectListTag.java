package com.jxtech.tag.selectlist;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 列表选择，两个列表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public class SelectListTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(SelectListTag.class);
    private static final long serialVersionUID = 1574700941931680297L;
    protected String jboname;// 表名
    protected String wherecause;// 条件
    protected String orderby;// 排序字段
    protected String ulClass;// UI标签的样式
    protected String liClass;// li标签的样式
    protected String dataattribute;// 属性名
    protected String displayName;// 显示名称
    protected String displayValue;// 显示值
    protected JboSetIFace jboset;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new SelectList(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        SelectList sl = (SelectList) component;
        sl.setJboname(jboname);
        sl.setWherecause(wherecause);
        sl.setOrderby(orderby);
        sl.setUlClass(ulClass);
        sl.setLiClass(liClass);
        sl.setDataattribute(dataattribute);
        sl.setDisplayName(displayName);
        sl.setDisplayValue(displayValue);
        if (!StrUtil.isNull(jboname)) {
            try {
                jboset = JboUtil.getJboSet(jboname);
                jboset.getQueryInfo().setWhereCause(wherecause);
                jboset.queryAll();
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
            sl.setJboset(jboset);
        }
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getWherecause() {
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getLiClass() {
        return liClass;
    }

    public void setLiClass(String liClass) {
        this.liClass = liClass;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getUlClass() {
        return ulClass;
    }

    public void setUlClass(String ulClass) {
        this.ulClass = ulClass;
    }

}
