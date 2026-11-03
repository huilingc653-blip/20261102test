package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolBailDtl
 * @Description: 保证金流水Entity
 * @author wangzy
 * @date 2020/1/10 18:08
 *
 */
@Entity
@Table(name = "AB_POOL_BAIL_DTL", schema = "")
@SuppressWarnings("serial")
public class AbPoolBailDtl  extends IdEntity implements java.io.Serializable{
    /**保证金主表ID**/
    private String bailId;
    /**存取类型**/
    private String type;
    /**保证金额度**/
    private BigDecimal bailMoney;
    /**存取金额**/
    private BigDecimal money;
    /**存取账户**/
    private String oppAcc;
    /**存取账户名称**/
    private String oppAccName;
    /**存取账户行号**/
    private String oppBankNo;
    /**存取账户行名**/
    private String oppBankName;
    /**申请日期**/
    private Date appDate;
    /**指令状态  -3银行交易失败；-1发送失败；16待发送；18银行处理中；19银行交易成功**/
    private String optStatus;
    /**审批状态  -2已删除；-1打回；0 暂存；11审批中；95 审批通过**/
    private Integer status;
    /**创建人ID**/
    private String createId;
    /**创建日期**/
    private Date createTime;
    /**更新人ID**/
    private String updateId;
    /**更新时间**/
    private Date updateTime;
    @Column(name ="bail_id")
    public String getBailId() {
        return bailId;
    }
    public void setBailId(String bailId) {
        this.bailId = bailId;
    }
    @Column(name ="type")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name ="bail_money")
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    @Column(name ="money")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name ="opp_acc")
    public String getOppAcc() {
        return oppAcc;
    }
    public void setOppAcc(String oppAcc) {
        this.oppAcc = oppAcc;
    }
    @Column(name ="opp_acc_name")
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    @Column(name ="opp_bank_no")
    public String getOppBankNo() {
        return oppBankNo;
    }
    public void setOppBankNo(String oppBankNo) {
        this.oppBankNo = oppBankNo;
    }
    @Column(name ="opp_bank_name")
    public String getOppBankName() {
        return oppBankName;
    }
    public void setOppBankName(String oppBankName) {
        this.oppBankName = oppBankName;
    }
    @Column(name ="app_date")
    public Date getAppDate() {
        return appDate;
    }
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }
    @Column(name ="opt_status")
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    @Column(name ="status")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name ="create_id")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name ="create_time")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name ="update_id")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name ="update_time")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
