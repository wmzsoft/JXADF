package com.jxtech.app.export.word;

import java.util.Map;

/**
 * 导出Word的接口
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public interface ExpWord {

    /**
     * 生成Word
     * 
     * @param xmlFileName
     *            Word的模板文件，转换为XML文件
     * @param docFileName
     *            生成的WORD文件名，带路径
     * @param data
     *            需要替换的数据
     * @return
     */
    public boolean createDocByFile(String xmlFileName, String docFileName, Map<String, Object> data);

    /**
     * 生成Word
     * 
     * @param templateContent
     *            模板的内容
     * @param docFileName
     *            生成的WORD文件名，带路径
     * @param data
     *            需要替换的数据
     * @return
     */
    public boolean createDocByTemplate(String templateContent, String docFileName, Map<String, Object> data);

    /**
     * 将Word文件转为PDF文件
     * 
     * @param docFileName
     * @param pdfFileName
     * @return
     */
    public boolean saveAsPDF(String docFileName, String pdfFileName);
}
