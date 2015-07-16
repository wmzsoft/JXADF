package com.jxtech.app.attachment;

import com.jxtech.app.attachment.handler.ImportFileHandler;
import com.jxtech.app.attachment.util.ImportFileHandlerFactory;
import com.jxtech.jbo.App;
import com.jxtech.jbo.AppSet;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
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
import java.util.List;
import java.util.Map;

public class ImportFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ImportFileServlet.class);

    public ImportFileServlet() {
        super();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return;
        }

        request.getRequestURI();
        String relationship = request.getParameter("relationship");
        // 暂时没有使用
        // String appNameType = request.getParameter("appNameType");

        ServletFileUpload fileUpload = new ServletFileUpload();
        StringBuffer json = new StringBuffer();
        json.append("{\"files\": [");
        try {
            fileUpload.setHeaderEncoding("UTF-8");
            FileItemIterator items = fileUpload.getItemIterator(request);
            while (items.hasNext()) {
                FileItemStream item = items.next();
                if (!item.isFormField()) {
                    InputStream in = item.openStream();
                    String filename = item.getName();
                    String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());// 判断文件类型

                    HttpSession session = request.getSession();
                    /*
                     * 当后面的JBOset无法获取用户的信息的时候，需要将此传到后面去
                     */
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
                        ImportFileHandler handler = ImportFileHandlerFactory.getImportFileHandler(fileType);
                        List<Map<Object, String>> importFileResult = handler.handle(in, fileType);
                        // LOG.debug("=======1===" + importFileResult);
                        String msg = "";
                        String errorMsg = "";

                        AppSet appSet = (AppSet) session.getAttribute(JxSession.APPS);
                        App app = null;
                        if (appSet != null)
                            app = appSet.getApp();
                        if (null == app) {
                            // app = appSet.getApp(appNameType);
                            return;
                        }
                        try {
                            msg = app.loadImportFile(importFileResult, relationship, userinfo);
                        } catch (JxException e) {
                            errorMsg = e.getMessage();
                        }

                        if (!"".equals(msg)) {
                            resulet = msg;
                            json.append("{\"success\":\"" + resulet + "\",");
                            json.append("\"name\":\"" + filename + "\",");
                            json.append("\"size\": " + filesize + "}");
                        } else {
                            resulet = errorMsg == "" ? "导入失败！" : errorMsg;
                            json.append("{\"error\":\"" + resulet + "\"}");
                        }
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    } finally {
                        in.close();
                    }
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e.getMessage());
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        json.append("]}");
        PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        writer.close();
    }

    @Override
    public void init() throws ServletException {
    }

}
