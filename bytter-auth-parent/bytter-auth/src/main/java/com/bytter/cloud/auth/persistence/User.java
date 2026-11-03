package com.bytter.cloud.auth.persistence;
import com.bytter.cloud.auth.constants.UserConstant;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
/**
 * 用户传输对象
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class User implements Serializable {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户登录失败次数
     */
    private Integer failsCount;
    /**
     * 证书序列号
     */
    private String caCertNumber;
    /**
     * 是否已删除：1已删除，0未删除
     */
    private String deleted;
    /**
     * 是否有效用户：1可用 ；0不可用；
     */
    private String enabled;
    /**
     * 是否是管理员： 1是 ；0不是；
     */
    private String isAdmin;
    /**
     * 用户账号是否过期：1未过期；0已过期；
     */
    private String accountNonExpired;
    /**
     * 用户密码是否过期：1未过期；0已过期；
     */
    private String credentialsNonExpired;
    /**
     * 用户账号是否被锁定：1未锁定；0已锁定；
     */
    private String accountNonLocked;
    /**
     * 证书名称
     */
    private String certName;
    /**
     * 证书内容
     */
    private String certInfo;
    /**
     * 证书启用日期
     */
    private Date certBeginDate;
    /**
     * 证书到期日期
     */
    private Date certEndDate;
    /**
     * 用戶salt
     */
    private String salt;
    /**
     * 是否有效用户
     *
     * @return 是否有效用户
     */
    public boolean getEnabled() {
        if (UserConstant.DELETED_YES.equals(this.deleted)) {
            return false;
        }
        return UserConstant.STATUS_ENABLE.equals(this.enabled);
    }
    /**
     * 用户账号是否过期
     *
     * @return 用户账号是否过期
     */
    public boolean getAccountNonExpired() {
        return UserConstant.ACCOUNT_NON_EXPIRED_YES.equals(this.accountNonExpired);
    }
    /**
     * 用户密码是否过期
     *
     * @return 用户密码是否过期
     */
    public boolean getCredentialsNonExpired() {
        return UserConstant.CREDENTIALS_NON_EXPIRED_YES.equals(this.credentialsNonExpired);
    }
    /**
     * 用户账号是否被锁定
     *
     * @return 用户账号是否被锁定
     */
    public boolean getAccountNonLocked() {
        return UserConstant.ACCOUNT_NON_LOCKED_YES.equals(this.accountNonLocked);
    }
}
