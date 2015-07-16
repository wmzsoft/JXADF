package com.jxtech.jbo.base;

import com.jxtech.tag.table.Table;

import java.io.Serializable;

/**
 * 处理MaxTable表
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxTable implements Serializable {

    private static final long serialVersionUID = -3847320139435698360L;
    private String tablename;// VARCHAR2(30 BYTE) not null,
    private int addrowstamp;// NUMBER not null,
    private String eaudittbname;// VARCHAR2(30 BYTE),
    private int isaudittable;// NUMBER not null,
    private int restoredata;// NUMBER not null,
    private String storagepartition;// VARCHAR2(30 BYTE),
    private int textsearchenabled;// NUMBER not null,
    private String langtablename;// VARCHAR2(30 BYTE),
    private String langcolumnname;// VARCHAR2(30 BYTE),
    private String uniquecolumnname;// VARCHAR2(30 BYTE),
    private int islangtable;// NUMBER not null,
    private long maxtableid;// NUMBER not null,
    private String altixname;// VARCHAR2(30 BYTE),
    private String trigroot;// VARCHAR2(29 BYTE) not null,
    private String contentattribute;// VARCHAR2(30 BYTE),
    private String rowstamp;// VARCHAR2(40 BYTE) not null

    private Table tableModle;

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public int getAddrowstamp() {
        return addrowstamp;
    }

    public void setAddrowstamp(int addrowstamp) {
        this.addrowstamp = addrowstamp;
    }

    public String getEaudittbname() {
        return eaudittbname;
    }

    public void setEaudittbname(String eaudittbname) {
        this.eaudittbname = eaudittbname;
    }

    public int getIsaudittable() {
        return isaudittable;
    }

    public void setIsaudittable(int isaudittable) {
        this.isaudittable = isaudittable;
    }

    public int getRestoredata() {
        return restoredata;
    }

    public void setRestoredata(int restoredata) {
        this.restoredata = restoredata;
    }

    public String getStoragepartition() {
        return storagepartition;
    }

    public void setStoragepartition(String storagepartition) {
        this.storagepartition = storagepartition;
    }

    public int getTextsearchenabled() {
        return textsearchenabled;
    }

    public void setTextsearchenabled(int textsearchenabled) {
        this.textsearchenabled = textsearchenabled;
    }

    public String getLangtablename() {
        return langtablename;
    }

    public void setLangtablename(String langtablename) {
        this.langtablename = langtablename;
    }

    public String getLangcolumnname() {
        return langcolumnname;
    }

    public void setLangcolumnname(String langcolumnname) {
        this.langcolumnname = langcolumnname;
    }

    public String getUniquecolumnname() {
        return uniquecolumnname;
    }

    public void setUniquecolumnname(String uniquecolumnname) {
        this.uniquecolumnname = uniquecolumnname;
    }

    public int getIslangtable() {
        return islangtable;
    }

    public void setIslangtable(int islangtable) {
        this.islangtable = islangtable;
    }

    public long getMaxtableid() {
        return maxtableid;
    }

    public void setMaxtableid(long maxtableid) {
        this.maxtableid = maxtableid;
    }

    public String getAltixname() {
        return altixname;
    }

    public void setAltixname(String altixname) {
        this.altixname = altixname;
    }

    public String getTrigroot() {
        return trigroot;
    }

    public void setTrigroot(String trigroot) {
        this.trigroot = trigroot;
    }

    public String getContentattribute() {
        return contentattribute;
    }

    public void setContentattribute(String contentattribute) {
        this.contentattribute = contentattribute;
    }

    public String getRowstamp() {
        return rowstamp;
    }

    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
    }

    public Table getTableModle() {
        return tableModle;
    }

    public void setTableModle(Table tableModle) {
        this.tableModle = tableModle;
    }
}
