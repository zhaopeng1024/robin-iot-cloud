package com.robin.iot.common.mybatis.security;

/**
 * 数据加密接口
 *
 * @author zhao peng
 * @date 2024/10/9 18:27
 **/
public interface DataEncryptor {

    /**
     * 加密
     * @param content 原始数据
     * @return 加密数据
     */
    String encrypt(String content);


    /**
     * 解密
     * @param content 加密数据
     * @return 原始数据
     */
    String decrypt(String content);

}
