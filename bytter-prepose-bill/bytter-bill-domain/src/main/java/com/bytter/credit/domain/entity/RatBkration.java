package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 授信额度录入表
 * @date 2017-07-21 14:35:43
 */
@Entity
@Table(name = "RAT_BKRATION", schema = "")
@SuppressWarnings("serial")
public class RatBkration extends IdEntity implements java.io.Serializable {
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 网点编号
     */
    private String netCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 授信种类
     */
    private String ratType;
    /**
     * 银行类型
     */
    private String bankType;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行类别名称
     */
    private String bankTypeName;
    /**
     * 敞口授信
     */
    private String openSign;
    /**
     * 授信模式
     */
    private String ratMode;
    /**
     * 授信币种
     */
    private String curCode;
    /**
     * 授信开始时间
     */
    private Date begindate;
    /**
     * 授信结束时间
     */
    private Date enddate;
    /**
     * 授信额度
     */
    private java.math.BigDecimal money;
    /**
     * 敞口额度
     */
    private java.math.BigDecimal openmoney;
    /**
     * 额度类型
     */
    private String type;
    /**
     * 通过时间
     */
    private Date passDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位代码
     */
    private String corpNo;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 授信合同号
     */
    private String contractNo;
    /**
     * 担保类型
     */
    private String guaType;
    /**
     * 担保单位
     */
    private String guaCorpId;
    /**
     * 担保合同
     */
    private String guaContract;
    /**
     * 附件路径
     */
    private String attachfilepath;
    /**
     * 合同附件
     */
    private String contractAttachfilepath;
    /**
     * 额度是否包含保证金
     */
    private String includeBailmoney;
    /**
     * 到期日30到90天是否发生消息
     */
    private String sendSign;

    /**
     * 到期日小于等于30天是否发生消息
     */
    private String sendSign1;

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
    private Date createDate;
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
    private Date updateDate;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /**
     * 担保备注
     **/
    private String guaranteeRmk;
    /**
     * 合同挂接ID
     **/
    private String conId;
    /**
     * 合同类型ID
     **/
    private String conTypeId;
    /**
     * 结转授信编号(存储为id)
     **/
    private String oldRatCode;
    /**
     * 担保开始时间
     */
    private Date guaranteeBeginDate;
    /**
     * 担保结束时间
     */
    private Date guaranteeEndDate;

    @Column(name = "GUARANTEE_BEGIN_DATE")
    public Date getGuaranteeBeginDate() {
        return guaranteeBeginDate;
    }

    public void setGuaranteeBeginDate(Date guaranteeBeginDate) {
        this.guaranteeBeginDate = guaranteeBeginDate;
    }

    @Column(name = "GUARANTEE_END_DATE")
    public Date getGuaranteeEndDate() {
        return guaranteeEndDate;
    }

    public void setGuaranteeEndDate(Date guaranteeEndDate) {
        this.guaranteeEndDate = guaranteeEndDate;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 网点编号
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
     * @return: java.lang.String 网点编号
     */
    @Column(name = "NET_CODE")
    public String getNetCode() {
        return this.netCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 网点编号
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
     * @return: java.lang.String 银行类型
     */
    @Column(name = "BANK_TYPE")
    public String getBankType() {
        return this.bankType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 银行类型
     */
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 银行编号
     */
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return this.bankCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 银行编号
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 敞口授信
     */
    @Column(name = "OPEN_SIGN")
    public String getOpenSign() {
        return this.openSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 敞口授信
     */
    public void setOpenSign(String openSign) {
        this.openSign = openSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信模式
     */
    @Column(name = "RAT_MODE")
    public String getRatMode() {
        return this.ratMode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信模式
     */
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 授信币种
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信币种
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 授信开始时间
     */
    @Column(name = "BEGINDATE")
    public Date getBegindate() {
        return this.begindate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 授信开始时间
     */
    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 授信结束时间
     */
    @Column(name = "ENDDATE")
    public Date getEnddate() {
        return this.enddate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 授信结束时间
     */
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
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
     * @return: java.math.BigDecimal 敞口额度
     */
    @Column(name = "OPENMONEY")
    public java.math.BigDecimal getOpenmoney() {
        return this.openmoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 敞口额度
     */
    public void setOpenmoney(java.math.BigDecimal openmoney) {
        this.openmoney = openmoney;
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
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 通过时间
     */
    @Column(name = "PASS_DATE")
    public Date getPassDate() {
        return this.passDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 通过时间
     */
    public void setPassDate(Date passDate) {
        this.passDate = passDate;
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
     * @return: java.lang.String 授信合同号
     */
    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return this.contractNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 授信合同号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保类型
     */
    @Column(name = "GUA_TYPE")
    public String getGuaType() {
        return guaType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保类型
     */
    public void setGuaType(String guaType) {
        this.guaType = guaType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保单位
     */
    @Column(name = "GUA_CORP_ID")
    public String getGuaCorpId() {
        return this.guaCorpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保单位
     */
    public void setGuaCorpId(String guaCorpId) {
        this.guaCorpId = guaCorpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 担保合同
     */
    @Column(name = "GUA_CONTRACT")
    public String getGuaContract() {
        return this.guaContract;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 担保合同
     */
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 附件路径
     */
    @Column(name = "ATTACHFILEPATH")
    public String getAttachfilepath() {
        return this.attachfilepath;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 附件路径
     */
    public void setAttachfilepath(String attachfilepath) {
        this.attachfilepath = attachfilepath;
    }
    @Column(name = "CONTRACT_ATTACHFILEPATH")
    public String getContractAttachfilepath() {
        return contractAttachfilepath;
    }
    public void setContractAttachfilepath(String contractAttachfilepath) {
        this.contractAttachfilepath = contractAttachfilepath;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 额度是否包含保证金
     */
    @Column(name = "INCLUDE_BAILMONEY")
    public String getIncludeBailmoney() {
        return this.includeBailmoney;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 额度是否包含保证金
     */
    public void setIncludeBailmoney(String includeBailmoney) {
        this.includeBailmoney = includeBailmoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 发送标识
     */
    @Column(name = "SEND_SIGN")
    public String getSendSign() {
        return this.sendSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 发送标识
     */
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }


    @Column(name = "SEND_SIGN_1")
    public String getSendSign1() {
        return sendSign1;
    }

    public void setSendSign1(String sendSign1) {
        this.sendSign1 = sendSign1;
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
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(Date createDate) {
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
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否挂接保证金
     */
    @Column(name = "IS_HOOK_BOND")
    public String getIsHookBond() {
        return isHookBond;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否挂接保证金
     */
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 是否挂接担保
     */
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 是否挂接担保
     */
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUARANTEE_RMK")
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    @Column(name = "OLD_RAT_CODE")
    public String getOldRatCode() {
        return oldRatCode;
    }
    public void setOldRatCode(String oldRatCode) {
        this.oldRatCode = oldRatCode;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CORP_CODE")
    public String getCorpNo() {
        return corpNo;
    }
    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }
    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
}
