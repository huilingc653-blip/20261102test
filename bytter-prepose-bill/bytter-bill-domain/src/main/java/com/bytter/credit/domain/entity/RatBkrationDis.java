package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 授信分配信息表
 * @date 2017-07-31 16:07:30
 */
@Entity
@Table(name = "RAT_BKRATION_DIS", schema = "")
@SuppressWarnings("serial")
public class RatBkrationDis extends IdEntity implements java.io.Serializable {
    /**
     * 分配编号
     */
    private String disCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 原授信单位编号
     */
    private String ratCorpId;
    /**
     * 公司代码
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行类别名称
     */
    private String bankTypeName;
    /**
     * 授信种类
     */
    private String ratType;
    /**
     * 授信金额
     */
    private java.math.BigDecimal money;
    /**
     * 敞口金额
     */
    private java.math.BigDecimal openMoney;
    /**
     * 是否通过
     */
    private String ispassed;
    /**
     * 分配方式
     */
    private String distributeWay;
    /**
     * 备注
     */
    private String remark;
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
     * 额度类型
     */
    private String type;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 额度使用单位代码
     */
    private String corpCode;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 分配编号
     */
    @Column(name = "DIS_CODE")
    public String getDisCode() {
        return this.disCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 分配编号
     */
    public void setDisCode(String disCode) {
        this.disCode = disCode;
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
     * @return: java.lang.String 原授信单位编号
     */
    @Column(name = "RAT_CORP_ID")
    public String getRatCorpId() {
        return this.ratCorpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 原授信单位编号
     */
    public void setRatCorpId(String ratCorpId) {
        this.ratCorpId = ratCorpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 公司代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 公司代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
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
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 授信金额
     */
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 授信金额
     */
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 敞口金额
     */
    @Column(name = "OPEN_MONEY")
    public java.math.BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 敞口金额
     */
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否通过
     */
    @Column(name = "ISPASSED")
    public String getIspassed() {
        return this.ispassed;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否通过
     */
    public void setIspassed(String ispassed) {
        this.ispassed = ispassed;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 分配方式
     */
    @Column(name = "DISTRIBUTE_WAY")
    public String getDistributeWay() {
        return this.distributeWay;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 分配方式
     */
    public void setDistributeWay(String distributeWay) {
        this.distributeWay = distributeWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK")
    public String getRemark() {
        return this.remark;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 备注
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
     * @return: java.lang.String 额度类型
     */
    @Column(name = "TYPE")
    public String getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 额度类型
     */
    public void setType(String type) {
        this.type = type;
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
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
}
