package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 授信额度统计bean
 *
 * @author liyh
 *
 */
public class RatUrationDisBean implements Serializable {
    /**
     * 授信ID
     */
    private String id;
    /**
     * 授信ID
     */
    private String ratCode;
    /**
     * 分配ID
     */
    private String disCode;
    /**
     * 授信编号
     */
    private String ratNo;
    /** 单位代码 */
//    @Corp(inject = "corpName") TODO 不属于本域字段
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 单位代码 */
    private String corpNo;
    /** 授信种类 */
    private String ratType;
    /** 银行类型 */
    private String bankType;
    /** 银行类型名称 */
    private String bankTypeName;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 币别 */
    private String curCode;
    private String curName;
    /** 授信总额度 **/
    private BigDecimal money;
    /** 占用额度 **/
    private BigDecimal useMoney;
    /** 在审占用额度 **/
    private BigDecimal approveMoney;
    /** 剩余额度 **/
    private BigDecimal leftMoney;
    /** 可用额度 **/
    private BigDecimal overPlusMoney;
    /** 授信开始日期 **/
    private Date beginDate;
    /** 授信结束日期 **/
    private Date endDate;
    /**
     * 分配方式
     */
    private String disWay;
    // 授信模式
    private String ratMode;
    // 冻结额度
    private BigDecimal freezeMoney;
    // 追加在途额度
    private BigDecimal addMoney;
    // 虚拟占用额度
    private BigDecimal virtualUration;
    // 虚拟调剂额度
    private BigDecimal virtualRelief;
    // 虚拟占用额度
    private BigDecimal reliefAndUrationMoney;
    /**
     * 授信品种
     */
    private String itemFlag;
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    private String itemName;
    private Integer itemOrder;
    // 综合使用标识
    private String compSign;
    // 最高额度
    private BigDecimal maxMoney;
    // 最高占用额度
    private BigDecimal useMaxMoney;
    // 最高冻结额度
    private BigDecimal freezeMaxMoney;
    // 最高追加在途额度
    private BigDecimal addMaxMoney;
    // 虚拟占用的最高额度
    private BigDecimal virtualMaxMoney;
    // 虚拟在途占用的最高额度
    private BigDecimal virtualUrationMaxMoney;
    // 授信占用比列
    private double ratOccupyRatio;
    // 保证金比例
    private BigDecimal assurepercent;
    /** 是否挂接担保 */
    private String isHangGuarantee;
    /** 授信方式 */
    private String creditType;
    /** 状态 */
    private Integer status;

    /**授信合同号*/
    private String contractNo;

    /**授信品种类型id*/
    private String ritCode;

    public String getRitCode() {
        return ritCode;
    }

    public void setRitCode(String ritCode) {
        this.ritCode = ritCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
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
    public String getCorpNo() {
        return corpNo;
    }
    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getUseMoney() {
        return useMoney;
    }
    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }
    public BigDecimal getApproveMoney() {
        return approveMoney;
    }
    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
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
    public String getDisWay() {
        return disWay;
    }
    public void setDisWay(String disWay) {
        this.disWay = disWay;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    public BigDecimal getAddMoney() {
        return addMoney;
    }
    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }
    public BigDecimal getOverPlusMoney() {
        return overPlusMoney;
    }
    public void setOverPlusMoney(BigDecimal overPlusMoney) {
        this.overPlusMoney = overPlusMoney;
    }
    public BigDecimal getVirtualUration() {
        return virtualUration;
    }
    public void setVirtualUration(BigDecimal virtualUration) {
        this.virtualUration = virtualUration;
    }
    public BigDecimal getVirtualRelief() {
        if (EmptyUtils.isEmpty(virtualRelief)) {
            virtualRelief = BigDecimal.ZERO;
        }
        return virtualRelief;
    }
    public void setVirtualRelief(BigDecimal virtualRelief) {
        this.virtualRelief = virtualRelief;
    }
    public BigDecimal getReliefAndUrationMoney() {
        if (EmptyUtils.isEmpty(reliefAndUrationMoney)) {
            reliefAndUrationMoney = BigDecimal.ZERO;
        }
        return reliefAndUrationMoney;
    }
    public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
        this.reliefAndUrationMoney = reliefAndUrationMoney;
    }
    public String getRatNo() {
        return ratNo;
    }
    public void setRatNo(String ratNo) {
        this.ratNo = ratNo;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public BigDecimal getUseMaxMoney() {
        if (null == this.useMaxMoney) {
            this.useMaxMoney = BigDecimal.ZERO;
        }
        return useMaxMoney;
    }
    public void setUseMaxMoney(BigDecimal useMaxMoney) {
        this.useMaxMoney = useMaxMoney;
    }
    public BigDecimal getFreezeMaxMoney() {
        if (null == this.freezeMaxMoney) {
            this.freezeMaxMoney = BigDecimal.ZERO;
        }
        return freezeMaxMoney;
    }
    public void setFreezeMaxMoney(BigDecimal freezeMaxMoney) {
        this.freezeMaxMoney = freezeMaxMoney;
    }
    public BigDecimal getAddMaxMoney() {
        return addMaxMoney;
    }
    public void setAddMaxMoney(BigDecimal addMaxMoney) {
        this.addMaxMoney = addMaxMoney;
    }
    public BigDecimal getVirtualMaxMoney() {
        if (null == this.virtualMaxMoney) {
            this.virtualMaxMoney = BigDecimal.ZERO;
        }
        return virtualMaxMoney;
    }
    public void setVirtualMaxMoney(BigDecimal virtualMaxMoney) {
        this.virtualMaxMoney = virtualMaxMoney;
    }
    public BigDecimal getVirtualUrationMaxMoney() {
        if (null == this.virtualUrationMaxMoney) {
            this.virtualUrationMaxMoney = BigDecimal.ZERO;
        }
        return virtualUrationMaxMoney;
    }
    public void setVirtualUrationMaxMoney(BigDecimal virtualUrationMaxMoney) {
        this.virtualUrationMaxMoney = virtualUrationMaxMoney;
    }
    public double getRatOccupyRatio() {
        return ratOccupyRatio;
    }
    public void setRatOccupyRatio(double ratOccupyRatio) {
        this.ratOccupyRatio = ratOccupyRatio;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
