package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.entity.BisPayPolicyPayee;
import com.bytter.biz.netbank.entity.BisPayPolicySort;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: VO
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@ApiModel("自动支付策略表")
public class BisPayPolicyVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;
    /**
     * 单位
     */
    @ApiModelProperty("单位ID")
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpId;
    @ApiModelProperty("单位名称")
    private String corpName;
    /**
     * 调拨策略代码
     */
    @ApiModelProperty("调拨策略代码")
    @BtCompositeSearch(fieldName = "b.policy_Code")
    private String policyCode;
    /**
     * 调拨策略名称
     */
    @ApiModelProperty("调拨策略名称")
    private String policyName;
    /**
     * 币别
     */
    @ApiModelProperty("币别ID")
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "b.cur_Id")
    private String curId;
    @ApiModelProperty("币别")
    private String curName;
    /**
     * 范围1  10＞、11＞＝、20＜、21＜＝
     */
    @ApiModelProperty("范围1  10＞、11＞＝、20＜、21＜＝")
    private String range1;
    /**
     * 范围值1
     */
    @ApiModelProperty("范围值1")
    private BigDecimal rangeVal1;
    /**
     * 范围拼接 1-并且  2-或者
     */
    @ApiModelProperty("范围拼接 1-并且  2-或者")
    private String rangeJoin;
    /**
     * 范围2  10＞、11＞＝、20＜、21＜＝
     */
    @ApiModelProperty("范围2  10＞、11＞＝、20＜、21＜＝")
    private String range2;
    /**
     * 范围值2
     */
    @ApiModelProperty("范围值2")
    private BigDecimal rangeVal2;
    /**
     * 范围值2
     */
    @ApiModelProperty("范围值2")
    private BigDecimal payTotal;
    /**
     * 是否启用自动支付 0-否  1-是
     */
    @ApiModelProperty("是否启用自动支付 0-否  1-是")
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;

    @ApiModelProperty("是否启用自动支付")
    private String validSignName;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty("创建人登录名称")
    private String createBy;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    @ApiModelProperty("更新人登录名称")
    private String updateBy;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    private Date updateDate;
    /**
     * 业务类别
     */
    @ApiModelProperty("业务类别ID")
    private String busTypeId;
    @ApiModelProperty("业务类别")
    private String busTypeName;
    /**
     * 共享业务类别
     */
    @ApiModelProperty("共享业务类别ID")
    private String shareBusTypeId;
    @ApiModelProperty("共享业务类别")
    private String shareBusTypeName;
    /**
     * 支付收款人
     */
    @ApiModelProperty("支付收款人ID")
    private String payeeId;
    @ApiModelProperty("支付收款人名称")
    private String payeeName;
    /**
     * 不支付收款人
     */
    @ApiModelProperty("不支付收款人")
    private String noPayeeAcc;
    @ApiModelProperty("不支付收款人名称")
    private String noPayeeName;
    /**
     * 支付账号
     */
    @ApiModelProperty("支付账号")
    private String payerAccId;
    /**
     * 优先策略
     */
    @ApiModelProperty("优先策略")
    private List<BisPayPolicySort> policyTypeList = new ArrayList<>();
    /**
     * 优先策略
     */
    @ApiModelProperty("优先策略")
    private String sort;
    @ApiModelProperty("策略类型")
    private String policyType;
    /**
     * 是否跳过非工作日；1是，0否
     */
    @ApiModelProperty("是否跳过非工作日；1是，0否")
    @Dict(dictGroup = "sf_yn", inject = "isWorkDayName")
    private String isWorkDay;
    private String isWorkDayName;
    /**
     * 不支付用途
     */
    @ApiModelProperty("不支付用途")
    private String noPayPurpose;
    /**
     * 不支付收款人集合
     */
    @ApiModelProperty("不支付收款人集合")
    private List<BisPayPolicyPayee> noPayeeList;
    /**
     * 不支付收款人是否更新标志，0未更新，1为已更新
     */
    @ApiModelProperty("不支付收款人是否更新标志，0未更新，1为已更新")
    private String isEdit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
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

    public String getRange1() {
        return range1;
    }

    public void setRange1(String range1) {
        this.range1 = range1;
    }

    public BigDecimal getRangeVal1() {
        return rangeVal1;
    }

    public void setRangeVal1(BigDecimal rangeVal1) {
        this.rangeVal1 = rangeVal1;
    }

    public String getRangeJoin() {
        return rangeJoin;
    }

    public void setRangeJoin(String rangeJoin) {
        this.rangeJoin = rangeJoin;
    }

    public String getRange2() {
        return range2;
    }

    public void setRange2(String range2) {
        this.range2 = range2;
    }

    public BigDecimal getRangeVal2() {
        return rangeVal2;
    }

    public void setRangeVal2(BigDecimal rangeVal2) {
        this.rangeVal2 = rangeVal2;
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
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

    public String getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getShareBusTypeId() {
        return shareBusTypeId;
    }

    public void setShareBusTypeId(String shareBusTypeId) {
        this.shareBusTypeId = shareBusTypeId;
    }

    public String getShareBusTypeName() {
        return shareBusTypeName;
    }

    public void setShareBusTypeName(String shareBusTypeName) {
        this.shareBusTypeName = shareBusTypeName;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getNoPayeeAcc() {
        return noPayeeAcc;
    }

    public void setNoPayeeAcc(String noPayeeAcc) {
        this.noPayeeAcc = noPayeeAcc;
    }

    public String getNoPayeeName() {
        return noPayeeName;
    }

    public void setNoPayeeName(String noPayeeName) {
        this.noPayeeName = noPayeeName;
    }

    public String getPayerAccId() {
        return payerAccId;
    }

    public void setPayerAccId(String payerAccId) {
        this.payerAccId = payerAccId;
    }

    public List<BisPayPolicySort> getPolicyTypeList() {
        return policyTypeList;
    }

    public void setPolicyTypeList(List<BisPayPolicySort> policyTypeList) {
        this.policyTypeList = policyTypeList;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getIsWorkDay() {
        return isWorkDay;
    }

    public void setIsWorkDay(String isWorkDay) {
        this.isWorkDay = isWorkDay;
    }

    public String getNoPayPurpose() {
        return noPayPurpose;
    }

    public void setNoPayPurpose(String noPayPurpose) {
        this.noPayPurpose = noPayPurpose;
    }

    public List<BisPayPolicyPayee> getNoPayeeList() {
        return noPayeeList;
    }

    public void setNoPayeeList(List<BisPayPolicyPayee> noPayeeList) {
        this.noPayeeList = noPayeeList;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getIsWorkDayName() {
        return isWorkDayName;
    }

    public void setIsWorkDayName(String isWorkDayName) {
        this.isWorkDayName = isWorkDayName;
    }
}
