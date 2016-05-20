package com.jxtech.jbo.base;

import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxTableDao {

    private static final Logger LOG = LoggerFactory.getLogger(JxTableDao.class);
    public static final String CACHE_PREX = "TABLE.";

    /**
     * 
     * @param tableName
     * @return
     * @throws JxException
     */
    public static JxTable getTable(String tableName) throws JxException {
        if (StrUtil.isNull(tableName)) {
            return null;
        }
        List<JxTable> list = queryTable(tableName);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            LOG.info("list is null :" + tableName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<JxTable> queryTable(String tableName) throws JxException {
        if (StrUtil.isNull(tableName)) {
            return null;
        }
        // 检查是否以前读取过基本信息
        String cachekey = StrUtil.contact(CACHE_PREX, tableName);
        Object obj = CacheUtil.getBase(cachekey);
        if (obj instanceof List) {
            return (List<JxTable>) obj;
        }
        String msql = "Select * From maxtable where tablename = ?";
        DataQuery dq = DBFactory.getDataQuery(null, null);
        List<JxTable> list = dq.getResult(new BeanListHandler<JxTable>(JxTable.class), msql, new Object[] { tableName.toUpperCase() });
        CacheUtil.putBaseCache(cachekey, list);
        return list;
    }

}
