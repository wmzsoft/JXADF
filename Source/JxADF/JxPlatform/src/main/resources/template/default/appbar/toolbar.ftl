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
            <span class="toolbar_btn appbar-toolbar-${toolbar.data['MENUTYPE']}"
                  onclick="${toolbar.data['MENU']?lower_case}(this, event)" <#rt>
                <#lt> mxevent="${toolbar.data['MENU']?lower_case}" ${toolbar.data['EXTENDS']!''} ><#rt>
                <#if (toolbar.data['IMAGE']?? && toolbar.data['IMAGE']!='')>
                    <#lt><img src="${base}/skin/${skinName}/images/${toolbar.data['IMAGE']}" <#rt>
                        <#lt> class="appbar-menu-toolbar-icon"/> <#rt>
                </#if>
                <#assign buttonValue = toolbar.data['DESCRIPTION']>
                <#if lang??>
                    <#assign buttonValue = lang["maxmenu.${parameters.appNameType!''}.${toolbar.data['MENU']}"]!''>
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
            ${buttonValue}<#t>
            </span><#t>
            <span class="appbar-toolbar-SEP"></span><#t>
            </#if>
        </#list>
    </#if>
</#if>