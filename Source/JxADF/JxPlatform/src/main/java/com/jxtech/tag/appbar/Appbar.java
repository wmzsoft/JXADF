package com.jxtech.tag.appbar;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.09
 */

@StrutsTag(name = "Appbar", tldTagClass = "com.jxtech.tag.appbar.AppbarTag", description = "Appbar")
public class Appbar extends JxBaseUIBean {

    // 以下不是标签属性
    private List<JboIFace> menusList;// 下拉菜单选项
    private List<JboIFace> menusToolbar;// 下拉菜单选项

    private String appNameType; // 应用程序
    private String hideSearch;//隐藏搜索框

    private String quickSearchValue;

    public Appbar(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("appbar", true);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("appbar", true);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("menusList", menusList);
        addParameter("menusToolbar", menusToolbar);

        if (!StrUtil.isNull(appNameType)) {
            addParameter("appNameType", findString(appNameType).toUpperCase());
        }

        if(!StrUtil.isNull(quickSearchValue)){
            addParameter("quickSearchValue", findString(quickSearchValue));
        }
        if(!StrUtil.isNull(hideSearch)) {
            addParameter("hideSearch", findString(hideSearch));
        }
    }

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public String getHideSearch(){
        return hideSearch;
    }

    public void setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
    }

    public void setMenusList(List<JboIFace> menusList) {
        this.menusList = menusList;
    }

    public void setMenusToolbar(List<JboIFace> menusToolbar) {
        this.menusToolbar = menusToolbar;
    }

    public void setAppNameType(String appNameType) {
        this.appNameType = appNameType;
    }

    public void setHideSearch(String hideSearch){
        this.hideSearch = hideSearch;
    }
}
