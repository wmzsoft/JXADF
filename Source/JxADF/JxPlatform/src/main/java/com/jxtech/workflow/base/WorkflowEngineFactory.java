package com.jxtech.workflow.base;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * Created by cxm on 2014/8/13
 */
public class WorkflowEngineFactory {
    public static IWorkflowEngine getWorkflowEngine(String workflowType) throws JxException {
        IWorkflowEngine engine = null;
        if (!StrUtil.isNull(workflowType)) {
            if ("OBPM".equalsIgnoreCase(workflowType)) {
                //oracle bpm 12c
                //engine = new OBPMWorkflowEngine();
                engine = WorkflowEngineManager.getEngine(workflowType);
                if (null == engine) {
                    throw new JxException("工作流引擎【" + workflowType + "】插件尚未安装！");
                }
            } else if ("JXBPM".equalsIgnoreCase(workflowType)) {
                // jx bpm
                engine = WorkflowEngineManager.getEngine(workflowType);
                if (null == engine) {
                    throw new JxException("工作流引擎【" + workflowType + "】插件尚未安装！");
                }
            } else {
                throw new JxException("暂时不支持【" + workflowType + "】类型的工作流引擎");
            }
        }

        return engine;
    }

    public static IWorkflowEngine getAppWorkflowEngine(String appname) throws JxException {
        JboIFace appWfInfo = JboUtil.getJbo("MAXAPPSWFINFO", "APP", appname.toUpperCase());
        if (null != appWfInfo) {
            String engine = appWfInfo.getString("ENGINE");
            if (StrUtil.isNull(engine)) {
                throw new JxException("当前应用没有配置工作流信息！");
            }

            return getWorkflowEngine(engine);
        }
        return null;
    }

}
