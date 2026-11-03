package com.bytter.tally.vo;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.core.base.entity.ICurrentUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 凭证封装对象
 * @author liyh
 *
 */
public class VoucherWrapper implements Serializable {
	
	List<FcAccbookVoucherVO> target = new ArrayList<FcAccbookVoucherVO>(); 
	
	/**
     * 原凭证对象
     */
    protected List<FcAccbookVoucherVO> oriTarget = new ArrayList<FcAccbookVoucherVO>();
	
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
     * 指定复核人
     */
    private boolean haveCheckUser  = false;
    
    /**
     * 记账人
     */
    private String userCode;
    
    /**
     * 网点
     */
    private String netId;
    
    /**
     * 是否自动记账
     */
    private boolean autoAccounter = true;
    
    /**
     * 是否产生指令
     */
    private boolean createBisExc = false;
    
    /**
     * 用户
     */
    private ICurrentUser user;
    
    /**
     * 付款账号
     */
    private String payerAcc;
    
    /**
     * 是否对私付款,默认对公
     */
    private String isForindividual = BdlinkConstant.TRANS_MODE_PUBLIC;
    /**
     * 记账复核人
     */
    private String checkUser;
    public String getCheckUser() {
        return checkUser;
    }
    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }
    public List<FcAccbookVoucherVO> getTarget() {
		return target;
	}
	public void setTarget(List<FcAccbookVoucherVO> target) {
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
	public Integer getOriType() {
		return oriType;
	}
	public void setOriType(Integer oriType) {
		this.oriType = oriType;
	}
	
	public void addTarget(FcAccbookVoucherVO accbook) {
        target.add(accbook);
    }
    public void addTargets(List<FcAccbookVoucherVO> accbooks) {
        target.addAll(accbooks);
    }
    
    public FcAccbookVoucherVO getBookVoucher(){
    	if (target.isEmpty()) {
            return null;
        }
        return target.get(0);
    }
    
    public List<FcAccbookVoucherVO> getOriTarget() {
		return oriTarget;
	}
	public void setOriTarget(List<FcAccbookVoucherVO> oriTarget) {
		this.oriTarget = oriTarget;
	}
	
	public void addOriTarget(FcAccbookVoucherVO accbook) {
        oriTarget.add(accbook);
    }
	
	public void addOriTargets(List<FcAccbookVoucherVO> accbooks) {
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
    public boolean isHaveCheckUser() {
        return haveCheckUser;
    }
    public void setHaveCheckUser(boolean haveCheckUser) {
        this.haveCheckUser = haveCheckUser;
    }
    public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public boolean isAutoAccounter() {
		return autoAccounter;
	}
	public void setAutoAccounter(boolean autoAccounter) {
		this.autoAccounter = autoAccounter;
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
