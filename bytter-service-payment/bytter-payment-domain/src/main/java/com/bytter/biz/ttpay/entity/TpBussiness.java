package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "TP_BUSINESS", schema = "")
@SuppressWarnings("serial")
public class TpBussiness extends IdEntity implements java.io.Serializable {
    //编号
    private String code;
    //商户类型
    private String shopType;
    //商户名称
    private String shopName;
    //所在省市
    private String province;
    //地址
    private String address;
    //联系人
    private String contactMan;
    //联系电话
    private String tel;
    //有效标志 0有效1无效
    private String isFlag;
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
    /**
     * 状态
     */
    private Integer status;
    @Column(name = "CODE", nullable = true, length = 32)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "SHOPTYPE", nullable = true, length = 32)
    public String getShopType() {
        return shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
    @Column(name = "IS_FLAG", nullable = true, length = 32)
    public String getIsFlag() {
        return isFlag;
    }
    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true, length = 32)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true, length = 32)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "STATUS", nullable = true, length = 32)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "SHOP_NAME")
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    @Column(name = "PROVINCE")
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "CONTACT_MAN")
    public String getContactMan() {
        return contactMan;
    }
    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }
    @Column(name = "TEL")
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
}
