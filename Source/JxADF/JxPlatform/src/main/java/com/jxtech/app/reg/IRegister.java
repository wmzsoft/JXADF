package com.jxtech.app.reg;

import com.jxtech.jbo.util.JxException;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public interface IRegister {



    public boolean Register(String account, String password,  String mobile, String requestor, String company) throws JxException;;
    public String RegisterUrl();

}
