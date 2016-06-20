package com.jxtech.distributed;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;

import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;

/**
 * 为分布式处理Bundle的基础类
 * 
 * @author wmzsoft@gmail.com
 * @date 2016.06
 */
public class BundleHelper {
    private static final String DATA_APP = "APP";
    private static final String DATA_URL = "URL";

    /**
     * 返回 /jxwb/com.jxtech.autokey.1.0.0
     * 
     * @param bundle
     * @return
     */
    public static String getPath(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        return StrUtil.contact(Configuration.getInstance().getBaseBundle(), "/", bundle.getSymbolicName(), ".", bundle.getVersion().toString());
    }

    /**
     * 返回JSON格式的[{"app":"autokey","url":"/autokey/index.action"}]
     * 
     * @param bundle
     * @return
     */
    public static String getData(Bundle bundle) {
        if (bundle == null) {
            return "[]";
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Dictionary<String, String> dic = bundle.getHeaders();
        String url = dic.get("Jx-AppURL");
        if (StrUtil.isNull(url)) {
            return "[]";
        }
        String[] urls = url.split(",");
        for (int i = 0; i < urls.length; i++) {
            Map<String, Object> dto = new HashMap<String, Object>();
            if (urls[i] != null) {
                int pos = urls[i].indexOf('/', 1);
                String appname;
                if (pos > 1) {
                    appname = urls[i].substring(1, pos);
                } else {
                    appname = urls[i].substring(1);
                }
                dto.put(DATA_APP, appname.toUpperCase());
                dto.put(DATA_URL, urls[i]);
                list.add(dto);
            }
        }
        return JsonUtil.getJsonOfList(list);
    }

}
