<#--
/** 应用程序菜单之下拉菜单
$id:appbar-appmenu$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<#t/>
<#if (parameters.menusToolbar??) >
    <#if ((parameters.menusToolbar?size) > 0) >
        <#list parameters.menusToolbar as toolbar>
            <#if toolbar.data??>
            <li>
                <span <#t>
                   onclick="${toolbar.data['MENU']?lower_case}(this,event)" <#t>
                   mxevent="${toolbar.data['MENU']?lower_case}" ${toolbar.data['EXTENDS']!''} ><#t>
                    <#if (toolbar.data['IMAGE']??)>
                        <img src="${base}/skin/${skinName}/images/${toolbar.data['IMAGE']}" <#t>
                             class="appbar-menu-toolbar-icon"/> <#t>
                    </#if>
                    <#assign buttonValue = toolbar.data['DESCRIPTION']>
                    <#if lang??>
                        <#assign buttonValue = lang["maxmenu.${parameters.appNameType!''}.${toolbar.data['MENU']}"]>
                        <#if (buttonValue == "")>
                            <#assign buttonValue = lang["maxmenu.${toolbar.data['MENU']}"]>
                            <#if (buttonValue == "")>
                                <#assign buttonValue = lang["maxmenu.${toolbar.data['DESCRIPTION']}"]>
                                <#if (buttonValue == "")>
                                    <#assign buttonValue = toolbar.data['DESCRIPTION']>
                                </#if>
                            </#if>
                        </#if>
                    </#if>
                ${buttonValue}</span><#t>
            </li><#t>
            </#if>
        </#list>
    </#if>
</#if>