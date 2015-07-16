package com.jxtech.app.maxmenu;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

import java.util.List;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.09
 * 
 */
public interface MaxMenuSetIFace extends JboSetIFace {

    // 应用菜单的操作权限
    public static final int PERMISSION_OPERATION = 1;
    // 应用菜单的管理权限
    public static final int PERMISSION_MANAGE = 2;
    // 应用菜单管理授权限
    public static final int PERMISSION_GRANTMANAGE = 4;
    // 全部权限
    public static final long PERMISSION_ALL = 0xFFFFFF;

    /**
     * 获得应用程序菜单
     * 
     * @param appname 应用程序名
     * @param appType 应用程序类型，列表、还是主程序
     * @param tabDisplay 显示到更多中，还是显示到Toolbar中。
     * @param parent 父级菜单
     * @return
     * @throws JxException
     */
    public List<JboIFace> getMenus(String appname, String appType, String tabDisplay, String parent) throws JxException;

    /**
     * 返回权限检查的SQL脚本。
     * 
     * @return
     */
    public String getPermissionSql();
}
