package com.jxtech.app.maxobject;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxTableDao;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxTable extends Jbo {

    private static final long serialVersionUID = -8446692242794980024L;

    public MaxTable(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly("tablename", true);
        }
    }

    @Override
    public boolean save() throws JxException {
        String cachekey = JxTableDao.CACHE_PREX + getString("tablename").toUpperCase();
        CacheUtil.putBaseCache(cachekey, null);
        return super.save();
    }

}
