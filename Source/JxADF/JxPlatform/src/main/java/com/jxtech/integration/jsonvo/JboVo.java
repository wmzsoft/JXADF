package com.jxtech.integration.jsonvo;

import java.util.List;
import java.util.Map;

/**
 * Created by cxm on 15/4/20
 */
public class JboVo {
    private String _relationshipname;//关系名称
    private String _action;// 操作
    private String _jboname;//jboname
    private String _uid; //唯一关键字
    private Map<String, Object> _datas;
    private List<JboVo> _childrens;

    public String get_relationshipname() {
        return _relationshipname;
    }

    public void set_relationshipname(String _relationshipname) {
        this._relationshipname = _relationshipname;
    }

    public String get_action() {
        return _action;
    }

    public void set_action(String _action) {
        this._action = _action;
    }

    public String get_jboname() {
        return _jboname;
    }

    public void set_jboname(String _jboname) {
        this._jboname = _jboname;
    }

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

    public Map<String, Object> get_datas() {
        return _datas;
    }

    public void set_datas(Map<String, Object> _datas) {
        this._datas = _datas;
    }

    public List<JboVo> get_childrens() {
        return _childrens;
    }

    public void set_childrens(List<JboVo> _childrens) {
        this._childrens = _childrens;
    }
}
