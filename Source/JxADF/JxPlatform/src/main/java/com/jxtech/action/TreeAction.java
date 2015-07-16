package com.jxtech.action;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 返回JSON格式的数据
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class TreeAction extends ActionSupport {
    private static final Logger LOG = LoggerFactory.getLogger(TreeAction.class);
    private static final long serialVersionUID = -8495877223259256411L;
    private String jboname;// 表名
    private String orderby;// 排序
    private String cause;// 查询条件
    private String idKey;// 节点标识
    private String pidKey;// 父节点标识
    private String name; // 显示名称
    private String hasChildName;// 是否有孩子节点，专门针对树结构的
    private String leafDisplay;
    private String root;
    private String relationship; // 关系
    private String i18n; //国际化

    public void toJson() {
        // LOG.debug("进入JSON获取数据啦.......................");
        if (StrUtil.isNull(jboname)) {
            LOG.info("jboname is null");
            return;
        }
        if (StrUtil.isNull(idKey) || StrUtil.isNull(pidKey) || StrUtil.isNull(name)) {
            LOG.info("idKey、pidKey、name is null.");
            return;
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, String[]> map = request.getParameterMap();
        String[] ids = map.get(idKey);
        String whereCause = null;
        if (ids != null && ids.length > 0) {
            whereCause = pidKey + "='" + ids[0] + "'";
        }
        if (StrUtil.isNull(cause)) {
            cause = whereCause;
        } else if (!StrUtil.isNull(whereCause)) {
            cause = "(" + cause + ") AND (" + whereCause + ")";
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            JboSetIFace js = null;
            if (StrUtil.isNull(relationship)) {
                js = JboUtil.getJboSet(jboname);
            } else {
                App myApp = JxSession.getApp();
                js = myApp.getJbo().getRelationJboSet(relationship);
            }

            js.setAppname(JxSession.getAppNameOfJboname(jboname, false));
            DataQueryInfo qi = js.getQueryInfo();
            qi.setWhereCause(cause);
            qi.setOrderby(orderby);
            js.queryAll();

            String[] attributes = new String[] { idKey, pidKey, name };
            out.println("[" + js.toZTreeJson(attributes, idKey, pidKey, name, hasChildName, !"false".equalsIgnoreCase(leafDisplay), root, i18n) + "]");
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getPidKey() {
        return pidKey;
    }

    public void setPidKey(String pidKey) {
        this.pidKey = pidKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHasChildName() {
        return hasChildName;
    }

    public void setHasChildName(String hasChildName) {
        this.hasChildName = hasChildName;
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
    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }
}
