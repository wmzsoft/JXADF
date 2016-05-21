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
        return queryTable(objectName);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, JxAttribute> queryTable(String objectName) throws JxException {
        if (StrUtil.isNull(objectName)) {
            return null;
        }
        objectName = objectName.toUpperCase();
        String cachekey = StrUtil.contact(CACHE_PREX, objectName);
        Object obj = CacheUtil.getBase(cachekey);
        if (obj instanceof Map) {
            return (Map<String, JxAttribute>) obj;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        String msql = "Select * From maxattribute where objectname = ?";
        @SuppressWarnings("rawtypes")
        Class c = null;
        try {
            c = Class.forName(DBFactory.getDefaultAttributeImpl());
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
        List<JxAttribute> list = dq.getResult(new BeanListHandler<JxAttribute>(c), msql, new Object[] { objectName });
        if (list != null) {
            int size = list.size();
            Map<String, JxAttribute> map = new DataMap<String, JxAttribute>();
            for (int i = 0; i < size; i++) {
                JxAttribute attr = list.get(i);
                map.put(attr.getAttributeName(), attr);
            }
            CacheUtil.putBaseCache(cachekey, map);// 缓存
            return map;
        } else {
            LOG.info("maxattribute not found " + objectName);
        }
        return null;
    }

    public static JxAttribute getAttribute(String objectName, String attributeName) throws JxException {
        if (objectName == null || attributeName == null) {
            return null;
        }
        attributeName = attributeName.toUpperCase();
        Map<String, JxAttribute> table = queryTable(objectName);
        if (table != null) {
            return table.get(attributeName);
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
