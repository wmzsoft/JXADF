<%@ page import="com.jxtech.i18n.JxLangResourcesUtil" %>
<!DOCTYPE HTML>
<%
    request.setCharacterEncoding("utf-8");
    String label = request.getParameter("label");
    String os = System.getProperties().getProperty("os.name");

    if (null != label && (os.startsWith("win") || os.startsWith("Win"))) {
        label = new String(request.getParameter("label").getBytes("ISO-8859-1"), "UTF-8");
    }
    request.setAttribute("label", label);
%>
<!--
<html lang="zh" manifest="./index.manifest" >
-->
<html>
<head>
    <!-- Force latest IE rendering engine or ChromeFrame if installed -->
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <meta charset="utf-8">
    <title><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_TITLE")%>
    </title>
    <meta name="description"
          content="File Upload widget with multiple file selection, drag&amp;drop support, progress bars, validation and preview images, audio and video for jQuery. Supports cross-domain, chunked and resumable file uploads and client-side image resizing. Works with any server-side platform (PHP, Python, Ruby on Rails, Java, Node.js, Go etc.) that supports standard HTML form file uploads.">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap styles -->
    <link rel="stylesheet" href="./css/bootstrap.min.css">
    <!-- Generic page styles -->
    <link rel="stylesheet" href="css/style.css">
    <!-- blueimp Gallery styles -->
    <link rel="stylesheet" href="./css/blueimp-gallery.min.css">
    <!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
    <link rel="stylesheet" href="css/jquery.fileupload-ui.css">
    <!-- CSS adjustments for browsers with JavaScript disabled -->
    <noscript>
        <link rel="stylesheet" href="css/jquery.fileupload-ui-noscript.css">
    </noscript>
    <link rel="stylesheet" href="css/jxrepair.css">
    <script type='text/javascript'>
        function backList(me, e) {
            var hf = '../app.action?fromUid=<%=request.getParameter("fromUid")%>&code=<%=request.getParameter("code")%>&readonly=<%=request.getParameter("readonly")%>&app=attachment&type=<%=request.getParameter("type")%>&fromid=<%=request.getParameter("fromUid")%>&imgtype=<%=request.getParameter("imgtype")%>&vfolder=<%=request.getParameter("vfolder")%>&isNeeded=<%=request.getParameter("isNeeded")%>&label=${label}';
            $(location).attr('href', hf);
        }
    </script>
</head>
<body>
<div class="container">
    <h3><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_TITLE")%>
    </h3>
    <!-- The file upload form used as target for the file upload widget -->
    <form id="fileupload"
          action="/jxweb/fileup/fileup?fromUid=<%=request.getParameter("fromUid")%>&code=<%=request.getParameter("code")%>&imgtype=<%=request.getParameter("imgtype")%>&vfolder=<%=request.getParameter("vfolder")%>"
          method="POST" enctype="multipart/form-data">
        <!-- Redirect browsers with JavaScript disabled to the origin page -->
        <noscript><input type="hidden" name="redirect" value="../fileup.action"></noscript>
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_ADDFILE")%></span>
                    <input type="file" name="files[]" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class=""></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_UPLOAD_ALL")%></span>
                </button>
                <button type="reset" class="btn btn-warning cancel">
                    <i class=""></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_CANCLE_ALL")%></span>
                </button>
                <button type="button" class="btn btn-danger back" onclick="backList(this,event)">
                    <i class=""></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_BACK")%></span>
                </button>
                <!-- <input type="checkbox" class="toggle"> -->
                <!-- The loading indicator is shown during file processing -->
                <span class="fileupload-loading"></span>
            </div>
            <!-- The global progress information -->
            <div class="col-lg-5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="progress-bar progress-bar-success" style="width:0;"></div>
                </div>
                <!-- The extended global progress information -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped">
            <tbody class="files"></tbody>
        </table>
    </form>
    <br>
</div>
<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <p class="size">{%=o.formatFileSize(file.size)%}</p>
            {% if (!o.files.error) { %}
                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
            {% } %}
        </td>
        <td>
            {% if (!o.files.error && !i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start">
                    <i class=""></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_UPLOAD")%></span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class=""></i>
                    <span><%=JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_CANCLE")%></span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}




</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class=""></i>
                    <span>删除</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
                <!-- <button class="btn btn-warning cancel">
                    <i class=""></i>
                    <span>取消1</span>
                </button> -->
            {% } %}
        </td>
    </tr>
{% } %}




</script>
<script src="../javascript/jquery-ui-1.10.3/jquery-1.9.1.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="js/vendor/jquery.ui.widget.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="js/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="js/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
<script src="js/bootstrap.min.js"></script>
<!-- blueimp Gallery script -->
<script src="js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="js/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="js/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="js/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="js/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="js/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="js/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="js/jquery.fileupload-validate.js"></script>
<!-- The File Upload user interface plugin -->
<script src="js/jquery.fileupload-ui.js"></script>
<!-- The main application script -->
<script src="js/main.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
</body>
</html>
