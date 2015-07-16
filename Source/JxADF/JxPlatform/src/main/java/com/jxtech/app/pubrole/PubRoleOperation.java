package com.jxtech.app.pubrole;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;


/**
 * 康拓普角色用户信息- 健新科技优化实现
 *
 * @author smellok@126.com
 * @date 2014.09
 */
public class PubRoleOperation extends Jbo {

    private static final long serialVersionUID = -3364690602633425701L;

    public PubRoleOperation(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        JboIFace parent = getParent();
        if (parent != null) {
            this.setObject("role_id", parent.getObject("role_id"));
        }
        return super.setDefaultValue();
    }

}
