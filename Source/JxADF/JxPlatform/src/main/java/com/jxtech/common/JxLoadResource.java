package com.jxtech.common;

import javax.servlet.http.HttpServletRequest;

import com.jxtech.util.StrUtil;

/**
 * 专门处理加载JS、CSS的代码
 * 
 * @author wmzsoft@gmail.com
 * 
 */
public class JxLoadResource {
    private static String ckedit = null;
    private static String jquerTimepicker = null;
    private static String fullCalendar = null;
    private static String moment = null;
    private static String code = null;
    private static String form = null;
    private static String tree = null;
    private static String bootstrap = null;
    private static String select2 = null;

    private JxLoadResource() {

    }

    public static void loadCKEditor(HttpServletRequest request) {
        if (ckedit == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/ckeditor/ckeditor.js'></script>\n");
            ckedit = sb.toString();
        }
        JxResource.putAppBody(request, "ckeditor", ckedit);
    }

    public static void loadJquerTimepicker(HttpServletRequest request) {
        if (jquerTimepicker == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/jquery-ui-1.10.3/datetime/jquery-ui-timepicker-addon.js'></script>\n");
            jquerTimepicker = sb.toString();
        }
        JxResource.putAppBody(request, "timepicker", jquerTimepicker);
    }

    public static void loadTable(HttpServletRequest request) {

    }

    public static void loadFullCalendar(HttpServletRequest request) {
        if (fullCalendar == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' href='");
            sb.append(path);
            sb.append("/javascript/fullcalendar/fullcalendar.min.css' />\n");
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/fullcalendar/fullcalendar.min.js'></script>\n");
            fullCalendar = sb.toString();
        }
        JxResource.putAppHead(request, "fullCalendar", fullCalendar);
        loadMoment(request);
    }

    public static void loadMoment(HttpServletRequest request) {
        if (moment == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/lib/moment.min.js'></script>\n");
            moment = sb.toString();
        }
        JxResource.putAppHead(request, "moment", moment);
    }

    public static void loadCode(HttpServletRequest request) {
        if (code == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/google-code-prettify/prettify.css'/>\n");
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/google-code-prettify/prettify.js'></script>\n");
            code = sb.toString();
        }
        JxResource.putAppBody(request, "code", code);
    }

    public static void loadForm(HttpServletRequest request, String langCode) {
        String path = request.getContextPath();
        if (form == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/formValidationEngine/css/validationEngine.jquery.css'/>\n");
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/formValidationEngine/css/template.css'/>\n");
            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/formValidationEngine/js/jquery.validationEngine.js'></script>\n");
            form = sb.toString();
        }
        JxResource.putAppBody(request, "form", form);
        String lang = "zh_CN";
        StringBuilder js = new StringBuilder();
        if (StrUtil.isNull(langCode) || langCode.indexOf("en") >= 0) {
            lang = "en";
        }
        js.append("<script type='text/javascript' src='");
        js.append(path);
        js.append("/javascript/formValidationEngine/js/languages/jquery.validationEngine-" + lang + ".js'></script>\n");
        JxResource.putAppBody(request, "form-lang", js.toString());
    }

    public static void loadTree(HttpServletRequest request) {
        if (tree == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/zTree/css/zTreeStyle/zTreeStyle.css'/>\n");

            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/zTree/js/jquery.ztree.all-3.5.min.js'></script>\n");
            tree = sb.toString();
        }
        JxResource.putAppBody(request, "tree", tree);
    }

    public static void loadBootstrap(HttpServletRequest request) {
        if (bootstrap == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/bootstrap/bootstrap/3.3.4/css/bootstrap.min.css'/>\n");

            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/bootstrap/bootstrap/3.3.4/js/bootstrap.min.js'></script>\n");
            bootstrap = sb.toString();
        }
        JxResource.putAppHead(request, "bootstrap", bootstrap);
    }

    public static void loadSelect2(HttpServletRequest request) {
        if (select2 == null) {
            String path = request.getContextPath();
            StringBuilder sb = new StringBuilder();
            sb.append("<link rel='stylesheet' type='text/css' href='");
            sb.append(path);
            sb.append("/javascript/select2/css/select2.min.css'/>\n");

            sb.append("<script type='text/javascript' src='");
            sb.append(path);
            sb.append("/javascript/select2/js/select2.full.min.js'></script>\n");
            select2 = sb.toString();
        }
        JxResource.putAppHead(request, "select2", select2);
    }

}
