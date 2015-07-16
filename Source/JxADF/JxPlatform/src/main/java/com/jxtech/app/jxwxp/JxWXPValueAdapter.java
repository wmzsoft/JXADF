package com.jxtech.app.jxwxp;

public interface JxWXPValueAdapter {

	/**
	 * 导出到Excel的时候 值适配器<br/>
	 * 使用场景:比如 0和1 代表同意 不同意或者通过与不通过 这种业务需求定制
	 * @param value 对象的值
	 * @return 返回需要展示的String类型的值
	 */
	public String valueExcelAdapter(Object value);
	
	
}
