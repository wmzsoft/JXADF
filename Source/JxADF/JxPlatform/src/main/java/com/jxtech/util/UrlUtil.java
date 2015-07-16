package com.jxtech.util;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.util.JxException;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 处理URL获得的数据
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class UrlUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtil.class);

    public static Object getUrlContent(String url) {
        return getUrlContent(url, "UTF-8");
    }

    /**
     * 读取URL文件中的内容
     * 
     * @param url
     * @param urlCode
     * @return
     */
    public static Object getUrlContent(String url, String urlCode) {
        if (StrUtil.isNull(url)) {
            return url;
        }
        BufferedInputStream bis = null;
        Reader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
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
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(bis);
        }
        return sb.toString();
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
