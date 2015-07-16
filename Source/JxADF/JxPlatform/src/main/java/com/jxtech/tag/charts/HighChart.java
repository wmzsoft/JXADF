package com.jxtech.tag.charts;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 再次包装HighChart
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 * 
 */
@StrutsTag(name = "HighChart", tldTagClass = "com.jxtech.tag.charts.HighChartTag", description = "HighChart")
public class HighChart extends JxBaseUIBean {

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

    public HighChart(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("highchart", false);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("highchart", false);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (chart != null) {
            addParameter("chart", findString(chart));
        }
        if (colors != null) {
            addParameter("colors", findString(colors));
        }
        if (title != null) {
            addParameter("title", findString(title));
        }
        if (subtitle != null) {
            addParameter("subtitle", findString(subtitle));
        }
        if (xAxis != null) {
            addParameter("xAxis", findString(xAxis));
        }
        if (yAxis != null) {
            addParameter("yAxis", findString(yAxis));
        }
        if (tooltip != null) {
            addParameter("tooltip", findString(tooltip));
        }
        if (plotOptions != null) {
            addParameter("plotOptions", findString(plotOptions));
        }
        if (series != null) {
            addParameter("series", findString(series));
        }
        if (legend != null) {
            addParameter("legend", findString(legend));
        }
        if (credits != null) {
            addParameter("credits", findString(credits));
        }
        if (functions != null) {
            addParameter("functions", findString(functions));
        }
        if (exporting != null) {
            addParameter("exporting", findString(exporting));
        }
        if (labels != null) {
            addParameter("labels", findString(labels));
        }
        if (loading != null) {
            addParameter("loading", findString(loading));
        }
        if (navigation != null) {
            addParameter("navigation", findString(navigation));
        }
        if (pane != null) {
            addParameter("pane", findString(pane));
        }
        if (width != null) {
            addParameter("width", findString(width));
        }
        if (height != null) {
            addParameter("height", findString(height));
        }
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void setPlotOptions(String plotOptions) {
        this.plotOptions = plotOptions;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public void setExporting(String exporting) {
        this.exporting = exporting;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setLoading(String loading) {
        this.loading = loading;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public void setPane(String pane) {
        this.pane = pane;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
