package com.bytter.cloud.auth.authorization.oauth2;

import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.constants.UserConstant;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 证书登录用户加载service，配合spring security
 *
 * @author huangw 2021-11-12
 * @since 1.0.0
 */
@Slf4j
@Service("certificateUserDetailsService")
public class CertificateUserDetailsServiceImpl extends BaseUserDetailsService {

	@DubboReference(check = true)
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String uniqueId) throws UsernameNotFoundException {
        String[] split = uniqueId.split("@BT@");
        String username = split[0];
        String caCertNumber = "";
        if(split!=null && split.length>1) {
        	caCertNumber = split[1];
        }
        UserDTO user = userService.getByUsername(username);//caCertNumber如果不为空，根据 username,caCertNumber查出来
        if (user == null) {
            throw new InvalidGrantException(ErrorConstant.AUTH_BAD_CREDENTIALS_MSG);
        }
        /// Integer isLocked = loginLockCache.get(user.getId());
        /// if ((isLocked == null) && !user.getAccountNonLocked()) {
            // 如果缓存不存在，并且用户处于锁定状态，自动解锁
            /// userService.resetStatus(user.getUsername(), tenant.getCode());
            /// user.setAccountNonLocked(UserConstant.ACCOUNT_NON_LOCKED_YES);
        /// }
        if (!user.isAccountNonLocked()) {
            //判断用户是否被锁定
            log.error("User has been locked");
            throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_USER_LOCKED);
        }
        // 获取用户登入方式 0用户密码，1银行密码+证书，2证书
        String userLoginType = String.valueOf(StringUtils.isEmpty(user.getLoginType()) ? "0" : user.getLoginType());//此处验证一下
        if (!UserConstant.LOGIN_TYPE_CERT.equals(userLoginType)) {
            if (UserConstant.LOGIN_TYPE_PASSWORD.equals(userLoginType)) {
                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD);
            } else if (UserConstant.LOGIN_TYPE_PASSWORD_CERT.equals(userLoginType)) {
                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_LOG_TYPE_PASSOWRD_CERT);
            } else {
                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_LOG_TYPE);
            }
        }
       
        
//        validCertNumber(user, caCertNumber);TODO
        // success登录成功！
        // 登录后清除角色资源授权的缓存
        Set<GrantedAuthority> roles = this.obtainGrantedAuthorities(user);
        AssertHelper.isTrueLoginEx(!CollectionUtils.isEmpty(roles), "用户未分配角色");
        String salt = user.getSalt();
        if (StringUtil.isBlank(salt)) {
			salt = DigestUtil.sha256Hex("000"+ user.getLoginName());
        }
        return getAuthUser(user, salt, roles);
    }
    
    // @NotNull
    // private static AuthUser getAuthUser(UserDTO user, String salt, Set<GrantedAuthority> roles) {
    //     org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getLoginName(),
	// 		   user.getPassword() + "|" + salt,
    //            user.isEnabled(),
    //            user.isAccountNonExpired(),
    //            user.isCredentialsNonExpired(),
    //            user.isAccountNonLocked(),
    //             roles);
    //     AuthUser userImpl = new AuthUser(userDetail);
    //     userImpl.setCurrentUserId(user.getId());
    //     userImpl.setCorpId(user.getCorpId());
    //     //用户名
    //     userImpl.setName(user.getUsername());
    //     userImpl.setLoginName(user.getLoginName());
    //     return userImpl;
    // }
    
//    private void validCertNumber(UserDTO user, String caCertNumber) {
//        if (!StringUtils.hasText(caCertNumber) || UserConstant.CA_CERT_NUMBER.equals(caCertNumber)) {
//            // 证书无效
//            throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_CERT_INVALID);
//        }
//        if (!StringUtils.hasText(user.getCertName())) {
//            // 用户未绑定证书，抛出错误
//            throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_USER_NOT_BINDING_CERT);
//        }
//        if (!caCertNumber.equals(user.getCertName())) {
//            // 校验用户绑定的证书是否正确
//            throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_CA_CERT_NUMBER);
//        }
//        LocalDateTime now = LocalDateTime.now();
//        ZoneId zoneId = ZoneId.systemDefault();
//        try {
//            LocalDateTime beginDateTime = LocalDateTime.ofInstant(user.getCertEndDate().toInstant(), zoneId);
//            if (now.isAfter(beginDateTime)) {
//                // 校验用户证书已过期
//                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_CA_CERT_ENDDATE_AFTER);
//            }
//            LocalDateTime endDateTime = LocalDateTime.ofInstant(user.getCertBeginDate().toInstant(), zoneId);
//            if (now.isBefore(endDateTime)) {
//                // 校验用户证书未生效
//                throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE);
//            }
//        } catch (InvalidGrantException e) {
//            throw e;
//        } catch (Exception e) {
//            // 用户证书启用日期、到期日期不正确
//            throw new InvalidGrantException(ErrorConstant.AUTH_ERROR_CA_CERT_DATE_ERROR);
//        }
//    }

    // /**
    //  * 获得登录者所有角色的权限集合.
    //  *
    //  * @param user 用户
    //  * @return 鉴权信息
    //  */
    // protected Set<GrantedAuthority> obtainGrantedAuthorities(UserDTO user) {
    //     Set<String> roles = userService.queryRolesByUserId(user.getId());
    //     log.info("user:{},roles:{}", user.getUsername(), roles);
    //     return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    // }
}
