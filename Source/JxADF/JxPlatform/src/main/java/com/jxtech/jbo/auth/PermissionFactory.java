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

    public static PermissionIFace getPermissionInstance() {
        JxVars vars = JxVarsFactory.getInstance();
        String permclass = vars.getValue("jx.permission.class", "com.jxtech.jbo.auth.impl.PermissionImpl");
        if (!StrUtil.isNull(permclass)) {
            try {
                Object obj = ClassUtil.getInstance(permclass);
                if (obj != null && obj instanceof PermissionIFace) {
                    return (PermissionIFace) obj;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return new PermissionImpl();
    }

}
