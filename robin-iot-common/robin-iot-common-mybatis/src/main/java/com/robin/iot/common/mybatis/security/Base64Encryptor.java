package com.robin.iot.common.mybatis.security;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base 64 加密解密器
 *
 * @author zhao peng
 * @date 2024/10/9 18:28
 **/
@Slf4j
public class Base64Encryptor implements DataEncryptor {

    @Override
    public String encrypt(String content) {
        try {
            return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("failed to encrypt content!", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String decrypt(String content) {
        try {
            byte[] bytes = Base64.getDecoder().decode(content);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("failed to decrypt content!", e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
