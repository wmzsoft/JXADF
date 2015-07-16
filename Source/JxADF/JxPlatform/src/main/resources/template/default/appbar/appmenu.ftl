<#--
/** 应用程序菜单之下拉菜单
$id:appbar-appmenu$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<#t/>
<#if (parameters.menusList??) >
    <#if ((parameters.menusList?size) > 0) >
	<div class="choose-option">
    <div class="appbar-menu-option">${parameters.tagbundle['appbar.menu.option']}</div>
    <div class="appbar-menu-list">
        <table>
            <#list parameters.menusList as menu>
                <#if menu.data??>
                    <tr><#t/>
                    <td class="appbar-menu-${menu.data['MENUTYPE']}"><#t/>
                        <#assign submenus=menu.getJboSet().getMenus(menu.data['APP'],menu.data['APPTYPE'],'LIST',menu.data['MENU']) >
                        <#if (submenus??)>
                            <#if (submenus?size>0)>

                                <#assign menuValue = menu.data['MENU']>

                                <#if lang??>
                                    <#assign  menuValue = lang["maxmenu.${parameters.appNameType!''}.${menu.data['MENU']}"]>
                                    <#if menuValue == "">
                                        <#assign menuValue = lang["maxmenu.${menu.data['MENU']}"]>
                                        <#if (menuValue == "")>
                                            <#assign menuValue = lang["maxmenu.${menu.data['DESCRIPTION']}"]>
                                            <#if (menuValue == "")>
                                                <#assign menuValue = menu.data['DESCRIPTION']>
                                            </#if>
                                        </#if>
                                    </#if>
                                </#if>

                            ${menuValue}
                                <#t/>

                            </td><#t/>
                                <td class="appbar-menu-sub"><#t/>
                                    <#list submenus as subm>
                                        <#if subm.data??>
                                            <span class="appbar-menu-vsline">|</span>
                                            <#if (subm.data['IMAGE']??)>
                                                <img src="${base}/skin/${skinName}/images/${subm.data['IMAGE']}"
                                                     class="appbar-menu-list-icon"/><#t/>
                                            </#if>
                                            <a href="javascript:void(0)"
                                               onclick="${subm.data['MENU']?lower_case}(this,event)" <#t/>
                                               mxevent="${subm.data['MENU']?lower_case}" ${subm.data['EXTENDS']!''}
                                               class="appbar-menu-sub-${subm.data['MENUTYPE']}"<#t/>
                                                    ><#t>

                                                <#assign subMenuValue = subm.data['DESCRIPTION']>

                                                <#if lang??>
                                                <#assign subMenuValue = lang["maxmenu.${parameters.appNameType!''}.${subm.data['MENU']}"]>
                                                <#if (subMenuValue == "")>
                                                    <#assign subMenuValue = lang["maxmenu.${subm.data['MENU']}"]>
                                                    <#if (subMenuValue == "")>
                                                        <#assign subMenuValue = lang["maxmenu.${subm.data['DESCRIPTION']}"]>
                                                        <#if (subMenuValue == "")>
                                                            <#assign subMenuValue = subm.data['DESCRIPTION']>
                                                        </#if>
                                                    </#if>
                                                </#if>

                                            </#if>
                                                ${subMenuValue}
                                            </a><#t/>
                                        </#if>
                                    </#list>
                                </td><#t/>
                            <#else>
                                <#if (menu.data['IMAGE']??)>
                                    <img src="${base}/skin/${skinName}/images/${menu.data['IMAGE']}" <#rt>
                                        <#lt> class="appbar-menu-list-icon"/><#rt/>
                                </#if>
                                <a href="javascript:void(0)"
                                   onclick="${menu.data['MENU']?lower_case}(this,event)" <#rt/>
                                    <#lt> mxevent="${menu.data['MENU']?lower_case}" ${menu.data['EXTENDS']!''} <#rt/>
                                        ><#t>

                                    <#assign subMenuValue = menu.data['DESCRIPTION']>
                                    <#if lang??>
                                    <#assign subMenuValue = lang["maxmenu.${parameters.appNameType!''}.${menu.data['MENU']}"]>
                                    <#if (subMenuValue == "")>
                                        <#assign subMenuValue = lang["maxmenu.${menu.data['MENU']}"]>
                                        <#if (subMenuValue == "")>
                                            <#assign subMenuValue = lang["maxmenu.${menu.data['DESCRIPTION']}"]>
                                            <#if (subMenuValue == "")>
                                                <#assign subMenuValue = menu.data['DESCRIPTION']>
                                            </#if>
                                        </#if>
                                    </#if>
                                </#if>
                                    ${subMenuValue}
                                </a><#t/>
                                </td><#t/>
                            </#if>
                        </#if>
                    </tr><#t/>
                </#if>
            </#list>
        </table>
    </div>
	</div>
    </#if>
</#if>
