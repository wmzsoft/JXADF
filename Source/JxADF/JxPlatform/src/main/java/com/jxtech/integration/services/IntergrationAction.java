package com.jxtech.integration.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.JxActionSupport;

/**
 * 平台集成服务servlet Created by cxm on 15/4/20
 */

public class IntergrationAction extends JxActionSupport {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(IntergrationAction.class);

    private String intergrationData;

    public String execute() throws Exception {
        Intergration interg = IntergrationFactory.getIntergration();
        if (!interg.execute(intergrationData)) {
            LOG.info("处理数据失败:" + intergrationData);
        }
        return super.execute();
    }

    public String getIntergrationData() {
        return intergrationData;
    }

    public void setIntergrationData(String intergrationData) {
        this.intergrationData = intergrationData;
    }
}
