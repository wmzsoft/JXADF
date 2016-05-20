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
 * 处理MaxRelationship表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxRelationshipDao {

    private static final Logger LOG = LoggerFactory.getLogger(JxRelationshipDao.class);
    public static final String CACHE_PREX = "RELATIONSHIP.";

    /**
     * 查询 maxrelationship 中的数据
     * 
     * @param parent
     * @return
     * @throws JxException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, JxRelationship> query(String parent) throws JxException {
        // 查询缓存数据
        String cachekey = StrUtil.contact(CACHE_PREX, parent);
        Object objm = CacheUtil.getBase(cachekey);
        if (objm instanceof Map) {
            return (Map<String, JxRelationship>) objm;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select * from maxrelationship");
        Object[] params = null;
        if (!StrUtil.isNull(parent)) {
            msql.append(" where parent=? ");
            parent = parent.toUpperCase();
            params = new Object[] { parent };
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        List<JxRelationship> rs = dq.getResult(new BeanListHandler<JxRelationship>(JxRelationship.class), msql.toString(), params);
        Map<String, JxRelationship> ships = new DataMap<String, JxRelationship>();
        if (rs != null) {
            int size = rs.size();
            for (int i = 0; i < size; i++) {
                JxRelationship ship = rs.get(i);
                String key = StrUtil.contact(ship.getName(), ".", ship.getParent());
                ships.put(key, ship);
            }
            CacheUtil.putBaseCache(cachekey, ships);// 保存缓存
            return ships;
        } else {
            LOG.info("没有查询到数据。" + parent);
        }
        return ships;
    }

    /**
     * 获得联系
     * 
     * @param parent
     *            主对象
     * @param name
     *            联系名
     * @return
     * @throws JxException
     */
    public static JxRelationship getJxRelationship(String parent, String name) throws JxException {
        Map<String, JxRelationship> ships = query(parent);
        if (ships == null) {
            return null;
        }
        String key = StrUtil.contact(name, ".", parent);
        key = StrUtil.convertUtf8CharToString(key);
        JxRelationship ship = ships.get(key);
        if (ship == null) {
            LOG.warn("没有获到正确的联系，联系名：" + name + "，主对象：" + parent);
        }
        return ship;
    }
}
