package com.jxtech.action;

import com.jxtech.common.JxActionSupport;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 * 
 */
public class TestAction extends JxActionSupport {
    private static final long serialVersionUID = 1L;

    private String app;
    private String pagenum;// 当前页号

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String execute() throws Exception {
        if (!StrUtil.isNull(app)) {
            app = StrUtil.getSplitFirst(app).toLowerCase();
            return app;
        }
        return super.execute();
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

}
