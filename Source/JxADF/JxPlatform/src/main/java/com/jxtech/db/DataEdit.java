package com.jxtech.db;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

import java.io.InputStream;
import java.sql.Connection;

/**
 * 数据持久化接口
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public interface DataEdit {

    /**
     * 执行SQL脚本
     * 
     * @param conn 数据库连接
     * @param msql SQL语句
     * @param columns 字段名
     * @param values 字段值
     * @return
     * @throws JxException
     */
    public int execute(Connection conn, String msql, Object[] columns, Object[] values) throws JxException;

    public boolean execute(Connection conn,String msql,Object[] params) throws JxException;
    
    /**
     * 更新数据
     * 
     * @param conn
     * @param jbo
     * @return
     * @throws JxException
     */
    public int update(Connection conn, JboIFace jbo) throws JxException;

    /**
     * 插入数据，执行insert语句
     * 
     * @param conn
     * @param jbo
     * @return
     */
    public int insert(Connection conn, JboIFace jbo) throws JxException;

    public long insertBlob(Connection conn, String tbName, String uidName, String uidValue, String blobName, InputStream inputs, StringBuffer md5) throws JxException ;
    
    /**
     * 保存数据
     * 
     * @param conn
     * @param jbo
     * @return
     */
    public boolean save(Connection conn, JboIFace jbo) throws JxException;

    public String getDataSourceName();

    public void setDataSourceName(String dataSourceName);
}
