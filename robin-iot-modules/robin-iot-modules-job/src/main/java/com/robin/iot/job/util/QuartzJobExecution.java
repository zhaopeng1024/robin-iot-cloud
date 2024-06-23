package com.robin.iot.job.util;

import org.quartz.JobExecutionContext;

import com.robin.iot.job.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author zhaopeng
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
