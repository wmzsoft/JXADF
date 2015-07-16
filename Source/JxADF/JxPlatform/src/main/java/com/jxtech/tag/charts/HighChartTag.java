package com.jxtech.tag.charts;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.SqlParserUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 再次包装HighChart
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 * 
 */
public class HighChartTag extends JxBaseUITag {

    private static final long serialVersionUID = 8133953017208924736L;
    private static final Logger LOG = LoggerFactory.getLogger(HighChartTag.class);
    public final static int TYPE_STRING = 1;// 字符串类型
    public final static int TYPE_ARRAY = 2;// 数组类型
    public final static int TYPE_JSON = 3;// JSON类型

    private String chart;// 设定Chart类型，JSON格式，详细参见HighChart官网说明
    private String colors;// 颜色，JSON格式，详细参见HighChart官网说明
    private String title;// 标题，JSON格式，详细参见HighChart官网说明
    private String subtitle;// 子标题，JSON格式，详细参见HighChart官网说明
    private String xAxis;// X轴，支持SQL脚本，JSON格式，详细参见HighChart官网说明
    private String yAxis;// Y轴，支持SQL脚本，JSON格式，详细参见HighChart官网说明
    private String tooltip;// 提示，JSON格式，详细参见HighChart官网说明
    private String plotOptions;// 点选项，JSON格式，详细参见HighChart官网说明
    private String series;// 数据，支持SQL脚本，JSON格式，详细参见HighChart官网说明
    private String legend;// 图例，JSON格式，详细参见HighChart官网说明
    private String credits;// credits，JSON格式，详细参见HighChart官网说明
    private String functions;// 函数，详细参见HighChart官网说明
    private String exporting;// 导出功能选项，详细参见HighChart官网说明
    private String labels;// HTML 标签，详细参见HighChart官网说明
    private String loading;// loading 选项控制图表加载时显示文字及样式，详细参见HighChart官网说明
    private String navigation;// 导出模块按钮和菜单配置选项组,详细参见HighChart官网说明
    private String pane;// 只用在极地图和角仪表中，详细参见HighChart官网说明

    private String width;// 图表宽度
    private String height;// 图表高度

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new HighChart(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("highchart")) {
            return;
        }
        super.populateParams();
        HighChart hc = (HighChart) component;
        hc.setChart(getData(chart));
        hc.setColors(colors);
        hc.setTitle(getData(title));
        hc.setSubtitle(getData(subtitle));
        hc.setxAxis(getData(xAxis, TYPE_ARRAY));
        hc.setyAxis(getData(yAxis, TYPE_ARRAY));
        hc.setTooltip(getData(tooltip));
        hc.setPlotOptions(getData(plotOptions));
        hc.setSeries(getDataSeries(series));
        hc.setLegend(getData(legend));
        hc.setCredits(getData(credits));
        hc.setFunctions(getData(functions));
        hc.setExporting(getData(exporting));
        hc.setLabels(getData(labels));
        hc.setLoading(getData(loading));
        hc.setNavigation(getData(navigation));
        hc.setPane(getData(pane));
        hc.setWidth(width);
        hc.setHeight(height);
    }

    /**
     * 将传入的数据进行处理，如果有SQL脚本，则进行翻译为具体的数据。
     * 
     * @param str
     * @return
     */
    protected String getData(String str) {
        return getData(str, TYPE_STRING);
    }

    protected String getData(String str, int flag) {
        if (StrUtil.isNull(str)) {
            return null;
        }
        try {
            JSONObject json = JSONObject.fromObject(str);
            if (json == null) {
                return str;
            }
            for (Iterator iter = json.keys(); iter.hasNext();) {
                String key = (String) iter.next();
                String sql = json.getString(key);
                if (SqlParserUtil.isSelectSql(sql)) {
                    String val = getSqlData(sql, flag);
                    json.put(key, val);
                }

            }
            return json.toString();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        return str;
    }

    protected String getSqlData(String sql, int flag) throws JxException {
        if (StrUtil.isNull(sql)) {
            return null;
        }
        if (SqlParserUtil.isSelectSql(sql)) {
            DataQuery dq = DBFactory.getDataQuery(null, null);
            List<Map<String, Object>> data = dq.getResultSet(sql, null);
            if (flag == TYPE_STRING) {
                return JsonUtil.getStringOfFirstValue(data);
            } else if (flag == TYPE_ARRAY) {
                return JsonUtil.getArrayOfValue(data, sql);
            } else if (flag == TYPE_JSON) {

            }
        }
        return sql;
    }

    protected String getDataSeries(String str) {
        if (StrUtil.isNull(str)) {
            return null;
        }
        try {
            JSONArray json = JSONArray.fromObject(str);
            if (json == null) {
                return str;
            } else {
                int len = json.size();
                for (int i = 0; i < len; i++) {
                    JSONObject jso = json.getJSONObject(i);

                    // 如果Data为SQL脚本
                    String sql = jso.getString("data");
                    String name = jso.getString("name");
                    if (name != null && name.startsWith("$SQLDATA_") && SqlParserUtil.isSelectSql(sql)) {
                        // 如果name=$SQLDATA，并且data为SQL
                        // 解析为：[{ name: 'Tokyo',data: [7.0, 6.9, 9.5]}, {name: 'London',data: [3.9, 4.2, 5.7]}]
                        DataQuery dq = DBFactory.getDataQuery(null, null);
                        List<Map<String, Object>> data = dq.getResultSet(sql, null);
                        if (data != null) {
                            int dc = data.size();
                            if (dc > 0) {
                                String[] columns = SqlParserUtil.getColumns(sql);// 字段信息
                                if ("$SQLDATA_COLUMN".equals(name)) {
                                    // 将Series按列解析
                                    for (int k = 0; k < columns.length; k++) {
                                        JSONObject njso = JSONObject.fromObject(jso);
                                        njso.put("name", columns[k]);
                                        List<Object> lv = new ArrayList<Object>();
                                        for (int m = 0; m < dc; m++) {
                                            Map<String, Object> dto = data.get(m);// 单条记录
                                            lv.add(dto.get(columns[k]));
                                        }
                                        njso.put("data", JSONArray.fromObject(lv).toString());
                                        if (k == 0) {
                                            json.remove(i);
                                            json.add(i, njso);
                                        } else {
                                            json.add(njso);
                                        }
                                    }
                                } else {
                                    // 将Series按行解析
                                    for (int j = 0; j < dc; j++) {
                                        Map<String, Object> dto = data.get(j);// 单条记录
                                        JSONObject njso = JSONObject.fromObject(jso);
                                        if (columns != null) {
                                            njso.put("name", dto.get(columns[0]));
                                            List<Object> lv = new ArrayList<Object>();
                                            for (int m = 1; m < columns.length; m++) {
                                                lv.add(dto.get(columns[m]));
                                            }
                                            njso.put("data", JSONArray.fromObject(lv).toString());
                                            if (j == 0) {
                                                json.remove(i);
                                                json.add(i, njso);
                                            } else {
                                                json.add(njso);
                                            }
                                        }
                                    }
                                }
                            } else {
                                jso.put("name", null);
                                jso.put("data", null);
                            }
                        } else {
                            jso.put("name", null);
                            jso.put("data", null);
                        }
                    } else {
                        json.remove(i);
                        json.add(i, getData(jso.toString(), TYPE_ARRAY));
                    }
                }
            }
            return json.toString();

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        return str;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getTooltip() {
        return tooltip;
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getPlotOptions() {
        return plotOptions;
    }

    public void setPlotOptions(String plotOptions) {
        this.plotOptions = plotOptions;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getExporting() {
        return exporting;
    }

    public void setExporting(String exporting) {
        this.exporting = exporting;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getLoading() {
        return loading;
    }

    public void setLoading(String loading) {
        this.loading = loading;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getPane() {
        return pane;
    }

    public void setPane(String pane) {
        this.pane = pane;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
