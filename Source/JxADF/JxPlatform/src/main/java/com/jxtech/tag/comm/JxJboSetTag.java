package com.jxtech.tag.comm;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.util.NumUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * 处理数据集的基类
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.01
 * 
 */

public class JxJboSetTag extends JxBaseUITag {

    private static final long serialVersionUID = -6477721806237829726L;
    private static final Logger LOG = LoggerFactory.getLogger(JxJboSetTag.class);

    private String jboname;
    private String relationship;
    private String orderby;
    private String pagesize;
    private String apprestrictions;
    private JboSetIFace jboset;

    private String initWhereCause;// 初始条件，可配置问号
    private String initWhereParam;// 初始参数，以逗号分隔
    private String initOverlay;// 是否覆盖，默认为FALSE

    private int count;// 记录数
    private int pagecount; // 每页大小
    private int pagenum; // 当前页号
    private String appName;// 应用程序名
    private String appType;// 应用程序类型
    private long loadType; // 数据加载的方式，取值参见：JxConstant.READ_XXX。

    public void initValue(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        pagenum = StrUtil.parseInt(request.getParameter("pagenum"), 1);
        if (pagenum <= 0) {
            pagenum = 1;
        }
        pagesize = StrUtil.parseIntToString(request.getParameter("pagesize"), 20);
        appName = StrUtil.getSplitFirst(request.getParameter("app"));// 应用程序名
        appType = StrUtil.getSplitFirst(request.getParameter("apptype"));// 应用程序类型
        loadType = NumUtil.parseLong(StrUtil.getSplitFirst(request.getParameter("loadType")));
    }

    @Override
    public void initPropertiesValue(boolean all) {
        super.initPropertiesValue(all);
        // jboname = null;
        // relationship = null;
        // orderby = null;
        // pagesize = null;
        // apprestrictions = null;
        // jboset = null;
        // initWhereCause = null;
        // initWhereParam = null;
        // initOverlay = null;
        // count = 0;
        // pagecount = 0;
        // pagenum = 0;
        // appName = null;
        // appType = null;
        // loadType = 0;
    }

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    protected void populateParams() {
        JxJboSet js = (JxJboSet) component;
        try {
            queryJboSet();
        } catch (JxException e) {
            LOG.error(e.getMessage(),e);
        }
        js.setJboset(jboset);
        js.setJboname(jboname);
        js.setRelationship(relationship);
        js.setPagecount(String.valueOf(pagecount));
        js.setPagesize(String.valueOf(pagesize));
        js.setPagenum(String.valueOf(pagenum));
        js.setCount(String.valueOf(count));
        super.populateParams();
    }

    /**
     * 通过标签中配置的信息，查询数据结果集
     * 
     * @return
     * @throws JxException 
     */
    public JboSetIFace queryJboSet() throws JxException {
        App myapp;
        Tag tag = findAncestorWithClass(getParent(), BodyTag.class);
        if (tag != null) {
            BodyTag body = (BodyTag) tag;
            appName = body.getAppName();
            myapp = JxSession.getApp(appName, body.getAppType());
        } else {
            myapp = JxSession.getApp(appName, appType);
        }
        try {
            if (null != jboname) {
                if (!StrUtil.isNull(relationship)) {
                    getJboSetByRelationship();// 查询子记录集
                } else {
                    String wherecause = null;
                    if (null != apprestrictions) {
                        wherecause = findString(apprestrictions);
                    }

                    if (myapp != null) {
                        jboset = myapp.findJboSet(jboname, null, loadType);
                        if (jboset == null) {
                            jboset = JboUtil.getJboSet(jboname);
                        }
                        myapp.setJboset(jboset);
                    } else {
                        jboset = JboUtil.getJboSet(jboname);
                        jboset.setAppname(appName);
                    }
                    DataQueryInfo dqi = jboset.getQueryInfo();
                    dqi.setJboset(jboset);
                    dqi.setWhereCause(wherecause);
                    dqi.putParams(initWhereCause, initWhereParam, "true".equalsIgnoreCase(initOverlay));
                }
                if (jboset == null) {
                    LOG.info("Jboset is null...");
                } else {
                    if (loadType == JxConstant.READ_CACHE) {
                        count = jboset.getJbolist().size();
                    } else {
                        DataQueryInfo qi = jboset.getQueryInfo();
                        qi.setOrderby(orderby);
                        qi.setPageSize(StrUtil.parseInt(pagesize, 20));
                        qi.setPageNum(pagenum);
                        jboset.query();
                        count = jboset.getCount();
                    }
                    try {
                        pagecount = NumUtil.getPageSize(count, pagesize);
                    } catch (Exception e) {
                        pagecount = 0;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return jboset;
    }

    public JboSetIFace getJboSetByRelationship() throws JxException {
        if (StrUtil.isNull(relationship)) {
            return null;
        }
        App myapp = JxSession.getApp(appName, appType);
        if (myapp != null) {
            jboset = myapp.findJboSet(jboname, relationship, loadType);
        }
        if (jboset == null) {
            LOG.warn("没有找到对应的联系Jboset：" + jboname + "," + relationship);
        }
        return jboset;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getApprestrictions() {
        return apprestrictions;
    }

    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getInitWhereCause() {
        return initWhereCause;
    }

    public void setInitWhereCause(String initWhereCause) {
        this.initWhereCause = initWhereCause;
    }

    public String getInitWhereParam() {
        return initWhereParam;
    }

    public void setInitWhereParam(String initWhereParam) {
        this.initWhereParam = initWhereParam;
    }

    public String getInitOverlay() {
        return initOverlay;
    }

    public void setInitOverlay(String initOverlay) {
        this.initOverlay = initOverlay;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public long getLoadType() {
        return loadType;
    }

    public void setLoadType(long loadType) {
        this.loadType = loadType;
    }

}
