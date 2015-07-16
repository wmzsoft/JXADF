package com.jxtech.tag.json;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
@StrutsTag(name = "jsonCol", tldTagClass = "com.jxtech.tag.json.JsonColTag", description = "Json Column")
public class JsonCol extends JxBaseUIBean {
    protected String dataattribute;//
    private String dataname;//数据显示名称，默认为dataattribute

    public JsonCol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "json/json-col-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "json/json-col.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (dataattribute != null) {
            addParameter("dataattribute", findString(dataattribute));
        }
        if (dataname != null) {
            addParameter("dataname", findString(dataname));
        }
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

}
