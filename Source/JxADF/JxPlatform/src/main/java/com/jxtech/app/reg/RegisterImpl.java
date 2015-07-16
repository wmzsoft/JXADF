package com.jxtech.app.reg;

import com.jxtech.jbo.util.JxException;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public class RegisterImpl implements IRegister {
    @Override
    public boolean Register(String account, String password, String mobile, String requestor, String company) throws JxException {
        return false;
    }

    @Override
    public String RegisterUrl() {
        return null;
    }
}
