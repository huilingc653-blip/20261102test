package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_BANK_ACC_DTL
 * @date 2019-5-13
 */
@Entity
@Table(name = "FC_BANK_ACC_DTL", schema = "")
@SuppressWarnings("serial")
public class FcBankAccDtl extends IdEntity implements java.io.Serializable {
    /**
     * 流水ID
     **/
    private String dtlId;
    /**
     * 所属网点资金池
     **/
    private String poolId;
    /**
     * 所属单位
     **/
    private String corpId;
    /**
     * 流水号
     **/
    private String orderNo;
    /**
     * 凭证号
     **/
    private String voucherNo;
    /**
     * 全局I
     **/
    private String busGlobalId;
    /**
     * 记账人
     **/
    private String tallyCode;
    /***记账时间*/
    private Date tallyDate;
    /**
     * 复核人
     **/
    private String checkCode;
    /**
     * 复核时间
     **/
    private Date checkDate;
    /**
     * 记账状态
     * 0：待记账（改账打回）;1：记账复核通过;2：记账无效;3：记账待复核;4：记账打回;5：记账暂存;8：自动记账失败
     **/
    private String tallyStatus;
    @Column(name = "DTL_ID")
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    @Column(name = "POOL_ID")
    public String getPoolId() {
        return poolId;
    }
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    @Column(name = "BUS_GLOBAL_ID")
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    @Column(name = "TALLY_CODE")
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
    @Column(name = "TALLY_DATE")
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    @Column(name = "CHECK_CODE")
    public String getCheckCode() {
        return checkCode;
    }
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
    @Column(name = "CHECK_DATE")
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    @Column(name = "TALLY_STATUS")
    public String getTallyStatus() {
        return tallyStatus;
    }
    public void setTallyStatus(String tallyStatus) {
        this.tallyStatus = tallyStatus;
    }
}
