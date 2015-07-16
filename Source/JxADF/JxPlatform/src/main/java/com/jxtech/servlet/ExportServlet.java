package com.jxtech.servlet;

import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理导出数据
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 * 
 */
public class ExportServlet extends HttpServlet {

    private static final long serialVersionUID = 5262493875438450053L;
    private static final Logger LOG = LoggerFactory.getLogger(ExportServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String context = request.getParameter("context");
        if (StrUtil.isNull(context)) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.println("没有要导出的数据。");
            pw.close();
            LOG.info("没有要导出的数据。");
            return;
        }
        String filetype = request.getParameter("filetype");
        if (StrUtil.isNull(filetype)) {
            filetype = "excel";
        }
        String filename = request.getParameter("filename");
        if (StrUtil.isNull(filename)) {
            filename = "export";
        }
        if ("excel".equalsIgnoreCase(filetype)) {
            filename += ".xls";
        } else if ("word".equalsIgnoreCase(filetype)) {
            filename += ".doc";
        } else if ("pdf".equalsIgnoreCase(filetype)) {
            filename += ".pdf";
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        ServletOutputStream out = response.getOutputStream();
        out.write(context.getBytes());
        out.close();
    }
}
