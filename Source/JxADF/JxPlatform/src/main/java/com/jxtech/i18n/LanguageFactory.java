package com.jxtech.i18n;

import com.jxtech.util.ClassUtil;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 专用于插件的语言包
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.10
 * 
 */
public class LanguageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(LanguageFactory.class);

    // 加载的语言包，Map<语言，bundle>
    private static Map<String, Bundle> languagePackages = new HashMap<String, Bundle>();
    private static String DEFAULT_IMPL_LANGUAGE = "com.jxtech.osgi.framework.i18n.Language";

    /**
     * 获得语言包的实现类
     * 
     * @param bundle
     * @return
     */
    public static LanguageIface getLanguage(Bundle bundle) {
        if (bundle == null) {
            if (languagePackages.size() > 0) {
                Bundle mybundle = languagePackages.values().iterator().next();
                return getLanguage(mybundle, DEFAULT_IMPL_LANGUAGE);
            }
            return null;
        }
        String impl = (String) bundle.getHeaders().get("language-impl");
        if (impl == null || "".equals(impl)) {
            impl = DEFAULT_IMPL_LANGUAGE;
        }
        return getLanguage(bundle, impl);
    }

    public static LanguageIface getLanguage(Bundle bundle, String classname) {
        Class<?> c = null;
        try {
            c = bundle.loadClass(classname);
        } catch (ClassNotFoundException cnfe) {
            c = ClassUtil.loadClass(classname);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        if (c != null) {
            Constructor<?> ct;
            try {
                ct = c.getDeclaredConstructor(new Class[] { Bundle.class });
                return (LanguageIface) ct.newInstance(new Object[] { bundle });
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return null;
    }

    public static Map<String, Bundle> getLanguagePackages() {
        return languagePackages;
    }

    public static void setLanguagePackages(Map<String, Bundle> languagePackages) {
        LanguageFactory.languagePackages = languagePackages;
    }

}
