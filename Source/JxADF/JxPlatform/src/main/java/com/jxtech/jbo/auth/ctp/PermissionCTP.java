package com.jxtech.jbo.auth.ctp;

import com.jxtech.jbo.auth.Permission;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 定义权限相关的处理
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class PermissionCTP extends Permission {
    private static final long serialVersionUID = 3132860231432719525L;
    private static final Logger LOG = LoggerFactory.getLogger(PermissionCTP.class);

    /**
     * 是否具有某个页面的某个操作权限
     *
     * @param pageid
     * @param methodName
     * @return
     */
    @Override
    public boolean hasFunctions(String pageid, String methodName) {
        LOG.debug("权限认证：" + pageid + "." + methodName);
        if (StrUtil.isNull(pageid)) {
            return true;// 页面为空
        }
        String key = pageid;
        if (methodName != null) {
            key = key + "." + methodName;
        }
        // 获最功能ID号
        Map<String, String> funs = getFunctions();
        if (funs == null) {
            LOG.info("没有正确加载权限:" + key.toUpperCase());
            return false;// 没有正确加载数据。
        }
        String optid = funs.get(key.toUpperCase());
        if (StrUtil.isNull(optid)) {
            LOG.debug(key.toUpperCase() + "，没有在MAXMENU表中配置此操作。");
            return true;// 此功能不需要进行权限管理。
        }
        Map<String, String> usrfuns = getUserFunctions();
        if (usrfuns == null) {
            LOG.info("没有正确加载用户权限" + pageid + "." + methodName);
            return false;// 没有正确加载用户权限。
        }
        return usrfuns.containsKey(optid);
    }
}
