package com.bytter.finance.domain.loan.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.dao.RepayTypeDao;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.loan.dao.CmsLoanInfoDao;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.provide.dao.LoanInfoDao;
import com.bytter.finance.dto.GuaContractCmsProvideLoanDTO;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
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
//@WorkFlow({CmsWorkFlowConstant.CMS_LOAN_INFO, CmsWorkFlowConstant.CMS_LOAN_REPAIR})
@Service("cmsLoanInfoApplyService")
@Transactional
public class CmsLoanInfoApplyService extends BaseService<CmsLoanInfo, String> implements
        ICustomVerifyHandler<CmsLoanInfoVO>, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private CmsLoanInfoDao cmsLoanInfoDao;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @Autowired
    private LoanInfoDao loanInfoDao;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private RepayTypeDao repayTypeDao;
    public void saveImport(List<CmsLoanInfoVO> cmsLoanInfoVOs, ICurrentUser user) {
        for (int i = 0; i < cmsLoanInfoVOs.size(); i++) {
            CmsLoanInfo cmsLoanInfo = new CmsLoanInfo();
            CmsLoanInfoVO cmsLoanInfoVO=cmsLoanInfoVOs.get(i);
            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsAutoRepay())) {// 是否自动还款
                if (cmsLoanInfoVO.getIsAutoRepay().equals("否")) {
                    cmsLoanInfoVO.setIsAutoRepay("0");
                } else if (cmsLoanInfoVO.getIsAutoRepay().equals("是")) {
                    cmsLoanInfoVO.setIsAutoRepay("1");
                }
            }
            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsOverRepair())) {// 是否跳过补录
                if (cmsLoanInfoVO.getIsOverRepair().equals("否")) {
                    cmsLoanInfoVO.setIsOverRepair("0");
                } else if (cmsLoanInfoVO.getIsOverRepair().equals("是")) {
                    cmsLoanInfoVO.setIsOverRepair("1");
                }
            }
            BeanUtils.copyNotNullProperties(cmsLoanInfoVO, cmsLoanInfo);
            cmsLoanInfo.setStatus(CmsConstant.TEMP);
            cmsLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_10);// 暂存
            cmsLoanInfo.setCreateDate(new Date());
            setCreateUpdateInfo(1, cmsLoanInfo, user);
            super.saveEntity(cmsLoanInfo);
            this.getEntityDao().flush();
        }
    }
    public void setCreateUpdateInfo(int type, CmsLoanInfo cmsLoanInfo, ICurrentUser user) {
        if (type == 1) {// create
            cmsLoanInfo.setCreateBy(user.getId());
            cmsLoanInfo.setCreateName(user.getUsername());
        } else {
            cmsLoanInfo.setUpdateBy(user.getId());
            cmsLoanInfo.setUpdateName(user.getUsername());
        }
    }
    @Override
    public BaseDao<CmsLoanInfo, String> getEntityDao() {
        return cmsLoanInfoDao;
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
    public ExcelVerifyHanlderResult verifyHandler(CmsLoanInfoVO cmsLoanInfoVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsLoanInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsLoanInfoVO cmsLoanInfoVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                CorpDTO corpDTO = corpService.getCorpIdByName(cmsLoanInfoVO.getCorpName());
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getCorpName())) {
                    errorMsgList.add("cms:loaninfo:corpid:notnull");// 申请单位不能为空
                } else {
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add("accebill.message.applycorpid.isnotexist");// 申请单位不存在
                    } else {
                        cmsLoanInfoVO.setCorpId(corpDTO.getId());
                    }
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getNetName())) {
                    errorMsgList.add("cms.loaninfo.netid.notnull");// 所属资金中心不能为空
                } else {
                    if (!EmptyUtils.isEmpty(corpDTO)) {
                        CorpDTO net = corpService.getNetByCorpId(corpDTO.getId());
                        if (cmsLoanInfoVO.getNetName().equals(net.getName())) {
                            cmsLoanInfoVO.setNetCode(net.getId());
                        } else {
                            errorMsgList.add("cms.loaninfo.netid.notcorpid");// 所属资金中心与申请单位不对应
                        }
                    }
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanGenreName())) {
                    errorMsgList.add("cms.loaninfo.loangener.notnull");// 贷款类型不能为空
                } else {
                    List<LoanTypeDTO> loanTypeDTOS = cmsLoanInfoService.queryLoanTypeList(BaseDataConstant.TYPE_CREDIT);
                    boolean flag = false;
                    if (!EmptyUtils.isEmpty(loanTypeDTOS)) {
                        for (LoanTypeDTO dto : loanTypeDTOS) {
                            if (cmsLoanInfoVO.getLoanGenreName().equals(dto.getName())) {
                                cmsLoanInfoVO.setLoanGenre(dto.getCode());
                                flag = true;
                            }
                        }
                        if (!flag) {
                            errorMsgList.add("cms.loaninfo.loantypes.error");// 无对应贷款类型
                        }
                    }
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanMoney())) {
                    errorMsgList.add("cms.loaninfo.loanmoney.notnull");// 贷款金额不能为空
                } else {
                    if (cmsLoanInfoVO.getLoanMoney().compareTo(BigDecimal.ZERO) < 0) {
                        errorMsgList.add("贷款金额不能小于0");// 贷款金额不能小于0
                    }
                }
                List<TbBaseRateDTO> tbBaseRates = new ArrayList<TbBaseRateDTO>();
                BtCurrencyDTO curDto = new BtCurrencyDTO();
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getCurName())) {
                    errorMsgList.add("cms.loaninfo.loancurid.notnull");// 贷款币别不能为空
                } else {
                    String curId = btCurrencyService.getCurrencyByName(cmsLoanInfoVO.getCurName());
                    curDto = btCurrencyService.getObjectById(curId);
                    if (EmptyUtils.isEmpty(curDto)) {
                        errorMsgList.add("cms.loaninfo.loancur.error");// 无对应贷款币别
                    } else {
                        cmsLoanInfoVO.setCurCode(curDto.getId());
                        // 基准利率类型
                        BtCurrencyDTO natcur = btCurrencyService.getNatualCurrency();// 查询本外币
                        if (natcur.getId().equals(cmsLoanInfoVO.getCurCode())) { // 是本外币
                            tbBaseRates = iBaseRateService.getBaseRateByType(BaseDataConstant.IS_BANKBASERATE);
                        } else {// 不是本外币
                            tbBaseRates = iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE);
                        }
                    }
                }
                boolean f = false;
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanDate())) {
                    errorMsgList.add("cms.loaninfo.loandate.notnull");// 贷款日期不能为空
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getRepayDate())) {
                    errorMsgList.add("cms.loaninfo.repaydate.notnull");// 贷款到期日不能为空
                } else {
                    if (cmsLoanInfoVO.getRepayDate().compareTo(cmsLoanInfoVO.getLoanDate()) < 0) {
                        errorMsgList.add("cms.loaninfo.repaydate.error");// 贷款到期日不能小于贷款日期
                    } else {
                        // 贷款期限
                        int itvday = (int) ((cmsLoanInfoVO.getRepayDate().getTime()
                                - cmsLoanInfoVO.getLoanDate().getTime()) / (1000 * 3600 * 24));
                        if (itvday <= 28) {
                            cmsLoanInfoVO.setLoanTerm(itvday);
                            cmsLoanInfoVO.setTernUnit("0");
                            // 贷款种类
                            cmsLoanInfoVO.setLoanClass("01");
                        } else {
                            Calendar beginDate = Calendar.getInstance();
                            Calendar endDate = Calendar.getInstance();
                            beginDate.setTime(cmsLoanInfoVO.getLoanDate());
                            endDate.setTime(cmsLoanInfoVO.getRepayDate());
                            int result = endDate.get(Calendar.MONTH) - beginDate.get(Calendar.MONTH);// 相差月数
                            int months = (endDate.get(Calendar.YEAR) - beginDate.get(Calendar.YEAR)) * 12;// 计算跨年的时候相差月数
                            int monthsDiff = result + months;
                            if (endDate.get(Calendar.DAY_OF_MONTH) - beginDate.get(Calendar.DAY_OF_MONTH) > 0) {
                                monthsDiff++;
                            }
                            cmsLoanInfoVO.setLoanTerm(monthsDiff);
                            cmsLoanInfoVO.setTernUnit("1");
                            // 贷款种类
                            if (monthsDiff <= 12) {
                                cmsLoanInfoVO.setLoanClass("01");
                            } else if (monthsDiff > 12 && monthsDiff <= 60) {
                                cmsLoanInfoVO.setLoanClass("02");
                            } else if (monthsDiff > 60) {
                                cmsLoanInfoVO.setLoanClass("03");
                            }
                        }
                        // 贷款种类
                    }
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getRepayTypeName())) {
                    errorMsgList.add("cms.provide.loaninfo.repaytypeempty");// 还款方式不能为空
                } else {
                    //String code = dictService.getCodeByName("cms.LoanRepayType", cmsLoanInfoVO.getRepayTypeName());
                    RepayTypeVO repayTypeByName = repayTypeDao.getRepayTypeByName(cmsLoanInfoVO.getRepayTypeName());
                    if (repayTypeByName == null || StrUtil.isBlank(repayTypeByName.getId())) {
                        errorMsgList.add("cms.loaninfo.repaytype.error");// 无对应还款方式
                    } else {
                        cmsLoanInfoVO.setRepayType(repayTypeByName.getId());
                    }
                }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getPurpose())) {
                    errorMsgList.add("nis.netbank.message.purpose.null");// 用途不能为空
                }
                // else {
                // if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getCorpId())) {
                // List<BtPurposeDTO> btPurposeDTOs = btPurposeService.listByCorpIdOrBusType(cmsLoanInfoVO.getCorpId(),
                // BtPurposeConstant.SYS_BUS_TYPE_CMS);
                // if (!EmptyUtils.isEmpty(btPurposeDTOs)) {
                // boolean flag = false;
                // for (BtPurposeDTO dto : btPurposeDTOs) {
                // if (cmsLoanInfoVO.getPurpose().equals(dto.getPurpose())) {
                // flag = true;
                // }
                // }
                // if (!flag) {
                // errorMsgList.add("cms.loaninfo.purpose.error");//无对应用途
                // }
                // }
                // }
                // }
                if (EmptyUtils.isEmpty(cmsLoanInfoVO.getIsOverRepair())) {
                    errorMsgList.add("cms.loaninfo.isoverrepair.isnull");// 是否跳过补录不能为空
                } else {
                    if (cmsLoanInfoVO.getIsOverRepair().equals("否")) {
//                        cmsLoanInfoVO.setIsOverRepair("0");
                    } else if (cmsLoanInfoVO.getIsOverRepair().equals("是")) {
//                        cmsLoanInfoVO.setIsOverRepair("1");
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getOverdueRate())) {
                            errorMsgList.add("cms.loaninfo.overduerate.null");// 是否跳过补录为是,逾期利率不能为空
                        }
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getIsOverDistribute())) {
                            errorMsgList.add("cms.loaninfo.isautodistribute.null");// 是否跳过补录为是，是否跳过发放功能不能为空
                        } else {
                            if (cmsLoanInfoVO.getIsOverDistribute().equals("否")) {
                                cmsLoanInfoVO.setIsOverDistribute("0");
                            } else if (cmsLoanInfoVO.getIsOverDistribute().equals("是")) {
                                cmsLoanInfoVO.setIsOverDistribute("1");
                            } else {
                                errorMsgList.add("cms.loaninfo.isoverdistribute.error");// 请输入正确的是否跳过发放功能
                            }
                        }
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getFloatWayName())) {
                            errorMsgList.add("cms.provide.loaninfo.floatwayempty");// 浮动方式不能为空
                        } else {
                            String code = dictService.getCodeByName("Cms.FloatWay", cmsLoanInfoVO.getFloatWayName());
                            if (EmptyUtils.isEmpty(code)) {
                                errorMsgList.add("cms.loaninfo.floatway.error");// 无对应浮动方式
                            } else {
                                cmsLoanInfoVO.setFloatWay(code);// 浮动方式
                                checkRateEntriesEffectDate(cmsLoanInfoVO, code, errorMsgList); // 校验利率调整日期
                            }
                        }
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getReferenceRateTypeName())) {
                            errorMsgList.add("cms.loaninfo.ratetype.notnull");// 利率类型不能为空
                        } else {
                            if (!EmptyUtils.isEmpty(tbBaseRates)) {
                                for (TbBaseRateDTO tb : tbBaseRates) {
                                    if (cmsLoanInfoVO.getReferenceRateTypeName().equals(tb.getBaseTypeName())) {
                                        f = true;
                                        cmsLoanInfoVO.setReferenceRateType(tb.getId());
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
                        LoanRateMaintenceVO loanRateMaintenceVO = null;
                        // 合同信息-利率期限校验
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getRepayCycleName())) {
                            errorMsgList.add("cms.loaninfo.repaycycle.null");// 利率期限不能为空
                        } else {
                            f = false;
                            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getReferenceRateType())
                                    && !EmptyUtils.isEmpty(curDto.getId())) {// 币别，浮动方式，利率类型
                                List<LoanRateMaintenceVO> loanRateMaintenceVOS = loanRateMaintenceService
                                        .getRateMaintenceByTimeCurBase(cmsLoanInfoVO.getLoanDate(), curDto.getId(),
                                                cmsLoanInfoVO.getReferenceRateType());
                                if (!EmptyUtils.isEmpty(loanRateMaintenceVOS)) {
                                    for (LoanRateMaintenceVO rateMaintenceVO : loanRateMaintenceVOS) {
                                        if (cmsLoanInfoVO.getRepayCycleName()
                                                .equals(rateMaintenceVO.getCreditPeriodName())) {
                                            f = true;
                                            cmsLoanInfoVO.setRepayCycle(rateMaintenceVO.getCreditPeriodId());
                                            if ("lpr".equals(rateMaintenceVO.getCode())) {// BP点数
                                                cmsLoanInfoVO.setPointsProportionSelect("01");
                                            } else {// 比例
                                                cmsLoanInfoVO.setPointsProportionSelect("02");
                                            }
                                            loanRateMaintenceVO = rateMaintenceVO;
                                            break;
                                        }
                                    }
                                } else {
                                    errorMsgList.add("cms.loaninfo.repaycycle.iserror");// 无对应利率期限
                                }
                                if (!f) {
                                    errorMsgList.add("cms.loaninfo.repaycycle.iserror");// 无对应利率期限
                                }
                            }
                        }
                        if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getPointsProportion())) {// 校验bp点数或比例
                            if (!EmptyUtils.isEmpty(loanRateMaintenceVO)) {
                                if (cmsLoanInfoVO.getPointsProportion()
                                        .compareTo(loanRateMaintenceVO.getRateUpLimit()) >= 0) {// 上限
                                    cmsLoanInfoVO
                                            .setPointsProportionText(loanRateMaintenceVO.getRateUpLimit().toString());
                                    cmsLoanInfoVO.setPointsProportion(loanRateMaintenceVO.getRateUpLimit());
                                } else if (cmsLoanInfoVO.getPointsProportion()
                                        .compareTo(loanRateMaintenceVO.getRateDownLimit()) <= 0) {// 下限
                                    cmsLoanInfoVO
                                            .setPointsProportionText(loanRateMaintenceVO.getRateDownLimit().toString());
                                    cmsLoanInfoVO.setPointsProportion(loanRateMaintenceVO.getRateDownLimit());
                                } else {
                                    cmsLoanInfoVO
                                            .setPointsProportionText(cmsLoanInfoVO.getPointsProportion().toString());
                                }
                            }
                        } else {
                            errorMsgList.add("cms.loaninfo.points.null");// BP点数或比例不能为空
                        }
                        if (EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanRate())
                                && !EmptyUtils.isEmpty(cmsLoanInfoVO.getPointsProportion())) {// 贷款利率为空则计算
                            if ("01".equals(cmsLoanInfoVO.getPointsProportionSelect())) {// bp点数
                                // rate+pointsProportionText*(0.01/100)
                                BigDecimal b = new BigDecimal("0.0001");
                                cmsLoanInfoVO.setLoanRate((loanRateMaintenceVO.getRate()
                                        .add(cmsLoanInfoVO.getPointsProportion().multiply(b))).stripTrailingZeros());
                            } else if ("02".equals(cmsLoanInfoVO.getPointsProportionSelect())) {// 比例
                                // rate+(pointsProportionText/100)*rate)
                                BigDecimal b = new BigDecimal("100");
                                cmsLoanInfoVO
                                        .setLoanRate((loanRateMaintenceVO.getRate()
                                                .add(cmsLoanInfoVO.getPointsProportion().divide(b)
                                                        .multiply(loanRateMaintenceVO.getRate())))
                                                                .stripTrailingZeros());
                            }
                        }
                    } else {
                        errorMsgList.add("cms.loaninfo.isoverrepair.error");// 请输入正确的是否跳过补录
                    }
                }
//                if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsAutoRepay())) {// 是否自动还款
//                    if (cmsLoanInfoVO.getIsAutoRepay().equals("否")) {
//                        cmsLoanInfoVO.setIsAutoRepay("0");
//                    } else if (cmsLoanInfoVO.getIsAutoRepay().equals("是")) {
//                        cmsLoanInfoVO.setIsAutoRepay("1");
//                    }
//                }
                cmsLoanInfoVO.setStatus(CmsConstant.TEMP);
                cmsLoanInfoVO.setIsHangCredit("0");
                cmsLoanInfoVO.setIsHangGuarantee("0");
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    cmsLoanInfoVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }
    // 设置调整生效日期
    public void checkRateEntriesEffectDate(CmsLoanInfoVO cmsLoanInfoVO, String code, List<String> errorMsgList) {
        String floatWayNext = "";
        if ("02".equals(cmsLoanInfoVO.getFloatWay())) { // 即时浮动
            cmsLoanInfoVO.setRateEntriesEffectDate(new Date());
        } else if ("03".equals(cmsLoanInfoVO.getFloatWay())) { // 按月浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayMonth", cmsLoanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                cmsLoanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(cmsLoanInfoVO, "m"); // 设置利率调整生效日期
        } else if ("04".equals(cmsLoanInfoVO.getFloatWay())) { // 按季浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWaySeason", cmsLoanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                cmsLoanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(cmsLoanInfoVO, "j"); // 设置利率调整生效日期
        } else if ("05".equals(cmsLoanInfoVO.getFloatWay())) { // 按半年浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayHY", cmsLoanInfoVO.getFloatWayNext());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                cmsLoanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(cmsLoanInfoVO, "b"); // 设置利率调整生效日期
        } else if ("06".equals(cmsLoanInfoVO.getFloatWay())) { // 按年浮动
            floatWayNext = dictService.getCodeByName("Cms.FloatWayYear", cmsLoanInfoVO.getFloatWayNextName());
            if (EmptyUtils.isEmpty(code)) {
                errorMsgList.add("cms.loaninfo.floatwaynext.isnull");// 无对应浮动调整日期
            } else {
                cmsLoanInfoVO.setFloatWayNext(floatWayNext);
            }
            toSetRateEntriesEffectDate(cmsLoanInfoVO, "y"); // 设置利率调整生效日期
        }
    }
    /*
     * 设置调整日期 icon: m -月 ，j -季，b-半年，y-年
     */
    public void toSetRateEntriesEffectDate(CmsLoanInfoVO cmsLoanInfoVO, String icon) {
        if ("1".equals(cmsLoanInfoVO.getFloatWayNext())) { // 次_第一天
            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanDate())) {
                cmsLoanInfoVO.setRateEntriesEffectDate(getdate(cmsLoanInfoVO.getLoanDate(), 1, icon));
            }
        } else if ("2".equals(cmsLoanInfoVO.getFloatWayNext())) { // 次_贷款日
            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getLoanDate())) {
                cmsLoanInfoVO.setRateEntriesEffectDate(getNextLoandate(cmsLoanInfoVO.getLoanDate(), icon));
            }
        } else { // 次_计息日
            ConfigDTO dto = configServiceImpl.getConfig(ConfigConstant.IST, "batchInstDay");
            int val = (int) Integer.parseInt(dto.getValue()) + 1;
            cmsLoanInfoVO.setRateEntriesEffectDate(getdate(cmsLoanInfoVO.getLoanDate(), val, icon));
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
    // 获取贷款日
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
    /**
     * 获取所有挂接担保的贷款数据
     */
    public List<GuaContractCmsProvideLoanDTO> getGuaCmsProvideLoan() {
        return loanInfoDao.getGuaCmsProvideLoan();
    }
}
