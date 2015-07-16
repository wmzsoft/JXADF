package com.jxtech.app.dashboard.plugin;

import java.util.Map;

/**
 * Created by chenxiaomin@jxtech.net on 2015/1/27.
 */
public abstract class DashboardLetPlugin implements IDashboardLetPlugin {

    @Override
    public abstract String getArtifactId();

    @Override
    public abstract String getTitle();

    @Override
    public String getPreview() {
        return "/jxweb/static/" + getArtifactId() + "/preview/let.png";
    }


    public abstract String getType();
    public String getContent() {
        return "/jxweb/" + getArtifactId() + "/index.action";
    }

    @Override
    public abstract String getDescription();

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
        data.append(getTitle().equals("") ? "插件无标题" : getTitle());
        data.append("', ");
        data.append("description : '");
        data.append(getDescription().equals("") ? "插件无描述" : getDescription());
        data.append("', ");
        data.append("type : '");
        data.append(getType().equals("") ? "" : getType());
        data.append("', ");
        data.append("content : '");
        data.append(getContent().equals("") ? "" : getContent());
        data.append("'}");

        return data.toString();

    }


}
