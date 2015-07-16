package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 处理MaxSequence表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxSequence implements Serializable {

    private static final long serialVersionUID = 1390320265865075377L;
    private String tbname;// VARCHAR2(30 BYTE) not null,
    private String name;// VARCHAR2(30 BYTE) not null,
    private long maxreserved;// NUMBER(15) not null,
    private long maxvalue;// NUMBER(15),
    private long range;// NUMBER(15),
    private String sequencename;// VARCHAR2(30 BYTE) not null,
    private long maxsequenceid;// NUMBER not null,
    private String rowstamp;// VARCHAR2(40 BYTE) not null

    public String getTbname() {
        return tbname;
    }

    public void setTbname(String tbname) {
        this.tbname = tbname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMaxreserved() {
        return maxreserved;
    }

    public void setMaxreserved(long maxreserved) {
        this.maxreserved = maxreserved;
    }

    public long getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(long maxvalue) {
        this.maxvalue = maxvalue;
    }

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public String getSequencename() {
        return sequencename;
    }

    public void setSequencename(String sequencename) {
        this.sequencename = sequencename;
    }

    public long getMaxsequenceid() {
        return maxsequenceid;
    }

    public void setMaxsequenceid(long maxsequenceid) {
        this.maxsequenceid = maxsequenceid;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }
}
