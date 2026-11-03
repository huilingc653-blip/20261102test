package com.bytter.tally.dto;

import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.tally.vo.FcAccbookDtlVO;
import com.bytter.tally.vo.FcAccbookExtVO;
import com.bytter.tally.vo.FcAccbookVO;

import java.io.Serializable;
import java.util.List;

/**
 * 凭证传输对象，用于页面显示
 *
 * @author liyh
 */
public class FcAccbookPageDTO implements Serializable {
    /**
     * 账务凭证主档表记录
     **/
    private FcAccbookDTO accbook;
    /**
     * 账务凭证分录明细记录
     */
    private List<FcAccbookDtlDTO> bookDtls;
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
     * 现金流项目标识(0-非现金流项目，1-现金流项目)
     */
    private String cashFlag;

    /**
     * 凭证处理类型
     */
    protected ProcessType processType = ProcessType.PROCESS_COMMON;

    private Boolean dateRule;

    /**
     * 联机余额
     */
    private FcOnlineLedgerDTO fcOnlineLedgerDTO;

    public List<FcAccbookDtlDTO> getBookDtl() {
        return bookDtl;
    }

    public void setBookDtl(List<FcAccbookDtlDTO> bookDtl) {
        this.bookDtl = bookDtl;
    }

    public FcAccbookDTO getAccbook() {
        return accbook;
    }

    public void setAccbook(FcAccbookDTO accbook) {
        this.accbook = accbook;
    }

    public List<FcAccbookDtlDTO> getBookDtls() {
        return bookDtls;
    }

    public void setBookDtls(List<FcAccbookDtlDTO> bookDtls) {
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

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public FcOnlineLedgerDTO getFcOnlineLedgerDTO() {
        return fcOnlineLedgerDTO;
    }

    public void setFcOnlineLedgerDTO(FcOnlineLedgerDTO fcOnlineLedgerDTO) {
        this.fcOnlineLedgerDTO = fcOnlineLedgerDTO;
    }

    @Override
    public String toString() {
        return "FcAccbookPageVO [accbook=" + accbook + ", bookDtls=" + bookDtls + ", bookExt=" + bookExt + ", opType="
                + opType + ", processType=" + processType + "]";
    }
}
