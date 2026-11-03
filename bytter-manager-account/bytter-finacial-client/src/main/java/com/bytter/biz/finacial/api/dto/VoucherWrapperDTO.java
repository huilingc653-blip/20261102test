package com.bytter.biz.finacial.api.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.bytter.biz.finacial.api.constant.BdlinkConstant;
import com.bytter.biz.finacial.api.constant.BudgetConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.core.base.entity.ICurrentUser;
import lombok.Data;
/**
 * 凭证封装对象,外模块调用账务时初始化此对象调用账务接口
 * @author liyh
 *
 */
@Data
public class VoucherWrapperDTO implements Serializable {

	/**
	 * 租户id
	 */
	private String tenantId;

	List<FcAccbookVoucherDTO> target = new ArrayList<FcAccbookVoucherDTO>();
	/**
     * 原凭证对象
     */
    protected List<FcAccbookVoucherDTO> oriTarget = new ArrayList<FcAccbookVoucherDTO>();
	/**
	 * 操作类型
	 */
	protected OperateType opType;
	/**
	 * 凭证处理类型
	 */
	protected ProcessType processType = ProcessType.PROCESS_COMMON;
	/**
     * 业务全局id
     */
    protected String busGlobalId;
    /**
     * 凭证号
     */
    protected String voucherNo;
    /**
     * 业务来源类型
     */
    protected Integer oriType;
    /**
     * 是否手续费计提
     */
    private boolean isCharge;
    /**
     * 是否自动复核
     */
    private boolean isAutoCheck  = false;
    /**
     * 是否自动记账,默认否
     */
    private boolean autoAccounter = false;
    /**
     * 成员单位内部资金计划标识,默认为非内转
     */
    private String innerSign = BudgetConstant.INNER_SIGN_0;
    /**
     * 记账人
     */
    private String userCode;
    /**
     * 作废冲销标识 0 作废  1冲销
     */
    private String flag = FinacialConstant.OVER_SIGN_NO;
    /**
     * 是否产生指令
     */
    private boolean createBisExc = false;
    /**
     * 用户
     */
    private ICurrentUser user;
	/**
	 * 记账复核人
	 */
	private String checkUser;
    /**
     * 付款账号
     */
    private String payerAcc;
    private String netId;
    /**
     * 是否对私付款,默认对公
     */
    private String isForindividual = BdlinkConstant.TRANS_MODE_PUBLIC;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public List<FcAccbookVoucherDTO> getTarget() {
		return target;
	}
	public void setTarget(List<FcAccbookVoucherDTO> target) {
		this.target = target;
	}
	public OperateType getOpType() {
		return opType;
	}
	public void setOpType(OperateType opType) {
		this.opType = opType;
	}
	public ProcessType getProcessType() {
		return processType;
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Integer getOriType() {
		return oriType;
	}
	public void setOriType(Integer oriType) {
		this.oriType = oriType;
	}
	public void addTarget(FcAccbookVoucherDTO accbook) {
        target.add(accbook);
    }
    public void addTargets(List<FcAccbookVoucherDTO> accbooks) {
        target.addAll(accbooks);
    }
    public FcAccbookVoucherDTO getBookVoucher(){
    	if (target.isEmpty()) {
            return null;
        }
        return target.get(0);
    }
    public List<FcAccbookVoucherDTO> getOriTarget() {
		return oriTarget;
	}
	public void setOriTarget(List<FcAccbookVoucherDTO> oriTarget) {
		this.oriTarget = oriTarget;
	}
	public void addOriTarget(FcAccbookVoucherDTO accbook) {
        oriTarget.add(accbook);
    }
	public void addOriTargets(List<FcAccbookVoucherDTO> accbooks) {
        oriTarget.addAll(accbooks);
    }
	public boolean isCharge() {
		return isCharge;
	}
	public void setCharge(boolean isCharge) {
		this.isCharge = isCharge;
	}
	public boolean isAutoCheck() {
		return isAutoCheck;
	}
	public void setAutoCheck(boolean isAutoCheck) {
		this.isAutoCheck = isAutoCheck;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public boolean isAutoAccounter() {
		return autoAccounter;
	}
	public void setAutoAccounter(boolean autoAccounter) {
		this.autoAccounter = autoAccounter;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	public boolean isCreateBisExc() {
		return createBisExc;
	}
	public void setCreateBisExc(boolean createBisExc) {
		this.createBisExc = createBisExc;
	}
	public ICurrentUser getUser() {
		return user;
	}
	public void setUser(ICurrentUser user) {
		this.user = user;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getIsForindividual() {
		return isForindividual;
	}
	public void setIsForindividual(String isForindividual) {
		this.isForindividual = isForindividual;
	}
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
    	System.out.println(list.remove(2));
	}
}
