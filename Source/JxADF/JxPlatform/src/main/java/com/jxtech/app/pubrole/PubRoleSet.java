package com.jxtech.app.pubrole;

import java.util.List;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 康拓普角色信息- 健新科技优化实现
 *
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */
public class PubRoleSet extends JboSet {

    /**
     *
     */
    private static final long serialVersionUID = 6399268545462601488L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new PubRole(this);
        return currentJbo;
    }

    @Override
    public List<JboIFace> query() throws JxException {
        App app = JxSession.getMainApp();
        if (app.getAppNameType().equalsIgnoreCase("PUBDEPARTMENT.LIST")) {
            DataQueryInfo queryInfo = getQueryInfo();

            App userApp = JxSession.getApp("pubdepartment.user");
            if (null != userApp) {
                JboIFace userJbo = userApp.getJbo();
                if (null != userJbo) {
                    JboSetIFace userRoleJboSet = userJbo.getRelationJboSet("PUB_ROLE_USERUSER_IDP");
                    if (null != userRoleJboSet) {
                        List<JboIFace> userRoleList = userRoleJboSet.getJbolist();
                        if (!userRoleList.isEmpty()) {
                            StringBuffer sb = new StringBuffer();

                            sb.append("ROLE_ID NOT IN (");
                            int size = userRoleList.size();
                            for (int i = 0; i < size; i++) {
                                JboIFace tempJbo = userRoleList.get(i);
                                sb.append("'").append(tempJbo.getString("ROLE_ID")).append("'");
                                if (i < size - 1) {
                                    sb.append(",");
                                }
                            }
                            sb.append(")");

                            queryInfo.setWhereCause(sb.toString());
                        }

                    }
                }
            }
        }

        getQueryInfo().setPageNum(0);
        getQueryInfo().setPageSize(40);
        return super.query();
    }
}
