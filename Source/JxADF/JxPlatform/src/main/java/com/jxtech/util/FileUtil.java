package com.jxtech.util;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * 读入microsoft project文件 写出xml格式文件工具类
 */

/**
 * 文件处理工具类
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class FileUtil {
    private static Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获得文件的MD5码
     * 
     * @param is
     * @param md5
     * @return
     */
    public static long getMd5OfFile(InputStream is, StringBuilder md5) {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            digestInputStream = new DigestInputStream(is, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            long filesize = 0;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = digestInputStream.read(buffer)) > 0) {
                filesize += len;
            }
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            md5.append(byteArrayToHex(resultByteArray));
            return filesize;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return -1;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
        }

    }

    public static String getMd5OfFile(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteBuffer);
            in.close();
            return byteArrayToHex(messageDigest.digest());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            // if (n < byteArray.length - 1) {
            // hs = hs + "";
            // }
        }
        return hs.toUpperCase();
    }

    /**
     * 获得文件的字符集
     * 
     * @param file
     * @return
     */
    public static String getChartset(File file) {
        String code = "UTF-8";
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            is = new FileInputStream(file);
            isr = new InputStreamReader(is);
            code = isr.getEncoding();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(isr);
            IOUtils.closeQuietly(is);
        }
        if (StrUtil.isNull(code)) {
            code = "UTF-8";
        }
        return code;
    }

    /**
     * 复制单个文件
     * 
     * @param s
     * @param t
     * @return
     */
    public static boolean fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            /* 得到对应的文件通道 */
            in = fi.getChannel();
            /* 得到对应的文件通道 */
            out = fo.getChannel();
            /* 连接两个通道，并且从in通道读取，然后写入out通道 */
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 复制文件
     * 
     * @param sourceFilePath
     * @param toFilePath
     * @return
     */
    public static File fileCopy(String sourceFilePath, String toFilePath) {
        File oldFile = new File(sourceFilePath);
        String oldFileName = oldFile.getName();
        String oldFileType = oldFileName.substring(oldFileName.lastIndexOf("."));
        oldFileName = oldFileName.substring(0, oldFileName.lastIndexOf(".") - 1);
        String newFileName = oldFileName + System.currentTimeMillis() + oldFileType;
        int lastIndex = toFilePath.lastIndexOf(File.separator);
        String newFilePath = toFilePath.substring(0, lastIndex);
        File newFile = new File(newFilePath + File.separator + newFileName);
        try {
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
        } catch (IOException ie) {
            LOG.info("文件操作错误");
            ie.printStackTrace();
        }
        /* 复制文件 */
        boolean f = fileChannelCopy(oldFile, newFile);
        if (f) {
            return newFile;
        }
        return null;
    }

    public static String getFileSufix(String FilePath) {
        if (StrUtil.isNull(FilePath)) {
            return "";
        }
        return FilePath.substring(FilePath.lastIndexOf(".") + 1);
    }

    /**
     * 得到标准的路径
     * 
     * @param path
     * @return
     */
    public static String formatPath(String path) {
        if (StrUtil.isNull(path)) {
            return null;
        }
        if ("\\".equals(File.separator)) {
            return path.replaceAll("/", "\\\\");
        } else if ("/".equals(File.separator)) {
            return path.replaceAll("\\\\", "/");
        }
        return path;
    }

    /**
     * 将字符串写入文件中.
     * 
     * @param buf
     * @param filename
     * @return
     */
    public static boolean writeFile(String buf, String filename) {
        FileOutputStream fos = null;
        try {
            File f = new File(filename);
            File path = new File(f.getParent());
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);
            fos.write(buf.getBytes());
            return true;
        } catch (Exception ex) {
            LOG.info(ex.getMessage(), ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * 删除过期多少耗秒的文件
     * 
     * @param path
     * @param msecond
     */
    public static void deleteOldFile(String path, long msecond) {
        if (StrUtil.isNull(path) || msecond <= 0) {
            return;
        }
        File file = new File(path);
        long now = System.currentTimeMillis();
        if (file.isDirectory()) {
            // 删除过期的临时文件
            File[] files = file.listFiles();
            try {
                for (int i = 0; i < files.length; i++) {
                    // 超过1万秒的文件删除
                    if (now - files[i].lastModified() > msecond) {
                        files[i].delete();
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
