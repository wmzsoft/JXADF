package com.jxtech.action;

import com.jxtech.common.JxActionSupport;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 选择对话框
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class LookupAction extends JxActionSupport {

    private static final long serialVersionUID = 3522987664120671762L;
    private static final Logger LOG = LoggerFactory.getLogger(LookupAction.class);

    private String lookupPage;// 需要弹出哪个选择页面。
    private String fromid;// 从哪个ID弹出的选择框
    private String forwordUrl; // 自定义跳转页面

    @Override
    public String execute() throws Exception {
        if (!StrUtil.isNull(lookupPage)) {
            lookupPage = StrUtil.getSplitFirst(lookupPage).toLowerCase();
            forwordUrl = "/lookup/" + lookupPage + ".jsp";
        }
        LOG.debug(forwordUrl);
        return super.execute();
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getForwordUrl() {
        return forwordUrl;
    }

    public void setForwordUrl(String forwordUrl) {
        this.forwordUrl = forwordUrl;
    }

    public String getLookupPage() {
        return lookupPage;
    }

    public void setLookupPage(String lookupPage) {
        this.lookupPage = lookupPage;
    }

}
