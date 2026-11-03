package com.bytter.check.entity;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>银行对账已对账数据Bean<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年9月29日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class FcCheckOverBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**银行对账实体主键*/
	private String bankBillPK;
	/**中心对账实体主键*/
	private String compareAccPK;
	/**银行账号*/
	private String bankAcc;
	/**币别*/
	private String curId;
	private String bankId;
	private String importType;
	
	public FcCheckOverBean() {
	}
	
	public FcCheckOverBean(String bankBillPK, String compareAccPK, String bankAcc, String curId,String bankId,String importType) {
		super();
		this.bankBillPK = bankBillPK;
		this.compareAccPK = compareAccPK;
		this.bankAcc = bankAcc;
		this.curId = curId;
		this.bankId = bankId;
		this.importType = importType;
	}
	
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getBankBillPK() {
		return bankBillPK;
	}
	public void setBankBillPK(String bankBillPK) {
		this.bankBillPK = bankBillPK;
	}
	public String getCompareAccPK() {
		return compareAccPK;
	}
	public void setCompareAccPK(String compareAccPK) {
		this.compareAccPK = compareAccPK;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
