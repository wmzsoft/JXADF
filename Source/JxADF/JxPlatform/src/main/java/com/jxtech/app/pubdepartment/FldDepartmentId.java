package com.jxtech.app.pubdepartment;

import com.jxtech.jbo.field.Field;
import com.jxtech.jbo.util.JxException;

/**
 * Created by cxm on 2014/9/23
 */
public class FldDepartmentId extends Field {
    @Override
    public void execute(String attributeName, Object newVal) throws JxException {
        super.execute(attributeName, newVal);
        //当自己的编号变化了，其子集的编号也需要变化


    }
}
