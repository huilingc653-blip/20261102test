package com.bytter.bill.domain.billbank.vo;

import java.math.BigDecimal;

/**  *
 * 拆分票据基础ImpVO
 * 
 */
public interface SplitBaseImpVO {

	public String getRecordId();

	public void setRecordId(String recordId);

	public BigDecimal getBillMoney();

	public void setBillMoney(BigDecimal billMoney);

	public String getSbbllRngStrtSn();

	public void setSbbllRngStrtSn(String sbbllRngStrtSn);

	public String getSbbllRngEndSn();

	public void setSbbllRngEndSn(String sbbllRngEndSn);

	public String getOldDraftId();

	public void setOldDraftId(String oldDraftId);

	public String getOldAccId();

	public void setOldAccId(String oldAccId);

	public BigDecimal getOrgMoney();

	public void setOrgMoney(BigDecimal orgMoney);

	public String getOrgSbbllRngStrtSn();

	public void setOrgSbbllRngStrtSn(String orgSbbllRngStrtSn);

	public String getOrgSbbllRngEndSn();

	public void setOrgSbbllRngEndSn(String orgSbbllRngEndSn);

	public BigDecimal getSplitMoney();

	public void setSplitMoney(BigDecimal splitMoney);

	public String getCopyFlag() ;

	public void setCopyFlag(String copyFlag);

}
