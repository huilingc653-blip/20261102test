package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 授信调整实体
 * 
 * @author luxy Title: RatBkrationAdjust
 */
@Entity
@Table(name = "RAT_BKRATION_ADJUST", schema = "")
public class RatBkrationAdjust extends IdEntity implements java.io.Serializable {
    /** 业务id */
    private String businessId;
    /** 业务类型 授信录入/授信分配 */
    private String businessType;
    /** 调整编号 */
    private String adjustCode;
    /** 网点编号 */
    private String netCode;
    /** 单位代码 */
    private String corpId;
    /** 备注 */
    private String remark;
    /** 状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 附件ID */
    private String attachmentId;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务id
     */
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 业务id
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务类型
     */
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 业务类型
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 调整编号
     */
    @Column(name = "ADJUST_CODE")
    public String getAdjustCode() {
        return adjustCode;
    }
    /**
     * 方法:设置java.lang.String
     * 
     * @return: java.lang.String 调整编号
     */
    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 网点编号
     */
    @Column(name = "NET_CODE")
    public String getNetCode() {
        return netCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 网点编号
     */
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 附件ID
     */
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
