package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.UrlUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JTableTag extends JxBaseUITag {
    private static final long serialVersionUID = -5120286110694744395L;
    private List<JTableCol> cols = new ArrayList<JTableCol>(); // 定义的列信息
    private List<JTableButton> buttons = new ArrayList<JTableButton>();// 定义的按钮信息
    private String visibleHead;// 是否显示页头
    private String title;// 表头显示的内容
    private String jsonUrl;// 从URL中获得JSON数据
    private String jsonData;// 根据URL获得的具体JSON数据,如果url为空，则取自定义的值
    private List<Map<String, Object>> mapData;// 将数据转换为List<MAP<String,Objecct>>的格式

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JTable(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        JTable table = (JTable) component;
        table.setCols(cols);
        table.setButtons(buttons);
        table.setVisibleHead(visibleHead);
        table.setTitle(title);
        table.setJsonUrl(jsonUrl);
        if (!StrUtil.isNull(jsonUrl)) {
            // 读取JSON数据
            Object str = UrlUtil.getUrlContent(jsonUrl);
            if (str != null) {
                jsonData = (String) str;
                mapData = JsonUtil.fromJson(jsonData);
            }
        }
        if (!StrUtil.isNull(jsonData) && mapData == null) {
            mapData = JsonUtil.fromJson(jsonData);
        }
        table.setMapData(mapData);
        table.setJsonData(jsonData);

    }

    public List<JTableCol> getCols() {
        return cols;
    }

    public void setCols(List<JTableCol> cols) {
        this.cols = cols;
    }

    public List<JTableButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<JTableButton> buttons) {
        this.buttons = buttons;
    }

    public String getVisibleHead() {
        return visibleHead;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public List<Map<String, Object>> getMapData() {
        return mapData;
    }

    public void setMapData(List<Map<String, Object>> mapData) {
        this.mapData = mapData;
    }

}
