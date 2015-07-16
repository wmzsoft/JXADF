package com.jxtech.app.maxobject;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxAttributeDao;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxAttribute extends Jbo {

    private static final long serialVersionUID = -556121545379062702L;

    public MaxAttribute(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly("ObjectName", true);
            this.setReadonly("attributename", true);
        }
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        if (!super.setDefaultValue()) {
            return false;
        }
        JboIFace parent = this.getParent();
        if (parent != null) {
            setString("ObjectName", parent.getString("ObjectName"));
        }
        return true;
    }

    @Override
    public boolean save() throws JxException {
        String cachekey = JxAttributeDao.CACHE_PREX + getString("objectname").toUpperCase();
        CacheUtil.putBaseCache(cachekey, null);
        return super.save();
    }

}
