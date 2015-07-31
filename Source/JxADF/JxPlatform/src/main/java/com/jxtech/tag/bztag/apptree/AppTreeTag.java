package com.jxtech.tag.bztag.apptree;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.maxmenu.MaxAppMenu;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author xueliang@jxtech.net wmzsoft@gmail.com
 * @date 2015/4/23.
 */
public class AppTreeTag extends JxBaseUITag {
    private static final long serialVersionUID = -2309342153273689145L;
    private static final Logger LOG = LoggerFactory.getLogger(AppTreeTag.class);

    protected String width; // 宽
    protected String height; // 高，如果未设置，自适应窗口高度
    protected String border; // 边宽
    protected String needauth; // 是否筛选用户授权的应用，true/false
    protected String whereCause; // 用户自定义条件
    protected String workflow; // 是否筛选工作流的应用，true/false
    protected String fragmentid; // 要关联刷新的对象，要有url属性
    protected String fragmentRName;//要关联刷新对象的联系名

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new AppTree(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        AppTree appTree = (AppTree) component;
        appTree.setWidth(width);
        appTree.setHeight(height);
        appTree.setBorder(border);
        Boolean auth = false;
        if (needauth != null) {
            auth = (Boolean) findValue(needauth, Boolean.class);
        }
        appTree.setNeedauth(auth);
        Boolean wf = false;
        if (workflow != null) {
            wf = (Boolean) findValue(workflow, Boolean.class);
        }
        appTree.setWorkflow(wf);
        appTree.setWhereCause(whereCause);
        appTree.setFragmentid(fragmentid);
        appTree.setFragmentRName(fragmentRName);
        MaxAppMenu mam = new MaxAppMenu();
        try {
            String jsondata = mam.getMaxAppMenuJson(-1, whereCause, auth, wf);
            appTree.setJsondata(jsondata);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
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

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String getFragmentid() {
        return fragmentid;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

    public String getFragmentRName() {
        return fragmentRName;
    }

    public void setFragmentRName(String fragmentRName) {
        this.fragmentRName = fragmentRName;
    }

}