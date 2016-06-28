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

    // 保存时不做任何校验
    public static final long SAVE_NO_CHECK_ALL = 1L;
    // 保存时，不做只读校验
    public static final long SAVE_NO_CHECK_READONLY = 2L;
    // 保存时，不做必填校验
    public static final long SAVE_NO_CHECK_REQUIRED = 4L;
    // 保存时,不检查权限
    public static final long SAVE_NO_CHECK_PERMISSION = 8L;
    // 工作流状态字段
    public static final String WF_STATUS_COLUMN = "WFT_STATUS";
    // 工作流状态时间
    public static final String WF_STATUS_DATE_COLUMN = "WFT_STATUSDATE";
    // 工作流实例字段
    public static final String WF_INSTANCEID_COLUMN = "WFT_INSTANCEID";
    // 工作流当前分配人员字段
    public static final String WF_HOLD_NAME_COLUMN = "WFT_TRANSACTOR";
    // 工作流当前分配人员姓名字段
    public static final String WF_HOLD_ID_COLUMN = "WFT_TRANSACTOR_ID";

    // 工作流发送成功
    public static final long ROUTE_SUCCESS = 1;
    // 工作流发送完毕，关闭结点
    public static final long ROUTE_CLOSE = 2;
    // 工作流发送，启动成功
    public static final long ROUTE_START = 4;

    public static final String[] STATUS_HISTORY = new String[] { "CLOSE", "CAN", "CANCEL" };

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

    /**
     * 返回字段值
     * 
     * @param attributeName
     *            字段名
     * @return
     * @throws JxException
     */
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

    /**
     * 获得联系JboSet,并执行查询,查询第一页结果
     * 
     * @param name
     * @return
     * @throws JxException
     */
    public JboSetIFace getRelationJboSet(String name) throws JxException;

    /**
     * 获得联系JboSet，如果执行查询，则查询第一页结果
     * 
     * @param name
     * @param executeQuery
     *            是否执行查询
     * @return
     * @throws JxException
     */
    public JboSetIFace getRelationJboSet(String name, boolean executeQuery) throws JxException;

    /**
     * 获得联系JboSet，执行查询，获得第一页结果，可自定义是否从缓存取数据
     * 
     * @param name
     * @param flag
     * @return
     * @throws JxException
     */
    public JboSetIFace getRelationJboSet(String name, long flag) throws JxException;

    /**
     * 获得联系查询结果，可定义是否从缓存取数据，是否获得所有结果。
     * 
     * @param name
     * @param flag
     * @param queryAll
     * @return
     * @throws JxException
     */
    public JboSetIFace getRelationJboSet(String name, long flag, boolean queryAll) throws JxException;

    /**
     * 获得联系JboSet
     * 
     * @param name
     *            联系名
     * @param flag
     *            查询标识
     * @param queryAll
     *            是否获得所有结果
     * @param executeQuery
     *            是否执行查询
     * @return
     * @throws JxException
     */
    public JboSetIFace getRelationJboSet(String name, long flag, boolean queryAll, boolean executeQuery) throws JxException;

    /**
     * 获得Boolean类型值
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public boolean getBoolean(String attributeName) throws JxException;

    /**
     * 获得Boolean类型的值,并确定是否从缓存中获取
     * 
     * @param attributeName
     * @param flag
     * @return
     * @throws JxException
     */
    public boolean getBoolean(String attributeName, long flag) throws JxException;

    /**
     * 获得某个字段的定义信息
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public JxAttribute getJxAttribute(String attributeName) throws JxException;

    /**
     * 获得某个字段是否是数字类型
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public boolean isNumeric(String attributeName) throws JxException;

    /**
     * 获得一个新的序列号
     * 
     * @return
     * @throws JxException
     */
    public long getNewSequence() throws JxException;

    /**
     * 获得工作流标识，用于启动工作流
     * 
     * @return
     */
    public String getWorkflowId();

    /**
     * 获得工作流实例，用于继续发送工作流
     * 
     * @return
     * @throws JxException
     */
    public String getWorkflowInstanceId() throws JxException;

    /**
     * 是否只读
     * 
     * @return
     * @throws JxException
     */
    public boolean isReadonly() throws JxException;

    /**
     * 设定只读
     * 
     * @param readonly
     */
    public void setReadonly(boolean readonly);

    /**
     * 设定除 attributeNames 字段之外的其它字段只读
     * 
     * @param attributeNames
     * @param flag
     * @throws JxException
     */
    public void setReadonlyBesides(String[] attributeNames, boolean flag) throws JxException;

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
     * @param attributeNames
     *            字段名的数组
     * @param flag
     *            是否只读
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
     * 是否可以缓存,默认情况下，只缓存不发生变化的数据
     * 
     * @return
     * @throws JxException
     */
    public boolean canCache() throws JxException;

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
     * @param curAct
     *            当前节点
     * @param nextAct
     *            下一节点
     * @param assign
     *            当前工作流引擎计算出来的用户信息。<用户ID、用户名>
     * @param agree
     *            参见：JxConstant.WORKFLOW_ROUTE_XXX
     * @param note
     *            工作流备注
     * @param tousers
     *            指定用户,从页面中指定的用户,以逗号分隔
     * @param options
     *            选项，暂时无使用
     * @return 重新指定的用户，<用户ID、用户名>
     * @throws JxException
     */
    public Map<String, String> routeReassign(JboIFace curAct, JboIFace nextAct, Map<String, String> assign, int agree, String note, String tousers, String options) throws JxException;

    public int getAttachmentCount(String vFolder) throws JxException;

    /**
     * 获取用户根据状态自定义工作流发送界面需要填写的字段
     * 
     * @param status
     *            当前状态
     * @param nextStatus
     *            下一个状态
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
     * @param relationship
     *            关系名称
     * @return
     * @throws JxException
     */
    public JboSetIFace getChildrenJboSet(String relationship) throws JxException;

    /**
     * 获得Jbo的子Jbo。
     * 
     * @param relationship
     * @param isCreate
     *            如果不存在，是否创建一个新的JboSet
     * @return
     * @throws JxException
     */
    public JboSetIFace getChildrenJboSet(String relationship, boolean isCreate) throws JxException;

    /**
     * 设定当前Jbo的子JboSet的只读属性。
     * 
     * @param relationship
     * @param readonly
     * @throws JxException
     */
    public void setChildrenReadonly(String relationship, boolean readonly) throws JxException;

    public JboValue getValue(String attributeName, boolean isCreate) throws JxException;

    public void prepareMaxmenu(List<JboIFace> menusToolbar, List<JboIFace> menulist) throws JxException;

    public void removeSomeMaxMenu(List<JboIFace> menusToolbar, List<String> options) throws JxException;

    public long getSaveFlag();

    public void setSaveFlag(long saveFlag);

    public long getRouteStatus();

    public void setRouteStatus(long routeStatus);

    public boolean isRouteClose();

    public boolean isRouteStart();

    /**
     * 将值转换为URL的格式
     * 
     * @param attributeName
     * @return
     * @throws JxException
     */
    public String getURLString(String attributeName) throws JxException;

    /**
     * 返回工作流状态内部值
     * 
     * @param domainid
     * @return
     * @throws JxException
     */
    public String getInternalStaus(String domainid) throws JxException;

    /**
     * 直接根据表名、条件查询某个值
     * 
     * @param jboname
     * @param whereCause
     * @param params
     * @param attributeName
     * @return
     * @throws JxException
     */
    public Object findDataAttributeValue(String jboname, String whereCause, Object[] params, String attributeName) throws JxException;

    /**
     * 获得在导出时,需要导出的联系关联表.
     * 
     * @return 需要导出的联系名
     * @throws JxException
     */
    public String[] getExportRelationship() throws JxException;

    public boolean isToBeDuplicate();

    public void setToBeDuplicate(boolean toBeDuplicate);

    /**
     * 通过反射，调用Jbo的方法，无参数
     * 
     * @param name
     *            方法名
     * @return
     */
    public Object invokeGetMethod(String name);

    /**
     * 通过反射调用Jbo的方法，有参数
     * 
     * @param name
     *            方法名
     * @param parameterTypes
     *            参数类型
     * @param params
     *            参数值
     * @return
     */
    public Object invokeMethod(String name, Class<?>[] parameterTypes, Object[] params);

}
