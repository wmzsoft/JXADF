package com.jxtech.jbo.util;

import java.io.Serializable;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public abstract class JxType implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    public static final int ALN = 0;
    public static final int UPPER = 1;
    public static final int LOWER = 2;
    public static final int DATE = 3;
    public static final int DATETIME = 4;
    public static final int TIME = 5;
    public static final int INTEGER = 6;
    public static final int SMALLINT = 7;
    public static final int FLOAT = 8;
    public static final int DECIMAL = 9;
    public static final int DURATION = 10;
    public static final int AMOUNT = 11;
    public static final int YORN = 12;
    public static final int GL = 13;
    public static final int LONGALN = 14;
    public static final int CRYPTO = 15;
    public static final int CRYPTOX = 16;
    public static final int CLOB = 17;
    public static final int BLOB = 18;
    public static final int BIGINT = 19;
    public static final int UDTYPE = 99;
    public static final int DEFAULMAXLENGTH = -1;
    public static final int DEFAULTSCALE = 2;
    boolean isNull = true;

    public void setMaxLength(int l) {
    }

    public int getMaxLength() {
        return -1;
    }

    public int getScale() {
        return 0;
    }

    public void setScale(int s) throws JxException {
    }
}
