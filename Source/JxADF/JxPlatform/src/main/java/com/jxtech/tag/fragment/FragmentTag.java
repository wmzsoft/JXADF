package com.jxtech.tag.fragment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.JxLoadResource;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.ELUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 页面片段，专用于包含页面的处理
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.7
 */
public class FragmentTag extends JxBaseUITag {

    private static final long serialVersionUID = 5962383366910336662L;
    private static final Logger LOG = LoggerFactory.getLogger(FragmentTag.class);
    protected String url;
    protected String app;
    protected String type;

    protected String lazyload; // 懒加载,需要手工条件触发加载
    protected String displayMode;// 子元素list-table的显示模式

    private HttpServletRequest req;
    private HttpServletResponse resp;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        req = request;
        resp = response;
        return new Fragment(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Fragment frag = (Fragment) component;
        try {
            App myapp = JxSession.getApp();
            if(myapp!=null){
                JboIFace jbi = myapp.getJbo();
                if(jbi!=null){
                    frag.setUrl(findString(ELUtil.parseJboElValue(jbi, url)));
                }else{
                    frag.setUrl(url);
                }
            }else{
                frag.setUrl(url);
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        //frag.setUrl(url);
        frag.setApp(app);
        frag.setType(type);
        frag.setLazyload(lazyload);
        frag.setDisplayMode(displayMode);
        JxLoadResource.loadTable(req);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLazyload() {
        return lazyload;
    }

    public void setLazyload(String lazyload) {
        this.lazyload = lazyload;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public String getDisplayMode() {
        return displayMode;
    }
}
