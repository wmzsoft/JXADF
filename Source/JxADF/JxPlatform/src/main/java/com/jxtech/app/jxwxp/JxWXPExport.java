package com.jxtech.app.jxwxp;

import com.jxtech.jbo.util.JxException;
import org.directwebremoting.io.FileTransfer;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
/**
 * 把数据导出成为 Word Excel pdf 文件的接口，目前只实现了 Excel导出
 * @author ThinkPad
 *
 */
public interface JxWXPExport {
	/**
	 * Map集合导出为Excel数据
	 * @param title 表格标题名
	 * @param headers 需要导出的列 key即所要导出的dataattribute value即为 Excel的表头
	 * @param datas 需要显示的数据集合 
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd HH:mm:ss"
	 * @return 返回dwr专用下载链接
	 */
	public FileTransfer exportExcel(String title, Map<String,String> headers,
            Collection<Map<String,Object>> datas, String pattern,Map<String,JxWXPValueAdapter> adapters) throws JxException;
	

	/**
	 * 类型转换 把object转换成String类型的
	 * @param o 对象
	 * @return string类型的值
	 */
	public String defaultTypeConverter(Object o);
	/**
	 * boolean 类型的值转换
	 * @param b
	 * @return
	 */
	public String booleanTypeConverter(Boolean b);
	

	/**
	 * 数字类型的值转换 因为cell只有 setCellValue(double value)
	 * @param b
	 * @return
	 */
	public Double numberTypeConverter(Number n);
	
	
	/**
	 * 日期类型格式化
	 * @param o 值
	 * @param pattern 转换成的日期格式
	 * @return
	 */
	public String dateTypeConverter(Date date,String pattern);
	
}











