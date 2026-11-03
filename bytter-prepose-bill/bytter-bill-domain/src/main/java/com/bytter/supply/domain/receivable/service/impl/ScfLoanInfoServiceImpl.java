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
import org.hibernate.criterion.Restrictions;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.domain.receivable.dao.ScfLoanInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.domain.receivable.handler.SignHandlerFactory;
import com.bytter.supply.domain.receivable.service.ScfLoanInfoService;
import com.bytter.supply.domain.receivable.vo.ScfLoanInfoVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import com.bytter.supply.enums.OnlineEnum;
import com.bytter.supply.enums.SupplyMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfLoanInfoServiceImpl
 * @Description: 融资业务主信息服务实现
 * @since 2026/7/3
 */
@Service
@Slf4j
@WorkFlow({ScfWorkFlowConstant.SCF_FLOW_LOAN})
public class ScfLoanInfoServiceImpl extends BaseService<ScfLoanInfo, String> implements ScfLoanInfoService, IWorkFlowListener {

    @Autowired
    private ScfLoanInfoDao scfLoanInfoDao;

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private SignHandlerFactory signHandlerFactory;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;


    @Override
    public BaseDao<ScfLoanInfo, String> getEntityDao() {
        return scfLoanInfoDao;
    }

    /**
     * 列表查询
     */
    @Override
    public DataTablesOutput<ScfLoanInfoVO> dataTable(DataTablesInput input) {
        DataTablesOutput<ScfLoanInfoVO> data = scfLoanInfoDao.dataTable(input);
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

    private void fillStatusName(ScfLoanInfoVO loanInfo) {
        ApprovalStatusEnum status = ApprovalStatusEnum.getByCode(loanInfo.getLoanStatus());
        if (status != null) {
            loanInfo.setLoanStatusName(status.getDesc());
        }
    }

    @Override
    @Transactional
    public boolean sendLoan(ScfLoanInfoDTO loanInfoDTO, ICurrentUser currentUser) {
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", loanInfoDTO.getVoucherCode());
        if (ObjectUtil.isEmpty(voucherInfo)) {
            throw new RuntimeException("融资暂存查询对应应收凭证异常!");
        }
        if (StringUtils.isBlank(loanInfoDTO.getLoanNo())) {
            loanInfoDTO.setLoanNo(SequenceSingleton.getSequence(SequenceEnum.SCF_RZ_LOAN_NO));
        }
        if (ScfBusinessConstant.INTEREST_MODE_BUYER.equals(loanInfoDTO.getInterestMode())) {
            BigDecimal zero = BigDecimal.ZERO;
            loanInfoDTO.setLoanRate(zero);
            loanInfoDTO.setLoanInterestAmt(zero);
            loanInfoDTO.setLoanHandFeeRate(zero);
            loanInfoDTO.setLoanHandFeeAmt(zero);
            loanInfoDTO.setLoanServiceFeeRate(zero);
            loanInfoDTO.setLoanServiceFeeAmt(zero);
            loanInfoDTO.setLoanCostAmt(zero);
        }
        // 币种信息
        loanInfoDTO.setCurId(voucherInfo.getCurId());
        loanInfoDTO.setCurName(voucherInfo.getCurName());
        // 平台信息
        loanInfoDTO.setPlatformId(voucherInfo.getPlatformId());
        loanInfoDTO.setPlatformName(voucherInfo.getPlatformName());
        // 融资记录状态按审批流转，凭证业务状态按融资生命周期流转
        if ("save".equals(loanInfoDTO.getSendStatus())) {
            loanInfoDTO.setLoanStatus(ApprovalStatusEnum.DRAFT.getCode());
        } else {
            loanInfoDTO.setLoanStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
        }
        loanInfoDTO.setEnable(ScfBusinessConstant.ENABLE_YES);
        loanInfoDTO.setReceivableId(voucherInfo.getId());
        ScfLoanInfo loanInfo = BeanUtil.copyProperties(loanInfoDTO, ScfLoanInfo.class);
        scfLoanInfoDao.merge(loanInfo);
        executeLoan(loanInfoDTO, voucherInfo, currentUser);
        return Boolean.TRUE;
    }

    @Override
    public void updateLoanInfo(ScfLoanInfoDTO loanInfoDTO) {
        ScfLoanInfo loanInfo = BeanUtil.copyProperties(loanInfoDTO, ScfLoanInfo.class);
        updateEntity(loanInfo);
    }


    private boolean executeLoan(ScfLoanInfoDTO loanInfoDTO,ScfReceivableVoucherInfo voucherInfo,ICurrentUser currentUser) {
        if (loanInfoDTO.getSendStatus().equals("save")){  //暂存
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            voucherInfo.setVoucherStatus(BusinessStatusEnum.UN_LOAN.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
        }else {  //提交
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            voucherInfo.setVoucherStatus(BusinessStatusEnum.LOAN_ING.getCode());
            if (OnlineEnum.ONLINE.getCode().equals(voucherInfo.getOnlineSign())) {
                boolean judged = scfEntBankOpenService.judgeInstructionOpen(loanInfoDTO.getPlatformId(), ScfEntBankOpenConstant.RECEIVABLE_FINANCE_APPLY);
                if (!judged) {
                    throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN,
                            ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.RECEIVABLE_FINANCE_APPLY));
                }
            }
            try {
                getStartHandler().handler(
                        ScfWorkFlowConstant.SCF_FLOW_LOAN,
                        voucherInfo.getId(),
                        voucherInfo,
                        voucherInfo.getCorpId(),
                        currentUser.getId());
                scfReceivableVoucherInfoDao.update(voucherInfo);
            } catch (Exception e) {
                log.error("应收凭证进行融资操作启动审批流报错!", e);
                throw new BusinessException(String.format("应收凭证进行融资操作启动审批流报错:%s" + e));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean deleteLoanInfo(String id) {
        delete(id);
        return true;
    }

    @Override
    public ScfLoanInfoVO getLoanInfo(String id) {
        ScfLoanInfo scfLoanInfo = get(id);
        if (scfLoanInfo == null) {
            return null;
        }
        ScfLoanInfoVO result = BeanUtil.copyProperties(scfLoanInfo, ScfLoanInfoVO.class);
        fillStatusName(result);
        fillVoucherInfo(result);
        return result;
    }

    @Override
    public ScfLoanInfoVO getLoanInfoByVoucherCode(String voucherCode) {
        ScfLoanInfo scfLoanInfo = scfLoanInfoDao.getUnique(
                Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("enable", "1")
        );
        if (scfLoanInfo == null) {
            return null;
        }
        ScfLoanInfoVO result = BeanUtil.copyProperties(scfLoanInfo, ScfLoanInfoVO.class);
        fillStatusName(result);
        fillVoucherInfo(result);
        return result;
    }

    /** 填充融资关联的应收债权凭证信息。 */
    private void fillVoucherInfo(ScfLoanInfoVO loanInfo) {
        if (StringUtils.isBlank(loanInfo.getVoucherCode())) {
            return;
        }
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", loanInfo.getVoucherCode());
        if (voucherInfo != null) {
            loanInfo.setVoucherInfo(BeanUtil.copyProperties(voucherInfo, ScfReceivableVoucherInfoVO.class));
        }
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        log.info("供应链融资审批==workFlowResult:{}", JSON.toJSONString(workFlowResult));
        String businessKey = workFlowResult.getBusinessKey();
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.get(businessKey);
        if (ObjectUtil.isEmpty(voucherInfo)) {
            return;
        }
        ScfLoanInfo loanInfo = scfLoanInfoDao.getUnique(Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                                                        Restrictions.eq("loanStatus", ApprovalStatusEnum.APPROVAL_ING.getCode()));
        if (loanInfo == null) {
            return;
        }
        if (workFlowResult.getIsPass()) {
            SignHandler handler = signHandlerFactory.getHandler(voucherInfo.getOnlineSign());
            handler.processLoan(loanInfo);
        }else {
            voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
            log.info("供应链融资审批拒绝, voucherId={}", businessKey);
            loanInfo.setLoanStatus(ApprovalStatusEnum.REJECTED.getCode());
            scfLoanInfoDao.merge(loanInfo);
        }
    }
}
