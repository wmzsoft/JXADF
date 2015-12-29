package com.jxtech.db;

/**
 * 定义字段名，主要是为了防止各数据库中的关键字不一样，导致字段名的问题 通过DbFactory进行实例化
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public interface DbColumn {

    /**
     * 获得后缀名
     * 
     * @return
     */
    public String getSuffix();

    /**
     * 获得字段名+后缀名，为了避免关键字
     * 
     * @param column
     * @return
     */
    public String getColumn(String column);

    /**
     * 去掉后缀，直接获得原字段名
     * 
     * @param column
     * @return
     */
    public String getSourceColumn(String column);

    /**
     * 获得所有的关键字
     * 
     * @return
     */
    public String[] getKeys();
}
