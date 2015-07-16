package com.jxtech.tag.gantt;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.02
 * 
 */
public class Gantt extends JxBaseUIBean {

    // Gantt的基本设置信息
    private String columns;// 表格中需要显示的列
    private String scale;// 默认值：week
    private String dateScale;// 默认值：%W
    private String step;// 间隔步长，如果scale为周，step为2，则表示每格间隔2周。
    private String width;// 整个Gantt图的宽度
    private String height;// 整个Gantt图的高度
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

    // 基本信息

    // 数据
    private String jboname;
    private String whereCause;
    private String orderby;
    private JboSetIFace jboset;

    public Gantt(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "gantt/gantt";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "gantt/gantt-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (columns != null) {
            this.addParameter("columns", findString(columns));
        }
        if (scale != null) {
            this.addParameter("scale", findString(scale));
        }
        if (dateScale != null) {
            this.addParameter("dateScale", findString(dateScale));
        }
        if (step != null) {
            this.addParameter("step", findString(step));
        }
        if (taskId != null) {
            this.addParameter("taskId", findString(taskId));
        }
        if (name != null) {
            this.addParameter("name", findString(name));
        }
        if (start != null) {
            this.addParameter("start", findString(start));
        }
        if (finish != null) {
            this.addParameter("finish", findString(finish));
        }
        if (predecessors != null) {
            this.addParameter("predecessors", findString(predecessors));
        }
        if (duration != null) {
            this.addParameter("duration", findString(duration));
        }
        if (label != null) {
            this.addParameter("label", findString(label));
        }
        if (progress != null) {
            this.addParameter("progress", findString(progress));
        }
        if (resource != null) {
            this.addParameter("resource", findString(resource));
        }
        if (parentId != null) {
            this.addParameter("parentId", findString(parentId));
        }
        if (wbs != null) {
            this.addParameter("wbs", findString(wbs));
        }
        if (priority != null) {
            addParameter("priority", findString(priority));
        }
        if (width != null) {
            addParameter("width", findString(width));
        }
        if (height != null) {
            addParameter("height", findString(height));
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (jboname != null) {
            addParameter("jboname", findString(jboname));
        }
        if (whereCause != null) {
            addParameter("whereCause", findString(whereCause));
        }
        if (orderby != null) {
            addParameter("orderby", findString(orderby));
        }
        if (jboset != null) {
            addParameter("jboset", jboset);
        }
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setPredecessors(String predecessors) {
        this.predecessors = predecessors;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setDateScale(String dateScale) {
        this.dateScale = dateScale;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

}
