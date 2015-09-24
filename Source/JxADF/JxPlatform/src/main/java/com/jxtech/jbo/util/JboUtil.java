package com.jxtech.jbo.util;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxObjectDao;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.field.FieldIFace;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.osgi.DefaultBundleAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理Jbo的工具类
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JboUtil {
    private static Logger LOG = LoggerFactory.getLogger(JboUtil.class);

    /**
     * @param jboname
     * @return
     * @throws JxException
     */
    public static JboSetIFace getJboSet(String jboname) throws JxException {
        JboSetIFace jboset = null;
        JxObject obj = JxObjectDao.getJxObject(jboname);
        String clsname = "";
        if (obj != null) {
            clsname = obj.getClassname();
            if (StrUtil.isNull(clsname)) {
                LOG.debug(jboname + " not config jboset ,use default jbo");
            }
            jboset = getJboSetLocal(clsname);
        } else {
            LOG.warn("没有找到" + jboname + ",请在maxobject中注册，谢谢。");
        }
        if (jboset == null) {
            jboset = new JboSet();
        }

        jboset.setJboname(jboname);

        return jboset;
    }

    /**
     * 直接加载,以本地类优先
     * 
     * @param className
     * @return
     */
    public static JboSetIFace getJboSetLocal(String className) {
        JboSetIFace jboset = null;
        if (!StrUtil.isNull(className) && !"psdi.mbo.custapp.CustomMboSet".equals(className)) {
            Object obj = ClassUtil.getInstance(className);
            if (obj != null && obj instanceof JboSetIFace) {
                jboset = (JboSetIFace) obj;
            }
        }
        if (jboset == null) {
            LOG.debug("not found class [" + className + "],use default jboSet");
            jboset = (new JboSet());
        }
        return jboset;
    }

    public static JboSetIFace getJboSetOsgi(String className) {
        JboSetIFace jboset = null;
        DefaultBundleAccessor bundleAcessor = DefaultBundleAccessor.getInstance();
        if (bundleAcessor != null) {
            Object bobj = ClassUtil.getInstance(className);
            if ( bobj instanceof JboSetIFace) {
                try {
                    jboset = (JboSetIFace) bobj;
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        if (jboset == null) {
            LOG.debug("not found class [" + className + "],use default jboSet");
            jboset = (new JboSet());
        }
        return jboset;
    }

    public static JboIFace getJbo(String jboname, String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(jboname) || StrUtil.isNull(uid)) {
            return null;
        }
        JboSetIFace js = getJboSet(jboname);
        if (js == null) {
            return null;
        }
        if (StrUtil.isNull(jboKey)) {
            return js.queryJbo(uid);
        } else
            return js.queryJbo(jboKey, uid);
    }

    public static JboIFace findJbo(String jboname, String jboKey, String value) throws JxException {
        if (StrUtil.isNull(jboname) || StrUtil.isNull(jboKey) || StrUtil.isNull(value)) {
            return null;
        }
        List<JboIFace> list = findJboList(jboname, jboKey.trim() + "=?", new Object[] { value });
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    /**
     * 根据当前jbo查询邻居数据
     * 
     * @param jbo 当前jbo
     * @return
     */
    public static JboIFace getSlibingJbo(JboIFace jbo, int slibing) throws JxException {
        if (null != jbo) {
            // 这个值有可能是日期格式的
            Object uidValue = jbo.getUidValue();
            String jboName = jbo.getJboName();
            JboSetIFace jboset = JboUtil.getJboSet(jboName);
            JboSetIFace jboset1 = jbo.getJboSet();

            // 列表定义的排序字段的属性和值
            String ordername = null;
            Object value = null;
            String sorttype = null;
            JxTable jxtable = jboset1.getJxTable();
            if (jxtable != null) {
                App listapp = JxSession.getApp(jxtable.getTableModle().getAppName() + "." + jxtable.getTableModle().getAppType());
                String orderby = listapp.getJboset().getQueryInfo().getOrderby();
                if (!StrUtil.isNull(orderby)) {
                    String strs[] = StringUtils.split(orderby, " ");
                    if (strs.length == 2) {
                        sorttype = strs[1];
                    } else {
                        sorttype = "ASC";
                    }
                    ordername = StringUtils.upperCase(strs[0]);
                    value = jbo.getData().get(ordername);
                }
            }

            if (null != jboset) {
                DataQueryInfo dqInfo = new DataQueryInfo();
                String uidname = null;
                if (!StrUtil.isNull(ordername) && value != null) {
                    uidname = ordername;
                    uidValue = value;
                } else {
                    uidname = jbo.getUidName();
                }

                if (slibing == JxConstant.SLIBING_PREVIOUS) {
                    if ("desc".equalsIgnoreCase(sorttype)) {
                        dqInfo.setWhereCause(uidname + " > ? ");
                        dqInfo.setOrderby(uidname + " ASC ");
                    } else {
                        dqInfo.setWhereCause(uidname + " < ? ");
                        dqInfo.setOrderby(uidname + " DESC ");
                    }
                    dqInfo.setWhereParams(new Object[] { uidValue });
                } else if (slibing == JxConstant.SLIBING_NEXT) {
                    if ("desc".equalsIgnoreCase(sorttype)) {
                        dqInfo.setOrderby(uidname + " DESC ");
                        dqInfo.setWhereCause(uidname + " < ? ");
                    } else {
                        dqInfo.setWhereCause(uidname + " > ? ");
                        dqInfo.setOrderby(uidname + " ASC ");
                    }
                    dqInfo.setWhereParams(new Object[] { uidValue });
                }

                jboset.setQueryInfo(dqInfo);
                List<JboIFace> jbolist = jboset.query();

                if (jbolist != null && !jbolist.isEmpty()) {
                    return jbolist.get(0);
                }
            }
        }
        return null;
    }

    /**
     * 根据条件获得某个表中的某条记录信息
     * 
     * @param jboname
     * @param whereCause
     * @param params
     * @param attributeName
     * @return
     * @throws JxException
     */
    public static Object getValue(String jboname, String whereCause, Object[] params, String attributeName) throws JxException {
        JboSetIFace jboset = getJboSet(jboname);
        if (jboset != null) {
            DataQueryInfo dqInfo = jboset.getQueryInfo();
            dqInfo.setWhereCause(whereCause);
            dqInfo.setWhereParams(params);
            List<JboIFace> list = jboset.query();
            if (list != null && !list.isEmpty()) {
                return list.get(0).getObject(attributeName);
            }
        }
        return null;
    }

    /**
     * 通过应用程序名称和UID的值，解析EL表达式中的内容。
     * 
     * @param appname 应用程序名 maxapps.app
     * @param uid 主键
     * @param expression EL表达式
     * @return
     */
    public static String getELValue(String appname, String uid, String expression) throws JxException {
        if (StrUtil.isNull(appname) || StrUtil.isNull(uid)) {
            return expression;
        }
        JboSetIFace js = getJboSet("maxapps");
        if (js != null) {
            DataQueryInfo dq = js.getQueryInfo();
            dq.setWhereCause("app=?");
            dq.setWhereParams(new Object[] { appname.toUpperCase() });
            List<JboIFace> list = js.query();
            if (list != null) {
                if (list.size() > 0) {
                    JboIFace jbo = null;
                    jbo = list.get(0);
                    if (jbo == null) {
                        return expression;
                    }
                    JboIFace val = getJbo(jbo.getString("MAINTBNAME"), "", uid);
                    if (val == null) {
                        return expression;
                    }
                    return ELUtil.getJboElValue(val, expression);
                }
            }
        }
        return expression;
    }

    public static FieldIFace getField(JboIFace jbo, String attributeName) throws JxException {
        FieldIFace fieldIFace = null;
        JxAttribute jxAttr = jbo.getJxAttribute(attributeName);
        if (null != jxAttr && !StrUtil.isNull(jxAttr.getClassName())) {
            Object obj = ClassUtil.getInstance(jxAttr.getClassName());
            if (obj != null && obj instanceof FieldIFace) {
                fieldIFace = (FieldIFace) obj;
                fieldIFace.setJbo(jbo);
            }
        }
        return fieldIFace;
    }


    /**
     * 获取在JXVAR表中的配置信息。
     * 
     * @param varname 配置项 key
     * @return
     * @throws JxException
     */
    public static String getJxvarsValue(String varname) throws JxException {
        Object obj = JboUtil.getValue("jxvars", "varname=?", new Object[] { varname }, "varvalue");
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;
    }

    /**
     * 根据 对象名称,条件,参数查询
     * 
     * @param jboName
     * @param whereCase
     * @param args
     * @return
     * @throws JxException
     */
    public static List<JboIFace> findJboList(String jboName, String whereCase, Object[] args) throws JxException {
        JboSetIFace jboJboSet = JboUtil.getJboSet(jboName);
        if (jboJboSet != null) {
            DataQueryInfo dao = jboJboSet.getQueryInfo();
            dao.setWhereCause(whereCase);
            dao.setWhereParams(args);
            return jboJboSet.queryAll();
        }
        return new ArrayList<JboIFace>();
    }

    /**
     * 返回指定的第一条数据的jbo
     * 
     * @param jboName
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public static JboIFace getJboSetIFaceByQuery(String jboName, String whereCause, Object[] params) throws JxException {
        return findJbo(jboName,whereCause,params);
    }

    public static JboIFace findJbo(String jboName, String whereCause, Object[] params) throws JxException {
        List<JboIFace> list = findJboList(jboName, whereCause, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 
     * @param parent
     * @param shipName
     * @param flag
     * @return
     * @throws JxException
     */
    public static JboIFace getRelationShipJbo(JboIFace parent, String shipName, long flag) throws JxException {
        if (parent == null || StrUtil.isNull(shipName)) {
            return null;
        }
        JboSetIFace childSet = parent.getRelationJboSet(shipName, flag);
        if (childSet != null && !childSet.getJbolist().isEmpty()) {
            return childSet.getJbolist().get(0);
        }
        return null;
    }
}
