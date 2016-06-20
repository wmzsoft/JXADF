package com.jxtech.distributed;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 删除分布式服务器中，过期的Session
 * 
 * @author wmzsoft@gmail.com
 *
 */
public class SessionJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Distributed dist = DistributedFactory.getDistributed();
        if (dist != null) {
            dist.removeTimeoutSession();
        }
    }

}
