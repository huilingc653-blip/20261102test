package com.bytter.guarantee.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * Title Description
 * 
 * @author liusy
 * @since 2019-07-22 18:33
 * @version 1.0
 */
public class GuaContractOccupDTO implements Serializable {
    /**
     * 业务ID（必填）
     */
    private String businessId;
    /**
     * 担保合同ID（必填）
     */
    private List<String> contractIds;
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
    /**
     * 担保合同号（选填）
     */
    private String contractCode;
    /**
     * 担保方式ID
     */
    private String guaWay;
    /**
     * 担保方式名（选填）
     */
    private String guaWayName;
    /**
     * 担保备注（选填）
     */
    private String guaRmk;
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public List<String> getContractIds() {
        return contractIds;
    }
    public void setContractIds(List<String> contractIds) {
        this.contractIds = contractIds;
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
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
