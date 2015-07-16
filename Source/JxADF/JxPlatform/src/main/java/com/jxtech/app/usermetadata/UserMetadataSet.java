package com.jxtech.app.usermetadata;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class UserMetadataSet extends JboSet implements UserMetadataSetIFace {

    private static final long serialVersionUID = 2162419661714931088L;

    /**
     * 加载用户的个性化配置
     * 
     * @throws JxException
     */
    public void loadUserMetadata() throws JxException {
        JxUserInfo userinfo = JxSession.getJxUserInfo();
        if (userinfo == null) {
            return;
        }
        DataQueryInfo dqi = getQueryInfo();
        dqi.setWhereCause("upper(userid)=upper(?)");
        dqi.setWhereParams(new Object[] { userinfo.getUserid() });
        List<JboIFace> list = queryAll();
        if (list != null) {
            int size = list.size();
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < size; i++) {
                JboIFace dto = list.get(i);
                map.put(dto.getString("KEY"), dto.getString("VALUE"));
            }
            userinfo.setMetadata(map);
        }
    }

    /**
     * 保存用户个性化信息
     * 
     * @param key
     * @param value 当为空时，表示删除。
     * @return
     * @throws JxException
     */
    public boolean saveUserMetadata(String key, String value) throws JxException {
        if (StrUtil.isNull(key)) {
            return false;
        }
        // 更新内存
        JxUserInfo userinfo = JxSession.getJxUserInfo();
        if (userinfo == null || StrUtil.isNull(userinfo.getUserid())) {
            return false;
        }
        boolean del = false;
        Map<String, String> meta = userinfo.getMetadata();
        if (meta == null) {
            meta = new HashMap<String, String>();
            meta.put(key, value);
            userinfo.setMetadata(meta);
        } else if (meta.containsKey(key)) {
            if (StrUtil.isNull(value)) {
                del = true;// 删除
                meta.remove(key);
            } else if (StrUtil.equals(value, meta.get(key))) {
                // 值相同，不用再存数据库了。
                return true;
            } else {
                meta.put(key, value);
            }
        }
        // 保存到数据库
        DataQueryInfo dqi = getQueryInfo();
        dqi.setWhereCause("upper(userid)=upper(?) and key=?");
        dqi.setWhereParams(new Object[] { userinfo.getUserid(), key });
        List<JboIFace> list = query();
        boolean add = false;
        if (list == null) {
            add = true;
        }else if (list.size() < 1) {
            add = true;
        }
        if (del) {
            if (!add) {
                return delete(new String[] { list.get(0).getUidValue() });
            } else {
                return true;
            }
        } else {
            JboIFace jbo;
            if (add) {
                // 新增
                jbo = add();
                jbo.setString("USERID", userinfo.getUserid());
                jbo.setString("KEY", key);
            } else {
                // 更新
                jbo = list.get(0);
            }
            jbo.setString("Value", value);
            return commit();
        }
    }
}
