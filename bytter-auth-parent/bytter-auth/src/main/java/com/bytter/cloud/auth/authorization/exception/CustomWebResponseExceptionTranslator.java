package com.bytter.cloud.auth.authorization.exception;

import com.bytter.cloud.auth.cache.LoginLockCache;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.event.BytterLoginException;
import com.bytter.cloud.auth.event.BytterLoginFailureEvent;
import com.bytter.cloud.common.core.exception.ErrorType;
import com.bytter.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import java.util.Objects;

/**
 * 自定义web错误转换器
 *
 * @author zhoul 2021-1-19
 * @since 1.0.0
 */
@Slf4j
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LoginLockCache loginLockCache;

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        log.error("oauth2 exception:", e);
        String msg = e.getMessage();
        ErrorType authErrorType = null;
        if (ErrorConstant.AUTH_TENANT_NOT_EXIST_MSG.equals(msg)) {
            // 租户不存在
            authErrorType = AuthMessageEnum.AUTH_TENANT_DOES_NOT_EXIST;
        } else if (ErrorConstant.AUTH_TENANT_DISABLED_MSG.equals(msg)) {
            // 租户已被禁用
            authErrorType = AuthMessageEnum.AUTH_TENANT_DISABLED;
        } else if (ErrorConstant.AUTH_TENANT_INVALID_MSG.equals(msg)) {
            // 租户无效
            authErrorType = AuthMessageEnum.AUTH_TENANT_INVALID;
        } else if (ErrorConstant.AUTH_TENANT_EXPIRED_MSG.equals(msg)) {
            // 租户已到期
            authErrorType = AuthMessageEnum.AUTH_TENANT_EXPIRED;
        } else if (ErrorConstant.AUTH_ERROR_USER_NOT_BINDING_CERT.equals(msg)) {
            // 用户未绑定证书
            authErrorType = AuthMessageEnum.AUTH_USER_NOT_BINDING_CERT;
        } else if (ErrorConstant.AUTH_ERROR_USER_LOCKED.equals(msg)) {
            //用户已经被锁定
            authErrorType = AuthMessageEnum.AUTH_USER_LOCKED;
        } else if (ErrorConstant.AUTH_ERROR_CERT_INVALID.equals(msg)) {
            // 证书无效
            authErrorType = AuthMessageEnum.AUTH_ERROR_CERT_INVALID;
        } else if (ErrorConstant.AUTH_ERROR_CA_CERT_NUMBER.equals(msg)) {
            // 用户证书错误
            authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_NUMBER;
        } else if (ErrorConstant.AUTH_TENANT_CODE_NOT_EXIST_MSG.equals(msg)) {
            // 集团号不正确
            authErrorType = AuthMessageEnum.AUTH_TENANTCODE_NOTEXIST;
        } else if (ErrorConstant.AUTH_BAD_CREDENTIALS_MSG.equals(msg)) {
            // 密码错误
            authErrorType = AuthMessageEnum.AUTH_BAD_CREDENTIAL;
        } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE.equals(msg)) {
            // 登陆失败，请您选择正确的登陆方式
            authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_WRONG;
        } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD.equals(msg)) {
            // 登陆失败，请您选择正确的登陆方式
            authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_PASSOWRD;
        } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD_CERT.equals(msg)) {
            // 登陆失败，请您选择正确的登陆方式
            authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_PASSOWRD_CERT;
        } else if (ErrorConstant.AUTH_ERROR_TOKEN_INVALID.equals(msg)) {
            // 无效token
            authErrorType = AuthMessageEnum.AUTH_INVALID_TOKEN;
        } else if (ErrorConstant.AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE.equals(msg)) {
            // 用户证书未生效
            authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE;
        } else if (ErrorConstant.AUTH_ERROR_CA_CERT_ENDDATE_AFTER.equals(msg)) {
            // 用户证书已过期
            authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_ENDDATE_AFTER;
        } else if (ErrorConstant.AUTH_ERROR_CA_CERT_DATE_ERROR.equals(msg)) {
            // 用户证书日期错误
            authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_DATE_ERROR;
        } else if (ErrorConstant.AUTH_TENANT_PHONE_NOF_FOUND.equals(msg)) {
            // 手机未绑定
            authErrorType = AuthMessageEnum.AUTH_ERROR_PHONE_UNBIND;
        }
        //登陆失败消息提示
        if (e instanceof BytterLoginException) {
            applicationEventPublisher.publishEvent(new BytterLoginFailureEvent((BytterLoginException) e));
            return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(e.getMessage(), e));
        }
        if (null != authErrorType) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(authErrorType, e));
        }
        if (e instanceof CustomOauthException) {
            return ResponseEntity.status(HttpStatus.OK).body((CustomOauthException) e);
        }
        if (e instanceof OAuth2Exception) {
            log.error("OAuth2Exception:", e);
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(oAuth2Exception));
        }
        //用户锁定包含动态参数 需要走单独exception
        if (e.getCause() instanceof UserLockException) {
            UserLockException userLockException = (UserLockException) e.getCause();
            return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(userLockException));
        }
        if (Objects.nonNull(e.getCause()) && StringUtils.isNotBlank(e.getCause().getMessage())) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(e.getCause().getMessage(), e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(ErrorConstant.UNK_EX, e));
    }
}
