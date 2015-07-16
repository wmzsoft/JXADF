package com.jxtech.tag.fullcalendar;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JQuery 之 fullCalendar控件再包装
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.04
 */

public class FullCalendarTag extends JxBaseUITag {

    /**
     * 
     */
    private static final long serialVersionUID = 5617955817017954977L;
    private static final Logger LOG = LoggerFactory.getLogger(FullCalendarTag.class);

    protected String calendarEvent;// JSON格式的内容{日历属性1：字段名称1,日历属性2:字段名称2}
    protected String jboname;
    protected String wherecause;
    protected String startDate;// 字段名：开始时间，这个主要用来进行数据加载条件
    private JboSetIFace jboset;
    private String loadjs;
    // 以下是URL中的参数信息
    protected String month;// 查询几月的数据，格式：yyyy-mm

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        month = request.getParameter("month");
        //JxLoadResource.loadFullCalendar(request);
        return new FullCalendar(stack, request, response);
    }

    protected void populateParams() {
        super.populateParams();
        FullCalendar cal = (FullCalendar) component;
        cal.setJboname(jboname);
        cal.setCalendarEvent(calendarEvent);
        cal.setWherecause(wherecause);
        cal.setStartDate(startDate);
        cal.setLoadjs(loadjs);
        cal.setMonth(month);
        if (!StrUtil.isNull(jboname)) {
            try {
                jboset = JboUtil.getJboSet(jboname);
            } catch (JxException e1) {
                LOG.error(e1.getMessage(),e1);
            }
            if (jboset != null) {
                DataQueryInfo qi = jboset.getQueryInfo();
                qi.setWhereCause(wherecause);
                qi.putParams("to_char(" + startDate + ",'yyyy-MM')=?", month);
                try {
                    jboset.query();
                } catch (JxException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        cal.setJboset(jboset);
    }

    public String getCalendarEvent() {
        return calendarEvent;
    }

    public void setCalendarEvent(String calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getWherecause() {
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLoadjs() {
        return loadjs;
    }

    public void setLoadjs(String loadjs) {
        this.loadjs = loadjs;
    }
}
