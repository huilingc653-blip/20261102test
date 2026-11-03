package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolIntoRepeal
 * @Description: 入池撤销表
 * @author zhenghj
 * @date 2021/11/08
 *
 */
@Entity
@Table(name = "AB_POOL_INTO_REPEAL", schema = "")
@SuppressWarnings("serial")
public class AbPoolIntoRepeal extends IdEntity implements java.io.Serializable  {
    /**
     * 票据ID编码
     */
    private String billIdCode;
    /**
     * 票据流水表id
     */
    private String aid;
    /**
     * 从表ID
     */
    private String vvId;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 业务类型：8032 入池撤销
     */
    private String briefId;
    /**
     * 申请单号
     */
    private String appNo;
    /**
     * 票据号码
     */
    private String origNo;
    /**
     * 状态：1启用、0在途、2作废 
     */
    private String validSign;
    
    /**
     * 创建人名称
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 票据类型
     */
    private String billType;
    /** 票据金额  */
    private BigDecimal billMoney;
    /** 出票日期 start_date date */
    private Date startDate;
    /** 到期日期 end_date date */
    private Date endDate;
    /** 出票人 payer varchar2(240) */
    private String payer;
    /** 出票人类型 payerType char(4)*/
    private String payerType;
    /** 出票人组织机构代码 char(20)*/
    private String payerCode;
    /** 出票人账号 payeracc varchar2(30) */
    private String payeracc;
    /** 出票人开户银行名称 payer_bank_name varchar2(240) */
    private String payerBankName;
    /** 出票人开户银行行号 payerbankno varchar2(30) */
    private String payerbankno;
    /** 收票人 payee varchar2(240) 240 */
    private String payee;
    /** 收票人类型 payeeType  char(4) */
    private String payeeType;
    /** 收票人组织机构代码 char(20) */
    private String payeeCode;
    /** 收票人账号 payeeacc varchar2(30) */
    private String payeeacc;
    /** 收票人开户银行名称 payee_bank_name varchar2(240) */
    private String payeeBankName;
    /** 收票人开户银行行号 payeebankno varchar2(30) */
    private String payeebankno;
     /** 承兑人类型 accpType char(4)*/
    private String accpType;
    /** 承兑人 acceptor varchar2(240) */
    private String acceptor;
    /** 承兑人组织机构代码 char(20)*/
    private String acceptorCode;
    /** 承兑人账号 acceptoracc varchar2(30) */
    private String acceptoracc;
    /** 承兑人开户行行号 acceptorbankno varchar2(30) */
    private String acceptorbankno;
    /** 承兑人开户行名称 acceptor_bank_name varchar2(240) */
    private String acceptorBankName;
    /**
     * 出质人类型 char(4)
     */
    private String plgrTyp;
    /**
     * 出质人名称 varchar(122)
     */
    private String plgrNam;
    /**
     * 出质人组织机构代码 char(20)
     */
    private String plgrCod;
    /**
     * 出质人账号 varchar(32)
     */
    private String plgrAccNo;
    /**
     * 出质人开户行行号 varchar(20)
     */
    private String plgrBnm;
    /**
     * 质权人类型  char(4)
     */
    private String plgeTyp;
    /**
     * 质权人名称 varchar(122)
     */
    private String plgeNam;
    /**
     * 质权人机构号 char(6)
     */
    private String plgeInstID;
    /**
     * 质权人组织机构代码 char(20)
     */
    private String plgeCod;
    /**
     * 质权人账号 varchar(32)
     */
    private String plgeAccNo;
    /**
     * 质权人开户行行号 varchar(20)
     */
    private String plgeBnm;
    /**
     *-票据质押价值 decimal(15,2)
     */
    private String billPlgVal;
    /**
     * 票据状态 char(6) 可空
     */
    private String billStatus;
    /**
     * 占用同业额度标识 char(1)
     */
    private String ocpSmidyLmtFlag;
    /**
     * 最高额质押合同编号 varchar(90)
     */
    private String highLmtPlgCtrID;
    /**
     * 最高额质押流水号 varchar(36)
     */
    private String highLmtPlgSrl;
    /**
     * -出入池状态 char(2)
     */
    private String poolSttOfIO;
    /**
     * 电子票据防重标识 char(1) 0:关闭,1:开启
     */
    private String avdRepeatFlag;
    /**
     * 交易日期
     */
    private Date transDate;
    /**
     * 是否允许客户自助出池票据 char(1) 1:是,0:否
     */
    private Date isAlwCstOutPool;
    @Column(name = "BILLID_CODE", nullable = true, length = 32)
    public String getBillIdCode() {
        return billIdCode;
    }
    public void setBillIdCode(String billIdCode) {
        this.billIdCode = billIdCode;
    }
    @Column(name = "AID", nullable = true, length = 32)
    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }
    @Column(name = "VV_ID", nullable = true, length = 32)
    public String getVvId() {
        return vvId;
    }
    @Column(name = "BP_ID", nullable = true, length = 32)
    public String getBpId() {
        return bpId;
    }
    @Column(name = "BRIEF_ID", nullable = true, length = 4)
    public String getBriefId() {
        return briefId;
    }
    @Column(name = "APP_NO", nullable = true, length = 50)
    public String getAppNo() {
        return appNo;
    }
    @Column(name = "ORIG_NO", nullable = true, length = 50)
    public String getOrigNo() {
        return origNo;
    }
    public void setVvId(String vvId) {
        this.vvId = vvId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
    @Column(name = "BILL_TYPE")
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    @Column(name = "BILL_MONEY")
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "end_Date")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "PAYER")
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    @Column(name = "PAYER_TYPE")
    public String getPayerType() {
        return payerType;
    }
    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }
    @Column(name = "PAYER_CODE")
    public String getPayerCode() {
        return payerCode;
    }
    public void setPayerCode(String payerCode) {
        this.payerCode = payerCode;
    }
    @Column(name = "PAYER_ACC")
    public String getPayeracc() {
        return payeracc;
    }
    public void setPayeracc(String payeracc) {
        this.payeracc = payeracc;
    }
    @Column(name = "PAYER_BANKNAME")
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    @Column(name = "PAYER_BANKNO")
    public String getPayerbankno() {
        return payerbankno;
    }
    public void setPayerbankno(String payerbankno) {
        this.payerbankno = payerbankno;
    }
    @Column(name = "PAYEE")
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    @Column(name = "PAYEE_TYPE")
    public String getPayeeType() {
        return payeeType;
    }
    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }
    @Column(name = "PAYEE_CODE")
    public String getPayeeCode() {
        return payeeCode;
    }
    public void setPayeeCode(String payeeCode) {
        this.payeeCode = payeeCode;
    }
    @Column(name = "PAYEE_ACC")
    public String getPayeeacc() {
        return payeeacc;
    }
    public void setPayeeacc(String payeeacc) {
        this.payeeacc = payeeacc;
    }
    @Column(name = "PAYEE_BANKNAME")
    public String getPayeeBankName() {
        return payeeBankName;
    }
    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }
    @Column(name = "PAYEE_BANKNO")
    public String getPayeebankno() {
        return payeebankno;
    }
    public void setPayeebankno(String payeebankno) {
        this.payeebankno = payeebankno;
    }
    @Column(name = "ACCP_TYPE")
    public String getAccpType() {
        return accpType;
    }
    public void setAccpType(String accpType) {
        this.accpType = accpType;
    }
    @Column(name = "ACCEPTOR")
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    @Column(name = "ACCEPTOR_CODE")
    public String getAcceptorCode() {
        return acceptorCode;
    }
    public void setAcceptorCode(String acceptorCode) {
        this.acceptorCode = acceptorCode;
    }
    @Column(name = "ACCEPTOR_ACC")
    public String getAcceptoracc() {
        return acceptoracc;
    }
    public void setAcceptoracc(String acceptoracc) {
        this.acceptoracc = acceptoracc;
    }
    @Column(name = "ACCEPTOR_BANKNO")
    public String getAcceptorbankno() {
        return acceptorbankno;
    }
    public void setAcceptorbankno(String acceptorbankno) {
        this.acceptorbankno = acceptorbankno;
    }
    @Column(name = "ACCEPTOR_BANKNAME")
    public String getAcceptorBankName() {
        return acceptorBankName;
    }
    public void setAcceptorBankName(String acceptorBankName) {
        this.acceptorBankName = acceptorBankName;
    }
    @Column(name = "PLGR_TYP")
    public String getPlgrTyp() {
        return plgrTyp;
    }
    public void setPlgrTyp(String plgrTyp) {
        this.plgrTyp = plgrTyp;
    }
    @Column(name = "PLGR_NAM")
    public String getPlgrNam() {
        return plgrNam;
    }
    public void setPlgrNam(String plgrNam) {
        this.plgrNam = plgrNam;
    }
    @Column(name = "PLGR_COD")
    public String getPlgrCod() {
        return plgrCod;
    }
    public void setPlgrCod(String plgrCod) {
        this.plgrCod = plgrCod;
    }
    @Column(name = "PLGR_ACCNO")
    public String getPlgrAccNo() {
        return plgrAccNo;
    }
    public void setPlgrAccNo(String plgrAccNo) {
        this.plgrAccNo = plgrAccNo;
    }
    @Column(name = "PLGR_BNM")
    public String getPlgrBnm() {
        return plgrBnm;
    }
    public void setPlgrBnm(String plgrBnm) {
        this.plgrBnm = plgrBnm;
    }
    @Column(name = "PLGE_TYP")
    public String getPlgeTyp() {
        return plgeTyp;
    }
    public void setPlgeTyp(String plgeTyp) {
        this.plgeTyp = plgeTyp;
    }
    @Column(name = "PLGE_NAM")
    public String getPlgeNam() {
        return plgeNam;
    }
    public void setPlgeNam(String plgeNam) {
        this.plgeNam = plgeNam;
    }
    @Column(name = "PLGE_INSTID")
    public String getPlgeInstID() {
        return plgeInstID;
    }
    public void setPlgeInstID(String plgeInstID) {
        this.plgeInstID = plgeInstID;
    }
    @Column(name = "PLGE_COD")
    public String getPlgeCod() {
        return plgeCod;
    }
    public void setPlgeCod(String plgeCod) {
        this.plgeCod = plgeCod;
    }
    @Column(name = "PLGE_ACCNO")
    public String getPlgeAccNo() {
        return plgeAccNo;
    }
    public void setPlgeAccNo(String plgeAccNo) {
        this.plgeAccNo = plgeAccNo;
    }
    @Column(name = "PLGE_BNM")
    public String getPlgeBnm() {
        return plgeBnm;
    }
    public void setPlgeBnm(String plgeBnm) {
        this.plgeBnm = plgeBnm;
    }
    @Column(name = "BILL_PLG_VAL")
    public String getBillPlgVal() {
        return billPlgVal;
    }
    public void setBillPlgVal(String billPlgVal) {
        this.billPlgVal = billPlgVal;
    }
    @Column(name = "BILL_STATUS")
    public String getBillStatus() {
        return billStatus;
    }
    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    @Column(name = "OCP_SMIDY_LMT_FLAG")
    public String getOcpSmidyLmtFlag() {
        return ocpSmidyLmtFlag;
    }
    public void setOcpSmidyLmtFlag(String ocpSmidyLmtFlag) {
        this.ocpSmidyLmtFlag = ocpSmidyLmtFlag;
    }
    @Column(name = "HIGH_LMT_PLG_CTRID")
    public String getHighLmtPlgCtrID() {
        return highLmtPlgCtrID;
    }
    public void setHighLmtPlgCtrID(String highLmtPlgCtrID) {
        this.highLmtPlgCtrID = highLmtPlgCtrID;
    }
    @Column(name = "HIGH_LMT_PLG_SRL")
    public String getHighLmtPlgSrl() {
        return highLmtPlgSrl;
    }
    public void setHighLmtPlgSrl(String highLmtPlgSrl) {
        this.highLmtPlgSrl = highLmtPlgSrl;
    }
    @Column(name = "POOL_STT_OFIO")
    public String getPoolSttOfIO() {
        return poolSttOfIO;
    }
    public void setPoolSttOfIO(String poolSttOfIO) {
        this.poolSttOfIO = poolSttOfIO;
    }
    @Column(name = "AVD_REPEAT_FLAG")
    public String getAvdRepeatFlag() {
        return avdRepeatFlag;
    }
    public void setAvdRepeatFlag(String avdRepeatFlag) {
        this.avdRepeatFlag = avdRepeatFlag;
    }
    @Column(name = "TRANS_DATE")
    public Date getTransDate() {
        return transDate;
    }
    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }
    @Column(name = "IS_ALWCST_OUTPOOL")
    public Date getIsAlwCstOutPool() {
        return isAlwCstOutPool;
    }
    public void setIsAlwCstOutPool(Date isAlwCstOutPool) {
        this.isAlwCstOutPool = isAlwCstOutPool;
    }
    @Override
    public String toString() {
        return "AbPoolIntoRepeal{" +
                "billIdCode='" + billIdCode + '\'' +
                ", aid='" + aid + '\'' +
                ", vvId='" + vvId + '\'' +
                ", bpId='" + bpId + '\'' +
                ", briefId='" + briefId + '\'' +
                ", appNo='" + appNo + '\'' +
                ", origNo='" + origNo + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                ", billType='" + billType + '\'' +
                ", billMoney=" + billMoney +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payer='" + payer + '\'' +
                ", payerType='" + payerType + '\'' +
                ", payerCode='" + payerCode + '\'' +
                ", payeracc='" + payeracc + '\'' +
                ", payerBankName='" + payerBankName + '\'' +
                ", payerbankno='" + payerbankno + '\'' +
                ", payee='" + payee + '\'' +
                ", payeeType='" + payeeType + '\'' +
                ", payeeCode='" + payeeCode + '\'' +
                ", payeeacc='" + payeeacc + '\'' +
                ", payeeBankName='" + payeeBankName + '\'' +
                ", payeebankno='" + payeebankno + '\'' +
                ", accpType='" + accpType + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", acceptorCode='" + acceptorCode + '\'' +
                ", acceptoracc='" + acceptoracc + '\'' +
                ", acceptorbankno='" + acceptorbankno + '\'' +
                ", acceptorBankName='" + acceptorBankName + '\'' +
                ", plgrTyp='" + plgrTyp + '\'' +
                ", plgrNam='" + plgrNam + '\'' +
                ", plgrCod='" + plgrCod + '\'' +
                ", plgrAccNo='" + plgrAccNo + '\'' +
                ", plgrBnm='" + plgrBnm + '\'' +
                ", plgeTyp='" + plgeTyp + '\'' +
                ", plgeNam='" + plgeNam + '\'' +
                ", plgeInstID='" + plgeInstID + '\'' +
                ", plgeCod='" + plgeCod + '\'' +
                ", plgeAccNo='" + plgeAccNo + '\'' +
                ", plgeBnm='" + plgeBnm + '\'' +
                ", billPlgVal='" + billPlgVal + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", ocpSmidyLmtFlag='" + ocpSmidyLmtFlag + '\'' +
                ", highLmtPlgCtrID='" + highLmtPlgCtrID + '\'' +
                ", highLmtPlgSrl='" + highLmtPlgSrl + '\'' +
                ", poolSttOfIO='" + poolSttOfIO + '\'' +
                ", avdRepeatFlag='" + avdRepeatFlag + '\'' +
                ", transDate=" + transDate +
                ", isAlwCstOutPool=" + isAlwCstOutPool +
                '}';
    }
}
