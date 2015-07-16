package com.jxtech.app.attachment.handler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ImportFileHandler {
    /**
     * 根据输入流和不同的文件类型做出不同的处理
     * 
     * @param in
     * @param type
     * @return
     */
    public List<Map<Object, String>> handle(InputStream in, String type);
}
