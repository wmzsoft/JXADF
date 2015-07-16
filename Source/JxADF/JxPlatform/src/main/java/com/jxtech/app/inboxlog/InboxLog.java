package com.jxtech.app.inboxlog;

import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboxLog extends Jbo {

    private static final long serialVersionUID = 1203011774084075352L;
    private static final Logger LOG = LoggerFactory.getLogger(InboxLog.class);

    public InboxLog(JboSetIFace jboset) throws JxException {
        super(jboset);
    }
}
