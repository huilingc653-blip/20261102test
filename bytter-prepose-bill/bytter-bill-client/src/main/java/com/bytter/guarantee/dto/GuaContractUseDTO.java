package com.bytter.guarantee.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * Title: GuaContractUseDTO
 *
 * @author luxy
 * @since 2019/7/29 15:27
 */
public class GuaContractUseDTO implements Serializable {
    /**
     * 业务ID（必填）
     */
    private String businessId;
    /**
     * 系统代码（必填）
     */
    private String sysCode;
    /**
     * 业务编号（必填）
     */
    private String itemNo;
    /**
     * 使用时间（选填）
     */
    private Date useDate;
    /**
     * 状态（选填）
     */
    private Integer status;
    private String guaWay;
    /**
     * 担保方式
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
     * 占用担保合同明细
     */
    private List<GuaContractDTO> contracts;
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
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
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
    public List<GuaContractDTO> getContracts() {
        return contracts;
    }
    public void setContracts(List<GuaContractDTO> contracts) {
        this.contracts = contracts;
    }
    public String getGuaPerson() {
        return guaPerson;
    }
    public void setGuaPerson(String guaPerson) {
        this.guaPerson = guaPerson;
    }
}
