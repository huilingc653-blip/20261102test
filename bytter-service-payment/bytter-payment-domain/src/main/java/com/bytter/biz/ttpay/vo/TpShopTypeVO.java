package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.util.Date;
public class TpShopTypeVO implements java.io.Serializable,  IBusinessKey {
    private String id;
    //编号
    @BtCompositeSearch(fieldName = "t.code")
    private String code;
    //商户类型
    @BtCompositeSearch(fieldName = "t.shopType")
    private String shopType;
    //有效标志 0有效1无效
    @Dict(dictGroup = "sf_yn", inject = "isValid")
    @BtCompositeSearch(fieldName = "t.IS_FLAG")
    private String isFlag;
    //有效标志 0有效1无效
    private String isValid;
    //备注
    @BtCompositeSearch(fieldName = "t.rmk")
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
    /**
     * 状态
     */
    private Integer status;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getShopType() {
        return shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
