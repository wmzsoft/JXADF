package com.jxtech.tag.tab;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cxm
 * @date 2014.03.21
 */
@StrutsTag(name = "Pushbutton", tldTagClass = "com.jxtech.tag.button.PushButtonTag", description = "Pushbutton")
public class Tab extends JxBaseUIBean {

    protected String page;
    protected String refreshonclick;// 点击tab重新加载

    public Tab(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tab/tab-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tab/tab";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (page != null) {
            addParameter("page", findString(page));
        }
        if (title != null) {
            addParameter("title", getI18NValue(title));
        }
        if (refreshonclick != null) {
            addParameter("refreshonclick", findString(refreshonclick).toUpperCase());
        }
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setRefreshonclick(String refreshonclick) {
        this.refreshonclick = refreshonclick;
    }
}
