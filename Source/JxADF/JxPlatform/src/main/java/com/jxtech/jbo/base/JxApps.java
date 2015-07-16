package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * Maxapps表的基本信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxApps implements Serializable {

    private static final long serialVersionUID = -7596263366259866965L;
    private String app;// VARCHAR2(10 BYTE) not null,
    private String description;// VARCHAR2(100 BYTE) not null,
    private String apptype;// VARCHAR2(5 BYTE) not null,
    private String restrictions;// VARCHAR2(254 BYTE),
    private String orderby;// VARCHAR2(254 BYTE),
    private String originalapp;// VARCHAR2(10 BYTE),
    private String custapptype;// VARCHAR2(18 BYTE),
    private String maintbname;// VARCHAR2(30 BYTE),
    private long maxappsid;// NUMBER not null,
    private String rowstamp;// VARCHAR2(40 BYTE) not null,
    private boolean ismobile;// NUMBER
    private String appurl;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getOriginalapp() {
        return originalapp;
    }

    public void setOriginalapp(String originalapp) {
        this.originalapp = originalapp;
    }

    public String getCustapptype() {
        return custapptype;
    }

    public void setCustapptype(String custapptype) {
        this.custapptype = custapptype;
    }

    public String getMaintbname() {
        return maintbname;
    }

    public void setMaintbname(String maintbname) {
        this.maintbname = maintbname;
    }

    public long getMaxappsid() {
        return maxappsid;
    }

    public void setMaxappsid(long maxappsid) {
        this.maxappsid = maxappsid;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }

    public boolean isIsmobile() {
        return ismobile;
    }

    public void setIsmobile(boolean ismobile) {
        this.ismobile = ismobile;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }
}
