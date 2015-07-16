package com.jxtech.tag.img;

import com.jxtech.tag.comm.JxJboSetTag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参见：http://svn.jxtech.net:8081/display/TECH/img
 * 
 * @author wmzsosft@gmail.com
 * @date 2014.01
 * 
 */
public class ImgTag extends JxJboSetTag {

    /**
     * 
     */
    private static final long serialVersionUID = -3748328991450053623L;
    private String mystyle;
    private String imgBase;// 图片的基本路径
    private String imgSrc;// 图片地址属性
    private String imgHref;// 图片点击链接地址
    private String imgTitle;// 图片标题
    private String width;
    private String height;
    private String direction;
    private String mxEvent;// 图片事件

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        super.initValue(stack, request, response);
        return new Img(stack, request, response);
    }

    @Override
    protected void populateParams() {
        Img img = (Img) component;
        img.setImgBase(imgBase);
        img.setImgHref(imgHref);
        img.setImgSrc(imgSrc);
        img.setImgTitle(imgTitle);
        img.setWidth(width);
        img.setHeight(height);
        img.setDirection(direction);
        img.setMxEvent(mxEvent);
        super.populateParams();
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getImgBase() {
        return imgBase;
    }

    public void setImgBase(String imgBase) {
        this.imgBase = imgBase;
    }

    public String getMxEvent() {
        return mxEvent;
    }

    public void setMxEvent(String mxEvent) {
        this.mxEvent = mxEvent;
    }

}
