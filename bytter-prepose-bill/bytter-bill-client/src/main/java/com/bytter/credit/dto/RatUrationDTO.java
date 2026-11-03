package com.bytter.credit.dto;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 授信占用DTO
 * 
 * @author liyh
 *
 */
public class RatUrationDTO implements Serializable {
    /**
     * 占用ID，可回写给业务表
     */
    private String urationId;
    /**
     * 单位ID(必填)
     */
    private String corpId;
    /**
     * 授信编号(必填)
     */
    private String ratCode;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 授信品种
     */
    private String ratItemCode;
    private String ratItemName;
    /**
     * 系统授信品种类型
     */
    private String sysCode;
    /**
     * 业务模块代码(必填)
     */
    private String businessName;
    /**
     * 业务币别(必填)
     */
    private String curCode;
    /**
     * 合同号(手工占用时必填，外部挂接时选填)
     */
    private String contactNo;
    /**
     * 业务ID(必填)
     */
    private String billId;
    /**
     * 业务单号(选填)
     */
    private String billNo;
    /**
     * 占用额度(必填)
     */
    private BigDecimal money;
    /**
     * 保证金比例（选填，如果存在）
     */
    private BigDecimal bailRate;
    /**
     * 开始日期(必填)
     */
    private Date beginDate;
    /**
     * 结束日期(必填)
     */
    private Date endDate;
    /**
     * 占用明细 如果占用单项，不必填写，需填写itemCode 如果占用综合授信，则明细必须填写
     */
    private List<RatUrationDtlDTO> applyDtls;
    /**
     * 释放标识
     */
    private boolean releaseFlag = false;
    /**
     * 是否全部释放
     */
    private boolean allRelease = false;
    /**
     * 品种类型(不用填写)
     */
    private String itemFlag;
    /**
     * 获取操作用户
     */
    private String userId;
    /** 业务品种ID */
    private String billName;
    /** 业务品种名称 */
    private String raceName;
    /**
     * 业务金额（必填）
     */
    private BigDecimal billMoney;
    /** 保证金金额(必填) */
    private BigDecimal bondMoney;
    /** 保证金币种(必填) */
    @Currency(inject = "bondCurName")
    private String bondCurId;
    private String bondCurName;
    /** 是否启用授信敞口(必填，根据授信基础参数设置，需给默认值，1=启用，0=不启用) */
    private String isOpenRat;
    /**
     * 业务币种（手工占用时用此字段）
     */
    private String initCurCode;
    /**
     * 授信是否挂接担保
     */
    private String ratIsHangGua;
    /**
     * 状态
     */
    private Integer status;
    /** 是否释放不循环授信 */
    private boolean releaseNonRecycleRat;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 外模块挂接授信查看时使用
     */
    private String itemCode;
    /**
     * 外模块挂接授信查看时使用
     */
    private BigDecimal useMaxMoney;

    String isRmbRat;
    public String getUrationId() {
        return urationId;
    }
    public void setUrationId(String urationId) {
        this.urationId = urationId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getRatItemCode() {
        return ratItemCode;
    }
    public void setRatItemCode(String ratItemCode) {
        this.ratItemCode = ratItemCode;
    }
    public String getRatItemName() {
        return ratItemName;
    }
    public void setRatItemName(String ratItemName) {
        this.ratItemName = ratItemName;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getBailRate() {
        return bailRate;
    }
    public void setBailRate(BigDecimal bailRate) {
        this.bailRate = bailRate;
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
    public List<RatUrationDtlDTO> getApplyDtls() {
        return applyDtls;
    }
    public void setApplyDtls(List<RatUrationDtlDTO> applyDtls) {
        this.applyDtls = applyDtls;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public RatUrationDTO() {
    }
    public RatUrationDTO(String sysCode, String billId) {
        super();
        this.sysCode = sysCode;
        this.billId = billId;
    }
    public boolean isReleaseFlag() {
        return releaseFlag;
    }
    public void setReleaseFlag(boolean releaseFlag) {
        this.releaseFlag = releaseFlag;
    }
    public boolean isAllRelease() {
        return allRelease;
    }
    public void setAllRelease(boolean allRelease) {
        this.allRelease = allRelease;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
    public String getInitCurCode() {
        return initCurCode;
    }
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    public String getRatIsHangGua() {
        return ratIsHangGua;
    }
    public void setRatIsHangGua(String ratIsHangGua) {
        this.ratIsHangGua = ratIsHangGua;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public boolean isReleaseNonRecycleRat() {
        return releaseNonRecycleRat;
    }
    public void setReleaseNonRecycleRat(boolean releaseNonRecycleRat) {
        this.releaseNonRecycleRat = releaseNonRecycleRat;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public BigDecimal getUseMaxMoney() {
        return useMaxMoney;
    }
    public void setUseMaxMoney(BigDecimal useMaxMoney) {
        this.useMaxMoney = useMaxMoney;
    }

    public String getIsRmbRat() {
        return isRmbRat;
    }

    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
}
