package com.jxtech.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgExcute {
    private static final Logger LOG = LoggerFactory.getLogger(ImgExcute.class);

    /**
     * @param filePath         添加水印图片的照片
     * @param markContent      水印的名字
     * @param markContentColor 水印文件的颜色
     * @param qualNum          水印的质量
     * @param font
     * @return
     */
    public void createMark(String filePath, String markContent, Color markContentColor, float qualNum, Font font) {
        ImageIcon imgIcon = new ImageIcon(filePath);// filePath是图片路劲
        Image theImg = imgIcon.getImage();
        int width = theImg.getWidth(null);// 图片的宽度
        int height = theImg.getHeight(null);// 图片的高度
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();
        g.setFont(font);
        g.setColor(markContentColor);
        g.setBackground(Color.white);
        g.drawImage(theImg, 0, 0, null);
        g.drawString(markContent, width - (width - 20), height - (height - 20));
        g.dispose();
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            //在JDK7下面，不推荐似乎用sun的私有类，无法通过编译啊。。。 下同
            /*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
            param.setQuality(qualNum, true);
            encoder.encode(bimage, param);*/
            ImageIO.write(bimage, "jpeg", out);
            out.close();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

    }

    /**
     * @param ysPath  压缩图片保存地址
     * @param saveurl 上传图片的保存地址
     * @throws IOException ysPath
     */
    public void condense(String saveurl, String ysPath) throws IOException {
        // -----------------------上传完成，开始生成缩略图-------------------------
        java.io.File file = new java.io.File(saveurl); // 读入刚才上传的文件
        // String newurl = request.getRealPath("/") + url + filename + "_min." + ext; // 新的缩略图保存地址
        String newurl = ysPath;

        Image src = javax.imageio.ImageIO.read(file); // 构造Image对象
        float tagsize = 200;
        int old_w = src.getWidth(null); // 得到源图宽
        int old_h = src.getHeight(null);
        int new_w = 0;
        int new_h = 0; // 得到源图长

        float tempdouble;
        if (old_w > old_h) {
            tempdouble = old_w / tagsize;
        } else {
            tempdouble = old_h / tagsize;
        }
        new_w = Math.round(old_w / tempdouble);
        new_h = Math.round(old_h / tempdouble);// 计算新图长宽
        BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
        FileOutputStream newimage = new FileOutputStream(newurl); // 输出到文件流
        /*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
        encoder.encode(tag); // 近JPEG编码*/
        ImageIO.write(tag, "jpeg", newimage);
        newimage.close();
    }
}
