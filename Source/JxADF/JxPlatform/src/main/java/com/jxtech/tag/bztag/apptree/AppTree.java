package com.jxtech.tag.bztag.apptree;

import com.jxtech.common.JxLoadResource;
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
    protected String width; // 宽
    protected String height; // 高，如果未设置，自适应窗口高度
    protected String border; // 边宽
    protected boolean needauth; // 是否筛选用户授权的应用，true/false
    protected String whereCause; // 用户自定义条件
    protected boolean workflow; // 是否筛选工作流的应用，true/false
    protected String fragmentid; // 要关联刷新的对象，要有url属性
    protected String jsondata;// 得到的JSON数据
    protected String fragmentRName;// 要关联刷新对象的联系名
    protected String app;//字段名默认为APP

    public AppTree(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
        JxLoadResource.loadTree(request);
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
        if (width != null) {
            addParameter("width", findString(width));
        }
        if (height != null) {
            addParameter("height", findString(height));
        }
        if (border != null) {
            addParameter("border", findString(border));
        }
        addParameter("needauth", needauth);
        addParameter("workflow", workflow);
        if (whereCause != null) {
            addParameter("whereCause", findString(whereCause));
        }
        if (fragmentid != null) {
            addParameter("fragmentid", findString(fragmentid));
        }
        if (fragmentRName != null) {
            addParameter("fragmentRName", findString(fragmentRName));
        }
        if (app!=null){
            addParameter("app",findString(app));
        }
        if (jsondata != null) {
            addParameter("jsondata", jsondata);
        }
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

    public void setBorder(String border) {
        this.border = border;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

    public void setJsondata(String jsondata) {
        this.jsondata = jsondata;
    }

    public void setNeedauth(boolean needauth) {
        this.needauth = needauth;
    }

    public void setWorkflow(boolean workflow) {
        this.workflow = workflow;
    }

    public void setFragmentRName(String fragmentRName) {
        this.fragmentRName = fragmentRName;
    }

    public void setApp(String app) {
        this.app = app;
    }


}
