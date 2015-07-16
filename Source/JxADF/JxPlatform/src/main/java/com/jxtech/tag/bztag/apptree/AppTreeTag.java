package com.jxtech.tag.bztag.apptree;

import com.jxtech.app.maxapps.MaxappsSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author xueliang@jxtech.net
 * @date 2015/4/23.
 */
public class AppTreeTag extends JxBaseUITag {
    private static final long serialVersionUID = -2309342153273689145L;
    private static final Logger LOG = LoggerFactory.getLogger(AppTreeTag.class);

    protected String width;             //宽
    protected String height;            //高，如果未设置，自适应窗口高度
    protected String border;            //边宽
    protected String needauth;          //是否筛选用户授权的应用，true/false
    protected String whereCause;        //用户自定义条件
    protected String workflow;          //是否筛选工作流的应用，true/false
    protected String fragmentid;        //要关联刷新的对象，要有url属性

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
        appTree.setNeedauth(needauth);
        appTree.setWorkflow(workflow);
        appTree.setWhereCause(whereCause);
        appTree.setFragmentid(fragmentid);
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
        if (needauth != null && ("true".equalsIgnoreCase(needauth))) {
            String awc = authWhereCause();
            extWhereCause(awc);
        }
    }

    protected String authWhereCause() {
        StringBuffer s = new StringBuffer();
        s.append(" app in (null");
        try {
            MaxappsSetIFace jsi = (MaxappsSetIFace) JboUtil.getJboSet("MAXAPPS");
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            if (userInfo != null) {
                Set<String> apps = jsi.getAuthApps((userInfo.getUserid()));
                for(String appName:apps){
                    s.append(",'"+appName+"'");
                }
            }
            s.append(")");
        } catch (JxException e1) {
            LOG.error(e1.getMessage(), e1);
        }
       return s.toString();
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
        if (workflow != null && ("true".equalsIgnoreCase(workflow))) {
            String wwc = " length(trim(custapptype))>0 ";
            extWhereCause(wwc);
        }
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        extWhereCause(whereCause);
    }

    public void extWhereCause(String mywhereCause) {
        if (mywhereCause!=null && !("".equals(mywhereCause))) {
            if (this.whereCause==null){
                this.whereCause = mywhereCause;
            }else {
                this.whereCause += " and " + mywhereCause;
            }
        }
        //LOG.debug("[AppTreeTag.extWhereCause]whereCause=" + whereCause);
    }

    public String getFragmentid() {
        return fragmentid;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

}