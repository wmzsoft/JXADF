package com.jxtech.tag.bztag.apptree;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xueliang@jxtech.net
 * @date 2015/4/23.
 */
@StrutsTag(name = "AppTree", tldTagClass = "com.jxtech.bztag.apptree.AppTreeTag", description = "AppTree")
public class AppTree extends JxBaseUIBean {
    protected String width;             //宽
    protected String height;            //高，如果未设置，自适应窗口高度
    protected String border;            //边宽
    protected String needauth;          //是否筛选用户授权的应用，true/false
    protected String whereCause;        //用户自定义条件
    protected String workflow;          //是否筛选工作流的应用，true/false
    protected String fragmentid;        //要关联刷新的对象，要有url属性

    public AppTree(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "bztag/apptree/apptree-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "bztag/apptree/apptree";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("width", width);
        addParameter("height", height);
        addParameter("border", border);
        addParameter("needauth", needauth);
        addParameter("workflow", workflow);
        addParameter("whereCause",whereCause);
        addParameter("fragmentid",fragmentid);
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getNeedauth() {
        return needauth;
    }

    public void setNeedauth(String needauth) {
        this.needauth = needauth;
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getFragmentid() {
        return fragmentid;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

}
