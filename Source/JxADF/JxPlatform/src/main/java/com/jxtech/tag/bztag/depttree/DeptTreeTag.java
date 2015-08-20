package com.jxtech.tag.bztag.depttree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 部门树
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.08
 * 
 */
public class DeptTreeTag extends JxBaseUITag {

    private static final long serialVersionUID = -6492168913601367422L;
    private static final Logger LOG = LoggerFactory.getLogger(DeptTreeTag.class);

    private String checked;// Checkbox框字段名
    private String state;// 1：只显示有效的（默认），0：只显示无效的，-1：显示全部
    private String cause;// 查询条件
    private String jboname;// 默认为PUB_DEPARTMENT，可以为自己定义的Jboname
    private String checkedDisable;// Checkbox是否禁用，true禁用,false不禁用，其它值：Maxmenu.menu的权限
    protected String fragmentid; // 要关联刷新的对象，要有url属性
    protected String fragmentRName;//要关联刷新对象的联系名
    protected String fragmentRColumn;//要关联刷新对象联系的字段名。

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new DeptTree(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        DeptTree dt = (DeptTree) component;
        dt.setFragmentid(fragmentid);
        dt.setFragmentRName(fragmentRName);
        dt.setFragmentRColumn(fragmentRColumn);
        try {
            if (StrUtil.isNull(jboname)) {
                jboname = "PUB_DEPARTMENT";
            }
            JboSetIFace depts = JboUtil.getJboSet(jboname);
            Tag tag = findAncestorWithClass(this, BodyTag.class);
            String appname = null;
            if (tag != null) {
                BodyTag body = (BodyTag) tag;
                appname = body.getAppName();
            }else{
                appname = JxSession.getMainApp().getAppName();
            }
            depts.setAppname(appname);
            DataQueryInfo dqi = depts.getQueryInfo();
            dqi.setWhereCause(cause);
            if (!"-1".equals(state)) {
                if (StrUtil.isNull(state)) {
                    dqi.putParams(" state = ? ", "1", true);
                } else {
                    dqi.putParams(" state = ? ", state, true);
                }
            }
            String[] attributes = new String[] { "pub_department_id", "department_id", "name", "super_department_id" };
            if (!StrUtil.isNull(checked)) {
                dt.setChecked(checked);
                attributes = new String[] { "pub_department_id", "department_id", "name", "super_department_id", checked };
            }
            depts.queryAll();
            String jsondata = depts.toZTreeJson(attributes, "department_id", "super_department_id", "name", null, true, "", null);
            dt.setJsondata(jsondata);
            // 配置权限
            if (StrUtil.isNull(checkedDisable) || "false".equalsIgnoreCase(checkedDisable)) {
                dt.setCheckedDisable(false);
            } else if ("true".equalsIgnoreCase(checkedDisable)) {
                dt.setCheckedDisable(true);
            } else {
                // 根据菜单检查权限
                boolean cd = PermissionFactory.getPermissionInstance().hasFunctions(appname, checkedDisable);
                dt.setCheckedDisable(!cd);
            }
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getCheckedDisable() {
        return checkedDisable;
    }

    public void setCheckedDisable(String checkedDisable) {
        this.checkedDisable = checkedDisable;
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

    public String getFragmentRColumn() {
        return fragmentRColumn;
    }

    public void setFragmentRColumn(String fragmentRColumn) {
        this.fragmentRColumn = fragmentRColumn;
    }

}
