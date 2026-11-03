package com.bytter.system.vo;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 银行账号关系策略维护VO
 * @author onlineGenerator
 * @date 2017-06-20 15:20:33
 * @version 1.0
 *
 */
public class BisAccGatherPolicyVO implements  Serializable {
	/**账号*/
	private String id;
	/**账号Id*/
	private String bankAccId;
	/**账号*/
	private String bankAcc;
	/**账户名称*/
	private String accName;
	/**开户行名称*/
	private String bankName;
	/**调拨策略*/
	private String gatherPolicyId;
	/**接口代码*/
	private String bifCode;
	/**父账号Id*/
	private String parentAccId;
	/**父账号*/
	private String parentBankAcc;
	/**父账户名称*/
	private String parentAccName;
	/**父开户行名称*/
	private String parentBankName;
	/**首次归集时间*/
	private Date firstTime;
	/**下次归集时间*/
	private Date nextTime;
	/**下次下拔时间*/
	private Date appNextTime;
	/**开户行行号*/
	private String bankSourcecode;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**币别*/
	@Currency(inject = "curName")
	private String curId;
	private String curName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getGatherPolicyId() {
		return gatherPolicyId;
	}
	public void setGatherPolicyId(String gatherPolicyId) {
		this.gatherPolicyId = gatherPolicyId;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getParentAccId() {
		return parentAccId;
	}
	public void setParentAccId(String parentAccId) {
		this.parentAccId = parentAccId;
	}
	public String getParentBankAcc() {
		return parentBankAcc;
	}
	public void setParentBankAcc(String parentBankAcc) {
		this.parentBankAcc = parentBankAcc;
	}
	public String getParentAccName() {
		return parentAccName;
	}
	public void setParentAccName(String parentAccName) {
		this.parentAccName = parentAccName;
	}
	public String getParentBankName() {
		return parentBankName;
	}
	public void setParentBankName(String parentBankName) {
		this.parentBankName = parentBankName;
	}
	public Date getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	public Date getNextTime() {
		return nextTime;
	}
	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	public Date getAppNextTime() {
		return appNextTime;
	}
	public void setAppNextTime(Date appNextTime) {
		this.appNextTime = appNextTime;
	}
	public String getBankSourcecode() {
		return bankSourcecode;
	}
	public void setBankSourcecode(String bankSourcecode) {
		this.bankSourcecode = bankSourcecode;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
}
