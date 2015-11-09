package com.jxtech.app.dashboard.plugin;

import java.util.Map;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;

/**
 * Created by chenxiaomin@jxtech.net on 2015/1/27.
 */
public abstract class DashboardLetPlugin implements IDashboardLetPlugin {

    @Override
    public String getPreview() {
        return StrUtil.contact(SysPropertyUtil.getBase(), "/static/", getArtifactId(), "/preview/let.png");
    }

    @Override
    public String getContent() {
        return StrUtil.contact(SysPropertyUtil.getBase(), "/", getArtifactId(), "/index.action");
    }

    @Override
    public String getLetData(Map<String, String> paramMap) {
        StringBuilder data = new StringBuilder();

        data.append("{");
        data.append("isPlugin : true, ");
        data.append("artifactid : '");
        data.append(getArtifactId());
        data.append("', ");
        data.append("id : '");
        data.append(paramMap.get("DBNUM")).append("_").append(paramMap.get("DBLETNUM"));
        data.append("', ");
        data.append("title : '");
        String title = getTitle();
        if (StrUtil.isNull(title)) {
            title = JxLangResourcesUtil.getString("com.jxtech.app.dashboard.plugin.DashboardLetPlugin.getLetData.title");
        }
        data.append(title);
        data.append("', ");
        data.append("description : '");
        String desc = getDescription();
        if (StrUtil.isNull(desc)) {
            desc = JxLangResourcesUtil.getString("com.jxtech.app.dashboard.plugin.DashboardLetPlugin.getLetData.description");
        }
        data.append(desc);
        data.append("', ");
        data.append("type : '");
        String type = getType();
        if (!StrUtil.isNull(type)) {
            data.append(type);
        }
        data.append("', ");
        data.append("content : '");
        String content = this.getContent();
        if (!StrUtil.isNull(content)) {
            data.append(content);
        }
        data.append("'}");

        return data.toString();

    }

}
