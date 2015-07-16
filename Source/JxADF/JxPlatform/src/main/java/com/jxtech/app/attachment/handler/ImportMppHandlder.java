package com.jxtech.app.attachment.handler;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * xls格式
 * 
 * @author smellok
 * 
 */
public class ImportMppHandlder implements ImportFileHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ImportMppHandlder.class);

    @Override
    public List<Map<Object, String>> handle(InputStream in, String type) {
        List<Map<Object, String>> rowList = new LinkedList<Map<Object, String>>();
        try {
            Workbook wb = null;
            if ("xlsx".equals(type)) {
                wb = new XSSFWorkbook(in);
            } else if ("xls".equals(type)) {
                wb = new HSSFWorkbook(in);
            }

            Sheet sheet = null;
            if (wb != null)
                sheet = wb.getSheetAt(0);

            if (sheet != null)
                for (Row row : sheet) {
                    Map<Object, String> tempMap = new HashMap<Object, String>();
                    for (Cell cell : row) {
                        tempMap.put(Integer.valueOf(cell.getColumnIndex()), cell.toString());
                    }
                    rowList.add(tempMap);
                }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        return rowList;
    }

}
