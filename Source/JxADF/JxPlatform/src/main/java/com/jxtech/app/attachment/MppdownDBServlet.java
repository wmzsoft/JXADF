/**
 * 
 */
package com.jxtech.app.attachment;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.base.JxUserInfo;
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
 * @作者 刘展鹏
 * @日期 2014-2-21
 * @版本 V 1.0
 */
public class MppdownDBServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -6831794021693535102L;
    private static final Logger LOG = LoggerFactory.getLogger(MppdownDBServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pcode = request.getParameter("pcode");
        String id = request.getParameter("id");
        String prama = request.getParameter("prama");
        String fields = request.getParameter("fields");// 需要导入的字段
        String initVal = request.getParameter("initVal");
        String ccode = request.getParameter("ccode");
        String objectName = null;
        StringBuffer msg = new StringBuffer();
        if (true) {
            try {
                JboIFace pJbo = JboUtil.getJbo(pcode, "", id);
                if (pJbo != null) {
                    String fileName = pJbo.getString(prama);
                    response.setContentType("application/x-msdownload");
                    response.setCharacterEncoding("UTF-8");
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                    String agent = request.getHeader("USER-AGENT");
                    if (null != agent && -1 != agent.indexOf("MSIE")) {
                        fileName = URLEncoder.encode(fileName, "ISO8859-1");
                    }
                    fileName = fileName + ".xml";
                    response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                    ParseProjectMpp pm = new ParseProjectMpp();
                    OutputStream os = null;
                    try {
                        os = response.getOutputStream();

                        JxUserInfo userinfo = null;
                        pm.writeMppByOutStream(os, fields, ccode, userinfo, initVal);
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
                    msg.append("没有对应的附件信息：");
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
