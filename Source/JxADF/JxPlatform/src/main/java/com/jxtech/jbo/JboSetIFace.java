package com.jxtech.jbo;

import com.jxtech.app.jxwxp.JxWXPValueAdapter;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Task;
import org.directwebremoting.io.FileTransfer;

import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 每个表的记录信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public interface JboSetIFace extends Serializable {
    
    //健新工作流名称
    public static final String BPM_JX = "JXBPM";

    //Oracle 工作流名称
    public static final String BPM_ORACLE = "OBPM";
    
    //Activiti工作流名称 
    public static final String BPM_ACTIVITI = "ACTIVITI";
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
     * @param shipname 联系名
     * @return
     * @throws JxException
     */
    public List<JboIFace> query(String shipname) throws JxException;

    public List<JboIFace> queryAll() throws JxException;

    public boolean save(Connection conn) throws JxException;

    public boolean rollback() throws JxException;

    public boolean commit() throws JxException;

    /**
     * 提交数据
     * 
     * @param flag，标识是否检查校验一些东东，详细参见常数JboSetIFace.SAVE_NO_CHECK_xxx
     * @return
     * @throws JxException
     */
    public boolean commit(long flag) throws JxException;

    public boolean beforeAdd() throws JxException;

    public boolean canAdd() throws JxException;

    public JboIFace add() throws JxException;

    public boolean delete(String[] ids) throws JxException;

    /**
     * 删除Jbo中所有的记录
     * 
     * @return
     * @throws JxException
     */
    public boolean delete() throws JxException;

    public boolean delete(Connection conn, JboIFace jbi, boolean isSave) throws JxException;

    /**
     * 仅仅标记为删除，不执行真正地删除操作。
     * 
     * @param uid
     * @return
     * @throws JxException
     */
    public boolean delRow(String uid) throws JxException;

    public boolean unDelRow(String uid) throws JxException;

    public List<JboIFace> getJbolist();

    public void setJbolist(List<JboIFace> jbolist);

    public JboIFace queryJbo(String uid) throws JxException;

    public List<JboIFace> queryJbo(String[] ids) throws JxException;

    public JboIFace getJboOfUid(String uid) throws JxException;

    public JboIFace getJboOfIndex(int index, boolean reload) throws JxException;

    public String getJboname();

    public void setJboname(String jboname);

    public int getCount();

    public int getCount(boolean flag);

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

    public JboIFace getJbo() throws JxException;

    public JboIFace getJbo(boolean isnew) throws JxException;

    public void setJbo(JboIFace jbo);

    public Map<String, JxAttribute> getJxAttributes() throws JxException;

    public JxTable getJxTable() throws JxException;

    public JxObject getJxobject() throws JxException;

    public String getUidName() throws JxException;

    /**
     * 获得真实表名
     * 
     * @return
     * @throws JxException
     */
    public String getEntityname() throws JxException;

    public DataQueryInfo getQueryInfo();

    /**
     * 获得查询条件
     * 
     * @param flag ClearParams 清除之前记忆的查询条件。
     * @return
     */
    public DataQueryInfo getQueryInfo(String flag);

    public void setQueryInfo(DataQueryInfo queryInfo);

    public JxAttribute getJxAttribute(String attributeName) throws JxException;

    public String toJson() throws JxException;

    public String toJson(String[] attributes) throws JxException;

    public String toJson(String[] attributes, boolean head) throws JxException;

    /**
     * 返回zTree的JSON格式的字符串
     * 
     * @param attributes 需要返回的字段名
     * @param idName 唯一关键字（与父节点关联）的字段名
     * @param parentIdName 父节点字段名
     * @param name 显示名称
     * @param hasChildName 是否有孩子节点字段名
     * @param leafDisplay 是否显示叶子节点
     * @return
     */
    public String toZTreeJson(String[] attributes, String idName, String parentIdName, String name, String hasChildName, boolean leafDisplay, String root, String i18n) throws JxException;

    public String getAppname();

    public void setAppname(String appname);

    public void getBlob(String blobColumnName, String uid, OutputStream os) throws JxException;

    // 以下是关于工作流应用的处理
    public String getWorkflowId() throws JxException;

    public void setWorkflowId(String wfId);
    
    public String getWorkflowEngine() throws JxException;
    
    public void setWorkflowEngine(String workflowEngine) ;

    /**
     * 发送工作流
     * 
     * @return
     * @throws JxException
     */
    public boolean route() throws JxException;

    public JboIFace getParent() throws JxException;

    public void setParent(JboIFace parent) throws JxException;

    public void setReadonly(boolean readonly);

    public boolean isReadonly();

    public void afterLoad() throws JxException;

    public void addMpp(List<Task> tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException;

    public void expMpp(ProjectFile project, Map<String, String> paramMap, Map<String, String> initMap) throws JxException;

    public void setRequired(boolean required);

    public boolean isRequired();

    /**
     * 加载导入的文件，处理导入文件
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

    JboIFace queryJbo(String jboKey, String uid) throws JxException;

    JboIFace queryJbo(String where, String jboKey, String uid) throws JxException;

    public void lookup(List<JboIFace> lookupList) throws JxException;

    public void setFlag() throws JxException;

    public void tableQuickSearch(String searchValue) throws JxException;

    public String getRelationshipname();

    public void setRelationshipname(String relationshipname);

    public String getSql();

    public void setSql(String sql);

    public String getDbtype();

    public void setDbtype(String dbtype);

    public String getDataSourceName();

    public void setDataSourceName(String dataSourceName);

    /* 导出xls 或者pef接口 简单的实现，还在完善和修改中 */
    /**
     * 把jboset的数据导出成为Excel文件
     * 
     * @param title 文件的名称，一般在前台取网页的title标签
     * @param uids 页面选择的数据
     * @return dwr专用下载类
     * @throws JxException
     */
    public FileTransfer expExcel(String title, String uids) throws JxException;

    /**
     * 把jboset的数据导出成为Excel文件
     * 
     * @param title 文件的名称，一般在前台取网页的title标签
     * @param uids 页面选择的数据
     * @param dataMap 需要导出的数据 不为空时，优先dataMap
     * @return dwr专用下载类
     * @throws JxException
     */
    public FileTransfer expExcel(String title, String uids, List<Map<String, Object>> dataMap) throws JxException;

    /**
     * 修改导出的Excel的表头信息
     * 
     * @param header 默认导出的表头
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
     * @param elValue true时，计算EL表达式的值，否则不用计算
     * @return
     * @throws JxException
     */
    public String getSecurityrestrict(boolean elValue) throws JxException;

    public long getSaveFlag();

    public void setSaveFlag(long saveFlag);

    /**
     * 获得树节点及其所有的子节点
     * 
     * @param parentName 父节点的字段名
     * @param parentValue 父节点的值
     * @param idName 标识字段名称
     * @param includeSelf 是否包含自己
     * @return
     * @throws JxException
     */
    public List<JboIFace> getTree(String parentName, String parentValue, String idName,boolean includeSelf) throws JxException;

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
}
