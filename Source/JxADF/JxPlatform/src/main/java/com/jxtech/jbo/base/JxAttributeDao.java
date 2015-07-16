package com.jxtech.jbo.base;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 处理Maxattribute表的业务逻辑
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxAttributeDao {
    private static final Logger LOG = LoggerFactory.getLogger(JxAttributeDao.class);
    public static final String CACHE_PREX = "ATTRIBUTE.";

    public static Map<String, JxAttribute> getObject(String objectName) throws JxException {
        if (StrUtil.isNull(objectName)) {
            return null;
        }
        objectName = objectName.toUpperCase();
        String cachekey = StrUtil.contact(CACHE_PREX, objectName);
        Object obj = CacheUtil.getBase(cachekey);
        if (obj instanceof Map) {
            return (Map<String, JxAttribute>) obj;
        }
        Map<String, JxAttribute> list = queryTable(objectName);
        if (list != null) {
            CacheUtil.putBaseCache(cachekey, list);
        } else {
            LOG.info(" 没要查询到对应的对象：" + objectName);
        }
        return list;
    }

    private static Map<String, JxAttribute> queryTable(String objectName) throws JxException {
        if (StrUtil.isNull(objectName)) {
            return null;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        String msql = "Select * From maxattribute where objectname = ?";
        List<JxAttribute> list = dq.getResult(new BeanListHandler<JxAttribute>(JxAttribute.class), msql, new Object[] { objectName.toUpperCase() });
        if (list != null) {
            int size = list.size();
            Map<String, JxAttribute> map = new DataMap<String, JxAttribute>();
            for (int i = 0; i < size; i++) {
                JxAttribute attr = list.get(i);
                map.put(attr.getAttributeName(), attr);
            }
            return map;
        }
        return null;
    }

    public static JxAttribute getAttribute(String objectName, String attributeName) throws JxException {
        if (objectName == null || attributeName == null) {
            return null;
        }
        attributeName = attributeName.toUpperCase();
        Map<String, JxAttribute> table = getObject(objectName);
        if (table != null) {
            JxAttribute attr = table.get(attributeName);
            if (attr != null) {
                return attr;
            } else {
                Map<String, JxAttribute> map = queryTable(objectName);
                if (map != null) {
                    return map.get(attributeName);
                }
            }
        }
        return null;
    }

    /**
     * 初始化配置缓存信息
     */
    public static void reset() {
        CacheUtil.removeBaseOfStartWith(CACHE_PREX);
    }
}
