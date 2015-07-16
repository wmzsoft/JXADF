package com.jxtech.app.maxobject;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxObjectDao;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxObject extends Jbo {

    private static final long serialVersionUID = 7016190842255241238L;

    public MaxObject(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly("ObjectName", true);
        }
    }

    @Override
    public boolean save() throws JxException {
        String cachekey = JxObjectDao.CACHE_PREX + getString("Objectname").toUpperCase();
        CacheUtil.putBaseCache(cachekey, null);
        return super.save();
    }

}
