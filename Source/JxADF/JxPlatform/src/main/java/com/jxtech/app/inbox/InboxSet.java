package com.jxtech.app.inbox;

import com.jxtech.app.inboxlog.InboxLogSet;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboxSet extends JboSet {

    private static final long serialVersionUID = 6399268545462601488L;

    private static final Logger LOG = LoggerFactory.getLogger(InboxSet.class);

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Inbox(this);
        return currentJbo;
    }

    /**
     * 将任务分配编写到收件箱
     *
     * @param jbi              业务表
     * @param assignmentParams 任务分配信息
     */
    public void writeWfassignment(JboIFace jbi, Map<String, Object> assignmentParams) throws JxException {
        JxUserInfo userInfo = JxSession.getJxUserInfo();

        if (null != userInfo) {
            //写收件箱
            JboIFace inboxJbo = add();
            inboxJbo.setObject("WFID", assignmentParams.get("WFID"));
            inboxJbo.setObject("WFTYPE", assignmentParams.get("WFTYPE"));
            inboxJbo.setObject("DESCRIPTION", assignmentParams.get("DESCRIPTION"));
            inboxJbo.setObject("STATUS", assignmentParams.get("STATUS"));
            inboxJbo.setObject("APP", jbi.getJboSet().getAppname().toUpperCase());
            inboxJbo.setObject("OWNERID", jbi.getUidValue());
            inboxJbo.setObject("SENDER", userInfo.getUserid());
            inboxJbo.setObject("ASSIGNER", assignmentParams.get("ASSIGNER"));
            inboxJbo.setObject("STARTDATE", DateUtil.sqlDateTime());
            inboxJbo.setObject("DESCRIPTION", assignmentParams.get("DESCRIPTION"));
            inboxJbo.setObject("ACTIVE", 1);
            inboxJbo.setObject("ACTID", assignmentParams.get("ACTION"));
            Object memo = assignmentParams.get("NOTE");
            String memoStr = "";
            if (null != memo) {
                memoStr = memo.toString();
            }
            inboxJbo.setObject("MEMO", memoStr.length() > 190 ? memoStr.substring(0, 190) : memoStr);

            jbi.addNeedSaveList(inboxJbo);
        }
    }

    /**
     * 将收件箱相关的信息清空，并写入WFINBOXLOG表
     *
     * @param jbi    业务对象
     * @param params 信息表
     * @return
     */
    public void delAndwriteToLog(JboIFace jbi, String instanceId, Map<String, Object> params) throws JxException {
        JxUserInfo userInfo = JxSession.getJxUserInfo();

        DataQueryInfo dqInfo = new DataQueryInfo();
        dqInfo.setWhereCause("WFID = ?");
        dqInfo.setWhereParams(new Object[]{instanceId});
        setQueryInfo(dqInfo);
        List<JboIFace> inboxDelList = queryAll();

        LOG.debug("收件箱记录数：" + inboxDelList.size());
        if (!inboxDelList.isEmpty()) {
            InboxLogSet logJboSet = (InboxLogSet) JboUtil.getJboSet("WFINBOXLOG");

            for (JboIFace inboxDel : inboxDelList) {
                JboIFace logJbo = logJboSet.add();
                Map<String, Object> inboxData = inboxDel.getData();
                logJbo.getData().putAll(inboxData);

                if (logJbo.getString("ASSIGNER").equalsIgnoreCase(userInfo.getUserid())) {
                    String note = params.get("NOTE") == null ? "" : params.get("NOTE").toString();

                    logJbo.setString("MEMO", note.length() > 190 ? note.substring(0, 190) : note);
                } else {
                    logJbo.setString("MEMO", "");
                }
                logJbo.setObject("WFID", instanceId);

                Object logstepObj = params.get("LOGSTEP");
                int logstep;
                if (null != logstepObj) {
                    logstep = Integer.valueOf(logstepObj.toString());
                } else {
                    logstep = logJboSet.getNextStep(instanceId);
                }
                logJbo.setObject("LOGSTEP", logstep);
                logJbo.setString("LOGGER", userInfo.getLoginid());
                logJbo.setObject("LOGTIME", DateUtil.sqlDateTime());
                jbi.addNeedSaveList(logJbo);
                inboxDel.delete();
                jbi.addNeedSaveList(inboxDel);
            }
        }
    }

    public List<String> getCurrentActIds(String wfid) throws JxException {
        List<String> historyActs = new ArrayList<String>();
        DataQueryInfo dataQueryInfo = new DataQueryInfo();
        dataQueryInfo.setWhereCause("WFID = ?");
        dataQueryInfo.setWhereParams(new Object[]{wfid});

        List<JboIFace> logs = queryAll();

        for (JboIFace log : logs) {
            historyActs.add(log.getString("ACTID"));
        }

        return historyActs;
    }

}
