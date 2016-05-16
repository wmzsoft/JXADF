package com.jxtech.app.attachment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * 文档下载--从数据库中下载
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 */
public class FiledownDBServlet extends HttpServlet {

    private static final long serialVersionUID = -6289673415945282219L;
    private static final Logger LOG = LoggerFactory.getLogger(FiledownDBServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        AttachmentSetIFace asi=null;
        try {
            asi = (AttachmentSetIFace) JboUtil.getJboSet("TOP_ATTACHMENT");
        } catch (JxException e1) {
            LOG.error(e1.getMessage(),e1);
        }
        StringBuilder msg = new StringBuilder();
        if (asi != null) {
            try {
                JboIFace jbo = asi.queryJbo(id);
                if (jbo != null) {
                    AttachmentContentSetIFace acsi = (AttachmentContentSetIFace) JboUtil.getJboSet("TOP_ATTACHMENT_CONTENT");
                    JboIFace ct = acsi.queryJbo(id);
                    String fileName = jbo.getString("file_name");
                    if (ct != null && ct.getData() != null) {
                        response.setContentType("application/x-msdownload");
                        response.setCharacterEncoding("UTF-8");
                        fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                        String agent = request.getHeader("USER-AGENT");
                        if (null != agent && -1 != agent.indexOf("MSIE")) {
                            fileName = URLEncoder.encode(fileName, "ISO8859-1");
                        }
                        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

                        OutputStream os = null;
                        try {
                            os = response.getOutputStream();
                            acsi.getBlob(id, os);
                            os.flush();
                            return;
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                            msg.append(e.getMessage() + "\r\nid=" + id);
                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                            } catch (Exception ex) {
                                LOG.error(ex.getMessage());
                            }
                        }
                    } else {
                        msg.append("没有对应的附件信息：" + fileName);
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

}
