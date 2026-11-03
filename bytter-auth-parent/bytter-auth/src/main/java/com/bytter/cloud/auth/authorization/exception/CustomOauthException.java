package com.bytter.cloud.auth.authorization.exception;

import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.common.core.exception.ErrorType;
import com.bytter.cloud.common.core.exception.IMessage;
import com.bytter.cloud.common.core.persistence.vo.Result;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.StringUtils;

/**
 * 自定义oauth异常
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    private final Result<?> result;

    public CustomOauthException(OAuth2Exception oAuth2Exception) {
        super(oAuth2Exception.getSummary(), oAuth2Exception);
        String oauth2ErrorCode = oAuth2Exception.getOAuth2ErrorCode();
        String msg = oAuth2Exception.getMessage();
        ErrorType authErrorType = null;
        if (!StringUtils.isEmpty(oauth2ErrorCode)) {
            switch (oauth2ErrorCode) {
                case OAuth2Exception.INVALID_GRANT:
                    if (ErrorConstant.AUTH_ERROR_USER_IS_DISABLED_MSG.equals(msg)) {
                        // 用户被禁用
                        authErrorType = AuthMessageEnum.AUTH_USER_DISABLED;
                    } else if (ErrorConstant.AUTH_BAD_CREDENTIALS_MSG.equals(msg)) {
                        // 密码错误
                        authErrorType = AuthMessageEnum.AUTH_BAD_CREDENTIAL;
                    } else if (ErrorConstant.AUTH_ERROR_USER_LOCKED.equals(msg)) {
                        //用户已被锁定
                        authErrorType = AuthMessageEnum.AUTH_USER_LOCKED;
                    } else if (ErrorConstant.AUTH_ERROR_CERT_INVALID.equals(msg)) {
                        // 证书无效
                        authErrorType = AuthMessageEnum.AUTH_ERROR_CERT_INVALID;
                    } else if (ErrorConstant.AUTH_ERROR_CA_CERT_NUMBER.equals(msg)) {
                        // 用户证书错误
                        authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_NUMBER;
                    } else if (ErrorConstant.AUTH_ERROR_USER_NOT_BINDING_CERT.equals(msg)) {
                        // 用户未绑定证书
                        authErrorType = AuthMessageEnum.AUTH_USER_NOT_BINDING_CERT;
                    } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_WRONG;
                    } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_PASSOWRD;
                    } else if (ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD_CERT.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_USER_LOGIN_TYPE_PASSOWRD_CERT;
                    } else if (ErrorConstant.AUTH_ERROR_TOKEN_INVALID.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_INVALID_TOKEN;
                    } else if (ErrorConstant.AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE;
                    } else if (ErrorConstant.AUTH_ERROR_CA_CERT_ENDDATE_AFTER.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_ENDDATE_AFTER;
                    } else if (ErrorConstant.AUTH_ERROR_CA_CERT_DATE_ERROR.equals(msg)) {
                        authErrorType = AuthMessageEnum.AUTH_ERROR_CA_CERT_DATE_ERROR;
                    }
                    break;
                default:
                    ///authErrorType = AuthMessageEnum.getAuthErrorType(oAuth2Exception.getOAuth2ErrorCode().toUpperCase());
                    break;
            }
        }
        if (authErrorType == null) {
            IMessage msg1 = new IMessage() {
                @Override
                public String name() {
                    return oAuth2Exception.getMessage();
                }
            };
            this.result = Result.fail(msg1);
        } else {
            this.result = Result.fail(authErrorType, oAuth2Exception);
        }
    }

    CustomOauthException(ErrorType authErrorType, Exception e) {
        super(e.getMessage(), e);
        this.result = Result.fail(authErrorType);
    }

    CustomOauthException(String msg,Exception exception) {
        super(msg, exception);
        this.result = Result.fail(msg);
    }

    public Result<?> getResult() {
        return result;
    }
}
