package com.jxtech.dwr;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.io.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.jxtech.app.jxlogin.JxLoginUtil;
import com.jxtech.app.maxmenu.MaxAppMenu;
import com.jxtech.app.maxmenu.MenuAccess;
import com.jxtech.app.maxmenu.MenuAccessFactory;
import com.jxtech.app.usermetadata.MetaData;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.DataMap;
import com.jxtech.jbo.base.JxRelationship;
import com.jxtech.jbo.base.JxRelationshipDao;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import com.jxtech.workflow.base.IWorkflowEngine;
import com.jxtech.workflow.base.WorkflowEngineFactory;

/**
 * Web页面调用
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */

@Controller
@RemoteProxy
public class WebClientBean {

    // 忽略权限检查的方法名
    public static final String IGNORE_PERMISSION = "setSessionValue,login,generateId,setUserLang,pageLoaded,getUserLang,";
    private static final Logger LOG = LoggerFactory.getLogger(WebClientBean.class);

    public Map<String, Object> getData() throws JxException {
        App app = JxSession.getMainApp();
        if (app != null) {
            JboIFace jbo = app.getJbo();
            if (jbo != null) {
                return jbo.getData();
            }
            if (app.getJbo() != null) {
                return app.getJbo().getData();
            }
        }
        return new DataMap<String, Object>();
    }

    /**
     * 保存数据
     * 
     * @param appNameType
     * @return
     * @throws JxException
     */
    public JboSetIFace save(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.save();
        } else {
            throw new JxException("获得app出错，没有找到应用程序：" + appNameType);
        }
    }

    /**
     * 上一条
     * 
     * @param appNameType
     * @return
     */
    public String previous(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.previous();
        } else {
            throw new JxException("获得app出错，没有找到应用程序：" + appNameType);
        }
    }

    /**
     * 下一条
     * 
     * @param appNameType
     * @return
     */
    public String next(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.next();
        } else {
            throw new JxException("获得app出错，没有找到应用程序：" + appNameType);
        }
    }

    /**
     * 回滚数据
     * 
     * @param appNameType
     * @return
     * @throws JxException
     */
    public boolean rollback(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.rollback();
        } else {
            throw new JxException("获得app出错，没有找到应用程序：" + appNameType);
        }
    }

    public String getJsonData(String jboname, String relationship, String wherecause, String orderby, List<String> attributes) throws JxException {
        if (attributes == null) {
            return null;
        }

        JboSetIFace jboset = null;
        if (!StrUtil.isNull(jboname)) {
            jboset = JboUtil.getJboSet(jboname);
            if (jboset != null) {
                if (!StrUtil.isNull(relationship)) {
                    JboIFace j = jboset.getJbo();
                    if (j != null) {
                        JboSetIFace js = j.getRelationJboSet(relationship, JxConstant.READ_RELOAD);
                        if (js != null) {
                            jboset = js;
                        }
                    }
                }
            }
        } else if (!StrUtil.isNull(relationship)) {
            App app = JxSession.getApp();
            jboset = app.findJboSet(null, relationship);
        }
        if (jboset != null) {
            DataQueryInfo dqi = jboset.getQueryInfo();
            dqi.setWhereCause(wherecause);
            dqi.setOrderby(orderby);
            jboset.queryAll();
            int size = attributes.size();
            String[] attrs = new String[size];
            for (int i = 0; i < size; i++) {
                attrs[i] = attributes.get(i);
            }
            return jboset.toJson(attrs);
        }

        return null;
    }

    /**
     * 列表界面点击“新建按钮判断是否能够新增记录”
     * 
     * @param appNameType
     * @return
     * @throws JxException
     */
    public boolean canJboSetAdd(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (null != app) {
            return app.canJboSetAdd();
        } else {
            throw new JxException("新增记录失败，没有找到对应的应用，请重新登录之后再试。");
        }
    }

    /**
     * 创建记录
     * 
     * @param appNameType
     * @return
     * @throws JxException
     */
    public JboIFace add(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.add();
        } else {
            throw new JxException("新增记录失败，没有找到对应的应用，请重新登录之后再试。");
        }
    }

    public boolean delList(String jboname, String uids) throws JxException {
        if (StrUtil.isNull(jboname) || StrUtil.isNull(uids)) {
            throw new JxException("未选择记录或系统错误。");
        }
        JboSetIFace js = JboUtil.getJboSet(jboname);
        if (js != null) {
            return js.delete(uids.split(","));
        }
        return false;
    }

    /**
     * 删除自己创建的信息
     * 
     * @param jboname
     * @param uids
     * @return
     * @throws JxException
     */
    public boolean checkJboOwer(String jboname, String uids) throws JxException {
        if (StrUtil.isNull(jboname) || StrUtil.isNull(uids)) {
            throw new JxException("未选择记录或系统错误。");
        }
        String[] uidArray = uids.split(",");
        JxUserInfo user = JxSession.getJxUserInfo();
        for (int i = 0; i < uidArray.length; i++) {
            JboIFace jbo = JboUtil.getJbo(jboname, "", uidArray[i]);
            if (!(user.getUserid()).equals(jbo.getObject("CREATOR_ID"))) {
                throw new JxException("请选择自己创建的记录。");
            }
        }
        return true;
    }

    /**
     * 标记删除行，并没有真正删除，只是标记删除，点击保存时，才真正删除。
     * 
     * @param appNameType 应用程序名+应用程序类型
     * @param jboname 主表表名
     * @param relationship 联系名
     * @param uid 要删除的记录的唯一ID
     * @param isDel 是删除(true)还是恢复(false)
     * @return
     */
    public boolean delRow(String appNameType, String jboname, String relationship, String uid, boolean isDel) throws JxException {
        if (StrUtil.isNull(appNameType)) {
            throw new JxException("未知应用程序，删除失败。");
        }
        if (StrUtil.isNull(uid)) {
            throw new JxException("记录ID未知，删除失败。");
        }
        App app = JxSession.getApp(appNameType);
        if (app == null) {
            throw new JxException("你的应用已失效，请重新登录。\r\n" + appNameType);
        }
        JboSetIFace js = app.findJboSet(jboname, relationship, JxConstant.READ_CACHE);
        if (js == null) {
            throw new JxException("没有找到对应的记录集，删除失败。");
        }

        if (isDel) {
            return js.delRow(uid);
        } else {
            return js.unDelRow(uid);
        }
    }

    public JboIFace addRow(String appNameType, String jboname, String relationship) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app == null) {
            throw new JxException("你的应用已失效，请重新登录。\r\n" + appNameType);
        }
        JboSetIFace js = app.findJboSet(jboname, relationship, JxConstant.READ_CACHE);
        if (js == null) {
            throw new JxException("没有找到对应的记录集，新增失败。");
        }
        if (js.isReadonly()) {
            throw new JxException("当前记录集只读，无法添加记录！");
        }
        if (js.canAdd()) {
            JboIFace row = js.add();
            return row;
        } else {
            return null;
        }
    }

    /**
     * 输入框执行OnChange事件
     * 
     * @return
     */
    public Object onChange() {
        return null;
    }

    public String getStringOfFlag(String appNameType, String attributeName, long flag) throws JxException {
        LOG.debug(attributeName + "," + flag);
        if ((flag & JxConstant.READ_RELOAD) == JxConstant.READ_RELOAD) {
            if (attributeName.indexOf(".") > 0) {
                setValue(appNameType, attributeName, null);
            }
        }
        return getString(appNameType, attributeName, flag);
    }

    public String getString(String appNameType, String attributeName, long flag) throws JxException {
        LOG.debug(attributeName);
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            JboIFace jbo = app.getJbo();
            if (jbo != null) {
                return jbo.getString(attributeName, flag);
            } else {
                throw new JxException("找不到当前的Jbo对象");
            }
        } else {
            throw new JxException("找不到程序APP");
        }
    }

    /**
     * 输入框失去焦点
     * 
     * @param attributeName
     * @param value
     * @return
     */
    public JboIFace inputOnBlur(String appNameType, String attributeName, String value, String relationship) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.setValue(attributeName, value, relationship);
        }
        LOG.info("JxSession.getApp(appNameType) is null. " + appNameType);
        throw new JxException("没有找到应用程序。");
    }

    /**
     * 有两个文本框的焦点移出功能。
     * 
     * @param appNameType
     * @param attributeName
     * @param value
     * @param attributeName2
     * @return 返回描述框的值
     */
    public JboIFace inputOnBlur2(String appNameType, String attributeName, String value, String attributeName2, String relationship) throws JxException {
        /*
         * if (!StrUtil.isNull(relationship)) { JboIFace mainJbo = JxSession.getMainApp().getJbo(); JboSetIFace rJboSet = mainJbo.getRelationJboSet(relationship); JboIFace rJbo = rJboSet.getJbo(); if (null != rJbo) { rJbo.setObject(attributeName, value); } else { throw new JxException("获取数据失败。"); } return rJbo; }
         */

        JboIFace jbo = inputOnBlur(appNameType, attributeName, value, relationship);
        if (null == jbo) {
            throw new JxException("获取数据失败。");
        }
        return jbo;
    }

    /**
     * Table表格中，设定某个字段的值。
     * 
     * @param appNameType
     * @param attributeName
     * @param value
     * @param jboname
     * @param relationship
     * @param uid
     * @return
     * @throws JxException
     */
    public JboIFace tableInputOnBlur(String appNameType, String attributeName, String value, String jboname, String relationship, String uid) throws JxException {
        App app = JxSession.getApp(appNameType);
        JboIFace jbo = app.setValue(jboname, relationship, uid, attributeName, value);
        if (null != jbo) {
            return jbo;
        } else {
            return null;
        }
    }

    /**
     * 查询输入框失去焦点
     * 
     * @param relationship
     * @param cause
     * @param value
     */
    public void inputQueryOnBlur(String appNameType, String relationship, String cause, String value) throws JxException {
        if (StrUtil.isNull(cause)) {
            return;
        }
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            LOG.debug(appNameType + ",cause=" + cause + ",value=" + value);
            JboSetIFace jboset = app.findJboSet(null, relationship);
            if (jboset == null) {
                throw new JxException("没有对应的jboset，不知查询哪里：relationship=" + relationship + ",cause=" + cause);
            }
            DataQueryInfo qi = jboset.getQueryInfo();
            qi.getParams().put(cause, value);
        } else {
            throw new JxException("没有正确设置查询属性：" + appNameType + "," + cause + ",value=" + value);
        }
    }

    public void searchClear(String appNameType, String relationship) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            JboSetIFace jboset = app.findJboSet(null, relationship);
            if (jboset == null) {
                throw new JxException("没有对应的jboset，不知查询哪里：relationship=" + relationship);
            }
            DataQueryInfo qi = jboset.getQueryInfo();
            qi.getParams().clear();
        } else {
            throw new JxException("没有正确得到APP信息。appNameType=" + appNameType);
        }
    }

    public void setValue(String appNameType, String attributeName, String value) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            app.setValue(attributeName, value, "");
        } else {
            throw new JxException("没有找到对应的应用程序，请重新登录。");
        }
    }

    /**
     * 给relationship子对象某个字段赋值
     * 
     * @param appNameType
     * @param jboname
     * @param relationship
     * @param uid
     * @param attributeName
     * @param value
     * @throws JxException
     */
    public void setValue(String appNameType, String jboname, String relationship, String uid, String attributeName, String value) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            if (StrUtil.isNull(relationship)) {
                app.setValue(attributeName, value, "");
            } else {
                app.setValue(jboname, relationship, uid, attributeName, value);
            }

        } else {
            throw new JxException("没有找到对应的应用程序，请重新登录。");
        }
    }

    /**
     * 用户登录
     * 
     * @param userid
     * @param password
     * @return
     */
    public boolean login(String userid, String password, String lang) throws JxException {
        if (StrUtil.isNull(userid)) {
            return false;
        }
        boolean b = JxLoginUtil.login(userid, password, false);// 登录系统
        if (b) {
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            if (null != userInfo) {
                userInfo.setLangcode(lang.replace("-", "_"));
            }
        }
        return b;
    }

    /**
     * 通用的工作流发送
     * 
     * @param appname 对应MAXAPP表中的APP字段
     * @param jboname 对应MAXOBJECT
     * @param uid 单条记录发送的UID
     * @param action 操作比如SUBMIT,APPROVE,REJECT,自定义的action等
     * @param note 说明
     * @param toUsers 指定用户
     * @param options 扩展
     * @return
     * @throws JxException
     */
    public String routeCommon(String appname, String jboname, String uid, String action, String note, String toUsers, String options) throws JxException {
        JboSetIFace jboSet = JxSession.getMainApp().getJboset();
        String result = "";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ACTION", action);
        params.put("NOTE", note);
        params.put("TOUSERS", toUsers);
        params.put("OPTIONS", options);

        JboIFace jbi = jboSet.queryJbo(uid);
        if (null != jbi) {
            boolean routeResult = jbi.route(params);
            if (routeResult) {
                result = jbi.getString("WFT_TRANSACTOR");
                // 只有发送成功了才需要提交
                jbi.getJboSet().commit();
            } else {
                jbi.getJboSet().rollback();
            }
        }

        if (StrUtil.isNull(result)) {
            result = JxLangResourcesUtil.getString("dwr.WebClientBean.no_transactor");
        }

        return result;
    }

    /**
     * 康拓普 - 发送工作流
     * 
     * @param appname 应用程序名
     * @param jboname 表名
     * @param uids 需要发送工作流的单据ID号
     * @param toActId 发送到指定的节点号
     * @param isAgree 是否同意
     * @param note 备注
     * @param toUsers 指定用户
     * @return 返回成功错误信息
     */
    public String route(String appname, String jboname, String uids, String toActId, int isAgree, String note, String toUsers, String options) throws JxException {
        String wfType = JboUtil.getAppWorkflowEngine(appname);
        IWorkflowEngine engine = WorkflowEngineFactory.getWorkflowEngine(wfType);
        StringBuffer sb = new StringBuffer();
        String[] uidAry = uids.split(",");
        int uidLen = uids.length();

        JboSetIFace jboSet = JboUtil.getJboSet(jboname);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("toActId", toActId == null ? "" : toActId);
        params.put("isAgree", isAgree);
        params.put("note", note == null ? "" : note);
        params.put("toUsers", toUsers == null ? "" : toUsers);
        params.put("options", options == null ? "" : options);
        jboSet.queryAll();
        for (int i = 0; i < uidLen; i++) {
            JboIFace jbi = jboSet.getJboOfUid(uidAry[i]);
            if (null != jbi) {
                sb.append(engine.route(jbi, params));
            }
        }
        return sb.toString();
    }

    /**
     * 检查当前数据是否发生了更改
     * 
     * @param appNameType
     * @return
     * @throws JxException
     */
    public boolean checkDataChange(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            JboIFace jbo = app.getJbo();
            if (null != jbo) {
                return jbo.isModify() || jbo.isToBeAdd() || jbo.getChangedChildren().size() > 0;
            } else {
                return false;
            }
        } else {
            throw new JxException("没有找到对应的应用，请重新登录之后再试。");
        }
    }

    protected JboIFace findJbo(App app, String jboname, String jboKey, String jboUid, String relationship) throws JxException {
        JboIFace jf = null;
        JboSetIFace jsf;
        if (null == app.getJbo())
            return null;
        if (!StrUtil.isNull(relationship)) {
            jsf = app.getJbo().getRelationJboSet(relationship);
        } else {
            jsf = app.getJboset();
        }
        if (null != jsf) {
            Iterator it = jsf.getJbolist().iterator();
            while (it.hasNext()) {
                jf = (JboIFace) it.next();
                if (jboUid.equals(jf.getObject(jboKey).toString())) {
                    return jf;
                }
            }
        }
        return null;
    }

    /**
     * 根据jboname和jbouid获取jbo
     * 
     * @param appNameType 应用
     * @param jboname 名称
     * @param jboUid 应用的id
     * @param setCurrentJbo 是否将获取到的jbo设置为其children某个jboset的默认jbo
     * @param relationship 如果setCurrentJbo为true，必须要有关系名
     * @return
     * @throws JxException
     */
    public JboIFace getJbo(String appNameType, String jboname, String jboKey, String jboUid, boolean setCurrentJbo, String relationship) throws JxException {

        // FIXME:需要确认是否需要这个判断
        if ("LOOKUP".equalsIgnoreCase(JxSession.getApp().getAppType())) {
            return null;
        }

        App currentApp;
        if (StrUtil.isNull(appNameType)) {
            currentApp = JxSession.getApp();
        } else {
            currentApp = JxSession.getApp(appNameType);
        }

        if (null != currentApp) {
            JboIFace jbo = currentApp.getJbo();
            String oldJboname = jboname;// 原来的Jbo名称
            // 比如资产，主表就是一棵树
            if (null == jbo) {
                // 当jbo为空的时候，jboset也是为空的
                JboSetIFace jboSet = JboUtil.getJboSet(jboname);
                jbo = jboSet.queryJbo(jboKey, jboUid);
                currentApp.setJboset(jboSet);
                currentApp.getJboset().setJbo(jbo);
            } else {
                oldJboname = jbo.getJboName();
                if (!StrUtil.isNull(jboUid) && !jboUid.equalsIgnoreCase(jbo.getUidValue())) {
                    // 先从session获取当前的jbo，如果当前的jbo不是所需要的jbo或者不存在则从jboset中查询获取。
                    // 先查session
                    if (!StrUtil.isNull(relationship)) {
                        jbo = findJbo(currentApp, jboname, jboKey, jboUid, relationship);
                    } else {
                        jbo = JboUtil.getJbo(jboname, jboKey, jboUid);
                    }
                }
            }

            if (null != jbo) {
                if (setCurrentJbo) {
                    JboSetIFace jsf;
                    if (!StrUtil.isNull(relationship)) {
                        jsf = currentApp.getJbo().getRelationJboSet(relationship);
                    } else {
                        jsf = currentApp.getJboset();
                    }
                    jsf.setJbo(jbo);
                }
                return jbo;
            }
        }

        throw new JxException("无法找到对应【" + jboname + "】的应用，请重新登录后在尝试！");
    }

    /**
     * 通过过滤条件获取当前应用的jbo
     * 
     * @param cause
     * @return
     */
    public JboIFace getJboByCause(String jboname, String cause, String value) throws JxException {
        List<JboIFace> jboList = getJboListByCause(jboname, cause, value);
        if (null != jboList && jboList.size() > 0) {
            return jboList.get(0);
        }
        return null;
    }

    public List<JboIFace> getJboListByCause(String jboname, String cause, String value) throws JxException {
        JboSetIFace jboSet = JboUtil.getJboSet(jboname);
        DataQueryInfo dqInfo = new DataQueryInfo();
        dqInfo.setWhereCause(cause);
        String[] values = value.split(",");
        dqInfo.setWhereParams(values);
        jboSet.setQueryInfo(dqInfo);
        List<JboIFace> jboList = jboSet.queryAll();
        if (null != jboList) {
            return jboList;
        }
        return null;
    }

    /**
     * 将msg保存到当前应用中
     * 
     * @param msg (appNameType.msg);
     */
    public void saveMessage(String appNameType, String msg) throws JxException {
        App currentApp = JxSession.getApp();
        currentApp.setMsg(appNameType + "=" + msg);
    }

    /**
     * 移除当前应用的msg消息
     */
    public void clearMessage() throws JxException {
        App currentApp = JxSession.getApp();
        currentApp.setMsg("");
    }

    public String getAppWorkflowParam(String appNameType) throws JxException {
        String result = "";
        App app = JxSession.getApp(appNameType);

        if (app != null) {
            JboSetIFace jboset = app.getJboset();
            if (jboset != null) {
                JboIFace jbo = jboset.getJbo();
                if (null != jbo) {
                    result = JboUtil.getAppWorkflowEngine(app.getAppName());
                }
            }
        }
        return result;
    }

    /**
     * 重新加载当前的jbo数据
     * 
     * @throws JxException
     */
    public void reloadCurrentJboData() throws JxException {
        App app = JxSession.getApp();

        if (app != null) {
            JboSetIFace jboset = app.getJboset();
            if (jboset != null) {
                JboIFace jbo = jboset.getJbo();
                if (null != jbo) {
                    jbo.reloadData();
                }
            }
        }
    }

    /**
     * 执行jboSet中的jboSet method方法
     * 
     * @param jboName
     * @param methodname
     * @param params
     * @throws Exception
     */
    public String excuteJboSetMethod(String jboName, String methodname, String params) throws JxException {
        JboSetIFace jboSet = JboUtil.getJboSet(jboName);
        String ret = "true";
        Method method;
        try {
            method = jboSet.getClass().getMethod(methodname, String.class);
            String returnmess = (String) method.invoke(jboSet, params);
            if (returnmess != null && !"".equals(returnmess)) {
                ret = returnmess;
            }
        } catch (Exception e) {
            ret = "false";
            LOG.error(e.getMessage());
        }
        return ret;
    }

    /**
     * 查询jbodata
     * 
     * @param appNameType
     * @param relationship
     * @param apprestrictions
     * @throws JxException
     */
    public void queryJboSetData(String appNameType, String relationship, String apprestrictions) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            JboSetIFace jboset;
            if (!StrUtil.isNull(relationship)) {
                jboset = app.getJbo().getRelationJboSet(relationship);
            } else {
                jboset = app.getJboset();
            }

            if (jboset != null) {
                DataQueryInfo queryInfo = jboset.getQueryInfo();
                queryInfo.getParams().clear();
                queryInfo.getParams().put(apprestrictions, apprestrictions);
                jboset.count();
            }
        } else {
            throw new JxException("没有正确设置查询属性：" + appNameType + "," + apprestrictions);
        }
    }

    /**
     * 获取文件个数多少，保留code属性，为扩展做准备
     * 
     * @param jboName
     * @param fromuid
     * @param code
     * @return
     * @throws JxException
     */
    public int getAttachmentFileSize(String jboName, String fromuid, String code) throws JxException {
        if (jboName == null || "".equals(jboName)) {
            jboName = JxSession.getMainApp().getJbo().getJboName();
        }
        JboSetIFace attJboSet = JboUtil.getJboSet("TOP_ATTACHMENT_OBJECT_RELATION");
        DataQueryInfo query = new DataQueryInfo();
        query.setWhereCause(" OBJECT_NAME=? and OBJECT_ID=? ");
        query.setWhereParams(new Object[] { jboName, fromuid });
        attJboSet.setQueryInfo(query);
        List<JboIFace> jbolist = attJboSet.queryAll();
        if (jbolist != null && jbolist.size() != 0) {
            return jbolist.size();
        } else {
            return 0;
        }
    }

    /**
     * 解析relationship查询条件
     * 
     * @param appNameType
     * @param relationshipDataAttr
     * @param cause
     * @return
     * @throws JxException
     */
    public String getRelationShipCause(String appNameType, String relationshipDataAttr, String cause) throws JxException {
        App app = JxSession.getApp(appNameType);
        String jboName = app.getJboset().getJbo().getJboName();
        // String jboName = app.getJbo().getJboName();
        DataQueryInfo query = new DataQueryInfo();
        String condition = query.getRelationShipCauseCondition(jboName, relationshipDataAttr, cause);
        LOG.debug(condition);
        return condition;
    }

    /**
     * 根据relatinship 的dataattribute:relaiontship.attr和cause: attr=1 来获取attr值
     * 
     * @param relationshipDataAttr
     * @param causeValue
     * @return
     * @throws JxException
     */
    public String getRelationshipAttrValue(String jboname, String relationshipDataAttr, String causeValue) throws JxException {
        String attrValue = "";
        if (relationshipDataAttr.indexOf(".") > 0) {
            String[] strs = relationshipDataAttr.split("\\.");

            String relationshipName = strs[0];
            String relationshipAttrName = strs[1];

            JxRelationship relationship = JxRelationshipDao.getJxRelationship(jboname, relationshipName);

            if (null != relationship) {
                String child = relationship.getChild();
                String whereClause = relationship.getWhereclause();

                whereClause = whereClause.replace(":\\S*", "?");
                DataQuery dq = DBFactory.getDataQuery(null, null);
                DataQueryInfo dqInfo = new DataQueryInfo();

                dqInfo.setWhereCause(whereClause);
                dqInfo.setWhereParams(new Object[] { causeValue });
                List<Map<String, Object>> childList = dq.query(child, dqInfo);
                if (null != childList && !childList.isEmpty()) {
                    Map<String, Object> tempMap = childList.get(0);
                    Object tempValue = tempMap.get(relationshipAttrName);
                    if (null != tempValue) {
                        attrValue = tempValue.toString();
                    }
                }
            }
        }
        return attrValue;
    }

    /**
     * 设置当前session语言
     * 
     * @param lang
     * @throws JxException
     */
    public void setUserLang(String lang) throws JxException {
        if (!StrUtil.isNull(lang)) {
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            if (null != userInfo) {
                userInfo.setLangcode(lang.replace("-", "_"));
            }
        }
    }

    /**
     * 在table控件的tablebutton添加lookup交互 点击弹出框lookup的多选table中的tablebutton后的回调操作
     * 
     * @param lookupAppNameType 弹出窗的应用
     * @param lookupJboname 需要选择的数据来源
     * @param lookupRelationship 关系
     * @param lookupUids 选择的数据来源uid
     * @param pAppNameType 需要设置的应用
     * @param pJboname 需要设置的数据
     * @param pRelationship 需要设置数据的关系
     * @return
     * @throws JxException
     */
    public String lookup(String lookupAppNameType, String lookupJboname, String lookupRelationship, String lookupUids, String pAppNameType, String pJboname, String pRelationship) throws JxException {
        String result = "success";
        App lookupApp = JxSession.getApp(lookupAppNameType);
        if (null != lookupApp) {
            JboSetIFace lookupJboSetIFace;
            if (StrUtil.isNull(lookupRelationship)) {
                lookupJboSetIFace = lookupApp.getJboset();
            } else {
                lookupJboSetIFace = lookupApp.getJbo().getRelationJboSet(lookupRelationship);
            }

            if (null == lookupJboSetIFace) {
            }

            if (null != lookupJboSetIFace) {
                List<JboIFace> lookupList = new ArrayList<JboIFace>();
                if (!StrUtil.isNull(lookupUids)) {
                    String[] uids = lookupUids.split(",");

                    for (int i = 0; i < uids.length; i++) {
                        lookupList.add(lookupJboSetIFace.queryJbo("", uids[i]));
                    }
                }

                App pApp = JxSession.getApp(pAppNameType);
                JboSetIFace pJboSetIFace;
                if (null != pApp) {
                    JboIFace pJbo = pApp.getJbo();
                    if (StrUtil.isNull(pRelationship)) {
                        pJboSetIFace = pJbo.getJboSet();
                    } else {
                        pJboSetIFace = pJbo.getChildrenJboSet(pRelationship);
                    }

                    if (null != pJboSetIFace) {
                        pJboSetIFace.lookup(lookupList);
                    }
                }
            }
        }
        return result;
    }

    public List<Map<String, Object>> getMaxAppMenu(HttpSession session) throws SQLException {
        MaxAppMenu maxAppMenu = new MaxAppMenu();
        return maxAppMenu.getMaxAppMenu(session);
    }

    /**
     * 保存访问应用程序的记录
     * 
     * @param app
     * @throws JxException
     */
    public void saveMaxAppVisitData(String app) throws JxException {
        MenuAccess ma = MenuAccessFactory.getMenuAccessInstance();
        if (ma != null) {
            ma.saveAccess(app);
        }
    }

    public Map<String, List<Map<String, Object>>> getMenuHisData() throws JxException {
        MenuAccess ma = MenuAccessFactory.getMenuAccessInstance();
        if (ma != null) {
            return ma.getAccess();
        }
        return null;
    }

    /**
     * 保存用户个性化数据
     * 
     * @param key
     * @param value
     */
    public void saveUserMetadata(String key, String value) {
        try {
            MetaData.putUserMetadata(key, value);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * appbar上面的表格快速搜索
     * 
     * @param appNameType
     * @param searchValue
     * @throws JxException
     */
    public void tableQuickSearch(String appNameType, String searchValue) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (null != app) {
            app.tableQuickSearch(searchValue);
        }
    }

    /**
     * 导出Excel 方法
     * 
     * @param appNameType 当前应用的名称
     * @param title 文件名称 默认传的是页面的title
     * @return dwr专用下载链接
     * @throws JxException
     */
    public FileTransfer expExcel(String appNameType, String title, String uids) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.expExcel(title, uids);
        } else {
            throw new JxException("导出Excel失败，没有找到对应的应用，请重新登录之后再试。");
        }
    }

    /**
     * 导出Excel之前校验 判断是否需要提示
     * 
     * @param appNameType 当前应用的名称
     * @return 当为0的时候 就是可以直接导出 否则返回条数
     * @throws JxException
     */
    public int preExpExcel(String appNameType) throws JxException {
        App app = JxSession.getApp(appNameType);
        if (app != null) {
            return app.preExpExcel();
        } else {
            throw new JxException("导出Excel失败，没有找到对应的应用，请重新登录之后再试。");
        }
    }

    /**
     * 配置Session值
     * 
     * @param key
     * @param value
     */
    public void setSessionValue(String key, String value) {
        if (key == null) {
            return;
        }
        HttpSession session = JxSession.getSession();
        if (session != null) {
            if (value == null) {
                session.removeAttribute(key);
            } else {
                session.setAttribute(key, value);
            }
        } else {
            LOG.warn("session is null.");
        }
    }
}
