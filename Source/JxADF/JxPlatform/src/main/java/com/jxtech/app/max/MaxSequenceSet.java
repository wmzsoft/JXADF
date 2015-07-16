package com.jxtech.app.max;

import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * 平台基础表信息-健新科技
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.09
 * 
 */
public class MaxSequenceSet extends JboSet implements MaxSequenceSetIFace {

    private static final long serialVersionUID = -6160602986024989119L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxSequence(this);
        return currentJbo;
    }

    public String getSequeceName(String tbName, String name) throws JxException {
        return getSequeceName(tbName, name, "MAXSEQ");
    }

    public String getSequeceName(String tbName, String name, String defaultSeq) throws JxException {
        if (StrUtil.isNull(tbName)) {
            return defaultSeq;
        }
        DataQueryInfo qbe = getQueryInfo();
        if (StrUtil.isNull(name)) {
            qbe.setWhereCause("upper(TBName)=upper(?)");
            qbe.setWhereParams(new Object[] { tbName });
        } else {
            qbe.setWhereCause("upper(TBName)=upper(?) AND upper(name)=upper(?)");
            qbe.setWhereParams(new Object[] { tbName, name });
        }
        JboIFace jbo = getJboOfIndex(0, true);
        if (jbo != null) {
            return jbo.getString("sequencename");
        }
        return defaultSeq;
    }

    /**
     * 产生序列号
     * 
     * @param tbName 表名
     * @param name 字段名
     * @return 返回序列值
     */
    @Override
    public long generateNewSequence(String tbName, String name) throws JxException {
        return getSequenceValue(tbName, name, true);
    }

    /**
     * 产生序列号
     * 
     * @param tbName 表名
     * @param name 字段名
     * @return 返回序列值
     */
    public long getCurrentSequence(String tbName, String name) throws JxException {
        return getSequenceValue(tbName, name, false);
    }

    /**
     * 生产一个序列号
     * 
     * @param tbName
     * @param name
     * @param isNext
     * @return
     */
    public long getSequenceValue(String tbName, String name, boolean isNext) throws JxException {
        String sequenceName = getSequeceName(tbName, name);
        DataQuery dq = DBFactory.getDataQuery(this.getDbtype(), this.getDataSourceName());
        long val = dq.getSequence(sequenceName, isNext);
        if (val > 0) {
            return val;
        } else {
            val = dq.getSequence("MAXSEQ", isNext);
        }
        return val;
    }

}
