package com.jxtech.jbo.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.osgi.DefaultBundleAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.jxtech.tag.table.Table;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;

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
            if (bobj instanceof JboSetIFace) {
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
        if (jbo == null) {
            return null;
        }
        // 这个值有可能是日期格式的
        String uidValue = jbo.getUidValue();
        JboSetIFace jboset = jbo.getJboSet();
        JxTable jxtable = jboset.getJxTable();
        if (jxtable != null) {
            Table table = jxtable.getTableModle();
            if (table != null) {
                App listapp = JxSession.getApp(table.getAppName(), table.getAppType());
                if (listapp != null) {
                    JboSetIFace listjboset = listapp.getJboset();
                    if (listjboset != null) {
                        // 如果得到列表中的记录
                        List<JboIFace> list = listjboset.getJbolist();
                        if (list != null) {
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                JboIFace ji = list.get(i);
                                if (StrUtil.equals(uidValue, ji.getUidValue())) {
                                    if (slibing == JxConstant.SLIBING_PREVIOUS) {
                                        if (i > 0) {
                                            return list.get(i - 1);
                                        }
                                    } else if (slibing == JxConstant.SLIBING_NEXT) {
                                        if (i < size - 1) {
                                            return list.get(i + 1);
                                        }
                                    }
                                }
                            }
                        }
                        // 上面没有找到对应的记录，需要重新查询
                        DataQueryInfo ndqi = listjboset.getQueryInfo();
                        int pagenum = ndqi.getPageNum();
                        if (slibing == JxConstant.SLIBING_PREVIOUS) {
                            if (pagenum <= 1) {
                                LOG.debug("已是第1页，不能再向上翻页了");
                                return null;
                            } else {
                                pagenum = pagenum - 1;
                            }
                        } else if (slibing == JxConstant.SLIBING_NEXT) {
                            pagenum = pagenum + 1;
                        }
                        // 设定新的页码
                        ndqi.setPageNum(pagenum);
                        List<JboIFace> nlist = listjboset.query();
                        if (nlist != null && !nlist.isEmpty()) {
                            if (slibing == JxConstant.SLIBING_PREVIOUS) {
                                return nlist.get(nlist.size() - 1);// 上一页的最后一条
                            }
                            return nlist.get(0);// 下一页的第一条
                        }
                        // 经过上面的操作，仍然没有找到，直接退出，找不到了。
                        return null;
                    }
                }
            }
        }

        // 直接通过UID来判断，查找记录
        String jboName = jbo.getJboName();
        JboSetIFace newjboset = JboUtil.getJboSet(jboName);

        if (null != newjboset) {
            DataQueryInfo dqInfo = newjboset.getQueryInfo();
            dqInfo.setPageSize(1);// 只查询一条即可
            String uidname = jbo.getUidName();
            String uidvalue = jbo.getUidValue();
            if (slibing == JxConstant.SLIBING_PREVIOUS) {
                dqInfo.setWhereCause(uidname + " < ? ");
                dqInfo.setOrderby(uidname + " DESC ");
                dqInfo.setWhereParams(new Object[] { uidvalue });
            } else if (slibing == JxConstant.SLIBING_NEXT) {
                dqInfo.setWhereCause(uidname + " > ? ");
                dqInfo.setOrderby(uidname + " ASC ");
                dqInfo.setWhereParams(new Object[] { uidvalue });
            }
            List<JboIFace> jbolist = newjboset.query();

            if (jbolist != null && !jbolist.isEmpty()) {
                return jbolist.get(0);
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
        return findJbo(jboName, whereCause, params);
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
