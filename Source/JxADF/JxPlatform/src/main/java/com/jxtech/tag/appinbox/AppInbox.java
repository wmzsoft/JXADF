package com.jxtech.tag.appinbox;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 * 
 */
@StrutsTag(name = "appinbox", tldTagClass = "com.jxtech.tag.appinbox.AppInboxTag", description = "appinbox")
public class AppInbox extends JxBaseUIBean {

    protected String fragmentId;// 查询之后，显示数据到哪里
    protected String statusLabel;// 状态标签
    protected String statusColumn;// 状态字段，默认为AUDIT_STATUS
    protected String creatorColumn;// 创建记录的用户ID
    protected String transactorColumn;// 工作流当前执行人字段,原则上定义为： WFT_TRANSACTOR_ID
    protected String backflagColumn;// 回退标记
    protected String firstActTaskId;// 第一个任务节点
    protected String nodeLabel;// 工作流节点标签
    protected List<JboIFace> nodeList;// 节点数据
    protected String status;// 当前选中的状态
    protected String nodeId;// 当前选中的节点ID。
    protected String disnode;// true：显示节点下拉列表，false：隐藏节点下拉列表(默认)
    protected String disroute;// true：显示发送工作流按钮，false：隐藏发送工作流按钮(默认)

    public AppInbox(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate( "appinbox", false);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("appinbox", false);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (fragmentId != null) {
            addParameter("fragmentId", findString(fragmentId));
        }
        if (statusLabel != null) {
            addParameter("statusLabel", findString(statusLabel));
        }
        if (nodeLabel != null) {
            addParameter("nodeLabel", findString(nodeLabel));
        }
        if (transactorColumn != null) {
            addParameter("transactorColumn", findString(transactorColumn));
        }
        if (creatorColumn != null) {
            addParameter("creatorColumn", findString(creatorColumn));
        }
        if (backflagColumn != null) {
            addParameter("backflagColumn", findString(backflagColumn));
        }
        addParameter("nodeList", nodeList);
        addParameter("status", status);
        addParameter("nodeId", nodeId);
        if (disnode != null) {
            addParameter("disnode", findValue(disnode, Boolean.class));
        }
        if (disroute != null) {
            addParameter("disroute", findValue(disroute, Boolean.class));
        }
        addParameter("firstActTaskId", firstActTaskId);
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public void setStatusColumn(String statusColumn) {
        this.statusColumn = statusColumn;
    }

    public void setCreatorColumn(String creatorColumn) {
        this.creatorColumn = creatorColumn;
    }

    public void setTransactorColumn(String transactorColumn) {
        this.transactorColumn = transactorColumn;
    }

    public void setNodeList(List<JboIFace> nodeList) {
        this.nodeList = nodeList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setBackflagColumn(String backflagColumn) {
        this.backflagColumn = backflagColumn;
    }

    public void setFirstActTaskId(String firstActTaskId) {
        this.firstActTaskId = firstActTaskId;
    }

    public void setDisnode(String disnode) {
        this.disnode = disnode;
    }

    public void setDisroute(String disroute) {
        this.disroute = disroute;
    }

}
