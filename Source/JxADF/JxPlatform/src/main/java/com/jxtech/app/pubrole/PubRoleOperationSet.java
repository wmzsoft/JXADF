package com.jxtech.app.pubrole;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JxException;

import java.util.Iterator;
import java.util.List;

/**
 * 康拓普角色用户信息- 健新科技优化实现
 *
 * @author smellok@126.com
 * @date 2014.09
 */
public class PubRoleOperationSet extends JboSet {

    /**
     *
     */
    private static final long serialVersionUID = 5184840635624431623L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new PubRoleOperation(this);
        return currentJbo;
    }

    public void lookup(List<JboIFace> lookupList) throws JxException {
        if (!lookupList.isEmpty()) {
            for (JboIFace lookupJbo : lookupList) {
                JboIFace tempJbo = add();
                //tempJbo.getData().putAll(lookupJbo.getData());
                tempJbo.setObject("OPERATION_ID", lookupJbo.getObject("MAXMENUID"));
            }
        }
    }

    /**
     * 添加菜单
     *
     * @param params
     * @return
     */
    public String toggleOperation(String params) throws JxException {
        String result = "ok";
        String[] strs = params.split(",");

        boolean add = "1".equalsIgnoreCase(strs[0]) ? true : false;
        JboSetIFace jboSet = JxSession.getMainApp().getJbo().getRelationJboSet("PUB_ROLE_OPERATIONROLE_IDP");
        if (null != jboSet) {
            if (add) {
                JboIFace jbo = jboSet.add();
                jbo.setObject("ROLE_ID", jbo.getParent().getObject("ROLE_ID"));
                jbo.setObject("MENU_ID", strs[1]);

            } else {
                List<JboIFace> jboList = jboSet.getJbolist();
                Iterator<JboIFace> ite = jboList.iterator();
                String roleId = jboSet.getParent().getString("ROLE_ID");
                while (ite.hasNext()) {
                    JboIFace jbo = ite.next();

                    if (roleId.equalsIgnoreCase(jbo.getString("ROLE_ID"))) {
                        if (jbo.getString("MENU_ID").equalsIgnoreCase(strs[1])) {
                            jbo.delete();
                        }
                    }
                }
            }

            jboSet.commit();
        } else {
            result = "fail";
        }
        return result;
    }
}
