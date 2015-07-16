package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 每个对象（表）的基本信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxObject implements Serializable {

    private static final long serialVersionUID = -1474192953695268267L;

    private String objectname;// VARCHAR2(30 BYTE) not null,
    private String classname;// VARCHAR2(256 BYTE),
    private String description;// VARCHAR2(100 BYTE) not null,
    private boolean eauditenabled;// NUMBER not null,
    private String eauditfilter;// VARCHAR2(254 BYTE),
    private String entityname;// VARCHAR2(30 BYTE),
    private String esigfilter;// VARCHAR2(254 BYTE),
    private String extendsobject;// VARCHAR2(30 BYTE),
    private long imported;// NUMBER not null,
    private boolean isview;// NUMBER not null,
    private boolean persistent;// NUMBER not null,
    private String servicename;// VARCHAR2(18 BYTE) not null,
    private String siteorgtype;// VARCHAR2(18 BYTE) not null,
    private boolean userdefined;// NUMBER not null,
    private boolean mainobject;// NUMBER not null,
    private boolean internal;// NUMBER not null,
    private long maxobjectid;// NUMBER not null,
    private String rowstamp;// VARCHAR2(40 BYTE) not null,
    private String textdirection;// VARCHAR2(20 BYTE)

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEauditenabled() {
        return eauditenabled;
    }

    public void setEauditenabled(boolean eauditenabled) {
        this.eauditenabled = eauditenabled;
    }

    public String getEauditfilter() {
        return eauditfilter;
    }

    public void setEauditfilter(String eauditfilter) {
        this.eauditfilter = eauditfilter;
    }

    public String getEntityname() {
        return entityname;
    }

    public void setEntityname(String entityname) {
        this.entityname = entityname;
    }

    public String getEsigfilter() {
        return esigfilter;
    }

    public void setEsigfilter(String esigfilter) {
        this.esigfilter = esigfilter;
    }

    public String getExtendsobject() {
        return extendsobject;
    }

    public void setExtendsobject(String extendsobject) {
        this.extendsobject = extendsobject;
    }

    public long getImported() {
        return imported;
    }

    public void setImported(long imported) {
        this.imported = imported;
    }

    public boolean isIsview() {
        return isview;
    }

    public void setIsview(boolean isview) {
        this.isview = isview;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getSiteorgtype() {
        return siteorgtype;
    }

    public void setSiteorgtype(String siteorgtype) {
        this.siteorgtype = siteorgtype;
    }

    public boolean isUserdefined() {
        return userdefined;
    }

    public void setUserdefined(boolean userdefined) {
        this.userdefined = userdefined;
    }

    public boolean isMainobject() {
        return mainobject;
    }

    public void setMainobject(boolean mainobject) {
        this.mainobject = mainobject;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public long getMaxobjectid() {
        return maxobjectid;
    }

    public void setMaxobjectid(long maxobjectid) {
        this.maxobjectid = maxobjectid;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }

    public String getTextdirection() {
        return textdirection;
    }

    public void setTextdirection(String textdirection) {
        this.textdirection = textdirection;
    }

}
