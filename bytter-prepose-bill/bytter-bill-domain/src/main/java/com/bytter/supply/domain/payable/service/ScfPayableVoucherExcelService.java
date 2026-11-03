package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.constant.ScfPayableVoucherFieldConstant;
import com.bytter.supply.domain.payable.dao.ScfPayableVoucherDao;
import com.bytter.supply.domain.payable.dto.ScfPayableVoucherDataDTO;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherExcelVO;
import com.bytter.supply.domain.platform.dao.ScfPlatformDao;
import com.bytter.supply.domain.platform.dao.ScfProductDao;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
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
public class ScfPayableVoucherExcelService implements IExcelVerifyHandler<ScfPayableVoucherExcelVO> {

    @Autowired
    private ScfPayableVoucherService scfPayableVoucherService;

    @Autowired
    private ScfPayableVoucherDao scfPayableVoucherDao;

    @Autowired
    private ScfPlatformDao scfPlatformDao;

    @Autowired
    private ScfProductDao scfProductDao;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    public void saveImport(List<ScfPayableVoucherExcelVO> list, ICurrentUser user) {
        for (ScfPayableVoucherExcelVO excelVO : list) {
            // 回显时字段为中文，真正导入时转换回编码再落库
            if ("线下".equals(excelVO.getOnlineSign())) {
                excelVO.setOnlineSign(ScfPayableVoucherConstant.SCF_PAYABLE_NOT_ONLINE_0);
            } else if ("线上".equals(excelVO.getOnlineSign())) {
                excelVO.setOnlineSign(ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1);
            }
            if ("卖方付息".equals(excelVO.getInterestMode())) {
                excelVO.setInterestMode(ScfPayableVoucherFieldConstant.INTEREST_MODE_SELLER);
            } else if ("买方付息".equals(excelVO.getInterestMode())) {
                excelVO.setInterestMode(ScfPayableVoucherFieldConstant.INTEREST_MODE_BUYER);
            }

            ScfPayableVoucherDataDTO dto = new ScfPayableVoucherDataDTO();
            BeanUtils.copyProperties(excelVO, dto);
            dto.setApprovalStatus(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_TEMP);
            dto.setVoucherStatus(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_UNOPENED);
            dto.setSaveFlag(ScfPayableVoucherConstant.SCF_PAYABLE_SAVE_FLAG_0);
            dto.setVoucherSource(ScfPayableVoucherFieldConstant.VOUCHER_SOURCE_MANUAL);
            scfPayableVoucherService.savePayableVoucher(dto);
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(ScfPayableVoucherExcelVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<ScfPayableVoucherExcelVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            // 记录本次导入文件中已出现的债权凭证编号，用于文件内重复校验
            Set<String> seenVoucherCodes = new HashSet<>();
            for (ScfPayableVoucherExcelVO vo : list) {
                List<String> msgList = new ArrayList<>();

                // 1. 校验单位名称 → 单位ID（必填校验）
                if (StringUtils.isBlank(vo.getCorpName())) {
                    msgList.add("单位名称不能为空");
                } else {
                    CorpDTO corp = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corp) || StringUtils.isBlank(corp.getId())) {
                        msgList.add("单位名称\"" + vo.getCorpName() + "\"在系统中不存在");
                    } else {
                        vo.setCorpId(corp.getId());
                    }
                }

                // 2. 校验平台名称 → 平台ID（必填校验）
                if (StringUtils.isBlank(vo.getPlatformName())) {
                    msgList.add("平台名称不能为空");
                } else {
                    ScfPlatformVo platform = scfPlatformDao.getPlatformByName(vo.getPlatformName(), null);
                    if (EmptyUtils.isEmpty(platform) || StringUtils.isBlank(platform.getId())) {
                        msgList.add("平台名称\"" + vo.getPlatformName() + "\"在系统中不存在");
                    } else {
                        vo.setPlatformId(platform.getId());
                    }
                }

                // 3. 校验产品名称 → 产品ID（必填校验，需校验产品是否属于该平台）
                if (StringUtils.isBlank(vo.getProductName())) {
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

                // 4. 核心企业名称：支持手工录入，不做存在性校验，按录入内容直接落库

                // 5. 校验币种名称 → 币种ID（必填校验）
                if (StringUtils.isBlank(vo.getCurName())) {
                    msgList.add("币种名称不能为空");
                } else {
                    String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (StringUtils.isBlank(curId)) {
                        msgList.add("币种名称\"" + vo.getCurName() + "\"在系统中不存在");
                    } else {
                        vo.setCurId(curId);
                    }
                }

                // 6. 校验供应商名称 → 供应商ID（选填校验，外部供应商需校验收款账户并回填收款银行）
                if (StringUtils.isNotBlank(vo.getSupplyName())) {
                    CorpDTO supplyCorp = corpService.getCorpIdByName(vo.getSupplyName());
                    ExternalCorpDTO externalCorp = externalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getSupplyName());
                    boolean validSupplyCorp = !EmptyUtils.isEmpty(supplyCorp) && StringUtils.isNotBlank(supplyCorp.getId());
                    boolean validExternalCorp = !EmptyUtils.isEmpty(externalCorp);
                    if (!validSupplyCorp && !validExternalCorp) {
                        msgList.add("供应商名称\"" + vo.getSupplyName() + "\"在系统中不存在");
                    } else if (validSupplyCorp) {
                        // 优先取内部企业
                        vo.setSupplyId(supplyCorp.getId());
                    } else {
                        // 内部企业不存在，取外部企业，并校验收款账户
                        vo.setSupplyId(externalCorp.getId());
                        ExternalCorpBankAccDTO externalCorpBankAcc = externalCorpService.getExternalCorpBankAccByIdAndAcc(externalCorp.getId(), vo.getReceiveAccount());
                        if (externalCorpBankAcc == null) {
                            msgList.add("收款账号\"" + vo.getReceiveAccount() + "\"在系统中不存在");
                        } else {
                            vo.setReceiveBank(externalCorpBankAcc.getBank());
                        }
                    }
                }

                // 7. 校验凭证金额（必填校验，必须大于0）
                if (EmptyUtils.isEmpty(vo.getVoucherMoney())) {
                    msgList.add("凭证金额不能为空");
                } else if (vo.getVoucherMoney().compareTo(BigDecimal.ZERO) <= 0) {
                    msgList.add("凭证金额必须大于0");
                }

                // 8. 校验线上标识（必填校验，兼容编码0/1与中文，校验后回显为中文）
                if (EmptyUtils.isEmpty(vo.getOnlineSign())) {
                    msgList.add("线上标识不能为空");
                } else if (ScfPayableVoucherConstant.SCF_PAYABLE_NOT_ONLINE_0.equals(vo.getOnlineSign())
                        || "线下".equals(vo.getOnlineSign())) {
                    vo.setOnlineSign("线下");
                } else if (ScfPayableVoucherConstant.SCF_PAYABLE_IS_ONLINE_1.equals(vo.getOnlineSign())
                        || "线上".equals(vo.getOnlineSign())) {
                    vo.setOnlineSign("线上");
                } else {
                    msgList.add("线上标识格式不正确，只能为线下或线上");
                }

                // 9. 校验开立日期（必填校验）
                if (EmptyUtils.isEmpty(vo.getOpenDate())) {
                    msgList.add("开立日期不能为空");
                }

                // 10. 校验到期日期（必填校验，不能早于开立日期）
                if (EmptyUtils.isEmpty(vo.getExpireDate())) {
                    msgList.add("到期日期不能为空");
                } else if (vo.getOpenDate() != null && vo.getExpireDate().before(vo.getOpenDate())) {
                    msgList.add("到期日期不能早于开立日期");
                }

                // 11. 校验融资付息方式（必填校验，兼容编码1/2与中文，校验后回显为中文）
                if (EmptyUtils.isEmpty(vo.getInterestMode())) {
                    msgList.add("融资付息方式不能为空");
                } else if (ScfPayableVoucherFieldConstant.INTEREST_MODE_SELLER.equals(vo.getInterestMode())
                        || "卖方付息".equals(vo.getInterestMode())) {
                    vo.setInterestMode("卖方付息");
                } else if (ScfPayableVoucherFieldConstant.INTEREST_MODE_BUYER.equals(vo.getInterestMode())
                        || "买方付息".equals(vo.getInterestMode())) {
                    vo.setInterestMode("买方付息");
                } else {
                    msgList.add("融资付息方式\"" + vo.getInterestMode() + "\"不存在，只能为卖方付息或买方付息");
                }

                // 12. 合同金额校验（选填，若填写了合同金额则需大于0）
                if (vo.getContractMoney() != null && vo.getContractMoney().compareTo(BigDecimal.ZERO) <= 0) {
                    msgList.add("合同金额必须大于0");
                }

                // 13. 额度余额校验（选填，若填写了额度余额则需 >= 0）
                if (vo.getQuotaBalance() != null && vo.getQuotaBalance().compareTo(BigDecimal.ZERO) < 0) {
                    msgList.add("额度余额不能小于0");
                }

                // 14. 校验债权凭证编号不能重复（文件内重复 + 数据库已存在）
                if (StringUtils.isNotBlank(vo.getVoucherCode())) {
                    if (!seenVoucherCodes.add(vo.getVoucherCode())) {
                        msgList.add("债权凭证编号\"" + vo.getVoucherCode() + "\"在导入文件中重复");
                    } else if (scfPayableVoucherDao.countByVoucherCode(vo.getVoucherCode(), null) > 0) {
                        msgList.add("债权凭证编号\"" + vo.getVoucherCode() + "\"已存在，请勿重复录入");
                    }
                }

                vo.addErrorMsg(msgList);
            }
        }
    }
}
