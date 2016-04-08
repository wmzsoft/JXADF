package com.jxtech.app.attachment;

import com.jxtech.common.BeanUtil;
import com.jxtech.common.ImgExcute;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import com.jxtech.util.FileUtil;
import com.jxtech.util.StrUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 平台基础表信息-附件内容-上传到文件-健新科技
 *
 * @author wmzsoft@gmail.com
 * @date 2013.10
 */
public class FileupServlet extends HttpServlet {
    private static final long serialVersionUID = -4495254089104401886L;
    private static final Logger LOG = LoggerFactory.getLogger(FileupServlet.class);
    private static String docpath = null;// 文档存放路径

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }

        String ownerid = request.getParameter("fromUid");
        String code = request.getParameter("code");
        String imgtype = request.getParameter("imgtype"); // 该参数用来判断是否执行形成水印和缩略图的
        String vfolder = request.getParameter("vfolder");

        HttpSession session = request.getSession();
        JxUserInfo userinfo = null;
        if (session != null) {
            Object obj = session.getAttribute(JxSession.USER_INFO);
            if (obj != null) {
                userinfo = (JxUserInfo) obj;
            }
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        String tmp = StrUtil.contact(docpath, File.separator, "temp", File.separator);
        File tf = new File(tmp);
        if (!tf.exists()) {
            if (!tf.mkdirs()) {
                LOG.error("创建临时目录失败。" + tmp);
                return;
            }
        }
        factory.setRepository(new File(tmp));// 设定临时目录
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        StringBuffer json = new StringBuffer();
        json.append("{\"files\": [");
        try {
            fileUpload.setHeaderEncoding("UTF-8");// 设置编码
            // fileUpload.setSizeMax(1024 * 1024);// 设定缓冲区为1M
            AttachmentSetIFace acsi = (AttachmentSetIFace) JboUtil.getJboSet("TOP_ATTACHMENT");
            List<?> items = fileUpload.parseRequest(request); // 解析request请求
            Iterator<?> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) { // 如果是表单域 ，就是非文件上传元素
                    String filetype = item.getContentType();
                    String filename = item.getName();// 短路径文件名，在IE7、8下为长文件名
                    int pos = filename.lastIndexOf("\\");
                    if (pos < 0) {
                        pos = filename.lastIndexOf("/");
                    }
                    if (pos > 0) {
                        filename = filename.substring(pos + 1);
                    }
                    long filesize = item.getSize(); // 文件的大小，以字节为单位
                    String filepath = StrUtil.contact(File.separator, code, File.separator);

                    String filepaths = filepath;

                    File fp = new File(docpath + filepath);
                    if (!fp.exists()) {
                        if (!fp.mkdirs()) {
                            LOG.error("创建目录失败，无法添加文件。" + docpath + filepath);
                            continue;
                        }
                    }
                    filepath = filepath + filename; // 上传文件的具体地址

                    File saveFile = new File(docpath + filepath); // 定义一个file指向一个具体的文件
                    String imgTime = "";
                    if (saveFile.exists()) {
                        // 文件已存在。
                        String now = DateUtil.dateTimeToString(new Date());
                        imgTime = now.replaceAll(":", "-");
                        pos = filepath.lastIndexOf(".");
                        if (pos > 0) {
                            filepath = filepath.substring(0, pos) + "-" + now.replaceAll(":", "-") + filepath.substring(pos);
                        }
                        saveFile = new File(docpath + filepath);
                    }
                    item.write(saveFile); // 把上传的内容写到一个文件中

                    if (StrUtil.contains(imgtype, "watermark") || StrUtil.contains(imgtype, "mini")) {
                        img(filetype, filename, imgTime, filepaths, filepath, userinfo);// 生成水印和缩略图的方法
                    }
                    String md5 = FileUtil.getMd5OfFile(saveFile);

                    filesize = acsi.addAttachment(code, ownerid, filename, filetype, filesize, md5, filepath, userinfo, vfolder);
                    if (filesize > 0) {
                        json.append("{\"name\":\"" + filename + "\",");
                        json.append("\"size\": " + filesize + "}");
                    }
                }
            }
        } catch (Exception e) {
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        docpath = BeanUtil.getJxParams().getDocpath();
    }

    /**
     * @param filetype
     *            图片类型
     * @param filename
     *            图片名称
     * @param imgTime
     *            图片时间(当文件重复在+时间)
     * @param filepaths
     * @param filepath
     *            原图最终上传的地址
     * @param userinfo
     *            userinfo对象，用来获取projectName用的
     * @throws IOException
     * @throws JxException
     */
    public void img(String filetype, String filename, String imgTime, String filepaths, String filepath, JxUserInfo userinfo) throws IOException, JxException {
        ImgExcute imgExcute = new ImgExcute();
        // 必须上传的是图片才能够进行压缩和水印
        int imgIndex = filetype.indexOf("/");
        String imgType = filetype.substring(0, imgIndex);// 获取上传文件类型

        int extindex = filename.lastIndexOf(".");
        String ext = filename.substring(extindex, filename.length());// 获取后缀名称

        int fnameIndex = filename.lastIndexOf(".");
        String fname = filename.substring(0, fnameIndex);// 获取文件名称,不要后缀名
        String ysName = "";
        String syName = "";

        if (imgType.equals("image")) {// 判断必须上传图片才能够压缩和水印
            if (!imgTime.equals("")) {
                ysName = "mini-" + fname + "-" + imgTime + ext;// 压缩名字
                syName = "watermark-" + fname + "-" + imgTime + ext;// 水印名字
            } else {
                ysName = "mini-" + fname + imgTime + ext;// 压缩名字
                syName = "watermark-" + fname + imgTime + ext;// 水印名字
            }

            String ysPath = docpath + filepaths + ysName;// 压缩图片的地址
            String syPath = docpath + filepaths + syName;// 水印图片的地址

            String url = docpath + filepath;
            imgExcute.condense(url, ysPath);// 图片压缩的方法
            FileInputStream input = null;
            FileOutputStream output = null;
            try {
                input = new FileInputStream(url);// 原图地址
                output = new FileOutputStream(syPath);// 可替换为任何路径何和文件名

                int in = input.read();
                while (in != -1) {
                    output.write(in);
                    in = input.read();
                }
                if (in == -1) {
                    output.flush();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
            String pname = "TEST";
            imgExcute.createMark(syPath, pname, Color.BLACK, 70f, new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 25));
        }
    }
}
