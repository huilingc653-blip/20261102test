package com.bytter.finance.domain.basedata.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.dao.CmsRateReferedDao;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.IRateMaintenceService;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款利率维护
 * @date 2017-07-26 18:38:18
 */
@Service("loanRateMaintenceService")
@Transactional
public class LoanRateMaintenceService extends BaseService<T, String> {
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @DubboReference(check = false)
    private IRateMaintenceService iRateMaintenceService;
    @Autowired
    private CmsRateReferedDao cmsRateReferedDao;

    /**
     * 功能介绍 : 根据基准利率查询是否关联贷款利率维护<br>
     *
     * @param baseRateId 基准利率ID
     * @return 贷款利率维护关联基准利率：true,否则：false
     */
    public boolean checkBaseRateById(String baseRateId) {
        return iRateMaintenceService.checkBaseRateById(baseRateId);
    }

    /**
     * 功能介绍 : 根据条件查找合适的维护利率
     */
    public List<LoanRateMaintenceVO> getRateMaintence(String plDate, String referenceRateType, String curId, String repayCycle) {
        return iRateMaintenceService.getRateMaintenceByCondition(plDate, referenceRateType, curId, repayCycle);
    }

    /**
     * 功能介绍 : 保存贷款利率集合<br>
     */
    public void saveLoanRateMainTenceVO(LoanRateMaintenceVO vo, ICurrentUser user) {
        validate(vo);
        for (TbRateMaintenceDTO loan : vo.getMainList()) {
            loan.setCreateBy(user.getLoginName());
            loan.setBaseRateId(vo.getBaseRateId());
            loan.setMoneyType(vo.getMoneyType());
            loan.setBorrowType(vo.getBorrowType());
            // loan.setTimeOfLastModifier(new Date());
            loan.setIsBaseLoanRate(vo.getIsBaseLoanRate());
            loan.setRateFromWay("0");// 新增的默认为手工维护
            loan.setIsValid(BaseDataConstant.IS_VALID_TRUE);
            loan.setStatus(BaseDataConstant.STATUS_SUBMIT);
            loan.setId(StringUtils.getUUId());
        }
        iRateMaintenceService.batchSaveDate(vo.getMainList());
        // 新增日志
        for (TbRateMaintenceDTO rate : vo.getMainList()) {
            msService.insertProcessMsg(rate.getId(), BaseDataConstant.RATE_MSG_TYPE, CmsProcessMsgConstant.MSGA_SUBMIT,
                    CmsProcessMsgConstant.MSGA_INPUT_SUBMIT, user);
        }
    }

    /**
     * 功能介绍 : 更新贷款利率<br>
     *
     * @param loan
     */
    public void modifyLoanRate(TbRateMaintenceDTO loan, ICurrentUser user) {
        if (loan.getRateDownLimit() != null && loan.getRateUpLimit() != null
                && loan.getRateUpLimit().compareTo(loan.getRateDownLimit()) < 0) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATE_LOANRATE_CONTRAST);
        }
        TbRateMaintenceDTO t = iRateMaintenceService.getById(loan.getId());
        modifyValidata(loan);
        BeanUtils.copyNotNullProperties(loan, t);
        t.setTimeOfLastModifier(new Date());
        t.setStatus(BaseDataConstant.STATUS_SUBMIT);
        t.setIsValid(BaseDataConstant.IS_VALID_TRUE);
        iRateMaintenceService.saveOrUpdate(t);
        msService.insertProcessMsg(t.getId(), BaseDataConstant.RATE_MSG_TYPE, CmsProcessMsgConstant.MSGA_SUBMIT,
                CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "", user);
    }

    @Override
    public BaseDao<T, String> getEntityDao() {
        return cmsRateReferedDao;
    }

    /**
     * 功能介绍 : <br>
     * 根据Id查询贷款利率维护VO并注入字典
     */
    public LoanRateMaintenceVO getLoanRateById(String id) {
        return iRateMaintenceService.getLoanRateById(id);
    }

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCur(Date time, String cur) {
        return iRateMaintenceService.getRateMaintenceByTimeCur(time, cur);
    }

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param: time 生效日期
     * @param: cur  币别
     * @param: base 生效日期
     */
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCurBase(Date time, String cur, String base) {
        if (time == null) {
            time = new Date();
        }
        if (StringUtils.isEmpty(cur) || StringUtils.isEmpty(base)) {
            return Collections.emptyList();
        }
        List<LoanRateMaintenceVO> recentLoanRateMaintences = iRateMaintenceService.getRateMaintenceByTimeCurBase(time, cur, base);
        if (EmptyUtils.isEmpty(recentLoanRateMaintences)) {
            return Collections.emptyList();
        }
        List<LoanRateMaintenceVO> allLoanRateMaintences = iRateMaintenceService.getAllRateMaintenceByTimeCurBase(time,
                cur, base);
        if (EmptyUtils.isEmpty(allLoanRateMaintences)) {
            return Collections.emptyList();
        }
        Map<String, Map<String, LoanRateMaintenceVO>> baseRatePeriodMoneyType2LoanRateMaintences = Maps.newHashMap();
        for (LoanRateMaintenceVO loanRateMaintenceVO : allLoanRateMaintences) {
            String baseRatePeriodMoneyType = loanRateMaintenceVO.getBaseRateId() + CommonConstant.COLON_SPLIT
                    + loanRateMaintenceVO.getCreditPeriodId() + CommonConstant.COLON_SPLIT
                    + loanRateMaintenceVO.getMoneyType();
            Map<String, LoanRateMaintenceVO> effectDate2LoanRateMaintence = baseRatePeriodMoneyType2LoanRateMaintences
                    .get(baseRatePeriodMoneyType);
            if (!baseRatePeriodMoneyType2LoanRateMaintences.containsKey(baseRatePeriodMoneyType)) {
                effectDate2LoanRateMaintence = Maps.newHashMap();
                baseRatePeriodMoneyType2LoanRateMaintences.put(baseRatePeriodMoneyType, effectDate2LoanRateMaintence);
            }
            effectDate2LoanRateMaintence.put(
                    DateUtils.format(loanRateMaintenceVO.getTimeOfTakeEffect(), DateUtils.PATTERN_CLASSICAL_SIMPLE),
                    loanRateMaintenceVO);
        }
        for (LoanRateMaintenceVO loanRateMaintence : recentLoanRateMaintences) {
            String baseRatePeriodMoneyType = loanRateMaintence.getBaseRateId() + CommonConstant.COLON_SPLIT
                    + loanRateMaintence.getCreditPeriodId() + CommonConstant.COLON_SPLIT
                    + loanRateMaintence.getMoneyType();
            Map<String, LoanRateMaintenceVO> allEffectDate2LoanRateMaintence = baseRatePeriodMoneyType2LoanRateMaintences
                    .get(baseRatePeriodMoneyType);
            loanRateMaintence.setEffectDate2LoanRateMaintence(allEffectDate2LoanRateMaintence);
            if (!EmptyUtils.isEmpty(allEffectDate2LoanRateMaintence) && !allEffectDate2LoanRateMaintence.isEmpty()) {
                String[] allEffectDateArray = new String[allEffectDate2LoanRateMaintence.keySet().size()];
                Sets.newTreeSet(allEffectDate2LoanRateMaintence.keySet()).toArray(allEffectDateArray);
                loanRateMaintence.setAllEffectDateArray(allEffectDateArray);
            }
        }
        return recentLoanRateMaintences;
    }

    /**
     * 根据Id修改状态
     */
    public void updateStatusById(List<String> ids, Integer status, String backScribe, ICurrentUser user) {
        for (String id : ids) {
            TbRateMaintenceDTO tbLoanRateMaintence = iRateMaintenceService.getById(id);
            checkDel(tbLoanRateMaintence);
            tbLoanRateMaintence.setStatus(status);
            tbLoanRateMaintence.setReturndeScribe(backScribe);
            iRateMaintenceService.updateByEntity(tbLoanRateMaintence);
            if (BaseDataConstant.STATUS_PASS == status) {
                msService.insertProcessMsg(tbLoanRateMaintence.getId(), BaseDataConstant.RATE_MSG_TYPE,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_TALLYPASS + "", user);
                // 更新全部未还款完的 利息明细
                List<ProvideLoanSend> sends = cmsProvideLoanSendService.listSendByStatus(tbLoanRateMaintence);
                for (ProvideLoanSend send : sends) {
                    // 调整个别借款利息测算数据
                    cmsProvideInterestService.adjustProvideInterest(send, user);
                }
            } else {
                msService.insertProcessMsg(tbLoanRateMaintence.getId(), BaseDataConstant.RATE_MSG_TYPE,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_CHECKBACK, user);
            }
        }
    }

    /********* 数据验证 ******/
    private void validate(LoanRateMaintenceVO vo) {
        if (EmptyUtils.isEmpty(vo.getBaseRateId()) || EmptyUtils.isEmpty(vo.getBaseRateId().trim())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_BASERATEEMPTY);
        }
        if (EmptyUtils.isEmpty(vo.getMoneyType()) || EmptyUtils.isEmpty(vo.getMoneyType().trim())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_CUREMPTY);
        }
        // 外币添加需要验证 拆借类型
        if (BaseDataConstant.NOT_BANKBASERATE.equals(vo.getIsBaseLoanRate())) {
            /*
             * if(EmptyUtils.isEmpty(vo.getBorrowType()) || EmptyUtils.isEmpty(vo.getBorrowType().trim())){ throw new
             * ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_BORROWTYPEEMPTY); }
             */
        }
        for (TbRateMaintenceDTO loan : vo.getMainList()) {
            if (EmptyUtils.isEmpty(loan.getTimeOfTakeEffect())) {
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_EFFECTTIMEEMPTY,
                        loan.getCreditPreionName());
            }
            if (loan.getRate() == null) {// || loan.getRate().compareTo(BigDecimal.ZERO) <= 0
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_RATEEMPTY,
                        loan.getCreditPreionName());
            }
            if (loan.getRateUpLimit() == null) {
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_RATEUPNLIMIT,
                        loan.getCreditPreionName());
            }
            if (loan.getRateDownLimit() == null) {
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_RATEDOWNLIMIT,
                        loan.getCreditPreionName());
            }
            BigDecimal up = loan.getRateUpLimit();
            BigDecimal down = loan.getRateDownLimit();
            // 浮动范围上限不能小于浮动范围下限
            if (up.compareTo(down) == -1) {
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATE_LOANRATE_CONTRAST);
            }
            /*
             * Calendar timeOfTakeEffect = Calendar.getInstance(); timeOfTakeEffect.setTime(loan.getTimeOfTakeEffect());
             * Calendar toDay = Calendar.getInstance(); toDay.setTime(new Date());
             */
            loan.setBaseRateId(vo.getBaseRateId());
            loan.setMoneyType(vo.getMoneyType());
            Date maxRateDate = iRateMaintenceService.checkTimeOfTakeEffect(loan);
            if (maxRateDate != null) {
                if (loan.getTimeOfTakeEffect().getTime() < maxRateDate.getTime()) {// 利率生效日期 小于 最大利率生效日期
                    throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_EFFECTTIME_LE_MAXDATE,
                            loan.getCreditPreionName(), DateUtils.formatDate(maxRateDate));
                }
            }
            // if(loan.getTimeOfTakeEffect().getTime() > new Date().getTime()){//利率生效日期大于
            // throw new
            // ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_EFFECTTIMEGETODAY,loan.getCreditPreionName(),DateUtils.formatDate());
            // }
            // 根据贷款利率期限ID与生效日期判断是否一天存在多条数据
            if (iRateMaintenceService.checkRepeatByPeriodIdAndTime(loan.getCreditPeriodId(), loan.getTimeOfTakeEffect(),
                    vo.getMoneyType(), vo.getBaseRateId())) {
                throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_DATAREPEAT,
                        loan.getCreditPreionName(), DateUtils.formatDate(loan.getTimeOfTakeEffect()));
            }
        }
    }

    /**
     * 修改验证状态
     */
    private void modifyValidata(TbRateMaintenceDTO loan) {
        if (BaseDataConstant.STATUS_PASS.equals(loan.getStatus())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_DATAPASS);
        }
    }

    /**
     * 验证数据是否删除
     */
    public void checkDel(TbRateMaintenceDTO tbLoanRateMaintence) {
        if (tbLoanRateMaintence == null) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_DATADEL);
        }
    }

    /**
     * 判断贷款利率是否被引用
     */
    public boolean isRefered(TbRateMaintenceDTO tbLoanRateMaintence) {
        boolean is = false;
        Date date = iRateMaintenceService.getNextMinDate(tbLoanRateMaintence);
        if (date != null) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_EFFECTTIME_MAX);
        }
        // 贷款
        if (cmsRateReferedDao.isAdductionLoan(tbLoanRateMaintence, date)) {
            return is = true;
        }
        // 借款
        if (cmsRateReferedDao.isAdductionProvide(tbLoanRateMaintence, date)) {
            return is = true;
        }
        // 委代
        if (cmsRateReferedDao.isAdductionPrecative(tbLoanRateMaintence, date)) {
            return is = true;
        }
        return is;
    }

    /**
     * @param curId
     * @param baseRateId
     * @param creditPeriodId
     * @return
     */
    public TbRateMaintenceDTO getLoanRateMaintenceByDate(String curId, String baseRateId, String creditPeriodId,
                                                         Date date) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId", curId);
        params.put("baseRateId", baseRateId);
        params.put("creditPeriodId", creditPeriodId);
        params.put("date", date);
        TbRateMaintenceDTO tbLoanRateMaintence = null;
        List<TbRateMaintenceDTO> tbLoanRateMaintences = iRateMaintenceService.listLoanRateMaintence(params);
        if (ListUtils.isNotNullOrEmpty(tbLoanRateMaintences)) {
            tbLoanRateMaintence = tbLoanRateMaintences.get(0);
        }
        return tbLoanRateMaintence;
    }

    /**
     * @param curId
     * @param baseRateId
     * @param creditPeriodId
     * @return
     */
    public TbRateMaintenceDTO getLoanRateMaintence(String curId, String baseRateId, String creditPeriodId) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId", curId);
        params.put("baseRateId", baseRateId);
        params.put("creditPeriodId", creditPeriodId);
        TbRateMaintenceDTO tbLoanRateMaintence = null;
        List<TbRateMaintenceDTO> tbLoanRateMaintences = iRateMaintenceService.listLoanRateMaintence(params);
        if (ListUtils.isNotNullOrEmpty(tbLoanRateMaintences)) {
            tbLoanRateMaintence = tbLoanRateMaintences.get(0);
        }
        return tbLoanRateMaintence;
    }
}
