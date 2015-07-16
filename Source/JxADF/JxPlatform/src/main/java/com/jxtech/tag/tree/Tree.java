package com.jxtech.tag.tree;

import com.jxtech.common.JxLoadResource;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */

@StrutsTag(name = "tree", tldTagClass = "com.jxtech.tag.tree.TreeTag", description = "tree")
public class Tree extends JxBaseUIBean {
    protected String jboname;// 表名
    protected String restrictions;// 限制条件
    protected String whereCause;// 初始条件
    protected String treeNodeKey;// 节点字段名
    protected String treeNodeParentKey;// 父亲节点字段名
    protected String treeNodeName;// 节点显示内容
    protected String checkable;// 每个节点上是否显示 CheckBox
    protected String treeUrl;// 动态获取数据的URL路径
    protected String treeNodes;// JSON格式的数据
    protected String hasChildName;// 是否有孩子节点的字段名
    protected String orderby;// 排序字段
    protected String async;
    protected String leafDisplay;// 是否显示叶子节点，true显示，否则不显示。
    protected String root; // 自定义根节点
    protected String relationship; // 关系名
    protected String i18n;

    protected String mode; // 模式 一般在lookup页面或者search的页面需要指定
    private static final Logger LOG = LoggerFactory.getLogger(Tree.class);

    public Tree(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
        JxLoadResource.loadTree(request);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tree/tree";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tree/tree-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("jboname", jboname);
        addParameter("restrictions", restrictions);
        addParameter("whereCause", whereCause);
        if (treeNodeKey != null)
            addParameter("treeNodeKey", treeNodeKey.toLowerCase());
        if (treeNodeParentKey != null)
            addParameter("treeNodeParentKey", treeNodeParentKey.toLowerCase());
        if (treeNodeName != null)
            addParameter("treeNodeName", treeNodeName.toLowerCase());
        addParameter("checkable", checkable);
        addParameter("treeUrl", treeUrl);
        addParameter("treeNodes", treeNodes);
        addParameter("hasChildName", hasChildName);
        addParameter("orderby", orderby);
        if (async != null)
            addParameter("async", async.toLowerCase());
        addParameter("leafDisplay", leafDisplay);
        addParameter("root", root);
        addParameter("relationship", relationship);
        if (null != mode) {
            addParameter("mode", mode.toUpperCase());
        }
        if (null != i18n) {
            addParameter("i18n", findString(i18n));
        }
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public void setTreeNodeKey(String treeNodeKey) {
        this.treeNodeKey = treeNodeKey;
    }

    public void setTreeNodeParentKey(String treeNodeParentKey) {
        this.treeNodeParentKey = treeNodeParentKey;
    }

    public void setTreeNodeName(String treeNodeName) {
        this.treeNodeName = treeNodeName;
    }

    public void setCheckable(String checkable) {
        this.checkable = checkable;
    }

    public void setTreeUrl(String treeUrl) {
        this.treeUrl = treeUrl;
    }

    public void setTreeNodes(String treeNodes) {
        this.treeNodes = treeNodes;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public void setHasChildName(String hasChildName) {
        this.hasChildName = hasChildName;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public void setLeafDisplay(String leafDisplay) {
        this.leafDisplay = leafDisplay;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }

}
