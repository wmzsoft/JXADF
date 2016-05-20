package com.jxtech.jbo.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 处理MaxObject表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxObjectDao {
    private static final Logger LOG = LoggerFactory.getLogger(JxObjectDao.class);
    public static final String CACHE_PREX = "OBJECT.";

    /**
     * 查询MaxObject表记录信息
     * 
     * @param objectname
     * @return
     * @throws JxException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, JxObject> query(String objectname) throws JxException {
        String cachekey = StrUtil.contact(CACHE_PREX, objectname);
        Object rso = CacheUtil.getBase(cachekey);
        if (rso instanceof Map) {
            return (Map<String, JxObject>) rso;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select * from MaxObject");
        Object[] params;
        if (!StrUtil.isNull(objectname)) {
            msql.append(" where objectname= ?");
            params = new Object[] { objectname.toUpperCase() };
        } else {
            params = null;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        List<JxObject> rs = dq.getResult(new BeanListHandler<JxObject>(JxObject.class), msql.toString(), params);
        Map<String, JxObject> objs = new DataMap<String, JxObject>();
        if (rs != null) {
            int size = rs.size();
            for (int i = 0; i < size; i++) {
                JxObject obj = rs.get(i);
                objs.put(obj.getObjectname(), obj);
            }
            // 缓存数据
            CacheUtil.putBaseCache(cachekey, objs);
            return objs;
        } else {
            LOG.info("没有查询到数据。" + objectname);
        }
        return objs;
    }

    public static Map<String, JxObject> query() throws JxException {
        return query(null);
    }

    public static JxObject getJxObject(String objectname) throws JxException {
        if (StrUtil.isNull(objectname)) {
            return null;
        }
        Map<String, JxObject> map = query(objectname);
        if (map != null) {
            return map.get(objectname);
        }
        return null;
    }

    public static void reset() {
        CacheUtil.removeBaseOfStartWith(CACHE_PREX);
    }
}
