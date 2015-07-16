package com.jxtech.workflow.option;

import java.util.ArrayList;
import java.util.List;

public class WftParam {

    private String key; // 如果有多个wftParam，回调的时候根据key来判断
    private String sectionTitle; // 标题
    private String jboname;// 对应maxapps的应用名称
    private List<WftParamAttr> attr = new ArrayList<WftParamAttr>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<WftParamAttr> getAttr() {
        return attr;
    }

    public void setAttr(List<WftParamAttr> attr) {
        this.attr = attr;
    }

}
