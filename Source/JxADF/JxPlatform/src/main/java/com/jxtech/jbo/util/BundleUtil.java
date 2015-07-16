package com.jxtech.jbo.util;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.10
 */
public class BundleUtil {
    private static final Logger LOG = LoggerFactory.getLogger(BundleUtil.class);

    public static Object getService(String className) {
        return ClassUtil.getInstance(className);
    }

    /**
     * 读取Jar文件中，Manifest文件的内容。
     * 
     * @param jarFile
     * @param key
     * @return
     */
    public static String getManifestProperty(String jarFile, String key) {
        Attributes attrs = getManifestPropertys(jarFile);
        if (attrs != null) {
            return attrs.getValue(key);
        }
        return null;
    }

    /**
     * 读取Jar文件中，Manifest文件的内容。
     * 
     * @param jarFile
     * @return
     */
    public static Attributes getManifestPropertys(String jarFile) {
        if (StrUtil.isNull(jarFile)) {
            return null;
        }
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            return jar.getManifest().getMainAttributes();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
