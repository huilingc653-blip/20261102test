package com.bytter.tally.vo;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.tally.entity.TallyFcAccbook;
import com.bytter.tally.entity.TallyFcAccbookDtl;
import com.bytter.tally.entity.TallyFcAccbookExt;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * 凭证传输对象，用于其他业务调用记账接口中数据存储
 * 
 * @author liyh
 *
 */
public class FcAccbookVoucherVO implements Serializable {
	/** 账务凭证主档表记录 **/
	private TallyFcAccbook accbook;
	/**
	 * 账务凭证分录明细记录
	 */
	private List<TallyFcAccbookDtl> bookDtls;
	/**
	 * 账务凭证扩展属性
	 */
	private TallyFcAccbookExt bookExt;
	
	/**
	 * 操作类型
	 */
	protected OperateType opType;
	/**
	 * 是否冲账凭证
	 */
	private boolean isRush = false;
	/**
	 * 是否冲账录入凭证
	 */
	private boolean isNewRush = false;
	
	/**
	 * 记账网点
	 */
	private String netCode;
	
	/** 借方汇总 */
	private BigDecimal debitSum;
    /** 贷方汇总 */
    private BigDecimal creditSum;
    /**联行号*/
    private String bankCode;
	public TallyFcAccbook getAccbook() {
	    if (null == accbook) {
	        accbook = new TallyFcAccbook();
        }
		return accbook;
	}
	public void setAccbook(TallyFcAccbook accbook) {
		this.accbook = accbook;
	}
	public List<TallyFcAccbookDtl> getBookDtls() {
		return bookDtls;
	}
	public void setBookDtls(List<TallyFcAccbookDtl> bookDtls) {
		this.bookDtls = bookDtls;
	}
	public TallyFcAccbookExt getBookExt() {
		return bookExt;
	}
	public void setBookExt(TallyFcAccbookExt bookExt) {
		this.bookExt = bookExt;
	}
	public OperateType getOpType() {
		return opType;
	}
	public void setOpType(OperateType opType) {
		this.opType = opType;
	}
	public boolean isRush() {
		return isRush;
	}
	public void setRush(boolean isRush) {
		this.isRush = isRush;
	}
	public boolean isNewRush() {
		return isNewRush;
	}
	public void setNewRush(boolean isNewRush) {
		this.isNewRush = isNewRush;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public BigDecimal getDebitSum() {
		return debitSum;
	}
	public void setDebitSum(BigDecimal debitSum) {
		this.debitSum = debitSum;
	}
	public BigDecimal getCreditSum() {
		return creditSum;
	}
	public void setCreditSum(BigDecimal creditSum) {
		this.creditSum = creditSum;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Override
	public String toString() {
		return "FcAccbookVoucherVO{" +
				"accbook=" + accbook +
				", bookDtls=" + bookDtls +
				", bookExt=" + bookExt +
				", opType=" + opType +
				", isRush=" + isRush +
				", isNewRush=" + isNewRush +
				", netCode='" + netCode + '\'' +
				", debitSum=" + debitSum +
				", creditSum=" + creditSum +
				'}';
	}
}
