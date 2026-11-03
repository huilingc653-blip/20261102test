package com.bytter.supply.domain.receivable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.bill.domain.bill.service.BtCurrencyService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.SqlUtil;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.platform.dao.ScfProductDao;
import com.bytter.supply.domain.platform.entity.ScfPlatform;
import com.bytter.supply.domain.platform.entity.ScfProduct;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.domain.platform.service.ScfPlatformService;
import com.bytter.supply.domain.common.entity.ScfInstruction;
import com.bytter.supply.domain.common.service.ScfInstructionService;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.enums.*;
import com.bytter.supply.domain.common.dao.ScfInvoiceInfoDao;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.receivable.dao.ScfLoanInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivablePaymentInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableTransferInfoDao;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherBatchSignDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.domain.receivable.handler.SignHandlerFactory;
import com.bytter.supply.domain.common.service.ScfInvoiceInfoService;
import com.bytter.supply.domain.receivable.service.ScfReceivableVoucherInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherImportVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfoServiceImpl
 * @Description: 应收债权凭证信息服务实现
 * @since 2026/6/30
 */
@Service
@Slf4j
@WorkFlow({ScfWorkFlowConstant.SCF_FLOW_RECEIVABLE_VOUCHER})
public class ScfReceivableVoucherInfoServiceImpl extends BaseService<ScfReceivableVoucherInfo, String> implements ScfReceivableVoucherInfoService, IWorkFlowListener {

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private ScfInvoiceInfoService scfInvoiceInfoService;

    @Autowired
    private ScfInvoiceInfoDao scfInvoiceInfoDao;

    @Autowired
    private ScfReceivableTransferInfoDao scfReceivableTransferInfoDao;

    @Autowired
    private ScfLoanInfoDao scfLoanInfoDao;

    @Autowired
    private ScfReceivablePaymentInfoDao scfReceivablePaymentInfoDao;

    @Autowired
    private BtCurrencyService btCurrencyService;

    @Autowired
    private ScfPlatformService scfPlatformService;

    @Autowired
    private ScfProductDao scfProductDao;

    @Autowired
    private SignHandlerFactory signHandlerFactory;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    @Autowired
    private ScfInstructionService scfInstructionService;

    @Autowired
    private IWorkFlowService workFlowService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @Override
    public BaseDao<ScfReceivableVoucherInfo, String> getEntityDao() {
        return scfReceivableVoucherInfoDao;
    }

    /**
     * 列表查询
     */
    @Override
    public DataTablesOutput<ScfReceivableVoucherInfoVO> dataTable(DataTablesInput input) {
        DataTablesOutput<ScfReceivableVoucherInfoVO> data = scfReceivableVoucherInfoDao.dataTable(input);
        List<ScfReceivableVoucherInfoVO> result = data.getData();
        if (CollectionUtil.isEmpty(result)) {
            return data;
        }
        //填充枚举名称
        result.forEach(item -> {
            fillEnumText(item);
        });
        if (data != null) {
            data.setDraw(input.getDraw());
            data.setRecordsTotal(data.getRecordsTotal());
            data.setRecordsFiltered(data.getRecordsFiltered());
            data.setData(result);
        }
        return data;
    }

    /**
     * 填充枚举展示名称
     */
    private void fillEnumText(ScfReceivableVoucherInfoVO item) {
        // 审批状态
        if (ObjectUtil.isNotEmpty(item.getApprovalStatus())) {
            ApprovalStatusEnum approvalStatusEnum = ApprovalStatusEnum.getByCode(item.getApprovalStatus());
            if (ObjectUtil.isNotEmpty(approvalStatusEnum)) {
                item.setApprovalStatusName(approvalStatusEnum.getDesc());
            }
        }
        // 单据业务状态
        if (ObjectUtil.isNotEmpty(item.getVoucherStatus())) {
            BusinessStatusEnum businessStatusEnum = BusinessStatusEnum.getByCode(item.getVoucherStatus());
            if (ObjectUtil.isNotEmpty(businessStatusEnum)) {
                item.setVoucherStatusName(businessStatusEnum.getDesc());
            }
        }
        // 单据类型
        if (ObjectUtil.isNotEmpty(item.getVoucherType())) {
            VoucherTypeEnum voucherTypeEnum = VoucherTypeEnum.getByCode(item.getVoucherType());
            if (ObjectUtil.isNotEmpty(voucherTypeEnum)) {
                item.setVoucherTypeName(voucherTypeEnum.getDesc());
            }
        }
    }

    @Transactional
    @Override
    public boolean saveVoucherInfo(ScfReceivableVoucherInfoDTO voucherInfoDTO, ICurrentUser currentUser) {
        String sendStatus = voucherInfoDTO.getSendStatus();
        boolean isDraftSave = "save".equals(sendStatus);

        // 公共赋值逻辑抽取，仅审批状态区分
        fillCommonVoucherField(voucherInfoDTO, isDraftSave);
        //转换并保存主表
        ScfReceivableVoucherInfo voucherInfo = BeanUtil.copyProperties(voucherInfoDTO, ScfReceivableVoucherInfo.class);
        if (StrUtil.isBlank(voucherInfo.getId())) {
            if (StrUtil.isNotBlank(voucherInfoDTO.getVoucherCode())) {
                long count = scfReceivableVoucherInfoDao.countByVoucherCode(voucherInfoDTO.getVoucherCode());
                if (count > 0) {
                    throw new BusinessException("债权编号已存在，请勿重复录入");
                }
            }
            scfReceivableVoucherInfoDao.save(voucherInfo);
        } else {
            scfReceivableVoucherInfoDao.update(voucherInfo);
        }
        // 批量保存发票明细
        batchSaveInvoiceInfo(voucherInfoDTO, voucherInfo.getId());
        //启动工作流
        if (!isDraftSave) {
            getStartHandler().handler(
                    ScfWorkFlowConstant.SCF_FLOW_RECEIVABLE_VOUCHER,
                    voucherInfo.getId(),
                    voucherInfo,
                    voucherInfo.getCorpId(),
                    currentUser.getId()
            );
        }
        return Boolean.TRUE;
    }

    /**
     * 填充单据公共字段（编号、来源、币种、审批/单据状态）
     * @param dto 传输对象
     * @param isDraft 是否草稿暂存 save
     */
    private void fillCommonVoucherField(ScfReceivableVoucherInfoDTO dto, boolean isDraft) {
        // 生成单据编号
        String voucherNo = SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_VOUCHER_NO);
        dto.setVoucherNo(voucherNo);
        // 固定业务状态、来源
        dto.setVoucherStatus(BusinessStatusEnum.UNSIGNED.getCode());
        dto.setVoucherSource(VoucherSourceEnum.MANUAL.getCode());
        // 区分草稿/提交审批状态
        if (isDraft) {
            dto.setApprovalStatus(ApprovalStatusEnum.DRAFT.getCode());
        } else {
            dto.setSignOperation(VoucherSignEnum.SIGNED.getCode());
            dto.setApprovalStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
        }
        // 回填币种名称
        BtCurrencyDTO currencyDTO = btCurrencyService.get(dto.getCurId());
        if (ObjectUtil.isNotEmpty(currencyDTO)) {
            dto.setCurName(currencyDTO.getCurName());
        }
    }
    /**
     * 批量保存应收单关联发票明细
     * @param voucherDTO 单据DTO
     * @param receivableId 主表主键ID
     */
    private void batchSaveInvoiceInfo(ScfReceivableVoucherInfoDTO voucherDTO, String receivableId) {
        // 1. 删除该应收单下全部历史发票明细
        scfInvoiceInfoDao.deleteByReceivableId(receivableId);
        List<ScfInvoiceInfoDTO> invoiceDtoList = voucherDTO.getInvoiceInfo();
        if (CollectionUtil.isEmpty(invoiceDtoList)) {
            // 前端不传发票，删完旧数据直接返回
            return;
        }
        // 2. DTO转实体 + 赋值主表ID
        List<ScfInvoiceInfo> invoiceEntityList = BeanUtil.copyToList(invoiceDtoList, ScfInvoiceInfo.class);
        invoiceEntityList.forEach(item -> {
            item.setReceivableId(receivableId);
            item.setId(null);
        });
        // 3. 批量新增当前最新明细
        scfInvoiceInfoDao.batchSaveOrUpdate(invoiceEntityList);
    }

    @Transactional
    @Override
    public void updateVoucherInfo(ScfReceivableVoucherInfoDTO voucherInfoDTO) {
        ScfReceivableVoucherInfo voucherInfo = BeanUtil.copyProperties(voucherInfoDTO, ScfReceivableVoucherInfo.class);
        updateEntity(voucherInfo);

        if (voucherInfoDTO.getInvoiceInfo() != null) {
            String voucherId = voucherInfo.getId();
            List<ScfInvoiceInfo> oldInvoiceList = scfInvoiceInfoService.listBy("receivableId", voucherId);
            Map<String, ScfInvoiceInfo> oldInvoiceMap = oldInvoiceList.stream()
                    .collect(Collectors.toMap(ScfInvoiceInfo::getId, Function.identity(), (a, b) -> a));

            List<ScfInvoiceInfoDTO> invoiceInfoDTOList = voucherInfoDTO.getInvoiceInfo();
            List<String> newInvoiceIds = new ArrayList<>();
            for (ScfInvoiceInfoDTO dto : invoiceInfoDTOList) {
                ScfInvoiceInfo invoiceInfo;
                String dtoId = dto.getId();
                if (StrUtil.isNotBlank(dtoId) && oldInvoiceMap.containsKey(dtoId)) {
                    invoiceInfo = oldInvoiceMap.get(dtoId);
                    BeanUtil.copyProperties(dto, invoiceInfo, "id");
                } else {
                    invoiceInfo = BeanUtil.copyProperties(dto, ScfInvoiceInfo.class);
                    invoiceInfo.setId(null);
                }
                invoiceInfo.setReceivableId(voucherId);
                scfInvoiceInfoDao.saveOrUpdate(invoiceInfo);
                newInvoiceIds.add(invoiceInfo.getId());
            }
            scfInvoiceInfoDao.flush();

            // 删除不在新列表的旧发票
            List<String> finalNewInvoiceIds = newInvoiceIds;
            List<String> toDeleteIds = oldInvoiceList.stream()
                    .map(ScfInvoiceInfo::getId)
                    .filter(id -> !finalNewInvoiceIds.contains(id))
                    .collect(Collectors.toList());
            if (!toDeleteIds.isEmpty()) {
                scfInvoiceInfoDao.delete(toDeleteIds);
            }
        }
    }

    /**
     * 按暂存业务类型删除应收债权凭证暂存数据
     * <p>
     * 仅允许删除暂存数据：凭证当前审批状态必须与 type 对应的暂存状态一致，否则不允许删除。
     * 凭证开立暂存直接删除凭证信息表记录；融资/转让/提示付款暂存删除对应业务信息表记录，并将主凭证信息表状态恢复为"开立审批通过、已签收"。
     */
    @Override
    @Transactional
    public boolean deleteVoucherInfo(String id, String approvalStatus) {
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.get(id);
        if (voucherInfo == null) {
            throw new BusinessException("凭证信息不存在!");
        }
        ApprovalStatusEnum statusEnum = ApprovalStatusEnum.getByCode(approvalStatus);
        if (statusEnum == null) {
            throw new BusinessException("不支持的删除类型: " + approvalStatus);
        }
        // 非暂存数据不允许删除：仅当凭证当前状态与类型对应的暂存状态一致时才允许删除
        if (!approvalStatus.equals(voucherInfo.getApprovalStatus())) {
            throw new BusinessException("仅可删除[" + statusEnum.getDesc() + "]数据，当前凭证状态不允许删除!");
        }
        // 审批状态已统一为“暂存”，根据凭证业务状态和关联业务记录判断暂存类型。
        if (BusinessStatusEnum.UNSIGNED.getCode().equals(voucherInfo.getVoucherStatus())) {
            delete(id);
            return true;
        }
        String voucherCode = voucherInfo.getVoucherCode();
        if (!scfLoanInfoDao.list(Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("loanStatus", ApprovalStatusEnum.DRAFT.getCode())).isEmpty()) {
            return deleteLoanDraft(voucherInfo);
        }
        if (!scfReceivableTransferInfoDao.list(Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode())).isEmpty()) {
            return deleteTransferDraft(voucherInfo);
        }
        if (!scfReceivablePaymentInfoDao.list(Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("paymentStatus", ApprovalStatusEnum.DRAFT.getCode())).isEmpty()) {
            return deletePaymentDraft(voucherInfo);
        }
        return Boolean.TRUE;
    }

    /**
     * 删除融资暂存数据：删除融资业务信息表记录，并恢复凭证审批状态为"开立审批通过"、业务状态为"已签收"
     */
    private boolean deleteLoanDraft(ScfReceivableVoucherInfo voucherInfo) {
        List<ScfLoanInfo> loanList = scfLoanInfoDao.list(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("loanStatus", ApprovalStatusEnum.DRAFT.getCode()));
        for (ScfLoanInfo loanInfo : loanList) {
            scfLoanInfoDao.delete(loanInfo.getId());
        }
        releaseVoucherDraftStatus(voucherInfo);
        return true;
    }

    /**
     * 删除转让暂存数据：删除转让业务信息表记录，并恢复凭证审批状态为"开立审批通过"、业务状态为"已签收"
     */
    private boolean deleteTransferDraft(ScfReceivableVoucherInfo voucherInfo) {
        List<ScfReceivableTransferInfo> transferList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()));
        for (ScfReceivableTransferInfo transferInfo : transferList) {
            scfReceivableTransferInfoDao.delete(transferInfo.getId());
        }
        releaseVoucherDraftStatus(voucherInfo);
        return true;
    }

    /**
     * 删除提示付款暂存数据：删除收款业务信息表记录，并恢复凭证审批状态为"开立审批通过"、业务状态为"已签收"
     */
    private boolean deletePaymentDraft(ScfReceivableVoucherInfo voucherInfo) {
        List<ScfReceivablePaymentInfo> paymentList = scfReceivablePaymentInfoDao.list(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("paymentStatus", ApprovalStatusEnum.DRAFT.getCode()));
        for (ScfReceivablePaymentInfo paymentInfo : paymentList) {
            scfReceivablePaymentInfoDao.delete(paymentInfo.getId());
        }
        releaseVoucherDraftStatus(voucherInfo);
        return true;
    }

    /**
     * 释放凭证暂存状态：恢复为"开立审批通过、已签收"
     */
    private void releaseVoucherDraftStatus(ScfReceivableVoucherInfo voucherInfo) {
        voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
        scfReceivableVoucherInfoDao.update(voucherInfo);
    }

    @Override
    public ScfReceivableVoucherInfoVO getVoucherInfo(String id) {
        ScfReceivableVoucherInfo scfReceivableVoucherInfo = scfReceivableVoucherInfoDao.get(id);
        if (scfReceivableVoucherInfo == null) {
            return null;
        }
        return buildVoucherInfoVO(scfReceivableVoucherInfo);
    }

    /**
     * 组装凭证详情及其已完成的业务记录。
     */
    private ScfReceivableVoucherInfoVO buildVoucherInfoVO(ScfReceivableVoucherInfo scfReceivableVoucherInfo) {
        ScfReceivableVoucherInfoVO infoVO = BeanUtil.copyProperties(scfReceivableVoucherInfo, ScfReceivableVoucherInfoVO.class);
        extractedVoucher(infoVO);
        // 设置发票信息
        List<ScfInvoiceInfo> invoiceList = scfInvoiceInfoService.listBy("receivableId", scfReceivableVoucherInfo.getId());
        if (CollectionUtil.isNotEmpty(invoiceList)) {
            List<ScfInvoiceInfoDTO> invoiceInfoDTO = BeanUtil.copyToList(invoiceList, ScfInvoiceInfoDTO.class);
            infoVO.setInvoiceInfo(invoiceInfoDTO);
        }
        // 设置转让信息（仅查询转让成功的数据）
        String voucherCode = scfReceivableVoucherInfo.getVoucherCode();
        List<ScfReceivableTransferInfo> transferList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("transferStatus", ApprovalStatusEnum.APPROVAL_PASS.getCode())
        );
        if (CollectionUtil.isNotEmpty(transferList)) {
            infoVO.setTransferInfo(BeanUtil.copyToList(transferList, ScfReceivableTransferInfoDTO.class));
        }
        // 设置融资信息（仅查询融资成功的数据）
        List<ScfLoanInfo> loanList = scfLoanInfoDao.list(
                Restrictions.eq("receivableId", scfReceivableVoucherInfo.getId()),
                Restrictions.eq("loanStatus", ApprovalStatusEnum.APPROVAL_PASS.getCode())
        );
        if (CollectionUtil.isNotEmpty(loanList)) {
            infoVO.setLoanInfo(BeanUtil.copyToList(loanList, ScfLoanInfoDTO.class));
        }
        // 设置提示付款信息（仅查询收款成功的数据）
        List<ScfReceivablePaymentInfo> paymentList = scfReceivablePaymentInfoDao.list(
                Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("paymentStatus", ApprovalStatusEnum.APPROVAL_PASS.getCode())
        );
        if (CollectionUtil.isNotEmpty(paymentList)) {
            infoVO.setPaymentInfo(BeanUtil.copyToList(paymentList, ScfReceivablePaymentInfoDTO.class));
        }
        return infoVO;
    }

    @Override
    public List<ScfReceivableVoucherInfoVO> getVoucherInfoList(String id) {
        List<ScfReceivableVoucherInfo> receivableVoucherInfos = scfReceivableVoucherInfoDao.listBy("id", id, MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(receivableVoucherInfos)){
            return Collections.emptyList();
        }
        List<ScfReceivableVoucherInfoVO> receivableVoucherInfoVOS = BeanUtil.copyToList(receivableVoucherInfos, ScfReceivableVoucherInfoVO.class);
        receivableVoucherInfoVOS.forEach(item->{
            extractedVoucher(item);
        });
        return receivableVoucherInfoVOS;
    }

    /**
     * 付款办理（付款方式52）转让选择：datatable 分页查询可转让的应收债权凭证（单据状态=开立审批通过 且 业务状态=已签收），
     * 编辑时前端回传的已选择凭证id（selectedIds）一并返回（or id in selectedIds），用于回显已选择数据（即使已进入转让暂存）
     *
     * @param dataTablesInput datatable 分页/排序/检索参数（凭证编号/开立单位/平台/产品等检索条件由前端写在 formdata 的 params 中）
     * @param selectedIds     前端已选择的凭证id集合（逗号分隔或数组，可空）
     * @return 可转让应收债权凭证分页结果
     */
    @Override
    public DataTablesOutput<ScfReceivableVoucherInfoVO> listForPayTransfer(DataTablesInput dataTablesInput, String selectedIds) {
        List<String> ids = new ArrayList<>();
        if (StrUtil.isNotBlank(selectedIds)) {
            ids = Arrays.asList(selectedIds.split(","));
        }
        DataTablesOutput<ScfReceivableVoucherInfoVO> data = scfReceivableVoucherInfoDao.getReceivableVoucherForSelection(dataTablesInput, ids);
        List<ScfReceivableVoucherInfoVO> result = data.getData();
        if (CollectionUtil.isEmpty(result)) {
            return data;
        }
        // 填充枚举名称（与主列表 dataTable 一致）
        result.forEach(item -> {
            fillEnumText(item);
        });
        data.setDraw(dataTablesInput.getDraw());
        data.setRecordsTotal(data.getRecordsTotal());
        data.setRecordsFiltered(data.getRecordsFiltered());
        data.setData(result);
        return data;
    }

    private static void extractedVoucher(ScfReceivableVoucherInfoVO item) {
        if (ObjectUtil.isNotEmpty(item.getApprovalStatus())) {
            ApprovalStatusEnum approvalStatusEnum = ApprovalStatusEnum.getByCode(item.getApprovalStatus());
            if (ObjectUtil.isNotEmpty(approvalStatusEnum)) {
                item.setApprovalStatusName(approvalStatusEnum.getDesc());
            }
        }
        if (ObjectUtil.isNotEmpty(item.getVoucherStatus())) {
            BusinessStatusEnum businessStatusEnum = BusinessStatusEnum.getByCode(item.getVoucherStatus());
            if (ObjectUtil.isNotEmpty(businessStatusEnum)) {
                item.setVoucherStatusName(businessStatusEnum.getDesc());
            }
        }
    }

    @Override
    public ScfReceivableVoucherInfo getVoucherInfoByCode(String voucherCode) {
        return scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", voucherCode);
    }

    @Transactional
    @Override
    public void queryFinanceResult(String voucherCode) {
        ScfReceivableVoucherInfo voucherInfo = getOnlineVoucherForResultQuery(
                voucherCode,
                ScfEntBankOpenConstant.RECEIVABLE_FINANCE_RESULT_QUERY,
                BusinessStatusEnum.LOAN_ING);
        ScfLoanInfo loanInfo = scfLoanInfoDao.getUnique(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("enable", ScfBusinessConstant.ENABLE_YES));
        if (loanInfo == null) {
            throw new BusinessException("未找到对应的融资信息，凭证编号：" + voucherInfo.getVoucherCode());
        }
        ScfInstruction instruction = buildResultQueryInstruction(
                voucherInfo,
                ScfEntBankOpenConstant.RECEIVABLE_FINANCE_RESULT_QUERY,
                loanInfo.getLoanNo(),
                loanInfo.getLoanAmt(),
                loanInfo.getLoanCompanyId(),
                loanInfo.getLoanCompanyName(),
                loanInfo.getCreateBy());
        scfInstructionService.saveReceivableInstruction(instruction);
    }

    @Transactional
    @Override
    public void queryDueCollectResult(String voucherCode) {
        ScfReceivableVoucherInfo voucherInfo = getOnlineVoucherForResultQuery(
                voucherCode, null, BusinessStatusEnum.PAYMENT_ING);
        ScfReceivablePaymentInfo paymentInfo = scfReceivablePaymentInfoDao.getUnique(
                Restrictions.eq("voucherCode", voucherInfo.getVoucherCode()),
                Restrictions.eq("enable", ScfBusinessConstant.ENABLE_YES));
        if (paymentInfo == null) {
            throw new BusinessException("未找到对应的到期收款信息，凭证编号：" + voucherInfo.getVoucherCode());
        }
    }

    /**
     * 交易结果查询只允许指定业务状态的线上凭证，并校验对应查询接口已开通。
     */
    private ScfReceivableVoucherInfo getOnlineVoucherForResultQuery(String voucherCode,
                                                                    String instrType,
                                                                    BusinessStatusEnum requiredStatus) {
        if (StrUtil.isBlank(voucherCode)) {
            throw new BusinessException("债权凭证编号不能为空");
        }
        ScfReceivableVoucherInfo voucherInfo = getVoucherInfoByCode(voucherCode);
        if (voucherInfo == null) {
            throw new BusinessException("未找到对应的债权凭证信息，凭证编号：" + voucherCode);
        }
        if (!requiredStatus.getCode().equals(voucherInfo.getVoucherStatus())) {
            throw new BusinessException("仅允许业务状态为“" + requiredStatus.getDesc() + "”的债权凭证进行结果查询!");
        }
        if (!OnlineEnum.ONLINE.getCode().equals(voucherInfo.getOnlineSign())) {
            throw new BusinessException(SupplyMessageEnum.SCF_VOUCHER_ONLY_ONLINE_OPERATE);
        }
        if (StrUtil.isNotBlank(instrType)
                && !scfEntBankOpenService.judgeInstructionOpen(voucherInfo.getPlatformId(), instrType)) {
            throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN,
                    ScfEntBankOpenConstant.getInterfaceName(instrType));
        }
        return voucherInfo;
    }

    private ScfInstruction buildResultQueryInstruction(ScfReceivableVoucherInfo voucherInfo,
                                                        String instrType,
                                                        String relateNo,
                                                        BigDecimal instrAmt,
                                                        String corpId,
                                                        String corpName,
                                                        String createBy) {
        ScfInstruction instruction = new ScfInstruction();
        BeanUtil.copyProperties(voucherInfo, instruction);
        instruction.setId(null);
        instruction.setInstrCode(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_INSTRUCTION_NO));
        instruction.setInstrType(instrType);
        instruction.setInstrTypeName(ScfEntBankOpenConstant.getInterfaceName(instrType));
        instruction.setInstrAmt(instrAmt);
        instruction.setInstrDate(new Date());
        instruction.setRelateNo(relateNo);
        instruction.setCorpId(corpId);
        instruction.setCorpName(corpName);
        instruction.setCreateBy(createBy);
        return instruction;
    }

    @Transactional
    @Override
    public void batchSign(ScfReceivableVoucherBatchSignDTO batchSignDTO,ICurrentUser currentUser) {
        log.info("应收凭证进行签收操作==batchSignDTO:{}", JSON.toJSONString(batchSignDTO));

        List<String> voucherIds = Arrays.asList(batchSignDTO.getVoucherIds().split(","));
        VoucherSignEnum voucherStatus = batchSignDTO.getSignStatus();

        List<ScfReceivableVoucherInfo> voucherList = scfReceivableVoucherInfoDao.list(voucherIds);
        if (voucherList == null || voucherList.isEmpty()) {
            throw new BusinessException("查询应收凭证信息为空!");
        }
        boolean anyApproving = voucherList.stream().anyMatch(v ->
                ApprovalStatusEnum.APPROVAL_ING.getCode().equals(v.getApprovalStatus())
        );
        if (anyApproving) {
            throw new BusinessException(String.format("审批中的债权凭证不允许进行%s操作，请重新选择。", voucherStatus.getDesc()));
        }

        // 作废、拒签操作：直接修改状态，不发起审批流程
        if (VoucherSignEnum.CANCELLED.equals(voucherStatus)
                || VoucherSignEnum.REJECTED.equals(voucherStatus)) {
            if (VoucherSignEnum.CANCELLED.equals(voucherStatus)) {
                boolean allValid = voucherList.stream().allMatch(v ->
                        ApprovalStatusEnum.APPROVAL_PASS.getCode().equals(v.getApprovalStatus())
                        && (BusinessStatusEnum.SIGNED.getCode().equals(v.getVoucherStatus())
                        || BusinessStatusEnum.REJECTED.getCode().equals(v.getVoucherStatus()))
                );
                if (!allValid) {
                    throw new BusinessException("仅可对已签收或已拒签的债权凭证进行作废处理，您选择的数据不满足作废条件，请重新选择。");
                }
            }
            ApprovalStatusEnum approvalStatus = VoucherSignEnum.CANCELLED.equals(voucherStatus)
                    ? ApprovalStatusEnum.CANCELLED : ApprovalStatusEnum.APPROVAL_PASS;
            BusinessStatusEnum businessStatus = VoucherSignEnum.CANCELLED.equals(voucherStatus)
                    ? BusinessStatusEnum.CANCEL : BusinessStatusEnum.REJECTED;
            for (ScfReceivableVoucherInfo voucher : voucherList) {
                voucher.setApprovalStatus(approvalStatus.getCode());
                voucher.setVoucherStatus(businessStatus.getCode());
                voucher.setSignOperation(voucherStatus.getCode());
                if (VoucherSignEnum.CANCELLED.equals(voucherStatus)) {
                    voucher.setCancelDate(new Date());
                    voucher.setCancelReason(batchSignDTO.getCancelReason());
                }
                updateEntity(voucher);
            }
            return;
        }

        // 签收操作：需要发起审批流程
        for (ScfReceivableVoucherInfo voucher : voucherList) {
            try {
                voucher.setApprovalStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
                voucher.setSignOperation(batchSignDTO.getSignStatus().getCode());
                updateEntity(voucher);
                getStartHandler().handler(
                        ScfWorkFlowConstant.SCF_FLOW_RECEIVABLE_VOUCHER,
                        voucher.getId(),
                        voucher,
                        voucher.getCorpId(),
                        currentUser.getId());
            } catch (Exception e) {
                log.error("应收凭证进行签收操作启动审批流报错!", e);
                throw new BusinessException(String.format("应收凭证进行签收操作启动审批流报错:%s" + e.getMessage()));
            }
        }

    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        log.info("ScfReceivableVoucherInfoServiceImpl==flowCompleted:{}",JSON.toJSONString(workFlowResult));
        String businessKey = workFlowResult.getBusinessKey();
        ScfReceivableVoucherInfo voucherInfo = get(businessKey);
        if (voucherInfo == null) {
            log.warn("flowCompleted 未找到对应凭证, businessKey={}", businessKey);
            return;
        }
        if (workFlowResult.getIsPass()) {
            SignHandler handler = signHandlerFactory.getHandler(voucherInfo.getOnlineSign());
            handler.processSign(voucherInfo);
            log.info("线下凭证审批通过并更新业务状态完成, voucherId={}", businessKey);
        }else {
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.REJECTED.getCode());
            updateEntity(voucherInfo);
            log.info("线下凭证审批拒绝, voucherId={}", businessKey);
        }
    }


    @Transactional
    @Override
    public void supplement(ScfReceivableVoucherInfoDTO dto) {
        ScfReceivableVoucherInfo voucherInfo = get(dto.getId());
        if (voucherInfo == null) {
            throw new BusinessException("债权凭证信息不存在!");
        }
        if (!BusinessStatusEnum.DATA_SUPPLEMENT.getCode().equals(voucherInfo.getVoucherStatus())) {
            throw new BusinessException("仅可对数据待补录状态的债权凭证进行补录操作!");
        }
        // 必填字段校验
        if (StrUtil.isBlank(dto.getVoucherCode())) {
            throw new BusinessException("债权凭证编号不允许为空!");
        }
        if (StrUtil.isBlank(dto.getSourceVoucherCode())) {
            throw new BusinessException("根凭证编号不允许为空!");
        }
        if (StrUtil.isBlank(dto.getForehandVoucherCode())) {
            throw new BusinessException("上级凭证编号不允许为空!");
        }
        BeanUtil.copyProperties(dto, voucherInfo);
        //补录完成后状态流转到已签收
        voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
        updateEntity(voucherInfo);
        // 同步更新发票明细
        batchSaveInvoiceInfo(dto, dto.getId());
    }

    /**
     * 应付债权凭证台账查询
     * <p>
     * 单据状态固定为"开立审批通过"、"融资审批通过"或"提示付款审批通过"，
     * 业务状态固定为"已签收"、"已融资"或"已收款"。
     */
    @Override
    public DataTablesOutput<ScfReceivableVoucherInfoVO> getVoucherLedger(DataTablesInput input) {
        DataTablesOutput<ScfReceivableVoucherInfoVO> data = scfReceivableVoucherInfoDao.getVoucherLedger(input);
        List<ScfReceivableVoucherInfoVO> result = data.getData();
        if (CollectionUtil.isEmpty(result)) {
            return data;
        }
        //填充枚举名称
        result.forEach(item -> {
            fillEnumText(item);
        });
        if (data != null) {
            data.setDraw(input.getDraw());
            data.setRecordsTotal(data.getRecordsTotal());
            data.setRecordsFiltered(data.getRecordsFiltered());
            data.setData(result);
        }
        return data;
    }

    @Override
    public DataTablesOutput<WorkFlowPersistence> getPendingApprovalVoucher(DataTablesInput input) {
        Map<String, Object> params = input.getFormdata();
        if (ObjectUtil.isEmpty(params.get("eqs_scfWorkFlowType"))){
            throw new BusinessException("查询工作流审批类型不能为空!");
        }
        WorkFlowBusiness flowBusiness = workFlowService.getWorkFlowBusiness((String)params.get("eqs_scfWorkFlowType"), input);
        List<String> businessIdList = flowBusiness.getBusinessIdList();
        String flowType = (String) params.get("eqs_scfWorkFlowType");
        String businessSql = "";
        if (!CollectionUtil.isEmpty(businessIdList)) {
            if (ScfWorkFlowConstant.SCF_FLOW_RECEIVABLE_VOUCHER.equals(flowType)) {
                businessSql = SqlUtil.getInSql(businessIdList, "t.id", true);
            } else {
                // 融资、转让、付款流程的工作流业务ID是应收凭证ID，业务表通过凭证编号关联。
                String voucherIdSql = SqlUtil.getInSql(businessIdList, "v.id", true);
                businessSql = "t.voucher_code in (select v.voucher_code from scf_receivable_voucher_info v where " + voucherIdSql + ")";
            }
        }
        if (ScfWorkFlowConstant.SCF_FLOW_RECEIVABLE_VOUCHER.equals(flowType)) {
            DataTablesOutput<ScfReceivableVoucherInfoVO> dp = scfReceivableVoucherInfoDao.getPendingApprovalVoucher(input, businessSql);
            List<ScfReceivableVoucherInfoVO> data = dp.getData();
            if (CollectionUtil.isNotEmpty(data)) {
                data.forEach(this::fillEnumText);
            }
            return flowBusiness.merge(dp);
        }
        if (ScfWorkFlowConstant.SCF_FLOW_LOAN.equals(flowType)) {
            DataTablesOutput<ScfReceivableVoucherInfoVO> dp = scfReceivableVoucherInfoDao.getPendingApprovalVoucherByRelated(
                    input, voucherBusinessSql(businessIdList),
                    "scf_loan_info r where r.voucher_code = t.voucher_code and r.enable = 1 and r.loan_status = '" + ApprovalStatusEnum.APPROVAL_ING.getCode() + "'");
            if (CollectionUtil.isNotEmpty(dp.getData())) {
                dp.getData().forEach(this::fillEnumText);
            }
            return flowBusiness.merge(dp);
        }
        if (ScfWorkFlowConstant.SCF_FLOW_TRANSFER.equals(flowType)) {
            DataTablesOutput<ScfReceivableVoucherInfoVO> dp = scfReceivableVoucherInfoDao.getPendingApprovalVoucherByRelated(
                    input, voucherBusinessSql(businessIdList),
                    "scf_receivable_transfer_info r where r.voucher_code = t.voucher_code and r.transfer_status = '" + ApprovalStatusEnum.APPROVAL_ING.getCode() + "'");
            if (CollectionUtil.isNotEmpty(dp.getData())) {
                dp.getData().forEach(this::fillEnumText);
            }
            return flowBusiness.merge(dp);
        }
        if (ScfWorkFlowConstant.SCF_FLOW_PAYMENT.equals(flowType)) {
            DataTablesOutput<ScfReceivableVoucherInfoVO> dp = scfReceivableVoucherInfoDao.getPendingApprovalVoucherByRelated(
                    input, voucherBusinessSql(businessIdList),
                    "scf_receivable_payment_info r where r.voucher_code = t.voucher_code and r.enable = 1 and r.payment_status = '" + ApprovalStatusEnum.APPROVAL_ING.getCode() + "'");
            if (CollectionUtil.isNotEmpty(dp.getData())) {
                dp.getData().forEach(this::fillEnumText);
            }
            return flowBusiness.merge(dp);
        }
        throw new BusinessException("不支持的工作流审批类型: " + flowType);
    }

    private String voucherBusinessSql(List<String> businessIdList) {
        return CollectionUtil.isEmpty(businessIdList) ? "" : SqlUtil.getInSql(businessIdList, "t.id", true);
    }

    @Override
    public void importVoucherInfo(List<ScfReceivableVoucherImportVO> list, ICurrentUser currentUser) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("导入数据不能为空");
        }
        List<String> voucherCodeList = list.stream()
                .map(ScfReceivableVoucherImportVO::getVoucherCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(voucherCodeList)) {
            throw new BusinessException("凭证编号不能为空");
        }
        // 校验凭证编号是否重复
        long distinctCount = voucherCodeList.stream().distinct().count();
        if (distinctCount != voucherCodeList.size()) {
            throw new BusinessException("导入数据中存在重复的凭证编号");
        }
        // 校验凭证编号是否已存在
        List<ScfReceivableVoucherInfo> existList = scfReceivableVoucherInfoDao.listBy("voucherCode", voucherCodeList, MatchTypeEnum.IN);
        if (CollectionUtil.isNotEmpty(existList)) {
            String existCodes = existList.stream()
                    .map(ScfReceivableVoucherInfo::getVoucherCode)
                    .collect(Collectors.joining(", "));
            throw new BusinessException("凭证编号已存在: " + existCodes);
        }
        List<ScfReceivableVoucherInfo> saveList = new ArrayList<>();
        for (ScfReceivableVoucherImportVO vo : list) {
            ScfReceivableVoucherInfo entity = new ScfReceivableVoucherInfo();
            BeanUtil.copyProperties(vo, entity);
            // 凭证编号
            entity.setVoucherCode(vo.getVoucherCode());
            // 单据状态: 开立暂存
            entity.setApprovalStatus(ApprovalStatusEnum.DRAFT.getCode());
            // 业务状态: 未签收
            entity.setVoucherStatus(BusinessStatusEnum.UNSIGNED.getCode());
            // 凭证来源: 手动
            entity.setVoucherSource(VoucherSourceEnum.MANUAL.getCode());
            // 序号
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_VOUCHER_NO));
            // 回填币种ID（根据币种名称）
            if (StrUtil.isNotBlank(vo.getCurName())) {
                BtCurrencyDTO currencyDTO = btCurrencyService.getByCurName(vo.getCurName());
                if (currencyDTO != null) {
                    entity.setCurId(currencyDTO.getId());
                }
            }
            // 回填平台ID（根据平台名称）
            if (StrUtil.isNotBlank(vo.getPlatformName())) {
                ScfPlatform platform = scfPlatformService.getEntityDao().getUniqueBy("platformName", vo.getPlatformName());
                if (platform != null) {
                    entity.setPlatformId(platform.getId());
                }
            }
            // 回填产品ID（根据产品名称）
            if (StrUtil.isNotBlank(vo.getProductName())) {
                ScfProduct product = scfProductDao.getUniqueBy("productName", vo.getProductName());
                if (product != null) {
                    entity.setProductId(product.getId());
                }
            }
            // 回填持有单位ID（根据持有单位名称查 sys_corp，取 code）
            if (StrUtil.isNotBlank(vo.getCorpName())) {
                CorpDTO corpDTO = corpService.getCorpIdByName(vo.getCorpName());
                if (corpDTO != null) {
                    entity.setCorpId(corpDTO.getCode());
                }
            }
            // 回填开立/转让单位ID（先查 sys_external_corp，没有再查 bt_bank_acc.acc_name 取 corp_id，多条取第一条）
            if (StrUtil.isNotBlank(vo.getIssueTransferCompany())) {
                String issueTransferId = resolveCompanyIdByName(vo.getIssueTransferCompany());
                if (StrUtil.isNotBlank(issueTransferId)) {
                    entity.setIssueTransferCompanyId(issueTransferId);
                }
            }
            // 回填核心企业ID（先查 sys_external_corp，没有再查 bt_bank_acc.acc_name 取 corp_id，多条取第一条）
            if (StrUtil.isNotBlank(vo.getCoreCompanyName())) {
                String coreCompanyId = resolveCompanyIdByName(vo.getCoreCompanyName());
                if (StrUtil.isNotBlank(coreCompanyId)) {
                    entity.setCoreCompanyId(coreCompanyId);
                }
            }

            saveList.add(entity);
        }
        if (CollectionUtil.isNotEmpty(saveList)) {
            for (ScfReceivableVoucherInfo entity : saveList) {
                scfReceivableVoucherInfoDao.save(entity);
            }
            log.info("Excel批量导入应收债权凭证成功, 共导入{}条", saveList.size());
        }
    }

    @Override
    public ScfReceivableVoucherInfoVO getLinkChain(String id) {
        ScfReceivableVoucherInfo root = scfReceivableVoucherInfoDao.get(id);
        if (ObjectUtil.isEmpty(root)) {
            throw new BusinessException("凭证信息不存在, id=" + id);
        }
        return buildLinkNode(root, new HashSet<>());
    }

    /**
     * 从当前凭证沿 forehandVoucherCode 向前递归构建完整链路。
     * <p>
     * 每个节点同时装配签收展示节点、已成功的转让/融资/收款记录；若存在前手凭证，
     * 将前手凭证（及前手凭证对应的转让记录）放入 children。
     */
    private ScfReceivableVoucherInfoVO buildLinkNode(ScfReceivableVoucherInfo current, Set<String> visitedVoucherCodes) {
        ScfReceivableVoucherInfoVO node = buildVoucherInfoVO(current);
        String currentVoucherCode = current.getVoucherCode();
        if (StrUtil.isNotBlank(currentVoucherCode) && !visitedVoucherCodes.add(currentVoucherCode)) {
            log.warn("债权凭证链路存在循环引用, voucherCode={}", currentVoucherCode);
            node.setChildren(Collections.emptyList());
            return node;
        }

        List<ScfReceivableVoucherInfoVO> children = new ArrayList<>();
        boolean currentIsSignedNode = ApprovalStatusEnum.APPROVAL_PASS.getCode().equals(current.getApprovalStatus())
                && BusinessStatusEnum.SIGNED.getCode().equals(current.getVoucherStatus());
        if (VoucherSignEnum.SIGNED.getCode().equals(current.getSignOperation()) && !currentIsSignedNode) {
            children.add(buildSignLinkNode(current));
        }

        String forehandVoucherCode = current.getForehandVoucherCode();
        if (StrUtil.isBlank(forehandVoucherCode) || visitedVoucherCodes.contains(forehandVoucherCode)) {
            node.setChildren(children);
            return node;
        }

        ScfReceivableVoucherInfo forehandVoucher = scfReceivableVoucherInfoDao.getUniqueBy(
                "voucherCode", forehandVoucherCode);
        if (forehandVoucher == null) {
            node.setChildren(children);
            return node;
        }
        children.add(buildLinkNode(forehandVoucher, visitedVoucherCodes));
        node.setChildren(children);
        return node;
    }

    /**
     * 签收没有独立业务记录表，链路展示时根据凭证签收结果构造一条虚拟记录。
     */
    private ScfReceivableVoucherInfoVO buildSignLinkNode(ScfReceivableVoucherInfo voucherInfo) {
        ScfReceivableVoucherInfoVO signNode = BeanUtil.copyProperties(voucherInfo, ScfReceivableVoucherInfoVO.class);
        signNode.setId(voucherInfo.getId() + "-sign");
        signNode.setSignOperation(VoucherSignEnum.SIGNED.getCode());
        signNode.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        signNode.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
        signNode.setTransferInfo(Collections.emptyList());
        signNode.setLoanInfo(Collections.emptyList());
        signNode.setPaymentInfo(Collections.emptyList());
        signNode.setChildren(Collections.emptyList());
        extractedVoucher(signNode);
        return signNode;
    }

    /**
     * 按名称解析单位/企业 ID
     * <p>
     * 先通过 sys_external_corp.name 精确查询，命中则取其 id；
     * 未命中则通过 bt_bank_acc.acc_name 精确查询，取结果的 corp_id；多条取第一条。
     *
     * @param name 单位/企业名称
     * @return ID，未找到返回 null
     */
    private String resolveCompanyIdByName(String name) {
        ExternalCorpDTO externalCorpDTO = externalCorpService.findByCorpIdAndName(null, name);
        if (externalCorpDTO != null && StrUtil.isNotBlank(externalCorpDTO.getId())) {
            return externalCorpDTO.getId();
        }
        BtBankAccDTO query = new BtBankAccDTO();
        query.setAccName(name);
        List<BtBankAccDTO> bankAccList = btBankAccService.getBankAccList(query);
        if (CollectionUtil.isNotEmpty(bankAccList)) {
            return bankAccList.get(0).getCorpId();
        }
        return null;
    }

}
