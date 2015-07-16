package com.jxtech.app.maxmenu;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

/**
 * 菜单访问工厂类
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class MenuAccessFactory {
    // 我最近访问的应用
    public static final String MY_LAST_ACCESS_APP = "recentApp";
    // 我喜欢的应
    public static final String MY_FAVORITE_APP = "favoriteApp";
    // 访问实现类
    public static String accessImplClass = null;

    /**
     * 返回菜单访问实例
     * 
     * @return
     */
    public static MenuAccess getMenuAccessInstance() {
        if (StrUtil.isNull(accessImplClass)) {
            return null;
        }
        Object obj = ClassUtil.getInstance(accessImplClass);
        if (obj instanceof MenuAccess) {
            return (MenuAccess) obj;
        }
        return null;
    }

    public static String getAccessImplClass() {
        return accessImplClass;
    }

    public static void setAccessImplClass(String accessImplClass) {
        MenuAccessFactory.accessImplClass = accessImplClass;
    }
}
