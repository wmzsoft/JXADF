package com.jxtech.tag.gantt;

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
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 * 
 */
public class GanttTag extends JxBaseUITag {
    // Gantt的基本设置信息
    private String columns;// 表格中需要显示的列
    private String scale;// 默认值：week
    private String dateScale;// 默认值：%W
    private String step;// 间隔步长，如果scale为周，step为2，则表示每格间隔2周。
    private String width;// 整个Gantt图的宽度
    private String height; // 整个Gantt图的高度
    private String readonly;

    // Gantt的数据项设置
    private String taskId;// 任务号
    private String name;// 任务名
    private String start;// 开始时间
    private String finish;// 完成时间
    private String predecessors;// 前置任务
    private String duration;// 工期
    private String progress;// 完成进度
    private String resource;// 资源名称
    private String parentId;// 父任务
    private String wbs;// WBS代码
    private String priority;// 优先级

    // 数据
    private String jboname;
    private String whereCause;
    private String orderby;
    private JboSetIFace jboset;

    /**
     * 
     */
    private static final long serialVersionUID = -7400379139254254728L;
    private static final Logger LOG = LoggerFactory.getLogger(GanttTag.class);

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Gantt(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Gantt gantt = (Gantt) component;
        gantt.setColumns(columns);
        gantt.setScale(scale);
        gantt.setDateScale(dateScale);
        gantt.setStep(step);

        gantt.setTaskId(taskId);
        gantt.setName(name);
        gantt.setStart(start);
        gantt.setFinish(finish);
        gantt.setPredecessors(predecessors);
        gantt.setPriority(priority);
        gantt.setWbs(wbs);
        gantt.setDuration(duration);
        gantt.setProgress(progress);
        gantt.setResource(resource);
        gantt.setParentId(parentId);

        gantt.setWidth(width);
        gantt.setHeight(height);
        gantt.setReadonly(readonly);

        if (!StrUtil.isNull(jboname)) {
            try {
                jboset = JboUtil.getJboSet(jboname);
                DataQueryInfo dqi = jboset.getQueryInfo();
                dqi.setWhereCause(whereCause);
                dqi.setPageSize(-1);
                dqi.setOrderby(orderby);
                jboset.query();
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
        }
        gantt.setJboset(jboset);
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(String predecessors) {
        this.predecessors = predecessors;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getDateScale() {
        return dateScale;
    }

    public void setDateScale(String dateScale) {
        this.dateScale = dateScale;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getReadonly() {
        return readonly;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

}
