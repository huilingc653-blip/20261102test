package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 授信额度调剂表
 * @date 2017-07-31 16:10:53
 */
@Entity
@Table(name = "RAT_BKRATION_RELIEF", schema = "")
@SuppressWarnings("serial")
public class RatBkrationRelief extends IdEntity implements java.io.Serializable {
    /**
     * 授信种类
     */
    private String ratType;
    /**
     * 调剂编号
     */
    private String refCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 币种
     */
    private String curCode;
    /**
     * 调剂单位
     */
    private String reliefCorp;
    /**
     * 调剂额度编号
     */
    private String reliefCode;
    /**
     * 调剂额度品种
     */
    private String reliefItemFlag;
    /**
     * 调剂额度品种
     */
    private String reliefItemCode;
    /**
     * 被调剂单位
     */
    private String byReliefCorp;
    /**
     * 被调剂单位名称
     */
    private String byReliefCorpName;
    /**
     * 调剂单位名称
     */
    private String reliefCorpName;
    /**
     * 被调剂额度编号
     */
    private String byReliefCode;
    /**
     * 被调剂额度品种
     */
    private String byReliefItemFlag;
    /**
     * 被调剂额度品种
     */
    private String byReliefItemCode;
    /**
     * 被调剂金额
     */
    private java.math.BigDecimal reliefMoney;
    /**
     * 被调剂敞口金额
     */
    private java.math.BigDecimal reliefOpenMoney;
    /**
     * 开始日期
     */
    private java.util.Date begindate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信种类
     */
    @Column(name = "RAT_TYPE")
    public String getRatType() {
        return this.ratType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信种类
     */
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调剂编号
     */
    @Column(name = "REF_CODE")
    public String getRefCode() {
        return this.refCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 调剂编号
     */
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信编号
     */
    @Column(name = "RAT_CODE")
    public String getRatCode() {
        return this.ratCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信编号
     */
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币种
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 币种
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调剂单位
     */
    @Column(name = "RELIEF_CORP")
    public String getReliefCorp() {
        return this.reliefCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 调剂单位
     */
    public void setReliefCorp(String reliefCorp) {
        this.reliefCorp = reliefCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调剂额度编号
     */
    @Column(name = "RELIEF_CODE")
    public String getReliefCode() {
        return this.reliefCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 调剂额度编号
     */
    public void setReliefCode(String reliefCode) {
        this.reliefCode = reliefCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调剂额度品种
     */
    @Column(name = "RELIEF_ITEM_FLAG")
    public String getReliefItemFlag() {
        return this.reliefItemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 调剂额度品种
     */
    public void setReliefItemFlag(String reliefItemFlag) {
        this.reliefItemFlag = reliefItemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调剂额度品种
     */
    @Column(name = "RELIEF_ITEM_CODE")
    public String getReliefItemCode() {
        return this.reliefItemCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 调剂额度品种
     */
    public void setReliefItemCode(String reliefItemCode) {
        this.reliefItemCode = reliefItemCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 被调剂单位
     */
    @Column(name = "BY_RELIEF_CORP")
    public String getByReliefCorp() {
        return this.byReliefCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 被调剂单位
     */
    public void setByReliefCorp(String byReliefCorp) {
        this.byReliefCorp = byReliefCorp;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 被调剂额度编号
     */
    @Column(name = "BY_RELIEF_CODE")
    public String getByReliefCode() {
        return this.byReliefCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 被调剂额度编号
     */
    public void setByReliefCode(String byReliefCode) {
        this.byReliefCode = byReliefCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 被调剂额度品种
     */
    @Column(name = "BY_RELIEF_ITEM_FLAG")
    public String getByReliefItemFlag() {
        return this.byReliefItemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 被调剂额度品种
     */
    public void setByReliefItemFlag(String byReliefItemFlag) {
        this.byReliefItemFlag = byReliefItemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 被调剂额度品种
     */
    @Column(name = "BY_RELIEF_ITEM_CODE")
    public String getByReliefItemCode() {
        return this.byReliefItemCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 被调剂额度品种
     */
    public void setByReliefItemCode(String byReliefItemCode) {
        this.byReliefItemCode = byReliefItemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 被调剂金额
     */
    @Column(name = "RELIEF_MONEY")
    public java.math.BigDecimal getReliefMoney() {
        return this.reliefMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 被调剂金额
     */
    public void setReliefMoney(java.math.BigDecimal reliefMoney) {
        this.reliefMoney = reliefMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 被调剂敞口金额
     */
    @Column(name = "RELIEF_OPEN_MONEY")
    public java.math.BigDecimal getReliefOpenMoney() {
        return this.reliefOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 被调剂敞口金额
     */
    public void setReliefOpenMoney(java.math.BigDecimal reliefOpenMoney) {
        this.reliefOpenMoney = reliefOpenMoney;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    @Column(name = "BEGINDATE")
    public java.util.Date getBegindate() {
        return this.begindate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 开始日期
     */
    public void setBegindate(java.util.Date begindate) {
        this.begindate = begindate;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer
     * 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人登录名称
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
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人登录名称
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
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
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
    @Column(name = "BY_RELIEF_CORP_NAME")
    public String getByReliefCorpName() {
        return byReliefCorpName;
    }
    public void setByReliefCorpName(String byReliefCorpName) {
        this.byReliefCorpName = byReliefCorpName;
    }
    @Column(name = "RELIEF_CORP_NAME")
    public String getReliefCorpName() {
        return reliefCorpName;
    }
    public void setReliefCorpName(String reliefCorpName) {
        this.reliefCorpName = reliefCorpName;
    }
}
