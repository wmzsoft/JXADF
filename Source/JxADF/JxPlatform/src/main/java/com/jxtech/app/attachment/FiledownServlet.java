package com.jxtech.app.attachment;

import com.jxtech.common.BeanUtil;
import com.jxtech.common.JxParams;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.FileUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 文档下载--从文件中下载
 *
 * @author wmzsoft@gmail.com
 * @date 2013.10
 */
public class FiledownServlet extends HttpServlet {

    private static final long serialVersionUID = -6545946418326829027L;
    private static final Logger LOG = LoggerFactory.getLogger(FiledownServlet.class);
    private static String docpath = null;// 文档存放路径

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String img = request.getParameter("imgtype");// 参数需要以 min或者watermark开头
        String type = request.getParameter("type");// 是下载还是直接显示缩略图
        AttachmentSetIFace acsi=null;
        try {
            acsi = (AttachmentSetIFace) JboUtil.getJboSet("TOP_ATTACHMENT");
        } catch (JxException e1) {
            LOG.error(e1.getMessage(),e1);
        }
        StringBuilder msg = new StringBuilder();
        if (acsi != null) {
            try {
                JboIFace jbo = acsi.queryJbo("attachment_id", id);
                if (jbo != null) {
                    String fileName = jbo.getString("file_name");
                    String filepath = docpath + jbo.getString("file_dir");
                    if (img != null && !"".equals(img)) { // 获取界面参数是否显示水印和缩略图
                        int dirIndex = filepath.lastIndexOf(File.separator);
                        String typeCode = jbo.getString("JOB_TYPE_CODE");
                        String dirName = filepath.substring(dirIndex + 1, filepath.length());
                        filepath = docpath + File.separator + typeCode + File.separator + img + "-" + dirName;
                    }
                    String filetype = jbo.getString("file_type");
                    if ("imglist".equalsIgnoreCase(type)) {
                        // 显示缩略图
                        JxParams ps = BeanUtil.getJxParams();
                        Map<String, String> ft = ps.getFileTypes();
                        if (ft != null) {
                            String filebase = ft.get("BASE");
                            String filefile = ft.get("FILE");
                            if (StrUtil.isNull(filetype)) {
                                // 文件类型为空
                                response.sendRedirect(filebase + filefile);
                                return;
                            } else if (ft.containsKey(filetype)) {
                                if (!StrUtil.isNull(ft.get(filetype))) {
                                    response.sendRedirect(filebase + ft.get(filetype));
                                    return;
                                }
                            } else {
                                response.sendRedirect(filebase + filefile);
                                return;
                            }
                        }
                    }

                    File f = new File(filepath);
                    if (f.exists()) {
                        // response.setContentType("application/x-msdownload");
                        // response.setContentType("application/octet-stream");

                        if (!StrUtil.isNull(filetype) && filetype.length() > 4) {
                            response.setContentType(filetype);
                        } else {
                            response.setContentType("application/x-msdownload");
                        }
                        String code = "UTF-8";
                        code = FileUtil.getChartset(f);
                        response.setCharacterEncoding(code);
                        // fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                        String agent = request.getHeader("USER-AGENT");
                        if (null != agent && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("rv:10") || -1 != agent.indexOf("rv:11"))) {
                            // fileName = URLEncoder.encode(fileName, "ISO8859-1");
                            fileName = URLEncoder.encode(fileName, "UTF-8");

                        } else {
                            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                        }
                        response.setHeader("Content-Length", jbo.getString("file_size"));
                        // response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                        response.addHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
                        ServletOutputStream out = response.getOutputStream();
                        BufferedInputStream bis = null;
                        BufferedOutputStream bos = null;
                        FileInputStream in = null;
                        try {
                            in = new FileInputStream(f);
                            bis = new BufferedInputStream(in);
                            bos = new BufferedOutputStream(out);
                            byte[] buff = new byte[2048];
                            int bytesRead;
                            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                                bos.write(buff, 0, bytesRead);
                            }
                            return;
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                        } finally {
                            if (bis != null) {
                                bis.close();
                            }
                            if (bos != null) {
                                bos.close();
                            }
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                        }
                    } else {
                        msg.append("没找到对应的文件：id=" + id + ",文件名：" + filepath);
                    }

                } else {
                    msg.append("没有对应的附件信息：" + id);
                }
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
        } else {
            msg.append("没有找到附件基本信息。");
        }
        LOG.error(msg.toString());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        pw.println(msg.toString());
        pw.close();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        docpath = BeanUtil.getJxParams().getDocpath();
    }

}
