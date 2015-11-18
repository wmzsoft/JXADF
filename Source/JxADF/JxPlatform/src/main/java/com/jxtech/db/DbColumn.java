package com.jxtech.db;

/**
 * 定义字段名，主要是为了防止各数据库中的关键字不一样，导致字段名的问题
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public interface DbColumn {

    public String getSuffix();

    public String getColumn(String column);

    public String[] getKeys();
}
