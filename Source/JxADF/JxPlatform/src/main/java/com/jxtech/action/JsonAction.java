package com.jxtech.action;

import com.jxtech.common.JxActionSupport;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.NumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理JSON格式
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JsonAction extends JxActionSupport {

    private static final long serialVersionUID = -8495877223259256411L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonAction.class);
    private String jboname;
    private String cause;// 查询条件
    private String orderby;// 排序条件
    private String pageSize;// 页面大小
    private String pangeNum;// 当前第几页
    private String result;//查询到的结果信息

    public String execute() {
        return SUCCESS;
    }

    public String getJson() throws JxException {
        JboSetIFace jboset = JboUtil.getJboSet(jboname);
        DataQueryInfo dqi = jboset.getQueryInfo();
        dqi.setWhereCause(cause);
        dqi.setOrderby(orderby);
        dqi.setPageNum((int) NumUtil.parseLong(pangeNum, 1));
        dqi.setPageSize((int) NumUtil.parseLong(pageSize, 20));
        try {
            jboset.query();
            return jboset.toJson();
        } catch (JxException e) {
            LOG.warn(e.getMessage(), e);
        }
        return null;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPangeNum() {
        return pangeNum;
    }

    public void setPangeNum(String pangeNum) {
        this.pangeNum = pangeNum;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
