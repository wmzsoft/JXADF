package com.jxtech.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 处理缓存
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class CacheUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CacheUtil.class);

    // 缓存单个Jbo
    public static final String JBO_CACHE = "jboCache";
    // 缓存JboSet
    public static final String JBOSET_CACHE = "jboSetCache";

    public static final String BASE_CACHE = "baseCache";
    public static final String DOMAIN_CACHE = "domainCache";
    public static final String PERMISSION_CACHE = "permissionCache";

    public static void initCache() {
    }

    public static void shutdown() {
        CacheManager.getInstance().shutdown();
    }

    public static Object getObject(String cacheName, Object key) {
        if (key == null) {
            return null;
        }
        if (StrUtil.isNull(cacheName)) {
            cacheName = BASE_CACHE;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(cacheName);
        if (c == null) {
            return null;
        }
        Element e = c.get(key);
        if (e != null) {
            return e.getObjectValue();
        }
        return null;
    }

    /**
     * 移出以prekey开始的所有缓存，key为空时，移出所有
     * 
     * @param cacheName
     * @param prekey
     * @return
     */
    public static void removeObjectOfStartWith(String cacheName, String prekey) {
        if (StrUtil.isNull(cacheName)) {
            cacheName = BASE_CACHE;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(cacheName);
        if (c == null) {
            return;
        }
        List<?> list = c.getKeys();
        if (list == null) {
            return;
        }
        if (StrUtil.isNull(prekey)) {
            // 移出所有
            c.removeAll();
        } else {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Object k = list.get(i);
                if (k instanceof String) {
                    if (((String) k).startsWith(prekey)) {
                        c.remove(k);
                    }
                }
            }
        }
    }

    /**
     * 单个Jbo的缓存Key生成器
     * 
     * @param jboname
     * @param uid
     * @return
     */
    public static String genJboKey(String jboname, String uid) {
        if (StrUtil.isNull(jboname) || StrUtil.isNull(uid)) {
            return null;
        }
        return StrUtil.contact(jboname.toUpperCase(), ".", uid);
    }

    /**
     * Jbo缓存查询
     * 
     * @param key
     * @return
     */
    public static JboIFace getJbo(String key) {
        Object obj = getObject(JBO_CACHE, key);
        if (obj instanceof JboIFace) {
            return (JboIFace) obj;
        }
        return null;
    }

    /**
     * 将Jbo放入缓存
     * 
     * @param key
     * @param value
     */
    public static void putJboCache(String key, JboIFace value) throws JxException {
        if (StrUtil.isNull(key)) {
            return;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(JBO_CACHE);
        if (c == null) {
            return;
        }
        if (value == null || !value.canCache()) {
            LOG.debug("remove cache:" + key);
            c.remove(key);
        } else {
            Element element = new Element(key, value);
            c.put(element);
        }
    }

    /**
     * 将Jbo移出缓存
     * 
     * @param key
     */
    public static void removeJbo(String key) {
        if (StrUtil.isNull(key)) {
            return;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(JBO_CACHE);
        if (c == null) {
            return;
        }
        c.remove(key);
    }

    /**
     * 生成对应的JboSet的Key
     * 
     * @param jboset
     * @param cache
     * @return
     * @throws JxException
     */
    public static String genJboSetKey(JboSetIFace jboset, boolean cache) throws JxException {
        if (jboset == null || !jboset.canCache()) {
            return null;
        }
        DataQueryInfo dqi = jboset.getQueryInfo();
        return StrUtil.contact(jboset.getJboname(), ".", String.valueOf(dqi.getQueryId(cache)));
    }

    /**
     * 返回JboSet对象
     * 
     * @param key
     * @return
     * @throws JxException
     */
    public static JboSetIFace getJboSet(String key) throws JxException {
        Object obj = getObject(JBOSET_CACHE, key);
        if (obj instanceof JboSetIFace) {
            return (JboSetIFace) obj;
        }
        return null;
    }

    /**
     * 获得JboSet的结果集
     * 
     * @param key
     * @return
     * @throws JxException
     */
    public static List<JboIFace> getJboSetList(String key) throws JxException {
        JboSetIFace js = getJboSet(key);
        if (js != null) {
            return js.getJbolist();
        }
        return null;
    }

    /**
     * 存放JboSet
     * 
     * @param key
     * @param jboset
     */
    public static void putJboSet(JboSetIFace jboset) throws JxException {
        String key = genJboSetKey(jboset, true);// 生成Key
        putJboSet(key, jboset);
    }

    /**
     * 存放JboSet
     * @param key
     * @param jboset
     * @throws JxException
     */
    public static void putJboSet(String key, JboSetIFace jboset) throws JxException {
        if (StrUtil.isNull(key)) {
            return;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(JBOSET_CACHE);
        if (c == null) {
            return;
        }
        Element element = new Element(key, jboset);
        c.put(element);
    }

    public static Object getBase(String key) {
        return getObject(BASE_CACHE, key);
    }

    public static Object getDomain(String key) {
        return getObject(DOMAIN_CACHE, key);
    }

    public static Object getPermission(String key) {
        return getObject(PERMISSION_CACHE, key);
    }

    public static void putCache(String cacheName, Object key, Object value) {
        if (key == null) {
            return;
        }
        if (StrUtil.isNull(cacheName)) {
            cacheName = BASE_CACHE;
        }
        CacheManager manager = CacheManager.getInstance();
        Cache c = manager.getCache(cacheName);
        if (c == null) {
            return;
        }
        if (value == null) {
            c.remove(key);
        } else {
            Element element = new Element(key, value);
            c.put(element);
        }
    }

    public static void putBaseCache(Object key, Object value) {
        putCache(BASE_CACHE, key, value);
    }

    public static void putDomainCache(Object key, Object value) {
        putCache(DOMAIN_CACHE, key, value);
    }

    public static void putPermissionCache(Object key, Object value) {
        putCache(PERMISSION_CACHE, key, value);
    }

    public static void removeJboOfStartWith(String prekey) {
        removeObjectOfStartWith(JBO_CACHE, prekey);
        removeObjectOfStartWith(JBOSET_CACHE, prekey);
    }

    public static void removeBaseOfStartWith(String prekey) {
        removeObjectOfStartWith(BASE_CACHE, prekey);
    }

    public static void removeDomainOfStartWith(String prekey) {
        removeObjectOfStartWith(DOMAIN_CACHE, prekey);

    }

    public static void removePermissionOfStartWith(String prekey) {
        removeObjectOfStartWith(PERMISSION_CACHE, prekey);
    }

    /**
     * 清空登录用户的缓存
     */
    public static void cleanUserCache() {
        String ckey = StrUtil.contact(JxSession.getUserId(), ".");
        removeObjectOfStartWith(PERMISSION_CACHE, ckey);
    }
}
