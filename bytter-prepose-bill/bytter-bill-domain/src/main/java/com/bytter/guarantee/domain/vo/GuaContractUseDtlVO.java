package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaContractUseDtlVO
 * @ProjectName T3(3.2)
 * @Description: 担保合同使用明细
 * @date 2020/4/29 15:30
 */
public class GuaContractUseDtlVO {
    private String id;
    /** 业务ID */
    private String businessId;
    /**
     * 担保合同id
     */
    private String contractId;
    /** 系统代码 */
    @Dict(dictGroup = "gua.busType", inject = "businessName")
    private String sysCode;
    /** 业务品种名称 */
    private String businessName;
    /** 业务编号 */
    private String itemNo;
    /** 业务币别 */
//    @Currency(inject = "itemCurName")
    private String itemCurId;
    /** 业务币别名称 */
    private String itemCurName;
    /** 业务金额 */
    private BigDecimal itemMoney;
    /** 业务开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 业务结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 数据类型 */
    private String dataType;
    /** 业务合同编号 */
    private String itemConCode;
    /** 状态 */
    private Integer status;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getItemCurId() {
        return itemCurId;
    }
    public void setItemCurId(String itemCurId) {
        this.itemCurId = itemCurId;
    }
    public String getItemCurName() {
        return itemCurName;
    }
    public void setItemCurName(String itemCurName) {
        this.itemCurName = itemCurName;
    }
    public BigDecimal getItemMoney() {
        return itemMoney;
    }
    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getItemConCode() {
        return itemConCode;
    }
    public void setItemConCode(String itemConCode) {
        this.itemConCode = itemConCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
