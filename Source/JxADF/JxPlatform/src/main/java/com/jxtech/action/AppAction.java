package com.jxtech.action;

import com.jxtech.common.JxActionSupport;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Maximo 应用程序主页面
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class AppAction extends JxActionSupport {
    private static final long serialVersionUID = 1440993367883911449L;
    private static final Logger LOG = LoggerFactory.getLogger(AppAction.class);
    private String app;
    private String type;// app的类型，一般取值为list,空
    private String fromid;// 从哪个ID弹出的选择框
    private String forwordUrl; // 自定义跳转页面
    private String flag;// 标记
    private String msg; // 消息

    private String uid;
    private String instanceid;

    public AppAction() {
        super();
    }

    @Override
    public String execute() throws Exception {
        if (!StrUtil.isNull(app)) {
            app = StrUtil.getSplitFirst(app).toLowerCase();
            type = StrUtil.getSplitFirst(type);
            flag = StrUtil.getSplitFirst(flag);
            msg = StrUtil.getSplitFirst(msg);

            if ("init".equalsIgnoreCase(flag)) {
                // 初始化，将原来保存的内容删除。
                JxSession.removeApp(app, type);
            } else if ("add".equalsIgnoreCase(flag)) {
                // 新增
            } else if ("view".equalsIgnoreCase(flag)) {
            }

            if (type == null) {
                forwordUrl = "/app/" + app + "/" + app + ".jsp";
            } else {
                forwordUrl = "/app/" + app + "/" + type + ".jsp";
            }

            forwordUrl = forwordUrl.toLowerCase();
            LOG.debug(forwordUrl);
        }
        return super.execute();
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForwordUrl() {
        return forwordUrl;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

}
