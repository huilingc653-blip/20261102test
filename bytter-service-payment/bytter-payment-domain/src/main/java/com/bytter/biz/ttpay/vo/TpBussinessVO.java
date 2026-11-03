package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.bpm.IBusinessKey;
import java.util.Date;
import java.util.List;
public class TpBussinessVO implements java.io.Serializable,  IBusinessKey {
    private String id;
    //编号
    @BtCompositeSearch(fieldName = "t.code")
    private String code;
    //商户类型
    @BtCompositeSearch(fieldName = "t.SHOPTYPE")
    private String shopType;
    //商户类型名称
    @BtCompositeSearch(fieldName = "t.SHOPTYPE")
    private String shoptypeName;
    //商户名称
    @BtCompositeSearch(fieldName = "t.SHOP_NAME")
    private String shopName;
    //商户关联信息表
    private List<TpBussinessTextVO> tpBussinessTextVOS;
    /** 单位ID */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName="t.corp_id")
    private java.lang.String corpId;
    /** 单位 */
    @BtCompositeSearch(fieldName="t.corp_id")
    private java.lang.String corpName;
    //所在省市
    private String province;
    //地址
    private String address;
    //联系人
    private String contactMan;
    //联系电话
    private String tel;
    //商户号类别
    private String shoptypeNo;
    //登录号
    private String loginNo;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //有效标志 0有效1无效
    @BtCompositeSearch(fieldName="t.IS_FLAG")
    private String isFlag;
    //备注
    private String rmk;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**状态*/
    private Integer status;
    //支付类型
    private String payType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public List<TpBussinessTextVO> getTpBussinessTextVOS() {
        return tpBussinessTextVOS;
    }
    public void setTpBussinessTextVOS(List<TpBussinessTextVO> tpBussinessTextVOS) {
        this.tpBussinessTextVOS = tpBussinessTextVOS;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getShoptypeName() {
        return shoptypeName;
    }
    public void setShoptypeName(String shoptypeName) {
        this.shoptypeName = shoptypeName;
    }
    public String getShopType() {
        return shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
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
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getContactMan() {
        return contactMan;
    }
    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getShoptypeNo() {
        return shoptypeNo;
    }
    public void setShoptypeNo(String shoptypeNo) {
        this.shoptypeNo = shoptypeNo;
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
    public String getIsFlag() {
        return isFlag;
    }
    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }
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
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
