package com.bytter.supply.domain.payable.service;

import cn.hutool.core.bean.BeanUtil;
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
import com.bytter.core.utils.*;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.common.dao.ScfInstructionDao;
import com.bytter.supply.domain.common.dao.ScfInvoiceInfoDao;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.common.service.ScfInstructionService;
import com.bytter.supply.domain.payable.dao.ScfPayableVoucherDao;
import com.bytter.supply.domain.payable.dao.ScfVoucherCustomFieldDao;
import com.bytter.supply.domain.payable.dto.ScfPayableInstructionDTO;
import com.bytter.supply.domain.payable.dto.ScfPayableVoucherCancelDTO;
import com.bytter.supply.domain.payable.dto.ScfPayableVoucherDataDTO;
import com.bytter.supply.domain.payable.dto.ScfVoucherCustomFieldDTO;
import com.bytter.supply.domain.payable.entity.ScfPayableVoucher;
import com.bytter.supply.domain.payable.entity.ScfVoucherCustomField;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherDataVO;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherVO;
import com.bytter.supply.domain.payable.vo.ScfVoucherCustomFieldVO;
import com.bytter.supply.domain.platform.entity.ScfPlatform;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.domain.platform.service.ScfPlatformService;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import com.bytter.supply.enums.SupplyMessageEnum;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@WorkFlow({ScfWorkFlowConstant.SCF_VOUCHER_OPENED_FLOWKEY})
@Service("scfPayableVoucherService")
@Transactional(rollbackFor = Exception.class)
public class ScfPayableVoucherService extends BaseService<ScfPayableVoucher, String> implements IWorkFlowListener {

    @Autowired
    private ScfPayableVoucherDao scfPayableVoucherDao;

    @Autowired
    private ScfVoucherCustomFieldDao scfVoucherCustomFieldDao;

    @Autowired
    private ScfInvoiceInfoDao scfInvoiceInfoDao;

    @Autowired
    private ScfInstructionService scfInstructionService;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    @Autowired
    private ScfInstructionDao scfInstructionDao;

    @Autowired
    private IWorkFlowService workFlowService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;

    @Autowired
    private ScfPlatformService scfPlatformService;

    @Override
    public BaseDao<ScfPayableVoucher, String> getEntityDao() {
        return scfPayableVoucherDao;
    }

    public DataTablesOutput<ScfPayableVoucherDataVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput<ScfPayableVoucherVO> tablesUnion = scfPayableVoucherDao.getDataTablesUnion(dataTablesInput);
        if (tablesUnion == null || tablesUnion.getData() == null || tablesUnion.getData().isEmpty()) {
            return new DataTablesOutput<>();
        }
        List<String> mainIdList = tablesUnion.getData().stream()
                .map(ScfPayableVoucherVO::getId).collect(Collectors.toList());
        Map<String, List<ScfVoucherCustomFieldVO>> customFieldMap = scfVoucherCustomFieldDao.getCustomFieldByMainId(mainIdList);

        Map<String, String> corpNameMap = buildCorpNameMap(tablesUnion.getData());

        List<ScfPayableVoucherDataVO> dataList = new ArrayList<>();
        for (ScfPayableVoucherVO vo : tablesUnion.getData()) {
            ScfPayableVoucherDataVO dataVO = new ScfPayableVoucherDataVO();
            BeanUtils.copyNotNullProperties(vo, dataVO);
            dataVO.setCustomFields(customFieldMap.getOrDefault(vo.getId(), Collections.emptyList()));
            dataVO.setApprovalStatusName(ScfPayableVoucherConstant.getApprovalStatusName(vo.getApprovalStatus()));
            dataVO.setVoucherStatusName(ScfPayableVoucherConstant.getBusinessStatusName(vo.getVoucherStatus()));
            dataVO.setCorpName(corpNameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
            dataList.add(dataVO);
        }
        return new DataTablesOutput<>(tablesUnion, dataList);
    }


    public Object getDataTablesUnionQuery(DataTablesInput tablesInput) {
        DataTablesOutput<ScfPayableVoucherVO> tablesUnion = scfPayableVoucherDao.getDataTablesUnionQuery(tablesInput);
        if (tablesUnion == null || tablesUnion.getData() == null || tablesUnion.getData().isEmpty()) {
            return new DataTablesOutput<>();
        }
        List<String> mainIdList = tablesUnion.getData().stream()
                .map(ScfPayableVoucherVO::getId).collect(Collectors.toList());
        Map<String, List<ScfVoucherCustomFieldVO>> customFieldMap = scfVoucherCustomFieldDao.getCustomFieldByMainId(mainIdList);

        Map<String, String> corpNameMap = buildCorpNameMap(tablesUnion.getData());

        List<ScfPayableVoucherDataVO> dataList = new ArrayList<>();
        for (ScfPayableVoucherVO vo : tablesUnion.getData()) {
            ScfPayableVoucherDataVO dataVO = new ScfPayableVoucherDataVO();
            BeanUtils.copyNotNullProperties(vo, dataVO);
            dataVO.setCustomFields(customFieldMap.getOrDefault(vo.getId(), Collections.emptyList()));
            dataVO.setApprovalStatusName(ScfPayableVoucherConstant.getApprovalStatusName(vo.getApprovalStatus()));
            dataVO.setVoucherStatusName(ScfPayableVoucherConstant.getBusinessStatusName(vo.getVoucherStatus()));
            dataVO.setCorpName(corpNameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
            dataList.add(dataVO);
        }
        return new DataTablesOutput<>(tablesUnion, dataList);
    }

    public Object getDataTablesUnionApproval(DataTablesInput tablesInput) {
        WorkFlowBusiness flowBusiness = workFlowService.getWorkFlowBusiness(ScfWorkFlowConstant.SCF_VOUCHER_OPENED_FLOWKEY, tablesInput);
        DataTablesOutput<ScfPayableVoucherVO> dp = scfPayableVoucherDao.getDataTablesUnionApproval(tablesInput);
        List<String> mainIdList = dp.getData().stream()
                .map(ScfPayableVoucherVO::getId).collect(Collectors.toList());
        Map<String, List<ScfVoucherCustomFieldVO>> customFieldMap = scfVoucherCustomFieldDao.getCustomFieldByMainId(mainIdList);

        Map<String, String> corpNameMap = buildCorpNameMap(dp.getData());

        List<ScfPayableVoucherDataVO> dataList = new ArrayList<>();
        for (ScfPayableVoucherVO vo : dp.getData()) {
            ScfPayableVoucherDataVO dataVO = new ScfPayableVoucherDataVO();
            BeanUtils.copyNotNullProperties(vo, dataVO);
            dataVO.setCustomFields(customFieldMap.getOrDefault(vo.getId(), Collections.emptyList()));
            dataVO.setApprovalStatusName(ScfPayableVoucherConstant.getApprovalStatusName(vo.getApprovalStatus()));
            dataVO.setVoucherStatusName(ScfPayableVoucherConstant.getBusinessStatusName(vo.getVoucherStatus()));
            dataVO.setCorpName(corpNameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
            dataList.add(dataVO);
        }
        DataTablesOutput<ScfPayableVoucherDataVO> tablesOutput = new DataTablesOutput<>(dp, dataList);
        return flowBusiness.merge(tablesOutput);
    }
    @Transactional
    public void savePayableVoucher(ScfPayableVoucherDataDTO dto) {
        //校验债权凭证编号不能重复
        if (StringUtils.isNotEmpty(dto.getVoucherCode())
                && scfPayableVoucherDao.countByVoucherCode(dto.getVoucherCode(), null) > 0) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_CODE_EXIST);
        }
        ScfPayableVoucher entity = new ScfPayableVoucher();
        BeanUtils.copyNotNullProperties(dto, entity);
        entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_VOUCHER_NO));
        resolveApprovalStatus(entity, dto);
        entity.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_UNOPENED);
        ICurrentUser currentUser = getCurrentUser();
        entity.setCreateBy(currentUser.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        scfPayableVoucherDao.save(entity);
        persistInvoiceInfo(entity.getId(), dto.getInvoiceInfo());
        persistCustomFields(entity.getId(), dto.getCustomFields());
        startWorkflowIfSubmitted(dto.getSaveFlag(), ScfWorkFlowConstant.SCF_VOUCHER_OPENED_FLOWKEY, entity.getId(), dto, entity.getCorpId());
    }

    @Transactional
    public void updatePayableVoucher(ScfPayableVoucherDataDTO dto) {
        //校验债权凭证编号不能重复（排除当前记录自身）
        if (StringUtils.isNotEmpty(dto.getVoucherCode())
                && scfPayableVoucherDao.countByVoucherCode(dto.getVoucherCode(), dto.getId()) > 0) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_CODE_EXIST);
        }
        ScfPayableVoucher entity = new ScfPayableVoucher();
        BeanUtils.copyNotNullProperties(dto, entity);
        resolveApprovalStatus(entity, dto);
        ICurrentUser currentUser = getCurrentUser();
        entity.setUpdateBy(currentUser.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        scfPayableVoucherDao.update(entity);
        scfInvoiceInfoDao.deleteByPayId(dto.getId());
        persistInvoiceInfo(dto.getId(), dto.getInvoiceInfo());
        scfVoucherCustomFieldDao.deleteByVoucherId(dto.getId());
        persistCustomFields(dto.getId(), dto.getCustomFields());
        startWorkflowIfSubmitted(dto.getSaveFlag(), ScfWorkFlowConstant.SCF_VOUCHER_OPENED_FLOWKEY, entity.getId(), dto, entity.getCorpId());
    }

    @Transactional
    public void deletePayableVoucher(String id) {
        scfInvoiceInfoDao.deleteByPayId(id);
        scfVoucherCustomFieldDao.deleteByVoucherId(id);
        ScfPayableVoucher entity = this.get(id);
        this.deleteEntity(entity);
    }

    public ScfPayableVoucherDataVO getPayableVoucherDataById(String id) {
        ScfPayableVoucher entity = this.get(id);
        if (entity == null) {
            return null;
        }
        ScfPayableVoucherDataVO dataVO = new ScfPayableVoucherDataVO();
        BeanUtils.copyNotNullProperties(entity, dataVO);
        List<ScfInvoiceInfoVO> invoiceInfoList = scfInvoiceInfoDao.getInvoiceInfoByPayId(id);
        dataVO.setInvoiceInfoList(invoiceInfoList);
        List<ScfVoucherCustomFieldVO> customFields = scfVoucherCustomFieldDao.getCustomFieldByVoucherId(id);
        dataVO.setCustomFields(customFields);
        // 查询开立单位名称（核心企业为手工录入的名称，直接取落库值）
        if (StringUtils.isNotEmpty(entity.getCorpId())) {
            Map<String, String> nameMap = corpService.getNameByIds(
                    Collections.singletonList(entity.getCorpId()));
            if (nameMap != null) {
                dataVO.setCorpName(nameMap.getOrDefault(entity.getCorpId(), ""));
            }
        }
        return dataVO;
    }


    public ScfPayableVoucherVO getPayableVoucherBaseInfoById(String id) {
        return scfPayableVoucherDao.getPayableVoucherVOById(id);
    }

    public Object getEstablishedPayVoucher(DataTablesInput tablesInput) {
        DataTablesOutput<ScfPayableVoucherVO> tablesUnion = scfPayableVoucherDao.getEstablishedPayVoucher(tablesInput);
        if (tablesUnion == null || tablesUnion.getData() == null || tablesUnion.getData().isEmpty()) {
            return new DataTablesOutput<>();
        }
        Map<String, String> corpNameMap = buildCorpNameMap(tablesUnion.getData());
        for (ScfPayableVoucherVO vo : tablesUnion.getData()) {
            vo.setCorpName(corpNameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
        }
        return tablesUnion;
    }

    /**
     * 付款办理51-应付债权凭证选票（datatable 分页）
     * <p>
     * 仅查询"审批通过 + 已开立"的应付债权凭证，且排除已被付款单关联（pay_id 非空）的凭证。
     */
    public DataTablesOutput<ScfPayableVoucherVO> getPayVoucherForSelection(DataTablesInput tablesInput) {
        DataTablesOutput<ScfPayableVoucherVO> tablesUnion = scfPayableVoucherDao.getPayVoucherForSelection(tablesInput);
        if (tablesUnion == null || tablesUnion.getData() == null || tablesUnion.getData().isEmpty()) {
            return new DataTablesOutput<>();
        }
        Map<String, String> corpNameMap = buildCorpNameMap(tablesUnion.getData());
        for (ScfPayableVoucherVO vo : tablesUnion.getData()) {
            vo.setCorpName(corpNameMap.getOrDefault(vo.getCorpId(), vo.getCorpName()));
            vo.setApprovalStatusName(ScfPayableVoucherConstant.getApprovalStatusName(vo.getApprovalStatus()));
            vo.setVoucherStatusName(ScfPayableVoucherConstant.getBusinessStatusName(vo.getVoucherStatus()));
        }
        return tablesUnion;
    }

    @Transactional
    public void revokePayableVoucher(String id) {
        //首先要确认当前数据为 开立中状态 如果不是直接报错
        ScfPayableVoucher entity = this.get(id);
        if (!ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENING.equals(entity.getVoucherStatus())) {
            throw new BusinessException(SupplyMessageEnum.SCF_DATA_STATUS_NOT_ALLOW_REVOKE);
        }

        //校验是否已生成撤销指令
        if (scfInstructionDao.countByVoucherCodeAndInstrType(entity.getVoucherNo(),
                ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL,
                ScfEntBankOpenConstant.SCF_PAYABLE_INSTRUMENT_SIGN_0) > 0) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_HAS_CANCEL_INSTRUCTION);
        }

        if(ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1.equals(entity.getOnlineSign())){
            boolean isInstructionOpen = scfEntBankOpenService.judgeInstructionOpen(entity.getPlatformId(),ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL);
            if(!isInstructionOpen){
                throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN, ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL));
            }
        }
        ScfPayableInstructionDTO instructionDTO = buildInstructionDTO(entity);
        scfInstructionService.savePayableInstruction(instructionDTO, ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL);

        //这里需要后续处理  比如将在开立中的指令删除或者作废
//        entity.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_CANCELED);
        this.updateEntity(entity);
    }

    @Transactional
    public void batchRevokePayableVoucher(List<String> ids) {
        // 先校验所有单据状态，再筛选出无撤销指令的单据
        List<ScfPayableVoucher> entities = new ArrayList<>();
        for (String id : ids) {
            ScfPayableVoucher entity = this.get(id);
            if (!ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENING.equals(entity.getVoucherStatus())) {
                throw new BusinessException(SupplyMessageEnum.SCF_DATA_STATUS_NOT_ALLOW_REVOKE);
            }
            // 跳过已生成撤销指令的单据
            if (scfInstructionDao.countByVoucherCodeAndInstrType(entity.getVoucherNo(),
                    ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL,
                    ScfEntBankOpenConstant.SCF_PAYABLE_INSTRUMENT_SIGN_0) > 0) {
                continue;
            }
            if (ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1.equals(entity.getOnlineSign())) {
                boolean isInstructionOpen = scfEntBankOpenService.judgeInstructionOpen(entity.getPlatformId(), ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL);
                if (!isInstructionOpen) {
                    throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN, ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL));
                }
            }
            entities.add(entity);
        }
        // 如果所有单据都已生成撤销指令，则提示
        if (entities.isEmpty()) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_BATCH_ALL_HAS_CANCEL_INSTRUCTION);
        }
        // 统一执行撤销
        for (ScfPayableVoucher entity : entities) {
            ScfPayableInstructionDTO instructionDTO = buildInstructionDTO(entity);
            scfInstructionService.savePayableInstruction(instructionDTO, ScfEntBankOpenConstant.PAYABLE_VOUCHER_CANCEL);
            this.updateEntity(entity);
        }
    }

    /**
     * 作废应付债权凭证
     * <p>
     * 不走审批流，直接改状态：单据状态、业务状态均置为“已作废”，并记录作废原因与作废时间。
     * 作废原因、作废时间均由前端录入，不使用系统当前时间。
     *
     * @param dto 作废入参（凭证ID、作废原因、作废时间）
     */
    @Transactional
    public void cancelPayableVoucher(ScfPayableVoucherCancelDTO dto) {
        if (dto == null || StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(SupplyMessageEnum.SCF_DATA_ID_NULL);
        }
        if (StringUtils.isEmpty(dto.getCancelReason())) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_CANCEL_REASON_REQUIRED);
        }
        if (dto.getCancelDate() == null) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_CANCEL_DATE_REQUIRED);
        }
        // 仅“已开立/已撤销/已拒签”允许作废
        ScfPayableVoucher entity = this.get(dto.getId());
        if (entity == null || !allowCancel(entity.getVoucherStatus())) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_STATUS_NOT_ALLOW_CANCEL);
        }
        entity.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_CHANGE_REFUSE);// 已作废
        entity.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_VOIDED);// 已作废
        entity.setCancelReason(dto.getCancelReason());
        entity.setCancelDate(dto.getCancelDate());
        entity.setUpdateBy(getCurrentUser().getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.updateEntity(entity);
    }

    /**
     * 判断业务状态是否允许作废（已开立/已撤销/已拒签）
     */
    private boolean allowCancel(String voucherStatus) {
        return ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED.equals(voucherStatus)
                || ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_REJECTED.equals(voucherStatus)
                || ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_CANCELED.equals(voucherStatus);
    }

    /**
     * 从VO列表中收集所有的corpId，批量查询企业名称
     */
    private Map<String, String> buildCorpNameMap(List<ScfPayableVoucherVO> voList) {
        Set<String> corpIds = new HashSet<>();
        for (ScfPayableVoucherVO vo : voList) {
            if (StringUtils.isNotEmpty(vo.getCorpId())) {
                corpIds.add(vo.getCorpId());
            }
        }
        if (corpIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> nameMap = corpService.getNameByIds(new ArrayList<>(corpIds));
        return nameMap != null ? nameMap : Collections.emptyMap();
    }

    /**
     * 从应付凭证实体构建指令DTO，通过源表查询最新名称
     */
    public ScfPayableInstructionDTO buildInstructionDTO(ScfPayableVoucher entity) {
        ScfPayableInstructionDTO dto = new ScfPayableInstructionDTO();
        dto.setVoucherCode(entity.getVoucherCode());
        // 关联编号取凭证号（流水号，唯一且非空）：债权凭证编号由前端录入，可能为空或重复，不能用来定位凭证
        dto.setRelateNo(entity.getVoucherNo());
        dto.setPlatformId(entity.getPlatformId());
        dto.setProductId(entity.getProductId());
        dto.setCorpId(entity.getCorpId());
        dto.setCurId(entity.getCurId());
        dto.setVoucherMoney(entity.getVoucherMoney());
        dto.setExpireDate(entity.getExpireDate());
        dto.setCreateBy(entity.getCreateBy());
        // 查询最新名称
        if (StringUtils.isNotEmpty(entity.getPlatformId())) {
            ScfPlatform platform = scfPlatformService.get(entity.getPlatformId());
            if (platform != null) {
                dto.setPlatformName(platform.getPlatformName());
            }
        }
        if (StringUtils.isNotEmpty(entity.getProductId())) {
            ScfProductVo product = scfPlatformService.getProductById(entity.getProductId());
            if (product != null) {
                dto.setProductName(product.getProductName());
            }
        }
        if (StringUtils.isNotEmpty(entity.getCurId())) {
            Map<String, String> curNameMap = btCurrencyService.getCurrencyNameByIds(
                    Collections.singletonList(entity.getCurId()));
            if (curNameMap != null && curNameMap.containsKey(entity.getCurId())) {
                dto.setCurName(curNameMap.get(entity.getCurId()));
            }
        }
        if (StringUtils.isNotEmpty(entity.getCorpId())) {
            Map<String, String> nameMap = corpService.getNameByIds(
                    Collections.singletonList(entity.getCorpId()));
            if (nameMap != null) {
                dto.setCorpName(nameMap.get(entity.getCorpId()));
            }
        }
        return dto;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        logger.info("flowCompleted {}", workFlowResult.getBusinessKey());
        ICurrentUser user = workFlowResult.getUser();
        boolean isPass = workFlowResult.isPass();
        ScfPayableVoucher scfPayableVoucher = this.get(workFlowResult.getBusinessKey(), true);
        // 审批通过
        if (isPass) {
            //如果是线下的话 直接开立修改状态
            if(scfPayableVoucher.getOnlineSign().equals(ScfPayableVoucherConstant.SCF_PAYABLE_NOT_ONLINE_0)){
                scfPayableVoucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK);// 审批通过
                scfPayableVoucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED);// 已开立
            }
            //暂时不用去判断接口是否开通 因为有暂存数据时 不允许取消掉接口
            //开始拼接指令进行保存
            else{
                ScfPayableInstructionDTO instructionDTO = buildInstructionDTO(scfPayableVoucher);
                scfInstructionService.savePayableInstruction(instructionDTO, ScfEntBankOpenConstant.PAYABLE_VOUCHER_OPEN);
                scfPayableVoucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK);
                scfPayableVoucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENING);
            }
        }
        // 审批打回
        else {
            scfPayableVoucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_BACK);// 审批打回
        }
        this.updateEntity(scfPayableVoucher);
        // 开立审批通过后：按凭证ID反查付款单绑定关系，同步付款单的平台/产品（仅当不一致时回写 nis_scf_info）
        if (isPass) {
            try {
                nisBillheadService.syncScfPlatformProduct(scfPayableVoucher.getId(),
                        scfPayableVoucher.getPlatformId(), scfPayableVoucher.getProductId());
            } catch (Exception e) {
                logger.error("同步付款单绑定平台/产品失败 voucherId={}", scfPayableVoucher.getId(), e);
            }
        }


    }

    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
    }


    private void resolveApprovalStatus(ScfPayableVoucher entity, ScfPayableVoucherDataDTO dto) {
        if (ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_TEMP.equals(dto.getSaveFlag())) {
            entity.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_TEMP);
        } else {
            if (ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1.equals(dto.getOnlineSign())) {
                boolean isInstructionOpen = scfEntBankOpenService.judgeInstructionOpen(
                        entity.getPlatformId(), ScfEntBankOpenConstant.PAYABLE_VOUCHER_OPEN);
                if (!isInstructionOpen) {
                    throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN,
                            ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.PAYABLE_VOUCHER_OPEN));
                }
            }
            entity.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_WAIT_OPENED_CHECK);
        }
    }

    private void persistInvoiceInfo(String payId, List<ScfInvoiceInfoDTO> invoiceInfoList) {
        if (EmptyUtils.isEmpty(invoiceInfoList)) {
            return;
        }
        List<ScfInvoiceInfo> entities = BeanUtil.copyToList(invoiceInfoList, ScfInvoiceInfo.class);
        entities.forEach(item -> item.setPayId(payId));
        scfInvoiceInfoDao.batchSave(entities);
    }

    private void persistCustomFields(String voucherId, List<ScfVoucherCustomFieldDTO> customFields) {
        if (EmptyUtils.isEmpty(customFields)) {
            return;
        }
        List<ScfVoucherCustomField> fieldEntities = new ArrayList<>();
        for (ScfVoucherCustomFieldDTO fieldDTO : customFields) {
            ScfVoucherCustomField fieldEntity = new ScfVoucherCustomField();
            BeanUtils.copyNotNullProperties(fieldDTO, fieldEntity);
            fieldEntity.setVoucherId(voucherId);
            fieldEntities.add(fieldEntity);
        }
        scfVoucherCustomFieldDao.batchSave(fieldEntities);
    }

    private void startWorkflowIfSubmitted(String saveFlag, String flowKey, String businessId, Object dto, String corpId) {
        if (ScfPayableVoucherConstant.SCF_PAYABLE_SAVE_FLAG_1.equals(saveFlag)) {
            ICurrentUser currentUser = getCurrentUser();
            getStartHandler().handler(flowKey, businessId, dto,
                    corpId, currentUser.getId());
        }
    }
}
