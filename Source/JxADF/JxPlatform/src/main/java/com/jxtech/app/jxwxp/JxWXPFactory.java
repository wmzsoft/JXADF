package com.jxtech.app.jxwxp;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JxWXPFactory {

	private static final Logger LOG = LoggerFactory.getLogger(JxWXPFactory.class);

    // 实现变量管理的类
    private static String implClass = null;
	public static JxWXPExport getInstance(){
		 if (StrUtil.isNull(implClass)) {
	            return new JxWXPExportImpl();
	        }
	        Object obj = ClassUtil.getInstance(implClass);
	        if (obj != null && obj instanceof JxWXPExport) {
	            return (JxWXPExport) obj;
	        } else {
	            LOG.warn("配置的日志管理类" + implClass + "不能初始化。");
	        }
	        return null;
		
	}
    public static void setImplClass(String implClass) {
    	JxWXPFactory.implClass = implClass;
    }
	
}
