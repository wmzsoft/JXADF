package com.jxtech.integration.services;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

/**
 * 集成接口工厂类
 * 
 * @author Administrator
 * 
 */
public class IntergrationFactory {
    private static String implcass;

    public static String getImplcass() {
        return implcass;
    }

    public static void setImplcass(String implcass) {
        IntergrationFactory.implcass = implcass;
    }

    public static Intergration getIntergration() {
        if (StrUtil.isNull(implcass)) {
            return new IntergrationImpl();
        }
        Object val = ClassUtil.getInstance(implcass, true);
        if (val instanceof Intergration) {
            return (Intergration) val;
        }
        return null;
    }
}
