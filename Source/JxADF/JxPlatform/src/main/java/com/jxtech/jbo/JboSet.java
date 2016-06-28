package com.jxtech.jbo;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataEdit;
import com.jxtech.db.DataQuery;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.workflow.base.WorkflowEngineFactory;

/**
 * 每个表的记录信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JboSet extends BaseJboSet implements JboSetIFace {
    private static final Logger LOG = LoggerFactory.getLogger(JboSet.class);
    private static final long serialVersionUID = -847638493380867280L;
    // 工作流ID
    private String workflowId;
    // 工作流引擎,JXBPM、OBPM、ACTIVITI
    private String workflowEngine;

    /**
     * 命名规则：appname,appnameSet 子类必须覆盖此方法
     * 
     * @return
     * @throws JxException
     */
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Jbo(this);
        return currentJbo; // 创建自己的JBO
    }

    protected boolean isExecAfterLoad() {
        return (getQueryFlag() & JboSetIFace.NOEXEC_AFTERLOAD) != JboSetIFace.NOEXEC_AFTERLOAD;
    }

    /**
     * 查询结果集
     * 
     * @return
     */
    private List<Map<String, Object>> queryList() throws JxException {
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        String tn = getEntityname();
        if (StrUtil.isNull(tn) || getJboname().startsWith("SQL_")) {
            tn = this.getSql();
        }
        return dq.query(tn, getQueryInfo());
    }

    /**
     * 重新进行查询结果
     * 
     * @param uid
     *            唯一值
     */
    @Override
    public JboIFace queryJbo(String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }
        List<JboIFace> clist = getJbolist();
        String uidName = getUidName();
        if (clist != null && !clist.isEmpty()) {
            Iterator<JboIFace> iter = clist.iterator();
            while (iter.hasNext()) {
                currentJbo = iter.next();
                if (uid.equals(currentJbo.getString(uidName))) {
                    return currentJbo;
                }
            }
        }
        currentJbo = getJboInstance();
        String where = uidName + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        DataQueryInfo qbe = getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        qbe.setCacheme(false);
        qbe.setPageNum(1);
        qbe.setPageSize(1);
        List<Map<String, Object>> list = dq.query(getEntityname(), qbe);
        if (list != null && !list.isEmpty()) {
            currentJbo.setData(list.get(0));
            if (isExecAfterLoad()) {
                currentJbo.afterLoad();
            }
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param ids
     *            id序列
     */
    @Override
    public List<JboIFace> queryJbo(String[] ids) throws JxException {
        if (StrUtil.isNull(getJboname()) || ids == null) {
            return null;
        }
        String uidName = getUidName();
        StringBuilder wc = new StringBuilder();
        wc.append(uidName).append(" in (");
        for (int i = 0; i < ids.length; i++) {
            wc.append(" ?,");
        }
        StrUtil.deleteLastChar(wc).append(" )");
        currentJbo = getJboInstance();
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(wc.toString());
        qbe.setWhereParams(ids);
        qbe.setPageNum(-1);
        qbe.setPageSize(-1);
        // 读取缓存
        List<JboIFace> jbolist = CacheUtil.getJboSetList(CacheUtil.genJboSetKey(this, false));
        if (jbolist != null) {
            setJbolist(jbolist);
            if (jbolist.isEmpty()) {
                currentJbo = null;
            } else {
                currentJbo = jbolist.get(0);
            }
            return jbolist;
        }
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && !list.isEmpty()) {
            jbolist = getJbolist();
            jbolist.clear();
            for (Map<String, Object> map : list) {
                currentJbo = getJboInstance();
                currentJbo.setData(map);
                if (isExecAfterLoad()) {
                    currentJbo.afterLoad();
                }
                jbolist.add(currentJbo);
            }
            CacheUtil.putJboSet(this);
        }
        return getJbolist();
    }

    /**
     * 重新进行查询结果
     * 
     * @param jboKey
     *            列
     * @param uid
     *            唯一值
     */
    @Override
    public JboIFace queryJbo(String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }
        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
        }
        String key = StrUtil.contact(getJboname(), ".", jboKey, ".", uid);
        currentJbo = CacheUtil.getJbo(key);
        if (currentJbo != null) {
            return currentJbo;
        }
        currentJbo = getJboInstance();
        String where = jboKey + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        qbe.setPageSize(1);
        qbe.setPageNum(1);
        List<Map<String, Object>> list = dq.query(getJboname(), qbe);
        if (list != null && !list.isEmpty()) {
            currentJbo.setData(list.get(0));
            if (isExecAfterLoad()) {
                currentJbo.afterLoad();
            }
            CacheUtil.putJboCache(key, currentJbo);// 保存缓存
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param where
     *            固定条件
     * @param jboKey
     *            列
     * @param uid
     *            唯一值
     */
    @Override
    public JboIFace queryJbo(String where, String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }
        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
        }
        String key = CacheUtil.genJboKey(getJboname(), StrUtil.contact(where, jboKey, uid));
        currentJbo = CacheUtil.getJbo(key);
        if (currentJbo != null) {
            return currentJbo;
        }
        currentJbo = getJboInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(jboKey);
        sb.append(" = ?");
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        if (!StrUtil.isNull(where)) {
            sb.append(" and ");
            sb.append(where);
        }
        qbe.setWhereCause(sb.toString());
        qbe.setWhereParams(new Object[] { uid });
        qbe.setPageNum(1);
        qbe.setPageSize(1);
        List<Map<String, Object>> list = dq.query(getJboname(), qbe);
        if (list != null && !list.isEmpty()) {
            currentJbo.setData(list.get(0));
            if (isExecAfterLoad()) {
                currentJbo.afterLoad();
            }
        }
        CacheUtil.putJboCache(key, currentJbo);// 保存缓存
        return currentJbo;
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
        List<JboIFace> jbolist = super.query(shipname);// 读取缓存
        if (jbolist != null) {
            setJbolist(jbolist);
            if (!jbolist.isEmpty()) {
                this.currentJbo = jbolist.get(0);
            }
            return jbolist;
        }
        DataQueryInfo dqi = getQueryInfo();
        int pageNum = dqi.getPageNum();
        int pageSize = dqi.getPageSize();

        if (pageNum == 0 && pageSize == 0) {
            // 查询所有记录，不用分页
        } else {
            if (pageNum == 0) {
                dqi.setPageNum(1);
            }
            if (pageSize == 0) {
                dqi.setPageSize(20);
            }
        }

        List<Map<String, Object>> list = queryList();
        if (list == null) {
            LOG.info("没有正确查询到结果。jboname=" + getJboname());
            return null;
        }
        jbolist = getJbolist();
        jbolist.clear();
        // 转换类型
        int size = list.size();
        for (int i = 0; i < size; i++) {
            currentJbo = getJboInstance();
            currentJbo.setData(list.get(i));
            if (isExecAfterLoad()) {
                currentJbo.afterLoad();
            }
            jbolist.add(currentJbo);

        }
        CacheUtil.putJboSet(this);// 放入缓存中
        return jbolist;
    }

    @Override
    public JboIFace getJboOfIndex(int index, boolean reload) throws JxException {
        if (getJbolist() == null || reload) {
            queryAll();
        }
        return super.getJboOfIndex(index);
    }

    @Override
    public boolean save(Connection conn) throws JxException {
        if (!canSave()) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.cansave.false"));
        }
        if (getJbolist() == null) {
            LOG.info("没有要保存的数据，jbolist is null");
            return true;
        }
        List<JboIFace> list = getJbolist();
        int ls = list.size();
        boolean flag = true;
        for (int i = 0; i < ls; i++) {
            JboIFace jbo = list.get(i);
            if (jbo == null) {
                continue;
            }
            boolean ns = false;
            List<JboIFace> nsl = jbo.getNeedSaveList();
            if (nsl != null && !nsl.isEmpty()) {
                ns = true;
            }
            if (ns || jbo.isModify() || jbo.isToBeAdd() || jbo.isToBeDel() || !jbo.getChangedChildren().isEmpty()) {
                flag = flag & jbo.save(conn);
            }
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * 批量发送工作流
     * 
     * @param ids
     *            唯一ID列表
     * @param action
     *            发送之后的选择操作，可以是文字描述，也可以直接是 true/false
     * @return
     * @throws JxException
     */
    public int route(String[] ids, String action, String memo) throws JxException {
        if (ids == null) {
            return 0;
        }
        if (StrUtil.isNull(action)) {
            return 0;
        }
        int result = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DEFAULTACTION", action);
        params.put("NOTE", memo);
        for (int i = 0; i < ids.length; i++) {
            JboIFace ji = queryJbo(ids[i]);
            if (ji == null) {
                continue;
            }
            try {
                if (ji.route(params)) {
                    result++;
                    ji.getJboSet().commit();
                } else {
                    ji.getJboSet().rollback();
                }
            } catch (Exception e) {
                ji.getJboSet().rollback();
                LOG.error(e.getMessage());
            }
        }

        return result;
    }

    /**
     * 删除多个数据，直接执行删除。
     * 
     * @param ids
     * @return
     */
    @Override
    public boolean delete(String[] ids) throws JxException {
        if (ids == null) {
            return false;
        }
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            boolean flag = true;
            for (int i = 0; i < ids.length; i++) {
                JboIFace j = queryJbo(ids[i]);
                if (j == null) {
                    continue;
                }
                flag = delete(conn, j);
                if (!flag) {
                    break;
                }
            }
            if (flag) {
                LOG.debug("数据删除成功。");
                JxDataSourceUtil.commit(conn);
            } else {
                LOG.debug("数据删除失败。");
                JxDataSourceUtil.rollback(conn);
            }
            return flag;
        } finally {
            JxDataSourceUtil.close(conn);
        }
    }

    public boolean delete(Connection conn, JboIFace jbi, boolean isSave) throws JxException {
        if ((conn == null && isSave) || jbi == null) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.cannot.get.conn.or.jbo.null"));
        }
        if (!jbi.delete()) {
            return false;
        }
        String[] children = jbi.getDeleteChildren();
        // 1.删除子记录
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (StrUtil.isNullOfIgnoreCaseBlank(children[i])) {
                    continue;
                }
                JboSetIFace js = jbi.getRelationJboSet(children[i], JxConstant.READ_RELOAD);
                if (js != null) {
                    List<JboIFace> list = js.getJbolist();
                    int count = list.size();
                    for (int j = 0; j < count; j++) {
                        JboIFace ji = list.get(j);
                        boolean b = ji.delete();
                        if (!b) {
                            return false;
                        }
                        /*
                         * if (isSave) { if (!ji.save(conn)) { return false;// 删除失败 } }
                         */
                    }
                }
            }
        }
        if (isSave) {
            // 2.删除主记录
            return jbi.save(conn);
        } else {
            return true;
        }
    }

    @Override
    public boolean commit() throws JxException {
        return commit(getSaveFlag());
    }

    @Override
    public boolean commit(long flag) throws JxException {
        setSaveFlag(flag);// 设定保存标识
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        boolean bflag = true;
        try {
            if (currentJbo != null) {
                bflag = currentJbo.save(conn);
            }

            if (bflag) {
                // currentjbo在上面已经保存过来，就要将其移除掉不需要再次保存了。
                List<JboIFace> lists = getJbolist();
                if (lists != null) {
                    lists.remove(currentJbo);
                }
                bflag = save(conn);
            }
            // 移出缓存
            String key = StrUtil.contact(getJboname(), ".");
            CacheUtil.removeJboOfStartWith(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            bflag = false;
            throw new JxException(e.getMessage());
        } finally {
            // 当全部保存完成后，在将currentJbo放到jbolist中
            getJbolist().add(currentJbo);
            setCurrentJbo(currentJbo);

            if (bflag) {
                LOG.debug("数据保存成功。");
                JxDataSourceUtil.commit(conn);
                if (canCache()) {
                    // 清除缓存
                    CacheUtil.removeJboOfStartWith(StrUtil.contact(getJboname(), "."));
                }
                setFlag();
            } else {
                LOG.info("数据保存失败。");
                JxDataSourceUtil.rollback(conn);
            }
            JxDataSourceUtil.close(conn);
        }
        return bflag;
    }

    @Override
    public boolean rollback() throws JxException {
        boolean flag = true;
        List<JboIFace> removeJboList = new ArrayList<JboIFace>();
        if (getJbolist().isEmpty() && null != currentJbo) {
            getJbolist().add(currentJbo);
        }
        try {
            for (JboIFace jbo : getJbolist()) {
                if (null != jbo) {
                    JboIFace removeJbo = jbo.rollback();
                    if (null != removeJbo) {
                        removeJboList.add(removeJbo);
                    }
                }
            }
            getJbolist().removeAll(removeJboList);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            flag = false;
            throw new JxException(e.getMessage());
        }

        return flag;
    }

    /**
     * 获得工作流标识，优先在MaxappsWFInfo表中查询，如果没有，则直接在WFProcess表中查询。
     */
    @Override
    public String getWorkflowId() throws JxException {
        if (workflowId != null) {
            return workflowId;
        }
        initWorkflowInfo();
        return workflowId;
    }

    public String getWorkflowEngine() throws JxException {
        if (workflowEngine != null) {
            return workflowEngine;
        }
        initWorkflowInfo();
        return workflowEngine;
    }

    /**
     * 初始化工作流相关参数
     * 
     * @throws JxException
     */
    protected void initWorkflowInfo() throws JxException {
        String appName = getAppname();
        if (StrUtil.isNull(appName)) {
            String jn = getJboname();
            if (!StrUtil.isNull(jn)) {
                JboIFace jb = JboUtil.findJbo("maxapps", "maintbname=?", new Object[] { jn.toUpperCase() });
                if (jb != null) {
                    appName = jb.getString("APP");
                    this.setAppname(appName);
                }
            }
        }
        String[] wf = WorkflowEngineFactory.getWorkflow(appName);
        workflowId = wf[0];
        workflowEngine = wf[1];
    }

    public void setWorkflowEngine(String workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    /**
     * 获得安全限制条件，加载maxapp表中配置的条件和角色数据配置中的条件
     * 
     * @param elValue
     *            true时，计算EL表达式的值，否则不用计算
     * @return
     * @throws JxException
     */
    public String getSecurityrestrict(boolean elValue) throws JxException {
        String aname = getAppname();
        if (!StrUtil.isNull(aname)) {
            StringBuilder cause = new StringBuilder();
            aname = aname.toUpperCase().trim();
            String userid = JxSession.getUserId(getSession());
            String ckey = StrUtil.contact(userid, ".securityrestrict.", aname, ".", getJboname(), ".", String.valueOf(elValue));
            Object obj = CacheUtil.getPermission(ckey);
            if (obj instanceof String) {
                return (String) obj;
            }
            // 获得应用程序的限制条件
            JboSetIFace apps = JboUtil.getJboSet("Maxapps");
            DataQueryInfo dqi = apps.getQueryInfo();
            dqi.setIgnoreSecurityrestrict(true);
            dqi.setWhereCause("app=?");
            dqi.setWhereParams(new Object[] { aname });
            List<JboIFace> app = apps.query();
            if (!app.isEmpty()) {
                cause.append(app.get(0).getString("RESTRICTIONS"));
            }
            // 获得是否需要限制条件
            DataQuery dq = DBFactory.getDataQuery(null, null);
            int c = dq.count("pub_role", "role_type=1 and role_id in (select role_id from pub_role_user where user_id=?)", new Object[] { userid });
            if (c <= 0) {
                // 获得角色限制条件
                JboSetIFace secus = JboUtil.getJboSet("securityrestrict");
                DataQueryInfo sdqi = secus.getQueryInfo();
                sdqi.setIgnoreSecurityrestrict(true);
                StringBuilder sb = new StringBuilder();
                sb.append("groupname in (select role_id from pub_role_user where user_id=?)");
                sb.append(" and app=? and objectname=? ");
                sdqi.setWhereCause(sb.toString());
                sdqi.setWhereParams(new Object[] { userid, aname, getJboname() });
                List<JboIFace> secs = secus.queryAll();
                if (secs != null) {
                    int size = secs.size();
                    sb.setLength(0);
                    boolean start = false;
                    for (int i = 0; i < size; i++) {
                        JboIFace sec = secs.get(i);
                        String rec = sec.getString("restriction");
                        if (rec != null && rec.length() > 1) {
                            if (start) {
                                sb.append(" or ");
                            }
                            sb.append('(').append(rec).append(')');
                            start = true;
                        }
                    }
                    if (sb.length() > 2) {
                        if (cause.length() > 3) {
                            cause.insert(0, '(').append(" and (").append(sb).append(')');
                        } else {
                            cause.append(sb);
                        }
                    }
                }
            }
            String rst;
            if (cause.length() > 2 && elValue) {
                rst = ELUtil.getElValue(this, null, JxSession.getJxUserInfo(getSession()), cause.toString());
            } else {
                rst = cause.toString();
            }
            CacheUtil.putPermissionCache(ckey, rst);
            return rst;
        }
        return null;
    }

    @Override
    public void setWorkflowId(String wfId) {
        workflowId = wfId;
    }

    @Override
    public int count() throws JxException {
        List<JboIFace> list = getJbolist();
        if (list != null) {
            DataQueryInfo dqi = getQueryInfo();
            int pagesize = dqi.getPageSize();
            int pagenum = dqi.getPageNum();
            int size = list.size();
            // 计算记录总数
            if (pagesize > size && size > 0) {
                int count = 0;
                if (pagenum > 1) {
                    count = (pagenum - 1) * pagesize + size;
                } else {
                    count = size;
                }
                setCount(count);
                return count;
            } else if (size == 0 && pagenum < 2) {
                // 就是0条记录了
                setCount(0);
                return 0;
            }
        }
        // 满页的数据，需要重新计算
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        int count = dq.count(this);
        setCount(count);
        return count;
    }

    @Override
    public void getBlob(String blobColumnName, String uid, OutputStream os) throws JxException {
        if (StrUtil.isNull(uid) || os == null) {
            throw new JxException(JxLangResourcesUtil.getString("jboset.getblob.get.id.or.os.null"));
        }
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        dq.getBlob(getJboname(), blobColumnName, getUidName(), uid, os);
    }

    /**
     * 将当前查询的结果集全部发送工作流
     * 
     * @return
     * @throws JxException
     */
    @Override
    public int route() throws JxException {
        return route("true");
    }

    /**
     * 批量发送工作流，将当前结果集直接发送
     * 
     * @param action
     * @return
     * @throws JxException
     */
    public int route(String action) throws JxException {
        List<JboIFace> list = this.getJbolist();
        if (list == null || list.isEmpty()) {
            return 0;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DEFAULTACTION", action);
        int result = 0;
        Iterator<JboIFace> iter = list.iterator();
        while (iter.hasNext()) {
            JboIFace ji = iter.next();
            try {
                if (ji.route(params)) {
                    result++;
                    ji.getJboSet().commit();
                } else {
                    ji.getJboSet().rollback();
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
                ji.getJboSet().rollback();
            }
        }
        return result;
    }

    /**
     * 记录集加载完后执行的方法
     */
    @Override
    public void afterLoad() throws JxException {
        JboIFace parent = getParent();
        if (null != parent) {
            String parentStatus = parent.getString("wft_status");
            if (!StrUtil.isNull(parentStatus) && "CLOSE".equalsIgnoreCase(parentStatus)) {
                setReadonly(true);
            }
        }
    }

    @Override
    public String loadImportFile(List<Map<Object, String>> importFileResult, JxUserInfo userInfo) throws JxException {
        return "导入成功!";
    }

    @Override
    public void lookup(List<JboIFace> lookupList) throws JxException {
        if (!lookupList.isEmpty()) {
            for (JboIFace lookupJbo : lookupList) {
                JboIFace tempJbo = add();
                tempJbo.getData().putAll(lookupJbo.getData());
            }
        }
    }

    /**
     * 获得树节点及其所有的子节点
     * 
     * @param parentName
     *            父节点的字段名
     * @param parentValue
     *            父节点的值
     * @param idName
     *            标识字段名称
     * @return
     * @throws JxException
     */
    public List<JboIFace> getTree(String parentName, String parentValue, String idName, boolean includeSelf) throws JxException {
        List<JboIFace> lists = new ArrayList<JboIFace>();
        JboSetIFace js = JboUtil.getJboSet(getJboname());
        DataQueryInfo dqi = js.getQueryInfo();
        if (includeSelf) {
            // 添加自己这条记录
            if (StrUtil.isNull(parentValue)) {
                dqi.setWhereCause(StrUtil.contact(idName, " is NUll"));
            } else {
                dqi.setWhereCause(StrUtil.contact(idName, "=?"));
                dqi.setWhereParams(new Object[] { parentValue });
            }
            List<JboIFace> self = js.queryAll();
            lists.addAll(self);
        }
        if (StrUtil.isNull(parentValue)) {
            dqi.setWhereCause(StrUtil.contact(parentName, " is Null "));
        } else {
            dqi.setWhereCause(StrUtil.contact(parentName, "=?"));
            dqi.setWhereParams(new Object[] { parentValue });
        }
        List<JboIFace> children = js.queryAll();
        if (children != null) {
            int size = children.size();
            for (int i = 0; i < size; i++) {
                JboIFace ji = children.get(i);
                lists.add(ji);
                lists.addAll(getTree(parentName, ji.getString(idName), idName, false));
            }
        }
        return lists;
    }

    /**
     * 直接根据条件删除数据
     * 
     * @param conn
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public boolean delete(Connection conn, String whereCause, Object[] params) throws JxException {
        DataEdit de = DBFactory.getDataEdit(this.getDbtype(), this.getDataSourceName());
        try {
            String msql = StrUtil.contact("delete from ", this.getEntityname(), " where ", whereCause);
            return de.execute(conn, msql, params);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 直接删除数据，并提交
     * 
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public boolean delete(String whereCause, Object[] params) throws JxException {
        Connection conn = JxDataSourceUtil.getConnection(this.getDataSourceName());
        try {
            delete(conn, whereCause, params);
            conn.commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            JxDataSourceUtil.close(conn);
        }
    }

}
