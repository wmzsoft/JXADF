package com.jxtech.jbo.util;

import java.io.Serializable;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class BitFlag implements Serializable {

    private static final long serialVersionUID = -4921718747019825777L;
    long flags = 0L;

    public BitFlag() {
    }

    public BitFlag(long flags) {
        this.flags = flags;
    }

    public void setFlags(long flag) {
        this.flags = flag;
    }

    public long getFlags() {
        return this.flags;
    }

    public void setFlag(long flag, boolean state) {
        if (state) {
            this.flags |= flag;
        } else {
            flag = (flag + 1L) * -1L;
            this.flags &= flag;
        }
    }

    public boolean isFlagSet(long flag) {
        return (flag & this.flags) == flag;
    }

    public BitFlag or(BitFlag second) {
        return new BitFlag(getFlags() | second.getFlags());
    }
}
