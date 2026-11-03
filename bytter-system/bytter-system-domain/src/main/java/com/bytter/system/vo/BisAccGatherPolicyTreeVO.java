package com.bytter.system.vo;
import java.io.Serializable;
/**   
 * @Title: Entity
 * @Description: 银行账号关系策略维护VO
 * @author onlineGenerator
 * @date 2017-06-20 15:20:33
 * @version 1.0
 *
 */
public class BisAccGatherPolicyTreeVO implements Serializable {
	
	/**科目ID*/
	private String id;
	/**科目父ID*/
	private String fatherId;
	/**调拨策略代码*/
	private String gatherPolicyCode;
	/**调拨策略名称*/
	private String gatherPolicyName;
	/**账号*/
	private String bankAcc;
	/**账户名称*/
	private String accName;
	/**开户行名称*/
	private String bankName;
	
	/**调拨策略ID*/
	private String gatherPolicyId;
	/**父账号ID*/
	private String prentAccId;
	/**科目ID*/
	private String treeId;
	/**科目父ID*/
	private String treePrentId;
	/**币别*/
	private String curId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String getGatherPolicyCode() {
		return gatherPolicyCode;
	}
	public void setGatherPolicyCode(String gatherPolicyCode) {
		this.gatherPolicyCode = gatherPolicyCode;
	}
	public String getGatherPolicyName() {
		return gatherPolicyName;
	}
	public void setGatherPolicyName(String gatherPolicyName) {
		this.gatherPolicyName = gatherPolicyName;
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
	public String getPrentAccId() {
		return prentAccId;
	}
	public void setPrentAccId(String prentAccId) {
		this.prentAccId = prentAccId;
	}
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	public String getTreePrentId() {
		return treePrentId;
	}
	public void setTreePrentId(String treePrentId) {
		this.treePrentId = treePrentId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
}
