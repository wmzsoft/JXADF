package com.jxtech.tag.bztag.depttree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 部门树
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.08
 * 
 */
@StrutsTag(name = "DeptTree", tldTagClass = "com.jxtech.bztag.deptree.DeptTreeTag", description = "Department Tree")
public class DeptTree extends JxBaseUIBean {

    private String jsondata;// 得到的JSON数据
    private String checked;// Checkbox框字段名
    private boolean checkedDisable;// Checkbox是否禁用，true禁用
    protected String fragmentid; // 要关联刷新的对象，要有url属性
    protected String fragmentRName;//要关联刷新对象的联系名
    protected String fragmentRColumn;//要关联刷新对象联系的字段名。

    public DeptTree(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "bztag/depttree/depttree-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "bztag/depttree/depttree";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (checked != null) {
            addParameter("checked", checked);
        }
        addParameter("checkedDisable", checkedDisable);
        if (fragmentid != null) {
            addParameter("fragmentid", fragmentid);
        }
        if (fragmentRName != null) {
            addParameter("fragmentRName", fragmentRName);
        }
        if (fragmentRColumn != null) {
            addParameter("fragmentRColumn", fragmentRColumn);
        }
        if (jsondata != null) {
            addParameter("jsondata", jsondata);
        }
    }

    public void setJsondata(String jsondata) {
        this.jsondata = jsondata;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public void setCheckedDisable(boolean checkedDisable) {
        this.checkedDisable = checkedDisable;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

    public void setFragmentRName(String fragmentRName) {
        this.fragmentRName = fragmentRName;
    }

    public void setFragmentRColumn(String fragmentRColumn) {
        this.fragmentRColumn = fragmentRColumn;
    }

}
