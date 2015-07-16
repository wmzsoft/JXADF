/**
 * 
 */
package com.jxtech.app.attachment;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import net.sf.mpxj.MPXJException;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @作者 刘展鹏
 * @日期 2014-2-13
 * @版本 V 1.0
 */
public class MppupDBServlet extends HttpServlet {

    private static final long serialVersionUID = 6361325526423092342L;
    private static final Logger LOG = LoggerFactory.getLogger(FileupDBServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        request.getRequestURI();
        String code = request.getParameter("code");
        String fields = request.getParameter("fields");// 需要导入的字段
        String initVal = request.getParameter("initVal");
        ServletFileUpload fileUpload = new ServletFileUpload();
        StringBuffer json = new StringBuffer();
        json.append("{\"files\": [");
        try {
            fileUpload.setHeaderEncoding("UTF-8");
            FileItemIterator items = fileUpload.getItemIterator(request);
            while (items.hasNext()) {
                FileItemStream item = items.next();
                if (!item.isFormField()) {
                    InputStream is = item.openStream();
                    String filename = item.getName();
                    int mppindex = filename.lastIndexOf(".mpp");// 判断文件类型
                    HttpSession session = request.getSession();
                    JxUserInfo userinfo = null;
                    if (session != null) {
                        Object obj = session.getAttribute(JxSession.USER_INFO);
                        if (obj != null) {
                            userinfo = (JxUserInfo) obj;
                        }
                    }
                    long filesize = 0;
                    try {
                        String resulet = "";
                        if (mppindex > 0) {
                            ParseProjectMpp pm = new ParseProjectMpp();
                            filesize = pm.readMppByInputStream(is, fields, code, userinfo, initVal);
                            resulet = "导入成功";
                            if (filesize > 0) {
                                json.append("{\"success\":\"" + resulet + "\",");
                                json.append("\"name\":\"" + filename + "\",");
                                json.append("\"size\": " + filesize + "}");
                            }
                        } else {
                            resulet = "不是.mpp文件，无法导入数据";
                            json.append("{\"error\":\"" + resulet + "\"}");
                        }
                    } catch (JxException e) {
                        LOG.error(e.getMessage());
                    } catch (MPXJException e) {
                        LOG.error(e.getMessage());
                    } finally {
                        is.close();
                    }
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e.getMessage());
        }
        // response.setContentType("application/json");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        json.append("]}");
        PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        writer.close();
    }

}
