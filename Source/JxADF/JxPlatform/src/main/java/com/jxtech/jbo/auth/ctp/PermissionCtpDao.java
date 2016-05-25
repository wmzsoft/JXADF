package com.jxtech.jbo.auth.ctp;

import com.jxtech.app.pubuser.PubUserSetIFace;
import com.jxtech.app.usermetadata.UserMetadataSetIFace;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.AuthenticateIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.PermissionIFace;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义权限相关的处理
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class PermissionCtpDao implements AuthenticateIFace {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionCtpDao.class);

    /**
     * 获得用户的权限数据
     * 
     * @param userid
     * @return
     * @throws JxException
     */
    public PermissionIFace getPermission(String userid) throws JxException {
        if (StrUtil.isNull(userid)) {
            return null;
        }
        // 采用懒加载，用到的时候，再加载吧
        PermissionIFace perm = new PermissionCTP();
        // perm.setFunctions(getFunctions());
        // perm.setUserFunctions(getUserFunctions(userid));
        // perm.setSecurityRestrict(getSecurityRestrict(userid));
        return perm;
    }

    /**
     * Key:MAXMENU.APP . MAXMENU.method_name Value: MAXMENU.MAXMENUID
     * 
     * @return
     */
    public Map<String, String> getFunctions() throws JxException {
        JboSetIFace jboset = JboUtil.getJboSet("MAXMENU");
        if (jboset != null) {
            List<JboIFace> list = jboset.queryAll();// 查询结果集
            if (list != null) {
                int size = list.size();
                Map<String, String> funs = new HashMap<String, String>();
                for (int i = 0; i < size; i++) {
                    JboIFace jbo = list.get(i);
                    if (jbo.getObject("APP") == null) {
                        continue;
                    }
                    String key = jbo.getString("APP").toUpperCase();
                    if (jbo.getObject("MENU") != null) {
                        key = key + "." + jbo.getString("MENU").toUpperCase();
                    }
                    funs.put(key, jbo.getString("MAXMENUID"));
                }
                return funs;
            }
        }
        return null;
    }

    public Map<String, String> getUserFunctions(String userid) throws JxException {
        if (StrUtil.isNull(userid)) {
            LOG.warn("用户为空，无法加载用户的功能权限。");
        }
        JboSetIFace jboset = JboUtil.getJboSet("PUB_ROLE_OPERATION");
        if (jboset != null) {
            DataQueryInfo qi = jboset.getQueryInfo();
            qi.setWhereCause(" OPERATION=1 and ROLE_ID in (select ROLE_ID from PUB_ROLE_USER where upper(USER_ID)=upper(?))");
            qi.setWhereParams(new Object[] { userid.toUpperCase() });
            List<JboIFace> list = jboset.queryAll();// 查询结果集
            if (list != null) {
                int size = list.size();
                Map<String, String> funs = new HashMap<String, String>();
                for (int i = 0; i < size; i++) {
                    JboIFace jbo = list.get(i);
                    if (jbo.getObject("MENU_ID") == null || jbo.getObject("ROLE_ID") == null) {
                        continue;
                    }
                    funs.put(jbo.getString("MENU_ID"), jbo.getString("ROLE_ID"));
                }
                return funs;
            }
        }
        return null;
    }

    /**
     * 获得数据限制条件
     * 
     * @param userid
     * @return app.jboname, restriction
     */
    public Map<String, String> getSecurityRestrict(String userid) throws JxException {
        if (StrUtil.isNull(userid)) {
            LOG.warn("用户为空，无法加载用户的功能权限。");
        }
        JboSetIFace jboset = JboUtil.getJboSet("SECURITYRESTRICT");
        if (jboset != null) {
            DataQueryInfo qi = jboset.getQueryInfo();
            qi.setWhereCause("groupname in (select role_id from pub_role_user where upper(user_id)= (select upper(user_id) from pub_user where upper(user_id) = ?))");
            qi.setWhereParams(new Object[] { userid.toUpperCase() });
            List<JboIFace> list = jboset.queryAll();// 查询结果集
            if (list != null) {
                int size = list.size();
                Map<String, String> srs = new HashMap<String, String>();
                for (int i = 0; i < size; i++) {
                    JboIFace jbo = list.get(i);
                    if (jbo.getObject("objectname") == null || jbo.getObject("restriction") == null) {
                        continue;
                    }
                    String key = jbo.getString("objectname").toUpperCase();
                    if (jbo.getObject("app") != null) {
                        key = jbo.getString("app").toUpperCase() + "." + key;
                    }
                    String oldcause = srs.get(key);
                    String cause = jbo.getString("restriction");
                    if (!StrUtil.isNull(oldcause) && !StrUtil.isNull(cause)) {
                        cause = "(" + cause + ") or (" + oldcause + ")";
                    }
                    // 支持EL表达式
                    cause = ELUtil.getElOfJxUser(JxSession.getJxUserInfo(), cause);
                    srs.put(key, cause);
                }
                return srs;
            }
        }
        return null;
    }

    /**
     * 获得用户信息
     * 
     * @param userid
     * @return
     */
    public JboIFace getUser(String userid) throws JxException {
        if (StrUtil.isNull(userid)) {
            LOG.warn("不知查询哪个用户，loginid is null.");
            return null;
        }
        JboSetIFace obj = JboUtil.getJboSet("PUB_USER");
        if (obj instanceof PubUserSetIFace) {
            PubUserSetIFace jboset = (PubUserSetIFace) obj;
            return jboset.getUser(userid);
        } else {
            return obj.queryJbo("user_id", userid);
        }
    }

    public JboIFace getDepartment(String department) throws JxException {
        if (StrUtil.isNull(department)) {
            LOG.warn("不知查询哪个用户，loginid is null.");
            return null;
        }
        JboSetIFace jboset = JboUtil.getJboSet("PUB_DEPARTMENT");
        if (jboset != null) {
            DataQueryInfo dq = jboset.getQueryInfo();
            dq.setWhereCause(" upper(department_id)=upper(?) ");
            dq.setWhereParams(new Object[] { department });
            List<JboIFace> list = jboset.query();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        LOG.info("加载部门信息失败：" + department);
        return null;
    }

    @Override
    public JxUserInfo getUserInfo(String userid) throws JxException {
        JboIFace jbo = getUser(userid);
        return toJxUserInfo(jbo);
    }

    /**
     * 将Jbo转换为JxUserInfo类，并加载权限信息
     * 
     * @param jbo
     * @return
     * @throws JxException
     */
    public JxUserInfo toJxUserInfo(JboIFace jbo) throws JxException {
        if (jbo == null) {
            return null;
        }
        JxUserInfo user = new JxUserInfo();
        // 设定值
        user.setDisplayname(jbo.getString("displayname"));
        user.setLoginid(jbo.getString("login_id"));
        user.setUserid(jbo.getString("user_id"));
        user.setSiteid(jbo.getString("SITEID"));
        user.setOrgid(jbo.getString("orgid"));
        user.setTenantnum(jbo.getString("PUBUSERORGANIZATION.TenantNum"));
        user.setUser(jbo);
        // 加载部门信息
        user.setDepartment(getDepartment(jbo.getString("department_id")));
        // 加载权限信息
        user.setPermission(getPermission(jbo.getString("user_id")));
        // 加载用户个性化数据
        JboSetIFace umsi = JboUtil.getJboSet("USERMETADATA");
        if (umsi instanceof UserMetadataSetIFace) {
            ((UserMetadataSetIFace) umsi).loadUserMetadata();
            // 加载默认语言
            Map<String, String> map = user.getMetadata();
            if (map != null) {
                user.setLangcode(map.get(JxUserInfo.LANG_CODE));
            }
        }
        // 放入Session中。
        JxSession.putSession(JxSession.USER_INFO, user);
        return user;
    }

    /**
     * @param userid
     * @param password
     * @return 登录成功之后，加载用户信息
     */
    @Override
    public JxUserInfo getUserInfo(String userid, String password) throws JxException {
        JboIFace user = getUser(userid);
        if (user == null || StrUtil.isNull(password)) {
            throw new JxException(JxLangResourcesUtil.getString("login.failed"));
        }
        String ep = StrUtil.md5(password);
        String dp = user.getString("PASSWORD");
        if (ep.equalsIgnoreCase(dp)) {
            return toJxUserInfo(user);
        } else {
            LOG.warn("无效用户名密码:" + "u=" + userid + ",p=" + password + "[" + ep + "]=" + dp);
            throw new JxException(JxLangResourcesUtil.getString("login.failed"));
        }
    }

}
