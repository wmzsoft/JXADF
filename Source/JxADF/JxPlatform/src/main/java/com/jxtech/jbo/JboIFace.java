package com.jxtech.jbo;

import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.JxObject;
import com.jxtech.jbo.base.JxTable;
import com.jxtech.jbo.util.JxException;
import com.jxtech.workflow.option.WftParam;
import net.sf.mpxj.Task;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 每条记录的所有信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public interface JboIFace extends Serializable {

    public void add() throws JxException;

    public void addMpp(Task tasks, Map<String, String> paramMap, Map<String, String> initMap) throws JxException;

    /**
     * 创建Jbo之后就执行，如果要在加载数据之后执行请重载afterLoad方法。
     * 
     * @throws JxException
     */
    public void init() throws JxException;

    /**
     * 数据加载成功之后执行的操作
     * 
     * @throws JxException
     */
    public void afterLoad() throws JxException;

    public boolean delete() throws JxException;

    public boolean beforeUnDelete() throws JxException;

    /**
     * 将标记置为未删除。
     * 
     * @return
     * @throws JxException
     */
    public boolean unDelete() throws JxException;

    public String[] getDeleteChildren() throws JxException;

    public void setChildren(Map<String, JboSetIFace> children) throws JxException;

    /**
     * 复制记录
     * 
     * @return 复制之后的对象
     */
    public JboIFace duplicate() throws JxException;

    public boolean save(Connection conn) throws JxException;

    /**
     * 回滚记录
     * 
     * @return 返回需要移除的JBO对象
     * @throws JxException
     */
    public JboIFace rollback() throws JxException;

    public void setData(Map<String, Object> data);

    public Map<String, JxAttribute> getJxAttributes() throws JxException;

    public String getJboName();

    public String getUidName() throws JxException;

    public String getUidValue() throws JxException;

    public String getString(String attributeName) throws JxException;

    public String getString(String attributeName, long flag) throws JxException;

    public String getString(String attributeName, long flag, String formatter) throws JxException;

    public long getLong(String attributeName) throws JxException;

    public long getLong(String attributeName, long flag) throws JxException;

    public double getDouble(String attributeName) throws JxException;

    public double getDouble(String attributeName, long flag) throws JxException;

    /**
     * 将值转换为适合JSON格式的字符串
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public String getJsonString(String attributeName) throws JxException;

    /**
     * 直接返回java.util.date
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public java.util.Date getDate(String attributeName) throws JxException;

    /**
     * 直接返回java.util.date
     * 
     * @param attributeName
     * @param flag
     * @return
     * @throws JxException
     */
    public java.util.Date getDate(String attributeName, long flag) throws JxException;

    public Object getObject(String attributeName) throws JxException;

    public Object getObject(String attributeName, long flag) throws JxException;

    public boolean setObject(String attributeName, Object value) throws JxException;

    public boolean setObject(String attributeName, Object value, long flag) throws JxException;

    public boolean setString(String attributeName, String value) throws JxException;

    public boolean setString(String attributeName, String value, long flag) throws JxException;

    public Map<String, JboSetIFace> getChildren();

    public JboSetIFace getJboSet();

    public boolean isModify();

    public void setModify(boolean modify);

    public boolean setJboValueModifyAll(boolean modify) throws JxException;

    public boolean isToBeAdd();

    public void setToBeAdd(boolean toBeAdd);

    public boolean isToBeDel();

    public void setToBeDel(boolean toBeDel);

    public JxObject getJxobject() throws JxException;

    public Map<String, Object> getData();

    /**
     * 返回Data数据类型中的简单对象，专门用于JavaScript处理
     * 
     * @return
     */
    public Map<String, Object> getDatas();

    /**
     * 将Data数据作为字符串返回。
     * 
     * @return
     * @throws JxException
     */
    public String toJson() throws JxException;

    public JxTable getJxTable() throws JxException;

    public JboSetIFace getRelationJboSet(String name) throws JxException;

    public JboSetIFace getRelationJboSet(String name, long flag) throws JxException;

    public JboSetIFace getRelationJboSet(String name, long flag, boolean queryAll) throws JxException;

    public boolean getBoolean(String attributeName) throws JxException;

    public boolean getBoolean(String attributeName, long flag) throws JxException;

    public JxAttribute getJxAttribute(String attributeName) throws JxException;

    public boolean isNumeric(String attributeName) throws JxException;

    public long getNewSequence() throws JxException;

    public String getWorkflowId();

    public boolean isReadonly() throws JxException;

    public void setReadonly(boolean readonly);

    public JboIFace getParent() throws JxException;

    /**
     * 返回某个字段是否只读 默认为False
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public boolean isReadonly(String attributeName) throws JxException;

    /**
     * 设定某个字段的只读属性
     * 
     * @param attributeName
     * @param flag
     * @throws JxException
     */
    public void setReadonly(String attributeName, boolean flag) throws JxException;

    /**
     * 设定某个数组字段的只读属性
     * 
     * @param attributeNames 字段名的数组
     * @param flag 是否只读
     * @throws JxException
     */
    public void setReadonly(String[] attributeNames, boolean flag) throws JxException;

    /**
     * 获得某个字段的必填属性
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public boolean isRequired(String attributeName) throws JxException;

    /**
     * 设定某个字段的必填属性
     * 
     * @param attributeName
     * @param flag
     * @throws JxException
     */
    public void setRequired(String attributeName, boolean flag) throws JxException;

    public List<JboIFace> getNeedSaveList();

    /**
     * 添加需要保存
     * 
     * @param jbo
     * @throws JxException
     */
    public void addNeedSaveList(JboIFace jbo) throws JxException;

    /**
     * 能否发送工作流
     * 
     * @param params
     * @return
     * @throws JxException
     */
    public boolean canRoute(Map<String, Object> params) throws JxException;

    /**
     * 发送工作流之前处理的工作。
     * 
     * @param params
     * @return
     * @throws JxException
     */
    public boolean beforeRoute(Map<String, Object> params) throws JxException;

    /**
     * 发送完工作流之后，处理的工作。
     * 
     * @param params
     * @return
     * @throws JxException
     */
    public boolean afterRoute(Map<String, Object> params) throws JxException;

    /**
     * 正常发送工作流的入口程序，不停在第一个节点，启动之后，直接到第二个任务节点
     * 
     * @param params
     * @return
     * @throws JxException
     */
    public boolean route(Map<String, Object> params) throws JxException;

    /**
     * 远端节点调用，使用远端节点，必须覆盖此方法
     * 
     * @param params
     * @return
     * @throws JxException
     */
    public boolean routeWorkflow(Map<String, Object> params) throws JxException;

    /**
     * 可以按自己的业务逻辑重新指定用户
     * 
     * @param curAct 当前节点
     * @param nextAct 下一节点
     * @param assign 当前工作流引擎计算出来的用户信息。<用户ID、用户名>
     * @param agree 参见：JxConstant.WORKFLOW_ROUTE_XXX
     * @param note 工作流备注
     * @param tousers 指定用户,从页面中指定的用户,以逗号分隔
     * @param options 选项，暂时无使用
     * @return 重新指定的用户，<用户ID、用户名>
     * @throws JxException
     */
    public Map<String, String> routeReassign(JboIFace curAct, JboIFace nextAct, Map<String, String> assign, int agree, String note, String tousers, String options) throws JxException;

    public int getAttachmentCount(String vFolder) throws JxException;

    /**
     * 获取用户根据状态自定义工作流发送界面需要填写的字段
     * 
     * @param status 当前状态
     * @param nextStatus 下一个状态
     * @return 返回需要填写字段的集合
     */
    public List<WftParam> getWorkflowParam(String status, String nextStatus);

    /**
     * 将与之相关联的暂缓工作流改为待办
     * 
     * @param jbo
     * @throws JxException
     */
    public void routeHoldon(JboIFace jbo) throws JxException;

    /**
     * 重新加载jbo的数据
     * 
     * @throws JxException
     */
    public void reloadData() throws JxException;

    public void expMpp(Task task, JboIFace jbo) throws JxException;

    /**
     * 添加一个附件
     * 
     * @param name
     * @param attachementSet
     */
    public void addAttachment(String name, JboSetIFace attachementSet);

    /**
     * 移除一个附件
     * 
     * @param name
     * @return Attachment
     */
    public JboSetIFace removeAttachment(String name);

    /**
     * 获取附件
     * 
     * @return Map<String, Attachment>
     */
    public Map<String, JboSetIFace> getAttachments();

    /**
     * 设置附件列表
     * 
     * @param attachments
     */
    public void setAttachments(Map<String, JboSetIFace> attachments);

    /**
     * 附件加载完成之后处理的逻辑
     * 
     * @param name
     * @param attachmentSet
     */
    public void afterAttachmentLoad(String name, JboSetIFace attachmentSet);

    /**
     * 获取数据发生改变的子记录
     * 
     * @return
     */
    public Set<String> getChangedChildren();

    /**
     * 设置数据发生改变的子记录
     * 
     * @return
     */
    public void setChangedChildren(Set<String> changedChildren);

    /**
     * 根据relationship和jboname获取子（嵌套子）jboset
     * 
     * @param relationship 关系名称
     * @return
     * @throws JxException
     */
    public JboSetIFace getChildrenJboSet(String relationship) throws JxException;

    public JboValue getValue(String attributeName, boolean isCreate) throws JxException;

    public void prepareMaxmenu(List<JboIFace> menusToolbar, List<JboIFace> menulist) throws JxException;

    public void removeSomeMaxMenu(List<JboIFace> menusToolbar, List<String> options) throws JxException;
}
