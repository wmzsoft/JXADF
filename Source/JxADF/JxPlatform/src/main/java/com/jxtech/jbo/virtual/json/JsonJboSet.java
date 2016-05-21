package com.jxtech.jbo.virtual.json;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.jbo.virtual.VirtualJboSet;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.NumUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 虚拟Jbo，不会进行持久化
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JsonJboSet extends VirtualJboSet implements JsonJboSetIFace {

    private static final long serialVersionUID = 1947083557883416724L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonJboSet.class);
    private String jsonUrl;// 从URL中获取JSON数据
    private String jsonData;// 直接传入JSON数据,或通过jsonUrl转换而来的数据。
    private String pageSizeName = "pagesize";// 定义页面大小的字段名,会补充在URL后面.
    private String pageNumName = "pagenum";// 定义当前页的字段名，会补充在URL后面。
    private String countName = "count";// 定义记录数的字段名
    private String whereName = "where";
    private String whereParamName = "whereparam";
    private String orderbyName = "orderby";
    private String jsonHead;// 是否包含头部文件,Y,N,Auto
    private List<Map<String, Object>> listMap;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new JsonJbo(this);
        return currentJbo; // 创建自己的JBO
    }

    public List<JboIFace> toList() throws JxException {
        if (listMap == null) {
            return null;
        }
        int size = listMap.size();
        List<JboIFace> result = new ArrayList<JboIFace>();
        for (int i = 0; i < size; i++) {
            if (i == 0 && hasJsonHead()) {
                setCount((int) NumUtil.parseLong((String) listMap.get(0).get(countName), size));
                continue;
            }
            JboIFace dto = (JboIFace) getJboInstance();
            dto.setData(listMap.get(i));
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        queryJson();
        setJbolist(toList());
        return getJbolist();
    }

    public List<Map<String, Object>> queryJson() throws JxException {
        jsonUrl = getJsonUrl();
        if (StrUtil.isNull(jsonUrl)) {
            return null;
        }
        StringBuilder url = new StringBuilder();
        url.append(jsonUrl);
        if (jsonUrl.indexOf('?') < 0) {
            url.append('?');
        } else {
            url.append('&');
        }
        try {
            DataQueryInfo dqi = getQueryInfo();
            url.append(pageSizeName + "=");
            url.append(dqi.getPageSize());
            url.append("&" + pageNumName + "=");
            url.append(dqi.getPageNum());
            url.append("&" + whereName + "=");
            url.append(URLEncoder.encode(dqi.getWhereAllCause().trim(), "UTF-8"));
            url.append("&" + whereParamName + "=");
            url.append(URLEncoder.encode(StrUtil.toString(dqi.getWhereAllParams()), "UTF-8"));
            url.append("&" + orderbyName + "=");
            url.append(dqi.getOrderby());
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        Object obj = UrlUtil.getUrlContent(url.toString(), true);
        if (obj != null) {
            listMap = JsonUtil.fromJson((String) obj);
        }
        if (listMap != null && !listMap.isEmpty()) {
            Object cnt = listMap.get(0).get("count");
            if (cnt != null && !"".equals(cnt)) {
                try {
                    int count = Integer.parseInt((String) cnt);
                    this.setCount(count);
                } catch (Exception e) {
                    LOG.warn(e.getMessage());
                }
            }
        }
        return listMap;
    }

    /**
     * 此JSON数据是否包含头部
     * 
     * @return
     * @throws JxException
     */
    public boolean hasJsonHead() throws JxException {
        if ("Y".equalsIgnoreCase(jsonHead) || "T".equalsIgnoreCase(jsonHead) || "YES".equalsIgnoreCase(jsonHead)) {
            return true;
        } else if ("N".equalsIgnoreCase(jsonHead) || "F".equalsIgnoreCase(jsonHead) || "NO".equalsIgnoreCase(jsonHead)) {
            return false;
        } else {
            // 自动判断是否包含头
            if (listMap != null && !listMap.isEmpty()) {
                Map<String, Object> dto = listMap.get(0);
                if (dto.get(pageSizeName) != null && dto.get(pageNumName) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getPageSizeName() {
        return pageSizeName;
    }

    public void setPageSizeName(String pageSizeName) {
        this.pageSizeName = pageSizeName;
    }

    public String getPageNumName() {
        return pageNumName;
    }

    public void setPageNumName(String pageNumName) {
        this.pageNumName = pageNumName;
    }

    public String getJsonHead() {
        return jsonHead;
    }

    public void setJsonHead(String jsonHead) {
        this.jsonHead = jsonHead;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public List<Map<String, Object>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public String getWhereName() {
        return whereName;
    }

    public void setWhereName(String whereName) {
        this.whereName = whereName;
    }

    public String getOrderbyName() {
        return orderbyName;
    }

    public void setOrderbyName(String orderbyName) {
        this.orderbyName = orderbyName;
    }

    public String getWhereParamName() {
        return whereParamName;
    }

    public void setWhereParamName(String whereParamName) {
        this.whereParamName = whereParamName;
    }

}
