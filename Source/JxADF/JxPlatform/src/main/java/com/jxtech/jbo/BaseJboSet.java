package com.jxtech.jbo;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.io.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxwxp.JxWXPExport;
import com.jxtech.app.jxwxp.JxWXPFactory;
import com.jxtech.app.jxwxp.JxWXPValueAdapter;
import com.jxtech.app.max.MaxFactory;
import com.jxtech.app.max.MaxSequenceSetIFace;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.base.DataMap;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxAttributeDao;
import com.jxtech.jbo.base.JxAttributeProp;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxObjectDao;
import com.jxtech.jbo.base.JxRelationship;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.base.JxTableDao;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.table.Table;
import com.jxtech.tag.table.Tablecol;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 基类JboSet,此类不操作数据库
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 */
public abstract class BaseJboSet implements JboSetIFace {

    private static final long serialVersionUID = 6995868668178309701L;
    private static final Logger LOG = LoggerFactory.getLogger(BaseJboSet.class);
    protected JboIFace currentJbo;// 当前记录
    private String jboname;// 表名
    private String appname;// 当前应用程序名
    private List<JboIFace> jbolist = new ArrayList<JboIFace>();// 记录集
    // MaxAttribute 当前记录的字段定义信息 HashMap<String,JxAttribute>，这个属性是静态的
    private Map<String, JxAttribute> jxAttributes;
    // 每个字段的动态属性信息
    private Map<String, JxAttributeProp> jxAttributeProp;
    // 自动增加序列
    private Map<String, JxAttribute> autokeysAttributes;
    // MaxTable 当前表的定义
    private JxTable jxTable;
    // MaxRelationship 当前主表中，包括的联系定义
    private Map<String, JxRelationship> jxRelationships;
    // 获得当前对象的信息
    private JxObject jxobject;
    private DataQueryInfo queryInfo;
    private JboIFace parent;
    private int count = -1;// 记录数
    private boolean readonly;
    private boolean required;

    private String dbtype;// 数据库类型，包括：Oracle、MSSQLSERVER、MYSQL等
    private String dataSourceName;// 数据源，默认为base.properties中配置的数据源，可以指定JxDataSyncSource表中的数据源

    // 通过父对象添加的对应需要通过relationshipname反查自身
    private String relationshipname;

    private String sql;// 通过SQL语句查询数据结果集,当Jboname为空或以SQL_开头时此值有效
    // 当前记录的保存标识
    private long saveFlag = 0xFFFFFF;

    // 查询标识
    private long queryFlag = 0;

    // 保存Session，在有些线程中取不到，就直接在这里取了。
    private HttpSession session;

    /**
     * 命名规则：appname,appnameSet 子类必须覆盖此方法
     * 
     * @return
     * @throws JxException
     */
    protected JboIFace getJboInstance() throws JxException {
        throw new JxException(JxLangResourcesUtil.getString("jboset.instance.jbo"));
    }

    @Override
    public boolean beforeAdd() throws JxException {
        return true;
    }

    @Override
    public boolean canAdd() throws JxException {
        return true;
    }

    /**
     * 是否可以缓存当前结果集，默认为false，可以重载、修改
     * 
     * @return
     * @throws JxException
     */
    public boolean canCache() throws JxException {
        return false;
    }

    @Override
    public JboIFace add() throws JxException {
        if (canAdd()) {
            beforeAdd();
            currentJbo = getJboInstance();
            currentJbo.add();
            if (jbolist == null) {
                jbolist = new ArrayList<JboIFace>();
            }
            jbolist.add(currentJbo);
            return currentJbo;
        }
        return null;
    }

    @Override
    public JxAttribute getJxAttribute(String attributeName) throws JxException {
        if (getJxAttributes() == null || attributeName == null) {
            return null;
        }
        return jxAttributes.get(attributeName);
    }

    @Override
    public List<JboIFace> getJbolist() {
        return jbolist;
    }

    @Override
    public void setJbolist(List<JboIFace> jbolist) {
        this.jbolist = jbolist;
    }

    public boolean delete(Connection conn, JboIFace jbi) throws JxException {
        return delete(conn, jbi, true);
    }

    /**
     * 删除Jbo中所有的记录
     * 
     * @return
     * @throws JxException
     */
    public boolean delete() throws JxException {
        List<JboIFace> list = getJbolist();
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (!delete(null, list.get(i), false)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 仅仅标记为删除，不执行真正地删除操作。
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    @Override
    public boolean delRow(String uid) throws JxException {
        JboIFace jbi = getJboOfUid(uid);
        return delete(null, jbi, false);
    }

    @Override
    public boolean unDelRow(String uid) throws JxException {
        JboIFace jbi = getJboOfUid(uid);
        if (jbi == null) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.not.jbo") + "\r\n" + uid);
        }
        return jbi.unDelete();
    }

    public JboIFace getJboOfIndex(int index) throws JxException {
        if (jbolist == null) {
            return null;
        }
        int size = jbolist.size();
        if (size > index && index >= 0) {
            return jbolist.get(index);
        }
        return null;
    }

    @Override
    public JboIFace getJboOfUid(String uid) throws JxException {
        if (StrUtil.isNull(uid) || getJbolist() == null) {
            return currentJbo;
        }
        if (currentJbo != null && uid.equals(currentJbo.getUidValue())) {
            return currentJbo;
        }
        int count = jbolist.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                JboIFace dto = jbolist.get(i);
                if (uid.equals(dto.getUidValue())) {
                    currentJbo = dto;
                    return currentJbo;
                }
            }
        }
        return currentJbo;
    }

    /**
     * 查询结果
     */
    @Override
    public List<JboIFace> query() throws JxException {
        return query(null);
    }

    /**
     * 查询结果集
     * 
     * @param shipname
     *            联系名
     * @return
     * @throws JxException
     */
    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        if (canCache()) {
            String key = CacheUtil.genJboSetKey(this, true);
            return CacheUtil.getJboSetList(key);
        }
        return null;
    }

    @Override
    public List<JboIFace> queryAll() throws JxException {
        DataQueryInfo dqi = getQueryInfo();
        dqi.setPageNum(-1);
        dqi.setPageSize(-1);
        return query();
    }

    @Override
    public void setFlag() throws JxException {
        if (currentJbo != null) {
            currentJbo.setModify(false);
            currentJbo.setToBeAdd(false);
            currentJbo.setToBeDel(false);
            currentJbo.setJboValueModifyAll(false);
        }
        List<JboIFace> list = getJbolist();
        if (list != null) {
            int ls = list.size();
            for (int i = 0; i < ls; i++) {
                JboIFace jbo = list.get(i);
                if (jbo == null) {
                    continue;
                }
                jbo.setModify(false);
                jbo.setToBeAdd(false);
                jbo.setToBeDel(false);
                jbo.setJboValueModifyAll(false);

                Map<String, JboSetIFace> children = jbo.getChildren();
                if (children != null) {
                    for (Map.Entry<String, JboSetIFace> entry : children.entrySet()) {
                        JboSetIFace childSet = entry.getValue();
                        childSet.setFlag();
                    }
                }
                List<JboIFace> needJboList = jbo.getNeedSaveList();
                if (needJboList != null) {
                    int size = needJboList.size();
                    for (int k = 0; k < size; k++) {
                        needJboList.get(k).getJboSet().setFlag();
                    }
                }
            }
        }
    }

    /**
     * 获得查询条件
     * 
     * @param flag
     *            ClearParams 清除之前记忆的查询条件。
     * @return
     */
    @Override
    public DataQueryInfo getQueryInfo(String flag) {
        if (flag != null) {
            if ("ClearParams".equalsIgnoreCase(flag)) {
                getQueryInfo().getParams().clear();
            }
        }
        return getQueryInfo();
    }

    @Override
    public String getJboname() {
        return jboname;
    }

    @Override
    public void setJboname(String jboname) {
        if (jboname != null) {
            this.jboname = jboname.toUpperCase();
        } else {
            this.jboname = null;
        }
    }

    @Override
    public int getCount() {
        if (count < 0) {
            try {
                this.count = count();
            } catch (JxException e) {
                this.count = 0;
            }
        }
        return count;
    }

    public int getCount(boolean flag) {
        if (flag) {
            try {
                this.count = count();
            } catch (JxException e) {
                this.count = 0;
            }
            return count;
        }
        return getCount();
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public JboIFace getJbo() throws JxException {
        return currentJbo;
    }

    @Override
    public void setJbo(JboIFace jbo) {
        this.currentJbo = jbo;
    }

    @Override
    public JboIFace getJbo(boolean isnew) throws JxException {
        if (currentJbo == null && isnew) {
            currentJbo = this.getJboInstance();
        }
        return getJbo();
    }

    @Override
    public Map<String, JxAttribute> getJxAttributes() throws JxException {
        if (jxAttributes == null || jxAttributes.isEmpty()) {
            jxAttributes = JxAttributeDao.getObject(getJboname());
        }
        return jxAttributes;
    }

    public void setJxAttributes(Map<String, JxAttribute> jxAttributes) {
        this.jxAttributes = jxAttributes;
    }

    /**
     * 返回Autokey的字段信息
     * 
     * @return
     * @throws JxException
     */
    @Override
    public Map<String, JxAttribute> getAutokeysAttributes() throws JxException {
        if (autokeysAttributes != null) {
            return autokeysAttributes;
        }
        if (jxAttributes == null) {
            getJxAttributes();
        }
        if (jxAttributes != null) {
            if (autokeysAttributes == null) {
                autokeysAttributes = new HashMap<String, JxAttribute>();
            }
            for (Entry<String, JxAttribute> entry : jxAttributes.entrySet()) {
                JxAttribute attr = entry.getValue();
                if (null != attr && "&AUTOKEY&".equalsIgnoreCase(attr.getDefaultValue())) {
                    autokeysAttributes.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return autokeysAttributes;
    }

    @Override
    public JxTable getJxTable() throws JxException {
        if (jxTable == null) {
            jxTable = JxTableDao.getTable(getEntityname());
        }
        return jxTable;
    }

    public void setJxTable(JxTable jxTable) {
        this.jxTable = jxTable;
    }

    @Override
    public JxObject getJxobject() throws JxException {
        if (jxobject == null) {
            jxobject = JxObjectDao.getJxObject(getJboname());
        }
        return jxobject;
    }

    public void setJxobject(JxObject jxobject) {
        this.jxobject = jxobject;
    }

    @Override
    public String getUidName() throws JxException {
        if (getJxTable() != null) {
            return jxTable.getUniquecolumnname();
        }
        LOG.info("请配置表的主关键字：jboname=" + jboname);
        return null;
    }

    @Override
    public String getEntityname() throws JxException {
        if (getJxobject() != null) {
            return jxobject.getEntityname();
        }
        return jboname;

    }

    public String toJson() throws JxException {
        return toJson(null, true);
    }

    /**
     * 返回JSON格式的字符串
     * 
     * @param attributes
     *            字段名,为空则显示所有字段信息
     */
    @Override
    public String toJson(String[] attributes) throws JxException {
        return toZTreeJson(attributes, null, null, null, null, true, "", "");
    }

    @Override
    public DataQueryInfo getQueryInfo() {
        if (queryInfo == null) {
            queryInfo = new DataQueryInfo();
            queryInfo.setJboset(this);
            // 添加多组织、多地点的限制条件
            try {
                fillOrgSiteCause();
            } catch (JxException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return queryInfo;
    }

    /**
     * 填充组织地点的限制条件
     */
    protected void fillOrgSiteCause() throws JxException {
        JxUserInfo jui = JxSession.getJxUserInfo();
        if (jui == null) {
            // 未登录，不需要添加了
            return;
        }
        String orgid = jui.getOrgid();
        String siteid = jui.getSiteid();
        if (StrUtil.isNull(orgid) && StrUtil.isNull(siteid)) {
            // 未使用组织地点，不需要添加了
            return;
        }
        getJxobject();
        if (jxobject == null || !jxobject.isOrgSiteType()) {
            // 不是组织或地点表
            return;
        }
        Map<String, JxAttribute> attrs = getJxAttributes();
        if (jxobject.isSiteType()) {
            // 地点级别处理
            if (StrUtil.isNull(siteid)) {
                // 登录用户，地点为空，则不处理
                return;
            }
            if (!attrs.containsKey("SITEID")) {
                // 不存在这个字段，不处理
                return;
            }
            // 获得角色能管理的所有地点。
            StringBuilder sb = new StringBuilder();
            sb.append("( siteid = ? or siteid in (select siteid from pubrolesite ");
            sb.append(" where roleid in ( select role_id from pub_role_user where user_id=?) ))");
            queryInfo.setOrgsiteClause(sb.toString());
            queryInfo.setOrgsiteParams(new String[] { siteid, jui.getUserid() });
        } else if (jxobject.isOrgType()) {
            // 组织级别处理
            if (StrUtil.isNull(orgid)) {
                return;
            }
            if (!attrs.containsKey("ORGID")) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("( orgid = ? or orgid in (select orgid from pubroleorg ");
            sb.append(" where roleid in ( select role_id from pub_role_user where user_id=?) ))");
            queryInfo.setOrgsiteClause(sb.toString());
            queryInfo.setOrgsiteParams(new String[] { orgid, jui.getUserid() });
        }

    }

    @Override
    public void setQueryInfo(DataQueryInfo queryInfo) {
        this.queryInfo = queryInfo;
    }

    /**
     * 返回JSON数据格式
     * 
     * @param attributes
     *            属性名
     * @head 是否返回头部
     */
    public String toJson(String[] attributes, boolean head) throws JxException {
        StringBuilder sb = new StringBuilder();
        String[] columns = attributes;
        if (attributes == null) {
            columns = ((DataMap<String, JxAttribute>) getJxAttributes()).keys();
        }
        int size = jbolist.size();
        sb.append("[");
        if (head) {
            DataQueryInfo dqi = getQueryInfo();
            sb.append("{\"pagesize\":\"");
            sb.append(dqi.getPageSize());
            sb.append("\",\"pagenum\":\"");
            sb.append(dqi.getPageNum());
            sb.append("\",\"count\":\"");
            sb.append(count);
            sb.append("\",\"jboname\":\"");
            sb.append(jboname);
            sb.append("\"}");
            if (size > 0) {
                sb.append(",");
            }
        }
        for (int i = 0; i < size; i++) {
            sb.append("{");
            for (int j = 0; j < columns.length; j++) {
                String col1 = columns[j];
                String col2 = columns[j];
                int pos = columns[j].indexOf(':');
                if (pos > 0 && pos < columns[j].length()) {
                    col1 = columns[j].substring(0, pos);
                    col2 = columns[j].substring(pos + 1);
                }
                sb.append("\"");
                sb.append(col2);
                sb.append("\":\"");
                String val = jbolist.get(i).getString(col1);
                if (val != null) {
                    sb.append(StrUtil.toJson(val));
                }
                sb.append("\"");
                if (j < columns.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            if (i < (size - 1)) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 返回zTree的JSON格式的字符串
     * 
     * @param attributes
     *            需要返回的字段名
     * @param idName
     *            唯一关键字（与父节点关联）的字段名
     * @param parentIdName
     *            父节点字段名
     * @param name
     *            显示
     * @param hasChildName
     *            是否有孩子节点字段名
     * @param leafDisplay
     *            是否显示叶子节点,true显示，false不显示
     * @return
     */
    @Override
    public String toZTreeJson(String[] attributes, String idName, String parentIdName, String name, String hasChildName, boolean leafDisplay, String root, String i18n) throws JxException {
        ResourceBundle zTreeBundle = null;
        StringBuilder sb = new StringBuilder();
        boolean isCustomRoot = !StrUtil.isNull(root);
        boolean isI18n = !StrUtil.isNull(i18n);
        if (isI18n) {
            zTreeBundle = JxLangResourcesUtil.getResourceBundle("res.tree." + i18n);
        }
        String[] columns;
        if (attributes == null && getJxAttributes() != null) {
            columns = ((DataMap<String, JxAttribute>) getJxAttributes()).keys();
        } else {
            columns = attributes;
        }
        if (columns == null) {
            return sb.toString();
        }
        if (getJbolist() == null) {
            query();
        }
        if (getJbolist() == null) {
            return sb.toString();
        }
        int size = getJbolist().size();
        // 如果知道父节点，则可以自动检测
        if (StrUtil.isNull(idName)) {
            idName = getUidName();
        }
        // 添加自定义根节点
        if (isCustomRoot && size > 0) {
            sb.append("{");
            if (columns[0].indexOf(':') > 0) {
                sb.append(columns[0].split(":")[0].toLowerCase()).append(":\"-1\",");
            } else {
                sb.append(columns[0].toLowerCase()).append(":\"-1\",");
            }

            if (columns[1].indexOf(':') > 0) {
                sb.append(columns[1].split(":")[0].toLowerCase()).append(":\"-1\",");
            } else {
                sb.append(columns[1].toLowerCase()).append(":\"\",");
            }

            if (columns[2].indexOf(':') > 0) {
                sb.append(columns[2].split(":")[0].toLowerCase()).append(":\"-1\",");
            } else {
                if (isI18n) {
                    if (zTreeBundle.containsKey(root)) {
                        sb.append(columns[2].toLowerCase()).append(":\"").append(zTreeBundle.getString(root)).append("\"");
                    } else {
                        sb.append(columns[2].toLowerCase()).append(":\"").append(root).append("\"");
                    }
                } else {
                    sb.append(columns[2].toLowerCase()).append(":\"").append(root).append("\"");
                }

            }
            sb.append("},");
        }
        // 添加数据
        for (int s = 0; s < size; s++) {
            JboIFace dto = getJbolist().get(s);
            boolean isLeaf = false;// 默认为不是叶子节点
            if (!StrUtil.isNull(hasChildName) && !"false".equalsIgnoreCase(hasChildName) && !"true".equalsIgnoreCase(hasChildName)) {
                isLeaf = !dto.getBoolean(hasChildName);
            } else if (!StrUtil.isNull(parentIdName)) {
                DataQuery dq = DBFactory.getDataQuery(dbtype, dataSourceName);
                isLeaf = !dq.exist(getEntityname(), parentIdName, dto.getString(idName));
            }
            if (isLeaf && !leafDisplay) {
                continue;
            }
            sb.append("{");
            for (int i = 0; i < columns.length; i++) {
                if (columns[i] == null) {
                    continue;
                }
                String column = columns[i];
                String val = "";
                if (column.equalsIgnoreCase(name) && isI18n) {
                    if (zTreeBundle.containsKey(dto.getString(idName))) {
                        val = zTreeBundle.getString(dto.getString(idName));
                    } else {
                        val = dto.getString(name);
                    }
                } else if (column.indexOf(':') > 0) {
                    String[] cols = column.split(":");
                    column = cols[0];
                    if (cols[1].indexOf(',') > 0) {
                        String[] vals = cols[1].split(",");
                        StringBuilder sbval = new StringBuilder();
                        int vlen = vals.length;
                        for (int v = 0; v < vlen; v++) {
                            sbval.append(dto.getString(vals[v]));
                            if (v + 1 < vlen) {
                                sbval.append('_');
                            }
                        }
                        val = sbval.toString();
                    }

                } else {
                    val = dto.getString(columns[i]);
                }
                if (val != null) {
                    val = StrUtil.toJson(val);
                } else {
                    val = "";
                }
                if (columns[i].equalsIgnoreCase(parentIdName) && "".equals(val)) {
                    sb.append(column.toLowerCase()).append(":\"-1\"");
                } else {
                    sb.append(column.toLowerCase()).append(":\"").append(val).append("\"");
                }

                if (i < columns.length - 1) {
                    sb.append(",");
                }
            }
            if (!StrUtil.isNull(hasChildName)) {
                if ("true".equalsIgnoreCase(hasChildName)) {
                    sb.append(",isParent:true");
                } else if ("false".equalsIgnoreCase(hasChildName)) {
                    sb.append(",isParent:false");
                } else {
                    sb.append(",isParent:" + !isLeaf);
                }
            } else if (!StrUtil.isNull(parentIdName)) {
                sb.append(",isParent:" + !isLeaf);
            }
            if (s < size - 1) {
                sb.append("},\r\n");
            } else {
                sb.append("}");
            }
        }
        // LOG.debug("JSON:\r\n"+sb.toString());
        return sb.toString();
    }

    @Override
    public void tableQuickSearch(String searchValue) throws JxException {
        DataQueryInfo dqInfo = this.getQueryInfo();
        if (StrUtil.isNullOfIgnoreCaseBlank(searchValue)) {
            dqInfo.setQuickSearchQueryValue("");
            dqInfo.setQuickSearchCause("");
            return;
        }
        String sv = searchValue.toLowerCase().trim().replaceAll("'", "");
        StringBuilder sb = new StringBuilder();
        Map<String, JxAttribute> attrs = this.getJxAttributes();
        for (Map.Entry<String, JxAttribute> entry : attrs.entrySet()) {
            String key = entry.getKey();
            if (key.indexOf('.') < 0) {
                JxAttribute colu = entry.getValue();
                if (colu.isNumOrDateTime() || !colu.isPersistent() || colu.isBoolean()) {
                    continue;
                }
                sb.append(" lower(").append(key).append(") like '%");
                sb.append(sv);
                sb.append("%' or ");
            }
        }

        if (sb.length() > 4) {
            String quickSql = sb.substring(0, sb.length() - 4);
            dqInfo.setQuickSearchQueryValue(searchValue);
            dqInfo.setQuickSearchCause(quickSql);
        }
    }

    /**
     * 返回某个属性的最大值
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public Object max(String attributename) throws JxException {
        DataQuery dq = DBFactory.getDataQuery(dbtype, dataSourceName);
        DataQueryInfo dqi = getQueryInfo();
        return dq.max(getEntityname(), attributename, dqi.getWhereAllCause(), dqi.getWhereAllParams());
    }

    /**
     * 返回最小值
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public Object min(String attributename) throws JxException {
        DataQuery dq = DBFactory.getDataQuery(dbtype, dataSourceName);
        DataQueryInfo dqi = getQueryInfo();
        return dq.min(getEntityname(), attributename, dqi.getWhereAllCause(), dqi.getWhereAllParams());
    }

    /**
     * 统计合
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public double sum(String attributename) throws JxException {
        DataQuery dq = DBFactory.getDataQuery(dbtype, dataSourceName);
        DataQueryInfo dqi = getQueryInfo();
        return dq.sum(getJboname(), attributename, dqi.getWhereAllCause(), dqi.getWhereAllParams());
    }

    public Object invokeGetMethod(String name) {
        try {
            Method m = getClass().getMethod(name);
            return m.invoke(this);
        } catch (SecurityException e) {
            LOG.debug(e.getMessage());
        } catch (NoSuchMethodException e) {
            // 不需处理
        } catch (Exception e) {
            LOG.debug(e.getMessage());
        }
        return null;
    }

    public Object invokeMethod(String name, Class<?>[] parameterTypes, Object[] params) {
        try {
            Method m;
            if (parameterTypes != null && parameterTypes.length > 0) {
                m = getClass().getMethod(name, parameterTypes);
                return m.invoke(this, params);
            } else {
                m = getClass().getMethod(name);
                return m.invoke(this);
            }
        } catch (SecurityException e) {
            LOG.debug(e.getMessage());
        } catch (NoSuchMethodException e) {
            // 不需处理
            LOG.debug(e.getMessage());
        } catch (Exception e) {
            LOG.debug(e.getMessage());
        }
        return null;
    }

    @Override
    public String getAppname() {
        return appname;
    }

    @Override
    public void setAppname(String appname) {
        this.appname = appname;
    }

    @Override
    public JboIFace getParent() throws JxException {
        return parent;
    }

    @Override
    public void setParent(JboIFace parent) throws JxException {
        this.parent = parent;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    public JboIFace getCurrentJbo() {
        return currentJbo;
    }

    public void setCurrentJbo(JboIFace currentJbo) {
        this.currentJbo = currentJbo;
    }

    public Map<String, JxRelationship> getJxRelationships() {
        return jxRelationships;
    }

    public void setJxRelationships(Map<String, JxRelationship> jxRelationships) {
        this.jxRelationships = jxRelationships;
    }

    public String getRelationshipname() {
        return this.relationshipname;
    }

    public void setRelationshipname(String relationshipname) {
        this.relationshipname = relationshipname;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * 最简单的实现，还有很多需要完善和修改
     */
    @Override
    public FileTransfer expExcel(String title, String uids) throws JxException {
        List<JboIFace> list = null;
        if (StrUtil.isNull(uids)) {
            queryAll();
            list = this.getJbolist();
        } else {
            list = this.queryJbo(StringUtils.split(uids, ","));
        }
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        for (JboIFace jf : list) {
            Map<String, Object> map = jf.getData();
            datas.add(map);
        }
        JxWXPExport wxp = JxWXPFactory.getInstance();
        return wxp.exportExcel(title, this.getExcelColumnAndTitle(), datas, null, this.getExcelAdapter());
    }

    public FileTransfer expExcel(String title, String uids, List<Map<String, Object>> dataMap) throws JxException {
        if (dataMap != null) {
            JxWXPExport wxp = JxWXPFactory.getInstance();
            return wxp.exportExcel(title, this.getExcelColumnAndTitle(), dataMap, null, this.getExcelAdapter());
        } else {
            return expExcel(title, uids);
        }
    }

    private Map<String, String> getExcelColumnAndTitle() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Table table = this.jxTable.getTableModle();
        if (table != null) {
            List<Tablecol> cols = table.getColumns();
            for (Tablecol col : cols) {
                String startHtml = col.getStartHtml();
                if (StringUtils.isEmpty(col.getVisibleHead()) && !StringUtils.isEmpty(startHtml)) {
                    // 移除中英文分号
                    map.put(col.getDataattribute(), StrUtil.removeLastChar(startHtml));
                } else {
                    map.put(col.getDataattribute(), StrUtil.removeSpecialChar(col.findLabel()));
                }
            }
        }
        return this.modifyExcelColumn(map);
    }

    @Override
    public Map<String, String> modifyExcelColumn(Map<String, String> header) {
        return header;
    }

    @Override
    public Map<String, JxWXPValueAdapter> getExcelAdapter() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 是否可以保存此集合
     * 
     * @return
     * @throws JxException
     */
    public boolean canSave() throws JxException {
        if ((saveFlag & JboIFace.SAVE_NO_CHECK_ALL) == JboIFace.SAVE_NO_CHECK_ALL) {
            return true;
        }
        // 如果做只读校验,并且只读,则返回false，保存失败
        if (readonly && !((saveFlag & JboIFace.SAVE_NO_CHECK_READONLY) == JboIFace.SAVE_NO_CHECK_READONLY)) {
            LOG.warn("不能保存此记录集" + this.getJboname() + ",saveFlag=" + saveFlag);
            // 此功能问题太多，暂是屏蔽
            // throw new JxException(JxLangResourcesUtil.getString("baseJboSet.canSave.failed.readonly", new Object[] { getJboname() }));
        }
        // 如果要做权限检查
        if (!((saveFlag & JboIFace.SAVE_NO_CHECK_PERMISSION) == JboIFace.SAVE_NO_CHECK_PERMISSION)) {
            boolean flag = PermissionFactory.getPermissionInstance().hasFunctions(getAppname(), "SAVE");
            if (!flag) {
                readonly = !flag;
                throw new JxException(JxLangResourcesUtil.getString("baseJboSet.canSave.failed.nopermission", new Object[] { appname }));
            }
            return flag;
        }
        return true;
    }

    public long getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(long saveFlag) {
        this.saveFlag = saveFlag;
    }

    public JxAttributeProp getJxAttributeProp(String dataattribute) throws JxException {
        if (StrUtil.isNull(dataattribute)) {
            return null;
        }
        if (jxAttributeProp == null) {
            jxAttributeProp = new HashMap<String, JxAttributeProp>();
        }
        String key = dataattribute.toUpperCase();
        if (jxAttributeProp.containsKey(key)) {
            return jxAttributeProp.get(key);
        } else {
            JxAttributeProp aprop = new JxAttributeProp();
            aprop.setAttribute(getJxAttribute(key));
            jxAttributeProp.put(key, aprop);
            return aprop;
        }
    }

    @Override
    public void setReadonly(String dataattribute, boolean readonly) throws JxException {
        JxAttributeProp prop = getJxAttributeProp(dataattribute);
        if (prop != null) {
            prop.setReadonly(readonly);
        }
    }

    @Override
    public boolean isReadonly(String dataattribute) throws JxException {
        if (isReadonly()) {
            return true;
        }
        JxAttributeProp prop = getJxAttributeProp(dataattribute);
        if (prop != null) {
            return prop.isReadonly();
        }
        return false;
    }

    @Override
    public void setRequired(String dataattribute, boolean required) throws JxException {
        JxAttributeProp prop = getJxAttributeProp(dataattribute);
        if (prop != null) {
            prop.setRequired(required);
        }
    }

    @Override
    public boolean isRequired(String dataattribute) throws JxException {
        JxAttributeProp prop = getJxAttributeProp(dataattribute);
        if (prop != null) {
            return prop.isRequired();
        }
        return false;
    }

    @Override
    public String getSequenceName(String columnName) throws JxException {
        MaxSequenceSetIFace seqset = MaxFactory.getMaxSequenceSetIFace();
        return seqset.getSequeceName(getEntityname(), columnName);
    }

    public long getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(long queryFlag) {
        this.queryFlag = queryFlag;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

}
