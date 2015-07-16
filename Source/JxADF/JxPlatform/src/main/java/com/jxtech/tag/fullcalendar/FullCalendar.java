package com.jxtech.tag.fullcalendar;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JQuery 之 fullCalendar控件再包装
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.04
 */
@StrutsTag(name = "fullCalendar", tldTagClass = "com.jxtech.tag.fullcalendar.FullCalendarTag", description = "FullCalendar")
public class FullCalendar extends JxBaseUIBean {
    protected String calendarEvent;// JSON格式的内容{日历属性1：字段名称1,日历属性2:字段名称2}
    protected String jboname;
    protected String wherecause;
    protected String startDate;// 开始时间，这个主要用来进行数据加载条件
    private JboSetIFace jboset;
    private String loadjs;
    protected String month;// 查询几月的数据，格式：yyyy-mm

    public FullCalendar(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "fullCalendar/close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "fullCalendar/open";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jboname != null) {
            addParameter("jboname", findString(jboname));
        }
        addParameter("startDate", startDate);
        if (StrUtil.isNull(month)) {
            month = DateUtil.dateToString(null, "yyyy-MM");
            addParameter("month", month);
        } else {
            addParameter("month", findString(month));
        }
        if (calendarEvent != null) {
            addParameter("calendarEvent", calendarEvent);
        }
        if (loadjs != null) {
            addParameter("loadjs", findValue(loadjs, Boolean.class));
        }
        addParameter("jboset", jboset);
    }

    public void setCalendarEvent(String calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setLoadjs(String loadjs) {
        this.loadjs = loadjs;
    }
}
