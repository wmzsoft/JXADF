package com.jxtech.util;

import java.util.List;

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

    public static final String JBO_CACHE = "jboCache";
    public static final String BASE_CACHE = "baseCache";
    public static final String DOMAIN_CACHE = "domainCache";
    public static final String PERMISSION_CACHE = "permissionCache";

    public static void initCache() {
        CacheManager manager = CacheManager.getInstance();
        manager.removeAllCaches();
        manager.addCache(JBO_CACHE);
        manager.addCache(BASE_CACHE);
        manager.addCache(DOMAIN_CACHE);
        manager.addCache(PERMISSION_CACHE);
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

    public static Object getJbo(String uid) {
        return getObject(JBO_CACHE, uid);
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
        if (value == null) {
            c.remove(key);
        } else {
            Element element = new Element(key, value);
            c.put(element);
        }
    }

    public static void putJboCache(Object key, Object value) {
        putCache(JBO_CACHE, key, value);
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

}
