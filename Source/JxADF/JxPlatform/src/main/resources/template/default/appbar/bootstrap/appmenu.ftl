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
    <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
        ${parameters.tagbundle['appbar.menu.option']}<span class="caret"></span></a><#t>
        <ul class="dropdown-menu" role="menu">
            <#list parameters.menusList as menu>
                <#if menu.data??>
                    <#assign submenus=menu.getJboSet().getMenus(menu.data['APP'],menu.data['APPTYPE'],'LIST',menu.data['MENU']) >
                    <#if (submenus??)>
                        <#if (submenus?size>0)>
                            <#list submenus as subm>
                                <#if subm.data??>
                                    <li><#t/>
                                        <span <#rt>
                                            <#lt> onclick="${subm.data['MENU']?lower_case}(this,event)" <#rt/>
                                            <#lt> mxevent="${subm.data['MENU']?lower_case}" ${subm.data['EXTENDS']!''}
                                                ><#t>
                                            <#if (toolbar.data['IMAGE']??)>
                                                <img src="${base}/skin/${skinName}/images/${toolbar.data['IMAGE']}" <#rt>
                                                    <#lt> class="appbar-menu-toolbar-icon"/> <#rt>
                                            </#if>
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
                                        ${subMenuValue}</span>
                                    </li><#t/>
                                </#if>
                            </#list>
                            <li class="divider"></li><#t/>
                        <#else>
                            <li><#t>
                                <a href="javascript:void(0)" <#rt>
                                    <#lt> onclick="${menu.data['MENU']?lower_case}(this,event)" <#rt/>
                                    <#lt> mxevent="${menu.data['MENU']?lower_case}" ${menu.data['EXTENDS']!''} <#rt/>
                                        ><#t>
                                    <#if (toolbar??) && (toolbar.data['IMAGE']??)>
                                        <img src="${base}/skin/${skinName}/images/${toolbar.data['IMAGE']}" <#rt>
                                            <#lt> class="appbar-menu-toolbar-icon"/> <#rt>
                                    </#if>
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
                                ${subMenuValue}</a>
                            </li><#t/>
                        </#if>
                    </#if>
                </#if>
            </#list>
        </ul>
    </#if>
</#if>
