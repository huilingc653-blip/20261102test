package com.bytter.biz.finacial.report.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
public class FcBookDtlBatchPrintVO implements Serializable{
	/**打印头 */
	private FcBookDtlListVO fcBookDtlListVO;
	/**打印列表 */
	private List<FcBookDtlListVO> fcBookDtlListVOList;
	/** 合计贷方发生额 */
	private BigDecimal sumCreditMoney;
	/**合计借方发生额*/
	private BigDecimal sumDebitMoney;
	public FcBookDtlListVO getFcBookDtlListVO() {
		return fcBookDtlListVO;
	}
	public void setFcBookDtlListVO(FcBookDtlListVO fcBookDtlListVO) {
		this.fcBookDtlListVO = fcBookDtlListVO;
	}
	public List<FcBookDtlListVO> getFcBookDtlListVOList() {
		return fcBookDtlListVOList;
	}
	public void setFcBookDtlListVOList(List<FcBookDtlListVO> fcBookDtlListVOList) {
		this.fcBookDtlListVOList = fcBookDtlListVOList;
	}
	public BigDecimal getSumCreditMoney() {
		return sumCreditMoney;
	}
	public void setSumCreditMoney(BigDecimal sumCreditMoney) {
		this.sumCreditMoney = sumCreditMoney;
	}
	public BigDecimal getSumDebitMoney() {
		return sumDebitMoney;
	}
	public void setSumDebitMoney(BigDecimal sumDebitMoney) {
		this.sumDebitMoney = sumDebitMoney;
	}
}
