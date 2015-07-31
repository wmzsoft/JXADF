package com.jxtech.app.pubrole;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;


/**
 * 康拓普角色信息- 健新科技优化实现
 *
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */
public class PubRole extends Jbo {

    private static final long serialVersionUID = 1203011774084075352L;

    public PubRole(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public String[] getDeleteChildren() throws JxException {
        return new String[]{"PUB_ROLE_USERROLE_IDP", "PUB_ROLE_OPERATIONROLE_IDP", "PUB_ROLE_SEC_ID"};
    }
        
}
