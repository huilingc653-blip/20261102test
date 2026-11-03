package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.User;
import java.io.Serializable;
import java.util.Date;
/**
 * @Title: DTO
 * @Description: 付款方式用户分配DTO
 * @author fusb
 * @date 2020-02-20 10:08:00
 * @version 1.0
 *
 */
public class BtAssignPayWayUserDTO implements Serializable {
    private String id;
    /**用户ID*/
    @User(inject = "userName")
    private String userId;
    private String userName;
    /**付款方式*/
    @Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
    private String voucherCode;
    /**付款方式*/
    private String voucherName;
    /**系统结算方式*/
    private String sysVoucherCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private java.util.Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getVoucherCode() {
        return voucherCode;
    }
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getSysVoucherCode() {
        return sysVoucherCode;
    }
    public void setSysVoucherCode(String sysVoucherCode) {
        this.sysVoucherCode = sysVoucherCode;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
