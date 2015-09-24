package com.jxtech.util;

import org.apache.struts2.osgi.BundleAccessor;
import org.apache.struts2.osgi.DefaultBundleAccessor;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 类加载器
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class ClassUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 首先在本地类加载，没发现再去加载OSGi中的类
     * 
     * @param className
     * @return
     */
    public static Object getInstance(String className) {
        return getInstance(className, true);
    }

    public static Class<?> loadClass(String className) {
        if (StrUtil.isNull(className)) {
            return null;
        }
        try {
            // 首先在本地找一找
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            // 本地找不到，去到OSGi中找一找
            DefaultBundleAccessor bundleAcessor = DefaultBundleAccessor.getInstance();
            if (bundleAcessor != null) {
                try {
                    Class<?> clas = bundleAcessor.loadClass(className);
                    if (clas != null) {
                        return clas;
                    } else {
                        return loadServiceClass(bundleAcessor, className);
                    }
                } catch (Exception e) {
                    LOG.info(e.getMessage() + "\r\n" + className);
                    return loadServiceClass(bundleAcessor, className);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public static Object getInstance(String className, boolean isNew) {
        if (StrUtil.isNull(className)) {
            return null;
        }
        try {
            // 首先在本地找一找
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException ex) {
            // 本地找不到，去到OSGi中找一找
            DefaultBundleAccessor bundleAcessor = DefaultBundleAccessor.getInstance();
            if (bundleAcessor != null) {
                try {
                    Class<?> clas = bundleAcessor.loadClass(className);
                    if (clas != null) {
                        return clas.newInstance();
                    } else {
                        return getService(bundleAcessor, className, isNew);
                    }
                } catch (Exception e) {
                    //LOG.debug(e.getMessage() + "\r\n" + className);
                    return getService(bundleAcessor, className, isNew);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public static Class<?> loadServiceClass(BundleAccessor bundle, String className) {
        ServiceReference<?> ref = bundle.getServiceReference(className);
        if (ref != null) {
            try {
                Object obj = bundle.getService(ref);
                try {
                    return obj.getClass();
                } catch (Exception e) {
                    LOG.debug(className + "不能直接newInstance");
                    return null;
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage() + "\r\n" + className);
            }
        }
        return null;
    }

    public static Object getService(BundleAccessor bundle, String className, boolean isNew) {
        ServiceReference<?> ref = bundle.getServiceReference(className);
        if (ref != null) {
            try {
                Object obj = bundle.getService(ref);
                if (isNew) {
                    try {
                        return obj.getClass().newInstance();
                    } catch (Exception e) {
                        LOG.info(className + "不能直接newInstance");
                        return obj;
                    }
                }
                return obj;
            } catch (Exception ex) {
                LOG.error(ex.getMessage() + "\r\n" + className);
            }
        }
        return null;
    }

    public static Object getStaticMethod(String className, String method) {
        return getStaticMethod(className, method, null, null);
    }

    /**
     * 调用静态方法
     * 
     * @param className 类名
     * @param method 方法名
     * @param parameterTypes
     * @param params
     * @return
     */
    public static Object getStaticMethod(String className, String method, Class<?>[] parameterTypes, Object[] params) {
        if (StrUtil.isNull(className) || StrUtil.isNull(method)) {
            return null;
        }
        Class<?> cs = null;
        try {
            // 首先在本地找一找
            cs = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            // 本地找不到，去到OSGi中找一找
            DefaultBundleAccessor bundleAcessor = DefaultBundleAccessor.getInstance();
            if (bundleAcessor != null) {
                try {
                    cs = bundleAcessor.loadClass(className);
                } catch (Exception e) {
                    LOG.info(e.getMessage() + "\r\n" + className);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        if (cs == null) {
            return null;
        }
        try {
            Method m;
            if (parameterTypes != null) {
                m = cs.getMethod(method, parameterTypes);
                return m.invoke(cs, params);
            } else {
                m = cs.getMethod(method);
                return m.invoke(cs);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }
}
