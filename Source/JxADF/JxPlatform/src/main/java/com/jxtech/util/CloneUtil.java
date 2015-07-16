package com.jxtech.util;

import java.io.*;

/**
 * 处理Clone的类
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.12
 * 
 */
public class CloneUtil {
    /**
     * 深Clone
     * 
     * @param obj 需要clone的对象
     * @return clone之后的对象
     */
    public static Object clone(Object obj) {
        Object anotherObj = null;
        byte[] bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes = baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    // ignore me
                }
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            anotherObj = ois.readObject();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    // ignore me
                }
            }
        }
        return anotherObj;
    }
}
