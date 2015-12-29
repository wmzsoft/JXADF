package com.jxtech.db.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DbColumn;
import com.jxtech.db.util.JxDataSourceUtil;

import junit.framework.Assert;

public class DbColumnImplTest {

    @Test
    public void testGetSourceColumn() {
        DbColumn dbc = DBFactory.getDbColumn(JxDataSourceUtil.MYSQL);
        Assert.assertEquals("KEY", dbc.getSourceColumn("KEY1A"));
        Assert.assertEquals("KEYA1A",dbc.getSourceColumn("KEYA1A"));
    }

}
