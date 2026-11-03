package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.common.service.ScfInstructionService;
import com.bytter.supply.domain.payable.dao.ScfVoucherRedemptionDao;
import com.bytter.supply.domain.payable.dto.ScfPayableInstructionDTO;
import com.bytter.supply.domain.payable.dto.ScfVoucherRedemptionDTO;
import com.bytter.supply.domain.payable.entity.ScfPayableVoucher;
import com.bytter.supply.domain.payable.entity.ScfVoucherRedemption;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherVO;
import com.bytter.supply.domain.payable.vo.ScfVoucherRedemptionVO;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.enums.SupplyMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WorkFlow({ScfWorkFlowConstant.SCF_VOUCHER_REDEMPTION_FLOWKEY})
@Service("scfVoucherRedemptionService")
@Transactional(rollbackFor = Exception.class)
public class ScfVoucherRedemptionService extends BaseService<ScfVoucherRedemption, String> implements IWorkFlowListener {

    @Autowired
    private ScfVoucherRedemptionDao scfVoucherRedemptionDao;

    @Autowired
    private ScfPayableVoucherService scfPayableVoucherService;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    @Autowired
    private ScfInstructionService scfInstructionService;

    @Autowired
    private IWorkFlowService workFlowService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<ScfVoucherRedemption, String> getEntityDao() {
        return scfVoucherRedemptionDao;
    }

    public DataTablesOutput<ScfVoucherRedemptionVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput<ScfVoucherRedemptionVO> tablesUnion = scfVoucherRedemptionDao.getDataTablesUnion(dataTablesInput);
        if (tablesUnion == null || tablesUnion.getData() == null || tablesUnion.getData().isEmpty()) {
            return tablesUnion;
        }
        Map<String, String> corpNameMap = buildCorpNameMap(tablesUnion.getData());
        for (ScfVoucherRedemptionVO data : tablesUnion.getData()) {
            data.setApprovalStatusName(ScfPayableVoucherConstant.getRedemptionStatusName(data.getApprovalStatus()));
            data.setCorpName(corpNameMap.get(data.getCorpId()));
            data.setCoreCorpName(corpNameMap.get(data.getCoreCorp()));
        }
        return tablesUnion;
    }

    @Transactional
    public void saveVoucherRedemption(ScfVoucherRedemptionDTO dto) {
        ScfVoucherRedemption entity = new ScfVoucherRedemption();
        BeanUtils.copyNotNullProperties(dto, entity);
        entity.setCashNo(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_VOUCHER_REDEEM_NO));
        resolveRedemptionApproval(entity, dto);
        scfVoucherRedemptionDao.saveOrUpdate(entity);
        startWorkflowIfSubmitted(dto.getSaveFlag(), ScfWorkFlowConstant.SCF_VOUCHER_REDEMPTION_FLOWKEY, entity.getId(), dto, entity.getCorpId());
    }

    @Transactional
    public void updateVoucherRedemption(ScfVoucherRedemptionDTO dto) {
        ScfVoucherRedemption entity = new ScfVoucherRedemption();
        BeanUtils.copyNotNullProperties(dto, entity);
        resolveRedemptionApproval(entity, dto);
        scfVoucherRedemptionDao.update(entity);
        startWorkflowIfSubmitted(dto.getSaveFlag(), ScfWorkFlowConstant.SCF_VOUCHER_REDEMPTION_FLOWKEY, entity.getId(), dto, entity.getCorpId());
    }

    @Transactional
    public void deleteVoucherRedemption(String id) {
        ScfVoucherRedemption entity = this.get(id);
        this.deleteEntity(entity);
    }

    public ScfVoucherRedemptionVO getVoucherRedemptionById(String id) {
        ScfVoucherRedemptionVO vo = scfVoucherRedemptionDao.getScfVoucherRedemptionVO(null, id);
        if (vo == null) {
            return null;
        }
        // 补充开立单位名称和核心企业名称
        Set<String> corpIds = new HashSet<>();
        if (StringUtils.isNotEmpty(vo.getCorpId())) {
            corpIds.add(vo.getCorpId());
        }
        if (StringUtils.isNotEmpty(vo.getCoreCorp())) {
            corpIds.add(vo.getCoreCorp());
        }
        if (!corpIds.isEmpty()) {
            Map<String, String> nameMap = corpService.getNameByIds(new ArrayList<>(corpIds));
            if (nameMap != null) {
                vo.setCorpName(nameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
                vo.setCoreCorpName(nameMap.getOrDefault(vo.getCoreCorp(), vo.getCoreCorpName()));
            }
        }
        return vo;
    }

    public ScfVoucherRedemptionVO getInfoByVoucherId(String voucherId) {
        ScfVoucherRedemptionVO voucherInfo = scfVoucherRedemptionDao.getScfVoucherRedemptionVO(voucherId,null);
        if(voucherInfo == null){
            return null;
        }
        // 补充开立单位名称和核心企业名称
        Set<String> corpIds = new HashSet<>();
        if (StringUtils.isNotEmpty(voucherInfo.getCorpId())) {
            corpIds.add(voucherInfo.getCorpId());
        }
        if (StringUtils.isNotEmpty(voucherInfo.getCoreCorp())) {
            corpIds.add(voucherInfo.getCoreCorp());
        }
        if (!corpIds.isEmpty()) {
            Map<String, String> nameMap = corpService.getNameByIds(new ArrayList<>(corpIds));
            if (nameMap != null) {
                voucherInfo.setCorpName(nameMap.getOrDefault(voucherInfo.getCorpId(), voucherInfo.getCorpName()));
                voucherInfo.setCoreCorpName(nameMap.getOrDefault(voucherInfo.getCoreCorp(), voucherInfo.getCoreCorpName()));
            }
        }
        // 补充审批状态名称
        voucherInfo.setApprovalStatusName(ScfPayableVoucherConstant.getRedemptionStatusName(voucherInfo.getApprovalStatus()));
        return voucherInfo;
    }

    private Map<String, String> buildCorpNameMap(List<ScfVoucherRedemptionVO> voList) {
        Set<String> corpIds = new HashSet<>();
        for (ScfVoucherRedemptionVO vo : voList) {
            if (StringUtils.isNotEmpty(vo.getCorpId())) {
                corpIds.add(vo.getCorpId());
            }
            if (StringUtils.isNotEmpty(vo.getCoreCorp())) {
                corpIds.add(vo.getCoreCorp());
            }
        }
        if (corpIds.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> nameMap = corpService.getNameByIds(new ArrayList<>(corpIds));
        return nameMap != null ? nameMap : new HashMap<>();
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        //审批完成
        logger.info("flowCompleted {}", workFlowResult.getBusinessKey());
        ICurrentUser user = workFlowResult.getUser();
        boolean isPass = workFlowResult.isPass();
        ScfVoucherRedemption scfVoucherRedemption = this.get(workFlowResult.getBusinessKey(), true);
        ScfPayableVoucher scfPayableVoucher = scfPayableVoucherService.get(scfVoucherRedemption.getVoucherId());
        // 审批通过
        if (isPass) {
            scfVoucherRedemption.setApprovalStatus(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_CHECK);
            //审批通过需要去修改应付债权凭证的状态
           scfPayableVoucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_REDEEMED);
           scfPayableVoucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_REDEMPTION_CHECK);
        }
        // 审批打回
        else {
            scfVoucherRedemption.setApprovalStatus(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_BACK);// 审批打回
            //兑付打回：审批状态还原为开立时的审批通过
            scfPayableVoucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK);
            //线下凭证：兑付被打回，业务状态还原为“已开立”（未兑付），允许重新提交兑付
            if (ScfPayableVoucherConstant.SCF_PAYABLE_NOT_ONLINE_0.equals(scfPayableVoucher.getOnlineSign())) {
                scfPayableVoucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED);
            }
        }
        this.updateEntity(scfVoucherRedemption);
        scfPayableVoucherService.updateEntity(scfPayableVoucher);

    }

    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        IWorkFlowListener.super.flowRevoke(workFlowResult);
        //打回
    }

    public ScfVoucherRedemptionVO getRedemptionInfo(String voucherId, String id) {
        //两者都没有直接返回null
        if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(voucherId)){
            return null;
        }
        //需要查询当前凭证ID是否有暂存的兑付信息  如果有直接返回暂存的所有信息
        ScfVoucherRedemptionVO infoVO = scfVoucherRedemptionDao.getScfVoucherRedemptionVO(voucherId,id);
        if(infoVO == null){
            infoVO = new ScfVoucherRedemptionVO();
            ScfPayableVoucherVO voucherVO = scfPayableVoucherService.getPayableVoucherBaseInfoById(voucherId);
            BeanUtils.copyNotNullProperties(voucherVO, infoVO, "receiveAccount", "receiveBank");
            infoVO.setId(null);
            infoVO.setVoucherId(voucherId);
        }
        // 查询企业名称和核心企业名称
        Set<String> corpIds = new HashSet<>();
        if (StringUtils.isNotEmpty(infoVO.getCorpId())) {
            corpIds.add(infoVO.getCorpId());
        }
        if (StringUtils.isNotEmpty(infoVO.getCoreCorp())) {
            corpIds.add(infoVO.getCoreCorp());
        }
        if (!corpIds.isEmpty()) {
            Map<String, String> nameMap = corpService.getNameByIds(new ArrayList<>(corpIds));
            if (nameMap != null) {
                infoVO.setCorpName(nameMap.getOrDefault(infoVO.getCorpId(), infoVO.getCorpName()));
                infoVO.setCoreCorpName(nameMap.getOrDefault(infoVO.getCoreCorp(), infoVO.getCoreCorpName()));
            }
        }
        return infoVO;
    }

    public Object getDataTablesUnionApproval(DataTablesInput tablesInput) {
        WorkFlowBusiness flowBusiness = workFlowService.getWorkFlowBusiness(ScfWorkFlowConstant.SCF_VOUCHER_REDEMPTION_FLOWKEY, tablesInput);
        DataTablesOutput<ScfVoucherRedemptionVO> dp = scfVoucherRedemptionDao.getDataTablesUnionApproval(tablesInput);
        if (dp == null || dp.getData() == null || dp.getData().isEmpty()) {
            return flowBusiness.merge(dp);
        }
        Map<String, String> corpNameMap = buildCorpNameMap(dp.getData());
        for (ScfVoucherRedemptionVO data : dp.getData()) {
            data.setApprovalStatusName(ScfPayableVoucherConstant.getRedemptionStatusName(data.getApprovalStatus()));
            data.setCorpName(corpNameMap.get(data.getCorpId()));
            data.setCoreCorpName(corpNameMap.get(data.getCoreCorp()));
        }
        return flowBusiness.merge(dp);
    }

    public Boolean getRedemptionResults(List<String> ids) {
        // 1. 查询应付债权凭证并构建指令DTO（含最新名称）
        List<ScfPayableInstructionDTO> instructionDTOs = new ArrayList<>();
        for (String id : ids) {
            ScfPayableVoucher voucher = scfPayableVoucherService.get(id, true);
            if(!ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1.equals(voucher.getOnlineSign())){

            }
            // 校验凭证状态必须为开立审批通过且已开立
            if (!ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK.equals(voucher.getApprovalStatus())
                    || !ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED.equals(voucher.getVoucherStatus())) {
                throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_NOT_OPENED_FOR_REDEMPTION);
            }
            instructionDTOs.add(scfPayableVoucherService.buildInstructionDTO(voucher));
        }

        // 2. 校验每个凭证对应平台的接口是否开通，收集未开通的平台名称（去重）
        Set<String> checkedPlatformIds = new HashSet<>();
        List<String> notOpenPlatformNames = new ArrayList<>();
        for (ScfPayableInstructionDTO dto : instructionDTOs) {
            String platformId = dto.getPlatformId();
            if (!checkedPlatformIds.contains(platformId)) {
                checkedPlatformIds.add(platformId);
                if (!scfEntBankOpenService.judgeInstructionOpen(platformId, ScfEntBankOpenConstant.PAYABLE_CASH_RESULT_QUERY)) {
                    notOpenPlatformNames.add(dto.getPlatformName());
                }
            }
        }

        // 3. 有平台未开通接口则报错
        if (!notOpenPlatformNames.isEmpty()) {
            throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_PLATFORM_INTERFACE_NOT_OPEN,
                    String.join("、", notOpenPlatformNames),
                    ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.PAYABLE_CASH_RESULT_QUERY));
        }

        // 4. 全部开通，保存兑付结果查询指令
        for (ScfPayableInstructionDTO dto : instructionDTOs) {
            scfInstructionService.savePayableInstruction(dto, ScfEntBankOpenConstant.PAYABLE_CASH_RESULT_QUERY);
        }
        return true;
    }

    private void resolveRedemptionApproval(ScfVoucherRedemption entity, ScfVoucherRedemptionDTO dto) {
        if (ScfPayableVoucherConstant.SCF_PAYABLE_SAVE_FLAG_0.equals(dto.getSaveFlag())) {
            entity.setApprovalStatus(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_TEMP);
        } else if (ScfPayableVoucherConstant.SCF_PAYABLE_SAVE_FLAG_1.equals(dto.getSaveFlag())) {
            if (scfVoucherRedemptionDao.hasConflictRedemption(dto.getVoucherId())) {
                throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_HAS_CONFLICT_REDEMPTION);
            }
            //兑付提交不修改应付债权凭证的审批状态（保持开立审批通过），仅线下凭证同步业务状态为“兑付中”
            ScfPayableVoucher scfPayableVoucher = scfPayableVoucherService.get(dto.getVoucherId());
            if (ScfPayableVoucherConstant.SCF_PAYABLE_NOT_ONLINE_0.equals(scfPayableVoucher.getOnlineSign())) {
                scfPayableVoucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_REDEEMING);
                scfPayableVoucherService.updateEntity(scfPayableVoucher);
            }
            entity.setApprovalStatus(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_WAIT_CHECK);
        }
    }

    private void startWorkflowIfSubmitted(String saveFlag, String flowKey, String businessId, Object dto, String corpId) {
        if (ScfPayableVoucherConstant.SCF_PAYABLE_SAVE_FLAG_1.equals(saveFlag)) {
            ICurrentUser currentUser = getCurrentUser();
            getStartHandler().handler(flowKey, businessId, dto,
                    corpId, currentUser.getId());
        }
    }
}
