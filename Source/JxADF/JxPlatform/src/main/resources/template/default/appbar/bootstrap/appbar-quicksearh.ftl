<#assign quickSearchValue = "${parameters.quickSearchValue!''}" />

  <input type="text" id="tableQuickSearchInput" class="form-control" <#rt>
    <#lt> onkeyup="tableQuickSearch(this, event,'enterKey')" onblur="tableQuickSearch(this,event,'blur')" <#rt>
    <#lt> value="${quickSearchValue!''}" origin="${quickSearchValue}" <#rt>
    <#lt> placeholder="${parameters.tagbundle['QUICK_SEACH_PLACE_HOLDER']!}"/><#rt>

