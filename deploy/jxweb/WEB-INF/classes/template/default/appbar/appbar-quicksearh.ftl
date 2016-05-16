<#--<input type="text" class="table_quick_search" onkeydown="tableQuickSearch(this, event)"/>-->
<#assign quickSearchValue = "${parameters.quickSearchValue!''}" />
<div class="quickSearchDiv" id="quickSearchDiv">
    <!--搜索placeHolder-->
    <label class="placeholder" for="tableQuickSearchInput">
    <#if ("${quickSearchValue}" == "")>
        ${parameters.tagbundle['QUICK_SEACH_PLACE_HOLDER']!}
    </#if>
    </label>
    <!--搜索框-->
<#--<input type="text" id="tableQuickSearchInput" class="table_quick_search" onkeyup="tableQuickSearch(this, event)"/>-->
    <input type="text" id="tableQuickSearchInput" class="table_quick_search"
           value="${quickSearchValue!''}" onkeyup="tableQuickSearch(this,event)"/>
</div>
