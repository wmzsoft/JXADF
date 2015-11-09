package com.jxtech.jbo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxRelationship;
import com.jxtech.jbo.base.JxRelationshipDao;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;

/**
 * JboSet的查询条件信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class DataQueryInfo implements java.io.Serializable {
    private static final long serialVersionUID = -7519580072622194415L;
    private String whereCause;// 查询条件
    private String quickSearchQueryValue;
    private String quickSearchCause;
    private String orderby;// 排序
    private String allCause;// 全部查询条件
    private Object[] whereParams;// 查询参数

    private String relationshipCause;// 联系条件
    private Object[] relationshipParams;// 联系参数

    private int pageSize = 20;// 页大小
    private int pageNum = 1;// 当前第几页
    // 查询参数，例：Map<"money>?",1000>
    private Map<String, Object> params;
    // 组装之后的所有参数信息
    private Object[] whereAllParams;

    // 应用程序限制条件，此条件一经设定，不能修改
    private String restrictions;

    private String orgsiteClause;// 组织地点查询条件
    private String[] orgsiteParams;// 组织地点参数值。

    private JboSetIFace jboset;
    private boolean ignoreSecurityrestrict;// 是否忽略安全限制条件。

    private static final Logger LOG = LoggerFactory.getLogger(DataQueryInfo.class);

    /**
     * 获得这个查询的ID值
     * 
     * @param cache 是否需要重新计算查询条件。
     * @return
     */
    public int getQueryId(boolean cache) {
        if (!cache) {
            getWhereAllCause();
        }
        StringBuilder id = new StringBuilder();
        if (jboset != null) {
            String jn = jboset.getJboname();
            if (jn != null) {
                id.append(jn).append(".");
            }
        }
        if (allCause != null) {
            id.append(allCause).append(".");
        }
        if (whereAllParams != null) {
            for (int i = 0; i < whereAllParams.length; i++) {
                id.append(whereAllParams[i]).append(".");
            }
        }
        if (orderby != null) {
            id.append(orderby);
        }
        id.append(pageSize).append(".");
        id.append(pageNum).append(".");
        // 一定要转换为字符串再取Hashcode，否则每次取的得值不一样。
        return id.toString().hashCode();
    }

    // 获得组装之后的所有查询条件
    public String getWhereAllCause() {
        StringBuilder cause = new StringBuilder();
        List<Object> list = new ArrayList<Object>();
        if (!StrUtil.isNull(whereCause)) {
            cause.append(whereCause);
            putParamsValueAll(whereParams, list);
        }

        // 组织地点的问题
        if (!StrUtil.isNullOfIgnoreCaseBlank(orgsiteClause)) {
            if (cause.length() > 3) {
                cause.insert(0, '(').append(") AND (").append(orgsiteClause).append(')');
            } else {
                cause.setLength(0);
                cause.append(orgsiteClause);
            }
            if (orgsiteParams != null) {
                putParamsValueAll(orgsiteParams, list);
            }
        }

        // 快速查询
        String quickSearchCause = getQuickSearchCause();
        if (!StrUtil.isNullOfIgnoreCaseBlank(quickSearchCause)) {
            if (cause.length() > 2) {
                cause.insert(0, '(').append(") AND (").append(quickSearchCause).append(')');
            } else {
                cause.setLength(0);
                cause.append(quickSearchCause);
            }
            // 如果有快速查询，将查询对话框中设置的查询条件清空
            params = null;
        }

        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                if (StrUtil.isNull(key)) {
                    continue;
                }
                Object value = entry.getValue();
                // LOG.debug("key:" + key + "  value:" + value);
                if (value == null || (value instanceof String && StrUtil.isNull((String) value))) {
                    if (key.indexOf("?") > 0) {
                        continue;
                    }
                }
                if (key.indexOf("?") >= 0) {
                    if (StrUtil.indexOfignoreCase(key, " like ") > 0) {
                        list.add("%" + value + "%");
                    } else if (StrUtil.indexOfignoreCase(key, " startwith ") > 0) {
                        list.add(value + "%");
                    } else if (StrUtil.indexOfignoreCase(key, " endwith ") > 0) {
                        list.add("%" + value);
                    } else {
                        list.add(value);
                    }
                }
                if (cause.length() > 2) {
                    cause.append(" And ").append(key);
                } else {
                    cause.setLength(0);
                    cause.append(key);
                }
            }
        }

        // 安全限制条件
        String secr = getSecurityrestrict();
        if (!StrUtil.isNullOfIgnoreCaseBlank(secr)) {
            if (cause.length() > 2) {
                cause.insert(0, '(').append(") AND (").append(secr).append(')');
            } else {
                cause.setLength(0);
                cause.append(secr);
            }
        }
        if (cause.length() < 3) {
            cause.setLength(0);
            cause.append(" 1=1 ");
        }

        // 联系条件
        if (!StrUtil.isNullOfIgnoreCaseBlank(relationshipCause)) {
            cause.insert(0, '(').append(") AND (").append(relationshipCause).append(')');
            putParamsValueAll(relationshipParams, list);
        }
        // 应用程序条件限制
        if (!StrUtil.isNullOfIgnoreCaseBlank(restrictions)) {
            cause.insert(0, '(').append(") AND (").append(restrictions).append(')');
        }
        whereAllParams = list.toArray();
        allCause = ELUtil.getElValue(jboset, null, JxSession.getJxUserInfo(), cause.toString());
        return allCause;
    }

    /**
     * 得到安全限制条件
     * 
     * @return
     */
    public String getSecurityrestrict() {
        if (jboset == null || ignoreSecurityrestrict) {
            return null;
        }
        try {
            return jboset.getSecurityrestrict(true);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public void putParams(String key, String value) {
        putParams(key, value, true);
    }

    /**
     * 将数据放到List中。
     * 
     * @param values
     * @param list
     */
    public void putParamsValueAll(Object[] values, List<Object> list) {
        if (values == null) {
            return;
        }
        if (list == null) {
            list = new ArrayList<Object>();
        }
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
    }

    /**
     * 将条件put 到 params对象中。
     * 
     * @param key column = ?
     * @param value
     * @param overlay 是否覆盖
     */
    public void putParams(String key, String value, boolean overlay) {
        if (StrUtil.isNull(key)) {
            return;
        }
        getParams();
        String[] keys = key.split(",");
        if (value == null) {
            value = "";
        }
        String[] values = value.split(",");
        for (int i = 0; i < keys.length; i++) {
            if (overlay == false && params.containsKey(keys[i])) {
                continue;
            }
            if (i < values.length) {
                params.put(keys[i], values[i]);
            } else {
                params.put(keys[i], "");
            }
        }
    }

    public String getWhereCause() {
        return whereCause;
    }

    public void setWhereCause(String whereCause) {
        this.whereCause = whereCause;
    }

    public String getJboname() {
        return jboset.getJboname();
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) throws JxException {
        // 对于relationship排序进行处理
        if (!StrUtil.isNull(orderby) && orderby.indexOf(".") > 0) {
            String[] orderbys = orderby.split(" ");
            String[] relationshipOrderbys = null;
            if (orderbys[0].indexOf("(") != -1 && orderbys[0].indexOf(")") != -1) {
                if (orderbys[0].substring(orderbys[0].indexOf("(") + 1, orderbys[0].indexOf(")")).indexOf(".") != -1) {
                    // 例如排序GET_ASSETSORTNUM(PS_PGC_ESISASSET_ASSET_ASSETID.ASSETNUM)
                    relationshipOrderbys = orderbys[0].substring(orderbys[0].indexOf("(") + 1, orderbys[0].indexOf(")")).split("\\.");
                } else {
                    // 例如排序PKG_MCPMS.PF_GETWBSSORT(MATERIAL_WBS)
                    this.orderby = orderby;
                    return;
                }
            } else {
                relationshipOrderbys = orderbys[0].split("\\.");
            }
            String sortMode = orderbys[1];

            String relationshipname = relationshipOrderbys[0];
            String relationshipOrderBy = relationshipOrderbys[1];
            String jboname = getJboname();
            JxRelationship ship = JxRelationshipDao.getJxRelationship(jboname, relationshipname);
            if (null != ship) {
                String where = ship.getWhereclause();
                where = where.replace(":", jboname + ".");
                if (orderbys[0].indexOf("(") != -1 && orderbys[0].indexOf(")") != -1) {
                    relationshipOrderBy = orderbys[0].substring(0, orderbys[0].indexOf("(")) + "(" + relationshipOrderBy + ")";
                }
                orderby = "(select " + relationshipOrderBy + " from " + ship.getChild() + " where (" + where + ") " + " ) " + sortMode;
            }
        }

        this.orderby = orderby;
    }

    /**
     * 根据jbo名称和relationship查询条件
     * 
     * @param jboName
     * @param relationshipDataAttr
     * @param cause
     * @return
     * @throws JxException
     */
    public String getRelationShipCauseCondition(String jboName, String relationshipDataAttr, String cause) throws JxException {
        StringBuilder condition = new StringBuilder();
        String[] relateArray = relationshipDataAttr.split("\\.");
        String relationShip = relateArray[0];
        String columnName = relateArray[1];
        JxRelationship ship = JxRelationshipDao.getJxRelationship(jboName, relationShip);
        String where = ship.getWhereclause();
        where = where.replace(":", jboName + ".");
        condition.append(" EXISTS (SELECT * FROM ").append(ship.getChild()).append(" WHERE ");
        condition.append(columnName).append(" ").append(cause).append(" AND ").append(where).append(" ) ");
        return condition.toString();
    }

    public Object[] getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(Object[] whereParams) {
        this.whereParams = whereParams;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Object[] getWhereAllParams() {
        return whereAllParams;
    }

    public String getRelationshipCause() {
        return relationshipCause;
    }

    public void setRelationshipCause(String relationshipCause) {
        this.relationshipCause = relationshipCause;
    }

    public Object[] getRelationshipParams() {
        return relationshipParams;
    }

    public void setRelationshipParams(Object[] relationshipParams) {
        this.relationshipParams = relationshipParams;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getAllCause() {
        return allCause;
    }

    public void setAllCause(String allCause) {
        this.allCause = allCause;
    }

    public String getQuickSearchQueryValue() {
        return quickSearchQueryValue;
    }

    public void setQuickSearchQueryValue(String quickSearchQueryValue) {
        this.quickSearchQueryValue = quickSearchQueryValue;
    }

    public String getQuickSearchCause() {
        return quickSearchCause;
    }

    public void setQuickSearchCause(String quickSearchCause) {
        this.quickSearchCause = quickSearchCause;
    }

    public String getOrgsiteClause() {
        return orgsiteClause;
    }

    public void setOrgsiteClause(String orgsiteClause) {
        this.orgsiteClause = orgsiteClause;
    }

    public String[] getOrgsiteParams() {
        return orgsiteParams;
    }

    public void setOrgsiteParams(String[] orgsiteParams) {
        this.orgsiteParams = orgsiteParams;
    }

    public String getAppname() {
        if (null == jboset)
            return "";
        return jboset.getAppname();
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public boolean isIgnoreSecurityrestrict() {
        return ignoreSecurityrestrict;
    }

    public void setIgnoreSecurityrestrict(boolean ignoreSecurityrestrict) {
        this.ignoreSecurityrestrict = ignoreSecurityrestrict;
    }

}
