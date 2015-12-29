package com.jxtech.db.impl;

import com.jxtech.db.DbColumn;
import com.jxtech.util.StrUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public class DbColumnImpl implements DbColumn {

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public String getColumn(String column) {
        if (StrUtil.isNull(column)) {
            return null;
        }
        String[] keys = getKeys();
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (column.equalsIgnoreCase(keys[i])) {
                    return StrUtil.contact(column, getSuffix());
                }
            }
        }
        return column;
    }

    public String[] getKeys() {
        return null;
    }

    @Override
    public String getSourceColumn(String column) {
        if (StrUtil.isNull(column)) {
            return column;
        }
        String suf = this.getSuffix();
        if (StrUtil.isNull(suf)) {
            return column;
        }
        int pos = column.indexOf(suf);
        if (pos <= 0) {
            return column;
        }
        String sc = column.substring(0, pos);
        String[] keys = this.getKeys();
        if (keys == null) {
            return column;
        }
        for (int i = 0; i < keys.length; i++) {
            if (sc.equalsIgnoreCase(keys[i])) {
                return sc;
            }
        }
        return column;
    }
}
