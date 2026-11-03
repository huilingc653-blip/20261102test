package com.bytter.finance.domain.provide.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.ProvideConstant;
import com.bytter.finance.domain.basedata.service.*;
import com.bytter.finance.domain.basedata.vo.AccrualTypeVO;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.provide.dao.LoanInfoApplyDao;
import com.bytter.finance.domain.provide.dao.LoanInfoDao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("loanInfoApplyService")
@Transactional
public class LoanInfoApplyService extends BaseService<CmsProvideLoanInfo, String>
        implements ICorpDeleteVerifyService, ICorpDisableVerifyService, ICustomVerifyHandler<LoanInfoVO> {
    @Autowired
    private LoanInfoDao loanInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private LoanInfoApplyDao loanInfoApplyDao;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IBtBankService btBankService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private LoanInfoService loanInfoService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @Autowired
    private RepayTypeService repayTypeService;
    @Autowired
    private AccrualTypeService accrualTypeService;
    @Autowired
    private LoanTypeService loanTypeService;

    public void saveImport(List<LoanInfoVO> loanInfoVOs, ICurrentUser user) {
        for (int i = 0; i < loanInfoVOs.size(); i++) {
            LoanInfoVO vo = loanInfoVOs.get(i);
            CmsProvideLoanInfo cmsProvideLoanInfo = new CmsProvideLoanInfo();
            if (StringUtils.isEmpty(vo.getIsRmbRat())) {
                vo.setIsRmbRat(RationConstant.RAT_COMMON_N);// 不折合人民币
            }
            BeanUtils.copyNotNullProperties(vo, cmsProvideLoanInfo);
            validata(cmsProvideLoanInfo);
            CorpDTO corp = corpServiceImpl.get(cmsProvideLoanInfo.getCorpId());
            if (StringUtils.isEmpty(corp.getNetId())) {// 顶级网点 没有netid
                cmsProvideLoanInfo.setNetId(corp.getId());
            } else {
                cmsProvideLoanInfo.setNetId(corp.getNetId());
            }
            if (!"01".equals(cmsProvideLoanInfo.getFloatWay())) {// 非固定利率
                LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(
                        DateUtils.formatDate(cmsProvideLoanInfo.getPlDate(), "yyyy-MM-dd"),
                        cmsProvideLoanInfo.getReferenceRateType(), cmsProvideLoanInfo.getCurId(),
                        cmsProvideLoanInfo.getRepayCycle());
                cmsProvideLoanInfo.setBenchmarkRate(rate.getRate());// 设置基础利率
            }
            btBankService.saveBtBank(cmsProvideLoanInfo.getBankId(), cmsProvideLoanInfo.getBankType());// 保存银行信息
            // 用途
            cmsProvideLoanInfo.setPlPurpose(vo.getPlPurpose());
            cmsProvideLoanInfo.setBondBankAcc(vo.getBondBankAcc());
            cmsProvideLoanInfo.setStatus(CmsConstant.TEMP);
            cmsProvideLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_10);
            // 不挂授信担保
            cmsProvideLoanInfo.setIsHangCredit(CommonConstant.NO);
            cmsProvideLoanInfo.setIsHookBond(CommonConstant.NO);
            cmsProvideLoanInfo.setIsHangGuarantee(CommonConstant.NO);
            if (StringUtils.isNotEmpty(cmsProvideLoanInfo.getBankId())) {
                // 保存银行信息到btBank表
                btBankService.saveBtBank(cmsProvideLoanInfo.getBankId(), cmsProvideLoanInfo.getBankType());
            }
            msService.insertProcessMsg(cmsProvideLoanInfo.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_INPUT + "", user);
            loanInfoService.save(cmsProvideLoanInfo, user);
        }
    }

    /**
     * 功能介绍 : 借款录入验证<br>
     */
    private void validata(CmsProvideLoanInfo loan) {
        if (EmptyUtils.isEmpty(loan.getCorpId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CORPEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlClass())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLCLASSEMPTY);
        }
        /*
         * if(EmptyUtils.isEmpty(loan.getBankType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKTYPEEMPTY); }
         */
        if (EmptyUtils.isEmpty(loan.getBankId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKIDEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlMoney())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLMONEYEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getCurId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CURIDEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlTerm())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTERMEMPTY);
        }
        if (loan.getPlDate() != null && loan.getRepayDate() != null) {
            if (loan.getPlDate().getTime() > loan.getRepayDate().getTime()) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLDATELEREPAYDATE);
            }
        }
        if (EmptyUtils.isEmpty(loan.getRepayType())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_REPAYTYPEEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getContractTerm())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CONTRACTTERMEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getFloatWay())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_FLOATWAYEMPTY);
        }
        if (!CmsConstant.FLOAT_WAY_01.equals(loan.getFloatWay()) && loan.getRateEntriesEffectDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_EFFECTDATEEMPTY);
        }
        /**
         * 利率验证
         */
        // if(loan.getPlRate() == null || loan.getPlRate().compareTo(BigDecimal.ZERO) == 0){
        // throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PlRATEEMPTY);
        // }
        if (EmptyUtils.isEmpty(loan.getPlPurpose())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLPURPOSEEMPTY);
        }
        if (loan.getPlPurpose().length() > 40) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PURPOSEEMPTY_LENGTH);
        }
        /*
         * if(EmptyUtils.isEmpty(loan.getMainPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_MAINPLTYPEEMPTY); }
         * if(EmptyUtils.isEmpty(loan.getPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTYPEEMPTY); }
         */
    }

    @Override
    public BaseDao<CmsProvideLoanInfo, String> getEntityDao() {
        return loanInfoApplyDao;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        return null;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        return null;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(LoanInfoVO infoVO, Object... objects) {
        return null;
    }

    @Override
    public void verifyHandler(List<LoanInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (LoanInfoVO loanInfoVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(loanInfoVO.getCorpName())) {
                    errorMsgList.add("cms:loaninfo:corpid:notnull");// 申请单位不能为空
                } else {
                    CorpDTO corpDTO = corpService.getCorpIdByName(loanInfoVO.getCorpName());
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add("accebill.message.applycorpid.isnotexist");// 申请单位不存在
                    } else {
                        loanInfoVO.setCorpId(corpDTO.getId());
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getBankName())) {
                    errorMsgList.add("cms.provide.loaninfo.banknull");// 借款银行不能为空
                } else {
                    //TODO 服务需要外调
//                    BtBankDTO btBankDTO = loanInfoApplyDao.getBanlBybankName(loanInfoVO.getBankName());
                    BtBankDTO btBankDTO = btBankService.getByBankName(loanInfoVO.getBankName());
                    if (EmptyUtils.isEmpty(btBankDTO)) {
                        errorMsgList.add("cms.provide.loaninfo.banknerror");// 借款银行不存在
                    } else {
                        loanInfoVO.setBankId(btBankDTO.getBankCode());
                        loanInfoVO.setBankType(btBankDTO.getBankTypeId());
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getPlMoney())) {
                    errorMsgList.add("借款金额不能为空");// 借款金额不能为空
                } else {
                    if (loanInfoVO.getPlMoney().compareTo(BigDecimal.ZERO) < 0) {
                        errorMsgList.add("cms.loaninfo.plmoney.message.miszero");// 借款金额不能小于0
                    }
                }
                BtCurrencyDTO curDto = new BtCurrencyDTO();
                List<TbBaseRateDTO> tbBaseRates = new ArrayList<TbBaseRateDTO>();
                if (EmptyUtils.isEmpty(loanInfoVO.getCurName())) {
                    errorMsgList.add("cms.provide.loaninfo.curidempty");// 借款币别不能为空
                } else {
                    String curId = btCurrencyService.getCurrencyByName(loanInfoVO.getCurName());
                    curDto = btCurrencyService.getObjectById(curId);
                    if (EmptyUtils.isEmpty(curDto)) {
                        errorMsgList.add("cms.provide.loaninfo.curidemptyerror");// 无对应借款币别
                    } else {
                        loanInfoVO.setCurId(curDto.getId());
                        // 基准利率类型
                        BtCurrencyDTO natcur = btCurrencyService.getNatualCurrency();// 查询本外币
                        if (natcur.getId().equals(loanInfoVO.getCurId())) { // 是本外币
                            tbBaseRates = iBaseRateService.getBaseRateByType(BaseDataConstant.IS_BANKBASERATE);
                        } else {// 不是本外币
                            tbBaseRates = iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE);
                        }
                    }
                }
                boolean f = false;
                if (EmptyUtils.isEmpty(loanInfoVO.getReferenceRateTypeName())) {
                    errorMsgList.add("cms.loaninfo.ratetype.notnull");// 利率类型不能为空
                } else {
                    if (!EmptyUtils.isEmpty(tbBaseRates)) {
                        for (TbBaseRateDTO tb : tbBaseRates) {
                            if (loanInfoVO.getReferenceRateTypeName().equals(tb.getBaseTypeName())) {
                                f = true;
                                loanInfoVO.setReferenceRateType(tb.getId());
                                if (EmptyUtils.isEmpty(loanInfoVO.getRateQuotedPriceDate())) {// 利率报价日期
                                    loanInfoVO.setRateQuotedPriceDate(tb.getTimeOfTakeEffect());
                                }
                                break;
                            }
                        }
                    } else {
                        errorMsgList.add("cms.loaninfo.ratetype.error");// 无对应利率类型
                    }
                    if (!f) {
                        errorMsgList.add("cms.loaninfo.ratetype.error");// 无对应利率类型
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getPlDate())) {
                    errorMsgList.add("cms.loaninfo.loandate.notnull");// 借款日期不能为空
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getRepayDate())) {
                    errorMsgList.add("cms.loaninfo.repaydate.notnull");// 借款到期日不能为空
                }
                if (!EmptyUtils.isEmpty(loanInfoVO.getRepayDate()) && !EmptyUtils.isEmpty(loanInfoVO.getPlDate())) {
                    if (loanInfoVO.getRepayDate().compareTo(loanInfoVO.getPlDate()) < 0) {
                        errorMsgList.add("cms.loaninfo.repaydate.iserror");// 借款到期日不能小于借款日期
                    } else {
                        // 贷款期限
                        int itvday = (int) ((loanInfoVO.getRepayDate().getTime() - loanInfoVO.getPlDate().getTime())
                                / (1000 * 3600 * 24));
                        if (itvday <= 28) {
                            loanInfoVO.setPlTerm(itvday);
                            loanInfoVO.setTernUnit("D");
                            loanInfoVO.setContractTerm("01");// 合同期限，短期
                            // 贷款种类
                            loanInfoVO.setPlClass("01");
                        } else {
                            Calendar beginDate = Calendar.getInstance();
                            Calendar endDate = Calendar.getInstance();
                            beginDate.setTime(loanInfoVO.getPlDate());
                            endDate.setTime(loanInfoVO.getRepayDate());
                            int result = endDate.get(Calendar.MONTH) - beginDate.get(Calendar.MONTH);// 相差月数
                            int months = (endDate.get(Calendar.YEAR) - beginDate.get(Calendar.YEAR)) * 12;// 计算跨年的时候相差月数
                            int monthsDiff = result + months;
                            if (endDate.get(Calendar.DAY_OF_MONTH) - beginDate.get(Calendar.DAY_OF_MONTH) > 0) {
                                monthsDiff++;
                            }
                            loanInfoVO.setPlTerm(monthsDiff);
                            loanInfoVO.setTernUnit("M");
                            if (monthsDiff < 13) {
                                loanInfoVO.setContractTerm("01");// 合同期限，短期
                            } else if (13 <= monthsDiff && monthsDiff <= 60) {
                                loanInfoVO.setContractTerm("02");// 中期
                            } else if (monthsDiff > 60) {
                                loanInfoVO.setContractTerm("03");// 长期期
                            }
                        }
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getPlClassName())) {
                    errorMsgList.add("cms.provide.plclass.null");// 借款种类不能为空
                } else {
                    List<LoanTypeDTO> loanTypeDTOS = loanTypeService
                            .queryLoanTypeList(BaseDataConstant.TYPE_BORROW);
                    if (!EmptyUtils.isEmpty(loanTypeDTOS)) {
                        boolean flag = false;
                        for (LoanTypeDTO dto : loanTypeDTOS) {
                            if (loanInfoVO.getPlClassName().equals(dto.getName())) {
                                flag = true;
                                loanInfoVO.setPlClass(dto.getCode());
                                loanInfoVO.setPlClassId(dto.getId());
                                break;
                            }
                        }
                        if (!flag) {
                            errorMsgList.add("cms.provide.plclass.error");// 无对应借款种类
                        }
                    } else {
                        errorMsgList.add("cms.provide.plclass.error");// 无对应借款种类
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getRepayTypeName())) {
                    errorMsgList.add("cms.provide.loaninfo.repaytypeempty");// 还款方式不能为空
                } else {
                    RepayTypeVO typeVO = repayTypeService.getRepayTypeByName(loanInfoVO.getRepayTypeName());
                    if (EmptyUtils.isEmpty(typeVO)) {
                        errorMsgList.add("cms.loaninfo.repaytype.error");// 无对应还款方式
                    } else {
                        loanInfoVO.setRepayType(typeVO.getId());
                    }
                }
                // 计提方式
                if (EmptyUtils.isEmpty(loanInfoVO.getAccrualTypeName())) {
                    errorMsgList.add("计提方式不能为空");// 还款方式不能为空
                } else {
                    AccrualTypeVO accrualTypeVO = accrualTypeService
                            .queryAccrualTypeByName(loanInfoVO.getAccrualTypeName());
                    if (EmptyUtils.isEmpty(accrualTypeVO)) {
                        errorMsgList.add("无对应计提方式");// 无对应计提方式
                    } else {
                        loanInfoVO.setAccrualType(accrualTypeVO.getId());
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getPlPurpose())) {
                    errorMsgList.add("nis.netbank.message.purpose.null");// 用途不能为空
                }
                // else {
                // if (!EmptyUtils.isEmpty(loanInfoVO.getCorpId())) {
                // List<BtPurposeDTO> btPurposeDTOs = btPurposeService.listByCorpIdOrBusType(loanInfoVO.getCorpId(),
                // BtPurposeConstant.SYS_BUS_TYPE_CMS);
                // if (!EmptyUtils.isEmpty(btPurposeDTOs)) {
                // boolean flag = false;
                // for (BtPurposeDTO dto : btPurposeDTOs) {
                // if (loanInfoVO.getPlPurpose().equals(dto.getPurpose())) {
                // flag = true;
                // }
                // }
                // if (!flag) {
                // errorMsgList.add("cms.loaninfo.purpose.error");//无对应用途
                // }
                // }
                // }
                // }
                if (EmptyUtils.isEmpty(loanInfoVO.getFloatWayName())) {
                    errorMsgList.add("cms.provide.loaninfo.floatwayempty");// 浮动方式不能为空
                } else {
                    String code = dictService.getCodeByName("Cms.FloatWay", loanInfoVO.getFloatWayName());
                    if (EmptyUtils.isEmpty(code)) {
                        errorMsgList.add("cms.loaninfo.floatway.error");// 无对应浮动方式
                    } else {
                        loanInfoVO.setFloatWay(code);// 浮动方式
                        checkRateEntriesEffectDate(loanInfoVO, code, errorMsgList); // 校验利率调整日期
                    }
                }
                // 利息测算基数
                if (EmptyUtils.isEmpty(loanInfoVO.getRateBase())) {
                    errorMsgList.add("cms.provide.ratebase.null");
                } else if (!loanInfoVO.getRateBase().equals("360") && !loanInfoVO.getRateBase().equals("365")) {
                    errorMsgList.add("cms.provide.ratebase.true");
                }
                LoanRateMaintenceVO loanRateMaintenceVO = null;
                if (EmptyUtils.isEmpty(loanInfoVO.getRepayCycleName())) {
                    errorMsgList.add("cms.loaninfo.repaycycle.null");// 利率期限不能为空
                } else {
                    f = false;
                    if (!EmptyUtils.isEmpty(loanInfoVO.getReferenceRateType()) && !EmptyUtils.isEmpty(curDto.getId())) {// 币别，浮动方式，利率类型
                        List<LoanRateMaintenceVO> loanRateMaintenceVOS = loanRateMaintenceService.getRateMaintenceByTimeCurBase
                                (loanInfoVO.getPlDate(), curDto.getId(),loanInfoVO.getReferenceRateType());
                        if (!EmptyUtils.isEmpty(loanRateMaintenceVOS)) {
                            for (LoanRateMaintenceVO rateMaintenceVO : loanRateMaintenceVOS) {
                                if (loanInfoVO.getRepayCycleName().equals(rateMaintenceVO.getCreditPeriodName())) {
                                    f = true;
                                    loanInfoVO.setRepayCycle(rateMaintenceVO.getCreditPeriodId());
                                    if ("lpr".equals(rateMaintenceVO.getCode())) {// BP点数
                                        loanInfoVO.setPointsProportionSelect("01");
                                    } else {// 比例
                                        loanInfoVO.setPointsProportionSelect("02");
                                    }
                                    loanRateMaintenceVO = rateMaintenceVO;
                                    break;
                                }
                            }
                        }
                        if (!f) {
                            errorMsgList.add("cms.loaninfo.repaycycle.iserror");// 无对应利率期限
                        }
                    }
                }
                if (!EmptyUtils.isEmpty(loanInfoVO.getPointsProportionText())) {// 校验bp点数或比例
                    if (!EmptyUtils.isEmpty(loanRateMaintenceVO)) {
                        if (loanInfoVO.getPointsProportionText().compareTo(loanRateMaintenceVO.getRateUpLimit()) >= 0) {// 上限
                            loanInfoVO.setPointsProportionText(loanRateMaintenceVO.getRateUpLimit());
                            loanInfoVO.setPointsProportionText(loanRateMaintenceVO.getRateUpLimit());
                        } else if (loanInfoVO.getPointsProportionText()
                                .compareTo(loanRateMaintenceVO.getRateDownLimit()) <= 0) {// 下限
                            loanInfoVO.setPointsProportionText(loanRateMaintenceVO.getRateDownLimit());
                            loanInfoVO.setPointsProportionText(loanRateMaintenceVO.getRateDownLimit());
                        } else {
                            loanInfoVO.setPointsProportionText(loanInfoVO.getPointsProportionText());
                        }
                    }
                } else {
                    errorMsgList.add("cms.loaninfo.points.null");// BP点数或比例不能为空
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getPlDate())) {// 贷款利率为空则计算
                    if ("01".equals(loanInfoVO.getPointsProportionSelect())) {// bp点数
                        // rate+pointsProportionText*(0.01/100)
                        BigDecimal b = new BigDecimal(0.01 / 100);
                        loanInfoVO.setPlRate(
                                loanRateMaintenceVO.getRate().add(loanInfoVO.getPointsProportionText().multiply(b)));
                    } else if ("02".equals(loanInfoVO.getPointsProportionSelect())) {// 比例
                        // rate+(pointsProportionText/100)*rate)
                        BigDecimal b = new BigDecimal(100);
                        loanInfoVO.setPlRate(loanRateMaintenceVO.getRate().add(loanInfoVO.getPointsProportionText()
                                .divide(b).multiply(loanRateMaintenceVO.getRate())));
                    }
                }
                if (EmptyUtils.isEmpty(loanInfoVO.getIsOverRepairName())) {
                    errorMsgList.add("cms.loaninfo.isoverrepair.isnull");// 是否跳过补录不能为空
                } else {
                    if (loanInfoVO.getIsOverRepairName().equals("否")) {
                        loanInfoVO.setIsOverRepair("0");
                    } else if (loanInfoVO.getIsOverRepairName().equals("是")) {
                        loanInfoVO.setIsOverRepair("1");
                        if (EmptyUtils.isEmpty(loanInfoVO.getContractNo())) {
                            errorMsgList.add("cms.provide.loaninfo.ContractNonull");// 借款合同号为空
                        }
                        if (EmptyUtils.isEmpty(loanInfoVO.getCplInterestPaymentName())) {
                            errorMsgList.add("cms.provide.loaninfo.InterestPaymennull");// 付息方式为空
                        } else {
                            String cplInterestPayment = dictService.getCodeByName("provide.payInterest",
                                    loanInfoVO.getCplInterestPaymentName());
                            loanInfoVO.setCplInterestPayment(cplInterestPayment);
                        }
                        if (EmptyUtils.isEmpty(loanInfoVO.getLoanSendOutTypeName())) {
                            errorMsgList.add("cms.provide.loaninfo.SendOutTypenull");// 发放方式为空
                        } else {
                            String loanSendOutTypeName = dictService.getCodeByName("cms.loan.loanSendOutType",
                                    loanInfoVO.getLoanSendOutTypeName());
                            loanInfoVO.setLoanSendOutTypeName(loanSendOutTypeName);
                        }
                        if (EmptyUtils.isEmpty(loanInfoVO.getAcrossSendName())) {
                            errorMsgList.add("cms.provide.loaninfo.AcrossSendnull");// 发跳过借款发放功能不能为空
                        } else {
                            if (loanInfoVO.getAcrossSendName().equals("否")) {
                                loanInfoVO.setAcrossSend("0");
                            } else if (loanInfoVO.getAcrossSendName().equals("是")) {
                                loanInfoVO.setAcrossSend("1");
                            } else {
                                errorMsgList.add("cms.loaninfo.isoverdistribute.error");// 请输入正确的是否跳过发放功能
                            }
                        }
                        if (EmptyUtils.isEmpty(loanInfoVO.getOverdueRate())) {
                            errorMsgList.add("cms.provide.loaninfo.overduerateempty");// 逾期利率不能为空
                        } else {
                            if (loanInfoVO.getOverdueRate().compareTo(new BigDecimal(100)) > 0
                                    || loanInfoVO.getOverdueRate().compareTo(new BigDecimal(0)) < 0) {
                                errorMsgList.add("逾期利率不合理");
                            }
                        }
                        if (!EmptyUtils.isEmpty(loanInfoVO.getDeferredRate())) {
                            if (loanInfoVO.getDeferredRate().compareTo(new BigDecimal(100)) > 0
                                    || loanInfoVO.getDeferredRate().compareTo(new BigDecimal(0)) < 0) {
                                errorMsgList.add("展期利率不合理");
                            }
                        }
                    } else {
                        errorMsgList.add("cms.loaninfo.isoverrepair.error");// 请输入正确的是否跳过补录
                    }
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    loanInfoVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }

    // 设置调整生效日期
    public void checkRateEntriesEffectDate(LoanInfoVO loanInfoVO, String code, List<String> errorMsgList) {
        String floatWayNext = "";
        if ("02".equals(loanInfoVO.getFloatWay())) { // 即时浮动
            loanInfoVO.setRateEntriesEffectDate(new Date());
        } else if ("03".equals(loanInfoVO.getFloatWay())) { // 按月浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayMonth", loanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                loanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(loanInfoVO, "m"); // 设置利率调整生效日期
        } else if ("04".equals(loanInfoVO.getFloatWay())) { // 按季浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWaySeason", loanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                loanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(loanInfoVO, "j"); // 设置利率调整生效日期
        } else if ("05".equals(loanInfoVO.getFloatWay())) { // 按半年浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayHY", loanInfoVO.getFloatWayNext());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                loanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(loanInfoVO, "b"); // 设置利率调整生效日期
        } else if ("06".equals(loanInfoVO.getFloatWay())) { // 按年浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayYear", loanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                loanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(loanInfoVO, "y"); // 设置利率调整生效日期
        }
    }

    /*
     * 设置调整日期 icon: m -月 ，j -季，b-半年，y-年
     */
    public void toSetRateEntriesEffectDate(LoanInfoVO loanInfoVO, String icon) {
        if ("1".equals(loanInfoVO.getFloatWayNext())) { // 次_第一天
            if (!EmptyUtils.isEmpty(loanInfoVO.getPlDate())) {
                loanInfoVO.setRateEntriesEffectDate(getdate(loanInfoVO.getPlDate(), 1, icon));
            }
        } else if ("2".equals(loanInfoVO.getFloatWayNext())) { // 次_贷款日
            if (!EmptyUtils.isEmpty(loanInfoVO.getPlDate())) {
                loanInfoVO.setRateEntriesEffectDate(getNextLoandate(loanInfoVO.getPlDate(), icon));
            }
        } else { // 次_计息日
            // ConfigDTO dto = configServiceImpl.getConfig(ConfigConstant.IST, "batchInstDay");
            // int val = (int) Integer.parseInt(dto.getValue());
            loanInfoVO.setRateEntriesEffectDate(getdate(loanInfoVO.getPlDate(), 21, icon));
        }
    }

    /*
     * 获取计息日，第一天
     */
    public Date getdate(Date date, int addval, String flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if ("m".equals(flag)) { // 按月
            calendar.set(Calendar.DAY_OF_MONTH, addval);
            calendar.add(Calendar.MONTH, 1);
        }
        if ("j".equals(flag)) { // 按季
            int month = calendar.get(Calendar.MONTH) + 4;// 获取当前月三个月后的月份
            int quarter = new Double(Math.ceil((month % 12) / 3.0D)).intValue();
            int year = calendar.get(Calendar.YEAR);
            // 三个月后所在季度的第一个月
            int startMonth = quarter * 3 - 2;
            Calendar a = Calendar.getInstance();
            a.set(Calendar.YEAR, year + month / 12);
            a.set(Calendar.MONTH, startMonth - 1);
            a.set(Calendar.DATE, addval);
            calendar.setTime(a.getTime());
        }
        if ("b".equals(flag)) { // 按半年
            int month = calendar.get(Calendar.MONTH) + 7;// 获取当前月半年后的月份
            int quarter = new Double(Math.ceil((month % 12) / 6.0D)).intValue();
            int year = calendar.get(Calendar.YEAR);
            // 半年后所在半年的第一个月
            int startMonth = quarter * 6 - 5;
            Calendar a = Calendar.getInstance();
            a.set(Calendar.YEAR, year + month / 12);
            a.set(Calendar.MONTH, startMonth - 1);
            a.set(Calendar.DATE, addval);
            calendar.setTime(a.getTime());
        }
        if ("y".equals(flag)) { // 按年
            calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, addval);
        }
        return calendar.getTime();
    }

    // 获取借款日
    public Date getNextLoandate(Date date, String flag) {
        Calendar calendar = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        calendar.setTime(date);
        // next.setTime(date);
        next.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        next.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        if ("m".equals(flag)) { // 按月
            next.add(Calendar.MONTH, 1);
            int day = calendar.get(Calendar.DATE);
            int lastDay = next.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (lastDay < day) {
                day = lastDay;
            }
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, day);
        }
        if ("j".equals(flag)) { // 按季
            int month = next.get(Calendar.MONTH) + 4;// 获取当前月三个月后的月份
            int year = next.get(Calendar.YEAR);// 获取当前月三个月后的月份
            int day = calendar.get(Calendar.DATE);
            if (month / 12 >= 1) { // 跨年
                next.set(Calendar.MONTH, (month - 1) % 12);
                next.set(Calendar.YEAR, year + 1);
            } else {
                next.set(Calendar.MONTH, month - 1);
                next.set(Calendar.YEAR, year);
            }
            int lastDay = next.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (lastDay < day) {
                day = lastDay;
            }
            next.set(Calendar.DAY_OF_MONTH, day);
            calendar.setTime(next.getTime());
        }
        if ("b".equals(flag)) { // 半年
            int month = next.get(Calendar.MONTH) + 7;// 获取当前月半年后的月份
            int year = next.get(Calendar.YEAR);// 获取当前月半年后的月份
            int day = calendar.get(Calendar.DATE);
            if (month / 12 >= 1) { // 跨年
                next.set(Calendar.MONTH, (month - 1) % 12);
                next.set(Calendar.YEAR, year + 1);
            } else {
                next.set(Calendar.MONTH, month - 1);
                next.set(Calendar.YEAR, year);
            }
            int lastDay = next.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (lastDay < day) {
                day = lastDay;
            }
            next.set(Calendar.DAY_OF_MONTH, day);
            calendar.setTime(next.getTime());
        }
        if ("y".equals(flag)) { // 年
            int day = calendar.get(Calendar.DATE);
            int year = next.get(Calendar.YEAR);
            next.set(Calendar.YEAR, year + 1);
            int lastDay = next.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (lastDay < day) {
                day = lastDay;
            }
            next.set(Calendar.DAY_OF_MONTH, day);
            calendar.setTime(next.getTime());
        }
        return calendar.getTime();
    }

    @Override
    public ImportCustomRules getImportCustomRules(Object... objects) {
        return null;
    }
}
