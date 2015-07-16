package com.jxtech.action;

import com.jxtech.common.JxActionSupport;
import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.util.DateUtil;
import org.apache.poi.util.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * CKEditor 上传图片的Action
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class CKEditorUploadImgAction extends JxActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_PATH = "/upload/ckeditor";
    private File upload; // 文件
    private String uploadContentType; // 文件类型
    private String uploadFileName; // 文件名
    private String message;
    private static final Logger LOG = LoggerFactory.getLogger(CKEditorUploadImgAction.class);

    /**
     * 图片上传
     * 
     * @return
     * @throws IOException
     */
    public String execute() throws Exception {
        StringBuffer sb = new StringBuffer();
        // 获得response,request
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();

        response.setCharacterEncoding("UTF-8");
        // CKEditor提交的很重要的一个参数
        String callback = request.getParameter("CKEditorFuncNum");
        //String expandedName; // 文件扩展名
        if (uploadContentType.equals("image/pjpeg") || uploadContentType.equals("image/jpeg")) {
            // IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg
            //expandedName = ".jpg";
        } else if (uploadContentType.equals("image/png") || uploadContentType.equals("image/x-png")) {
            // IE6上传的png图片的headimageContentType是"image/x-png"
            //expandedName = ".png";
        } else if (uploadContentType.equals("image/gif")) {
            //expandedName = ".gif";
        } else if (uploadContentType.equals("image/bmp")) {
            //expandedName = ".bmp";
        } else {
            sb.append("<script type=\"text/javascript\">");
            sb.append("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'','");
            sb.append(JxLangResourcesUtil.getString("ckeditor.file.format.info") + "');");
            sb.append("</script>");
            message = sb.toString();
            return SUCCESS;
        }
        if (upload.length() > 3000 * 1024) {
            sb.append("<script type=\"text/javascript\">");
            sb.append("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'','");
            sb.append(JxLangResourcesUtil.getString("ckeditor.file.maxsize.info") + "');");
            sb.append("</script>");
            message = sb.toString();
            return SUCCESS;
        }
        InputStream is = null;
        OutputStream os = null;
        String uploadPath = ServletActionContext.getServletContext().getRealPath(UPLOAD_PATH);
        String fileName = String.valueOf(DateUtil.now().getTime())+uploadFileName; // 采用时间+原文件名
        try {
            is = new FileInputStream(upload);
            // 图片上传路径
            File file = new File(uploadPath);
            if (!file.exists()) { // 如果路径不存在，创建
                file.mkdirs();
            }
            File toFile = new File(uploadPath, fileName);
            os = new FileOutputStream(toFile);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }

        // 返回"图像"选项卡并显示图片 request.getContextPath()为web项目名
        sb.append("<script type=\"text/javascript\">");
        sb.append("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'");
        sb.append(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
        sb.append(request.getContextPath() + UPLOAD_PATH + "/" + fileName + "','')");
        sb.append("</script>");
        message = sb.toString();
        return SUCCESS;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
