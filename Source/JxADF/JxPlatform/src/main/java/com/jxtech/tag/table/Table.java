package com.jxtech.tag.table;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.tag.json.JsonFoot;
import com.jxtech.tag.json.JsonHead;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.7
 */

@StrutsTag(name = "table", tldTagClass = "com.jxtech.tag.table.TableTag", description = "Table")
public class Table extends JxBaseUIBean {
    // private static final Logger LOG = LoggerFactory.getLogger(Table.class);
    protected String jboname;
    protected String selectmode;
    protected String orderby;
    protected String inputmode;
    protected String datasrc;
    protected String apprestrictions;// 应用程序限制
    protected String pagesize;// 页面大小
    protected String readonly;
    protected String visible;
    protected String jboClassName;// 定义JboSet的ClassName,一般用于虚拟情况

    protected List<Tablecol> columns = new ArrayList<Tablecol>();
    protected JboSetIFace jboset;
    protected int pagecount;
    protected int pagenum;
    protected int count;// 记录数
    protected String uidName;
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
    protected String url;// 加载数据的URL地址
    protected String type;// 加载数据的类型（JSP）
    protected String footVisible;// 是否显示页脚
    protected String expandtype; // 表格更多显示页面
    protected String height; // 表格高度
    protected String filterable; // 表格快速过滤
    protected String visibleHead;
    protected String rootparent; // treetable的根条件比如pid:1001
    protected List<TableButton> tableButtons = new ArrayList<TableButton>();// 表格按钮
    protected String fixedWidth;
    private String appName;// 应用程序名
    private String appType;// 应用程序类型
    private String tabletype;// 图片类型
    private String imgcols;
    private String imgwidth;
    private String imgheight;
    private String imgtype;
    private String render;// 渲染格式，默认为html，可选:html,json,jsonp
    private List<JsonHead> jsonHead = new ArrayList<JsonHead>();
    private List<JsonFoot> jsonFoot = new ArrayList<JsonFoot>();
    private String startStr;// 主要用于JSON渲染，添加到JSON最前的字符串
    private String endStr;// 主要用于JSON渲染，添加到JSON最后的字符串

    private String ignoreLayoutFixed;
    private String ignoreDataTable;
    private String rowSelectable;

    public Table(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    public String getDefaultOpenTemplate() {
        if ("json".equalsIgnoreCase(render)) {
            return "table/table";
        }
        return "table/" + getRenderer() + "table";
    }

    @Override
    protected String getDefaultTemplate() {
        if ("json".equalsIgnoreCase(render)) {
            return "table/table-close";
        }
        return "table/" + getRenderer() + "table-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (null != jboname) {
            addParameter("jboname", findString(jboname).toUpperCase());
        }
        if (null != apprestrictions) {
            addParameter("apprestrictions", findString(apprestrictions));
        }
        addParameter("pagesize", pagesize);
        addParameter("orderby", orderby);
        addParameter("jboset", jboset);
        addParameter("pagenum", pagenum);
        addParameter("count", count);
        addParameter("uidName", uidName);
        addParameter("pagecount", pagecount);
        addParameter("url", url);
        addParameter("type", type);
        addParameter("appName", appName);
        addParameter("appType", appType);
        addParameter("initOverlay", initOverlay);
        if (render != null) {
            addParameter("render", findString(render));
        }
        if (jsonHead != null) {
            addParameter("jsonHead", jsonHead);
        }
        if (jsonFoot != null) {
            addParameter("jsonFoot", jsonFoot);
        }
        if (footVisible != null) {
            addParameter("footVisible", findValue(footVisible, Boolean.class));
        }
        if (startStr != null) {
            addParameter("startStr", findString(startStr));
        }
        if (endStr != null) {
            addParameter("endStr", findString(endStr));
        }
        if (null != id) {
            addParameter("id", findString(id));
        }
        if (null != selectmode) {
            addParameter("selectmode", findString(selectmode).toUpperCase());
        }
        if (null != inputmode) {
            addParameter("inputmode", findString(inputmode).toUpperCase());
        }
        if (null != datasrc) {
            addParameter("datasrc", findString(datasrc).toUpperCase());
        }
        if (columns != null) {
            addParameter("columns", columns);
        }
        if (relationship != null) {
            addParameter("relationship", findString(relationship).toUpperCase());
        }
        if (trwhereattr != null) {
            addParameter("trwhereattr", findString(trwhereattr).toUpperCase());
        }
        if (trwhere != null) {
            addParameter("trwhere", findString(trwhere));
        }
        if (trwherevalue != null) {
            addParameter("trwherevalue", findString(trwherevalue).toUpperCase());
        }
        if (trfontcolor != null) {
            addParameter("trfontcolor", findString(trfontcolor).toUpperCase());
        }
        if (trbgcolor != null) {
            addParameter("trbgcolor", findString(trbgcolor).toUpperCase());
        }
        if (bottomtipvalue != null) {
            bottomtipvalue = getI18NValue(bottomtipvalue);
            addParameter("bottomtipvalue", findString(bottomtipvalue));
        }
        if (bottomtipcolor != null) {
            addParameter("bottomtipcolor", findString(bottomtipcolor).toUpperCase());
        }
        if (imgcols != null) {
            addParameter("imgcols", findValue(imgcols, Integer.class));
        }
        if (imgwidth != null) {
            addParameter("imgwidth", findString(imgwidth));
        }
        if (imgheight != null) {
            addParameter("imgheight", findString(imgheight));
        }
        if (imgtype != null) {
            addParameter("imgtype", findString(imgtype));
        }
        if (tabletype != null) {
            addParameter("tabletype", findString(tabletype).toUpperCase());
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (rootparent != null) {
            addParameter("rootparent", findString(rootparent));
        }
        if (expandtype != null) {
            addParameter("expandtype", findString(expandtype));
        }
        if (visibleHead != null) {
            addParameter("visibleHead", findValue(visibleHead, Boolean.class));
        }

        if (height != null) {
            addParameter("height", findString(height));
        }
        if (filterable != null) {
            addParameter("filterable", findString(filterable));
        }
        if (jboClassName != null) {
            addParameter("jboClassName", findString(jboClassName));
        }
        if (null != fixedWidth) {
            addParameter("fixedWidth", findString(fixedWidth));
        }

        rowSelectable = findString(rowSelectable);
        if("".equals(rowSelectable)){
            rowSelectable = "false";
        }
        addParameter("rowSelectable",rowSelectable);

        if (null != title) {
            addParameter("title", getI18NValue(title));
        }

        addParameter("initWhereCause", initWhereCause);
        addParameter("initWhereParam", initWhereParam);
        addParameter("tableButtons", tableButtons);

        addParameter("appBundle", JxLangResourcesUtil.getResourceBundle("res.app." + appName));

        addParameter("ignoreLayoutFixed",findString(ignoreLayoutFixed));
        addParameter("ignoreDataTable",findString(ignoreDataTable));
    }

    @StrutsTagAttribute(description = "set Selectmode", type = "String")
    public void setSelectmode(String selectmode) {
        this.selectmode = selectmode;
    }

    @StrutsTagAttribute(description = "set Orderby", type = "String")
    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    @StrutsTagAttribute(description = "set Inputmode", type = "String")
    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    @StrutsTagAttribute(description = "set Datasrc", type = "String")
    public void setDatasrc(String datasrc) {
        this.datasrc = datasrc;
    }

    @StrutsTagAttribute(description = "setApprestrictions", type = "String")
    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    @StrutsTagAttribute(description = "setPagesize", defaultValue = "20", type = "Integer")
    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public List<Tablecol> getColumns() {
        return columns;
    }

    public void setColumns(List<Tablecol> columns) {
        this.columns = columns;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUidName(String uidName) {
        this.uidName = uidName;
    }

    @StrutsTagAttribute(description = "setRelationship", type = "String")
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @StrutsTagAttribute(description = "set Jbo Name", type = "String")
    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setTrwhereattr(String trwhereattr) {
        this.trwhereattr = trwhereattr;
    }

    public void setTrwhere(String trwhere) {
        this.trwhere = trwhere;
    }

    public void setTrwherevalue(String trwherevalue) {
        this.trwherevalue = trwherevalue;
    }

    public void setTrfontcolor(String trfontcolor) {
        this.trfontcolor = trfontcolor;
    }

    public void setTrbgcolor(String trbgcolor) {
        this.trbgcolor = trbgcolor;
    }

    public void setBottomtipvalue(String bottomtipvalue) {
        this.bottomtipvalue = bottomtipvalue;
    }

    public void setBottomtipcolor(String bottomtipcolor) {
        this.bottomtipcolor = bottomtipcolor;
    }

    public void setInitWhereCause(String initWhereCause) {
        this.initWhereCause = initWhereCause;
    }

    public void setInitWhereParam(String initWhereParam) {
        this.initWhereParam = initWhereParam;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setInitOverlay(String initOverlay) {
        this.initOverlay = initOverlay;
    }

    public void setTableButtons(List<TableButton> tableButtons) {
        this.tableButtons = tableButtons;
    }

    public void setFootVisible(String footVisible) {
        this.footVisible = footVisible;
    }

    public void setTabletype(String tabletype) {
        this.tabletype = tabletype;
    }

    public void setImgcols(String imgcols) {
        this.imgcols = imgcols;
    }

    public void setImgwidth(String imgwidth) {
        this.imgwidth = imgwidth;
    }

    public void setImgheight(String imgheight) {
        this.imgheight = imgheight;
    }

    public void setImgtype(String imgtype) {
        this.imgtype = imgtype;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setRootparent(String rootparent) {
        this.rootparent = rootparent;
    }

    @Override
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setExpandtype(String expandtype) {
        this.expandtype = expandtype;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setFilterable(String filterable) {
        this.filterable = filterable;

    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
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

    public void setRender(String render) {
        this.render = render;
    }

    public void setJsonHead(List<JsonHead> jsonHead) {
        this.jsonHead = jsonHead;
    }

    public void setJsonFoot(List<JsonFoot> jsonFoot) {
        this.jsonFoot = jsonFoot;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

	public String getOrderby() {
		return orderby;
	}

	public String getAppName() {
		return appName;
	}

	public String getAppType() {
		return appType;
	}

    public String getIgnoreLayoutFixed(){ return  ignoreLayoutFixed; }

    public void setIgnoreLayoutFixed(String ignoreLayoutFixed){ this.ignoreLayoutFixed = ignoreLayoutFixed; }

    public String getIgnoreDataTable(){ return ignoreDataTable; }

    public void setIgnoreDataTable(String ignoreDataTable){ this.ignoreDataTable = ignoreDataTable; }

    public void setRowSelectable(String rowSelectable){ this.rowSelectable = rowSelectable; }
}
