package com.jxtech.workflow.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.i18n.JxLangResourcesUtil;
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

}
