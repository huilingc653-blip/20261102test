package com.bytter.biz.inneraccount.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * Created by wangzx on 2017/6/23.
 */
public class FcAccForBankAccVO  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1251011134278157469L;
	@BtCompositeSearch(fieldName = "acc.id")
	private String id;
	/**账号*/
	@BtCompositeSearch(fieldName = "acc.blnctr_acc")
	private String blnctrAcc;
	
	/**户名*/
	private String accName;
	/**单位*/
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpId;
	private String corpName;
	/**币别*/
	@Currency(inject="curName")
	@BtCompositeSearch(fieldName = "acc.cur_id")
    private String curId;
    private String curName;    
    /**
     * 网点
     */
    @Corp(inject = "netName")
    @BtCompositeSearch(fieldName = "acc.net_id")
    private String netId;
    private String netName;
    
    /**开户日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accOpenTime;
	
	private String bankName;
	
	/**账户类别*/
	@Dict(dictGroup = "fcacc.accType", inject = "accTypeName")
	@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accType;
	private String accTypeName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
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
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public Date getAccOpenTime() {
		return accOpenTime;
	}
	public void setAccOpenTime(Date accOpenTime) {
		this.accOpenTime = accOpenTime;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	
	
}
