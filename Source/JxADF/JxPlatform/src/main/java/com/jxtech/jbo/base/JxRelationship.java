package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 处理MaxRelationship表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxRelationship implements Serializable {

    private static final long serialVersionUID = 4860903260055401149L;

    private String name;// VARCHAR2(50 BYTE) not null,
    private String parent;// VARCHAR2(30 BYTE) not null,
    private String child;// VARCHAR2(30 BYTE) not null,
    private String whereclause;// VARCHAR2(4000 BYTE),
    private String remarks;// VARCHAR2(4000 BYTE),
    private long maxrelationshipid;// NUMBER not null,
    private String rowstamp;// VARCHAR2(40 BYTE) not null,
    private String cardinality;// VARCHAR2(20 BYTE),
    private long dbjoinrequired;// NUMBER

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getWhereclause() {
        return whereclause;
    }

    public void setWhereclause(String whereclause) {
        this.whereclause = whereclause;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getMaxrelationshipid() {
        return maxrelationshipid;
    }

    public void setMaxrelationshipid(long maxrelationshipid) {
        this.maxrelationshipid = maxrelationshipid;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public long getDbjoinrequired() {
        return dbjoinrequired;
    }

    public void setDbjoinrequired(long dbjoinrequired) {
        this.dbjoinrequired = dbjoinrequired;
    }
}
