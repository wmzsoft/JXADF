package com.jxtech.app.pubdepartment;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxm on 2014/9/22
 */
public class PubDepartment extends Jbo {
    private static final long serialVersionUID = 1203011774084075352L;

    public PubDepartment(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        boolean b = super.setDefaultValue();
        if (b) {
            setObject("STATE", 1);
        }
        return b;
    }

    @Override
    public boolean canDelete() throws JxException {
        if (!super.canDelete()) {
            return false;
        }
        JboSetIFace childDeptSet = getRelationJboSet("PUB_DEPARTMENTSUPER_DEPARTMENT_IDP");
        List<JboIFace> childs = childDeptSet.getJbolist();
        if (childs == null) {
            return true;
        }
        if (childs.isEmpty()) {
            JboSetIFace deptUserJboSet = getRelationJboSet("PUB_USERDEPARTMENT_IDP");
            if (!deptUserJboSet.getJbolist().isEmpty()) {
                throw new JxException(JxLangResourcesUtil.getString("app.pubdepartment.DEL.HASUSER"));
            }
        } else {
            int size = childs.size();
            for (int i = 0; i < size; i++) {
                JboIFace jbo = childs.get(i);
                JboSetIFace deptUserJboSet = jbo.getRelationJboSet("PUB_USERDEPARTMENT_IDP");
                if (!deptUserJboSet.getJbolist().isEmpty()) {
                    getJboSet().rollback();
                    throw new JxException(JxLangResourcesUtil.getString("app.pubdepartment.DEL.HASUSER"));
                } else {
                    jbo.delete();
                }
            }
        }
        return true;
    }

    /**
     * 获取某个部门极其下面的所有子级
     * 
     * @param includeSelf 是否包含自己 true || false
     * @return
     */
    public List<JboIFace> getCascadeDepartment(boolean includeSelf) throws JxException {
        List<JboIFace> departmentList = new ArrayList<JboIFace>();

        if (includeSelf) {
            departmentList.add(this);
        }

        JboSetIFace jboset = JboUtil.getJboSet("PUB_DEPARTMENT");
        if (null != jboset) {
            DataQueryInfo dataQueryInfo = jboset.getQueryInfo();
            dataQueryInfo.setWhereCause("SUPER_DEPARTMENT_ID = ? and STATE = 1");
            dataQueryInfo.setWhereParams(new Object[] { getString("DEPARTMENT_ID") });
            List<JboIFace> jboIFaceList = jboset.queryAll();
            for (JboIFace jboIFace : jboIFaceList) {
                PubDepartment pubDepartmentJbo = (PubDepartment) jboIFace;
                departmentList.addAll(pubDepartmentJbo.getCascadeDepartment(true));
            }
        }

        return departmentList;
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly("Department_id", true);
        }
    }

}
