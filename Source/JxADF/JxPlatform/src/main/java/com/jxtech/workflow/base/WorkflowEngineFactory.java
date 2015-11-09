package com.jxtech.workflow.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * Created by cxm on 2014/8/13
 */
public class WorkflowEngineFactory {
    private static Logger LOG = LoggerFactory.getLogger(WorkflowEngineFactory.class);

    public static IWorkflowEngine getWorkflowEngine(String workflowType) throws JxException {
        IWorkflowEngine engine = null;
        if (!StrUtil.isNull(workflowType)) {
            engine = WorkflowEngineManager.getEngine(workflowType);
            if (null == engine) {
                String msg = JxLangResourcesUtil.getString("WorkflowEngineFactory.getWorkflowEngine.notFoundEngine", new Object[] { workflowType });
                LOG.warn(msg);
                throw new JxException(msg);
            }
        }
        return engine;
    }

    /**
     * 通过应用程序名，返回工作流ID和工作流引擎的名称
     * 
     * @param appname
     * @return
     * @throws JxException
     */
    public static String[] getWorkflow(String appname) throws JxException {
        String[] wfinfo = new String[2];// 0.workdflowId,1:workflowEngine
        if (!StrUtil.isNull(appname)) {
            appname = appname.toUpperCase();
            // 1.优先查看MaxappsWFInfo表中的配置
            JboIFace mwf = JboUtil.findJbo("MAXAPPSWFINFO", "app=? and active=1", new Object[] { appname });
            if (mwf != null) {
                wfinfo[0] = mwf.getString("PROCESS");
                wfinfo[1] = mwf.getString("engine");
            } else {
                // 2. 直接在WFPRocess表中查询
                JboSetIFace wfps = JboUtil.getJboSet("WFPROCESS");
                DataQueryInfo wdqi = wfps.getQueryInfo();
                wdqi.setWhereCause("Appname=? and active=1");
                wdqi.setWhereParams(new Object[] { appname });
                wdqi.setOrderby("processrev desc");
                List<JboIFace> wplist = wfps.query();
                if (wplist != null && wplist.size() > 0) {
                    mwf = wplist.get(0);
                }
                if (mwf != null) {
                    wfinfo[0] = mwf.getString("processNUM");
                    wfinfo[1] = JboSetIFace.BPM_JX;
                } else {
                    LOG.warn("没有找到对应的工作流。");
                }
            }
        } else {
            LOG.warn("没有找到对应的应用程序名。");
        }
        return wfinfo;
    }
}
