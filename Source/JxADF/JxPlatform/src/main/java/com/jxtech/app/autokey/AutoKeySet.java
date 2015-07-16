package com.jxtech.app.autokey;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

import java.util.List;

/**
 * 
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */

public class AutoKeySet extends JboSet implements AutoKeySetIFace {

    /**
     * 
     */
    private static final long serialVersionUID = 3311405484526268655L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new AutoKey(this);
        return currentJbo;
    }

    /**
     * 生成自动增长编号
     * 
     * @param keyname
     * @param orgid
     * @param siteid
     * @return
     * @throws JxException
     */
    @Override
    public String generateKeyValue(String keyname, String orgid, String siteid) throws JxException {
        if (StrUtil.isNull(keyname)) {
            throw new JxException("没有指定 AutoKey Name,keyname=" + keyname + ",siteid=" + siteid + ",orgid=" + orgid);
        }
        DataQueryInfo dqi = getQueryInfo();
        dqi.putParams("autokeyname=upper(?)", keyname);
        dqi.putParams("orgid=upper(?)", orgid);
        dqi.putParams("siteid=upper(?)", siteid);
        List<JboIFace> list = query();
        if (list != null) {
            if (list.size() > 0) {
                JboIFace myjbo = list.get(0);
                long seed = myjbo.getLong("SEED");
                long newseed = seed + 1;
                myjbo.setObject("SEED", newseed);
                commit();
                String prefix = myjbo.getString("PREFIX");
                if (StrUtil.isNull(prefix)) {
                    return String.valueOf(newseed);
                } else {
                    return prefix + String.valueOf(newseed);
                }
            } else {
                throw new JxException("请在AutoKey表中配置正确的 AutoKey Name : " + keyname + ",siteid=" + siteid + ",orgid=" + orgid);
            }
        }
        throw new JxException("没有得到正确的值,keyname=" + keyname + ",siteid=" + siteid + ",orgid=" + orgid);
    }
}
