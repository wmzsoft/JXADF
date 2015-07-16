package com.jxtech.tag.json;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.NumUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
public class JsonTag extends JxBaseUITag {

    private static final long serialVersionUID = 8044566844659869531L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonTag.class);
    protected String jboname;
    protected String relationship;
    protected String orderby;
    protected String apprestrictions;
    protected String pagesize;
    protected String startStr;// JSON开始字符串
    protected String endStr;// JSON结束字符串
    private List<JsonCol> jsonCol = new ArrayList<JsonCol>();
    private List<JsonHead> jsonHead = new ArrayList<JsonHead>();
    private List<JsonFoot> jsonFoot = new ArrayList<JsonFoot>();
    private JboSetIFace jboset;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json; charset=utf-8");
        return new Json(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Json json = (Json) component;
        json.setJboname(jboname);
        json.setOrderby(orderby);
        json.setApprestrictions(apprestrictions);
        json.setPagesize(pagesize);
        json.setJsonCol(jsonCol);
        json.setJsonHead(jsonHead);
        json.setJsonFoot(jsonFoot);
        json.setStartStr(startStr);
        json.setEndStr(endStr);
        if (StrUtil.isNull(jboname)) {
            return;
        }
        try {
            jboset = JboUtil.getJboSet(jboname);
            DataQueryInfo dqi = jboset.getQueryInfo();
            dqi.setWhereCause(apprestrictions);
            dqi.setOrderby(orderby);
            dqi.setPageSize((int) NumUtil.parseLong(pagesize, 20));
            jboset.query();
            json.setJboset(jboset);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<JsonCol> getJsonCol() {
        return jsonCol;
    }

    public void setJsonCol(List<JsonCol> jsonCol) {
        this.jsonCol = jsonCol;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getApprestrictions() {
        return apprestrictions;
    }

    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public List<JsonHead> getJsonHead() {
        return jsonHead;
    }

    public void setJsonHead(List<JsonHead> jsonHead) {
        this.jsonHead = jsonHead;
    }

    public List<JsonFoot> getJsonFoot() {
        return jsonFoot;
    }

    public void setJsonFoot(List<JsonFoot> jsonFoot) {
        this.jsonFoot = jsonFoot;
    }

    public String getStartStr() {
        return startStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

}
