package com.bytter.tally.vo;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import java.io.Serializable;
import java.util.List;
/**
 * 凭证传输对象，用于页面显示
 * 
 * @author liyh
 *
 */
public class FcAccbookPageVO implements Serializable {
	/** 账务凭证主档表记录 **/
	private FcAccbookVO accbook;
	/**
	 * 账务凭证分录明细记录
	 */
	private List<FcAccbookDtlVO> bookDtls;
	private List<FcAccbookDtlDTO> bookDtl;
	/**
	 * 账务凭证扩展属性
	 */
	private FcAccbookExtVO bookExt;
	/**
	 * 操作类型
	 */
	protected OperateType opType;
	/**
	 * 凭证处理类型
	 */
	protected ProcessType processType = ProcessType.PROCESS_COMMON;
	private Boolean dateRule;
	public List<FcAccbookDtlDTO> getBookDtl() {
		return bookDtl;
	}
	public void setBookDtl(List<FcAccbookDtlDTO> bookDtl) {
		this.bookDtl = bookDtl;
	}
	public FcAccbookVO getAccbook() {
		return accbook;
	}
	public void setAccbook(FcAccbookVO accbook) {
		this.accbook = accbook;
	}
	public List<FcAccbookDtlVO> getBookDtls() {
		return bookDtls;
	}
	public void setBookDtls(List<FcAccbookDtlVO> bookDtls) {
		this.bookDtls = bookDtls;
	}
	public FcAccbookExtVO getBookExt() {
		if (null == bookExt) {
			bookExt = new FcAccbookExtVO();
		}
		return bookExt;
	}
	public void setBookExt(FcAccbookExtVO bookExt) {
		this.bookExt = bookExt;
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
    public Boolean getDateRule() {
        return dateRule;
    }
    public void setDateRule(Boolean dateRule) {
        this.dateRule = dateRule;
    }
    @Override
	public String toString() {
		return "FcAccbookPageVO [accbook=" + accbook + ", bookDtls=" + bookDtls + ", bookExt=" + bookExt + ", opType="
				+ opType + ", processType=" + processType + "]";
	}
	
}
