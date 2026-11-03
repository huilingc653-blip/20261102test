package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolBail
 * @Description: 保证金信息Entity
 * @author wangzy
 * @date 2020/1/10 18:07
 *
 */
@Entity
@Table(name = "AB_POOL_BAIL", schema = "")
@SuppressWarnings("serial")
public class AbPoolBail extends IdEntity  implements java.io.Serializable {
    /**单位Id**/
    private String corpId;
    /**票据池Id**/
    private String bpId;
    /**保证金账户**/
    private String bankAcc;
    /**保证金金额**/
    private BigDecimal bailMoney;
    /**保证金日期**/
    private Date bailDate;
    /**状态**/
    private String validSign;
    /**创建时间**/
    private Date createTime;
    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="BP_ID")
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    @Column(name ="BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    @Column(name ="BAIL_MONEY")
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    @Column(name ="BAIL_DATE")
    public Date getBailDate() {
        return bailDate;
    }
    public void setBailDate(Date bailDate) {
        this.bailDate = bailDate;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
