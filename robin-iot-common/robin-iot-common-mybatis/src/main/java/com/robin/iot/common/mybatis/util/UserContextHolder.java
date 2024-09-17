package com.robin.iot.common.mybatis.util;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 用户上下文
 *
 * @author zhao peng
 * @date 2024/9/18 1:05
 **/
public class UserContextHolder {

    private static final ThreadLocal<String> USER_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置用户
     * @param user 用户
     */
    public static void save(String user) {
        USER_CONTEXT.set(user);
    }

    /**
     * 获取当前用户
     * @return 当前用户
     */
    public static String getCurrentUser() {
        return USER_CONTEXT.get();
    }

    /**
     * 清空用户
     */
    public static void clear() {
        USER_CONTEXT.remove();
    }
}
