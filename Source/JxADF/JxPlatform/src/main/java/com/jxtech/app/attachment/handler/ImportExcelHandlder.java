package com.jxtech.app.attachment.handler;

import com.jxtech.jbo.util.JxConstant;
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
 */
public class ImportExcelHandlder implements ImportFileHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ImportExcelHandlder.class);

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

            if (wb != null) {
                Sheet sheet = null;
                int sheetNumber = wb.getNumberOfSheets();
                for (int i = 0; i < sheetNumber; i++) {
                    sheet = wb.getSheetAt(i);

                    if (sheet != null)
                        for (Row row : sheet) {
                            Map<Object, String> tempMap = new HashMap<Object, String>();
                            for (Cell cell : row) {
                                tempMap.put(Integer.valueOf(cell.getColumnIndex()), cell.toString());
                            }
                            rowList.add(tempMap);
                        }
                    // 添加分页标记
                    if (i < sheetNumber - 1) {
                        Map pageMap = new HashMap<Object, String>();
                        pageMap.put(JxConstant.SHEET_PAGE, String.valueOf(i));
                        rowList.add(pageMap);
                    }
                }

            }

        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        return rowList;
    }

}
