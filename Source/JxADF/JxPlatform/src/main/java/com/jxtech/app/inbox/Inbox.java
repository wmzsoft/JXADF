package com.jxtech.app.inbox;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inbox extends Jbo {

    private static final long serialVersionUID = 1203011774084075352L;
    private static final Logger LOG = LoggerFactory.getLogger(Jbo.class);

    public Inbox(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    /**
     * 加载完后需要将状态转成中文
     *
     * @throws JxException
     */
    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        String appid = getString("OWNERID");

        Object tbName = getObjectOfReleationship("WFINBOXAPP.MAINTBNAME", JxConstant.READ_CACHE);
        if (null != tbName) {
            Jbo jbo = (Jbo)JboUtil.getJbo(tbName.toString(), "", appid);
            if (null != jbo) {
                Object statusVal = jbo.getObjectOfReleationship("WFSTATUS.DESCRIPTION", JxConstant.READ_CACHE);
                if (null != statusVal) {
                    setString("STATUSDESC", statusVal.toString());
                }

                setString("WFT_INSTANCEID", jbo.getString("WFT_INSTANCEID"));
            }
        }

    }
}
