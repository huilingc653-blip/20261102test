package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbBillSignStrategy
 * @Description: 自动签收策略实体
 * @author wangzy
 * @date 2020/7/13 16:25
 *
 */
@Entity
@Table(name = "AB_BILL_SIGN_STRATEGY", schema = "")
@SuppressWarnings("serial")
public class AbBillSignStrategy extends IdEntity implements java.io.Serializable{
    /**单位ID*/
    private String corpId;
    // 单位编码
    private String corpCode;
    // 单位名称
    private String corpName;
    /**策略代码code*/
    private String code;
    /**票面金额开始数值*/
    private BigDecimal beginNum;
    /**票面金额结束数值*/
    private BigDecimal endNum;
    /**开始数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
    private String beginOperator;
    /**结束数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
    private String endOperator;
    /**数值间运算符(01或者、02并且)*/
    private String numOperator;
    /**策略名称*/
    private String name;
    /**票据种类：10 银票；11 商票多个用*/
    private String billType;
    /**签收类型，字典（AB_BILL_SIGNERTYPE:提示承兑002,提示收票003,提示保证017,背书申请010,贴现赎回签收012,质押申请018,质押解除019,提示付款020,逾期付款021,追索同意清偿023）多个用,号隔开*/
    private String signType;
    /**不得转让标记：EM00 可再转让；EM01 不得转让*/
    private String bankendrsmtmk;
    /**有效标志:0否； 1是*/
    private String validSign;
    /**创建人ID*/
    private String createId;
    /**创建日期*/
    private Date createDate;
    /**创建人ID*/
    private String updateId;
    /**创建日期*/
    private Date updateDate;
    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    @Column(name ="CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name ="CODE")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name ="BEGIN_NUM")
    public BigDecimal getBeginNum() {
        return beginNum;
    }
    public void setBeginNum(BigDecimal beginNum) {
        this.beginNum = beginNum;
    }
    @Column(name ="END_NUM")
    public BigDecimal getEndNum() {
		return endNum;
	}
	public void setEndNum(BigDecimal endNum) {
		this.endNum = endNum;
	}
    @Column(name ="BEGIN_OPERATOR")
    public String getBeginOperator() {
        return beginOperator;
    }
	public void setBeginOperator(String beginOperator) {
        this.beginOperator = beginOperator;
    }
    @Column(name ="END_OPERATOR")
    public String getEndOperator() {
        return endOperator;
    }
    public void setEndOperator(String endOperator) {
        this.endOperator = endOperator;
    }
    @Column(name ="NUM_OPERATOR")
    public String getNumOperator() {
        return numOperator;
    }
    public void setNumOperator(String numOperator) {
        this.numOperator = numOperator;
    }
    @Column(name ="NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name ="BILL_TYPE")
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    @Column(name ="SIGN_TYPE")
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    @Column(name ="BANKENDRSMTMK")
    public String getBankendrsmtmk() {
        return bankendrsmtmk;
    }
    public void setBankendrsmtmk(String bankendrsmtmk) {
        this.bankendrsmtmk = bankendrsmtmk;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
