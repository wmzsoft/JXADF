package com.jxtech.jbo;

import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.io.FileTransfer;

import com.jxtech.app.jxwxp.JxWXPValueAdapter;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 每个表的记录信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public interface JboSetIFace extends Serializable {

    // 健新工作流名称
    public static final String BPM_JX = "JXBPM";

    // Oracle 工作流名称
    public static final String BPM_ORACLE = "OBPM";

    // Activiti工作流名称
    public static final String BPM_ACTIVITI = "ACTIVITI";

    // 不要执行AfterLoad方法
    public static final long NOEXEC_AFTERLOAD = 1;

    /**
     * 如果要添加业务逻辑，请继承此方法。
     * 
     * @return
     * @throws JxException
     */
    public List<JboIFace> query() throws JxException;

    /**
     * 查询结果集，如果要根据不同联系名，处理不同业务逻辑，则请继承此方法 。
     * 
     * @param shipname
     *            联系名
     * @return
     * @throws JxException
     */
    public List<JboIFace> query(String shipname) throws JxException;

    /**
     * 查询所有结果，不分页
     * 
     * @return
     * @throws JxException
     */
    public List<JboIFace> queryAll() throws JxException;

    /**
     * 保存数据
     * 
     * @param conn
     * @return
     * @throws JxException
     */
    public boolean save(Connection conn) throws JxException;

    /**
     * 回滚数据
     * 
     * @return
     * @throws JxException
     */
    public boolean rollback() throws JxException;

    /**
     * 提交数据，持久化
     * 
     * @return
     * @throws JxException
     */
    public boolean commit() throws JxException;

    /**
     * 提交数据
     * 
     * @param flag，标识是否检查校验一些东东，详细参见常数JboSetIFace.SAVE_NO_CHECK_xxx
     * @return
     * @throws JxException
     */
    public boolean commit(long flag) throws JxException;

    /**
     * 添加记录之前执行操作
     * 
     * @return
     * @throws JxException
     */
    public boolean beforeAdd() throws JxException;

    /**
     * 是否可以添加数据
     * 
     * @return
     * @throws JxException
     */
    public boolean canAdd() throws JxException;

    /**
     * 执行添加数据操作
     * 
     * @return
     * @throws JxException
     */
    public JboIFace add() throws JxException;

    /**
     * 删除数据
     * 
     * @param ids
     *            唯一标识
     * @return
     * @throws JxException
     */
    public boolean delete(String[] ids) throws JxException;

    /**
     * 删除Jbo中所有的记录
     * 
     * @return
     * @throws JxException
     */
    public boolean delete() throws JxException;

    /**
     * 删除记录
     * 
     * @param conn
     * @param jbi
     * @param isSave
     * @return
     * @throws JxException
     */
    public boolean delete(Connection conn, JboIFace jbi, boolean isSave) throws JxException;

    /**
     * 仅仅标记为删除，不执行真正地删除操作。
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    public boolean delRow(String uid) throws JxException;

    /**
     * 撤消删除
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    public boolean unDelRow(String uid) throws JxException;

    /**
     * 获得记录集
     * 
     * @return
     */
    public List<JboIFace> getJbolist();

    /**
     * 重新指定记录集
     * 
     * @param jbolist
     */
    public void setJbolist(List<JboIFace> jbolist);

    /**
     * 根据唯一标识，查询记录
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    public JboIFace queryJbo(String uid) throws JxException;

    /**
     * 根据唯一标识，查询记录集
     * 
     * @param ids
     * @return
     * @throws JxException
     */
    public List<JboIFace> queryJbo(String[] ids) throws JxException;

    /**
     * 在当前的记录集中，根据唯一标识查询记录
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    public JboIFace getJboOfUid(String uid) throws JxException;

    /**
     * 获得某条记录
     * 
     * @param index
     * @param reload
     * @return
     * @throws JxException
     */
    public JboIFace getJboOfIndex(int index, boolean reload) throws JxException;

    /**
     * 获得对象的名字，即在 MaxObject 表中的 Objectname 字段
     * 
     * @return
     */
    public String getJboname();

    /**
     * 设定对象名称
     * 
     * @param jboname
     */
    public void setJboname(String jboname);

    /**
     * 获得当前记录集的条数，是缓存中的数据
     * 
     * @return
     */
    public int getCount();

    /**
     * 获得当前记录集的条数
     * 
     * @param flag
     *            是否重新计算
     * @return
     */
    public int getCount(boolean flag);

    /**
     * 执行 Select Count(*) 查询记录条数
     * 
     * @return
     * @throws JxException
     */
    public int count() throws JxException;

    /**
     * 统计合
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public double sum(String attributename) throws JxException;

    /**
     * 返回某个属性的最大值
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public Object max(String attributename) throws JxException;

    /**
     * 返回最小值
     * 
     * @param attributename
     * @return
     * @throws JxException
     */
    public Object min(String attributename) throws JxException;

    /**
     * 获得记录集中，当前正在操作的记录
     * 
     * @return
     * @throws JxException
     */
    public JboIFace getJbo() throws JxException;

    /**
     * 获得当前正在操作的记录
     * 
     * @param isnew
     *            如果没有，是否创建一个新的
     * @return
     * @throws JxException
     */
    public JboIFace getJbo(boolean isnew) throws JxException;

    /**
     * 设定当前正在操作的记录
     * 
     * @param jbo
     */
    public void setJbo(JboIFace jbo);

    /**
     * 获得对象的字段定义信息
     * 
     * @return
     * @throws JxException
     */
    public Map<String, JxAttribute> getJxAttributes() throws JxException;

    /**
     * 获得对象关联表的定义
     * 
     * @return
     * @throws JxException
     */
    public JxTable getJxTable() throws JxException;

    /**
     * 获得对象的定义信息
     * 
     * @return
     * @throws JxException
     */
    public JxObject getJxobject() throws JxException;

    /**
     * 获得唯一标识的字段名
     * 
     * @return
     * @throws JxException
     */
    public String getUidName() throws JxException;

    /**
     * 获得真实表名
     * 
     * @return
     * @throws JxException
     */
    public String getEntityname() throws JxException;

    /**
     * 获得查询条件
     * 
     * @return
     */
    public DataQueryInfo getQueryInfo();

    /**
     * 获得查询条件
     * 
     * @param flag
     *            ClearParams 清除之前记忆的查询条件。
     * @return
     */
    public DataQueryInfo getQueryInfo(String flag);

    /**
     * 设定查询条件，尽量不使用此方法
     * 
     * @param queryInfo
     */
    public void setQueryInfo(DataQueryInfo queryInfo);

    /**
     * 获得某个字段的定义信息
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public JxAttribute getJxAttribute(String attributeName) throws JxException;

    /**
     * 将结果集转换为JSON字符串
     * 
     * @return
     * @throws JxException
     */
    public String toJson() throws JxException;

    /**
     * 将结果转换为JSON字符串。
     * 
     * @param attributes
     * @return
     * @throws JxException
     */
    public String toJson(String[] attributes) throws JxException;

    /**
     * 将结果转换为JSON字符串。
     * 
     * @param attributes
     * @param head
     * @return
     * @throws JxException
     */
    public String toJson(String[] attributes, boolean head) throws JxException;

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
     *            显示名称
     * @param hasChildName
     *            是否有孩子节点字段名
     * @param leafDisplay
     *            是否显示叶子节点
     * @return
     */
    public String toZTreeJson(String[] attributes, String idName, String parentIdName, String name, String hasChildName, boolean leafDisplay, String root, String i18n) throws JxException;

    /**
     * 获得记录集所在的应用程序名
     * 
     * @return
     */
    public String getAppname();

    /**
     * 设定记录集所在的应用程序名
     * 
     * @param appname
     */
    public void setAppname(String appname);

    /**
     * 获得BLOB字段值
     * 
     * @param blobColumnName
     * @param uid
     * @param os
     * @throws JxException
     */
    public void getBlob(String blobColumnName, String uid, OutputStream os) throws JxException;

    /**
     * 获得工作流定义的ID
     * 
     * @return
     * @throws JxException
     */
    public String getWorkflowId() throws JxException;

    /**
     * 设定工作流定义的ID
     * 
     * @param wfId
     */
    public void setWorkflowId(String wfId);

    /**
     * 获得工作流引擎，可能是JXBPM、OracleBPM、Activiti等
     * 
     * @return
     * @throws JxException
     */
    public String getWorkflowEngine() throws JxException;

    /**
     * 配置工作流引擎
     * 
     * @param workflowEngine
     */
    public void setWorkflowEngine(String workflowEngine);

    /**
     * 发送工作流
     * 
     * @return
     * @throws JxException
     */
    public boolean route() throws JxException;

    /**
     * 获得当前记录集的父亲记录
     * 
     * @return
     * @throws JxException
     */
    public JboIFace getParent() throws JxException;

    /**
     * 设定当前记录集的父记录
     * 
     * @param parent
     * @throws JxException
     */
    public void setParent(JboIFace parent) throws JxException;

    /**
     * 设定整个记录集只读
     * 
     * @param readonly
     */
    public void setReadonly(boolean readonly);

    /**
     * 判断整个记录集是否只读
     * 
     * @return
     */
    public boolean isReadonly();

    /**
     * 查询结果之后，加载记录到JboSet之后，执行的操作
     * 
     * @throws JxException
     */
    public void afterLoad() throws JxException;

    /**
     * 设定记录集必填
     * 
     * @param required
     */
    public void setRequired(boolean required);

    /**
     * 设定字段的readonly属性
     * 
     * @param dataattribute
     * @param readonly
     * @throws JxException
     */
    public void setReadonly(String dataattribute, boolean readonly) throws JxException;

    /**
     * 某字段的只读属性
     * 
     * @param dataattribute
     * @return
     * @throws JxException
     */
    public boolean isReadonly(String dataattribute) throws JxException;

    /**
     * 设定字段的必填属性
     * 
     * @param dataattribute
     * @param required
     * @throws JxException
     */
    public void setRequired(String dataattribute, boolean required) throws JxException;

    /**
     * 某字段是否必填的属性
     * 
     * @param dataattribute
     * @return
     * @throws JxException
     */
    public boolean isRequired(String dataattribute) throws JxException;

    /**
     * 某个字段是否必填
     * 
     * @return
     */
    public boolean isRequired();

    /**
     * 加载导入的文件，处理导入文件，已废弃
     * 
     * @return
     */
    public String loadImportFile(List<Map<Object, String>> importFileResult, JxUserInfo userInfo) throws JxException;

    /**
     * 返回Autokey的字段信息
     * 
     * @return
     * @throws JxException
     */
    public Map<String, JxAttribute> getAutokeysAttributes() throws JxException;

    /**
     * 查询记录
     * 
     * @param jboKey
     * @param uid
     * @return
     * @throws JxException
     */
    public JboIFace queryJbo(String jboKey, String uid) throws JxException;

    /**
     * 查询记录
     * 
     * @param where
     * @param jboKey
     * @param uid
     * @return
     * @throws JxException
     */
    public JboIFace queryJbo(String where, String jboKey, String uid) throws JxException;

    public void lookup(List<JboIFace> lookupList) throws JxException;

    /**
     * 设定记录集的初始化状态
     * 
     * @throws JxException
     */
    public void setFlag() throws JxException;

    /**
     * 快速查询
     * 
     * @param searchValue
     * @throws JxException
     */
    public void tableQuickSearch(String searchValue) throws JxException;

    /**
     * 获得记录集的联系名
     * 
     * @return
     */
    public String getRelationshipname();

    /**
     * 设定记录集的联系名
     * 
     * @param relationshipname
     */
    public void setRelationshipname(String relationshipname);

    /**
     * 如果这个记录集是直接通过SQL获得的，则返回查询的SQL
     * 
     * @return
     */
    public String getSql();

    /**
     * 直接设定SQL语句
     * 
     * @param sql
     */
    public void setSql(String sql);

    /**
     * 获得当前数据库的类型
     * 
     * @return
     */
    public String getDbtype();

    /**
     * 设定数据库的类型
     * 
     * @param dbtype
     */
    public void setDbtype(String dbtype);

    /**
     * 获得数据源名称
     * 
     * @return
     */
    public String getDataSourceName();

    /**
     * 设定数据源名称
     * 
     * @param dataSourceName
     */
    public void setDataSourceName(String dataSourceName);

    /**
     * 把jboset的数据导出成为Excel文件
     * 
     * @param title
     *            文件的名称，一般在前台取网页的title标签
     * @param uids
     *            页面选择的数据
     * @return dwr专用下载类
     * @throws JxException
     */
    public FileTransfer expExcel(String title, String uids) throws JxException;

    /**
     * 把jboset的数据导出成为Excel文件
     * 
     * @param title
     *            文件的名称，一般在前台取网页的title标签
     * @param uids
     *            页面选择的数据
     * @param dataMap
     *            需要导出的数据 不为空时，优先dataMap
     * @return dwr专用下载类
     * @throws JxException
     */
    public FileTransfer expExcel(String title, String uids, List<Map<String, Object>> dataMap) throws JxException;

    /**
     * 修改导出的Excel的表头信息
     * 
     * @param header
     *            默认导出的表头
     * @return 修改后的表头 key即所要导出的dataattribute value即为表头的标题
     */
    public Map<String, String> modifyExcelColumn(Map<String, String> header);

    /**
     * 应用于对导出的列的值需要定制特殊含义业务情况
     * 
     * @return 返回map为键值对， key即所要导出的dataattribute value即为 JxWXPValueAdapter的具体实现类
     */
    public Map<String, JxWXPValueAdapter> getExcelAdapter();

    /**
     * 是否可以保存此集合
     * 
     * @return
     * @throws JxException
     */
    public boolean canSave() throws JxException;

    /**
     * 获得安全限制条件，加载maxapp表中配置的条件和角色数据配置中的条件
     * 
     * @param elValue
     *            true时，计算EL表达式的值，否则不用计算
     * @return
     * @throws JxException
     */
    public String getSecurityrestrict(boolean elValue) throws JxException;

    /**
     * 获得保存的标记
     * 
     * @return
     */
    public long getSaveFlag();

    /**
     * 设定保存的标记
     * 
     * @param saveFlag
     */
    public void setSaveFlag(long saveFlag);

    /**
     * 获得树节点及其所有的子节点
     * 
     * @param parentName
     *            父节点的字段名
     * @param parentValue
     *            父节点的值
     * @param idName
     *            标识字段名称
     * @param includeSelf
     *            是否包含自己
     * @return
     * @throws JxException
     */
    public List<JboIFace> getTree(String parentName, String parentValue, String idName, boolean includeSelf) throws JxException;

    /**
     * 直接根据条件删除数据
     * 
     * @param conn
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public boolean delete(Connection conn, String whereCause, Object[] params) throws JxException;

    /**
     * 直接删除数据，并提交
     * 
     * @param whereCause
     * @param params
     * @return
     * @throws JxException
     */
    public boolean delete(String whereCause, Object[] params) throws JxException;

    /**
     * 获得序列的名字
     * 
     * @param columnName
     *            字段名，为空，则表示唯一一个序列
     * @return 返回序列名称
     * @throws JxException
     */
    public String getSequenceName(String columnName) throws JxException;

    /**
     * 获得查询标识
     * 
     * @return
     */
    public long getQueryFlag();

    /**
     * 设定查询标识
     * 
     * @param queryFlag
     */
    public void setQueryFlag(long queryFlag);

    /**
     * 获得Session
     * 
     * @return
     */
    public HttpSession getSession();

    /**
     * 设定Session
     * 
     * @param session
     */
    public void setSession(HttpSession session);

}
