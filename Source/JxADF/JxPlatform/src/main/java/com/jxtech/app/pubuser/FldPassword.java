package com.jxtech.app.pubuser;

import com.jxtech.jbo.field.Field;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * Created by cxm on 2014/9/23
 */
public class FldPassword extends Field {
    @Override
    public void execute(String attributeName, Object newVal) throws JxException {
        if (null != newVal) {
            String val = newVal.toString();
            val = StrUtil.md5(val).toUpperCase();
            super.execute(attributeName, val);
        }
    }
}
