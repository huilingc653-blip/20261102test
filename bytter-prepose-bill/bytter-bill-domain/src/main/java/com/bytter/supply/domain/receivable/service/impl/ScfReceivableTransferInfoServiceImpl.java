package com.bytter.supply.domain.receivable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.bill.api.scf.dto.ScfPayTransferDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.constant.ScfWorkFlowConstant;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferReceiveDTO;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.domain.receivable.dao.ScfReceivableTransferInfoDao;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.handler.SignHandler;
import com.bytter.supply.domain.receivable.handler.SignHandlerFactory;
import com.bytter.supply.domain.receivable.service.ScfReceivableTransferInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoGroupVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import com.bytter.supply.domain.platform.service.ScfEntBankOpenService;
import com.bytter.supply.enums.OnlineEnum;
import com.bytter.supply.enums.SupplyMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static cn.hutool.core.util.ArrayUtil.map;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoServiceImpl
 * @Description: 债权凭证转让信息服务实现
 * @since 2026/7/8
 */
@Service
@Slf4j
@WorkFlow({ScfWorkFlowConstant.SCF_FLOW_TRANSFER})
public class ScfReceivableTransferInfoServiceImpl extends BaseService<ScfReceivableTransferInfo, String>
        implements ScfReceivableTransferInfoService, IWorkFlowListener {

    @Autowired
    private ScfReceivableTransferInfoDao scfReceivableTransferInfoDao;

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    @Lazy
    private SignHandlerFactory signHandlerFactory;

    @Autowired
    private ScfEntBankOpenService scfEntBankOpenService;

    private void checkJudge(String platformId) {
        boolean judged = scfEntBankOpenService.judgeInstructionOpen(platformId,
                ScfEntBankOpenConstant.RECEIVABLE_VOUCHER_TRANSFER);
        if (!judged) {
            throw new BusinessException(SupplyMessageEnum.SCF_ONLINE_INTERFACE_NOT_OPEN,
                    ScfEntBankOpenConstant.getInterfaceName(ScfEntBankOpenConstant.RECEIVABLE_VOUCHER_TRANSFER));
        }
    }


    @Override
    public BaseDao<ScfReceivableTransferInfo, String> getEntityDao() {
        return scfReceivableTransferInfoDao;
    }

    /**
     * 列表查询
     */
    @Override
    public DataTablesOutput<ScfReceivableTransferInfoVO> dataTable(DataTablesInput input) {
        DataTablesOutput<ScfReceivableTransferInfoVO> data = scfReceivableTransferInfoDao.dataTable(input);
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

    private void fillStatusName(ScfReceivableTransferInfoVO transferInfo) {
        ApprovalStatusEnum status = ApprovalStatusEnum.getByCode(transferInfo.getTransferStatus());
        if (status != null) {
            transferInfo.setTransferStatusName(status.getDesc());
        }
    }

    /**
     * 转让列表查询：返回单个分组对象。转让状态为空时查询所有状态。
     * 一对多：voucherInfoList 为 1 条凭证信息，transferInfoList 为多条接收方转让信息；
     * 多对一：voucherInfoList 为多条凭证信息，transferInfoList 为 1 条接收方转让信息
     */
    @Override
    public ScfReceivableTransferInfoGroupVO listTemp(String transferFlag, String voucherCode, String voucherIds, String transferStatus) {
        List<ScfReceivableTransferInfoVO> list = scfReceivableTransferInfoDao.listTemp(transferFlag, voucherCode, voucherIds, transferStatus);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        fillVoucherInfo(list);
        ScfReceivableTransferInfoGroupVO group = new ScfReceivableTransferInfoGroupVO();
        group.setTransferFlag(list.get(0).getTransferFlag());
        // 凭证信息列表：按凭证编号去重（一对多 1 条，多对一多条）
        Map<String, ScfReceivableVoucherInfoVO> voucherMap = new LinkedHashMap<>();
        for (ScfReceivableTransferInfoVO item : list) {
            if (item.getVoucherInfo() != null) {
                voucherMap.putIfAbsent(item.getVoucherCode(), item.getVoucherInfo());
            }
        }
        group.setVoucherInfoList(new ArrayList<>(voucherMap.values()));
        // 转让信息列表：多对一仅返回 1 条接收方转让信息，一对多/单条暂存返回全部记录
        if (ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(group.getTransferFlag())) {
            group.setTransferInfoList(Collections.singletonList(list.get(0)));
        } else {
            group.setTransferInfoList(list);
        }
        return group;
    }

    /**
     * 按凭证编号批量查询凭证信息，组装到每条转让记录的 voucherInfo 字段
     */
    private void fillVoucherInfo(List<ScfReceivableTransferInfoVO> list) {
        List<String> voucherCodes = new ArrayList<>();
        for (ScfReceivableTransferInfoVO item : list) {
            if (StringUtils.isNoneBlank(item.getVoucherCode()) && !voucherCodes.contains(item.getVoucherCode())) {
                voucherCodes.add(item.getVoucherCode());
            }
        }
        if (CollectionUtil.isEmpty(voucherCodes)) {
            return;
        }
        List<ScfReceivableVoucherInfo> vouchers = scfReceivableVoucherInfoDao.listBy(
                "voucherCode", String.join(",", voucherCodes), MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(vouchers)) {
            return;
        }
        Map<String, ScfReceivableVoucherInfoVO> voucherMap = new HashMap<>();
        for (ScfReceivableVoucherInfo voucher : vouchers) {
            voucherMap.put(voucher.getVoucherCode(), BeanUtil.copyProperties(voucher, ScfReceivableVoucherInfoVO.class));
        }
        for (ScfReceivableTransferInfoVO item : list) {
            item.setVoucherInfo(voucherMap.get(item.getVoucherCode()));
        }
    }

    @Transactional
    @Override
    public boolean sendTransferInfo(ScfReceivableTransferInfoDTO transferInfoDTO, ICurrentUser currentUser) {
        String transferInfoFlag = transferInfoDTO.getTransferFlag();
        // 暂存：仅写入转让信息，不发起审批流
        if (ScfBusinessConstant.SEND_STATUS_SAVE.equals(transferInfoDTO.getSendStatus())) {
            if (ScfBusinessConstant.TRANSFER_FLAG_ONE_TO_MANY.equals(transferInfoFlag)) {
                doTemporarySaveOneToMany(transferInfoDTO);
            } else if (ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(transferInfoFlag)) {
                doTemporarySaveManyToOne(transferInfoDTO);
            } else {
                doTemporarySaveSingle(transferInfoDTO);
            }
            return true;
        }
        // 提交：写入转让信息并发起审批流
        if (ScfBusinessConstant.TRANSFER_FLAG_ONE_TO_MANY.equals(transferInfoFlag)) {
            doOneToManyTransfer(transferInfoDTO, currentUser);
            return true;
        } else if (ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(transferInfoFlag)) {
            doManyToOneTransfer(transferInfoDTO, currentUser);
            return true;
        }
        return Boolean.TRUE;
    }


    /**
     * 单条暂存：仅依据 voucherCode 更新凭证状态并写入一条转让信息
     *
     * @return 草稿记录ID
     */
    private String doTemporarySaveSingle(ScfReceivableTransferInfoDTO transferInfoDTO) {
        if (StringUtils.isBlank(transferInfoDTO.getVoucherCode())) {
            throw new RuntimeException("债权凭证编号不能为空");
        }
        ScfReceivableVoucherInfo voucher = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", transferInfoDTO.getVoucherCode());
        if (voucher == null) {
            throw new RuntimeException("未找到对应的债权凭证信息，凭证编号：" + transferInfoDTO.getVoucherCode());
        }
        ScfReceivableTransferInfo entity;
        // 再次暂存：该凭证已存在暂存记录时，复用Session中的持久态对象并保留原转让编号，
        // 避免新复制实例 saveOrUpdate 时抛 NonUniqueObjectException
        if (StringUtils.isBlank(transferInfoDTO.getId())) {
            ScfReceivableTransferInfo exist = getTemporaryByVoucherCode(transferInfoDTO.getVoucherCode());
            if (exist != null) {
                entity = exist;
                BeanUtil.copyProperties(transferInfoDTO, entity);
                entity.setId(exist.getId());
                entity.setTransferNo(exist.getTransferNo());
                entity.setTransferBatchNo(exist.getTransferBatchNo());
            } else {
                entity = BeanUtil.copyProperties(transferInfoDTO, ScfReceivableTransferInfo.class);
            }
        } else {
            entity = BeanUtil.copyProperties(transferInfoDTO, ScfReceivableTransferInfo.class);
        }
        if (StringUtils.isBlank(entity.getTransferNo())) {
            entity.setTransferNo(SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_NO));
        }
        if (StringUtils.isBlank(entity.getTransferBatchNo())) {
            entity.setTransferBatchNo(SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_BATCH_NO));
        }
        entity.setVoucherCode(voucher.getVoucherCode());
        // 付款方式52暂存时，平台和融资产品信息以关联凭证为准，避免前端未传导致转让记录字段为空。
        entity.setPlatformId(voucher.getPlatformId());
        entity.setPlatformName(voucher.getPlatformName());
        entity.setProductId(voucher.getProductId());
        entity.setProductName(voucher.getProductName());
        // 开立/转让单位及币种必须以凭证信息为准，不能依赖前端传入。
        entity.setIssueTransferCompany(voucher.getIssueTransferCompany());
        entity.setIssueTransferCompanyId(voucher.getIssueTransferCompanyId());
        entity.setCurId(voucher.getCurId());
        entity.setCurName(voucher.getCurName());
        entity.setTransferStatus(ApprovalStatusEnum.DRAFT.getCode());
        saveOrUpdate(entity);
        // 付款方式52暂存：凭证保持审批通过并回到待转让，转让记录自身为暂存。
        voucher.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        voucher.setVoucherStatus(BusinessStatusEnum.UN_TRANSFER.getCode());
        scfReceivableVoucherInfoDao.update(voucher);
        return entity.getId();
    }

    /**
     * 查询凭证的暂存转让记录（待转让）
     */
    private ScfReceivableTransferInfo getTemporaryByVoucherCode(String voucherCode) {
        List<ScfReceivableTransferInfo> list = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.eq("voucherCode", voucherCode));
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 获取转让批次号：已有暂存记录存在批次号则复用（再次暂存保持同批次），否则生成新的
     */
    private String getTransferBatchNo(List<ScfReceivableTransferInfo> existList) {
        if (CollectionUtil.isNotEmpty(existList)) {
            for (ScfReceivableTransferInfo exist : existList) {
                if (StringUtils.isNoneBlank(exist.getTransferBatchNo())) {
                    return exist.getTransferBatchNo();
                }
            }
        }
        return SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_BATCH_NO);
    }

    /**
     * 一对多暂存前校验：凭证已存在多对一暂存数据时，不允许以一对多方式暂存
     */
    @Override
    public void checkOneTemporary(String voucherCode) {
        List<ScfReceivableTransferInfo> conflictList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.eq("voucherCode", voucherCode),
                Restrictions.eq("transferFlag", ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE));
        if (CollectionUtil.isNotEmpty(conflictList)) {
            throw new BusinessException("该凭证已存在批量转让暂存数据，不允许单笔发起转让暂存!");
        }
    }

    /**
     * 多对一暂存前校验：所选凭证已存在一对多暂存数据时，不允许以多对一方式暂存
     */
    @Override
    public void checkManyTemporary(String voucherCodes) {
        if (StringUtils.isBlank(voucherCodes)) {
            return;
        }
        List<String> voucherCodeList = Arrays.asList(voucherCodes.split(","));
        List<ScfReceivableTransferInfo> conflictList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.in("voucherCode", voucherCodeList),
                Restrictions.eq("transferFlag", ScfBusinessConstant.TRANSFER_FLAG_ONE_TO_MANY));
        if (CollectionUtil.isNotEmpty(conflictList)) {
            throw new BusinessException("所选凭证已单笔转让暂存数据，不允许批量转让暂存!");
        }
    }

    /**
     * 一对多暂存：一张凭证拆分转让多家，仅写入转让信息并将凭证状态置为"提示转让暂存"，不发起审批流
     *
     * @return 草稿记录ID集合
     */
    private List<String> doTemporarySaveOneToMany(ScfReceivableTransferInfoDTO transferInfoDTO) {
        if (CollectionUtil.isEmpty(transferInfoDTO.getTransferReceive())) {
            throw new RuntimeException("批量转让接收方信息不能为空!");
        }
        ScfReceivableVoucherInfo voucher = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", transferInfoDTO.getVoucherCode());
        if (voucher == null) {
            throw new RuntimeException("未找到对应的债权凭证信息，凭证编号：" + transferInfoDTO.getVoucherCode());
        }
        // 该凭证已存在多对一暂存数据时，不允许以一对多方式暂存
        checkOneTemporary(voucher.getVoucherCode());
        // 不允许拆分的凭证不能进行一对多转让暂存
        validateAllowSplit(transferInfoDTO.getTransferReceive(), voucher);
        // 已存在的暂存记录（按id），再次暂存时前端删除的接收方需同步删除
        List<ScfReceivableTransferInfo> existList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.eq("voucherCode", voucher.getVoucherCode()));
        Map<String, ScfReceivableTransferInfo> existMap = new HashMap<>();
        for (ScfReceivableTransferInfo exist : existList) {
            existMap.put(exist.getId(), exist);
        }
        // 转让批次号：复用已有暂存记录的批次号，否则生成新的（同一凭证拆分的多家接收方共享批次号）
        String transferBatchNo = getTransferBatchNo(existList);
        Set<String> keepIds = new HashSet<>();
        List<String> savedIds = new ArrayList<>();
        for (ScfReceivableTransferReceiveDTO item : transferInfoDTO.getTransferReceive()) {
            ScfReceivableTransferInfo entity;
            if (StringUtils.isNoneBlank(item.getId()) && existMap.containsKey(item.getId())) {
                // 更新原暂存记录：复用Session中的持久态对象并保留原转让编号，
                // 避免新复制实例 saveOrUpdate 时抛 NonUniqueObjectException
                entity = existMap.get(item.getId());
                BeanUtil.copyProperties(item, entity);
                entity.setId(item.getId());
                entity.setTransferNo(existMap.get(item.getId()).getTransferNo());
                keepIds.add(item.getId());
            } else {
                // 新增接收方
                entity = BeanUtil.copyProperties(item, ScfReceivableTransferInfo.class);
                entity.setId(null);
                entity.setTransferNo(StringUtils.isNoneBlank(entity.getTransferNo()) ? entity.getTransferNo() : SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_NO));
            }
            entity.setTransferBatchNo(transferBatchNo);
            entity.setVoucherCode(voucher.getVoucherCode());
            entity.setTransferStatus(ApprovalStatusEnum.DRAFT.getCode());
            entity.setTransferFlag(transferInfoDTO.getTransferFlag());
            entity.setPlatformId(voucher.getPlatformId());
            entity.setPlatformName(voucher.getPlatformName());
            entity.setProductId(voucher.getProductId());
            entity.setProductName(voucher.getProductName());
            // 开立/转让单位、币种均以凭证信息为准，忽略前端传入的单位字段。
            entity.setIssueTransferCompany(voucher.getIssueTransferCompany());
            entity.setIssueTransferCompanyId(voucher.getIssueTransferCompanyId());
            entity.setCurId(voucher.getCurId());
            entity.setCurName(voucher.getCurName());
            saveOrUpdate(entity);
            savedIds.add(entity.getId());
        }
        // 删除前端已移除的接收方暂存记录
        for (ScfReceivableTransferInfo exist : existList) {
            if (!keepIds.contains(exist.getId())) {
                scfReceivableTransferInfoDao.delete(exist.getId());
            }
        }
        // 付款方式52暂存：凭证保持审批通过并回到待转让，转让记录自身为暂存。
        voucher.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
        voucher.setVoucherStatus(BusinessStatusEnum.UN_TRANSFER.getCode());
        scfReceivableVoucherInfoDao.update(voucher);
        return savedIds;
    }

    /**
     * 多对一暂存：多张凭证转让一家，仅写入转让信息并将凭证状态置为"待转让"，不发起审批流
     *
     * @return 草稿记录ID集合
     */
    private List<String> doTemporarySaveManyToOne(ScfReceivableTransferInfoDTO transferInfoDTO) {
        if (ObjectUtil.isEmpty(transferInfoDTO.getVoucherIds())) {
            throw new RuntimeException("多对一转让时，应收凭证id不能为空!");
        }
        List<ScfReceivableVoucherInfo> receivableVoucherInfo = scfReceivableVoucherInfoDao.listBy("id", transferInfoDTO.getVoucherIds(), MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(receivableVoucherInfo)) {
            return null;
        }
        // 已存在的暂存记录（按凭证编号），再次暂存时更新原记录并保留原转让编号
        List<String> voucherCodes = new ArrayList<>();
        for (ScfReceivableVoucherInfo voucherInfo : receivableVoucherInfo) {
            voucherCodes.add(voucherInfo.getVoucherCode());
        }
        // 所选凭证已存在一对多暂存数据时，不允许以多对一方式暂存
        //checkNoOneToManyTemporary(voucherCode);
        List<ScfReceivableTransferInfo> existList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.in("voucherCode", voucherCodes));
        Map<String, ScfReceivableTransferInfo> existMap = new HashMap<>();
        for (ScfReceivableTransferInfo exist : existList) {
            existMap.put(exist.getVoucherCode(), exist);
        }
        // 转让批次号：复用已有暂存记录的批次号，否则生成新的（同一接收方的多张凭证共享批次号）
        String transferBatchNo = getTransferBatchNo(existList);
        List<String> savedIds = new ArrayList<>();
        for (ScfReceivableVoucherInfo voucherInfo : receivableVoucherInfo) {
            ScfReceivableTransferInfo transferInfo = existMap.get(voucherInfo.getVoucherCode());
            if (transferInfo == null) {
                transferInfo = new ScfReceivableTransferInfo();
                transferInfo.setTransferNo(SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_NO));
            }
            transferInfo.setTransferBatchNo(transferBatchNo);
            transferInfo.setVoucherCode(voucherInfo.getVoucherCode());
            transferInfo.setTransferStatus(ApprovalStatusEnum.DRAFT.getCode());
            transferInfo.setPlatformId(voucherInfo.getPlatformId());
            transferInfo.setPlatformName(voucherInfo.getPlatformName());
            transferInfo.setProductId(voucherInfo.getProductId());
            transferInfo.setProductName(voucherInfo.getProductName());
            // 开立/转让单位、币种均以凭证信息为准，忽略前端传入的单位字段。
            transferInfo.setIssueTransferCompanyId(voucherInfo.getIssueTransferCompanyId());
            transferInfo.setIssueTransferCompany(voucherInfo.getIssueTransferCompany());
            transferInfo.setCurId(voucherInfo.getCurId());
            transferInfo.setCurName(voucherInfo.getCurName());
            transferInfo.setReceiveCompany(transferInfoDTO.getReceiveCompany());
            transferInfo.setReceiveCompanyId(transferInfoDTO.getReceiveCompanyId());
            transferInfo.setReceiverCreditCode(transferInfoDTO.getReceiverCreditCode());
            transferInfo.setTransferDate(transferInfoDTO.getTransferDate());
            //多对一转让为全部转让，不存在部分金额，转让金额取凭证金额
            transferInfo.setTransferAmt(voucherInfo.getVoucherAmt());
            transferInfo.setPurpose(transferInfoDTO.getPurpose());
            transferInfo.setTransferFlag(transferInfoDTO.getTransferFlag());
            saveOrUpdate(transferInfo);
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            voucherInfo.setVoucherStatus(BusinessStatusEnum.UN_TRANSFER.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
            savedIds.add(transferInfo.getId());
        }
        // 删除前端已移除凭证的暂存记录
        for (ScfReceivableTransferInfo exist : existList) {
            if (!voucherCodes.contains(exist.getVoucherCode())) {
                scfReceivableTransferInfoDao.delete(exist.getId());
            }
        }
        return savedIds;
    }

    /**
     * 保存转让草稿（付款方式52前端提交付款单前调用）：按转让标识分发到对应暂存逻辑，返回草稿记录ID集合。
     * <p>多对一（transferFlag=2）：voucherIds + 接收方信息；单笔（其余，含transferFlag=1）：voucherCode + 接收方信息。
     *
     * @param transferInfoDTO 转让信息
     * @return 草稿记录ID集合
     */
    @Transactional
    @Override
    public List<String> temporarySaveTransfer(ScfReceivableTransferInfoDTO transferInfoDTO) {
        if (ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(transferInfoDTO.getTransferFlag())) {
            return doTemporarySaveManyToOne(transferInfoDTO);
        }
        return Collections.singletonList(doTemporarySaveSingle(transferInfoDTO));
    }

    /**
     * 按草稿ID集合查询转让暂存信息（付款方式52编辑回显反查凭证用）
     *
     * @param ids 草稿ID集合
     * @return 转让暂存信息列表
     */
    @Override
    public List<ScfReceivableTransferInfo> getTransferInfoByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return scfReceivableTransferInfoDao.listBy("id", String.join(",", ids), MatchTypeEnum.IN);
    }

    /**
     * 释放转让暂存（付款方式52再次进入变更时调用）：删除指定草稿记录，
     * 并将对应凭证的审批状态从"转让暂存"恢复为"开立审批通过"
     */
    @Transactional
    @Override
    public void deleteTransferDrafts(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        List<ScfReceivableTransferInfo> drafts = getTransferInfoByIds(ids);
        if (CollectionUtil.isEmpty(drafts)) {
            return;
        }
        for (ScfReceivableTransferInfo draft : drafts) {
            scfReceivableTransferInfoDao.delete(draft.getId());
            releaseVoucherTransferDraft(draft.getVoucherCode());
        }
    }

    /**
     * 释放单个凭证的转让暂存：仅当凭证当前为"转让暂存"时恢复为"开立审批通过"
     */
    private void releaseVoucherTransferDraft(String voucherCode) {
        String hql = "update ScfReceivableVoucherInfo set approvalStatus = :pass where voucherCode = :voucherCode and approvalStatus = :draft";
        Map<String, Object> params = new HashMap<>();
        params.put("pass", ApprovalStatusEnum.APPROVAL_PASS.getCode());
        params.put("draft", ApprovalStatusEnum.DRAFT.getCode());
        params.put("voucherCode", voucherCode);
        scfReceivableVoucherInfoDao.batchExecute(hql, params);
    }

    /**
     * 多对一转让：多张凭证转让一接收方,产生多笔审批
     * 多对一转让为全部转让，不存在部分金额
     */
    private void doManyToOneTransfer(ScfReceivableTransferInfoDTO transferInfoDTO, ICurrentUser currentUser) {
        if (ObjectUtil.isEmpty(transferInfoDTO.getVoucherIds())) {
            throw new BusinessException("多对一转让时，应收凭证id集合不能为空!");
        }

        List<ScfReceivableVoucherInfo> receivableVoucherInfo = scfReceivableVoucherInfoDao.listBy("id", transferInfoDTO.getVoucherIds(), MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(receivableVoucherInfo)) {
            return;
        }

        validateManyToOneTransfer(receivableVoucherInfo, transferInfoDTO);
        List<String> voucherIdList= Arrays.asList(transferInfoDTO.getVoucherIds().split(","));
        // 提交转让：凭证审批状态保持开立审批通过，业务状态置为“转让中”，转让审批中不再改写凭证审批状态
        updateVoucherApprovalAndBusinessStatusByIds(voucherIdList,
                ApprovalStatusEnum.APPROVAL_PASS.getCode(), BusinessStatusEnum.TRANSFER_ING.getCode());

        // 暂存记录按凭证编号索引：提交时已存在则更新（保留主键/转让编号/批次号），不存在则新增
        List<String> voucherCodes = new ArrayList<>();
        for (ScfReceivableVoucherInfo voucherInfo : receivableVoucherInfo) {
            voucherCodes.add(voucherInfo.getVoucherCode());
        }
        List<ScfReceivableTransferInfo> temporaryList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.in("voucherCode", voucherCodes));
        Map<String, ScfReceivableTransferInfo> temporaryMap = new HashMap<>();
        for (ScfReceivableTransferInfo exist : temporaryList) {
            temporaryMap.put(exist.getVoucherCode(), exist);
        }
        for (ScfReceivableVoucherInfo voucherInfo : receivableVoucherInfo) {
            ScfReceivableTransferInfo transferInfo = temporaryMap.get(voucherInfo.getVoucherCode());
            if (transferInfo == null) {
                // 新增记录：无暂存记录，沿用暂存批次号（无暂存时生成新批次号）
                transferInfo = new ScfReceivableTransferInfo();
                transferInfo.setTransferNo(SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_NO));
                transferInfo.setTransferBatchNo(getTransferBatchNo(temporaryList));
            }
            transferInfo.setVoucherCode(voucherInfo.getVoucherCode());
            transferInfo.setTransferStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
            transferInfo.setPlatformId(voucherInfo.getPlatformId());
            transferInfo.setPlatformName(voucherInfo.getPlatformName());
            transferInfo.setProductId(voucherInfo.getProductId());
            transferInfo.setProductName(voucherInfo.getProductName());
            transferInfo.setIssueTransferCompanyId(voucherInfo.getIssueTransferCompanyId());
            transferInfo.setIssueTransferCompany(voucherInfo.getIssueTransferCompany());
            transferInfo.setCurId(voucherInfo.getCurId());
            transferInfo.setCurName(voucherInfo.getCurName());
            transferInfo.setReceiveCompany(transferInfoDTO.getReceiveCompany());
            transferInfo.setReceiveCompanyId(transferInfoDTO.getReceiveCompanyId());
            transferInfo.setReceiverCreditCode(transferInfoDTO.getReceiverCreditCode());
            transferInfo.setTransferDate(transferInfoDTO.getTransferDate());
            transferInfo.setTransferAmt(voucherInfo.getVoucherAmt());
            transferInfo.setPurpose(transferInfoDTO.getPurpose());
            transferInfo.setTransferFlag(transferInfoDTO.getTransferFlag());
            saveOrUpdate(transferInfo);
            try {
                getStartHandler().handler(
                        ScfWorkFlowConstant.SCF_FLOW_TRANSFER,
                        voucherInfo.getId(),
                        voucherInfo,
                        voucherInfo.getCorpId(),
                        currentUser.getId());
            } catch (Exception e) {
                log.error("应收凭证进行转让操作启动审批流报错!", e);
            }
        }
    }

    /**
     * 一对多转让：凭证拆分转让多接收方
     */
    private void doOneToManyTransfer(ScfReceivableTransferInfoDTO transferInfoDTO, ICurrentUser currentUser) {
        List<ScfReceivableTransferReceiveDTO> transferInfoList = transferInfoDTO.getTransferReceive();
        if (CollectionUtil.isEmpty(transferInfoList)) {
            throw new BusinessException("批量转让接收方信息不能为空!");
        }
        ScfReceivableVoucherInfo voucher = scfReceivableVoucherInfoDao.getUniqueBy("voucherCode", transferInfoDTO.getVoucherCode());
        if (voucher == null) {
            throw new BusinessException("未找到对应的债权凭证信息，凭证编号：" + transferInfoDTO.getVoucherCode());
        }
        validateOneToManyTransfer(transferInfoList, voucher);
        // 提交转让：凭证审批状态保持开立审批通过，业务状态置为“转让中”，转让审批中不再改写凭证审批状态
        updateVoucherApprovalAndBusinessStatusByVoucherCode(voucher.getVoucherCode(),
                ApprovalStatusEnum.APPROVAL_PASS.getCode(), BusinessStatusEnum.TRANSFER_ING.getCode());

        // 暂存记录按id索引：提交时已存在则更新（保留主键/转让编号/批次号），不存在则新增
        List<ScfReceivableTransferInfo> temporaryList = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.eq("voucherCode", voucher.getVoucherCode()));
        Map<String, ScfReceivableTransferInfo> temporaryMap = new HashMap<>();
        for (ScfReceivableTransferInfo exist : temporaryList) {
            temporaryMap.put(exist.getId(), exist);
        }
        for (ScfReceivableTransferReceiveDTO item : transferInfoList) {
            ScfReceivableTransferInfo entity;
            if (StringUtils.isNoneBlank(item.getId()) && temporaryMap.containsKey(item.getId())) {
                // 更新暂存记录：复用Session中的持久态实例，避免NonUniqueObjectException
                entity = temporaryMap.get(item.getId());
                BeanUtil.copyProperties(item, entity);
                entity.setId(item.getId());
                entity.setTransferNo(temporaryMap.get(item.getId()).getTransferNo());
                entity.setTransferBatchNo(temporaryMap.get(item.getId()).getTransferBatchNo());
            } else {
                // 新增接收方：无暂存记录，沿用暂存批次号（无暂存时生成新批次号）
                entity = BeanUtil.copyProperties(item, ScfReceivableTransferInfo.class);
                entity.setId(null);
                entity.setTransferNo(SequenceSingleton.getSequence(SequenceEnum.SCF_ZR_TRANSFER_NO));
                entity.setTransferBatchNo(getTransferBatchNo(temporaryList));
            }
            entity.setVoucherCode(voucher.getVoucherCode());
            entity.setPlatformId(transferInfoDTO.getPlatformId());
            entity.setPlatformName(transferInfoDTO.getPlatformName());
            entity.setCurId(voucher.getCurId());
            entity.setProductId(voucher.getProductId());
            entity.setProductName(voucher.getProductName());
            entity.setIssueTransferCompany(voucher.getIssueTransferCompany());
            entity.setIssueTransferCompanyId(voucher.getIssueTransferCompanyId());
            entity.setCreateDate(new Date());
            entity.setTransferStatus(ApprovalStatusEnum.APPROVAL_ING.getCode());
            entity.setTransferFlag(transferInfoDTO.getTransferFlag());
            saveOrUpdate(entity);
        }
        try {
            getStartHandler().handler(
                    ScfWorkFlowConstant.SCF_FLOW_TRANSFER,
                    voucher.getId(),
                    voucher,
                    voucher.getCorpId(),
                    currentUser.getId());
        } catch (Exception e) {
            log.error("应收凭证进行转让操作启动审批流报错!", e);
            throw new BusinessException(String.format("应收凭证进行转让操作启动审批流报错:%s" + e.getMessage()));

        }
    }

    /**
     * 一对多转让金额校验
     */
    private void validateOneToManyTransfer(List<ScfReceivableTransferReceiveDTO> transferInfoList,
                                           ScfReceivableVoucherInfo voucher) {
        // 接收单位不能等于开立单位
        for (ScfReceivableTransferReceiveDTO item : transferInfoList) {
            if (ObjectUtil.equal(item.getReceiveCompanyId(), voucher.getIssueTransferCompanyId())) {
                throw new BusinessException("接收单位不能等于开立单位!");
            }
        }
        boolean isOnlineSign = OnlineEnum.ONLINE.getCode().equals(voucher.getOnlineSign());
        // 校验接口是否开通权限
        if (isOnlineSign) {
            checkJudge(voucher.getPlatformId());
        }
        validateAllowSplit(transferInfoList, voucher);
        if (!ScfBusinessConstant.ALLOW_SPLIT_NO.equals(voucher.getAllowSplit())) {
            BigDecimal totalTransferAmt = transferInfoList.stream()
                    .map(ScfReceivableTransferReceiveDTO::getTransferAmt)
                    .filter(amt -> amt != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (voucher.getVoucherAmt() != null
                    && totalTransferAmt.compareTo(voucher.getVoucherAmt()) > 0) {
                throw new BusinessException("接收方信息转让金额总和不能超过凭证金额!");
            }
        }
    }

    /**
     * 不允许拆分校验：凭证不允许拆分时，一对多转让（含暂存）只能 1 个接收单位且转让金额必须等于凭证金额
     */
    private void validateAllowSplit(List<ScfReceivableTransferReceiveDTO> transferInfoList,
                                    ScfReceivableVoucherInfo voucher) {
        if (ScfBusinessConstant.ALLOW_SPLIT_NO.equals(voucher.getAllowSplit())) {
            if (transferInfoList.size() != 1) {
                throw new BusinessException("该笔债权凭证不允许拆分，接收单位只能存在1个!");
            }
            ScfReceivableTransferReceiveDTO targetItem = transferInfoList.get(0);
            if (voucher.getVoucherAmt() != null && targetItem.getTransferAmt() != null
                    && targetItem.getTransferAmt().compareTo(voucher.getVoucherAmt()) != 0) {
                throw new BusinessException("该笔债权凭证不允许拆分，转让金额必须等于凭证金额!");
            }
        }
    }

    /**
     * 转让前置校验接口，根据 transferFlag 分发一对多或多对一校验。
     */
    @Override
    public void paymentValidateTransfer(ScfPayTransferDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getTransferFlag())) {
            throw new BusinessException("转让标识不能为空!");
        }
        if (StringUtils.isBlank(dto.getVoucherIds())) {
            throw new BusinessException("应收债权凭证id集合不能为空!");
        }
        if (ScfBusinessConstant.TRANSFER_FLAG_ONE_TO_MANY.equals(dto.getTransferFlag())) {
            String voucherId = StringUtils.splitToList(",", dto.getVoucherIds()).get(0);
            ScfReceivableVoucherInfo voucher = scfReceivableVoucherInfoDao.get(voucherId);
            if (voucher == null) {
                throw new BusinessException("未找到对应的应收债权凭证，凭证ID：" + voucherId);
            }
            ScfReceivableTransferReceiveDTO receiveDTO = new ScfReceivableTransferReceiveDTO();
            receiveDTO.setReceiveCompany(dto.getReceiverName());
            receiveDTO.setReceiveCompanyId(dto.getReceiverCorpId());
            receiveDTO.setReceiverCreditCode(dto.getReceiverCreditCode());
            receiveDTO.setTransferDate(dto.getTransferDate());
            receiveDTO.setTransferAmt(dto.getTransferMoney());
            receiveDTO.setPurpose(dto.getPurpose());
            validateOneToManyTransfer(Collections.singletonList(receiveDTO), voucher);
            return;
        }
        if (!ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(dto.getTransferFlag())) {
            throw new BusinessException("转让标识不正确!");
        }
        List<ScfReceivableVoucherInfo> voucherInfoList = scfReceivableVoucherInfoDao.listBy(
                "id", dto.getVoucherIds(), MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(voucherInfoList)) {
            throw new BusinessException("未找到所选应收债权凭证信息!");
        }
        ScfReceivableTransferInfoDTO transferInfoDTO = new ScfReceivableTransferInfoDTO();
        transferInfoDTO.setReceiveCompany(dto.getReceiverName());
        transferInfoDTO.setReceiveCompanyId(dto.getReceiverCorpId());
        transferInfoDTO.setReceiverCreditCode(dto.getReceiverCreditCode());
        transferInfoDTO.setTransferDate(dto.getTransferDate());
        transferInfoDTO.setTransferAmt(dto.getTransferMoney());
        transferInfoDTO.setPurpose(dto.getPurpose());
        validateManyToOneTransfer(voucherInfoList, transferInfoDTO);
    }

    /**
     * 多对一转让前置校验：
     * 1) 同一持有单位、同一开立/转让单位
     * 2) 转让金额不能大于应收债权凭证金额之和
     */
    private void validateManyToOneTransfer(List<ScfReceivableVoucherInfo> receivableVoucherInfo,
                                            ScfReceivableTransferInfoDTO transferInfoDTO) {
        String corpId = receivableVoucherInfo.get(0).getCorpId();
        String issueTransferCompanyId = receivableVoucherInfo.get(0).getIssueTransferCompanyId();
        boolean allMatch = receivableVoucherInfo.stream().allMatch(v ->
                ObjectUtil.equal(corpId, v.getCorpId())
                        && ObjectUtil.equal(issueTransferCompanyId, v.getIssueTransferCompanyId())
        );
        if (!allMatch) {
            throw new BusinessException("您选择的债权凭证不满足批量转让条件，请选择同一持有单位，同一开立/转让单位的债权凭证进行批量转让");
        }
        // 接收单位不能等于开立单位
        if (ObjectUtil.equal(transferInfoDTO.getReceiveCompanyId(), issueTransferCompanyId)) {
            throw new BusinessException("接收单位不能等于开立单位!");
        }
        BigDecimal totalVoucherAmt = receivableVoucherInfo.stream()
                .map(ScfReceivableVoucherInfo::getVoucherAmt)
                .filter(amt -> amt != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal transferAmt = transferInfoDTO.getTransferAmt();
        if (transferAmt != null && transferAmt.compareTo(totalVoucherAmt) != 0) {
            throw new BusinessException("转让金额合计与应收债权凭证金额合计不一致!");
        }
        // 校验所在平台是否开通接口权限
        receivableVoucherInfo.stream()
                .filter(voucher -> OnlineEnum.ONLINE.getCode().equals(voucher.getOnlineSign()))
                .map(ScfReceivableVoucherInfo::getPlatformId)
                .filter(ObjectUtil::isNotEmpty)
                .distinct()
                .forEach(this::checkJudge);
    }

    /**
     * 按 id 集合同时更新应收凭证审批状态和业务状态
     */
    private void updateVoucherApprovalAndBusinessStatusByIds(List<String> ids, String approvalStatus, String voucherStatus) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        String hql = "update ScfReceivableVoucherInfo set approvalStatus = :approvalStatus, voucherStatus = :voucherStatus where id in (:ids)";
        Map<String, Object> params = new HashMap<>();
        params.put("approvalStatus", approvalStatus);
        params.put("voucherStatus", voucherStatus);
        params.put("ids", ids);
        scfReceivableVoucherInfoDao.batchExecute(hql, params);
    }

    /**
     * 按 voucherCode 同时更新审批状态和业务状态
     */
    private void updateVoucherApprovalAndBusinessStatusByVoucherCode(String voucherCode, String approvalStatus, String voucherStatus) {
        String hql = "update ScfReceivableVoucherInfo set approvalStatus = :approvalStatus, voucherStatus = :voucherStatus where voucherCode = :voucherCode";
        Map<String, Object> params = new HashMap<>();
        params.put("approvalStatus", approvalStatus);
        params.put("voucherStatus", voucherStatus);
        params.put("voucherCode", voucherCode);
        scfReceivableVoucherInfoDao.batchExecute(hql, params);
    }

    @Transactional
    @Override
    public void updateTransferInfo(ScfReceivableTransferInfoDTO transferInfoDTO) {
        ScfReceivableTransferInfo transferInfo = BeanUtil.copyProperties(transferInfoDTO, ScfReceivableTransferInfo.class);
        scfReceivableTransferInfoDao.update(transferInfo);
    }

    @Override
    public boolean deleteTransferInfo(String id) {
        delete(id);
        return true;
    }

    @Override
    public ScfReceivableTransferInfoVO getTransferInfo(String id) {
        ScfReceivableTransferInfo transferInfo = scfReceivableTransferInfoDao.get(id);
        if (transferInfo == null) {
            return null;
        }
        ScfReceivableTransferInfoVO result = BeanUtil.copyProperties(transferInfo, ScfReceivableTransferInfoVO.class);
        fillStatusName(result);
        // 详情同时返回对应的应收债权凭证信息，供前端直接展示。
        fillVoucherInfo(Collections.singletonList(result));
        return result;
    }

    /**
     * 根据应收凭证id集合查询暂存转让信息（待转让状态）：
     * 凭证id先转凭证编号，再按编号查询待转让的暂存记录（一对多返回多条接收方记录，多对一返回多条凭证记录）
     */
    @Override
    public List<ScfReceivableTransferInfoVO> listTempByVoucherIds(String voucherIds) {
        if (StringUtils.isBlank(voucherIds)) {
            return new ArrayList<>();
        }
        List<ScfReceivableVoucherInfo> vouchers = scfReceivableVoucherInfoDao.listBy("id", voucherIds, MatchTypeEnum.IN);
        if (CollectionUtil.isEmpty(vouchers)) {
            return new ArrayList<>();
        }
        List<String> voucherCodes = new ArrayList<>();
        for (ScfReceivableVoucherInfo voucher : vouchers) {
            voucherCodes.add(voucher.getVoucherCode());
        }
        List<ScfReceivableTransferInfo> list = scfReceivableTransferInfoDao.list(
                Restrictions.eq("transferStatus", ApprovalStatusEnum.DRAFT.getCode()),
                Restrictions.in("voucherCode", voucherCodes));
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<ScfReceivableTransferInfoVO> result = new ArrayList<>();
        for (ScfReceivableTransferInfo info : list) {
            result.add(BeanUtil.copyProperties(info, ScfReceivableTransferInfoVO.class));
        }
        return result;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        log.info("ScfReceivableTransferInfoServiceImpl==flowCompleted:{}", JSON.toJSONString(workFlowResult));
        ScfReceivableVoucherInfo voucherInfo = scfReceivableVoucherInfoDao.get(workFlowResult.getBusinessKey());
        if (ObjectUtil.isEmpty(voucherInfo)) {
            return;
        }
        if (workFlowResult.getIsPass()) {
            SignHandler handler = signHandlerFactory.getHandler(voucherInfo.getOnlineSign());
            handler.processTransfer(voucherInfo);
        } else {
            // 转让打回：凭证审批状态保持开立审批通过，业务状态还原为“已签收”
            voucherInfo.setVoucherStatus(BusinessStatusEnum.SIGNED.getCode());
            voucherInfo.setApprovalStatus(ApprovalStatusEnum.APPROVAL_PASS.getCode());
            scfReceivableVoucherInfoDao.update(voucherInfo);
            // 更新转让信息表的转让状态为打回（只更新审批中的记录）
            List<ScfReceivableTransferInfo> transferList = scfReceivableTransferInfoDao.list(
                    Restrictions.eq("transferStatus", ApprovalStatusEnum.APPROVAL_ING.getCode()),
                    Restrictions.eq("voucherCode", voucherInfo.getVoucherCode())
            );
            if (CollectionUtil.isNotEmpty(transferList)){
                for (ScfReceivableTransferInfo transferInfo : transferList) {
                    transferInfo.setTransferStatus(ApprovalStatusEnum.REJECTED.getCode());
                    scfReceivableTransferInfoDao.update(transferInfo);
                }
            }
        }
        log.info("债权凭证转让审批流处理完成, voucherCode={}, isPass={}", voucherInfo.getVoucherCode(), workFlowResult.getIsPass());
    }
}
