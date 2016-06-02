package com.jxtech.app.pubdepartment;

import java.util.ArrayList;
import java.util.List;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

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
     * @param includeSelf
     *            是否包含自己 true || false
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

    /**
     * 获取部门有效用户
     * 
     * @return
     * @throws JxException
     */
    public long getActiveuser() throws JxException {
        DataQuery dq = DBFactory.getDataQuery(this.getJboSet().getDbtype(), this.getJboSet().getDataSourceName());
        int count = dq.count("pub_user", "department_id=? and active=1", new Object[] { getString("department_id") });
        return count;
    }

    /**
     * 获取部门无效用户
     * 
     * @return
     * @throws JxException
     */
    public long getNoactiveuser() throws JxException {
        DataQuery dq = DBFactory.getDataQuery(this.getJboSet().getDbtype(), this.getJboSet().getDataSourceName());
        int count = dq.count("pub_user", "department_id=? and (active=0 or active is null)", new Object[] { getString("department_id") });
        return count;
    }

    @Override
    public boolean canCache() throws JxException {
        return !isToBeAdd();
    }

    @Override
    public boolean beforeSave() throws JxException {
        // 保存全名
        String fullname = this.getString("full_name");
        if (StrUtil.isNull(fullname)) {
            // 得到父级名称
            String superid = getString("super_department_id");
            StringBuilder fn = new StringBuilder(getString("name"));
            int i = 0;// 最多只记录i层，避免死循环
            while (!StrUtil.isNull(superid) && i < 10) {
                JboIFace ji = JboUtil.findJbo("Pub_Department", "department_id=?", new Object[] { superid });
                if (ji != null) {
                    fn.insert(0, "/");
                    fn.insert(0, ji.getString("name"));
                    superid = ji.getString("super_department_id");
                    if (ji.getString("department_id").equals(superid)) {
                        //相同的ID，退出
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            setObject("full_name", StrUtil.contact(fn.toString()));
        }
        return super.beforeSave();
    }

}
