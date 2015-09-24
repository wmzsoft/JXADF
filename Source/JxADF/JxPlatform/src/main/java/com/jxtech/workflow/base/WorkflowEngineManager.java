package com.jxtech.workflow.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流引擎管理器 Created by chenxiaomin@jxtech.net on 2015/3/10.
 */
public class WorkflowEngineManager {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowEngineManager.class);
    // <engineType,engineClass>
    private static Map<String, String> engines = new HashMap<String, String>();

    /**
     * 注册工作流引擎
     * 
     * @param engineType
     * @param engine
     */
    public static void regiestEngine(String engineType, String engine) {
        if (StrUtil.isNull(engine)) {
            engines.remove(engineType);
            LOG.info("移出工作流引擎类型【" + engineType + "】：" + engine);
        } else {
            engines.put(engineType, engine);
            LOG.info("注册工作流引擎类型【" + engineType + "】：" + engine);
        }
    }

    /**
     * 获得工作流引擎实例
     * 
     * @param engineType
     * @return
     */
    public static IWorkflowEngine getEngine(String engineType) {
        if (!engines.isEmpty() && engines.containsKey(engineType)) {
            String myengine = engines.get(engineType);
            Object obj = ClassUtil.getInstance(myengine, true);
            if (obj instanceof IWorkflowEngine) {
                return (IWorkflowEngine) obj;
            } else {
                LOG.warn("实例化工作流引擎失败。" + engineType);
            }
        }
        return null;
    }
}
