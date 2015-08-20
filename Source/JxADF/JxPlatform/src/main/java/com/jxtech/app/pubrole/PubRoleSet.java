package com.jxtech.app.pubrole;

import java.util.ArrayList;
import java.util.List;

import com.jxtech.app.pubdepartment.PubDepartment;
import com.jxtech.app.pubuser.PubUserSet;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
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
                            queryInfo.setPageNum(0);
                            queryInfo.setPageSize(40);
                        }

                    }
                }
            }
        }

        return super.query();
    }

    /**
     * 角色用户管理界面切换用户显示
     *
     * @param params 构成有showAll 1 所有 0 角色用户 ； departmentId 部门ID
     * @return
     * @throws JxException
     */
    public String showRoleUser(String params) throws JxException {
        String[] param = params.split(",");
        String showAll = param[0];
        String departmentId = param[1];

        String result = "";
        PubUserSet pubUserJboSet = (PubUserSet) JboUtil.getJboSet("PUB_USER");

        JboIFace roleJbo = JxSession.getMainApp().getJbo();
        if (null != roleJbo) {
            JboSetIFace roleUserJboSetAll = roleJbo.getRelationJboSet("PUB_ROLE_PUB_USER_ALL", JxConstant.READ_RELOAD);
            roleUserJboSetAll.getJbolist().clear();

            List<JboIFace> pubDepartmentList = null;
            PubDepartment pubDepartment = (PubDepartment) JboUtil.getJbo("PUB_DEPARTMENT", "DEPARTMENT_ID", departmentId);
            if (null != pubDepartment) {
                pubDepartmentList = pubDepartment.getCascadeDepartment(true);
            }

            List<JboIFace> departmentUserList = new ArrayList<JboIFace>();
            //查询当前部门的所有用户
            if (null != pubDepartmentList) {
                for (JboIFace jbo : pubDepartmentList) {
                    departmentUserList.addAll(jbo.getRelationJboSet("PUB_USERDEPARTMENT_IDP", JxConstant.READ_RELOAD, true).getJbolist());
                }
                //有缓存需要重置该属性
                for (JboIFace tempJbo : departmentUserList) {
                    tempJbo.setObject("INROLE", "0");
                }
            }

            List<JboIFace> roleUserList = new ArrayList<JboIFace>();
            //再查询角色下的用户
            PubRoleUserSet roleUserJboSet = (PubRoleUserSet) roleJbo.getRelationJboSet("PUB_ROLE_USERROLE_IDP", JxConstant.READ_RELOAD, true);
            for (JboIFace roleUserJbo : roleUserJboSet.getJbolist()) {
                if (null != roleUserJbo) {
                    String userId = roleUserJbo.getString("USER_ID");
                    JboIFace userJbo = pubUserJboSet.getUser(userId);
                    if (null !=userJbo ) {
                        roleUserList.add(userJbo);
                    }
                }
            }

            //显示所有用户
            if ("1".equalsIgnoreCase(showAll)) {
                for (JboIFace roleUser : roleUserList) {
                    String roleUserId = roleUser.getString("USER_ID");
                    for (JboIFace departmentUser : departmentUserList) {
                        String userId = departmentUser.getString("USER_ID");
                        if (userId.equalsIgnoreCase(roleUserId)) {
                            departmentUser.setObject("INROLE", "1");
                        } /*else {
                            departmentUser.setObject("INORLE", "0");
                        }*/
                    }
                }
                roleUserJboSetAll.getJbolist().addAll(departmentUserList);
                result = "ok";
            } else if ("0".equalsIgnoreCase(showAll)) {
                for (JboIFace departmentUser : departmentUserList) {
                    String userId = departmentUser.getString("USER_ID");
                    for (JboIFace roleUser : roleUserList) {
                        String roleUserId = roleUser.getString("USER_ID");
                        if (userId.equalsIgnoreCase(roleUserId)) {
                            departmentUser.setObject("INROLE", "1");
                            roleUserJboSetAll.getJbolist().add(departmentUser);
                        }
                    }
                }

                result = "ok";
            }
        }
        return result;
    }
}
