package com.bytter.cloud.auth.config;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 更改密码校验器
 *
 * @author mfs 2022-08-15
 * @since 1.0.0
 */
@Component
public class BytterPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence password) {
        return DigestUtil.md5Hex(password.toString());
    }
    /**
     * rawPassword 国密解密后的密码
     * encodePasswordAndSalt 数据库里查出来的密码
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodePasswordAndSalt) {
        int index = encodePasswordAndSalt.indexOf("|");
        if (index < 0) {
            return encodePasswordAndSalt.equals(DigestUtil.md5Hex(rawPassword.toString()));
        }
        String encodePassword = encodePasswordAndSalt.substring(0, index);
        String salt = encodePasswordAndSalt.substring(index + 1);
        return encodePassword.equals(DigestUtil.md5Hex(rawPassword.toString() + salt));
    }

}
