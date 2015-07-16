package com.jxtech.tag.comm;

import com.jxtech.jbo.JboSetIFace;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 处理数据集的基类
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.01
 * 
 */
public class JxJboSet extends JxBaseUIBean {
    private JboSetIFace jboset;
    private String jboname;
    private String relationship;
    private String pagesize;
    private String count;// 记录数
    private String pagecount; // 每页大小
    private String pagenum; // 当前页号

    public JxJboSet(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    public void evaluateParams() {
        if (jboname != null) {
            addParameter("jboname", findString(jboname).toUpperCase());
        }
        if (relationship != null) {
            addParameter("relationship", findString(relationship).toUpperCase());
        }
        if (pagesize != null) {
            addParameter("pagesize", findValue(pagesize, Integer.class));
        }
        if (pagecount != null) {
            addParameter("pagecount", findValue(pagecount, Integer.class));
        }
        if (pagenum != null) {
            addParameter("pagenum", findValue(pagenum, Integer.class));
        }
        if (count != null) {
            addParameter("count", findValue(count, Integer.class));
        }
        if (jboset != null) {
            addParameter("jboset", jboset);
        }
        super.evaluateParams();
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

}
