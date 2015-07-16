<#--<input type="text" class="table_quick_search" onkeydown="tableQuickSearch(this, event)"/>-->
<#assign quickSearchValue = "${parameters.quickSearchValue!''}" />
<div class="quickSearchDiv" id="quickSearchDiv">
    <!--搜索placeHolder-->
    <label class="placeholder" for="tableQuickSearchInput">
    <#if ("${quickSearchValue}" == "")>
        ${parameters.tagbundle['QUICK_SEACH_PLACE_HOLDER']}
    </#if>
    </label>
    <!--搜索框-->
<#--<input type="text" id="tableQuickSearchInput" class="table_quick_search" onkeyup="tableQuickSearch(this, event)"/>-->
    <input type="text" id="tableQuickSearchInput" class="table_quick_search"
           value="${quickSearchValue!''}"/>
    <script type="text/javascript">
        /*IE8中 onkeyup="tableQuickSearch(this, event) event是空值，因此改用jquery绑定事件兼容IE8*/
        $("input[type='text'][class='table_quick_search']").each(function (index, element) {
            $(this).keyup(function (e) {
                tableQuickSearch(this, e);
            });
        });
    </script>
</div>
