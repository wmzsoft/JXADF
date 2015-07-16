package com.jxtech.app.pubuser;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

import java.util.List;
import java.util.Map;

/**
 * 康拓普用户信息- 健新科技优化实现
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 * 
 */
public interface PubUserSetIFace extends JboSetIFace {

    public JboIFace getUser(String userid) throws JxException;

    /**
     * 
     * @param users 传入用户ＩＤ，多个之间用分号或逗号分隔。
     * @return 返回<user_id,name>
     */
    public Map<String, String> getUsers(String users) throws JxException;

    /**
     * 获得同部门用户列表
     * 
     * @param deptid
     * @return
     */
    public List<JboIFace> getSameDepartmentUser(String deptid) throws JxException;

    /**
     * 获得同部门用户列表
     * 
     * @param deptid
     * @return Map<User_id,name>
     */
    public Map<String, String> getSameDepartmentUsers(String deptid) throws JxException;
}
