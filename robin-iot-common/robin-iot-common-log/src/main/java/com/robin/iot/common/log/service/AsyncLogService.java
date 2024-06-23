package com.robin.iot.common.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.robin.iot.common.core.constant.SecurityConstants;
import com.robin.iot.system.api.RemoteLogService;
import com.robin.iot.system.api.domain.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * @author zhaopeng
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception
    {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
