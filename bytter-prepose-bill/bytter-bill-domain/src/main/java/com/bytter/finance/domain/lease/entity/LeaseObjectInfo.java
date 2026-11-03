package com.bytter.finance.domain.lease.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_LEASE_SUB_MATER", schema = "")
@SuppressWarnings("serial")
public class LeaseObjectInfo extends IdEntity implements java.io.Serializable {
    /**
     * 租赁表ID
     */
    private String leaseId;
    /**
     * 租赁物类别表ID
     */
    private String leaseTypeId;
    /**
     * 租赁物编号
     */
    private String leaseItemCode;
    /**
     * 租赁物名称
     */
    private String leaseItemName;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 规格型号
     */
    private String speModel;
    /**
     * 计量单位
     */
    private String calCorp;
    /**
     * 数量
     */
    private Integer numb;
    /**
     * 币种
     */
    private String curId;
    /**
     * 原值金额
     */
    private BigDecimal oldMoney;
    /**
     * 现值金额
     */
    private BigDecimal nowMoney;
    /**
     * 存放地点
     */
    private String saveAddr;
    /**
     * 生产厂家
     */
    private String makeBy;
    /**
     * 购置时间
     */
    private Date buyDate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    public LeaseObjectInfo() {
    }
    /**
     * 租赁表ID
     */
    @Column(name = "LEASE_ID")
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    @Column(name = "LEASE_TYPE_ID")
    public String getLeaseTypeId() {
        return leaseTypeId;
    }
    public void setLeaseTypeId(String leaseTypeId) {
        this.leaseTypeId = leaseTypeId;
    }
    @Column(name = "LEASE_ITEM_CODE")
    public String getLeaseItemCode() {
        return leaseItemCode;
    }
    public void setLeaseItemCode(String leaseItemCode) {
        this.leaseItemCode = leaseItemCode;
    }
    @Column(name = "LEASE_ITEM_NAME")
    public String getLeaseItemName() {
        return leaseItemName;
    }
    public void setLeaseItemName(String leaseItemName) {
        this.leaseItemName = leaseItemName;
    }
    @Column(name = "BRAND")
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Column(name = "SPE_MODEL")
    public String getSpeModel() {
        return speModel;
    }
    public void setSpeModel(String speModel) {
        this.speModel = speModel;
    }
    @Column(name = "CAL_CORP")
    public String getCalCorp() {
        return calCorp;
    }
    public void setCalCorp(String calCorp) {
        this.calCorp = calCorp;
    }
    @Column(name = "NUMB")
    public Integer getNumb() {
        return numb;
    }
    public void setNumb(Integer numb) {
        this.numb = numb;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    @Column(name = "OLD_MONEY")
    public BigDecimal getOldMoney() {
        return oldMoney;
    }
    public void setOldMoney(BigDecimal oldMoney) {
        this.oldMoney = oldMoney;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "NOW_MONEY")
    public BigDecimal getNowMoney() {
        return nowMoney;
    }
    public void setNowMoney(BigDecimal nowMoney) {
        this.nowMoney = nowMoney;
    }
    @Column(name = "SAVE_ADDR")
    public String getSaveAddr() {
        return saveAddr;
    }
    public void setSaveAddr(String saveAddr) {
        this.saveAddr = saveAddr;
    }
    @Column(name = "MAKE_BY")
    public String getMakeBy() {
        return makeBy;
    }
    public void setMakeBy(String makeBy) {
        this.makeBy = makeBy;
    }
    @Column(name = "BUY_DATE")
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
