package com.jxtech.tag.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.JxLoadResource;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.KeyValue;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.form.FormTag;
import com.jxtech.tag.table.TableTag;
import com.jxtech.tag.table.Tablecol;
import com.jxtech.tag.table.TablecolTag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class SelectTag extends JxBaseUITag {
    private static final long serialVersionUID = 7332612073493621349L;
    private static final Logger LOG = LoggerFactory.getLogger(SelectTag.class);

    protected String jboname;// 下拉列表的数据对象
    protected String relationship;// 联系名
    protected String wherecause;// 下拉列表中数据查询限制条件
    protected String options;// 固定的选项值，用逗号分隔，冒号分隔Key和显示值，例： 1:有效,0:无效
    protected String dataattribute;// 数据字段，主对象的字段名。
    protected String displayvalue;// 显示值，如果缺省则为dataattribute
    protected String displayname;// 显示字段
    protected String inputmode;// 输入模式
    protected String orderby;// 排序
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected String partialTriggers;// 触发器IDS，多个ID之间可以用逗号分隔
    protected String partialCause;// 触发之后的条件
    protected String selected;// 当前选中的选项。
    protected String sql;// 直接使用SQL语句加载数据
    protected String cause;// 选中值之后，查询数据库的条件
    protected String colspan; // 列宽
    protected String width; // 宽度
    protected String required;
    protected String render; // 渲染
    protected String renderExtends;// 渲染扩展属性
    protected String renderParam;
    protected String multiple;// 是否可多选
    protected String ajax;// 是否异步加载数据
    // 以下不是标签属性
    protected JboSetIFace jboset;// 查询到的结果集
    private int optionsCount = 0;// 有多少个选项

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        readonly = null;
        super.initPropertiesValue(false);
        JxLoadResource.loadSelect2(request);
        return new Select(stack, request, response);
    }

    @Override
    protected void populateParams() {
        Select select = (Select) component;
        select.setJboname(jboname);
        select.setRelationship(relationship);
        select.setWherecause(wherecause);
        select.setOptions(options);
        select.setDataattribute(dataattribute);
        select.setInputmode(inputmode);
        if ("READONLY".equalsIgnoreCase(inputmode)) {
            select.setReadonly("true");
        }
        select.setDisplayname(displayname);
        select.setMystyle(mystyle);
        select.setOrderby(orderby);
        select.setPartialTriggers(partialTriggers);
        select.setPartialCause(partialCause);
        select.setCause(cause);
        select.setColspan(colspan);
        select.setWidth(width);
        select.setRender(render);
        select.setRenderExtends(renderExtends);
        select.setRenderParam(renderParam);
        select.setMultiple(multiple);
        select.setAjax(ajax);
        if (StrUtil.isNull(displayvalue)) {
            displayvalue = dataattribute;
        }
        select.setDisplayvalue(displayvalue);
        String selectedDisplay = selected;

        String mycause = wherecause;
        jboset = null;
        try {
            doOptions(select);// 处理直接输入的值和SQL的值。
            if (!StrUtil.isNull(jboname)) {
                jboset = JboUtil.getJboSet(jboname);
                if (jboset != null) {
                    if (!StrUtil.isNull(relationship)) {
                        JboIFace j = jboset.getJbo();
                        if (j != null) {
                            JboSetIFace js = j.getRelationJboSet(relationship, JxConstant.READ_RELOAD);
                            if (js != null) {
                                jboset = js;
                            }
                        }
                    }
                    if (!StrUtil.isNull(partialCause)) {
                        if (partialCause.indexOf("?") < 0) {
                            if (!StrUtil.isNull(mycause) && mycause.length() > 3) {
                                mycause = "(" + mycause + ") AND (" + partialCause + ")";
                            } else {
                                mycause = partialCause;
                            }
                        }
                    }
                }
            } else if (!StrUtil.isNull(relationship)) {
                App app = JxSession.getApp();
                jboset = app.findJboSet(null, relationship);
            }
            // 只有当不是ajax加载的情况下，才进行数据查询
            if (jboset != null && StrUtil.isNull(ajax)) {
                jboset.getQueryInfo().setWhereCause(mycause);
                jboset.getQueryInfo().setOrderby(orderby);
                jboset.queryAll();
                optionsCount = jboset.getJbolist().size();
            }

            select.setJboset(jboset);
            App myapp = JxSession.getApp();

            Tag bodyTag = findAncestorWithClass(this, BodyTag.class);
            if (null != bodyTag) {
                BodyTag bt = (BodyTag) bodyTag;
                String appname = bt.getAppName();
                myapp = JxSession.getApp(appname, bt.getAppType());
                select.setAppName(appname);
            }

            if (myapp != null) {
                if ("query".equalsIgnoreCase(inputmode) || "queryImmediately".equalsIgnoreCase(inputmode)) {
                    // 查询框
                    JboSetIFace jsi = myapp.getJboset();
                    if (jsi != null && !StrUtil.isNull(dataattribute)) {
                        Map<String, Object> map = jsi.getQueryInfo().getParams();
                        if (map != null) {
                            Object o = map.get(dataattribute.toUpperCase() + "=?");
                            if (o != null) {
                                selected = String.valueOf(o);
                            } else {
                                o = map.get(dataattribute.toUpperCase() + cause);
                                selected = String.valueOf(o);
                            }
                        }
                    }
                } else {
                    // 不是查询框
                    JboIFace ji = myapp.getJbo();
                    Tag tag = findAncestorWithClass(getParent(), FormTag.class);
                    if (tag != null) {
                        // 当form为relationship时，从form中取数据
                        FormTag ft = (FormTag) tag;
                        if (ft.getRelationship() != null && !"".equals(ft.getRelationship())) {
                            ji = ft.getJbo();
                        }
                    } else {
                        //这里是TableTag，但仍然没用
                        tag = findAncestorWithClass(getParent(), TableTag.class);
                        if (tag != null) {
                            TableTag tt = (TableTag) tag;// 表
                            JboSetIFace tableset = tt.getJboset();
                            if (tableset != null) {
                                ji = tableset.getJbo();
                            }
                        }
                    }
                    if (ji != null && !StrUtil.isNull(dataattribute)) {
                        selected = ji.getString(dataattribute);
                        if (jboset != null && !StrUtil.isNull(selected)) {
                            JboIFace oj;
                            if (!StrUtil.isNull(wherecause)) {
                                oj = jboset.queryJbo(wherecause, displayvalue, selected);
                                select.setSelectedJbo(oj);
                            } else {
                                oj = jboset.queryJbo(displayvalue, selected);
                                select.setSelectedJbo(oj);
                            }
                            if (oj != null && !StrUtil.isNull(displayname)) {
                                selectedDisplay = oj.getString(displayname);
                            }
                        }
                        if (!"true".equalsIgnoreCase(required)) {
                            required = String.valueOf(ji.isRequired(dataattribute));
                        } else {
                            ji.setRequired(dataattribute, true);
                        }
                        if (!"true".equalsIgnoreCase(readonly)) {
                            readonly = String.valueOf(ji.isReadonly(dataattribute));
                            if (!"true".equalsIgnoreCase(readonly)) {
                                readonly = String.valueOf(ji.isReadonly());
                            }
                        }
                    }
                }
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        select.setSelected(selected);
        if (StrUtil.isNull(selectedDisplay) && !StrUtil.isNull(selected)) {
            selectedDisplay = getSelectedDispaly(select.getOptionsList(), selected);
        }
        if (StrUtil.isNull(selectedDisplay)) {
            selectedDisplay = selected;
        }
        select.setSelectedDisplay(selectedDisplay);
        select.setSql(sql);
        select.setReadonly(readonly);
        select.setVisible(visible);
        select.setRequired(required);
        select.setOptionsCount(optionsCount);
        Tag tag = getParent();
        if (tag != null && dataattribute != null) {
            if (tag instanceof TablecolTag) {
                select.setParentName("tablecol");
                TablecolTag colTag = (TablecolTag) tag;
                Component col = colTag.getComponent();
                if (col instanceof Tablecol) {
                    Tablecol tc = (Tablecol) col;
                    tc.setSec(select);
                }
            }
        }

        super.populateParams();
    }

    /**
     * 根据value返回要显示的值
     * 
     * @param list
     * @param value
     * @return
     */
    private String getSelectedDispaly(List<KeyValue> list, String value) {
        if (list == null || StrUtil.isNull(value)) {
            return null;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            KeyValue kv = list.get(i);
            if (value.equals(kv.getKey())) {
                return kv.getValue();
            }
        }
        return null;
    }

    protected void doOptions(Select select) throws JxException {
        List<KeyValue> list = new ArrayList<KeyValue>();
        if (!StrUtil.isNull(options)) {
            String[] opts = options.split(",");
            for (int i = 0; i < opts.length; i++) {
                int pos = opts[i].indexOf(":");
                KeyValue kv = new KeyValue();
                if (pos == 0) {
                    kv.setKey("null");
                    kv.setValue(opts[i].substring(pos + 1));
                } else if (pos > 0) {
                    kv.setKey(opts[i].substring(0, pos));
                    kv.setValue(opts[i].substring(pos + 1));
                } else {
                    kv.setKey(opts[i]);
                    kv.setValue(opts[i]);
                }
                list.add(kv);
            }
            optionsCount += list.size();
        }
        doSql(select, list);
        select.setOptionsList(list);
    }

    /**
     * 处理SQL脚本的数据加载
     * 
     * @param select
     * @param list
     * @throws JxException
     */
    protected void doSql(Select select, List<KeyValue> list) throws JxException {
        if (!StrUtil.isNull(sql)) {
            DataQuery dq = DBFactory.getDataQuery(null, null);
            List<Map<String, Object>> rs = dq.getResultSet(sql, null);
            if (rs != null) {
                if (list == null) {
                    list = new ArrayList<KeyValue>();
                }
                int size = rs.size();
                for (int i = 0; i < size; i++) {
                    Map<String, Object> data = rs.get(i);
                    KeyValue kv = new KeyValue();
                    kv.setKey(StrUtil.toString(data.get(displayvalue), "null"));
                    kv.setValue(StrUtil.toString(data.get(displayname), ""));
                    list.add(kv);
                }
            }
        }
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

    public String getWherecause() {
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPartialTriggers() {
        return partialTriggers;
    }

    public void setPartialTriggers(String partialTriggers) {
        this.partialTriggers = partialTriggers;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getDisplayvalue() {
        return displayvalue;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public String getPartialCause() {
        return partialCause;
    }

    public void setPartialCause(String partialCause) {
        this.partialCause = partialCause;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String getRequired() {
        return required;
    }

    @Override
    public void setRequired(String required) {
        this.required = required;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getRenderExtends() {
        return renderExtends;
    }

    public void setRenderExtends(String renderExtends) {
        this.renderExtends = renderExtends;
    }

    public String getRenderParam() {
        return renderParam;
    }

    public void setRenderParam(String renderParam) {
        this.renderParam = renderParam;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getAjax() {
        return ajax;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public int getOptionsCount() {
        return optionsCount;
    }

    public void setOptionsCount(int optionsCount) {
        this.optionsCount = optionsCount;
    }
}
