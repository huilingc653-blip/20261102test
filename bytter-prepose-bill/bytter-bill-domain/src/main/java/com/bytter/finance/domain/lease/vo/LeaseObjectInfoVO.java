package com.bytter.finance.domain.lease.vo;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/*
融资租赁标的物vo
 */
public class LeaseObjectInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    /** ID */
    private String Id;
    /** 租赁表ID */
    private String leaseId;
    /** 租赁物类别表ID */
    private String leaseTypeId;
    /** 租赁单号 */
    private String leaseNo;
    @Excel(name = "租赁物类别")
    private String name;
    /**
     * 承租人
     */
    private String acceptor;
    /** 租赁物编号 */
    @Excel(name = "*租赁物编号")
    private String leaseItemCode;
    /** 租赁物名称 */
    @Excel(name = "*租赁物名称")
    private String leaseItemName;
    /** 原值金额 */
    @Excel(name = "原值金额")
    private BigDecimal oldMoney;
    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;
    /** 规格型号 */
    @Excel(name = "规格型号")
    private String speModel;
    /** 计量单位 */
    @Excel(name = "计量单位")
    private String calCorp;
    /** 数量 */
    @Excel(name = "数量")
    private Integer numb;
//    @Currency(inject = "curName")
    private String curId;
    @Excel(name = "*币种")
    private String curName;
    /** 现值金额 */
    @Excel(name = "*现值金额")
    private BigDecimal nowMoney;
    /** 存放地点 */
    @Excel(name = "存放地点")
    private String saveAddr;
    /** 生产厂家 */
    @Excel(name = "生产厂家")
    private String makeBy;
    /** 购置时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "购置时间", format = "yyyy-MM-dd")
    private Date buyDate;
    /** 备注 */
    @Excel(name = "备注")
    private String rmk;
    /** 状态 */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public String getLeaseNo() {
        return leaseNo;
    }
    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }
    public Integer getNumb() {
        return numb;
    }
    public void setNumb(Integer numb) {
        this.numb = numb;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    public String getLeaseTypeId() {
        return leaseTypeId;
    }
    public void setLeaseTypeId(String leaseTypeId) {
        this.leaseTypeId = leaseTypeId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getOldMoney() {
        return oldMoney;
    }
    public void setOldMoney(BigDecimal oldMoney) {
        this.oldMoney = oldMoney;
    }
    public String getLeaseItemCode() {
        return leaseItemCode;
    }
    public void setLeaseItemCode(String leaseItemCode) {
        this.leaseItemCode = leaseItemCode;
    }
    public String getLeaseItemName() {
        return leaseItemName;
    }
    public void setLeaseItemName(String leaseItemName) {
        this.leaseItemName = leaseItemName;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getSpeModel() {
        return speModel;
    }
    public void setSpeModel(String speModel) {
        this.speModel = speModel;
    }
    public String getCalCorp() {
        return calCorp;
    }
    public void setCalCorp(String calCorp) {
        this.calCorp = calCorp;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getNowMoney() {
        return nowMoney;
    }
    public void setNowMoney(BigDecimal nowMoney) {
        this.nowMoney = nowMoney;
    }
    public String getSaveAddr() {
        return saveAddr;
    }
    public void setSaveAddr(String saveAddr) {
        this.saveAddr = saveAddr;
    }
    public String getMakeBy() {
        return makeBy;
    }
    public void setMakeBy(String makeBy) {
        this.makeBy = makeBy;
    }
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Override
    public String getBeanName() {
        return "leaseObjectInfoService";
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
