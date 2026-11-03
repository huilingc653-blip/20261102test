package com.bytter.supply.domain.receivable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.enums.OnlineEnum;
import com.bytter.supply.domain.receivable.dao.ScfReceivablePaymentInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.domain.receivable.handler.SignHandlerFactory;
import com.bytter.supply.domain.receivable.service.ScfReceivablePaymentInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivablePaymentInfoVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import com.bytter.supply.enums.SupplyMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivablePaymentInfoServiceImpl
 * @Description: 债权凭证收款明细服务实现
 * @since 2026/7/10
 */
@Service
@Slf4j
@WorkFlow({ScfWorkFlowConstant.SCF_FLOW_PAYMENT})
public class ScfReceivablePaymentInfoServiceImpl extends BaseService<ScfReceivablePaymentInfo, String>
        implements ScfReceivablePaymentInfoService, IWorkFlowListener {

    @Autowired
    private ScfReceivablePaymentInfoDao scfReceivablePaymentInfoDao;

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private SignHandlerFactory signHandlerFactory;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    @Override
    public BaseDao<ScfReceivablePaymentInfo, String> getEntityDao() {
        return scfReceivablePaymentInfoDao;
    }

    /**
     * 列表查询
     */
    @Override
    public DataTablesOutput<ScfReceivablePaymentInfoVO> dataTable(DataTablesInput input) {
        DataTablesOutput<ScfReceivablePaymentInfoVO> data = scfReceivablePaymentInfoDao.dataTable(input);
        if (data != null && data.getData() != null) {
            data.getData().forEach(this::fillStatusName);
        }
        if (data != null) {
            data.setDraw(input.getDraw());
            data.setRecordsTotal(data.getRecordsTotal());
            data.setRecordsFiltered(data.getRecordsFiltered());
        }
        return data;
    }

    private void fillStatusName(ScfReceivablePaymentInfoVO paymentInfo) {
        ApprovalStatusEnum status = ApprovalStatusEnum.getByCode(paymentInfo.getPaymentStatus());
        if (status != null) {
            paymentInfo.setPaymentStatusName(status.getDesc());
        }
    }

    @Transactional
    @Override
    public boolean savePaymentInfo(ScfReceivablePaymentInfoDTO paymentInfoDTO) {
        ScfReceivablePaymentInfo paymentInfo = BeanUtil.copyProperties(paymentInfoDTO, ScfReceivablePaymentInfo.class);
        saveEntity(paymentInfo);
        return true;
    }

    @Transactional
    @Override
    public void updatePaymentInfo(ScfReceivablePaymentInfoDTO paymentInfoDTO) {
        ScfReceivablePaymentInfo paymentInfo = BeanUtil.copyProperties(paymentInfoDTO, ScfReceivablePaymentInfo.class);
        scfReceivablePaymentInfoDao.update(paymentInfo);
    }

    @Override
    public boolean deletePaymentInfo(String id) {
        delete(id);
        return true;
    }

    @Override
    public ScfReceivablePaymentInfoVO getPaymentInfo(String voucherCode) {
        ScfReceivablePaymentInfo paymentInfo = scfReceivablePaymentInfoDao.getUnique(
                Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("enable", ScfBusinessConstant.ENABLE_YES));
        if (paymentInfo == null) {
            return null;
        }
        ScfReceivablePaymentInfoVO result = BeanUtil.copyProperties(paymentInfo, ScfReceivablePaymentInfoVO.class);
        fillStatusName(result);
        fillVoucherInfo(result);
        return result;
    }

    @Override
    public ScfReceivablePaymentInfoVO getPaymentInfoById(String id) {
        ScfReceivablePaymentInfo paymentInfo = scfReceivablePaymentInfoDao.get(id);
        if (paymentInfo == null) {
            return null;
        }
        ScfReceivablePaymentInfoVO result = BeanUtil.copyProperties(paymentInfo, ScfReceivablePaymentInfoVO.class);
        fillStatusName(result);
        fillVoucherInfo(result);
        return result;
    }

    /** 填充收款记录关联的应收债权凭证信息。 */
    private void fillVoucherInfo(ScfReceivablePaymentInfoVO paymentInfo) {
        if (StringUtils.isBlank(paymentInfo.getVoucherCode())) {
            return;
        }
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", paymentInfo.getVoucherCode());
        if (voucherInfo != null) {
            paymentInfo.setVoucherInfo(BeanUtil.copyProperties(voucherInfo, ScfReceivableVoucherInfoVO.class));
        }
    }

    @Override
    @Transactional
    public void sendPayment(ScfReceivablePaymentInfoDTO paymentDTO, ICurrentUser currentUser) {
        log.info("债权凭证进行单条收款操作==paymentDTO:{}", JSON.toJSONString(paymentDTO));
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", paymentDTO.getVoucherCode());
        if (ObjectUtil.isEmpty(voucherInfo)) {
            throw new BusinessException("查询应收凭证信息为空!");
        }
        // 付款暂存直接放行，不执行下面的审批+签收校验
        if (!ApprovalStatusEnum.DRAFT.getCode().equals(voucherInfo.getApprovalStatus())) {
            if (!ApprovalStatusEnum.APPROVAL_PASS.getCode().equals(voucherInfo.getApprovalStatus())
                    || !BusinessStatusEnum.SIGNED.getCode().equals(voucherInfo.getVoucherStatus())) {
                throw new BusinessException("仅可对单据状态为\"审批通过\"、业务状态为\"已签收\"的债权凭证进行收款处理。");
            }
        }
        if (StringUtils.isBlank(paymentDTO.getPaymentNo())) {
            paymentDTO.setPaymentNo(SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_PAYMENT_INFO));
        }
        // 币种信息
        paymentDTO.setCurId(voucherInfo.getCurId());
        paymentDTO.setCurName(voucherInfo.getCurName());
        // 平台信息
        paymentDTO.setPlatformId(voucherInfo.getPlatformId());
        paymentDTO.setPlatformName(voucherInfo.getPlatformName());
        // 暂存时收款状态为待收款，提交时为收款中
        if ("save".equals(paymentDTO.getSendStatus())) {
            paymentDTO.setPaymentStatus(ApprovalStatusEnum.DRAFT.getCode());
        } else {
            paymentDTO.setPaymentStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
        }
        paymentDTO.setEnable(ScfBusinessConstant.ENABLE_YES);
        ScfReceivablePaymentInfo paymentInfo = BeanUtil.copyProperties(paymentDTO, ScfReceivablePaymentInfo.class);
        ScfReceivablePaymentInfo paymentData = scfReceivablePaymentInfoDao.getUnique(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("paymentStatus", ApprovalStatusEnum.APPROVAL_ING.getCode()));
        if (ObjectUtil.isNotEmpty(paymentData)){
            paymentInfo.setId(paymentData.getId());
        }else {
            paymentInfo.setId(null);
        }
        scfReceivablePaymentInfoDao.merge(paymentInfo);
        executePayment(paymentDTO, voucherInfo, currentUser);
    }

    private void executePayment(ScfReceivablePaymentInfoDTO paymentDTO, ScfReceivableVoucherInfo voucherInfo, ICurrentUser currentUser) {
        if ("save".equals(paymentDTO.getSendStatus())) {
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            voucherInfo.setVoucherStatus(BusinessStatusEnum.UN_PAYMENT.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
            return;
        }
        voucherInfo.setVoucherStatus(BusinessStatusEnum.PAYMENT_ING.getCode());
        voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        try {
            getStartHandler().handler(
                    ScfWorkFlowConstant.SCF_FLOW_PAYMENT,
                    voucherInfo.getId(),
                    voucherInfo,
                    voucherInfo.getCorpId(),
                    currentUser.getId());
            scfReceivableVoucherInfoDao.update(voucherInfo);
        } catch (Exception e) {
            log.error("债权凭证收款操作启动审批流报错! voucherId={}", voucherInfo.getId(), e);
            throw new BusinessException(String.format("债权凭证收款操作启动审批流报错:%s" + e.getMessage()));
        }
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        log.info("ScfReceivablePaymentInfoServiceImpl==flowCompleted:{}",JSON.toJSONString(workFlowResult));
        String businessKey = workFlowResult.getBusinessKey();
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.get(businessKey);
        if (voucherInfo == null) {
            log.warn("flowCompleted 未找到对应凭证, businessKey={}", businessKey);
            return;
        }
        // 查询付款信息
        ScfReceivablePaymentInfo paymentInfo = scfReceivablePaymentInfoDao.getUnique(
                                                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                                                Restrictions.eq("paymentStatus", ApprovalStatusEnum.APPROVAL_ING.getCode()));
        if (paymentInfo == null) {
            log.warn("flowCompleted 未找到对应付款信息, voucherCode={}", voucherInfo.getVoucherCode());
            return;
        }
        if (workFlowResult.getIsPass()) {
            SignHandler handler = signHandlerFactory.getHandler(voucherInfo.getOnlineSign());
            handler.processPayment(paymentInfo);
            log.info("线下收款审批通过并生成到期收款记录完成, voucherId={}", businessKey);
        } else {
            voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
            paymentInfo.setPaymentStatus(ApprovalStatusEnum.REJECTED.getCode());
            scfReceivablePaymentInfoDao.merge(paymentInfo);
        }
    }


    private ScfReceivablePaymentInfo buildPaymentInfo(ScfReceivableVoucherInfo voucher, ScfReceivablePaymentInfoDTO paymentDTO) {
        ScfReceivablePaymentInfo paymentInfo = new ScfReceivablePaymentInfo();
        paymentInfo.setVoucherCode(voucher.getVoucherCode());
        paymentInfo.setPlatformId(voucher.getPlatformId());
        paymentInfo.setPlatformName(voucher.getPlatformName());
        paymentInfo.setProductId(voucher.getProductId());
        paymentInfo.setProductName(voucher.getProductName());
        paymentInfo.setCoreCompanyId(voucher.getCoreCompanyId());
        paymentInfo.setCoreCompanyName(voucher.getCoreCompanyName());
        paymentInfo.setCurId(voucher.getCurId());
        paymentInfo.setCurName(voucher.getCurName());
        paymentInfo.setVoucherAmt(voucher.getVoucherAmt());
        paymentInfo.setIssueTransferCompany(voucher.getIssueTransferCompany());
        paymentInfo.setIssueTransferCompanyId(voucher.getIssueTransferCompanyId());
        paymentInfo.setExpireDate(voucher.getExpireDate());

        if (paymentDTO != null) {
            paymentInfo.setRecAccountNo(paymentDTO.getRecAccountNo());
            paymentInfo.setRecAccountName(paymentDTO.getRecAccountName());
            paymentInfo.setRecBank(paymentDTO.getRecBank());
            paymentInfo.setRecAmt(paymentDTO.getRecAmt());
            paymentInfo.setReceiptDate(paymentDTO.getReceiptDate());
            paymentInfo.setPaymentNo(StringUtils.isNotBlank(paymentDTO.getPaymentNo())
                    ? paymentDTO.getPaymentNo()
                    : SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_PAYMENT_INFO));
        }
        paymentInfo.setPaymentStatus(ApprovalStatusEnum.DRAFT.getCode());

        return paymentInfo;
    }

    @Transactional
    @Override
    public void temporarySave(ScfReceivablePaymentInfoDTO paymentDTO,ScfReceivableVoucherInfo voucher) {
        if (StringUtils.isBlank(paymentDTO.getVoucherCode())) {
            throw new BusinessException("债权凭证编号不能为空");
        }
        voucher.setApprovalStatus(ApprovalStatusEnum.DRAFT.getCode());
        scfReceivableVoucherInfoDao.update(voucher);

        ScfReceivablePaymentInfo paymentInfo = buildPaymentInfo(voucher, paymentDTO);
        saveOrUpdate(paymentInfo);
        log.info("收款暂存完成, voucherCode={}, paymentCode={}", voucher.getVoucherCode(), paymentInfo.getPaymentNo());
    }
}
