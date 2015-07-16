package com.jxtech.tag.code;

import com.jxtech.common.JxLoadResource;
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
@StrutsTag(name = "Code", tldTagClass = "com.jxtech.tag.code.CodeTag", description = "Code")
public class Code extends JxBaseUIBean {

    public Code(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
        JxLoadResource.loadCode(request);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("code", false);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("code", false);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
    }

}
