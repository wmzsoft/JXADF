package com.jxtech.jbo;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxApps;
import com.jxtech.jbo.base.JxAppsDao;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.field.FieldIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import com.jxtech.workflow.base.IWorkflowEngine;
import com.jxtech.workflow.base.WorkflowBaseInfo;
import com.jxtech.workflow.base.WorkflowEngineFactory;
import com.jxtech.workflow.option.WftParam;
import org.directwebremoting.io.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述单个应用程序的所有信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class App {
    private Logger LOG = LoggerFactory.getLogger(App.class);
    private String appName;// 当前应用程序E文名
    private String appType;// 应用程序类型
    // MaxApps表中对应的记录信息
    private JxApps jxapps;
    // 主对象结果集
    private JboSetIFace jboset;
    private String msg; // 当前应用的提示消息

    private List<WftParam> wftParam; // 工作流发送界面扩展

    private Map<String, App> refApp = new HashMap<String, App>();

    public App(String appname, String appType) throws JxException {
        this.appName = appname;
        this.appType = appType;
        jxapps = JxAppsDao.getApp(appname);
        if (jxapps != null && jxapps.getMaintbname() != null) {
            jboset = JboUtil.getJboSet(jxapps.getMaintbname());
            if (jboset != null) {
                jboset.getQueryInfo().setRestrictions(jxapps.getRestrictions());
                jboset.setAppname(appName);
            }
        }
    }

    public JboSetIFace save() throws JxException {
        if (jboset != null) {
            if (jboset.commit()) {
                return jboset;
            } else {
                throw new JxException(JxLangResourcesUtil.getString("app.jboset.save.error") + appName);
            }
        } else {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.save.error") + appName + " jboset is null.");
        }
    }

    /**
     * 上一条
     * 
     * @return
     * @throws JxException
     */
    public String previous() throws JxException {
        if (jboset != null) {
            JboIFace jbo = getJbo();
            if (jbo != null) {
                JboIFace pJbo = JboUtil.getSlibingJbo(jbo, JxConstant.SLIBING_PREVIOUS);

                if (null != pJbo) {
                    return pJbo.getUidValue();
                }
            }
        } else {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.previous.not.find") + appName);
        }

        return "";
    }

    /**
     * 上一条
     * 
     * @return
     * @throws JxException
     */
    public String next() throws JxException {
        if (jboset != null) {
            JboIFace jbo = getJbo();
            if (jbo != null) {
                JboIFace nJbo = JboUtil.getSlibingJbo(jbo, JxConstant.SLIBING_NEXT);

                if (null != nJbo) {
                    return nJbo.getUidValue();
                }
            }
        } else {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.next.not.find") + appName);
        }

        return "";
    }

    /**
     * 回滚数据
     * 
     * @return
     * @throws JxException
     */
    public boolean rollback() throws JxException {
        if (jboset != null) {
            return jboset.rollback();
        } else {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.null.operation.failure") + " jboset is null." + appName + "." + appType);
        }
    }

    public boolean canJboSetAdd() throws JxException {
        boolean result = false;
        if (jboset == null) {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.null.jbosetadd.failure") + appName + "." + appType);
        }
        result = jboset.canAdd();

        return result;
    }

    public FileTransfer expExcel(String title, String uids) throws JxException {
        if (jboset == null) {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.null.expexcel.failure"));
        }
        return jboset.expExcel(title, uids);
    }

    /**
     * 导出之前预处理一下 检查数据是否过多 提示页面是否导出
     * 
     * @return 是否需要提示
     * @throws JxException
     */
    public int preExpExcel() throws JxException {
        if (jboset == null) {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.null.preexpexcel.failure"));
        }
        // 这里暂时写500行 以后扩展到配置文件里面配置
        int count = jboset.getCount();
        return count >= 500 ? count : 0;
    }

    public JboIFace add() throws JxException {
        if (jboset == null) {
            throw new JxException(JxLangResourcesUtil.getString("app.jboset.null.add.failure") + appName + "." + appType);
        }
        return jboset.add();
    }

    /**
     * 设定主记录的字段值
     * 
     * @param attributeName
     * @param value
     * @return
     * @throws JxException
     * @description 先取mainApp
     */
    public JboIFace setValue(String attributeName, String value, String relationship) throws JxException {
        App mainApp = JxSession.getMainApp();

        JboIFace jbo = null;
        FieldIFace field = null;
        if (jboset != null) {
            jbo = jboset.getJbo();
            if (!StrUtil.isNull(relationship) && this == mainApp) {
                JboSetIFace RJboSet = jbo.getRelationJboSet(relationship);
                if (null != RJboSet) {
                    jbo = RJboSet.getJbo();
                }
            }
            if (!StrUtil.isNull(attributeName)) {
                // 如果是relationship字段，将值设置到relationship的jbo中
                if (attributeName.indexOf(".") > 0) {
                    String[] relationshipAttributes = attributeName.split("\\.");
                    String relationshipname = relationshipAttributes[0];
                    String rAttributeName = relationshipAttributes[1];

                    JboSetIFace relationshipJboSet = jbo.getRelationJboSet(relationshipname);
                    JboIFace rJbo = relationshipJboSet.getJbo();

                    if (null == rJbo) {
                        throw new JxException(JxLangResourcesUtil.getString("app.setvalue.relationshipjbo.null") + appName + "." + appType);
                    }
                    field = JboUtil.getField(rJbo, rAttributeName);
                    if (null != field) {
                        field.execute(rAttributeName, value);
                    } else {
                        rJbo.setObject(rAttributeName, value);
                    }
                } else {
                    field = JboUtil.getField(jbo, attributeName);
                    if (null != field) {
                        field.execute(attributeName, value);
                    } else {
                        jbo.setObject(attributeName, value);
                    }
                }
            }
        }
        return jbo;
    }

    /**
     * 用于设定子表中某字段的值
     * 
     * @param jboname
     * @param relationship
     * @param uid 子表的唯一关键字
     * @param attributeName 字段名
     * @param value 字段值
     * @return
     * @throws JxException
     */
    public JboIFace setValue(String jboname, String relationship, String uid, String attributeName, String value) throws JxException {
        if (StrUtil.isNull(uid)) {
            throw new JxException(JxLangResourcesUtil.getString("app.setvalue.uid.null"));
        }
        JboSetIFace js = findJboSet(jboname, relationship, JxConstant.READ_CACHE);
        if (js != null) {
            JboIFace j = js.getJboOfUid(uid);
            FieldIFace field = JboUtil.getField(j, attributeName);

            if (null != field) {
                field.execute(attributeName, value);
                if (j != null) {
                    return j;
                } else {
                    throw new JxException(JxLangResourcesUtil.getString("app.setvalue.jbo.null") + "id=" + uid);
                }
            } else {
                j.setObject(attributeName, value);
                // 这里没有字段验证类啊，直接返回null,界面不需要重新设置值
                return null;
            }
        } else {
            throw new JxException(JxLangResourcesUtil.getString("app.setvalue.jboset.null") + "jboname=" + jboname + ",relationship=" + relationship);
        }
    }

    /**
     * 通过联系名，找到对应的子记录集
     * 
     * @param parent
     * @param relationship
     * @return
     */
    public JboSetIFace findJboSet(String parent, String relationship) throws JxException {
        return findJboSet(parent, relationship, JxConstant.READ_RELOAD);
    }

    /**
     * 通过联系名，找到对应的子记录集
     * 
     * @param parent
     * @param relationship
     * @param flag 参见：JxConstant.READ_XXX
     * @return
     * @throws JxException
     */
    public JboSetIFace findJboSet(String parent, String relationship, long flag) throws JxException {
        JboSetIFace findJboSet = null;
        if (StrUtil.isNull(relationship)) {
            if (jboset != null) {
                if (parent == null) {
                    findJboSet = jboset;
                } else if (parent.equalsIgnoreCase(jboset.getJboname())) {
                    findJboSet = jboset;
                }
            }
            if (!StrUtil.isNull(parent)) {
                if (findJboSet == null || (flag & JxConstant.READ_RELOAD) == JxConstant.READ_RELOAD) {
                    findJboSet = JboUtil.getJboSet(parent);
                }
            }
            if (null != findJboSet) {
                findJboSet.afterLoad();
            }
            return findJboSet;
        }

        JboIFace j = null;
        if (jboset != null) {
            j = jboset.getJbo();
        }
        findJboSet = findJboSet(j, parent, relationship, flag);
        if (null != findJboSet) {
            findJboSet.setRelationshipname(relationship);
            findJboSet.afterLoad();
        }
        return findJboSet;
    }

    private JboSetIFace findJboSet(JboIFace j, String parent, String relationship, long flag) throws JxException {
        if (j == null && !StrUtil.isNull(parent) && StrUtil.isNull(relationship)) {
            return JboUtil.getJboSet(parent);
        } else if (j == null || StrUtil.isNull(parent) || StrUtil.isNull(relationship)) {
            return null;
        }
        String jn = j.getJboName();
        if (parent.equalsIgnoreCase(jn)) {
            return j.getRelationJboSet(relationship);
        }
        JboSetIFace js = j.getRelationJboSet(relationship, flag);
        if (js != null) {
            return js;
        }
        Map<String, JboSetIFace> jbos = j.getChildren();
        if (jbos == null) {
            LOG.info("没找到JboSet,parent=" + parent + ",relationship=" + relationship);
            return null;
        }
        for (Map.Entry<String, JboSetIFace> entry : jbos.entrySet()) {
            // 处理查询子对象。
            js = entry.getValue();
            if (js != null) {
                JboIFace je = js.getJbo();
                if (je != null) {
                    if (parent.equalsIgnoreCase(je.getJboName())) {
                        return je.getRelationJboSet(relationship);
                    } else {
                        JboSetIFace jss = findJboSet(je, parent, relationship, flag);
                        if (jss != null) {
                            return jss;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 加载导入文件
     * 
     * @param importFileResult LinkedList 有序
     * @return
     */
    public String loadImportFile(List<Map<Object, String>> importFileResult, String relationship, JxUserInfo unserInfo) throws JxException {
        String msg = "";
        if (!StrUtil.isNull(relationship)) {
            JboIFace jbo = getJbo();
            if (null != jbo) {
                jboset = jbo.getRelationJboSet(relationship);
            } else {
                throw new JxException(JxLangResourcesUtil.getString("app.loadimportfile.jbo.null"));
            }
        }

        msg = jboset.loadImportFile(importFileResult, unserInfo);
        return msg;
    }

    /**
     * 在发送工作流页面初始化的时候获取当前应用的工作流参数(主要用来扩展工作流发送界面）
     * 
     * @return
     * @throws JxException
     */
    public WorkflowBaseInfo getWorkflowinfo() throws JxException {
        IWorkflowEngine wfEngine = WorkflowEngineFactory.getWorkflowEngine(getJboset().getWorkflowEngine());
        WorkflowBaseInfo workFlowInfo = wfEngine.pretreatment(getAppName(), getJbo().getJboName(), getJbo().getUidValue());
        return workFlowInfo;
    }

    /**
     * 在发送工作流页面初始化的时候获取当前应用的工作流参数(主要用来扩展工作流发送界面,这个给康拓扑那边的应用调用）
     * 
     * @param jbo
     * @return WorkflowInfo
     * @throws JxException
     */
    public WorkflowBaseInfo getWorkflowinfo(JboIFace jbo) throws JxException {
        IWorkflowEngine wfEngine = WorkflowEngineFactory.getWorkflowEngine(this.getJboset().getWorkflowEngine());
        WorkflowBaseInfo workFlowInfo = wfEngine.pretreatment(getAppName(), jbo.getJboName(), jbo.getUidValue());
        return workFlowInfo;
    }

    public String getAppNameType() {
        return appName + "." + appType;
    }

    public JboIFace getJbo() throws JxException {
        if (jboset != null) {
            return jboset.getJbo();
        }
        return null;
    }

    public void tableQuickSearch(String searchValue) throws JxException {
        if (null != jboset) {
            jboset.tableQuickSearch(searchValue);
        }
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public JxApps getJxapps() {
        return jxapps;
    }

    public void setJxapps(JxApps jxapps) {
        this.jxapps = jxapps;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<WftParam> getWftParam() {
        return this.wftParam;
    }

    public void setWftParam(List<WftParam> wftParam) {
        this.wftParam = wftParam;
    }

    public Map<String, App> getRefApp() {
        return refApp;
    }

    public void setRefApp(Map<String, App> refApp) {
        this.refApp = refApp;
    }

}
