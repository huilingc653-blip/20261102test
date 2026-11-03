package com.bytter.guarantee.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b> 借款查询担保合同DTO <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/10/26 0026 <br>
 */
public class GuaContractQueryDTO implements Serializable {
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 担保合同ID
     */
    private String guaId;
    /**
     * 系统代码
     */
    private String sysCode;
    /**
     * 状态（选填）
     */
    private Integer status;
    /**
     * 担保方式ID
     */
    private String guaWay;
    /**
     * 担保方式名
     */
    private String guaWayName;
    /**
     * 担保备注
     */
    private String guaRmk;
    /**
     * 担保单位
     */
    private String guaPerson;
    /**
     * 担保金额
     */
    private BigDecimal guaMoney;
    /**
     * 抵质押物
     */
    private String pledgeName;
    /**
     * 合同编号
     */
    private String contractCode;
    /**
     * 担保开始日
     */
    private Date startDate;
    /**
     * 担保截止日
     */
    private Date endDate;
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getGuaWayName() {
        return guaWayName;
    }
    public void setGuaWayName(String guaWayName) {
        this.guaWayName = guaWayName;
    }
    public String getGuaRmk() {
        return guaRmk;
    }
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    public String getGuaPerson() {
        return guaPerson;
    }
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
    }
    public BigDecimal getGuaMoney() {
        if (null == guaMoney) {
            guaMoney = BigDecimal.ZERO;
        }
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
    public String getPledgeName() {
        return pledgeName;
    }
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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
}
