package com.jxtech.workflow.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxiaomin@jxtech.net on 2015/3/10.
 */
public class WorkflowEngineManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowEngineManager.class);
    private static Map<String, IWorkflowEngine> engines = new HashMap<String, IWorkflowEngine>();
    
    public static void regiestEngine(String engineType, IWorkflowEngine engine) {
        if (engines.containsKey(engineType)) {
            LOG.info("当前工作流引擎类型【" + engineType + "】已经存在，使用新的覆盖旧的");
        }

        engines.put(engineType, engine);
    }

    public static IWorkflowEngine getEngine(String engineType) {
        if (!engines.isEmpty() && engines.containsKey(engineType)) {
            return engines.get(engineType);
        } else {
            return null;
        }
    }
}
