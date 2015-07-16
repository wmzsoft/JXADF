package com.jxtech.app.maxobject;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxRelationshipDao;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class MaxRelationShip extends Jbo {

    /**
     * 
     */
    private static final long serialVersionUID = -872005655556457912L;

    public MaxRelationShip(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!this.isToBeAdd()) {
            this.setReadonly(new String[] { "Name", "Parent" }, true);
        }
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        if (!super.setDefaultValue()) {
            return false;
        }
        JboIFace parent = this.getParent();
        if (parent != null) {
            if ("MAXOBJECT".equalsIgnoreCase(parent.getJboName())) {
                setString("PARENT", parent.getString("ObjectName"));
            }
        }
        return true;
    }

    @Override
    public boolean save() throws JxException {
        // 清除缓存
        StringBuilder ckey = new StringBuilder();
        ckey.append(JxRelationshipDao.CACHE_PREX);
        ckey.append(getString("name").toUpperCase()).append(".");
        ckey.append(getString("parent").toUpperCase());
        CacheUtil.putBaseCache(ckey.toString(), null);
        return super.save();
    }

}
