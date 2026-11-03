package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaContractOccupVO
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/7 16:14
 */
public class GuaContractOccupVO {
    private String id;
    /** 业务ID */
    private String businessId;
    /** 担保合同ID */
    @BtCompositeSearch(fieldName = "m.contract_id")
    private String contractId;
    /** 系统代码 */
    @BtCompositeSearch(fieldName = "m.SYSCODE")
    private String sysCode;
    /** 业务编号 */
    private String itemNo;
    /** 状态 */
    @BtCompositeSearch(fieldName = "m.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;
    /** 授信合同号 */
    private String ratContractNo;
    /** 授信模式 */
    @Dict(dictGroup = "RatBkration.RatMode", inject = "ratModeName")
    private String ratMode;
    private String ratModeName;
    /** 授信开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /** 授信结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 授信金额 */
    private BigDecimal money;
    /** 使用时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "m.use_date")
    private Date useDate;
    /** 担保合同号 */
    private String contractNo;
    /**
     * 单位id
     */
    private String corpId;
    private String corpName;
    /**
     * 币种
     */
    private String curId;
    private String curName;
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
    public String getContractId() {
        return contractId;
    }
    public void setContractId(String contractId) {
        this.contractId = contractId;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRatContractNo() {
        return ratContractNo;
    }
    public void setRatContractNo(String ratContractNo) {
        this.ratContractNo = ratContractNo;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getRatModeName() {
        return ratModeName;
    }
    public void setRatModeName(String ratModeName) {
        this.ratModeName = ratModeName;
    }
}
