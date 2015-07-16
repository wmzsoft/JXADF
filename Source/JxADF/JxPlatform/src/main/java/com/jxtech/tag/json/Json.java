package com.jxtech.tag.json;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

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
@StrutsTag(name = "json", tldTagClass = "com.jxtech.tag.json.JsonTag", description = "Json")
public class Json extends JxBaseUIBean {

    protected String jboname;
    protected String relationship;
    protected String orderby;
    protected String apprestrictions;
    protected String pagesize;
    protected String startStr;// JSON开始字符串
    protected String endStr;// JSON结束字符串
    private JboSetIFace jboset;
    private List<JsonCol> jsonCol = new ArrayList<JsonCol>();
    private List<JsonHead> jsonHead = new ArrayList<JsonHead>();
    private List<JsonFoot> jsonFoot = new ArrayList<JsonFoot>();

    public Json(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "json/json-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "json/json.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jboname != null) {
            this.addParameter("jboname", findString(jboname));
        }
        if (relationship != null) {
            this.addParameter("relationship", findString(relationship));
        }
        if (orderby != null) {
            this.addParameter("orderby", findString(orderby));
        }
        if (apprestrictions != null) {
            this.addParameter("apprestrictions", findString(apprestrictions));
        }
        if (pagesize != null) {
            this.addParameter("pagesize", findString(pagesize));
        }
        if (startStr != null) {
            this.addParameter("startStr", findString(startStr));
        }
        if (endStr != null) {
            this.addParameter("endStr", findString(endStr));
        }
        if (jboset != null) {
            addParameter("jboset", jboset);
        }
        if (jsonCol != null) {
            addParameter("jsonCol", jsonCol);
        }
        if (jsonHead != null) {
            addParameter("jsonHead", jsonHead);
        }
        if (jsonFoot != null) {
            addParameter("jsonFoot", jsonFoot);
        }
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setJsonCol(List<JsonCol> jsonCol) {
        this.jsonCol = jsonCol;
    }

    public void setJsonHead(List<JsonHead> jsonHead) {
        this.jsonHead = jsonHead;
    }

    public void setJsonFoot(List<JsonFoot> jsonFoot) {
        this.jsonFoot = jsonFoot;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

}
