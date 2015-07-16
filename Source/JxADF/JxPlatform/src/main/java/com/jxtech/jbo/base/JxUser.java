package com.jxtech.jbo.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxUser implements Serializable {

    private static final long serialVersionUID = 9097820800597305152L;
    private String userid;// VARCHAR2(30 BYTE) not null,
    private String personid;// VARCHAR2(30 BYTE) not null,
    private String status;// VARCHAR2(12 BYTE) not null,
    private String type;// VARCHAR2(30 BYTE) not null,
    private String defsite;// VARCHAR2(8 BYTE),
    private long querywithsite;// NUMBER not null,
    private String defstoreroom;// VARCHAR2(12 BYTE),
    private String storeroomsite;// VARCHAR2(8 BYTE),
    private String pwhintquestion;// VARCHAR2(25 BYTE),
    private String pwhintanswer;// RAW(2000),
    private long forceexpiration;// NUMBER not null,
    private Date pwexpiration;// DATE,
    private long failedlogins;// NUMBER not null,
    private String databaseuserid;// VARCHAR2(18 BYTE),
    private String password;// RAW(128) not null,
    private String loginid;// VARCHAR2(50 BYTE) not null,
    private long maxuserid;// NUMBER not null,
    private String memo;// VARCHAR2(256 BYTE),
    private long sysuser;// NUMBER not null,
    private long inactivesites;// NUMBER not null,
    private long screenreader;// NUMBER not null,
    private String rowstamp;// VARCHAR2(40 BYTE) not null,
    private String defaultrepfacsiteid;// VARCHAR2(8 BYTE),
    private String defaultrepfac;// VARCHAR2(12 BYTE)

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefsite() {
        return defsite;
    }

    public void setDefsite(String defsite) {
        this.defsite = defsite;
    }

    public long getQuerywithsite() {
        return querywithsite;
    }

    public void setQuerywithsite(long querywithsite) {
        this.querywithsite = querywithsite;
    }

    public String getDefstoreroom() {
        return defstoreroom;
    }

    public void setDefstoreroom(String defstoreroom) {
        this.defstoreroom = defstoreroom;
    }

    public String getStoreroomsite() {
        return storeroomsite;
    }

    public void setStoreroomsite(String storeroomsite) {
        this.storeroomsite = storeroomsite;
    }

    public String getPwhintquestion() {
        return pwhintquestion;
    }

    public void setPwhintquestion(String pwhintquestion) {
        this.pwhintquestion = pwhintquestion;
    }

    public String getPwhintanswer() {
        return pwhintanswer;
    }

    public void setPwhintanswer(String pwhintanswer) {
        this.pwhintanswer = pwhintanswer;
    }

    public long getForceexpiration() {
        return forceexpiration;
    }

    public void setForceexpiration(long forceexpiration) {
        this.forceexpiration = forceexpiration;
    }

    public Date getPwexpiration() {
        return pwexpiration;
    }

    public void setPwexpiration(Date pwexpiration) {
        this.pwexpiration = pwexpiration;
    }

    public long getFailedlogins() {
        return failedlogins;
    }

    public void setFailedlogins(long failedlogins) {
        this.failedlogins = failedlogins;
    }

    public String getDatabaseuserid() {
        return databaseuserid;
    }

    public void setDatabaseuserid(String databaseuserid) {
        this.databaseuserid = databaseuserid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public long getMaxuserid() {
        return maxuserid;
    }

    public void setMaxuserid(long maxuserid) {
        this.maxuserid = maxuserid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getSysuser() {
        return sysuser;
    }

    public void setSysuser(long sysuser) {
        this.sysuser = sysuser;
    }

    public long getInactivesites() {
        return inactivesites;
    }

    public void setInactivesites(long inactivesites) {
        this.inactivesites = inactivesites;
    }

    public long getScreenreader() {
        return screenreader;
    }

    public void setScreenreader(long screenreader) {
        this.screenreader = screenreader;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }

    public String getDefaultrepfacsiteid() {
        return defaultrepfacsiteid;
    }

    public void setDefaultrepfacsiteid(String defaultrepfacsiteid) {
        this.defaultrepfacsiteid = defaultrepfacsiteid;
    }

    public String getDefaultrepfac() {
        return defaultrepfac;
    }

    public void setDefaultrepfac(String defaultrepfac) {
        this.defaultrepfac = defaultrepfac;
    }

}
