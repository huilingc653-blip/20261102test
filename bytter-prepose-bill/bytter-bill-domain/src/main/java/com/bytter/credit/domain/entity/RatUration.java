package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 授信占用台账表
 * @author onlineGenerator
 * @date 2017-08-19 11:36:37
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_URATION", schema = "")
@SuppressWarnings("serial")
public class RatUration extends IdEntity implements java.io.Serializable {
    /** 占用台账编号 */
    private String urationCode;
    /** 系统类型 */
    private String type;
    /** 系统代码 */
    private String syscode;
    /** 业务名称 */
    private String businessName;
    /** 关联主业务主码 */
    private String billId;
    /** 关联主业务主码 */
    private String billNo;
    /** 单位代码 */
    private String corpId;
    /** 占用授信类型 */
    private String ratType;
    /** 授信编号 */
    private String ratCode;
    /** 授信分配编号 */
    private String disCode;
    /** 授信币别 */
    private String curCode;
    /** 品种标识 */
    private String itemFlag;
    /** 占用授信品种 */
    private String itemCode;
    /** 占用到期日期 */
    private java.util.Date scheduleenddate;
    /** 金额 */
    private BigDecimal money;
    /** 敞口额度 */
    private BigDecimal openMoney;
    /** 拆算前金额 */
    private BigDecimal preMoney;
    /** 折算前额度 */
    private BigDecimal convertPreMoney;
    /** 汇率 */
    private BigDecimal exchangeRate;
    /** 业务币别 */
    private String initCurCode;
    /** 备注 */
    private String remark;
    /** 状态 */
    // 状态 0 录入暂存占用；1 提交申请后冻结；2 额度已占用；3 额度已释放
    private Integer status;
    /** 开始日期 */
    private java.util.Date beginDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 业务金额 */
    private BigDecimal billMoney;
    /** 保证金币种 */
    private String bondCurId;
    /** 保证金金额 */
    private BigDecimal bondMoney;
    /** 业务名称 */
    private String billName;
    /** 是否启用授信敞口 0—不启用，1—启用 */
    private String isOpenRat;
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用台账编号
     */
    @Column(name = "URATION_CODE")
    public String getUrationCode() {
        return this.urationCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用台账编号
     */
    public void setUrationCode(String urationCode) {
        this.urationCode = urationCode;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 系统类型
     */
    @Column(name = "TYPE")
    public String getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             系统类型
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 系统代码
     */
    @Column(name = "SYSCODE")
    public String getSyscode() {
        return this.syscode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             系统代码
     */
    public void setSyscode(String syscode) {
        this.syscode = syscode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务名称
     */
    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return this.businessName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             业务名称
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 关联主业务主码
     */
    @Column(name = "BILL_ID")
    public String getBillId() {
        return this.billId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             关联主业务主码
     */
    public void setBillId(String billId) {
        this.billId = billId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 关联主业务主码
     */
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return this.billNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             关联主业务主码
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用授信类型
     */
    @Column(name = "RAT_TYPE")
    public String getRatType() {
        return this.ratType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用授信类型
     */
    public void setRatType(String ratType) {
        this.ratType = ratType;
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
     *             授信编号
     */
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信分配编号
     */
    @Column(name = "DIS_CODE")
    public String getDisCode() {
        return this.disCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信分配编号
     */
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             币别
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 占用授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             占用授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 占用到期日期
     */
    @Column(name = "SCHEDULEENDDATE")
    public java.util.Date getScheduleenddate() {
        return this.scheduleenddate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             占用到期日期
     */
    public void setScheduleenddate(java.util.Date scheduleenddate) {
        this.scheduleenddate = scheduleenddate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 金额
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 敞口额度
     */
    @Column(name = "OPEN_MONEY")
    public BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             敞口额度
     */
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 拆算前金额
     */
    @Column(name = "PRE_MONEY")
    public BigDecimal getPreMoney() {
        return this.preMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             拆算前金额
     */
    public void setPreMoney(BigDecimal preMoney) {
        this.preMoney = preMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 折算前额度
     */
    @Column(name = "CONVERT_PRE_MONEY")
    public BigDecimal getConvertPreMoney() {
        return this.convertPreMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             折算前额度
     */
    public void setConvertPreMoney(BigDecimal convertPreMoney) {
        this.convertPreMoney = convertPreMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 汇率
     */
    @Column(name = "EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return this.exchangeRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             汇率
     */
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 初始币别
     */
    @Column(name = "INIT_CUR_CODE")
    public String getInitCurCode() {
        return this.initCurCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             初始币别
     */
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
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
     *             备注
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
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 开始日期
     */
    @Column(name = "BEGIN_DATE")
    public java.util.Date getBeginDate() {
        return this.beginDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             开始日期
     */
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 结束日期
     */
    @Column(name = "END_DATE")
    public java.util.Date getEndDate() {
        return this.endDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             结束日期
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
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
     *             创建人名称
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
     *             创建人登录名称
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
     *             创建日期
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
     *             更新人名称
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
     *             更新人登录名称
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
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "BILL_MONEY")
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    @Column(name = "BOND_CURID")
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    @Column(name = "BOND_MONEY")
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    @Column(name = "BILL_NAME")
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    @Column(name = "IS_OPEN_RAT")
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 保证金比例
     */
    @Column(name = "ASSUREPERCENT")
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 保证金比例
     */
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信额度类型
     */
    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 授信额度类型
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
