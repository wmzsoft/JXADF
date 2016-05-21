package com.jxtech.app.attachment;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
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

/**
 * 平台基础表信息-附件内容-上传到数据库-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 * 
 */
public class FileupDBServlet extends HttpServlet {

    private static final long serialVersionUID = 6361325526423092342L;
    private static final Logger LOG = LoggerFactory.getLogger(FileupDBServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        String ownerid = request.getParameter("fromUid");
        String code = request.getParameter("code");
        String vfolder = request.getParameter("vfolder");
        ServletFileUpload fileUpload = new ServletFileUpload();
        StringBuilder json = new StringBuilder();
        json.append("{\"files\": [");
        try {
            fileUpload.setHeaderEncoding("UTF-8");
            FileItemIterator items = fileUpload.getItemIterator(request);
            AttachmentSetIFace acsi=null;
            try {
                acsi = (AttachmentSetIFace) JboUtil.getJboSet("TOP_ATTACHMENT");
            } catch (JxException e1) {
                LOG.error(e1.getMessage(),e1);
            }
            while (items.hasNext()) {
                FileItemStream item = items.next();
                if (!item.isFormField()) {
                    InputStream is = item.openStream();
                    String filename = item.getName();

                    String filetype = item.getContentType();
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
                        filesize = acsi.addAttachment(code, ownerid, filename, filetype, is, userinfo, vfolder);
                        if (filesize > 0) {
                            json.append("{\"name\":\"" + filename + "\",");
                            json.append("\"size\": " + filesize + "}");
                        }
                    } catch (JxException e) {
                        LOG.error(e.getMessage());
                    }
                }
            }
        } catch (FileUploadException e) {
            LOG.error(e.getMessage());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        json.append("]}");
        PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        writer.close();
    }

}
