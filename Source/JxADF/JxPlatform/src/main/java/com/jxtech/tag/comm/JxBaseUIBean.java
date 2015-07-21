package com.jxtech.tag.comm;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.ClosingUIBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */

public class JxBaseUIBean extends ClosingUIBean {
    // private static final Logger LOG = LoggerFactory.getLogger(JxBaseUIBean.class);
    protected String readonly;
    protected String visible;
    private String request_query_string;
    private ResourceBundle tagBundle = JxLangResourcesUtil.getResourceBundle("res.tag");
    private String renderer;// 默认为HTML，可为bootstrap/
    private String rendererTag;// 渲染指定的Tag标签,其它的全部不渲染

    public JxBaseUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
        request_query_string = request.getQueryString();
        renderer = request.getParameter("renderer");
        rendererTag = request.getParameter("rendererTag");
        if (StrUtil.isNull(renderer)) {
            renderer = (String) request.getSession().getAttribute("renderer");
        } else {
            request.getSession().setAttribute("renderer", renderer);
        }
        if ("bootstrap".equalsIgnoreCase(renderer)) {
            // JxLoadResource.loadBootstrap(request);
        }
    }

    protected String getStdDefaultTemplate(String tagname, boolean isRenderer) {
        if (!isRendererTag(tagname)) {
            return "null";
        }
        if (isRenderer) {
            return tagname + "/" + getRenderer() + tagname + "-close";
        } else {
            return tagname + "/" + tagname + "-close";
        }
    }

    protected String getStdDefaultOpenTemplate(String tagname, boolean isRenderer) {
        if (!isRendererTag(tagname)) {
            return "null";
        }
        if (isRenderer) {
            return tagname + "/" + getRenderer() + tagname;
        } else {
            return tagname + "/" + tagname;
        }
    }

    /**
     * 是否渲染某个Tag
     * 
     * @param tagname
     * @return
     */
    public boolean isRendererTag(String tagname) {
        if (!StrUtil.isNull(rendererTag)) {
            String rt = "," + rendererTag + ",";
            if (rt.indexOf("," + tagname + ",") < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected String getDefaultTemplate() {
        return "base-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "base";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (id == null) {
            Random random = new Random();
            id = String.valueOf(random.nextLong());
            id = "rnd" + id.replaceAll("\\.", "").replaceAll("-", "");
            addParameter("id", id);
        }
        addParameter("request_query_string", request_query_string);
        addParameter("tagbundle", tagBundle);
        if (renderer != null) {
            addParameter("renderer", findString(renderer));
        }
    }

    /**
     * 标签属性国际化
     * 
     * @param attr 一种是从app目录下对应的模块取值，一种是去tag下面的值
     */
    public String getI18NValue(String attr) {
        String value = attr;
        if (null != attr) {
            int start = attr.indexOf('{');
            int end = attr.indexOf('}');
            if (start == 0 && end > 0) {
                String attrI18NKey = attr.substring(1, end);
                if (attrI18NKey.indexOf("app.") == 0) {
                    String[] keys = attrI18NKey.split("\\.");
                    int keysLen = keys.length;
                    if (keysLen >= 3) {
                        String resPackage = keys[1];
                        StringBuilder resKey = new StringBuilder();
                        for (int i = 2; i < keysLen; i++) {
                            resKey.append(keys[i]);
                            if (i < keysLen - 1) {
                                resKey.append('.');
                            }
                        }

                        ResourceBundle appBundle = JxLangResourcesUtil.getResourceBundle("res.app." + resPackage);
                        if (appBundle != null) {
                            value = appBundle.getString(resKey.toString());
                        }
                    }

                } else {
                    value = tagBundle.getString(attrI18NKey);
                }

            }
        }
        return value;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getRenderer() {
        if (StrUtil.isNull(renderer) || "html".equalsIgnoreCase(renderer)) {
            return "";
        } else {
            return renderer.toLowerCase() + "/";
        }
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRendererTag() {
        return rendererTag;
    }

    public void setRendererTag(String rendererTag) {
        this.rendererTag = rendererTag;
    }
}
