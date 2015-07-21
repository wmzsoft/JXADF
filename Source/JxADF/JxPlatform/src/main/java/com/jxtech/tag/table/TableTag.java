package com.jxtech.tag.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.JxLoadResource;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.auth.PermissionIFace;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.json.JsonFoot;
import com.jxtech.tag.json.JsonHead;
import com.jxtech.util.ELUtil;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.NumUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */

public class TableTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(TableTag.class);
    private static final long serialVersionUID = 1L;
    protected String jboname;
    protected String selectmode;
    protected String orderby;
    protected String inputmode;// 输入模式，包括：readonly,query,edit等
    protected String datasrc;
    protected String jboClassName;// 定义JboSet的ClassName,一般用于虚拟情况
    protected String apprestrictions;// 应用程序限制
    protected String pagesize;// 页面大小
    protected List<Tablecol> columns = new ArrayList<Tablecol>();// 每一列
    protected JboSetIFace jboset;
    protected int pagecount;
    protected int pagenum;
    protected String relationship;// 联系名
    protected String initWhereCause;// 初始条件，可配置问号
    protected String initWhereParam;// 初始参数，以逗号分隔
    protected String initOverlay;// 是否覆盖，默认为FALSE

    protected String trwhereattr;
    protected String trwhere;
    protected String trwherevalue;
    protected String trfontcolor;
    protected String trbgcolor;

    protected String bottomtipvalue;
    protected String bottomtipcolor;
    protected String expandtype; // 表格展开更更多显示的页面
    protected String height; // 表格高度限制
    protected String filterable; // 是否有快速过滤
    protected String visibleHead;

    protected String url;// 加载数据的URL地址
    protected String type;// 加载数据的类型（JSP）
    protected String footVisible;// 是否显示页脚
    protected String fixedWidth; // 表格固定宽度
    protected String rootparent; // treetable的根条件比如pid=1001
    protected List<TableButton> tableButtons = new ArrayList<TableButton>();// 表格按钮
    private String appName;// 应用程序名
    private String appType;// 应用程序类型
    private String custColumns;// 自定义列信息
    private int colindex = 0;// 记录当前COL的顺序号
    private long loadType; // 数据加载的方式，取值参见：JxConstant.READ_XXX。
    private String tabletype;// 图片类型
    private String imgcols;
    private String imgwidth;
    private String imgheight;
    private String imgtype;
    private HttpServletRequest req;
    private String sql;// 查询数据用的SQL语句

    private String render;// 渲染格式，默认为html，可选:html,json,jsonp
    private List<JsonHead> jsonHead = new ArrayList<JsonHead>();
    private List<JsonFoot> jsonFoot = new ArrayList<JsonFoot>();
    private String startStr;// 主要用于JSON渲染，添加到JSON最前的字符串
    private String endStr;// 主要用于JSON渲染，添加到JSON最后的字符串
    private String quickSearchCause;// 快速查询的字段信息

    private String ignoreLayoutFixed;
    private String ignoreDataTable;
    private HttpServletResponse resp;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        req = request;
        resp = response;
        try {
            initValue(stack, request, response);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
        super.initPropertiesValue(false);
        return new Table(stack, request, response);
    }

    public void initValue(ValueStack stack, HttpServletRequest request, HttpServletResponse response) throws JxException {
        appName = StrUtil.getSplitFirst(request.getParameter("app"));// 应用程序名
        appType = StrUtil.getSplitFirst(request.getParameter("apptype"));// 应用程序类型
        render = StrUtil.getSplitFirst(request.getParameter("render"));
        if ("json".equalsIgnoreCase(render)) {
            response.setContentType("application/json; charset=utf-8");
        }
        startStr = StrUtil.getSplitFirst(request.getParameter("startStr"));
        endStr = StrUtil.getSplitFirst(request.getParameter("endStr"));
        loadType = NumUtil.parseLong(StrUtil.getSplitFirst(request.getParameter("loadType")));
        quickSearchCause = StrUtil.getSplitFirst(request.getParameter("quickSearchCause"));
        // 传入JSON格式的数据
        String jh = (String) request.getParameter("jsonhead");
        List<Map<String, Object>> jhl = JsonUtil.fromJson(jh);
        if (jhl != null) {
            int jsize = jhl.size();
            for (int i = 0; i < jsize; i++) {
                Map<String, Object> jm = jhl.get(i);
                for (Map.Entry<String, Object> entry : jm.entrySet()) {
                    JsonHead jhead = new JsonHead(stack, request, response);
                    String k = entry.getKey();
                    if ("fixText".equalsIgnoreCase(k)) {
                        jhead.setFixText((String) entry.getValue());
                    } else {
                        jhead.setAttributeLabel(entry.getKey());
                        jhead.setAttribute((String) entry.getValue());
                    }
                    jsonHead.add(jhead);
                }
            }
        }
        App app = JxSession.getMainApp();
        if (app != null) {
            if (loadType == JxConstant.READ_PERSISTENCE && null != app.getJboset()) {
                if (!StrUtil.isNull(appType) && "lookup".equalsIgnoreCase(appType)) {
                    pagenum = StrUtil.parseInt(request.getParameter("pagenum"), 1);
                    pagesize = StrUtil.parseIntToString(request.getParameter("pagesize"), 20);
                    loadType = JxConstant.READ_RELOAD;
                } else {
                    DataQueryInfo qf = JxSession.getMainApp().getJboset().getQueryInfo();

                    if (null != qf) {
                        // getCount() 返回-1，表示此表格还未查询过数据，需要使用配置的pagenum
                        if (qf.getJboset().getCount() >= 0 && StrUtil.isNull(pagesize)) {
                            pagesize = String.valueOf(qf.getPageSize());
                        }
                        pagenum = qf.getPageNum();
                        loadType = JxConstant.READ_RELOAD;
                    }
                }
            } else {
                pagenum = StrUtil.parseInt(request.getParameter("pagenum"), 1);
                pagesize = StrUtil.parseIntToString(request.getParameter("pagesize"), 20);
            }
        } else {
            pagenum = StrUtil.parseInt(request.getParameter("pagenum"), 1);
            pagesize = StrUtil.parseIntToString(request.getParameter("pagesize"), 20);
            // loadtype ?? 应该是可以不需要的。
        }

        if (pagenum <= 0) {
            pagenum = 1;
        }

        if ("-1".equals(pagesize))
            pagesize = "20";
        // custColumns = request.getParameter("custColumns");
        // initTablecol(stack, request, response);
        // LOG.debug("自定义列信息：\r\n" + custColumns);
        String orderbyS = request.getParameter("orderby");
        if (!StrUtil.isNull(orderbyS)) {
            if (orderbyS.toLowerCase().indexOf("asc") > 0 || orderbyS.toLowerCase().indexOf("desc") > 0) {
                orderby = orderbyS;
            } else {
                orderby = orderbyS + " asc";
            }
        }
        String jbonameS = request.getParameter("jboname");
        if (!StrUtil.isNull(jbonameS)) {
            jboname = jbonameS;
        }
        String relationshipS = request.getParameter("relationship");
        if (!StrUtil.isNull(relationshipS)) {
            relationship = relationshipS;
        }
        readonly = request.getParameter("readonly");
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    @Override
    protected void populateParams() {
        Table table = (Table) component;
        table.setJboname(jboname);
        table.setId(id);
        table.setSelectmode(selectmode);
        if (!StrUtil.isNull(orderby)) {
            if (orderby.toLowerCase().contains("asc") && orderby.toLowerCase().contains("desc")) {
                orderby = orderby + " asc";
            }
        }
        table.setOrderby(orderby);
        table.setInputmode(inputmode);
        table.setDatasrc(datasrc);
        table.setApprestrictions(apprestrictions);

        if (!StrUtil.isNull(footVisible)) {
            if ("false".equalsIgnoreCase(footVisible)) {
                pagesize = "0";
                pagenum = 0;
            }
        }

        table.setPagesize(pagesize);
        table.setColumns(columns);
        table.setPagenum(pagenum);

        table.setTrbgcolor(trbgcolor);
        table.setTrfontcolor(trfontcolor);
        table.setTrwhere(trwhere);
        table.setTrwhereattr(trwhereattr);
        table.setTrwherevalue(trwherevalue);
        table.setBottomtipvalue(bottomtipvalue);
        table.setBottomtipcolor(bottomtipcolor);
        table.setInitWhereCause(initWhereCause);
        table.setInitWhereParam(initWhereParam);
        table.setInitOverlay(initOverlay);
        table.setUrl(url);
        table.setType(type);
        table.setTableButtons(tableButtons);
        table.setFootVisible(footVisible);
        table.setTabletype(tabletype);
        table.setImgcols(imgcols);
        table.setImgheight(imgheight);
        table.setImgwidth(imgwidth);
        table.setImgtype(imgtype);
        table.setRootparent(rootparent);
        table.setHeight(height);
        table.setFilterable(filterable);
        table.setVisibleHead(visibleHead);
        table.setJboClassName(jboClassName);
        table.setRender(render);
        table.setStartStr(startStr);
        table.setEndStr(endStr);
        table.setJsonFoot(jsonFoot);
        table.setJsonHead(jsonHead);
        table.setAppName(appName);
        table.setAppType(appType);
        table.setRelationship(relationship);
        table.setReadonly(readonly);
        table.setVisible(footVisible);
        table.setExpandtype(expandtype);
        table.setFixedWidth(fixedWidth);
        table.setIgnoreLayoutFixed(ignoreLayoutFixed);
        table.setIgnoreDataTable(ignoreDataTable);

        Tag tag = findAncestorWithClass(this, BodyTag.class);
        App myapp;

        try {
            if (tag != null) {
                BodyTag body = (BodyTag) tag;
                appName = body.getAppName();
                appType = body.getAppType();
                myapp = JxSession.getApp(appName, appType);
            } else {
                myapp = JxSession.getApp(appName, appType);
                // 都属于没有权限系列
                PermissionIFace perm = PermissionFactory.getPermissionInstance();
                if (!perm.isPermission(appName, req, resp)) {
                    return;
                }
            }
            if (!StrUtil.isNull(relationship)) {
                jboset = getJboSetByRelationship();// 查询子记录集
            } else if (myapp != null) {
                if (!"MAINLIST".equalsIgnoreCase(myapp.getAppType())) {
                    jboset = myapp.findJboSet(null, null, loadType);
                }
            }
            if (jboset == null) {
                if (!StrUtil.isNull(jboClassName)) {
                    jboset = JboUtil.getJboSetLocal(jboClassName);
                    jboset.setJboname(jboname);
                    if (myapp != null && StrUtil.isNull(relationship)) {
                        myapp.setJboset(jboset);
                        jboset.setAppname(appName);
                    }
                } else {
                    if (!StrUtil.isNull(jboname)) {
                        jboset = JboUtil.getJboSet(jboname);
                        if (jboset != null) {
                            jboset.setAppname(this.appName);
                        }
                        if (myapp != null && StrUtil.isNull(relationship)) {
                            myapp.setJboset(jboset);
                        }
                    }
                }
            }
            if (jboset == null) {
                LOG.info("Jboset is null...");
            } else {
                jboset.setSql(sql);
                DataQueryInfo dqi = jboset.getQueryInfo();
                String wherecause = null;
                if (null != apprestrictions) {
                    wherecause = ELUtil.getJboSetElValue(jboset, findString(apprestrictions));
                }
                if (!StrUtil.isNull(rootparent)) {
                    String[] rootcauses = rootparent.split(":");
                    if (wherecause.length() > 0) {
                        wherecause += " and ";
                    }
                    if ("NULL".equalsIgnoreCase(rootcauses[1])) {
                        wherecause += rootcauses[0] + " is null";
                    } else {
                        wherecause += rootcauses[0] + "='" + rootcauses[1] + "'";
                    }

                }
                dqi.setWhereCause(wherecause);
                dqi.putParams(initWhereCause, initWhereParam, "true".equalsIgnoreCase(initOverlay));
                int count = 0;
                if (loadType == JxConstant.READ_CACHE) {
                    count = jboset.getJbolist().size();
                } else {
                    DataQueryInfo qi = jboset.getQueryInfo();
                    qi.setOrderby(orderby);
                    qi.setPageSize(StrUtil.parseInt(pagesize, 20));
                    qi.setPageNum(pagenum);
                    if (!StrUtil.isNull(rootparent)) {
                        String[] rootcauses = rootparent.split(":");
                        if ("NULL".equalsIgnoreCase(rootcauses[1])) {
                            qi.setWhereCause(rootcauses[0] + " is null");
                            qi.setWhereParams(new Object[] {});
                        } else {
                            qi.setWhereCause(rootcauses[0] + "=?");
                            qi.setWhereParams(new Object[] { rootcauses[1] });
                        }
                    }

                    if (!StrUtil.isNull(relationship)) {
                        jboset.query(relationship);
                        // 当重新查询时，之前做过的afterload设置的内容会丢失，在jboset query方法重新afterload容易造成死循环
                        jboset.afterLoad();
                    } else {
                        if (!StrUtil.isNull(quickSearchCause)) {
                            jboset.tableQuickSearch(quickSearchCause);
                        }
                        jboset.query();
                    }
                    // 从新从数据库查询总记录数
                    count = jboset.getCount(true);
                }
                try {
                    pagecount = NumUtil.getPageSize(count, pagesize);
                } catch (Exception e) {
                    pagecount = 0;
                }
                table.setUidName(jboset.getUidName());
                table.setCount(count);
                table.setPagecount(pagecount);
            }
            table.setJboset(jboset);
            JxTable jt = jboset.getJxTable();
            if (jt != null) {
                jt.setTableModle(table);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        JxLoadResource.loadTable(req);
        super.populateParams();
    }

    protected JboSetIFace findRelationship(JboIFace jf) throws JxException {
        JboSetIFace jsf = null;
        if (null != jf) {
            jsf = jf.getRelationJboSet(relationship, loadType);
            if (null == jsf) {
                Map<String, JboSetIFace> children = jf.getChildren();
                Iterator<Entry<String, JboSetIFace>> it = children.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry child = (Map.Entry) it.next();
                    jsf = findRelationship(((JboSetIFace) child.getValue()).getJbo());
                    if (null != jsf)
                        break;
                }
            }
        }
        return jsf;
    }

    public JboSetIFace getJboSetByRelationship() throws JxException {
        if (StrUtil.isNull(relationship)) {
            return null;
        }
        App myapp = JxSession.getApp(appName, appType);
        if (myapp != null) {
            JboIFace mj = myapp.getJbo();
            if (mj != null) {
                // jboset = mj.getRelationJboSet(relationship, loadType);

                jboset = mj.getRelationJboSet(relationship);

            } else {
                LOG.warn("没有找到Jbo，appName=" + myapp.getAppName());
            }
        }
        if (jboset == null) {
            LOG.warn("没有找到对应的联系Jboset：" + jboname + "," + relationship);
        } else {
            jboset.setRelationshipname(relationship);
        }
        return jboset;
    }

    @Override
    public int doStartTag() throws JspException {
        if (columns != null)
            columns.clear();
        colindex = 0;
        if (tableButtons != null) {
            tableButtons.clear();
        }
        return super.doStartTag();
    }

    private void initTablecol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        if (!StrUtil.isNull(custColumns)) {
            LOG.debug("自定义列：" + custColumns);
            List<Tablecol> ncol = new ArrayList<Tablecol>();
            String[] cls = custColumns.split(",");
            // 1、如果存在，则从原来的内容中获取,不存在，则新建
            for (int i = 0; i < cls.length; i++) {
                int idx = getTablecol(cls[i]);
                if (idx < 0) {
                    Tablecol col = new Tablecol(stack, request, response);
                    col.setDataattribute(cls[i]);
                    ncol.add(col);
                }
            }
            columns = ncol;
        }
    }

    public int getTablecol(String dataattribute) {
        if (StrUtil.isNull(dataattribute)) {
            return -1;
        }
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            Tablecol tc = columns.get(i);
            if (dataattribute.equalsIgnoreCase(tc.getDataattribute())) {
                return i;
            }
        }
        return -1;
    }

    public String getSelectmode() {
        return selectmode;
    }

    public void setSelectmode(String selectmode) {
        this.selectmode = selectmode;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getDatasrc() {
        return datasrc;
    }

    public void setDatasrc(String datasrc) {
        this.datasrc = datasrc;
    }

    public List<Tablecol> getColumns() {
        return columns;
    }

    public void setColumns(List<Tablecol> columns) {
        this.columns = columns;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getApprestrictions() {
        return apprestrictions;
    }

    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getTrwhereattr() {
        return trwhereattr;
    }

    public void setTrwhereattr(String trwhereattr) {
        this.trwhereattr = trwhereattr;
    }

    public String getTrwhere() {
        return trwhere;
    }

    public void setTrwhere(String trwhere) {
        this.trwhere = trwhere;
    }

    public String getTrwherevalue() {
        return trwherevalue;
    }

    public void setTrwherevalue(String trwherevalue) {
        this.trwherevalue = trwherevalue;
    }

    public String getTrfontcolor() {
        return trfontcolor;
    }

    public void setTrfontcolor(String trfontcolor) {
        this.trfontcolor = trfontcolor;
    }

    public String getTrbgcolor() {
        return trbgcolor;
    }

    public void setTrbgcolor(String trbgcolor) {
        this.trbgcolor = trbgcolor;
    }

    public String getBottomtipvalue() {
        return bottomtipvalue;
    }

    public void setBottomtipvalue(String bottomtipvalue) {
        this.bottomtipvalue = bottomtipvalue;
    }

    public String getBottomtipcolor() {
        return bottomtipcolor;
    }

    public void setBottomtipcolor(String bottomtipcolor) {
        this.bottomtipcolor = bottomtipcolor;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitOverlay() {
        return initOverlay;
    }

    public void setInitOverlay(String initOverlay) {
        this.initOverlay = initOverlay;
    }

    public String getCustColumns() {
        return custColumns;
    }

    public void setCustColumns(String custColumns) {
        this.custColumns = custColumns;
    }

    public int getColindex() {
        return colindex;
    }

    public void setColindex(int colindex) {
        this.colindex = colindex;
    }

    public List<TableButton> getTableButtons() {
        return tableButtons;
    }

    public void setTableButtons(List<TableButton> tableButtons) {
        this.tableButtons = tableButtons;
    }

    public String getFootVisible() {
        return footVisible;
    }

    public void setFootVisible(String footVisible) {
        this.footVisible = footVisible;
    }

    public String getTabletype() {
        return tabletype;
    }

    public void setTabletype(String tabletype) {
        this.tabletype = tabletype;
    }

    public String getImgcols() {
        return imgcols;
    }

    public void setImgcols(String imgcols) {
        this.imgcols = imgcols;
    }

    public String getImgwidth() {
        return imgwidth;
    }

    public void setImgwidth(String imgwidth) {
        this.imgwidth = imgwidth;
    }

    public String getImgheight() {
        return imgheight;
    }

    public void setImgheight(String imgheight) {
        this.imgheight = imgheight;
    }

    public String getImgtype() {
        return imgtype;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype;
    }

    public String getRootparent() {
        return rootparent;
    }

    public void setRootparent(String rootparent) {
        this.rootparent = rootparent;
    }

    public String getExpandtype() {
        return expandtype;
    }

    public void setExpandtype(String expandtype) {
        this.expandtype = expandtype;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFilterable() {
        return this.filterable;
    }

    public void setFilterable(String filterable) {
        this.filterable = filterable;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppType() {
        return appType;
    }

    public String getVisibleHead() {
        return visibleHead;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public String getJboClassName() {
        return jboClassName;
    }

    public void setJboClassName(String jboClassName) {
        this.jboClassName = jboClassName;
    }

    public String getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(String fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<JsonHead> getJsonHead() {
        return jsonHead;
    }

    public void setJsonHead(List<JsonHead> jsonHead) {
        this.jsonHead = jsonHead;
    }

    public List<JsonFoot> getJsonFoot() {
        return jsonFoot;
    }

    public void setJsonFoot(List<JsonFoot> jsonFoot) {
        this.jsonFoot = jsonFoot;
    }

    public String getIgnoreLayoutFixed() {
        return ignoreLayoutFixed;
    }

    public void setIgnoreLayoutFixed(String ignoreLayoutFixed) {
        this.ignoreLayoutFixed = ignoreLayoutFixed;
    }

    public String getIgnoreDataTable() {
        return ignoreDataTable;
    }

    public void setIgnoreDataTable(String ignoreDataTable) {
        this.ignoreDataTable = ignoreDataTable;
    }

}
