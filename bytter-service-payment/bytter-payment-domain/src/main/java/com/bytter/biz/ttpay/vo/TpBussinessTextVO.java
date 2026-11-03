package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.util.Date;
public class TpBussinessTextVO implements java.io.Serializable,  IBusinessKey {
    private String id;
    /**
     * 支付方式，选择用何字段作为收款人账号
     */
    @Dict(dictGroup = "ttpay.payeeMode", inject = "payeeModeName")
    private String payeeMode;
    private String payeeModeName;
    /**
     * 商户表id
     */
    private String tpBusinessId;
    /**
     * 状态
     */
    private Integer status;
    //支付方式
    @Dict(dictGroup = "TTPAY_PAY_TYPE", inject = "payTypeName")
    private String payType;
    private String payTypeName;
    //登录号
    private String loginNo;
    //手机号
    private String phone;
    //邮箱
    private String email;
    /**商户编号**/
    private String shopCode;
    /**商户名称**/
    private String shopName;
    /**客商类型：客户、供应商**/
    private String shopType;
    //备注
    private String rmk;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getTpBusinessId() {
        return tpBusinessId;
    }
    public void setTpBusinessId(String tpBusinessId) {
        this.tpBusinessId = tpBusinessId;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getLoginNo() {
        return loginNo;
    }
    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getShopCode() {
        return shopCode;
    }
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getShopType() {
        return shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
    public String getPayeeMode() {
        return payeeMode;
    }
    public void setPayeeMode(String payeeMode) {
        this.payeeMode = payeeMode;
    }
    public String getPayeeModeName() {
        return payeeModeName;
    }
    public void setPayeeModeName(String payeeModeName) {
        this.payeeModeName = payeeModeName;
    }
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
