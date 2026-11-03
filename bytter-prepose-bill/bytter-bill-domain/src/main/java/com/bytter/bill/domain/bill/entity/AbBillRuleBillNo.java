package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "AB_BILL_RULEBILLNO", schema = "")
public class AbBillRuleBillNo extends IdEntity implements java.io.Serializable {
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 票号字段
     */
    private String billNumField;
    /**
     * 匹配关系
     */
    private String relation;
    /**
     * 分隔符
     */
    private String separated;
    /**
     * 有效标志
     */
    private String mark;
    /**是否汇总认领流水 0-否 1-是*/
    private String isSummaryClaimBill;

    @Column(name ="IS_SUMMARY_CLAIM_BILL")
    public String getIsSummaryClaimBill() {
        return isSummaryClaimBill;
    }

    public void setIsSummaryClaimBill(String isSummaryClaimBill) {
        this.isSummaryClaimBill = isSummaryClaimBill;
    }

    @Column(name ="BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name ="BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name ="BUSTYPE")
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name ="BILL_NUMBER_FIELD")
    public String getBillNumField() {
        return billNumField;
    }
    public void setBillNumField(String billNumField) {
        this.billNumField = billNumField;
    }
    @Column(name ="RELATION_MATCHING")
    public String getRelation() {
        return relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    @Column(name ="BILLNO_SEPARATED")
    public String getSeparated() {
        return separated;
    }
    public void setSeparated(String separated) {
        this.separated = separated;
    }
    @Column(name ="VALID_MARK")
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
}
