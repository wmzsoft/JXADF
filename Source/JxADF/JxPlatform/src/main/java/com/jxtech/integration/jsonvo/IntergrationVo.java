package com.jxtech.integration.jsonvo;

import java.util.List;

/**
 * Created by cxm on 15/4/20
 */
public class IntergrationVo {
    private String _appNameType;
    private String _jboname;
    private List<JboVo> _jbos;


    public String get_appNameType() {
        return _appNameType;
    }

    public void set_appNameType(String _appNameType) {
        this._appNameType = _appNameType;
    }

    public String get_jboname() {
        return _jboname;
    }

    public void set_jboname(String _jboname) {
        this._jboname = _jboname;
    }

    public List<JboVo> get_jbos() {
        return _jbos;
    }

    public void set_jbos(List<JboVo> _jbos) {
        this._jbos = _jbos;
    }
}
