package com.jxtech.jbo;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;

import org.apache.commons.lang3.StringUtils;
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
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }
        currentJbo = getJboInstance();
        String uidName = getUidName();
        String where = uidName + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        DataQueryInfo qbe = new DataQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() >= 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param ids id序列
     */
    @Override
    public List<JboIFace> queryJbo(String[] ids) throws JxException {
        if (StrUtil.isNull(getJboname()) || ids == null) {
            return null;
        }
        currentJbo = getJboInstance();
        String uidName = getUidName();
        String where = uidName + " in (";
        for (int i = 0; i < ids.length; i++) {
            where = where + " ?,";
        }
        where = StringUtils.removeEnd(where, ",");
        where = where + " )";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(ids);
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        // List<JboIFace> data = new ArrayList<JboIFace>();
        if (list != null && list.size() > 0) {
            getJbolist().clear();
            for (Map<String, Object> map : list) {
                currentJbo = getJboInstance();
                currentJbo.setData(map);
                currentJbo.afterLoad();
                getJbolist().add(currentJbo);
            }
        }
        return getJbolist();
    }

    /**
     * 重新进行查询结果
     * 
     * @param jboKey 列
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }

        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
        }

        currentJbo = getJboInstance();
        String where = jboKey + " = ?";
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        DataQueryInfo qbe = this.getQueryInfo();
        qbe.setWhereCause(where);
        qbe.setWhereParams(new Object[] { uid });
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() == 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 重新进行查询结果
     * 
     * @param where 固定条件
     * @param jboKey 列
     * @param uid 唯一值
     */
    @Override
    public JboIFace queryJbo(String where, String jboKey, String uid) throws JxException {
        if (StrUtil.isNull(getJboname()) || StrUtil.isNull(uid)) {
            return null;
        }

        if (StrUtil.isNull(jboKey)) {
            return queryJbo(uid);
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
        List<Map<String, Object>> list = dq.queryAllPage(getJboname(), qbe);
        if (list != null && list.size() >= 1) {
            currentJbo.setData(list.get(0));
            currentJbo.afterLoad();
            return currentJbo;
        }
        return currentJbo;
    }

    /**
     * 查询结果集
     * 
     * @param shipname 联系名
     * @return
     * @throws JxException
     */
    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        int pageNum = getQueryInfo().getPageNum();
        int pageSize = getQueryInfo().getPageSize();

        if (pageNum == 0 && pageSize == 0) {
            return queryAll();
        } else {
            if (pageNum == 0) {
                getQueryInfo().setPageNum(1);
            }

            if (pageSize == 0) {
                getQueryInfo().setPageSize(20);
            }
        }

        List<Map<String, Object>> list = queryList();
        if (list == null) {
            LOG.info("没有正确查询到结果。jboname=" + getJboname());
            return null;
        }
        getJbolist().clear();
        // 转换类型
        int size = list.size();
        setCount(size);
        for (int i = 0; i < size; i++) {
            currentJbo = getJboInstance();
            currentJbo.setData(list.get(i));
            currentJbo.afterLoad();
            getJbolist().add(currentJbo);

        }
        return getJbolist();
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
            if (jbo.isModify() || jbo.isToBeAdd() || jbo.isToBeDel() || jbo.getChangedChildren().size() > 0) {
                flag = flag & jbo.save(conn);
            }
            if (!flag) {
                break;
            }
        }
        return flag;
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
            String key = StrUtil.contact(DBFactory.CACHE_PREX, getJboname(), ".");
            CacheUtil.removeJboOfStartWith(key);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            bflag = false;
            throw new JxException(e.getMessage());
        } finally {
            // 当全部保存完成后，在将currentJbo放到jbolist中
            getJbolist().add(currentJbo);
            setCurrentJbo(currentJbo);

            if (bflag) {
                LOG.debug("数据保存成功。");
                JxDataSourceUtil.commit(conn);
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
        if (!StrUtil.isNull(appName)) {
            // 1.优先查看MaxappsWFInfo表中的配置
            JboIFace mwf = JboUtil.findJbo("MAXAPPSWFINFO", "app=? and active=1", new Object[] { appName.toUpperCase() });
            if (mwf != null) {
                workflowId = mwf.getString("PROCESS");
                workflowEngine = mwf.getString("engine");
            } else {
                // 2. 直接在WFPRocess表中查询
                mwf = JboUtil.findJbo("WFPROCESS", "Appname=? and active=1", new Object[] { appName.toUpperCase() });
                if (mwf != null) {
                    workflowId = mwf.getString("processNUM");
                    workflowEngine = JboSetIFace.BPM_JX;
                } else {
                    LOG.warn("没有找到对应的工作流。");
                }
            }
        } else {
            LOG.warn("没有找到对应的应用程序名。");
        }
    }

    public void setWorkflowEngine(String workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    /**
     * 获得安全限制条件，加载maxapp表中配置的条件和角色数据配置中的条件
     * 
     * @param elValue true时，计算EL表达式的值，否则不用计算
     * @return
     * @throws JxException
     */
    public String getSecurityrestrict(boolean elValue) throws JxException {
        String aname = getAppname();
        if (!StrUtil.isNull(aname)) {
            StringBuilder cause = new StringBuilder();
            aname = aname.toUpperCase().trim();
            String ckey = StrUtil.contact(JxSession.getUserId(), ".Security.", aname, ".", getJboname(), ".", String.valueOf(elValue));
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
            if (app.size() > 0) {
                cause.append(app.get(0).getString("RESTRICTIONS"));
            }
            // 获得当前登录用户的角色数量，以便确定是否每个角色都配置了限制条件，只要有一个没有配置，则无需条件
            int rolescount = 0;
            Set<String> roles = JxSession.getJxUserInfo().getRoles();
            if (roles != null) {
                rolescount = roles.size();
            }
            // 获得角色限制条件
            JboSetIFace secus = JboUtil.getJboSet("securityrestrict");
            DataQueryInfo sdqi = secus.getQueryInfo();
            sdqi.setIgnoreSecurityrestrict(true);
            StringBuilder sb = new StringBuilder();
            sb.append("groupname in (select role_id from pub_role_user where user_id=?)");
            sb.append(" and app=? and objectname=? ");
            sdqi.setWhereCause(sb.toString());
            sdqi.setWhereParams(new Object[] { JxSession.getUserId(), aname, getJboname() });
            List<JboIFace> secs = secus.queryAll();
            if (secs != null) {
                int size = secs.size();
                sb.setLength(0);
                if (size < rolescount) {
                    LOG.debug("角色中有没配置限制条件的内容，无需限制。");
                } else {
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
                rst = ELUtil.getElValue(this, null, JxSession.getJxUserInfo(), cause.toString());
            }
            rst = cause.toString();
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
     * 发送工作流
     * 
     * @return
     * @throws JxException
     */
    @Override
    public boolean route() throws JxException {
        return false;
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
    public void addMpp(List<Task> tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        int jumpIndex = 0;
        for (Task task : tasks) {
            String wbs = task.getWBS();
            if (null != wbs && !"0".equals(wbs)) {
                JboIFace jbo = add();
                jbo.addMpp(task, paramMap, initMap);
                int temp = task.getID() - jumpIndex;
                task.setID(temp);
            } else {
                jumpIndex++;
            }
        }
        commit();
    }

    @Override
    public void expMpp(ProjectFile project, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {

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
     * @param parentName 父节点的字段名
     * @param parentValue 父节点的值
     * @param idName 标识字段名称
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
