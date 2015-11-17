package com.jxtech.util;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.util.JxException;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 处理URL获得的数据
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class UrlUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtil.class);
    public static final String CACHE_PREX = "URL.UTIL.";

    public static Object getUrlContent(String url, boolean cache) {
        return getUrlContent(url, "UTF-8", cache);
    }

    /**
     * 读取URL文件中的内容
     * 
     * @param url
     * @param urlCode
     * @return
     */
    public static Object getUrlContent(String url, String urlCode) {
        return getUrlContent(url, urlCode, false);
    }

    public static Object getUrlContent(String url, String urlCode, boolean cache) {
        if (StrUtil.isNull(url)) {
            return url;
        }
        String ckey = null;
        if (cache) {
            ckey = StrUtil.contact(CACHE_PREX, String.valueOf(url.hashCode()), ".", urlCode);
            Object obj = CacheUtil.getJbo(ckey);
            if (obj != null) {
                return obj;
            }
        }
        BufferedInputStream bis = null;
        Reader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = encodeUrl(url, "UTF-8");
            URL u = new URL(url);
            bis = new BufferedInputStream(u.openStream());
            reader = new InputStreamReader(bis, urlCode);
            char[] buf = new char[1];
            reader.read(buf);
            if (buf[0] == 65279) {
                LOG.debug("UTF-8 BOM : " + url);
            } else {
                sb.append(buf);
            }
            while ((reader.read(buf)) != -1) {
                sb.append(buf);
            }
            if (cache) {
                CacheUtil.putJboCache(ckey, sb.toString());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(bis);
        }
        return sb.toString();
    }

    public static String encodeUrl(String url, String code) throws UnsupportedEncodingException {
        if (StrUtil.isNull(url)) {
            return url;
        }
        int pos = url.indexOf('?');
        if (pos > 0) {
            if (StrUtil.isNull(code)) {
                code = "UTF-8";
            }
            StringBuilder usb = new StringBuilder();
            usb.append(url.substring(0, pos)).append("?");
            url = url.substring(pos + 1);
            String[] params = url.split("&");
            for (int k = 0; k < params.length; k++) {
                String[] kv = params[k].split("=");
                if (kv.length == 2 && !StrUtil.isNull(kv[1])) {
                    usb.append(kv[0]).append("=");
                    usb.append(URLEncoder.encode(URLDecoder.decode(kv[1], "UTF-8"), "UTF-8"));
                    usb.append("&");
                }
            }
            StrUtil.deleteLastChar(usb);
            return usb.toString();
        }
        return url;
    }

    /**
     * 下载URL的文件
     * 
     * @param url
     * @param savePath
     * @param fileType
     * @return 返回保存的文件名
     * @throws JxException
     */
    public static String downloadFile(String url, String savePath, String fileType) throws JxException {
        if (StrUtil.isNull(url)) {
            return null;
        }
        int pos;
        // 得到文件后缀名，判断是否可以下载
        if (fileType != null && !"*".equals(fileType) && !"*.*".equals(fileType)) {
            pos = url.lastIndexOf('.');
            String suffix = url.substring(pos);
            if (fileType.indexOf(suffix) < 0) {
                throw new JxException(JxLangResourcesUtil.getString("urlUtil.downloadFile.fileType", new Object[] { suffix }));
            }
        }
        String saveFile = savePath;
        if (savePath == null || "".equals(saveFile)) {
            saveFile = "." + File.separator;
        } else if (!savePath.endsWith(File.separator)) {
            saveFile += File.separator;
        }
        // 得得要下载的文件名，方便保存
        pos = url.lastIndexOf('/');
        if (pos > 0) {
            saveFile += url.substring(pos + 1);
        } else {
            saveFile += url;
        }
        // 准备开始下载
        URL u = null;
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            u = new URL(url);
            URLConnection conn = u.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(saveFile);
            int byteread = 0;
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage() + "\r\n" + url, e);
        } finally {
            IOUtils.closeQuietly(fs);
            IOUtils.closeQuietly(inStream);
        }
        return FileUtil.formatPath(saveFile);
    }
}
