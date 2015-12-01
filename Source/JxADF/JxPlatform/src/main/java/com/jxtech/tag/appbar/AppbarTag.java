package com.jxtech.tag.appbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.maxmenu.MaxMenuSetIFace;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.09
 */
public class AppbarTag extends JxBaseUITag {
    private static final long serialVersionUID = -1985878464219264608L;
    private static final Logger LOG = LoggerFactory.getLogger(AppbarTag.class);

    protected String hideSearch;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Appbar(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("appbar")) {
            return;
        }
        super.populateParams();
        Tag body = findAncestorWithClass(this, BodyTag.class);
        Appbar appbar = (Appbar) component;
        appbar.setHideSearch(hideSearch);
        if (body != null) {
            String appname = ((BodyTag) body).getAppName();
            String apptype = ((BodyTag) body).getAppType();
            JboSetIFace jsi = null;
            try {
                jsi = JboUtil.getJboSet("MAXMENU");
            } catch (JxException e1) {
                LOG.error(e1.getMessage(), e1);
            }
            if (jsi instanceof MaxMenuSetIFace) {
                try {
                    MaxMenuSetIFace mmjsi = (MaxMenuSetIFace) jsi;
                    List<JboIFace> menulist = new ArrayList<JboIFace>();// 下拉菜单选项
                    List<JboIFace> menusToolbar = new ArrayList<JboIFace>();// 工具栏选项
                    List<JboIFace> mlist = mmjsi.getMenus(appname, apptype, "LIST", null);
                    if (mlist != null) {
                        menulist.addAll(mlist);
                    }
                    mlist = mmjsi.getMenus(appname, apptype, "TOOLBAR", null);
                    if (mlist != null) {
                        menusToolbar.addAll(mlist);
                    }
                    // 如果当前jbo是新增的，则不应显示新增按钮
                    App app = JxSession.getMainApp();
                    if (null != app) {
                        JboIFace cJbo = app.getJbo();
                        if (null != cJbo) {
                            appbar.setQuickSearchValue(cJbo.getJboSet().getQueryInfo().getQuickSearchQueryValue());
                            if (cJbo.isToBeAdd()) {
                                cJbo.prepareMaxmenu(menusToolbar, menulist);
                            }
                            /* 如果jbo是只读模式，将保存与发送按钮移除 删除也移除 */
                            if (cJbo.isReadonly()) {
                                String opts[] = { "save", "routeme", "del" };
                                cJbo.removeSomeMaxMenu(menusToolbar, Arrays.asList(opts));
                            }
                        }
                    }

                    appbar.setMenusList(doEl(menulist));
                    appbar.setMenusToolbar(doEl(menusToolbar));

                    appbar.setAppNameType(appname + "." + apptype);
                } catch (JxException e) {
                    LOG.error(e.getMessage(), e);
                }
            } else {
                LOG.warn("jboset of maxmenu is null.");
            }
        } else {
            LOG.warn("body tag not found.");
        }
    }

    private List<JboIFace> doEl(List<JboIFace> list) throws JxException {
        if (list == null || list.isEmpty()) {
            return list;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            JboIFace ji = list.get(i);
            String ext = ji.getString("extends");
            if (!StrUtil.isNull(ext) && ext.indexOf('$') >= 0) {
                if (ext.indexOf("${base}") >= 0) {
                    ext = ext.replaceAll("\\$\\{base\\}", SysPropertyUtil.getBase());
                    ji.setObject("extends", ext);
                }
            }
        }
        return list;
    }

    public void setHideSearch(String hideSearch) {
        this.hideSearch = hideSearch;
    }

    public String getHideSearch() {
        return hideSearch;
    }
}
