package com.jxtech.app.securityrestrict;

import com.jxtech.jbo.field.Field;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/23
 */
public class FldApp extends Field {
    @Override
    public void execute(String attributeName, Object newVal) throws JxException {
        super.execute(attributeName, newVal);
        jbo.getRelationJboSet("SECURITYRESTRICT_MAXAPPS_ID", JxConstant.READ_RELOAD);
    }
}
