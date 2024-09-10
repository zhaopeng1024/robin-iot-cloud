package com.robin.iot.common.mybatis.util;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 租户上下文工具类
 *
 * @author zhao peng
 * @date 2024/9/10 22:37
 **/
public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 存储租户唯一标识
     * @param tenant 租户唯一标识
     */
    public static void save(String tenant) {
        TENANT_CONTEXT.set(tenant);
    }

    /**
     * 获取当前租户唯一标识
     * @return 当前租户唯一标识
     */
    public static String getCurrentTenant() {
        return TENANT_CONTEXT.get();
    }

    /**
     * 清空租户上下文
     */
    public static void clear() {
        TENANT_CONTEXT.remove();
    }

}
