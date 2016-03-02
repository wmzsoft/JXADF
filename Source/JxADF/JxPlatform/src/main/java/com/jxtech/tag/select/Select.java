package com.jxtech.tag.select;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.KeyValue;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
@StrutsTag(name = "Select", tldTagClass = "com.jxtech.tag.select.SelectTag", description = "Select")
public class Select extends JxBaseUIBean {

    protected String jboname;// 下拉列表的数据对象
    protected String relationship;// 联系名
    protected String wherecause;// 查询限制条件
    protected String options;// 固定的选项值，用逗号分隔，冒号分隔Key和显示值，例： 1:有效,0:无效
    protected String dataattribute;// 数据字段
    protected String displayvalue;// 显示值，如果缺省则为dataattribute
    protected String inputmode;// 输入模式
    protected String displayname;// 显示字段
    protected String orderby;// 排序
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected String partialTriggers;// 触发器IDS，多个ID之间可以用逗号分隔
    protected String partialCause;// 触发之后的条件
    protected String selected;// 当前选中的选项。
    protected String selectedDisplay;// 当前选中项的显示值
    protected String sql;// 直接使用SQL语句加载数据
    protected String cause;// 选中值之后，查询数据库的条件
    protected String readonly;
    protected String visible;
    protected String colspan; // 列宽
    protected String width; // 自定义宽度
    protected String render; // 渲染
    protected String renderExtends;// 渲染扩展属性
    protected String renderParam;
    protected String multiple;// 是否可多选
    protected String ajax;// 是否异步加载数据
    private int optionsCount;// 有多少个选项
    // 以下不是标签属性
    protected JboSetIFace jboset;// 查询到的结果集
    protected List<KeyValue> optionsList; // 固定选项值转换为List
    protected String required;
    protected JboIFace selectedJbo;// 当前选种的JBO
    private String parentName;// 父级标签的名称
    private String appName;

    public Select(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("select", true);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("select", true);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jboname != null) {
            addParameter("jboname", findString(jboname).toUpperCase());
        }
        if (relationship != null) {
            addParameter("relationship", findString(relationship).toUpperCase());
        }
        if (wherecause != null) {
            addParameter("wherecause", findString(wherecause));
        }
        if (options != null) {
            addParameter("options", findString(options));
        }
        if (dataattribute != null) {
            addParameter("dataattribute", findString(dataattribute).toUpperCase());
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (inputmode != null) {
            String im = findString(inputmode).toUpperCase();
            addParameter("inputmode", im);
            if ("READONLY".equals(im)) {
                addParameter("readonly", true);
            }
        }
        if (parentName != null) {
            addParameter("parentName", findString(parentName).toUpperCase());
        }
        if (displayname != null) {
            addParameter("displayname", findString(displayname).toUpperCase());
        }
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle).toUpperCase());
        }
        if (orderby != null) {
            addParameter("orderby", findString(orderby));
        }
        if (partialTriggers != null) {
            addParameter("partialTriggers", findString(partialTriggers));
        }
        if (selected != null) {
            addParameter("selected", findString(selected));
        }
        if (selectedDisplay != null) {
            addParameter("selectedDisplay", selectedDisplay);
        }
        if (selectedJbo != null) {
            addParameter("selectedJbo", selectedJbo);
        }
        if (displayvalue != null) {
            addParameter("displayvalue", findString(displayvalue).toUpperCase());
        }
        if (partialCause != null) {
            addParameter("partialCause", findString(partialCause));
        }

        addParameter("sql", sql);
        if (cause != null) {
            addParameter("cause", findString(cause));
        }
        if (null != visible) {
            addParameter("visible", findValue(visible, Boolean.class));
        }

        if (null != required) {
            addParameter("required", findValue(required, Boolean.class));
        }

        if (null != colspan) {
            addParameter("colspan", findString(colspan));
        }

        if (null != width) {
            addParameter("width", findString(width));
        }

        if (null != label) {
            addParameter("label", getI18NValue(label));
        }
        addParameter("optionsList", optionsList);
        addParameter("jboset", jboset);

        if (!StrUtil.isNull(appName)) {
            addParameter("appBundle", JxLangResourcesUtil.getResourceBundle("res.app." + appName.toLowerCase()));
        }

        if (null != cssClass) {
            dynamicAttributes.put("cssClass", findString(cssClass));
        }
        if (null != render) {
            addParameter("render", findString(render));
        }
        if (null != renderExtends) {
            addParameter("renderExtends", findString(renderExtends));
        }
        if (null != renderParam) {
            addParameter("renderParam", findString(renderParam));
        }
        if (null != multiple) {
            addParameter("multiple", findValue(multiple, Boolean.class));
        }
        if (null != ajax) {
            String aj = findString(ajax);            
            if (aj != null) {
                if (aj.indexOf('\'') < 0 && aj.indexOf('"') < 0) {
                    aj = "\"" + aj + "\"";
                }
            }
            addParameter("ajax", aj);
        }
        if (optionsCount > 0) {
            addParameter("optionsCount", optionsCount);
        }
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public void setOptionsList(List<KeyValue> optionsList) {
        this.optionsList = optionsList;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setPartialTriggers(String partialTriggers) {
        this.partialTriggers = partialTriggers;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    public void setPartialCause(String partialCause) {
        this.partialCause = partialCause;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    @Override
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public void setRenderExtends(String renderExtends) {
        this.renderExtends = renderExtends;
    }

    public void setSelectedJbo(JboIFace selectedJbo) {
        this.selectedJbo = selectedJbo;
    }

    public void setRenderParam(String renderParam) {
        this.renderParam = renderParam;
    }

    public void setSelectedDisplay(String selectedDisplay) {
        this.selectedDisplay = selectedDisplay;
    }

    public List<KeyValue> getOptionsList() {
        return optionsList;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public void setOptionsCount(int optionsCount) {
        this.optionsCount = optionsCount;
    }
}
