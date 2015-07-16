package com.jxtech.util;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft
 * @date 2015.07
 * 
 */
public class SqlParserUtilTest {
    private static Logger LOG = LoggerFactory.getLogger(SqlParserUtilTest.class);

    @Test
    public void testGetColumns() {
        String msql = "select a,b,c,d from table";
        String[] cols = new String[] { "a", "b", "c", "d" };
        Assert.assertArrayEquals(cols, SqlParserUtil.getColumns(msql));
        msql = "insert into table(a,b,c,d) values('a',1,2,2)";
        Assert.assertArrayEquals(cols, SqlParserUtil.getColumns(msql));
    }

    @Test
    public void testGetColumnsOfInsert() {
        String msql = "insert into table(a,b,c,d) values('a',1,2,2)";
        String[] cols = new String[] { "a", "b", "c", "d" };
        Assert.assertArrayEquals(cols, SqlParserUtil.getColumnsOfInsert(msql));
        msql = "insert into table(a,b,c,d) select 'a',1,2,2 from tx";
        Assert.assertArrayEquals(cols, SqlParserUtil.getColumnsOfInsert(msql));
        msql = "insert into table(a,b,c,d) (select 'a',1,2,2 from tx)";
        Assert.assertArrayEquals(cols, SqlParserUtil.getColumnsOfInsert(msql));
        msql = "insert into table select 'a',1,2,2 from tx";
        Assert.assertArrayEquals(null, SqlParserUtil.getColumnsOfInsert(msql));
    }
    
    @Test
    public void testGetValues(){
        String msql = "insert into table(a,b,c,d) values('a'',b',1,2,2)";
        String[] vals = new String[]{"'a'',b'","1","2","2"};
        Assert.assertArrayEquals(vals, SqlParserUtil.getValues(msql));
        msql = "insert into table(a,b,c,d) (select 'a'',b',1,2,2 from tx)";
        Assert.assertArrayEquals(vals, SqlParserUtil.getValues(msql));
    }
    
    @Test
    public void testIsInsert() {
        Assert.assertFalse(SqlParserUtil.isInsert(null));
        Assert.assertFalse(SqlParserUtil.isInsert(""));
        String sql = "select * from table1";
        Assert.assertFalse(SqlParserUtil.isInsert(sql));
        sql = "insert Into t1(a,b,c) values('1',2,3)";
        Assert.assertTrue(SqlParserUtil.isInsert(sql));
        sql = "insert Into t1(a,b,c) select 1,2,3 from a";
        Assert.assertTrue(SqlParserUtil.isInsert(sql));
        sql = "insert Into t1(a,b,c) ";
        Assert.assertFalse(SqlParserUtil.isInsert(sql));

    }

    @Test
    public void testIsSelectSql() {
        String sql = "select * from table1";
        Assert.assertTrue(SqlParserUtil.isSelectSql(sql));
        sql = "select from table1";
        Assert.assertFalse(SqlParserUtil.isSelectSql(sql));
        sql = "a select from table1";
        Assert.assertFalse(SqlParserUtil.isSelectSql(sql));
        sql = "selectfrom table1";
        Assert.assertFalse(SqlParserUtil.isSelectSql(sql));
        sql = "selectafrom table1";
        Assert.assertFalse(SqlParserUtil.isSelectSql(sql));
        sql = "select a From table1";
        Assert.assertTrue(SqlParserUtil.isSelectSql(sql));
        Assert.assertFalse(SqlParserUtil.isSelectSql(null));
    }
}
