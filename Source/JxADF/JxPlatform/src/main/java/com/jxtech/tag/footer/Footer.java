package com.jxtech.tag.footer;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
@StrutsTag(name = "footer", tldTagClass = "com.jxtech.tag.footer.FooterTag", description = "footer")
public class Footer extends JxBaseUIBean {


    public Footer(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "footer/footer-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "footer/footer";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
    }

}
