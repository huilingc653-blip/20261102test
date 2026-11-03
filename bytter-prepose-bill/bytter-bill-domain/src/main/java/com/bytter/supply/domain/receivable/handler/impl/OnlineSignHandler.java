package com.bytter.supply.domain.receivable.handler.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.domain.common.entity.ScfInstruction;
import com.bytter.supply.domain.common.service.ScfInstructionService;
import com.bytter.supply.domain.receivable.dao.ScfReceivableTransferInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfLoanInfoDao;
import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.enums.OnlineEnum;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ztd
 * @version 1.0
 * @Title: OnlineSignHandler
 * @Description: 线上签收处理器
 * @since 2026/7/3
 */
@Component
public class OnlineSignHandler implements SignHandler {

    @Autowired
    private ScfInstructionService scfInstructionService;

    @Autowired
    private ScfReceivableTransferInfoDao scfReceivableTransferInfoDao;

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private ScfLoanInfoDao scfLoanInfoDao;

    @Autowired
    private OfflineSignHandler offlineSignHandler;

    @Override
    public void processSign(ScfReceivableVoucherInfo voucherInfo) {
        offlineSignHandler.processSign(voucherInfo);
    }

    @Override
    public void processLoan(ScfLoanInfo loanInfo) {
        ScfReceivableVoucherInfo voucherInfo = getVoucher(loanInfo.getVoucherCode());
        if (voucherInfo == null) return;
        ScfInstruction instruction = newInstruction(voucherInfo);
        instruction.setInstrType(ScfEntBankOpenConstant.RECEIVABLE_FINANCE_APPLY);
        instruction.setInstrTypeName(ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.RECEIVABLE_FINANCE_APPLY));
        instruction.setInstrAmt(loanInfo.getLoanAmt());
        instruction.setCreateBy(loanInfo.getCreateBy());
        instruction.setRelateNo(loanInfo.getLoanNo());
        instruction.setCorpId(loanInfo.getLoanCompanyId());
        instruction.setCorpName(loanInfo.getLoanCompanyName());
        instruction.setCurId(voucherInfo.getCurId());
        instruction.setCurName(voucherInfo.getCurName());
        instruction.setExpireDate(voucherInfo.getExpireDate());
        scfInstructionService.saveReceivableInstruction(instruction);
        voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        voucherInfo.setVoucherStatus(BusinessStatusEnum.LOAN_ING.getCode());
        scfReceivableVoucherInfoDao.update(voucherInfo);
        loanInfo.setLoanStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        scfLoanInfoDao.update(loanInfo);
    }

    @Override
    public void processTransfer(ScfReceivableVoucherInfo voucherInfo) {
        List<ScfReceivableTransferInfo> transferList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("transferStatus", ApprovalStatusEnum.APPROVAL_ING.getCode()));
        if (transferList.isEmpty()) return;
        for (ScfReceivableTransferInfo transferInfo : transferList) {
            ScfInstruction instruction = new ScfInstruction();
            BeanUtil.copyProperties(transferInfo, instruction);
            instruction.setId(null);
            instruction.setInstrCode(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_INSTRUCTION_NO));
            instruction.setInstrDate(DateUtils.getDate());
            instruction.setSign(ScfEntBankOpenConstant.SCF_RECEIVABLE_INSTRUMENT_SIGN_1);
            instruction.setCreateDate(DateUtils.getDate());
            instruction.setInstrType(ScfEntBankOpenConstant.RECEIVABLE_VOUCHER_TRANSFER);
            instruction.setInstrTypeName(ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.RECEIVABLE_VOUCHER_TRANSFER));
            instruction.setInstrAmt(transferInfo.getTransferAmt());
            instruction.setRelateNo(transferInfo.getTransferNo());
            instruction.setCorpId(transferInfo.getIssueTransferCompanyId());
            instruction.setCorpName(transferInfo.getIssueTransferCompany());
            instruction.setCurId(voucherInfo.getCurId());
            instruction.setCurName(voucherInfo.getCurName());
            instruction.setExpireDate(voucherInfo.getExpireDate());
            scfInstructionService.saveReceivableInstruction(instruction);
        }
        offlineSignHandler.processTransfer(voucherInfo);
    }

    @Override
    public void processPayment(ScfReceivablePaymentInfo paymentInfo) {
        offlineSignHandler.processPayment(paymentInfo);
    }

    @Override
    public String getType() {
        return OnlineEnum.ONLINE.getCode();
    }

    private ScfReceivableVoucherInfo getVoucher(String voucherCode) {
        return scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", voucherCode);
    }

    private ScfInstruction newInstruction(ScfReceivableVoucherInfo voucherInfo) {
        ScfInstruction instruction = new ScfInstruction();
        BeanUtil.copyProperties(voucherInfo, instruction);
        instruction.setId(null);
        instruction.setInstrCode(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_INSTRUCTION_NO));
        instruction.setInstrDate(DateUtils.getDate());
        instruction.setSign(ScfEntBankOpenConstant.SCF_RECEIVABLE_INSTRUMENT_SIGN_1);
        instruction.setCreateDate(DateUtils.getDate());
        return instruction;
    }
}
