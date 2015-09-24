package com.jxtech.tag.tree;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class TreeTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(TreeTag.class);
    private static final long serialVersionUID = 5957658341257943881L;
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
    protected String async;// 是否异步获得数据
    protected String leafDisplay;// 是否显示叶子节点，false不显示，否则为显示。
    protected String root; // 自定义根节点
    protected String relationship;// 关系
    protected String i18n; // 国际化

    protected String moreAttributes;//返回的json中包含更多的属性

    protected String mode; // 模式 一般在lookup页面或者search页面需要指定

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Tree(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Tree tree = (Tree) component;
        tree.setJboname(jboname);
        tree.setRestrictions(restrictions);
        tree.setTreeNodeKey(treeNodeKey);
        tree.setTreeNodeName(treeNodeName);
        tree.setTreeNodeParentKey(treeNodeParentKey);
        tree.setCheckable(checkable);
        tree.setTreeUrl(treeUrl);
        tree.setHasChildName(hasChildName);
        tree.setOrderby(orderby);
        tree.setAsync(async);
        tree.setLeafDisplay(leafDisplay);
        tree.setRoot(getRoot());
        tree.setRelationship(relationship);
        tree.setMode(mode);
        tree.setI18n(i18n);
        try {
            setData(tree);
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * 读取树的数据
     *
     * @param tree
     */
    protected void setData(Tree tree) throws JxException {
        if (StrUtil.isNull(treeNodeKey) || StrUtil.isNull(treeNodeName) || StrUtil.isNull(treeNodeParentKey)) {
            return;
        }
        if (StrUtil.isNull(jboname)) {
            return;
        }

        JboSetIFace js = null;
        App myapp = JxSession.getApp();
        if (StrUtil.isNull(relationship)) {
            js = JboUtil.getJboSet(jboname);
        } else {

            if (myapp != null) {
                JboIFace jbo = myapp.getJbo();
                if (null != jbo) {
                    js = jbo.getRelationJboSet(relationship);
                }

            }
        }

        if (js != null) {
            if (myapp != null) {
                if (null == mode || !"ALONE".equalsIgnoreCase(mode)) {
                    myapp.setJboset(js);
                }
            }

            js.setAppname(JxSession.getAppNameOfJboname(jboname, false));
            DataQueryInfo dq = js.getQueryInfo();
            dq.setWhereCause(whereCause);
            dq.setRestrictions(restrictions);
            dq.setOrderby(orderby);
            js.queryAll();

            List<JboIFace> jboList = js.getJbolist();
            if (!jboList.isEmpty()) {
                js.setJbo(jboList.get(0));
            }

            String[] attributes = new String[]{treeNodeKey, treeNodeParentKey, treeNodeName};
            if (!StrUtil.isNull(moreAttributes)) {
                String[] moreAtts = moreAttributes.split(",");
                attributes = ArrayUtils.addAll(attributes, moreAtts);

            }

            treeNodes = js.toZTreeJson(attributes, treeNodeKey, treeNodeParentKey, treeNodeName, hasChildName, !"false".equalsIgnoreCase(leafDisplay), getRoot(), getI18n());
            tree.setTreeNodes(treeNodes);
        }
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getTreeNodeKey() {
        return treeNodeKey;
    }

    public void setTreeNodeKey(String treeNodeKey) {
        this.treeNodeKey = treeNodeKey;
    }

    public String getTreeNodeParentKey() {
        return treeNodeParentKey;
    }

    public void setTreeNodeParentKey(String treeNodeParentKey) {
        this.treeNodeParentKey = treeNodeParentKey;
    }

    public String getTreeNodeName() {
        return treeNodeName;
    }

    public void setTreeNodeName(String treeNodeName) {
        this.treeNodeName = treeNodeName;
    }

    public String getCheckable() {
        return checkable;
    }

    public void setCheckable(String checkable) {
        this.checkable = checkable;
    }

    public String getTreeUrl() {
        return treeUrl;
    }

    public void setTreeUrl(String treeUrl) {
        this.treeUrl = treeUrl;
    }

    public String getTreeNodes() {
        return treeNodes;
    }

    public void setTreeNodes(String treeNodes) {
        this.treeNodes = treeNodes;
    }

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public String getHasChildName() {
        return hasChildName;
    }

    public void setHasChildName(String hasChildName) {
        this.hasChildName = hasChildName;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String getLeafDisplay() {
        return leafDisplay;
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

    public String getMode() {
        return mode;
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

    public String getMoreAttributes() {
        return moreAttributes;
    }

    public void setMoreAttributes(String moreAttributes) {
        this.moreAttributes = moreAttributes;
    }
}
