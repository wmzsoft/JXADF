package com.jxtech.app.pubuser;

import com.jxtech.jbo.field.Field;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/23
 */
public class FldDepartmentId extends Field {
    @Override
    public void execute(String attributeName, Object newVal) throws JxException {
        super.execute(attributeName, newVal);
        jbo.getRelationJboSet("PUB_USERDEPARTMENT_ID", JxConstant.READ_RELOAD);
    }
}
