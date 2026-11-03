package com.bytter.supply.domain.payable.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.bill.api.scf.dto.ScfPayableVoucherSelDTO;
import com.bytter.bill.api.scf.dto.ScfPayBindDTO;
import com.bytter.bill.api.scf.dto.ScfPayGenerateDTO;
import com.bytter.bill.api.scf.dto.ScfPayTransferDTO;
import com.bytter.bill.api.scf.dto.ScfReceivableVoucherSelDTO;
import com.bytter.bill.api.scf.dto.ScfVoucherValidateDTO;
import com.bytter.bill.api.scf.dto.ScfVoucherValidateResultDTO;
import com.bytter.bill.api.scf.service.IScfVoucherPayService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.constant.ScfPayableVoucherFieldConstant;
import com.bytter.supply.domain.payable.dao.ScfPayableVoucherDao;
import com.bytter.supply.domain.payable.entity.ScfPayableVoucher;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherVO;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.service.ScfReceivableTransferInfoService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.ExternalCorpDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 供应链金融-付款办理（51 应付债权凭证）门面实现
 * <p>
 * 由 bytter-service-payment 模块通过 dubbo 调用，跨模块完成：
 * - 应付选票（已开立凭证）
 * - 提交前凭证校验
 * - 51-未开立：付款审批通过后生成暂存应付凭证
 * - 51-已开立：付款审批通过后校验已关联的开立凭证仍有效
 * <p>
 * 用户上下文由 DubboUserProviderFilter 从付款模块透传，此处可直接使用
 * {@link UserContextHelper#helper()#getCurrentUser()}。
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Slf4j
@Service("scfVoucherPayServiceImpl")
@DubboService(interfaceClass = IScfVoucherPayService.class)
public class ScfVoucherPayServiceImpl implements IScfVoucherPayService {

    @Autowired
    private ScfPayableVoucherDao scfPayableVoucherDao;

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;

    @Autowired
    private ScfReceivableTransferInfoService scfReceivableTransferInfoService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @Override
    public List<ScfPayableVoucherSelDTO> getPayableVoucherByIds(List<String> ids) {
        List<ScfPayableVoucherVO> voList = scfPayableVoucherDao.listPayableVoucherByIds(ids);
        List<ScfPayableVoucherSelDTO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(voList)) {
            for (ScfPayableVoucherVO vo : voList) {
                ScfPayableVoucherSelDTO dto = new ScfPayableVoucherSelDTO();
                dto.setId(vo.getId());
                dto.setVoucherNo(vo.getVoucherNo());
                dto.setVoucherCode(vo.getVoucherCode());
                dto.setVoucherMoney(vo.getVoucherMoney());
                dto.setCurId(vo.getCurId());
                dto.setCurName(vo.getCurName());
                dto.setPlatformId(vo.getPlatformId());
                dto.setPlatformName(vo.getPlatformName());
                dto.setProductId(vo.getProductId());
                dto.setProductName(vo.getProductName());
                dto.setCorpId(vo.getCorpId());
                dto.setCorpName(vo.getCorpName());
                dto.setSupplyId(vo.getSupplyId());
                dto.setSupplyName(vo.getSupplyName());
                dto.setOnlineSign(vo.getOnlineSign());
                dto.setOpenDate(vo.getOpenDate());
                dto.setExpireDate(vo.getExpireDate());
                dto.setPromisePayDate(vo.getPromisePayDate());
                dto.setInterestMode(vo.getInterestMode());
                dto.setVoucherStatus(vo.getVoucherStatus());
                dto.setVoucherStatusName(ScfPayableVoucherConstant.getBusinessStatusName(vo.getVoucherStatus()));
                dto.setApprovalStatus(vo.getApprovalStatus());
                dto.setApprovalStatusName(ScfPayableVoucherConstant.getApprovalStatusName(vo.getApprovalStatus()));
                dto.setPayId(vo.getPayId());
                dto.setPayNo(vo.getPayNo());
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public ScfVoucherValidateResultDTO validateVoucher(ScfVoucherValidateDTO dto) {
        ScfVoucherValidateResultDTO result = new ScfVoucherValidateResultDTO();
        if (dto == null || CollectionUtil.isEmpty(dto.getVoucherIds())) {
            return fail(result, "请选择债权凭证");
        }
        List<String> voucherIds = dto.getVoucherIds();
        if ("1".equals(dto.getVoucherType())) {
            validatePayableVouchers(voucherIds, dto, result);
        } else {
            return fail(result, "凭证类型不正确");
        }
        return result;
    }

    /**
     * 应付债权凭证校验：已开立、审批通过、供应商/平台/融资产品一致
     */
    private void validatePayableVouchers(List<String> voucherIds, ScfVoucherValidateDTO dto,
                                         ScfVoucherValidateResultDTO result) {
        List<ScfPayableVoucher> vouchers = scfPayableVoucherDao.list(voucherIds);
        if (CollectionUtil.isEmpty(vouchers) || vouchers.size() != voucherIds.size()) {
            fail(result, "部分应付债权凭证不存在，请刷新后重新选择");
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        String platformId = null, productId = null, supplyId = null, supplyName = null;
        for (ScfPayableVoucher v : vouchers) {
            if (!ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK.equals(v.getApprovalStatus())
                    || !ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED.equals(v.getVoucherStatus())) {
                fail(result, "存在非已开立状态的应付债权凭证，请重新选择");
                return;
            }
            total = total.add(v.getVoucherMoney() == null ? BigDecimal.ZERO : v.getVoucherMoney());
            platformId = v.getPlatformId();
            productId = v.getProductId();
            supplyId = v.getSupplyId();
            supplyName = v.getSupplyName();
        }
        Set<String> platformSet = vouchers.stream().map(ScfPayableVoucher::getPlatformId).collect(Collectors.toSet());
        Set<String> productSet = vouchers.stream().map(ScfPayableVoucher::getProductId).collect(Collectors.toSet());
        Set<String> supplySet = vouchers.stream().map(ScfPayableVoucher::getSupplyId).collect(Collectors.toSet());
        if (platformSet.size() > 1 || productSet.size() > 1 || supplySet.size() > 1) {
            fail(result, "所选应付债权凭证的平台、融资产品或供应商不一致，请重新选择");
            return;
        }
        result.setValid(true);
        result.setTotalMoney(total);
        result.setPlatformId(platformId);
        result.setProductId(productId);
        result.setSupplyId(supplyId);
        result.setSupplyName(supplyName);
    }

    private ScfVoucherValidateResultDTO fail(ScfVoucherValidateResultDTO result, String message) {
        result.setValid(false);
        result.setMessage(message);
        return result;
    }

    @Transactional
    @Override
    public String generateTempPayableVoucher(ScfPayGenerateDTO dto) {
        if (dto == null) {
            throw new BusinessException("生成暂存应付债权凭证参数不能为空");
        }
        ScfPayableVoucher voucher = new ScfPayableVoucher();
        voucher.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.SCF_PAYABLE_VOUCHER_NO));
        voucher.setPlatformId(dto.getPlatformId());
        voucher.setProductId(dto.getProductId());
        // 开立单位 = 付款单的付款单位
        voucher.setCorpId(dto.getCorpId());
        voucher.setCurId(dto.getCurId());
        voucher.setVoucherMoney(dto.getVoucherMoney());
        // 反写供应商ID：按 开立单位+供应商名称 匹配客商，收款账号校验归属，都对上才写 supply_id
        voucher.setSupplyId(resolveSupplyId(dto));
        voucher.setSupplyName(dto.getSupplyName());
        voucher.setSupplyCreditCode(dto.getSupplyCreditCode());
        voucher.setReceiveAccount(dto.getReceiveAccount());
        voucher.setReceiveBank(dto.getReceiveBank());
        voucher.setInterestMode(dto.getInterestMode());
        voucher.setPromisePayDate(dto.getPromisePayDate());
        voucher.setOnlineSign(dto.getOnlineSign());
        // 付款单生成 -> 暂存/未开立，审批通过后由用户发起开立
        voucher.setVoucherSource(ScfPayableVoucherFieldConstant.VOUCHER_SOURCE_PAYMENT);
        voucher.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_TEMP);
        voucher.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_UNOPENED);
        // 付款单标识：随审批通过一起打标，避免该凭证后续开立后可被其他付款单再次选择
        voucher.setPayId(dto.getPayId());
        voucher.setPayNo(dto.getPayNo());
        // 开立时间 = 审批通过时间（生成暂存凭证的时点）
        voucher.setOpenDate(new Date());
        voucher.setCreateDate(new Date());
        voucher.setCreateBy(resolveCreateBy(dto.getCreateBy()));
        scfPayableVoucherDao.save(voucher);
        // 付款单与凭证的关联通过中间表 nis_scf_info 承载，此处仅返回新生成凭证ID供付款侧写中间表
        return voucher.getId();
    }

    @Transactional
    @Override
    public void bindPayableVoucher(ScfPayBindDTO dto) {
        if (dto == null || CollectionUtil.isEmpty(dto.getVoucherIds())) {
            throw new BusinessException("绑定应付债权凭证的凭证id不能为空");
        }
        List<ScfPayableVoucher> vouchers = scfPayableVoucherDao.list(dto.getVoucherIds());
        if (CollectionUtil.isEmpty(vouchers) || vouchers.size() != dto.getVoucherIds().size()) {
            throw new BusinessException("部分应付债权凭证不存在，请刷新后重新选择");
        }
        // 审批通过时校验凭证仍为"已开立/审批通过"状态，并把付款单标识写入凭证（选票时据此排除已关联凭证）
        for (ScfPayableVoucher v : vouchers) {
            if (!ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK.equals(v.getApprovalStatus())
                    || !ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED.equals(v.getVoucherStatus())) {
                throw new BusinessException("应付债权凭证[" + v.getVoucherNo() + "]已不是开立状态，请刷新后重新选择");
            }
            // 防重复关联：凭证已被其他付款单打标则报错（同一付款单重复审批通过时 pay_id 相同，放行）
            if (StringUtils.isNotBlank(v.getPayId()) && !v.getPayId().equals(dto.getPayId())) {
                throw new BusinessException("应付债权凭证[" + v.getVoucherNo() + "]已被付款单[" + v.getPayNo() + "]关联，请刷新后重新选择");
            }
        }
        // 打标：仅在付款单审批通过时写入关联付款单ID/单号
        for (ScfPayableVoucher v : vouchers) {
            v.setPayId(dto.getPayId());
            v.setPayNo(dto.getPayNo());
            v.setUpdateBy(dto.getUpdateBy());
            v.setUpdateDate(new Date());
            scfPayableVoucherDao.update(v);
        }
    }

    /**
     * 52-应收债权凭证：保存转让草稿（前端提交付款单前调用）
     * <p>再次变更时先释放原转让暂存（删除旧草稿并恢复凭证开立审批状态），再保存当前转让数据。
     * 多对一（transferFlag=2）：voucherIds 全部凭证转让一家，金额取凭证金额；
     * 单笔（其余）：voucherIds 第一张凭证转让一家，金额取前端传值。
     */
    @Transactional
    @Override
    public String saveVoucherTransfer(ScfPayTransferDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getVoucherIds())) {
            throw new BusinessException("应收债权凭证转让暂存，凭证id不能为空");
        }
        // 再次变更：释放原先的转让暂存，再保存当前转让数据（逗号分隔）
        if (StringUtils.isNotBlank(dto.getTransferIds())) {
            scfReceivableTransferInfoService.deleteTransferDrafts(StringUtils.splitToList(",", dto.getTransferIds()));
        }
        ScfReceivableTransferInfoDTO transferDTO = new ScfReceivableTransferInfoDTO();
        transferDTO.setTransferFlag(dto.getTransferFlag());
        transferDTO.setSendStatus(ScfBusinessConstant.SEND_STATUS_SAVE);
        transferDTO.setReceiveCompany(dto.getReceiverName());
        transferDTO.setReceiveCompanyId(dto.getReceiverCorpId());
        transferDTO.setReceiverCreditCode(dto.getReceiverCreditCode());
        // 开立/转让单位必须以凭证信息为准，不能使用前端传入的值。
        // temporarySaveTransfer 落库时会按凭证逐笔回填单位及币种信息。
        transferDTO.setTransferDate(dto.getTransferDate());
        transferDTO.setTransferAmt(dto.getTransferMoney());
        transferDTO.setPurpose(dto.getPurpose());
        if (ScfBusinessConstant.TRANSFER_FLAG_MANY_TO_ONE.equals(dto.getTransferFlag())) {
            // 多对一：多张凭证转让一家（金额由 doTemporarySaveManyToOne 内部取凭证金额）
            transferDTO.setVoucherIds(dto.getVoucherIds());
        } else {
            // 单笔：取第一张凭证编号
            String voucherId = StringUtils.splitToList(",", dto.getVoucherIds()).get(0);
            ScfReceivableVoucherInfo voucher = scfReceivableVoucherInfoDao.get(voucherId);
            if (voucher == null) {
                throw new BusinessException("未找到对应的应收债权凭证，凭证ID：" + voucherId);
            }
            transferDTO.setVoucherCode(voucher.getVoucherCode());
        }
        List<String> ids = scfReceivableTransferInfoService.temporarySaveTransfer(transferDTO);
        return String.join(",", ids);
    }

    /**
     * 52-应收债权凭证：按转让草稿ID集合反查应收债权凭证（付款单编辑回显）
     * <p>由转让草稿反查凭证编号再查凭证，合并转让暂存信息（接收方/金额/日期/用途）返回，按凭证去重。
     */
    @Override
    public List<ScfReceivableVoucherSelDTO> getReceivableVoucherByTransferIds(List<String> transferIds) {
        List<ScfReceivableVoucherSelDTO> result = new ArrayList<>();
        if (CollectionUtil.isEmpty(transferIds)) {
            return result;
        }
        List<ScfReceivableTransferInfo> drafts = scfReceivableTransferInfoService.getTransferInfoByIds(transferIds);
        if (CollectionUtil.isEmpty(drafts)) {
            return result;
        }
        Map<String, ScfReceivableVoucherInfo> voucherMap = loadVoucherByCodes(drafts);
        // 按凭证ID去重：单个转让一条草稿对应一张凭证；一对多可能多条草稿对应同一张凭证
        Map<String, ScfReceivableVoucherSelDTO> voucherResultMap = new LinkedHashMap<>();
        for (ScfReceivableTransferInfo draft : drafts) {
            ScfReceivableVoucherInfo voucher = voucherMap.get(draft.getVoucherCode());
            if (voucher == null) {
                continue;
            }
            ScfReceivableVoucherSelDTO dto = voucherResultMap.get(voucher.getId());
            if (dto == null) {
                dto = toSelDTO(voucher);
                dto.setTransferAmt(draft.getTransferAmt());
                dto.setReceiveCompany(draft.getReceiveCompany());
                dto.setReceiveCompanyId(draft.getReceiveCompanyId());
                dto.setTransferDate(draft.getTransferDate());
                dto.setPurpose(draft.getPurpose());
                dto.setTransferFlag(draft.getTransferFlag());
                voucherResultMap.put(voucher.getId(), dto);
            }
        }
        return new ArrayList<>(voucherResultMap.values());
    }

    /**
     * 按凭证编号批量加载应收债权凭证（去重），返回 凭证编号 → 凭证
     */
    private Map<String, ScfReceivableVoucherInfo> loadVoucherByCodes(List<ScfReceivableTransferInfo> drafts) {
        List<String> voucherCodes = drafts.stream()
                .map(ScfReceivableTransferInfo::getVoucherCode)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, ScfReceivableVoucherInfo> map = new HashMap<>();
        if (CollectionUtil.isEmpty(voucherCodes)) {
            return map;
        }
        List<ScfReceivableVoucherInfo> vouchers = scfReceivableVoucherInfoDao.listBy("voucherCode", String.join(",", voucherCodes), MatchTypeEnum.IN);
        if (CollectionUtil.isNotEmpty(vouchers)) {
            for (ScfReceivableVoucherInfo v : vouchers) {
                map.put(v.getVoucherCode(), v);
            }
        }
        return map;
    }

    /**
     * 应收债权凭证 → 回显DTO（凭证基本信息，不含转让草稿信息）
     */
    private ScfReceivableVoucherSelDTO toSelDTO(ScfReceivableVoucherInfo v) {
        ScfReceivableVoucherSelDTO dto = new ScfReceivableVoucherSelDTO();
        dto.setId(v.getId());
        dto.setVoucherNo(v.getVoucherNo());
        dto.setVoucherCode(v.getVoucherCode());
        dto.setVoucherAmt(v.getVoucherAmt());
        dto.setCurId(v.getCurId());
        dto.setCurName(v.getCurName());
        dto.setPlatformId(v.getPlatformId());
        dto.setPlatformName(v.getPlatformName());
        dto.setProductId(v.getProductId());
        dto.setProductName(v.getProductName());
        dto.setCorpId(v.getCorpId());
        dto.setCorpName(v.getCorpName());
        dto.setIssueTransferCompany(v.getIssueTransferCompany());
        dto.setIssueTransferCompanyId(v.getIssueTransferCompanyId());
        dto.setOnlineSign(v.getOnlineSign());
        dto.setVoucherStatus(v.getVoucherStatus());
        dto.setApprovalStatus(v.getApprovalStatus());
        dto.setExpireDate(v.getExpireDate());
        dto.setPromisePayDate(v.getPromisePayDate());
        return dto;
    }

    /**
     * 解析创建人：优先取付款侧传入，取不到则回退到透传的当前用户
     */
    private String resolveCreateBy(String createBy) {
        if (StringUtils.isNotBlank(createBy)) {
            return createBy;
        }
        try {
            ICurrentUser user = UserContextHelper.helper().getCurrentUser();
            if (user != null && StringUtils.isNotBlank(user.getLoginName())) {
                return user.getLoginName();
            }
        } catch (Exception e) {
            log.warn("获取当前用户创建人信息失败，忽略", e);
        }
        return null;
    }

    /**
     * 反写供应商ID：按 开立单位+供应商名称 匹配客商，并用收款账号校验归属。
     * 名称匹配且（未提供收款账号 或 收款账号属于该客商）时返回客商ID，否则返回 null。
     */
    private String resolveSupplyId(ScfPayGenerateDTO dto) {
        if (StringUtils.isBlank(dto.getCorpId()) || StringUtils.isBlank(dto.getSupplyName())) {
            return null;
        }
        try {
            ExternalCorpDTO externalCorp = externalCorpService.findByCorpIdAndName(dto.getCorpId(), dto.getSupplyName());
            if (externalCorp == null || StringUtils.isBlank(externalCorp.getId())) {
                log.warn("按开立单位+供应商名称未匹配到客商：corpId={}, supplyName={}", dto.getCorpId(), dto.getSupplyName());
                return null;
            }
            // 收款账号校验归属：未填账号时仅按名称匹配；填了账号则必须属于该客商
            if (StringUtils.isNotBlank(dto.getReceiveAccount())) {
                if (externalCorpService.getExternalCorpBankAccByIdAndAcc(externalCorp.getId(), dto.getReceiveAccount()) == null) {
                    log.warn("客商[{}]下未找到收款账号[{}]，不反写supply_id", dto.getSupplyName(), dto.getReceiveAccount());
                    return null;
                }
            }
            return externalCorp.getId();
        } catch (Exception e) {
            log.warn("反写供应商ID失败，忽略：corpId={}, supplyName={}", dto.getCorpId(), dto.getSupplyName(), e);
            return null;
        }
    }
}
