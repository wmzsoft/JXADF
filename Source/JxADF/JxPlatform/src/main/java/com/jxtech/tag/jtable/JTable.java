package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
@StrutsTag(name = "jtable", tldTagClass = "com.jxtech.tag.jtable.JTableTag", description = "JTable")
public class JTable extends JxBaseUIBean {
    private List<JTableCol> cols; // 定义的列信息
    private List<JTableButton> buttons;// 定义的按钮信息
    private String visibleHead;// 是否显示页头
    private String title;// 表头显示的内容
    private String jsonUrl;// 从URL中获得JSON数据
    private String jsonData;// 根据URL获得的具体JSON数据
    private List<Map<String, Object>> mapData;// 将数据转换为List<MAP<String,Objecct>>的格式

    public JTable(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "jtable/jtable-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "jtable/jtable.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (cols != null) {
            addParameter("cols", cols);
        }
        if (buttons != null) {
            addParameter("buttons", buttons);
        }
        if (title != null) {
            addParameter("title", findString(title));
        }
        if (visibleHead != null) {
            addParameter("visibleHead", findValue(visibleHead, Boolean.class));
        }
        if (jsonUrl != null) {
            addParameter("jsonUrl", findString(jsonUrl));
        }
        if (jsonData != null) {
            addParameter("jsonData", jsonData);
        }
        if (mapData != null) {
            addParameter("mapData", mapData);
        }
    }

    public void setCols(List<JTableCol> cols) {
        this.cols = cols;
    }

    public void setButtons(List<JTableButton> buttons) {
        this.buttons = buttons;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setMapData(List<Map<String, Object>> mapData) {
        this.mapData = mapData;
    }

}
