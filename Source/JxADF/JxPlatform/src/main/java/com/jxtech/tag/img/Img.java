package com.jxtech.tag.img;

import com.jxtech.tag.comm.JxJboSet;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参见：http://svn.jxtech.net:8081/display/TECH/img
 * 
 * @author wmzsosft@gmail.com
 * @date 2014.01
 * 
 */
public class Img extends JxJboSet {

    private String mystyle;
    private String imgBase;// 图片的基本路径
    private String imgSrc;// 图片地址属性
    private String imgHref;// 图片点击链接地址
    private String imgTitle;// 图片标题

    private String width;
    private String height;
    private String direction;

    private String mxEvent;// 图片事件

    public Img(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "img-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "img";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle).toUpperCase());
        }
        if (imgBase != null) {
            addParameter("imgBase", findString(imgBase));
        }
        if (imgSrc != null) {
            addParameter("imgSrc", findString(imgSrc));
        }
        if (imgHref != null) {
            addParameter("imgHref", findString(imgHref));
        }
        if (imgTitle != null) {
            addParameter("imgTitle", findString(imgTitle));
        }
        if (width != null) {
            addParameter("width", findString(width));
        }
        if (height != null) {
            addParameter("height", findString(height));
        }
        if (direction != null) {
            addParameter("direction", findString(direction));
        }
        if (mxEvent != null) {
            addParameter("mxEvent", findString(mxEvent));
        }
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setImgBase(String imgBase) {
        this.imgBase = imgBase;
    }

    public void setMxEvent(String mxEvent) {
        this.mxEvent = mxEvent;
    }
}
