package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.io.Serializable;
import java.util.Date;

public class BisBankReceiptDtlDisposeVO implements Serializable {
    @BtCompositeSearch(fieldName = "d.id")

    private String id;

    /**
     * 银行类别id
     */
    @BtCompositeSearch(fieldName = "d.bank_type_id")

    private String bankTypeId;

    @BtCompositeSearch(fieldName = "d.bank_type_id")

    private String bankTypeName;

    /**
     * 是否启用0：禁用，1启用
     */
    @BtCompositeSearch(fieldName = "d.is_enable")

    private String isEnable;

    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = "d.cur_id")

    private String curId;

    /**
     * 交易时间
     */
    @BtCompositeSearch(fieldName = "d.trans_time")

    private String transTime;

    /**
     * 回单编号
     */
    @BtCompositeSearch(fieldName = "d.receipt_no")

    private String receiptNo;

    /**
     * 验证编码
     */
    @BtCompositeSearch(fieldName = "d.check_code")

    private String checkCode;

    /**
     * 收支标志0：收入，1支出
     */
    @BtCompositeSearch(fieldName = "d.cd_sign")

    private String cdSign;

    /**
     * 交易金额
     */
    @BtCompositeSearch(fieldName = "d.trans_amt")

    private String transAmt;

    /**
     * 付款账号
     */
    @BtCompositeSearch(fieldName = "d.pay_acc_no")

    private String payAccNo;

    /**
     * 付款户名
     */
    @BtCompositeSearch(fieldName = "d.pay_acc_name")

    private String payAccName;

    /**
     * 付款开户行
     */
    @BtCompositeSearch(fieldName = "d.pay_branch_name")

    private String payBranchName;

    /**
     * 收款账号
     */
    @BtCompositeSearch(fieldName = "d.payee_acc_no")

    private String payeeAccNo;

    /**
     * 收款户名
     */
    @BtCompositeSearch(fieldName = "d.payee_acc_name")

    private String payeeAccName;

    /**
     * 收款开户行
     */
    @BtCompositeSearch(fieldName = "d.payee_bracn_name")

    private String payeeBracnName;

    /**
     * 用途
     */
    @BtCompositeSearch(fieldName = "d.usefor")

    private String usefor;

    /**
     * 备注1
     */
    @BtCompositeSearch(fieldName = "d.remark1")

    private String remark1;

    /**
     * 备注2
     */
    @BtCompositeSearch(fieldName = "d.remark2")

    private String remark2;

    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "d.create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")

    private Date createDate;

    /**
     * 创建人
     */
    @BtCompositeSearch(fieldName = "d.create_by")

    private String createBy;

    /**
     * 更新时间
     */
    @BtCompositeSearch(fieldName = "d.update_date")
    @JsonFormat(pattern = "yyyy-MM-dd")

    private Date updateDate;

    /**
     * 更新人
     */
    @BtCompositeSearch(fieldName = "d.update_by")

    private String updateBy;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getIsEnable() {
        return isEnable;
    }
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getTransTime() {
        return transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
    public String getReceiptNo() {
        return receiptNo;
    }
    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
    public String getCheckCode() {
        return checkCode;
    }
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public String getTransAmt() {
        return transAmt;
    }
    public void setTransAmt(String transAmt) {
        this.transAmt = transAmt;
    }
    public String getPayAccNo() {
        return payAccNo;
    }
    public void setPayAccNo(String payAccNo) {
        this.payAccNo = payAccNo;
    }
    public String getPayAccName() {
        return payAccName;
    }
    public void setPayAccName(String payAccName) {
        this.payAccName = payAccName;
    }
    public String getPayBranchName() {
        return payBranchName;
    }
    public void setPayBranchName(String payBranchName) { this.payBranchName = payBranchName; }
    public String getPayeeAccNo() {
        return payeeAccNo;
    }
    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    public String getPayeeBracnName() {
        return payeeBracnName;
    }
    public void setPayeeBracnName(String payeeBracnName) { this.payeeBracnName = payeeBracnName; }
    public String getUsefor() {
        return usefor;
    }
    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }
    public String getRemark1() {
        return remark1;
    }
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    public String getRemark2() {
        return remark2;
    }
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
