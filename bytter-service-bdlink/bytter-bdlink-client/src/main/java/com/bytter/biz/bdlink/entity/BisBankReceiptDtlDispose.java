package com.bytter.biz.bdlink.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@ApiModel("BisBankReceiptDtlDispose")
@Entity
@Table(name = "BIS_BANK_RECEIPT_DTL_DISPOSE", schema = "")
public class BisBankReceiptDtlDispose extends IdEntity implements java.io.Serializable {
    /**
     * 银行类别id
     */
    @ApiModelProperty("银行类别id")
    private String bankTypeId;

    /**
     * 是否启用0：禁用，1启用
     */
    @ApiModelProperty("是否启用0：禁用，1启用")
    private String isEnable;

    /**
     * 币别id
     */
    @ApiModelProperty("币别id")
    private String curId;

    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    private String transTime;

    /**
     * 回单编号
     */
    @ApiModelProperty("回单编号")
    private String receiptNo;

    /**
     * 验证编码
     */
    @ApiModelProperty("验证编码")
    private String checkCode;

    /**
     * 收支标志
     */
    @ApiModelProperty("收支标志")
    private String cdSign;

    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    private String transAmt;

    /**
     * 付款账号
     */
    @ApiModelProperty("付款账号")
    private String payAccNo;

    /**
     * 付款户名
     */
    @ApiModelProperty("付款户名")
    private String payAccName;

    /**
     * 付款开户行
     */
    @ApiModelProperty("付款开户行")
    private String payBranchName;

    /**
     * 收款账号
     */
    @ApiModelProperty("收款账号")
    private String payeeAccNo;

    /**
     * 收款户名
     */
    @ApiModelProperty("收款户名")
    private String payeeAccName;

    /**
     * 收款开户行
     */
    @ApiModelProperty("收款开户行")
    private String payeeBracnName;

    /**
     * 用途
     */
    @ApiModelProperty("用途")
    private String usefor;

    /**
     * 备注1
     */
    @ApiModelProperty("备注1")
    private String remark1;

    /**
     * 备注2
     */
    @ApiModelProperty("备注2")
    private String remark2;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createDate;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateDate;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;
    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name = "IS_ENABLE")
    public String getIsEnable() {
        return isEnable;
    }
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "TRANS_TIME")
    public String getTransTime() {
        return transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
    @Column(name = "RECEIPT_NO")
    public String getReceiptNo() {
        return receiptNo;
    }
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
    @Column(name = "CHECK_CODE")
    public String getCheckCode() {
        return checkCode;
    }
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
    @Column(name = "CD_SIGN")
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    @Column(name = "TRANS_AMT")
    public String getTransAmt() {
        return transAmt;
    }
    public void setTransAmt(String transAmt) {
        this.transAmt = transAmt;
    }
    @Column(name = "PAY_ACC_NO")
    public String getPayAccNo() {
        return payAccNo;
    }
    public void setPayAccNo(String payAccNo) {
        this.payAccNo = payAccNo;
    }
    @Column(name = "PAY_ACC_NAME")
    public String getPayAccName() {
        return payAccName;
    }
    public void setPayAccName(String payAccName) {
        this.payAccName = payAccName;
    }
    @Column(name = "PAY_BRANCH_NAME")
    public String getPayBranchName() {
        return payBranchName;
    }
    public void setPayBranchName(String payBranchName) {
        this.payBranchName = payBranchName;
    }
    @Column(name = "PAYEE_ACC_NO")
    public String getPayeeAccNo() {
        return payeeAccNo;
    }
    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }
    @Column(name = "PAYEE_ACC_NAME")
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    @Column(name = "PAYEE_BRACN_NAME")
    public String getPayeeBracnName() {
        return payeeBracnName;
    }
    public void setPayeeBracnName(String payeeBracnName) {
        this.payeeBracnName = payeeBracnName;
    }
    @Column(name = "USEFOR")
    public String getUsefor() {
        return usefor;
    }
    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }
    @Column(name = "REMARK1")
    public String getRemark1() {
        return remark1;
    }
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    @Column(name = "REMARK2")
    public String getRemark2() {
        return remark2;
    }
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
