package com.jxtech.util;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(DateUtilTest.class);

    @Test
    public void testStringToDate() {
        String d = DateUtil.dateToString(DateUtil.stringToDate("2015-01-01T00:00:00 00:00"));
        Assert.assertEquals("2015-01-01", d);
    }

    @Test
    public void testSqlFirtDayOfMonth() {
        java.sql.Timestamp t = DateUtil.sqlFirtDayOfMonth("2015-09-23 10:12:21");
        Assert.assertEquals("2015-09-01 00:00:00",DateUtil.dateTimeToString(t));
        t = DateUtil.sqlFirtDayOfMonth("2015-09");
        Assert.assertEquals("2015-09-01 00:00:00",DateUtil.dateTimeToString(t));
    }

    @Test
    public void testSqlLastDayOfMonth() {
        java.sql.Timestamp t = DateUtil.sqlLastDayOfMonth("2015-09-23 10:12:21");
        Assert.assertEquals("2015-09-30 23:59:59",DateUtil.dateTimeToString(t));
        t = DateUtil.sqlLastDayOfMonth("2015-08-01 00:00:00");
        Assert.assertEquals("2015-08-31 23:59:59",DateUtil.dateTimeToString(t));
        t = DateUtil.sqlLastDayOfMonth("2015-12-31 23:59:59");
        Assert.assertEquals("2015-12-31 23:59:59",DateUtil.dateTimeToString(t));
        t = DateUtil.sqlLastDayOfMonth("201512");
        Assert.assertEquals("2015-12-31 23:59:59",DateUtil.dateTimeToString(t));
    }
    
    //@Test
    public void testFormatDayHourMins(){
        String s = DateUtil.formatDayHourMins(10000);
        LOG.debug(s);
    }
}
