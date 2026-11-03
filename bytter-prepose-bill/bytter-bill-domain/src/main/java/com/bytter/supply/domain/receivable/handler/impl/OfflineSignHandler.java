package com.bytter.supply.domain.receivable.handler.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.enums.*;
import com.bytter.supply.domain.receivable.dao.ScfLoanInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivablePaymentInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableTransferInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.domain.common.dao.ScfInvoiceInfoDao;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztd
 * @version 1.0
 * @Title: OfflineSignHandler
 * @Description: 凭证线下处理器
 * @since 2026/7/3
 */
@Component
@Slf4j
public class OfflineSignHandler implements SignHandler {

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private ScfLoanInfoDao scfLoanInfoDao;

    @Autowired
    private ScfReceivableTransferInfoDao scfReceivableTransferInfoDao;

    @Autowired
    private ScfReceivablePaymentInfoDao scfReceivablePaymentInfoDao;

    @Autowired
    private ScfInvoiceInfoDao scfInvoiceInfoDao;

    @Override
    public String getType() {
        return OnlineEnum.OFFLINE.getCode();
    }

    @Override
    public void processSign(ScfReceivableVoucherInfo voucherInfo) {
        log.info("processSign==voucherInfo:{}", JSON.toJSONString(voucherInfo));
        VoucherSignEnum signEnum = VoucherSignEnum.getByCode(voucherInfo.getSignOperation());
        switch (signEnum) {
            case SIGNED:
                voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
                break;
            case REJECTED:
                voucherInfo.setVoucherStatus(BusinessStatusEnum.REJECTED.getCode());
                break;
            case CANCELLED:
                voucherInfo.setVoucherStatus(BusinessStatusEnum.CANCEL.getCode());
                break;
            default:
                break;
        }
        voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        scfReceivableVoucherInfoDao.update(voucherInfo);
    }

    @Override
    public void processLoan(ScfLoanInfo loanInfo) {
        log.info("processLoan==loanInfo:{}", JSON.toJSONString(loanInfo));
        updateVoucherInfo(ApprovalStatusEnum.APPROVAL_PASS,
                BusinessStatusEnum.LOAN_SUCCESS,
                loanInfo.getVoucherCode());
        loanInfo.setLoanStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        scfLoanInfoDao.update(loanInfo);
    }

    @Override
    public void processTransfer(ScfReceivableVoucherInfo voucherInfo) {
        log.info("processTransfer==voucherInfo:{}", JSON.toJSONString(voucherInfo));
        List<ScfReceivableTransferInfo> transferList = scfReceivableTransferInfoDao
                .listBy("voucherCode", voucherInfo.getVoucherCode(), MatchTypeEnum.EQ);
        // 打回的转让记录不参与本次转让金额统计，也不再更新为审批通过。
        transferList.removeIf(item -> ApprovalStatusEnum.REJECTED.getCode().equals(item.getTransferStatus()));
        BigDecimal totalTransferAmt = transferList.stream()
                .map(ScfReceivableTransferInfo::getTransferAmt)
                .filter(amt -> amt != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        handleVoucherAfterTransfer(voucherInfo, totalTransferAmt);
        // 修改对应转让记录业务状态
        for (ScfReceivableTransferInfo transferInfo : transferList) {
            updateTransferStatusById(transferInfo.getId(), ApprovalStatusEnum.APPROVAL_PASS.getCode());
        }
    }

    @Override
    public void processPayment(ScfReceivablePaymentInfo paymentInfo) {
        log.info("processPayment==paymentInfo:{}", JSON.toJSONString(paymentInfo));
        // 修改凭证信息表状态
        Map<String, Object> params = new HashMap<>();
        params.put("voucherStatus", BusinessStatusEnum.PAYMENT_SUCCESS.getCode());
        params.put("approvalStatus", ApprovalStatusEnum.APPROVAL_PASS.getCode());
        params.put("voucherCode", paymentInfo.getVoucherCode());
        scfReceivableVoucherInfoDao.batchExecute(
                "update ScfReceivableVoucherInfo set voucherStatus = :voucherStatus, approvalStatus = :approvalStatus where voucherCode = :voucherCode and enable = '1'",
                params);
        paymentInfo.setPaymentStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        scfReceivablePaymentInfoDao.update(paymentInfo);
    }

    /**
     * 判断是否生成差额凭证
     *
     * @param voucherInfo      凭证信息
     * @param totalTransferAmt 累计转让金额
     */
    private void handleVoucherAfterTransfer(ScfReceivableVoucherInfo voucherInfo, BigDecimal totalTransferAmt) {
        BigDecimal diffAmt = voucherInfo.getVoucherAmt().subtract(totalTransferAmt);
        if (diffAmt.compareTo(BigDecimal.ZERO) > 0) {
            ScfReceivableVoucherInfo diffVoucher = new ScfReceivableVoucherInfo();
            BeanUtil.copyProperties(voucherInfo, diffVoucher);
            // 封装参数
            diffVoucher.setId(null);
            diffVoucher.setVoucherCode(null);
            diffVoucher.setForehandVoucherCode(voucherInfo.getVoucherCode()); // todo 备录上级凭证编号应该是前端手填,需求调整后修改
            diffVoucher.setVoucherSource(VoucherSourceEnum.MANUAL.getCode());
            diffVoucher.setVoucherType(VoucherTypeEnum.OPEN.getCode());
            // 根债权凭证编号先取母凭证根,取不到根时取母债权凭证编号
            String sourceVoucherCode = ObjectUtil.isNotEmpty(voucherInfo.getSourceVoucherCode()) ? voucherInfo.getSourceVoucherCode() : voucherInfo.getVoucherCode();
            diffVoucher.setSourceVoucherCode(sourceVoucherCode);
            diffVoucher.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_VOUCHER_NO));
            diffVoucher.setVoucherAmt(diffAmt);
            diffVoucher.setVoucherStatus(BusinessStatusEnum.DATA_SUPPLEMENT.getCode());
            diffVoucher.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            diffVoucher.setSignOperation(VoucherSignEnum.SIGNED.getCode());
            scfReceivableVoucherInfoDao.save(diffVoucher);
            // 查询发票信息并复制到新凭证
            List<ScfInvoiceInfo> originalInvoices = scfInvoiceInfoDao.listBy("receivableId", voucherInfo.getId());
            // 发票复制到新凭证（仅应收标识为1的发票）
            if (CollUtil.isNotEmpty(originalInvoices)) {
                List<ScfInvoiceInfo> newInvoices = BeanUtil.copyToList(originalInvoices, ScfInvoiceInfo.class);
                for (ScfInvoiceInfo invoice : newInvoices) {
                    invoice.setId(null);
                    invoice.setReceivableId(diffVoucher.getId());
                    invoice.setPayId(null);
                }
                scfInvoiceInfoDao.batchSaveOrUpdate(newInvoices);
            }
            // 将原债权凭证状态修改为已拆分
                    updateVoucherInfo(ApprovalStatusEnum.APPROVAL_PASS,
                    BusinessStatusEnum.SPLIT,
                    voucherInfo.getVoucherCode());
        } else {
            // 未形成差额修改为业务状态为已转让
                    updateVoucherInfo(ApprovalStatusEnum.APPROVAL_PASS,
                    BusinessStatusEnum.TRANSFERRED,
                    voucherInfo.getVoucherCode());
        }
    }

    /**
     * 按 voucherCode 更新应收凭证的审批状态、业务状态、凭证类型
     */
    private void updateVoucherInfo(ApprovalStatusEnum approvalStatus,
                                   BusinessStatusEnum voucherStatus,
                                   String voucherCode) {
        String hql = "update ScfReceivableVoucherInfo set approvalStatus = :approvalStatus, voucherStatus = :voucherStatus where voucherCode = :voucherCode and enable = '1'";
        Map<String, Object> params = new HashMap<>();
        params.put("approvalStatus", approvalStatus.getCode());
        params.put("voucherStatus", voucherStatus.getCode());
        params.put("voucherCode", voucherCode);
        scfReceivableVoucherInfoDao.batchExecute(hql, params);
    }

    /**
     * 按 voucherCode 更新应收凭证业务状态
     */
    private void updateVoucherStatusByVoucherCode(String voucherCode, String voucherStatus) {
        String hql = "update ScfReceivableVoucherInfo set voucherStatus = :voucherStatus where voucherCode = :voucherCode";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherStatus", voucherStatus);
        params.put("voucherCode", voucherCode);
        scfReceivableVoucherInfoDao.batchExecute(hql, params);
    }

    /**
     * 按转让记录 ID 更新转让状态，避免批量修改同一凭证下的其他转让记录。
     */
    private void updateTransferStatusById(String id, String transferStatus) {
        String hql = "update ScfReceivableTransferInfo set transferStatus = :transferStatus where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("transferStatus", transferStatus);
        params.put("id", id);
        scfReceivableTransferInfoDao.batchExecute(hql, params);
    }
}
