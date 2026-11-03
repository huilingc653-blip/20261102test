package com.bytter.supply.domain.receivable.service;

import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.constant.ScfPayableVoucherFieldConstant;
import com.bytter.supply.domain.platform.dao.ScfPlatformDao;
import com.bytter.supply.domain.platform.dao.ScfProductDao;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import com.bytter.supply.domain.receivable.dao.ScfReceivableVoucherInfoDao;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherImportVO;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.enums.OnlineEnum;
import com.bytter.supply.enums.VoucherSourceEnum;
import com.bytter.supply.enums.VoucherTypeEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScfReceivableVoucherExcelService implements IExcelVerifyHandler<ScfReceivableVoucherImportVO> {

    @Autowired
    private ScfReceivableVoucherInfoDao scfReceivableVoucherInfoDao;
    @Autowired
    private ScfPlatformDao scfPlatformDao;
    @Autowired
    private ScfProductDao scfProductDao;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    public void saveImport(List<ScfReceivableVoucherImportVO> list, ICurrentUser user) {
        for (ScfReceivableVoucherImportVO vo : list) {
            // 转换凭证类型
            String voucherTypeInput = vo.getVoucherType().trim();
            if (VoucherTypeEnum.OPEN.getDesc().equals(voucherTypeInput)) {
                vo.setVoucherType(VoucherTypeEnum.OPEN.getCode());
            } else if (VoucherTypeEnum.TRANSFER.getDesc().equals(voucherTypeInput)) {
                vo.setVoucherType(VoucherTypeEnum.TRANSFER.getCode());
            }

            // 转换线上线下标志
            String onlineSignInput = vo.getOnlineSign().trim();
            if (OnlineEnum.OFFLINE.getDesc().equals(onlineSignInput)) {
                vo.setOnlineSign(OnlineEnum.OFFLINE.getCode());
            } else if (OnlineEnum.ONLINE.getDesc().equals(onlineSignInput)) {
                vo.setOnlineSign(OnlineEnum.ONLINE.getCode());
            }

            // 转换是否允许拆分
            String allowSplitInput = vo.getAllowSplit().trim();
            if ("是".equals(allowSplitInput)) {
                vo.setAllowSplit(ScfBusinessConstant.ALLOW_SPLIT_YES);
            } else if ("否".equals(allowSplitInput)) {
                vo.setAllowSplit(ScfBusinessConstant.ALLOW_SPLIT_NO);
            }

            // 融资付息方式转换
            String interestMode = vo.getInterestMode().trim();
            String interestModeCode = ScfPayableVoucherFieldConstant.toInterestCode(interestMode);
            vo.setInterestMode(interestModeCode);

            ScfReceivableVoucherInfo entity = new ScfReceivableVoucherInfo();
            BeanUtils.copyProperties(vo, entity);
            entity.setVoucherCode(vo.getVoucherCode());
            entity.setApprovalStatus(ApprovalStatusEnum.DRAFT.getCode());
            entity.setVoucherStatus(BusinessStatusEnum.UNSIGNED.getCode());
            entity.setVoucherSource(VoucherSourceEnum.MANUAL.getCode());
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.SCF_RECEIVABLE_VOUCHER_NO));
            scfReceivableVoucherInfoDao.save(entity);
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(ScfReceivableVoucherImportVO vo) {
        // 行级校验已统一迁移到批量校验 verifyHandler(List) 中执行（仿照应付债权凭证导入），
        // 单行校验由框架在批量校验后追加调用，此处返回 null 避免错误信息被重复添加
        return null;
    }

    @Override
    public void verifyHandler(List<ScfReceivableVoucherImportVO> list) {
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        // 记录本次导入文件中已出现的凭证编号，用于文件内重复校验
        Set<String> seenVoucherCodes = new HashSet<>();
        for (ScfReceivableVoucherImportVO vo : list) {
            List<String> msgList = new ArrayList<>();

            // 1. 校验凭证编号（必填）
            if (StrUtil.isBlank(vo.getVoucherCode())) {
                msgList.add("凭证编号不能为空");
            }

            // 2. 校验持有单位 → 单位ID（必填校验，持有单位为系统内部企业 sys_corp）
            if (StrUtil.isBlank(vo.getCorpName())) {
                msgList.add("持有单位不能为空");
            } else {
                CorpDTO corp = corpService.getCorpIdByName(vo.getCorpName());
                if (EmptyUtils.isEmpty(corp) || StringUtils.isBlank(corp.getId())) {
                    msgList.add("持有单位\"" + vo.getCorpName() + "\"在系统中不存在");
                } else {
                    vo.setCorpId(corp.getId());
                }
            }

            // 3. 校验平台名称 → 平台ID（必填校验）
            if (StrUtil.isBlank(vo.getPlatformName())) {
                msgList.add("平台名称不能为空");
            } else {
                ScfPlatformVo platform = scfPlatformDao.getPlatformByName(vo.getPlatformName(), null);
                if (EmptyUtils.isEmpty(platform) || StringUtils.isBlank(platform.getId())) {
                    msgList.add("平台名称\"" + vo.getPlatformName() + "\"在系统中不存在");
                } else {
                    vo.setPlatformId(platform.getId());
                }
            }

            // 4. 校验产品名称 → 产品ID（必填校验，需校验产品是否属于该平台）
            if (StrUtil.isBlank(vo.getProductName())) {
                msgList.add("产品名称不能为空");
            } else if (StringUtils.isNotBlank(vo.getPlatformId())) {
                List<ScfProductVo> products = scfProductDao.getListByPlatformId(vo.getPlatformId());
                ScfProductVo matchedProduct = null;
                if (products != null) {
                    for (ScfProductVo product : products) {
                        if (vo.getProductName().equals(product.getProductName())) {
                            matchedProduct = product;
                            break;
                        }
                    }
                }
                if (matchedProduct == null) {
                    msgList.add("产品名称\"" + vo.getProductName() + "\"在平台\"" + vo.getPlatformName() + "\"下不存在");
                } else {
                    vo.setProductId(matchedProduct.getId());
                }
            }

            // 5. 校验核心企业 → 核心企业ID（核心企业为系统内部企业 sys_corp）
            if (StrUtil.isNotBlank(vo.getCoreCompanyName())) {
                CorpDTO coreCorp = corpService.getCorpIdByName(vo.getCoreCompanyName());
                if (EmptyUtils.isEmpty(coreCorp) || StringUtils.isBlank(coreCorp.getId())) {
                    msgList.add("核心企业\"" + vo.getCoreCompanyName() + "\"在系统中不存在");
                } else {
                    vo.setCoreCompanyId(coreCorp.getId());
                }
            }

            // 6. 校验币别名称 → 币种ID（必填校验）
            if (StrUtil.isBlank(vo.getCurName())) {
                msgList.add("币别名称不能为空");
            } else {
                String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                if (StringUtils.isBlank(curId)) {
                    msgList.add("币别名称\"" + vo.getCurName() + "\"在系统中不存在");
                } else {
                    vo.setCurId(curId);
                }
            }

            // 7. 校验开立单位 → 开立单位ID（必填校验，属客商资料：先查内部企业，查不到再查客商(外部企业)）
            if (StrUtil.isBlank(vo.getIssueTransferCompany())) {
                msgList.add("开立单位不能为空");
            } else {
                CorpDTO supplyCorp = corpService.getCorpIdByName(vo.getIssueTransferCompany());
                ExternalCorpDTO externalCorp = externalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getIssueTransferCompany());
                // 判断supplyCorp是否有效
                boolean validSupplyCorp = !EmptyUtils.isEmpty(supplyCorp) && StringUtils.isNotBlank(supplyCorp.getId());
                boolean validExternalCorp = !EmptyUtils.isEmpty(externalCorp);
                if (!validSupplyCorp && !validExternalCorp) {
                    msgList.add("开立单位\"" + vo.getIssueTransferCompany() + "\"在系统中不存在");
                } else if (validSupplyCorp) {
                    // 优先取内部企业
                    vo.setIssueTransferCompanyId(supplyCorp.getId());
                } else {
                    // 内部企业不存在，取外部企业（客商）
                    vo.setIssueTransferCompanyId(externalCorp.getId());
                }
            }

            // 8. 校验流转单位 → 流转单位ID（凭证类型为流转凭证时必填，属客商资料：先查内部企业，查不到再查客商(外部企业)）
            if (StrUtil.isBlank(vo.getTransferCompany())) {
                if (isTransferVoucher(vo.getVoucherType())) {
                    msgList.add("凭证类别为流转凭证时,流转单位不能为空");
                }
            } else {
                CorpDTO transferCorp = corpService.getCorpIdByName(vo.getTransferCompany());
                ExternalCorpDTO transferExternalCorp = externalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getTransferCompany());
                boolean validTransferCorp = !EmptyUtils.isEmpty(transferCorp) && StringUtils.isNotBlank(transferCorp.getId());
                boolean validTransferExternalCorp = !EmptyUtils.isEmpty(transferExternalCorp);
                if (!validTransferCorp && !validTransferExternalCorp) {
                    msgList.add("流转单位\"" + vo.getTransferCompany() + "\"在系统中不存在");
                } else if (validTransferCorp) {
                    // 优先取内部企业
                    vo.setTransferCompanyId(transferCorp.getId());
                } else {
                    // 内部企业不存在，取外部企业（客商）
                    vo.setTransferCompanyId(transferExternalCorp.getId());
                }
            }

            // 9. 校验凭证金额（必填校验，必须大于0）
            if (vo.getVoucherAmt() == null) {
                msgList.add("凭证金额不能为空");
            } else if (vo.getVoucherAmt().compareTo(BigDecimal.ZERO) <= 0) {
                msgList.add("凭证金额必须大于0");
            }

            // 10. 校验凭证类型（必填校验，仅支持"开立凭证"/"流转凭证"，或对应code：open/transfer）
            if (StrUtil.isBlank(vo.getVoucherType())) {
                msgList.add("凭证类型不能为空");
            } else {
                String voucherTypeInput = vo.getVoucherType().trim();
                if (!"open".equals(voucherTypeInput) && !"transfer".equals(voucherTypeInput)
                        && !VoucherTypeEnum.OPEN.getDesc().equals(voucherTypeInput) && !VoucherTypeEnum.TRANSFER.getDesc().equals(voucherTypeInput)) {
                    msgList.add("凭证类型\"" + vo.getVoucherType() + "\"不合法，仅支持：开立凭证、流转凭证");
                }
            }

            // 11. 校验线上线下标志（必填校验，兼容编码0/1与中文）
            if (StrUtil.isBlank(vo.getOnlineSign())) {
                msgList.add("线上线下标志不能为空");
            } else {
                String onlineSignInput = vo.getOnlineSign().trim();
                if (!OnlineEnum.OFFLINE.getCode().equals(onlineSignInput) && !OnlineEnum.ONLINE.getCode().equals(onlineSignInput)
                        && !OnlineEnum.OFFLINE.getDesc().equals(onlineSignInput) && !OnlineEnum.ONLINE.getDesc().equals(onlineSignInput)) {
                    msgList.add("线上线下标志\"" + vo.getOnlineSign() + "\"不合法，仅支持：0、1、线上、线下");
                }
            }

            // 12. 校验是否允许拆分（必填校验，兼容编码0/1与中文）
            if (StrUtil.isBlank(vo.getAllowSplit())) {
                msgList.add("是否允许拆分不能为空");
            } else {
                String allowSplitInput = vo.getAllowSplit().trim();
                if (!"0".equals(allowSplitInput) && !"1".equals(allowSplitInput)
                        && !"是".equals(allowSplitInput) && !"否".equals(allowSplitInput)) {
                    msgList.add("是否允许拆分\"" + vo.getAllowSplit() + "\"不合法，仅支持：0、1、是、否");
                }
            }

            // 13. 校验融资付息方式（必填校验）
            if (StrUtil.isBlank(vo.getInterestMode())) {
                msgList.add("融资付息方式不能为空");
            }

            // 14. 校验签收日期（必填校验）
            if (vo.getSignDate() == null) {
                msgList.add("签约日期不能为空");
            }

            // 15. 校验到期日期（必填校验，不能早于签收日期）
            if (vo.getExpireDate() == null) {
                msgList.add("到期日期不能为空");
            } else if (vo.getSignDate() != null && vo.getExpireDate().before(vo.getSignDate())) {
                msgList.add("到期日期不能早于签约日期");
            }

            // 16. 校验凭证编号唯一性（文件内重复 + 数据库已存在）
            if (StrUtil.isNotBlank(vo.getVoucherCode())) {
                if (!seenVoucherCodes.add(vo.getVoucherCode())) {
                    msgList.add("债权凭证编号\"" + vo.getVoucherCode() + "\"在导入文件中重复");
                } else if (scfReceivableVoucherInfoDao.countByVoucherCode(vo.getVoucherCode()) > 0) {
                    msgList.add("债权凭证编号\"" + vo.getVoucherCode() + "\"已存在，请勿重复录入");
                }
            }

            vo.addErrorMsg(msgList);
        }
    }

    /**
     * 判断凭证类型是否为流转凭证（兼容编码 transfer 与中文"流转凭证"）
     */
    private boolean isTransferVoucher(String voucherType) {
        if (StrUtil.isBlank(voucherType)) {
            return false;
        }
        String type = voucherType.trim();
        return VoucherTypeEnum.TRANSFER.getCode().equals(type) || VoucherTypeEnum.TRANSFER.getDesc().equals(type);
    }
}
