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
 * 处理MaxSequence表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxSequenceDao {
    private static final Logger LOG = LoggerFactory.getLogger(JxSequenceDao.class);
    public static final String CACHE_PREX = "SEQUENCE.";

    /**
     * 查询 maxsequence 中的记录
     * 
     * @param tableName
     *            表名
     * @param columnName
     *            字段名
     * @return
     * @throws JxException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, JxSequence> query(String tableName, String columnName) throws JxException {
        String ckey = StrUtil.contact(CACHE_PREX, tableName, ".", columnName);
        Object obj = CacheUtil.getBase(ckey);
        if (obj instanceof Map) {
            return (Map<String, JxSequence>) obj;
        }
        StringBuilder msql = new StringBuilder();
        msql.append("Select * from maxsequence");
        Object[] params = null;
        if (!StrUtil.isNull(tableName) && !StrUtil.isNull(columnName)) {
            msql.append(" where tbname=? and name=?");
            params = new Object[] { tableName.toUpperCase(), columnName.toUpperCase() };
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        List<JxSequence> rs = dq.getResult(new BeanListHandler<JxSequence>(JxSequence.class), msql.toString(), params);
        Map<String, JxSequence> list = new DataMap<String, JxSequence>();
        if (rs != null) {
            int size = rs.size();
            for (int i = 0; i < size; i++) {
                JxSequence seq = rs.get(i);
                String key = StrUtil.contact(seq.getTbname(), ".", seq.getName());
                list.put(key, seq);
            }
            CacheUtil.putBaseCache(ckey, list);// 保存缓存
        } else {
            LOG.debug("查询数据出错。");
        }
        return list;
    }

    /**
     * 获得序列
     * 
     * @param tableName
     * @param columnName
     * @return
     * @throws JxException
     */
    public static String getSequence(String tableName, String columnName) throws JxException {
        if (tableName == null || columnName == null) {
            return null;
        }
        String key = StrUtil.contact(tableName.trim(), ".", columnName.trim());
        Map<String, JxSequence> list = query(tableName, columnName);
        if (list != null) {
            JxSequence seq = list.get(key);
            if (seq != null) {
                return seq.getSequencename();
            }
        }
        return null;
    }
}
