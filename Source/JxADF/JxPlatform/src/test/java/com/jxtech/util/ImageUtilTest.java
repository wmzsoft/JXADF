package com.jxtech.util;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import junit.framework.Assert;

import org.junit.Test;

public class ImageUtilTest {

    //@Test
    public void testGenImageByUrl() {
        String myurl = "http://127.0.0.1/jxweb/leave/index_main.action?uid=117&type=login&username=admin&password=123456";
        try {
            JEditorPane ed = new JEditorPane(new URL(myurl));
            System.out.println("10");
            Thread.sleep(10000);
            ed.setSize(1000, 1000);

            // create a new image
            BufferedImage image = new BufferedImage(ed.getWidth(), ed.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // paint the editor onto the image
            SwingUtilities.paintComponent(image.createGraphics(), ed, new JPanel(), 0, 0, image.getWidth(), image.getHeight());
            // save the image to file
            ImageIO.write((RenderedImage) image, "png", new File("d:\\html.png"));
            System.out.println("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
