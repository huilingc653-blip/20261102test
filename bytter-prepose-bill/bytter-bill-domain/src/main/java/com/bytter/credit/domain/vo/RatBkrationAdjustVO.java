package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 授信调整DTO
 * 
 * @author luxy Title: RatBkrationAdjustVO
 */
public class RatBkrationAdjustVO implements Serializable, IBusinessKey {
    private String id;
    /** 业务id */
    @BtCompositeSearch(fieldName = "ra.business_Id")
    private String businessId;
    /** 业务类型 授信录入/授信分配 */
    @BtCompositeSearch(fieldName = "ra.BUSINESS_TYPE")
    private String businessType;
    /** 调整编号 */
    @BtCompositeSearch(fieldName = "ra.ADJUST_CODE")
    private String adjustCode;
    /** 网点编号 */
    private String netCode;
    /** 单位代码 */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "ra.CORP_ID")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 备注 */
    private String remark;
    /** 状态 */
    @BtCompositeSearch(fieldName = "ra.status")
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 授信币别 */
//    @Currency(inject = "curName")
    private String curCode;
    /** 授信币别名称 */
    private String curName;
    /** 授信银行名称 */
    private String bankName;
    /** 调整明细 */
    private List<RatBkrationAdjustDtlVO> adjustDtls;
    /** 品种总数 */
    private Integer itemSize;
    /** 附件ID */
    private String attachmentId;
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
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getAdjustCode() {
        return adjustCode;
    }
    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
    public List<RatBkrationAdjustDtlVO> getAdjustDtls() {
        return adjustDtls;
    }
    public void setAdjustDtls(List<RatBkrationAdjustDtlVO> adjustDtls) {
        this.adjustDtls = adjustDtls;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Integer getItemSize() {
        return itemSize;
    }
    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
