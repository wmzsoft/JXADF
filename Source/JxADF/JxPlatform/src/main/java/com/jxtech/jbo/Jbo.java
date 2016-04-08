package com.jxtech.jbo;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.workflow.base.IWorkflowEngine;
import com.jxtech.workflow.base.WorkflowEngineFactory;
import com.jxtech.workflow.option.WftParam;
import net.sf.mpxj.DataType;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 每条记录的所有信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class Jbo extends BaseJbo implements JboIFace {
    private static final Logger LOG = LoggerFactory.getLogger(Jbo.class);
    private static final long serialVersionUID = -4050576103988977731L;

    public Jbo(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    /**
     * 能否发送工作流
     */
    @Override
    public boolean canRoute(Map<String, Object> params) throws JxException {
        return true;
    }

    /**
     * 发送完工作流之后，处理的工作。
     */
    @Override
    public boolean afterRoute(Map<String, Object> params) throws JxException {
        return true;
    }

    /**
     * 发送流程前的处理
     */
    @Override
    public boolean beforeRoute(Map<String, Object> params) throws JxException {
        return true;
    }

    /**
     * todo:补充完成参数列表说明 发送工作流的程序
     * <p/>
     * canRoute 和 beforeRoute参数列表：
     * <p/>
     * afterRoute参数列表：
     */
    @Override
    public boolean route(Map<String, Object> params) throws JxException {
        if (!canRoute(params)) {
            return false;
        }
        if (!beforeRoute(params)) {
            return false;
        }

        IWorkflowEngine wfEngine = WorkflowEngineFactory.getWorkflowEngine(getJboSet().getWorkflowEngine());
        if (!wfEngine.route(this, params)) {
            return false;
        }

        if (!afterRoute(params)) {
            return false;
        }
        return true;
    }

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
    @Override
    public Map<String, String> routeReassign(JboIFace curAct, JboIFace nextAct, Map<String, String> assign, int agree, String note, String tousers, String options) throws JxException {
        return assign;
    }

    @Override
    public boolean routeWorkflow(Map<String, Object> params) throws JxException {
        // 如果要启动其它流程，则需要在此获得需要启动流程的JboIFace，然后使用jbo.route(true,agree,note,tousers,options);
        throw new JxException(JxLangResourcesUtil.getString("process.not.support.remote.node"));
    }

    @Override
    public String getWorkflowId() {
        try {
            String instanceid = getWorkflowInstanceId();
            if (!StrUtil.isNull(instanceid)) {
                if (instanceid.startsWith(JboSetIFace.BPM_JX)) {
                    // JXBPM工作流引擎，获得标识
                    int start = JboSetIFace.BPM_JX.length();
                    int end = instanceid.indexOf('.', start + 1);
                    if (start < end) {
                        return instanceid.substring(start + 1, end);
                    } else {
                        return instanceid.substring(start + 1);
                    }
                } else {
                    return instanceid;
                }
            }
            return getJboSet().getWorkflowId();
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获得工作流实例ID，格式：工作流类型.工作流标识(workflowId).具体流程实例，如：JXBPM.1002.1001
     */
    public String getWorkflowInstanceId() throws JxException {
        return getString(JboIFace.WF_INSTANCEID_COLUMN);
    }

    /**
     * 返回当前记录有多少附件
     * 
     * @return
     * @throws JxException
     */
    @Override
    public int getAttachmentCount(String vFolder) throws JxException {
        JboSetIFace attachset = JboUtil.getJboSet("TOP_ATTACHMENT_OBJECT_RELATION");
        DataQueryInfo dq = attachset.getQueryInfo();
        if (StrUtil.isNull(vFolder)) {
            dq.setWhereCause("object_id=? and upper(object_name)=upper(?)");
            dq.setWhereParams(new Object[] { getUidValue(), getJboName() });
        } else {
            dq.setWhereCause("object_id=? and upper(object_name)=upper(?) and attachment_id in (select attachment_id from top_attachment where data_from = ?)");
            dq.setWhereParams(new Object[] { getUidValue(), getJboName(), vFolder });
        }

        return attachset.count();
    }

    @Override
    public List<WftParam> getWorkflowParam(String status, String nextStatus) {
        return null;
    }

    /**
     * 将与之相关联的等待工作流改为待办
     * 
     * @param jbo 需要将等待变为正常的对象
     */
    @Override
    public void routeHoldon(JboIFace jbo) throws JxException {
        if (null == jbo) {
            LOG.debug("给我的JBO是个空的，无法发送相关的等待流程！");
            throw new JxException(JxLangResourcesUtil.getString("process.not.get.waitingrecord"));
        }

        IWorkflowEngine wfEngine = WorkflowEngineFactory.getWorkflowEngine(getJboSet().getWorkflowEngine());
        wfEngine.routeHoldon(this, jbo);
    }

    @Override
    public void reloadData() throws JxException {
        String uidName = getUidName();
        String where = uidName + " = ?";
        DataQuery dq = DBFactory.getDataQuery(getJboSet().getDbtype(), getJboSet().getDataSourceName());
        DataQueryInfo qbe = new DataQueryInfo();
        qbe.setWhereCause(where);
        Object uidValue = getUidValue();
        if (null != uidValue) {
            qbe.setWhereParams(new Object[] { getUidValue() });
            List<Map<String, Object>> list = dq.queryAllPage(getJboName(), qbe);
            if (list != null && list.size() == 1) {
                setData(list.get(0));
            }
        }
    }

    /**
     * 导入.mpp文件
     */
    @Override
    public void addMpp(Task task, Map<String, String> paramMap, Map<String, String> initMap) throws JxException {
        if (null != paramMap) {
            Iterator<?> ite = paramMap.keySet().iterator();
            while (ite.hasNext()) {
                String mppKey = ite.next().toString(); // 读取.mpp文件的标题
                String prama = paramMap.get(mppKey);// 数据库字段
                TaskField tf = TaskField.valueOf(mppKey);
                Object mppValue = task.getCurrentValue(tf);// mpp文件内容值
                DataType dateType = tf.getDataType();
                if (DataType.DATE.equals(dateType)) {// 日期
                    if (mppValue instanceof Date) {
                        Date date = (Date) mppValue;
                        java.sql.Timestamp ts = DateUtil.sqlDateTime(date);
                        setObject(prama, ts);
                    }
                } else {
                    setObject(prama, mppValue);
                }
            }
            if (null != initMap) {// 页面转递过来初始化数据
                Iterator<String> init = initMap.keySet().iterator();
                while (init.hasNext()) {
                    String key = init.next().toString();
                    if (null != key) {
                        String value = initMap.get(key);
                        setObject(key, value);
                    }
                }
            }

        }
    }

    @Override
    public void prepareMaxmenu(List<JboIFace> menusToolbar, List<JboIFace> menulist) throws JxException {
        if (this.isToBeAdd()) {
            Iterator<JboIFace> ite = menusToolbar.iterator();
            while (ite.hasNext()) {
                JboIFace menu = ite.next();
                if ("ADD".equals(menu.getString("MENU"))) {
                    ite.remove();
                }
            }
        }
    }

    /**
     * 移除某些按钮
     * 
     * @author yzh 2015-04-03 15:14:04
     * @param menusToolbar
     * @param options
     * @throws JxException
     */
    @Override
    public void removeSomeMaxMenu(List<JboIFace> menusToolbar, List<String> options) throws JxException {
        Iterator<JboIFace> ite = menusToolbar.iterator();
        while (ite.hasNext()) {
            JboIFace menu = ite.next();
            for (String opt : options) {
                if (opt.equalsIgnoreCase(menu.getString("MENU"))) {
                    ite.remove();
                    break;
                }
            }
        }
    }

    /**
     * 返回工作流状态内部值
     * 
     * @param domainid
     * @return
     * @throws JxException
     */
    @Override
    public String getInternalStaus(String domainid) throws JxException {
        String status = this.getString("WFT_STATUS");
        if (StrUtil.isNull(domainid) || StrUtil.isNull(status)) {
            return status;
        }
        JboIFace syn = JboUtil.findJbo("synonymdomain", "domainid=? and upper(value)=?", new Object[] { domainid.toUpperCase(), status.toUpperCase() });
        if (syn != null) {
            return syn.getString(DBFactory.getDefaultDbColumn().getColumn("MAXVALUE"));
        }
        return status;
    }

}
