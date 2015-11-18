package com.jxtech.db.impl.mysql;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public class DbColumnImpl extends com.jxtech.db.impl.DbColumnImpl {

    @Override
    public String getSuffix() {
        return "1A";
    }

    @Override
    public String[] getKeys() {
        return new String[] { "MAXVALUE", "SHOW", "RANGE", "KEY", "CONDITION" };
    }

}
