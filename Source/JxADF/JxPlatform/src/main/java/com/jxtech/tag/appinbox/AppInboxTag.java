package com.jxtech.tag.appinbox;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;
import java.util.List;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.09
 */
public class AppInboxTag extends JxBaseUITag {

    private static final long serialVersionUID = -8768261981605668620L;
    private static final Logger LOG = LoggerFactory.getLogger(AppInboxTag.class);

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

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new AppInbox(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("appinbox")) {
            return;
        }
        super.populateParams();
        AppInbox inbox = (AppInbox) component;
        inbox.setFragmentId(fragmentId);
        inbox.setStatusLabel(statusLabel);
        inbox.setStatusColumn(statusColumn);
        inbox.setCreatorColumn(creatorColumn);
        inbox.setTransactorColumn(transactorColumn);
        inbox.setBackflagColumn(backflagColumn);
        inbox.setNodeLabel(nodeLabel);
        inbox.setDisnode(disnode);
        inbox.setDisroute(disroute);
        Tag tag = findAncestorWithClass(getParent(), BodyTag.class);
        App myapp = null;
        String appName = null;
        if (tag != null) {
            BodyTag body = (BodyTag) tag;
            appName = body.getAppName();
            try {
                myapp = JxSession.getApp(appName, body.getAppType());
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
        }
        if (myapp != null) {
            JboSetIFace js = myapp.getJboset();
            if (js != null) {
                if (!StrUtil.isNull(appName)) {
                    js.setAppname(appName);
                }
            }

        }
    }

    public String getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(String fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getStatusColumn() {
        return statusColumn;
    }

    public void setStatusColumn(String statusColumn) {
        this.statusColumn = statusColumn;
    }

    public String getCreatorColumn() {
        return creatorColumn;
    }

    public void setCreatorColumn(String creatorColumn) {
        this.creatorColumn = creatorColumn;
    }

    public String getTransactorColumn() {
        return transactorColumn;
    }

    public void setTransactorColumn(String transactorColumn) {
        this.transactorColumn = transactorColumn;
    }

    public List<JboIFace> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<JboIFace> nodeList) {
        this.nodeList = nodeList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getBackflagColumn() {
        return backflagColumn;
    }

    public void setBackflagColumn(String backflagColumn) {
        this.backflagColumn = backflagColumn;
    }

    public String getFirstActTaskId() {
        return firstActTaskId;
    }

    public void setFirstActTaskId(String firstActTaskId) {
        this.firstActTaskId = firstActTaskId;
    }

    public String getDisnode() {
        return disnode;
    }

    public void setDisnode(String disnode) {
        this.disnode = disnode;
    }

    public String getDisroute() {
        return disroute;
    }

    public void setDisroute(String disroute) {
        this.disroute = disroute;
    }

}
