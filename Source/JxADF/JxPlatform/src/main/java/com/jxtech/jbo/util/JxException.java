package com.jxtech.jbo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(JxException.class);

    private static final long serialVersionUID = -8669543092633422588L;

    public JxException(String group, String key, Object[] params) {
        LOG.error(super.getMessage());
    }

    public JxException(String group, String key, Exception ex) {
        LOG.error("goup=" + group + ",key=" + key + "," + ex.getMessage());
    }

    public JxException(String group, String key) {
        LOG.error(super.getMessage());
    }

    public JxException(String message) {
        super(message);
        LOG.error(message);
        printStackTrace();
    }
}
