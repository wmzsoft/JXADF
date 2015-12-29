package com.jxtech.jbo.base;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DbColumn;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理基本数据信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class DataMap<K, V> extends HashMap<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(DataMap.class);
    private static final long serialVersionUID = 6516717659356304269L;
    private JboIFace jbo;

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (key == null) {
            LOG.warn("DataMap 输入的Key为空");
            return null;
        }
        if (super.containsKey(key)) {
            return super.get(key);
        }
        if (!(key instanceof String)) {
            return super.get(key);
        }
        String k = ((String) key).toUpperCase();
        if (super.containsKey(k)) {
            return super.get(k);
        }
        if (k.indexOf(".") > 0) {
            // 处理联系问题
            if (jbo != null) {
                try {
                    return (V) jbo.getObject(k);
                } catch (JxException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        return super.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        if (key instanceof String) {
            String mykey = ((String) key).toUpperCase();
            if (jbo != null) {
                DbColumn dbc = DBFactory.getDbColumn(jbo.getJboSet().getDbtype());
                if (dbc != null) {
                    String mykey2 = dbc.getColumn(mykey).toUpperCase();
                    if (!mykey.equals(mykey2)) {
                        super.put((K) mykey2, value);
                    }
                }
            }
            return super.put((K) mykey, value);
        }
        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String) {
            return super.remove(((String) key).toUpperCase());
        }
        return super.remove(key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return super.containsKey(key.toString());
        }
        return super.containsKey(key);
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String[] keys() {
        int size = size();
        String[] ks = new String[size];
        int i = 0;
        for (Map.Entry<K, V> entry : entrySet()) {
            ks[i] = (String) entry.getKey();
            i = i + 1;
        }
        return ks;
    }

}
