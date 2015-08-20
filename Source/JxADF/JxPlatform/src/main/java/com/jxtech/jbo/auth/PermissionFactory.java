package com.jxtech.jbo.auth;

import com.jxtech.app.jxvars.JxVars;
import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.jbo.auth.impl.PermissionImpl;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class PermissionFactory {
    private static Logger LOG = LoggerFactory.getLogger(PermissionFactory.class);
    private static PermissionIFace permissionInstance;

    /**
     * 获得权限类的单例
     * 
     * @return
     */
    public static PermissionIFace getPermissionInstance() {
        if (permissionInstance != null) {
            return permissionInstance;
        }
        JxVars vars = JxVarsFactory.getInstance();
        String permclass = vars.getValue("jx.permission.class", "com.jxtech.jbo.auth.impl.PermissionImpl");
        if (!StrUtil.isNull(permclass)) {
            try {
                Object obj = ClassUtil.getInstance(permclass);
                if (obj instanceof PermissionIFace) {
                    permissionInstance = (PermissionIFace) obj;
                    return permissionInstance;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        permissionInstance = new PermissionImpl();
        return permissionInstance;
    }

}
