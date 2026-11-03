package com.bytter.system.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.bytter.system.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 用户管理VO
 *
 * @author zhoul
 * @date 2017/5/19
 */
@ExcelTarget("UserVO")
public class UserVO extends ExcelModel implements Serializable {
    private static final long serialVersionUID = 3650731181127660208L;
    private String id;
    @Excel(name = "*登录账号")
    @NotNull(message = "sys.user.message.loginname.not.null")
    private String loginName;   //登录名
    private String password; // 密码
    private String salt;        //密码盐
    @Excel(name = "*用户名")
    @NotNull(message = "sys.user.message.username.not.null")
    private String username;    //用户名
    private Integer sex;        //性别
    //@Dict(dictGroup = "sys.user.status", inject = "statusLang")
    private Integer status;     //状态
    @NotNull(message = "sys.user.message.status.not.null")
    @Excel(name = "*状态")
    private String statusLang; //状态字典翻译
    private Integer isAdmin;    //是否为管理员
    @Excel(name = "是否管理员")
    private String isAdminLang;    //是否为管理员
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginUseTime;  //启用日期
    private String code;        //编号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;    //创建时间
    private String createBy;    //创建者
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;    //更新时间
    private String updateBy;    //更新者
    /*@Corps({
            @Corp(inject = "corpName"),
            @Corp(inject = "corpCode", field = "code")
    })*/
    private String corpId;      //单位ID
    @Excel(name = "*所属单位")
    @NotNull(message = "sys.user.message.corpname.not.null")
    private String corpName;    //单位名称
    private String corpCode;
    private String userCadn;   //用户ca证书序列号
    @Excel(name = "联系电话")
    private String phone;       //联系电话
    @Excel(name = "EMAIL")
    private String email;       //电子邮件
    private String sysSkin;//系统肤色
    private List<Role> roles;
    private Integer showReport;    //是否APP显示报表页面：空或0表示不显示；

    public String getSearchBox() {
        return searchBox;
    }

    public void setSearchBox(String searchBox) {
        this.searchBox = searchBox;
    }

    /**
     * 大森林特殊需求，app手机端，待办列表，结算管理-付款办理 不需要 会计凭证号 查询框，前端app不方便调整和修改，于是让后端 增加一个字段用于判断隐藏 输入框
     */
    private String searchBox;

    public Integer getShowReport() {
		return showReport;
	}
	public void setShowReport(Integer showReport) {
		this.showReport = showReport;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
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
    public String getStatusLang() {
        return statusLang;
    }
    public void setStatusLang(String statusLang) {
        this.statusLang = statusLang;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getUserCadn() {
        return userCadn;
    }
    public void setUserCadn(String userCadn) {
        this.userCadn = userCadn;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSysSkin() {
        return sysSkin;
    }
    public void setSysSkin(String sysSkin) {
        this.sysSkin = sysSkin;
    }
    public String getIsAdminLang() {
        return isAdminLang;
    }
    public void setIsAdminLang(String isAdminLang) {
        this.isAdminLang = isAdminLang;
    }
    @Override
    public String getBeanName() {
        return "userService";
    }
    @Override
    public String toString() {
        return "UserVO{" +
                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", username='" + username + '\'' +
                ", sex=" + sex +
                ", status=" + status +
                ", statusLang='" + statusLang + '\'' +
                ", isAdmin=" + isAdmin +
                ", beginUseTime=" + beginUseTime +
                ", code='" + code + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", userCadn='" + userCadn + '\'' +
                '}';
    }
}
