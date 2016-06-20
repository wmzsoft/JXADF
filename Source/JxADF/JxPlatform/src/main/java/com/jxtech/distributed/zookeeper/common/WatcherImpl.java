package com.jxtech.distributed.zookeeper.common;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2016.06
 *
 */
public class WatcherImpl implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(WatcherImpl.class);

    @Override
    public void process(WatchedEvent paramWatchedEvent) {
        LOG.debug("wather:" + paramWatchedEvent.getState().name());
    }

}
