package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 
　 * @Title: 贴现申请对应VO
　 * @author zhenghj
  * @Description:贴现申请对应VO
　 * @date 2020年3月10日
　 * @version 1.0
 */
public class AbPoolDiscountVO implements java.io.Serializable,  IBusinessKey{
    private String id;
    /** 主表ID**/
    private String vouId;
    /** 业务流水表**/
    private String accId;
    /** 审批业务流水ID**/
    private String businessKey;
	/**
     * 出入池状态
     * 0或空	未入池
     * 1	已入池
     * 2	入池在途
     */
    private String poolStatus;
    /**
     * 从表ID
     */
    private String vvId;
    /**
     * 票据池ID
     */
    @BtCompositeSearch(fieldName="ap.id")
    private String bpId;
    /**票据池名称**/
    private String bpName;
    /**
     * 业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
     */
    @Dict(dictGroup = "AB_POOL_BRIEF_ID", inject = "briefIdName")
    private String briefId;
    private String briefIdName;
    /**
     * 申请单号
     */
    private String appNo;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName="apd.orig_no")
    private String origNo;
    /**
     * 出入池方式：1质押2托管
     */
    @BtCompositeSearch(fieldName="apd.INOUT_TYPE")
    @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "inoutTypeName")
    private String inoutType;
    @BtCompositeSearch(fieldName="apd.INOUT_TYPE")
    private String inoutTypeName;
    /**
     * 上一笔流水id
     */
    private String lastId;
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
    /** 选中票据数 */
    private Integer length;
    /**持票单位*/
    @BtCompositeSearch(fieldName="apc.corp_id")
    @Corp(inject="corpName")
    private String corpId;
    /**
     * 票据池单位
     */
    @BtCompositeSearch(fieldName="apc.corp_name")
    private String corpName;
    /**
     * 票据池单位代码
     */
    private String corpCode;
    /**票据类型：10 银票、11 商票*/
    @BtCompositeSearch(fieldName="v.bill_type")
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    @BtCompositeSearch(fieldName="v.bill_type")
    private String  billTypeName;
    /**流通介质：10 纸票、11 电票*/
    @BtCompositeSearch(fieldName="v.bill_Flag")
    @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
    private Integer billFlag;
    @BtCompositeSearch(fieldName="v.bill_Flag")
    private String billFlagName;
    /**票据金额*/
    @BtCompositeSearch(fieldName="v.bill_money")
    private BigDecimal billMoney;
    /**出票日期*/
    @BtCompositeSearch(fieldName="v.start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName="v.end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    /**出票人*/
    @BtCompositeSearch(fieldName="v.payer")
    private String payer;
    /**出票人账号*/
    private String payeracc;
    /**出票人开户银行名称*/
    private String payerBankName;
    /**出票人开户银行行号 */
    private String payerbankno;
    /**收票人全称*/
    @BtCompositeSearch(fieldName="v.payee")
    private String payee;
    /**收票人账号*/
    private String payeeacc;
    /**收票人开户银行名称*/
    private String payeeBankName;
    /**收票人开户银行行号*/
    private String payeebankno;
    /**承兑人*/
    @BtCompositeSearch(fieldName="v.acceptor")
    private String acceptor;
    /**承兑人账号 */
    private String acceptoracc;
    /**承兑人开户行名称 */
    private String acceptorBankName;
    /**承兑人开户行行号 */
    private String acceptorbankno;
    /**票据状态*/
    @BtCompositeSearch(fieldName="apd.status")
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    private Integer status;
    @BtCompositeSearch(fieldName="status")
    private String statusName;
    /**
     * 指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
     */
    @BtCompositeSearch(fieldName="apd.opt_status")
    @Dict(dictGroup = "AB_POOL_DTL_OPT_STATUS", inject = "optStatusName")
    private String optStatus;
    @BtCompositeSearch(fieldName="apd.opt_status")
    private String optStatusName;
    /**是否隐藏复选框 1为是*/
	private String checkboxHide;
    /**
     * 银行接口代码
     */
    private String bifCode;
    /**显示票据信息*/
    /**持票单位账号 */
    private String bankAcc;
    /**持票单位银行*/
    private String bankCode;
    /**不得转让标记EM00可再转让EM01不得转让*/
    @Dict(dictGroup="AB_BANEM",inject="banendrsmtmkName")
    private String banendrsmtmk;
    private String banendrsmtmkName;
    /**所属库存*/
    @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
    private String enterStock;
    private String enterStockName;
    /**业务归属日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date businessDate;
    /**内部业务标志：0  否；1 是*/
    @Dict(dictGroup = "sf_yn", inject = "insideSingName")
    private Integer insideSing;
    private String insideSingName;
    /**是否发送指令标志：0否； 1是 ；*/
    @BtCompositeSearch(fieldName="s.send_Sign")
    @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
    private String sendSign;
    private String sendSignName;
    /**业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
    private String reqRoleName;
    @Dict(dictGroup="AB_BILL_ROLE",inject="reqRoleName")
    private String reqRole;
    /**业务申请人名称*/
    private String reqNm;
    /**业务申请人组织机构代码*/
    private String reqCmonid;
    /**业务申请人账号*/
    private String reqAcctId;
    /**业务申请人开户行号*/
    private String reqAcctsvcr;
    /**业务申请人开户行号*/
    private String reqAcctsvcrName;
    /**业务申请人承接行号*/
    private String reqAcctsvcrAg;
    /**业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
    private String recRoleName;
    @Dict(dictGroup="AB_BILL_ROLE",inject="recRoleName")
    private String recRole;
    /**业务接收人名称*/
    private String recNm;
    /**业务接收人组织机构代码*/
    private String recCmonid;
    /**业务接收人账号*/
    private String recAcctId;
    /**业务接收人开户行号*/
    private String recAcctsvcr;
    private String recAcctsvcrName;
    /**业务接收人承接行号*/
    private String recAcctsvcrAg;
    /**处理日期*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="applyDate")
    private Date applyDate;
    /**承兑合同号*/
    private String acontractNo;
    /**发票号*/
    private String ainvcnb;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVvId() {
        return vvId;
    }
    public void setVvId(String vvId) {
        this.vvId = vvId;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getBriefId() {
        return briefId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public String getAppNo() {
        return appNo;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getInoutType() {
        return inoutType;
    }
    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getLastId() {
        return lastId;
    }
    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    public String getBillFlagName() {
        return billFlagName;
    }
    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getOptStatusName() {
        return optStatusName;
    }
    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }
    public String getPoolStatus() {
        return poolStatus;
    }
    public void setPoolStatus(String poolStatus) {
        this.poolStatus = poolStatus;
    }
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	
    public String getVouId() {
		return vouId;
	}
    public void setVouId(String vouId) {
		this.vouId = vouId;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getInoutTypeName() {
		return inoutTypeName;
	}
	public void setInoutTypeName(String inoutTypeName) {
		this.inoutTypeName = inoutTypeName;
	}
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBpName() {
        return bpName;
    }
    public void setBpName(String bpName) {
        this.bpName = bpName;
    }
    public String getBriefIdName() {
        return briefIdName;
    }
    public void setBriefIdName(String briefIdName) {
        this.briefIdName = briefIdName;
    }
    public String getPayeracc() {
        return payeracc;
    }
    public void setPayeracc(String payeracc) {
        this.payeracc = payeracc;
    }
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    public String getPayerbankno() {
        return payerbankno;
    }
    public void setPayerbankno(String payerbankno) {
        this.payerbankno = payerbankno;
    }
    public String getPayeeacc() {
        return payeeacc;
    }
    public void setPayeeacc(String payeeacc) {
        this.payeeacc = payeeacc;
    }
    public String getPayeeBankName() {
        return payeeBankName;
    }
    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }
    public String getPayeebankno() {
        return payeebankno;
    }
    public void setPayeebankno(String payeebankno) {
        this.payeebankno = payeebankno;
    }
    public String getAcceptoracc() {
        return acceptoracc;
    }
    public void setAcceptoracc(String acceptoracc) {
        this.acceptoracc = acceptoracc;
    }
    public String getAcceptorBankName() {
        return acceptorBankName;
    }
    public void setAcceptorBankName(String acceptorBankName) {
        this.acceptorBankName = acceptorBankName;
    }
    public String getAcceptorbankno() {
        return acceptorbankno;
    }
    public void setAcceptorbankno(String acceptorbankno) {
        this.acceptorbankno = acceptorbankno;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getEnterStock() {
        return enterStock;
    }
    public void setEnterStock(String enterStock) {
        this.enterStock = enterStock;
    }
    public String getEnterStockName() {
        return enterStockName;
    }
    public void setEnterStockName(String enterStockName) {
        this.enterStockName = enterStockName;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public String getRecNm() {
        return recNm;
    }
    public void setRecNm(String recNm) {
        this.recNm = recNm;
    }
    public String getRecAcctId() {
        return recAcctId;
    }
    public void setRecAcctId(String recAcctId) {
        this.recAcctId = recAcctId;
    }
    public String getRecAcctsvcr() {
        return recAcctsvcr;
    }
    public void setRecAcctsvcr(String recAcctsvcr) {
        this.recAcctsvcr = recAcctsvcr;
    }
    public Integer getInsideSing() {
        return insideSing;
    }
    public void setInsideSing(Integer insideSing) {
        this.insideSing = insideSing;
    }
    public String getInsideSingName() {
        return insideSingName;
    }
    public void setInsideSingName(String insideSingName) {
        this.insideSingName = insideSingName;
    }
    public String getSendSign() {
        return sendSign;
    }
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }
    public String getSendSignName() {
        return sendSignName;
    }
    public void setSendSignName(String sendSignName) {
        this.sendSignName = sendSignName;
    }
    public String getReqRoleName() {
        return reqRoleName;
    }
    public void setReqRoleName(String reqRoleName) {
        this.reqRoleName = reqRoleName;
    }
    public String getReqRole() {
        return reqRole;
    }
    public void setReqRole(String reqRole) {
        this.reqRole = reqRole;
    }
    public String getReqNm() {
        return reqNm;
    }
    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }
    public String getReqCmonid() {
        return reqCmonid;
    }
    public void setReqCmonid(String reqCmonid) {
        this.reqCmonid = reqCmonid;
    }
    public String getReqAcctId() {
        return reqAcctId;
    }
    public void setReqAcctId(String reqAcctId) {
        this.reqAcctId = reqAcctId;
    }
    public String getReqAcctsvcr() {
        return reqAcctsvcr;
    }
    public void setReqAcctsvcr(String reqAcctsvcr) {
        this.reqAcctsvcr = reqAcctsvcr;
    }
    public String getReqAcctsvcrName() {
        return reqAcctsvcrName;
    }
    public void setReqAcctsvcrName(String reqAcctsvcrName) {
        this.reqAcctsvcrName = reqAcctsvcrName;
    }
    public String getReqAcctsvcrAg() {
        return reqAcctsvcrAg;
    }
    public void setReqAcctsvcrAg(String reqAcctsvcrAg) {
        this.reqAcctsvcrAg = reqAcctsvcrAg;
    }
    public String getRecRoleName() {
        return recRoleName;
    }
    public void setRecRoleName(String recRoleName) {
        this.recRoleName = recRoleName;
    }
    public String getRecRole() {
        return recRole;
    }
    public void setRecRole(String recRole) {
        this.recRole = recRole;
    }
    public String getRecCmonid() {
        return recCmonid;
    }
    public void setRecCmonid(String recCmonid) {
        this.recCmonid = recCmonid;
    }
    public String getRecAcctsvcrName() {
        return recAcctsvcrName;
    }
    public void setRecAcctsvcrName(String recAcctsvcrName) {
        this.recAcctsvcrName = recAcctsvcrName;
    }
    public String getRecAcctsvcrAg() {
        return recAcctsvcrAg;
    }
    public void setRecAcctsvcrAg(String recAcctsvcrAg) {
        this.recAcctsvcrAg = recAcctsvcrAg;
    }
    public Date getApplyDate() {
        return applyDate;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    public String getBanendrsmtmk() {
        return banendrsmtmk;
    }
    public void setBanendrsmtmk(String banendrsmtmk) {
        this.banendrsmtmk = banendrsmtmk;
    }
    public String getBanendrsmtmkName() {
        return banendrsmtmkName;
    }
    public void setBanendrsmtmkName(String banendrsmtmkName) {
        this.banendrsmtmkName = banendrsmtmkName;
    }
    public String getAcontractNo() {
        return acontractNo;
    }
    public void setAcontractNo(String acontractNo) {
        this.acontractNo = acontractNo;
    }
    public String getAinvcnb() {
        return ainvcnb;
    }
    public void setAinvcnb(String ainvcnb) {
        this.ainvcnb = ainvcnb;
    }
    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }
    public String getCheckboxHide() {
        if(StringUtils.isNotEmpty(this.getBusinessKey())){
            //审批列表查询
            return "0";
        }
		if(this.getStatus()!=null
				&& (AbPoolConstant.AB_POOL_STATUS11.equals(this.getStatus())
				    && StringUtils.isEmpty(this.getOptStatus())
                || (AbPoolConstant.AB_POOL_STATUS95.equals(this.getStatus())
                && StringUtils.isNotEmpty(this.getOptStatus()) && !AbPoolConstant.AB_POOL_OPT_STATUS_2.equals(this.getOptStatus())))){
			checkboxHide="1";
		}else{
			checkboxHide="0";
		}
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
    @Override
    public String toString() {
        return "AbPoolDiscountVO{" +
                "id='" + id + '\'' +
                ", vouId='" + vouId + '\'' +
                ", accId='" + accId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", poolStatus='" + poolStatus + '\'' +
                ", vvId='" + vvId + '\'' +
                ", bpId='" + bpId + '\'' +
                ", bpName='" + bpName + '\'' +
                ", briefId='" + briefId + '\'' +
                ", briefIdName='" + briefIdName + '\'' +
                ", appNo='" + appNo + '\'' +
                ", origNo='" + origNo + '\'' +
                ", inoutType='" + inoutType + '\'' +
                ", inoutTypeName='" + inoutTypeName + '\'' +
                ", lastId='" + lastId + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                ", length=" + length +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", billType=" + billType +
                ", billTypeName='" + billTypeName + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", billMoney=" + billMoney +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payer='" + payer + '\'' +
                ", payeracc='" + payeracc + '\'' +
                ", payerBankName='" + payerBankName + '\'' +
                ", payerbankno='" + payerbankno + '\'' +
                ", payee='" + payee + '\'' +
                ", payeeacc='" + payeeacc + '\'' +
                ", payeeBankName='" + payeeBankName + '\'' +
                ", payeebankno='" + payeebankno + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", acceptoracc='" + acceptoracc + '\'' +
                ", acceptorBankName='" + acceptorBankName + '\'' +
                ", acceptorbankno='" + acceptorbankno + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", checkboxHide='" + checkboxHide + '\'' +
                ", bifCode='" + bifCode + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", banendrsmtmk='" + banendrsmtmk + '\'' +
                ", banendrsmtmkName='" + banendrsmtmkName + '\'' +
                ", enterStock='" + enterStock + '\'' +
                ", enterStockName='" + enterStockName + '\'' +
                ", businessDate=" + businessDate +
                ", insideSing=" + insideSing +
                ", insideSingName='" + insideSingName + '\'' +
                ", sendSign='" + sendSign + '\'' +
                ", sendSignName='" + sendSignName + '\'' +
                ", reqRoleName='" + reqRoleName + '\'' +
                ", reqRole='" + reqRole + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", reqCmonid='" + reqCmonid + '\'' +
                ", reqAcctId='" + reqAcctId + '\'' +
                ", reqAcctsvcr='" + reqAcctsvcr + '\'' +
                ", reqAcctsvcrName='" + reqAcctsvcrName + '\'' +
                ", reqAcctsvcrAg='" + reqAcctsvcrAg + '\'' +
                ", recRoleName='" + recRoleName + '\'' +
                ", recRole='" + recRole + '\'' +
                ", recNm='" + recNm + '\'' +
                ", recCmonid='" + recCmonid + '\'' +
                ", recAcctId='" + recAcctId + '\'' +
                ", recAcctsvcr='" + recAcctsvcr + '\'' +
                ", recAcctsvcrName='" + recAcctsvcrName + '\'' +
                ", recAcctsvcrAg='" + recAcctsvcrAg + '\'' +
                ", applyDate=" + applyDate +
                ", acontractNo='" + acontractNo + '\'' +
                ", ainvcnb='" + ainvcnb + '\'' +
                '}';
    }
}
