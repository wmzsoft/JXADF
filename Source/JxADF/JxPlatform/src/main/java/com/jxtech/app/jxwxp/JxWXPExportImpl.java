package com.jxtech.app.jxwxp;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.directwebremoting.io.FileTransfer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class JxWXPExportImpl implements JxWXPExport {

    @Override
    public FileTransfer exportExcel(String title, Map<String, String> headers,
                                    Collection<Map<String, Object>> datas, String pattern, Map<String, JxWXPValueAdapter> adapters) throws JxException {
        // 声明一个工作薄
        ByteArrayOutputStream bos = null;
        DataOutputStream dos = null;

        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        if (StrUtil.isNull(title)) {
            title = "NONAME";
        }
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 声明一个画图的顶级管理器
        // HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        //HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        //comment.setString(new HSSFRichTextString(""));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        //comment.setAuthor("leno");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        if (headers != null) {
            int i = 0;
            for (Map.Entry<String, String> e : headers.entrySet()) {
                HSSFCell cell = row.createCell(i++);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(e.getValue());
                cell.setCellValue(text);
            }
        } else {
            throw new JxException("没有设置导出列");
        }
        // 遍历集合数据，产生数据行
        Iterator<Map<String, Object>> it = datas.iterator();
        int index = 0;
        //遍历数据
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Map<String, Object> map = it.next();
            //map 遍历索引
            int i = 0;
            for (Map.Entry<String, String> e : headers.entrySet()) {
                String key = e.getKey();
                Object value = map.get(key);
                JxWXPValueAdapter adapter = null;
                if (adapters != null) {
                    adapter = adapters.get(key);
                }
                if (adapter != null) {
                    value = adapter.valueExcelAdapter(value);
                }
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                if (value instanceof Boolean) {
                    cell.setCellValue(this.booleanTypeConverter((Boolean) value));
                } else if (value instanceof Date) {
                    HSSFRichTextString richString = new HSSFRichTextString(this.dateTypeConverter((Date) value, pattern));
                    cell.setCellValue(richString);
                } else if (value instanceof Number) {
                    //是数字当作double处理
                    cell.setCellValue(this.numberTypeConverter((Number) value));
                } else {
                    // 其它数据类型都当作字符串简单处理
                    cell.setCellValue(this.defaultTypeConverter(value));
                }
                i++;
                //以上是按照表头逻辑处理代码
            }
        }
        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            workbook.write(dos);
            //String fileName = URLEncoder.encode(title, "UTF-8");
            String fileName = new String(title.getBytes("GB2312"),"ISO-8859-1");
            return new FileTransfer("\"" + fileName+".xls\"", "application/msexcel", bos.toByteArray());
        } catch (IOException e) {
            throw new JxException(e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.flush();
                    dos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String defaultTypeConverter(Object o) {
        String str = null;
        if (o != null) {
            str = o.toString();
        } else {
            str = "";
        }
        return str;
    }

    @Override
    public String booleanTypeConverter(Boolean b) {
        // TODO Auto-generated method stub
        if (b) {
            return "是";
        } else {
            return "否";
        }

    }


    @Override
    public String dateTypeConverter(Date date, String pattern) {
        // TODO Auto-generated method stub
        if (pattern == null) {
            pattern = "yyy-MM-dd HH:mm:ss";
        }
        return DateFormatUtils.format(date, pattern);
    }

    @Override
    public Double numberTypeConverter(Number n) {
        // TODO Auto-generated method stub
        return n.doubleValue();
    }


}













