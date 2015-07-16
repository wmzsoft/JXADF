package com.jxtech.app.inboxlog;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboxLogSet extends JboSet {

    private static final long serialVersionUID = 6399268545462601488L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new InboxLog(this);
        return currentJbo;
    }

    /**
     * 流程发送过程中的日志
     *
     * @param jbi
     * @param wfinfoParams
     * @throws JxException
     */
    public void writeRoutingProcessLog(JboIFace jbi, Map<String, Object> wfinfoParams) throws JxException {
        JxUserInfo userInfo = JxSession.getJxUserInfo();
        if (null != userInfo) {
            JboIFace inboxLog = add();
            inboxLog.setObject("DESCRIPTION", wfinfoParams.get("DESCRIPTION"));
            inboxLog.setObject("WFID", wfinfoParams.get("WFID"));
            inboxLog.setObject("WFTYPE", wfinfoParams.get("WFTYPE"));
            inboxLog.setObject("STATUS", wfinfoParams.get("STATUS"));
            inboxLog.setObject("APP", jbi.getJboSet().getAppname().toUpperCase());
            inboxLog.setObject("OWNERID", jbi.getUidValue());
            inboxLog.setObject("SENDER", userInfo.getUserid());
            inboxLog.setObject("ASSIGNER", userInfo.getUserid());
            inboxLog.setObject("STARTDATE", DateUtil.sqlDateTime());
            inboxLog.setObject("ENDDATE", DateUtil.sqlDateTime());
            inboxLog.setObject("ACTID", wfinfoParams.get("ACTID"));
            Object memo = wfinfoParams.get("NOTE");
            String memoStr = "";
            if (null != memo) {
                memoStr = memo.toString();
            }
            inboxLog.setObject("MEMO", memoStr.length() > 190 ? memoStr.substring(0, 190) : memoStr);
            inboxLog.setObject("LOGGER", userInfo.getUserid());
            inboxLog.setObject("LOGTIME", DateUtil.sqlDateTime());
            inboxLog.setObject("LOGSTEP", wfinfoParams.get("LOGSTEP"));
            jbi.addNeedSaveList(inboxLog);
        }
    }

    /**
     * 获取下一个的步骤序列
     *
     * @return
     */
    public int getNextStep(String instanceid) throws JxException {
        int result = 0;

        DataQueryInfo dqInfo = new DataQueryInfo();
        dqInfo.setWhereCause("upper(wfid) = upper(?)");
        dqInfo.setWhereParams(new Object[]{instanceid});
        setQueryInfo(dqInfo);
        List<JboIFace> jboList = queryAll();

        if (!jboList.isEmpty()) {
            for (JboIFace jbo : jboList) {
                Object logStep = jbo.getObject("LOGSTEP");
                if (null != logStep) {
                    int step = Integer.valueOf(jbo.getString("LOGSTEP"));
                    if (step > result) {
                        result = step;
                    }
                }
            }
        }

        return result + 1;
    }

    public List<String> getHistoryActIds(String wfid) throws JxException {
        List<String> historyActs = new ArrayList<String>();
        DataQueryInfo dataQueryInfo = new DataQueryInfo();
        dataQueryInfo.setWhereCause("WFID = ?");
        dataQueryInfo.setWhereParams(new Object[]{wfid});
        setQueryInfo(dataQueryInfo);
        List<JboIFace> logs = queryAll();

        for (JboIFace log : logs) {
            historyActs.add(log.getString("ACTID"));
        }

        return historyActs;
    }

}
