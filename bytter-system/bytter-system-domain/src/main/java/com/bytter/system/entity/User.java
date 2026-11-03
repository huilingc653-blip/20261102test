package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Created by zhoul on 2017/4/11.
 */
@Entity
@Table(name = "SYS_USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"LOGIN_NAME"})})
public class User extends IdEntity implements Serializable {
    private String loginName;   //登录名
    private String password;    //密码
    private String salt;        //密码盐
    private String email;       //电子邮件
    private String username;    //用户名
    private Integer sex;        //性别
    private Integer status;     //状态, see UserConstant.java
    private Integer loginType;  //用户登录方式 0用户密码方式，1证书认证＋用户密码方式，2证书认证方式
    private String phone;       //联系电话
    private Integer isAdmin;    //是否为管理员
    private Date beginUseTime;  //启用日期
    private String code;        //编号
    private Date createTime;    //创建时间
    private String createBy;    //创建者
    private Date updateTime;    //更新时间
    private String updateBy;    //更新者
    private String userCadn;    //用户ca证书序列号
    private Integer showReport;    //是否APP显示报表页面：空或0表示不显示；
    private Integer faceVerifyStatus;   //人脸复核结果：1通过；0不通过
    private String sysSkin;//系统肤色
    private String employeeId;//第三方平台关联员工编号
    private String tenantId;// 租户ID
    @JsonIgnore
    private Corp corp;
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>(0);
    public User() {
    }
    @Column(name = "TENANT_ID", length = 50)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "LOGIN_NAME", nullable = false, length = 30)
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName != null ? loginName.trim() : loginName;
    }
    @Column(name = "PASSWORD", nullable = false, length = 32)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "SALT", nullable = false, length = 32)
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    @Column(name = "EMAIL", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "USERNAME", nullable = false, length = 30)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Column(name = "SEX", nullable = false, length = 1)
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    @Column(name = "STATUS", nullable = false, length = 1)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "LOGIN_TYPE", nullable = false, length = 1)
    public Integer getLoginType() {
        return loginType;
    }
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }
    @Column(name = "PHONE", nullable = false, length = 15)
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "IS_ADMIN", length = 1)
    public Integer getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }
    @Column(name = "BEGIN_USE_TIME")
    public Date getBeginUseTime() {
        return beginUseTime;
    }
    public void setBeginUseTime(Date beginUseTime) {
        this.beginUseTime = beginUseTime;
    }
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "CREATE_BY", length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "UPDATE_BY", length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    //fetch 关联对象抓取的方式
    //cascade 级联关系
    @ManyToOne
    @JoinColumn(name = "CORP_ID")
    public Corp getCorp() {
        return corp;
    }
    public void setCorp(Corp corp) {
        this.corp = corp;
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }
    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    @Column(name = "USER_CADN", length = 200)
    public String getUserCadn() {
        return userCadn;
    }
    public void setUserCadn(String userCadn) {
        this.userCadn = userCadn;
    }
    @Column(name = "SHOW_REPORT", length = 1)
    public Integer getShowReport() {
        return showReport;
    }
    public void setShowReport(Integer showReport) {
        this.showReport = showReport;
    }
    @Transient
    public Integer getFaceVerifyStatus() {
        return faceVerifyStatus;
    }
    public void setFaceVerifyStatus(Integer faceVerifyStatus) {
        this.faceVerifyStatus = faceVerifyStatus;
    }
    @Column(name = "SYS_SKIN")
    public String getSysSkin() {
        return sysSkin;
    }
    public void setSysSkin(String sysSkin) {
        this.sysSkin = sysSkin;
    }

    @Column(name = "EMPLOYEE_ID")
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
