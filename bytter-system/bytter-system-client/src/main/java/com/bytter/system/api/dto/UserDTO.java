package com.bytter.system.api.dto;
import com.bytter.core.base.entity.ICurrentUser;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhoul on 2017/6/19.
 */
public class UserDTO implements ICurrentUser, Serializable {
	private String tenantId ;
	private String id;
    private String loginName;   //登录名
    private String password;    //密码
    private String email;       //电子邮件
    private String username;    //用户名
    private Integer sex;        //性别
    private Integer status;     //状态
    private String phone;       //联系电话
    private Integer isAdmin;    //是否为管理员
    private Date beginUseTime;  //启用日期
    private String code;        //编号
    private Date createTime;    //创建时间
    private String createBy;    //创建者
    private Date updateTime;    //更新时间
    private String updateBy;    //更新者
    private CorpDTO corp;
    private String showText;//显示
    private String salt;        //密码盐
    private Integer loginType;  //用户登录方式 0用户密码方式，1证书认证＋用户密码方式，2证书认证方式
    private String employeeId;//第三方平台关联员工编号
    private String dingTalkUserId;//钉钉账号
    private String weiXinUserId;//企业微信账号
    private String feishuUserId;//飞书账号
    private String sysSkin;//系统肤色
	private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private Integer tenantStatus;
    private String userCadn;    //用户ca证书序列号

    public String getUserCadn() {
        return userCadn;
    }

    public void setUserCadn(String userCadn) {
        this.userCadn = userCadn;
    }

    public String getDingTalkUserId() {
        return dingTalkUserId;
    }

    public void setDingTalkUserId(String dingTalkUserId) {
        this.dingTalkUserId = dingTalkUserId;
    }

    public String getWeiXinUserId() {
        return weiXinUserId;
    }

    public void setWeiXinUserId(String weiXinUserId) {
        this.weiXinUserId = weiXinUserId;
    }

    public String getFeishuUserId() {
        return feishuUserId;
    }

    public void setFeishuUserId(String feishuUserId) {
        this.feishuUserId = feishuUserId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
    public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	@Override
    public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
    @Override
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public String getCorpId() {
        if (getCorp() != null) {
            return getCorp().getId();
        }
        return null;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Integer getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }
    public Date getBeginUseTime() {
        return beginUseTime;
    }
    public void setBeginUseTime(Date beginUseTime) {
        this.beginUseTime = beginUseTime;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public CorpDTO getCorp() {
        return corp;
    }
    public void setCorp(CorpDTO corp) {
        this.corp = corp;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getShowText() {
        return this.getLoginName() + "(" + this.getUsername() + ")";
    }
    public void setShowText(String showText) {
        this.showText = showText;
    }

    public Integer getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(Integer tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    public String getSysSkin() {
        return sysSkin;
    }

    public void setSysSkin(String sysSkin) {
        this.sysSkin = sysSkin;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", sex=" + sex +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", isAdmin=" + isAdmin +
                ", beginUseTime=" + beginUseTime +
                ", code='" + code + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", corp=" + corp +
                ", showText='" + showText + '\'' +
                ", salt='" + salt + '\'' +
                ", loginType=" + loginType +
                ", employeeId='" + employeeId + '\'' +
                ", dingTalkUserId='" + dingTalkUserId + '\'' +
                ", weiXinUserId='" + weiXinUserId + '\'' +
                ", feishuUserId='" + feishuUserId + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", tenantStatus=" + tenantStatus +
                ", userCadn='" + userCadn + '\'' +
                '}';
    }
}
