package com.jxtech.app.pubrole;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 康拓普角色用户信息- 健新科技优化实现
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.05
 * 
 */
public class PubRoleUser extends Jbo {

    /**
     * 
     */
    private static final long serialVersionUID = -3364690602633425701L;
    private static final Logger LOG = LoggerFactory.getLogger(PubRoleUser.class);

    public PubRoleUser(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        JboIFace parent = getParent();
        if (parent != null) {
            this.setObject("role_id", parent.getObject("role_id"));
            this.setObject("user_id", parent.getObject("user_id"));
        }
        return super.setDefaultValue();
    }

}
