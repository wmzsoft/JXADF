package com.jxtech.app.attachment.util;

import com.jxtech.app.attachment.handler.ImportExcelHandlder;
import com.jxtech.app.attachment.handler.ImportFileHandler;

public class ImportFileHandlerFactory {
    public static ImportFileHandler getImportFileHandler(String type) {
        ImportFileHandler handler = null;
        if ("xls".equals(type) || "xlsx".equals(type)) {
            handler = new ImportExcelHandlder();
        } else if ("mpp".equals(type)) {
            // todo : 还有其他需要支持的格式么？
        } else {
            // todo : 还有其他需要支持的格式么？
        }
        return handler;
    }
}
