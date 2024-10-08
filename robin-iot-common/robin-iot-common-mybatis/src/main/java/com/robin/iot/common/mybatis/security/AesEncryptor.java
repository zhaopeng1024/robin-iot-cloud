package com.robin.iot.common.mybatis.security;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.robin.iot.common.mybatis.autoconfigure.EnhancedMybatisProperties;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES 加密解密器
 *
 * @author zhao peng
 * @date 2024/10/9 18:39
 **/
@Slf4j
public class AesEncryptor implements DataEncryptor {

    private final EnhancedMybatisProperties properties;

    public AesEncryptor(EnhancedMybatisProperties properties) {
        this.properties = properties;
    }

    @Override
    public String encrypt(String content) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] valueByte = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(valueByte);
        } catch (Exception e) {
            log.error("failed to encrypt content:", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String decrypt(String content) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] originalData = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
            byte[] valueByte = cipher.doFinal(originalData);
            return new String(valueByte);
        } catch (Exception e) {
            log.error("failed to decrypt content:", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Cipher getCipher(int model) throws Exception {
        EnhancedMybatisProperties.DataEncrypt dataEncrypt = properties.getDataEncrypt();
        SecretKeySpec secretKey = new SecretKeySpec(dataEncrypt.getAesKey().getBytes(StandardCharsets.UTF_8), Constants.AES);
        byte[] encodedFormat = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(encodedFormat, Constants.AES);
        IvParameterSpec iv = new IvParameterSpec(dataEncrypt.getAesIv().getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(Constants.AES_CBC_CIPHER);
        cipher.init(model, secretKeySpec, iv);
        return cipher;
    }

}
