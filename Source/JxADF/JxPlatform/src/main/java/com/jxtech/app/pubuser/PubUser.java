package com.jxtech.app.pubuser;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;

/**
 * 康拓普用户信息- 健新科技优化实现
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 */
public class PubUser extends Jbo {

    private static final long serialVersionUID = -2155523293267855603L;

    public PubUser(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        boolean b = super.setDefaultValue();
        if (b) {
            setObject("ACTIVE", 1);
            setObject("USER_TYPE", 1);
        }
        return b;
    }

    @Override
    public boolean canSave() throws JxException {
        boolean b = super.canSave();
        if (b) {
            String loginId = getString("LOGIN_ID");

            JboIFace jbo = JboUtil.getJbo("PUB_USER", "LOGIN_ID", loginId);
            if (jbo != null && null != jbo.getData()) {
                if (null != jbo.getUidValue()) {
                    if (!jbo.getUidValue().equalsIgnoreCase(getUidValue())) {
                        b = false;

                        throw new JxException(JxLangResourcesUtil.getString("app.pubdepartment.ALREADY_HAS_SAME_LOGINID"));
                    }
                }
            }
        }
        return b;
    }

    /**
     * 查询当前用户是否属于某个角色
     * 
     * @return
     * @throws JxException
     */
    public int getRoleuser() throws JxException {
        JboIFace parent = getParent();
        if (parent != null) {
            if ("PUB_ROLE".equalsIgnoreCase(parent.getJboName())) {
                JboSetIFace rus = JboUtil.getJboSet("pub_role_user");
                DataQueryInfo dqi = rus.getQueryInfo();
                dqi.setWhereCause("role_id=? and user_id=?");
                dqi.setWhereParams(new Object[] { parent.getString("ROLE_ID"), getString("USER_ID") });
                return rus.count();
            }
        }
        return 0;
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly("USER_ID", true);
        }
    }

    @Override
    public String[] getDeleteChildren() throws JxException {
        return new String[] { "USERMETADATAUSERIDP", "PUB_ROLE_USERUSER_IDP", "JXACCESSSTATUSERIDP" };
    }

    @Override
    public String[] getExportRelationship() throws JxException {
        return new String[] { "USERMETADATAUSERIDP" };
    }

}
