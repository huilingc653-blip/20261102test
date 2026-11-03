package com.bytter.finance.domain.provide.service;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.AccrualTypeService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleDtlVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestDao;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestTempDao;
import com.bytter.finance.domain.provide.entity.*;
import com.bytter.finance.domain.provide.service.process.InterestWayHandler;
import com.bytter.finance.domain.provide.service.process.ParagraphBean;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.bytter.finance.domain.provide.vo.InstRateBean;
import com.bytter.finance.domain.provide.vo.IstFloatBean;
import com.bytter.finance.domain.provide.vo.LoanRepayVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.FcInstrateAdjustDTO;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 借款利息预计明细
 * @date 2017-08-24 15:50:26
 */
@Service("cmsProvideInterestService")
@Transactional
public class CmsProvideInterestService extends BaseService<CmsProvideInterest, String> implements ITaskService {
    @Autowired
    private CmsProvideInterestDao cmsProvideInterestDao;
    @Autowired
    private LoanInfoService loanInfoService;
    @DubboReference(check = false)
    private IFcInstrateAdjustService fcInstrateAdjustService;
    @Autowired
    private RepaymentScheduleService scheduleService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private RateFloatRuleService rateFloatRuleService;
    @Autowired
    private RepayTypeService repayTypeService;
    @Autowired
    private AccrualTypeService accrualTypeService;
    @Autowired
    private CmsProvideLoanRepayService loanRepayService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private ProvideLoanDeferredService provideLoanDeferredService;
    @Autowired
    private CmsProvideInterestTempDao cmsProvideInterestTempDao;
    @Autowired
    private CmsProvideLoanSendService loanSendService;

    @Autowired
    private ScheduleDtlTempService dtlTempService;

    @Autowired
    private CmsProvideInterestTempService tempService;

    /**
     * 功能：调整个别借款利息测算数据 调整方式包括：还款计划、计息方式、浮动方式
     *
     * @param send
     * @param user
     */
    public void adjustProvideInterest(ProvideLoanSend send, ICurrentUser user) {
        // 删除未还测算利息,重新生成
        this.deleteProvideInterest(send);
        cmsProvideInterestDao.flush();
        // 判断是否自定义还款计划
        InterestWayHandler interestHanlder = this.getInterestAdjustDetail(send);// 获取生成利息明细的必要资源
        // 1、根据参数生成分段数据 计息
        List<CmsProvideInterest> instList = interestHanlder.createInterestDetail();
        Date interestTime = DateUtils.getDate();
        for (CmsProvideInterest interest : instList) {
            interest.setInterestTime(interestTime);
        }
        cmsProvideInterestDao.batchSave(instList);
        // 将基准利率变化记录下来
        CopyOnWriteArrayList<ParagraphBean> beans = interestHanlder.getParagraphList();
        BigDecimal oldRate = send.getBenchmarkRate();
        if (!EmptyUtils.isEmpty(send.getLoanAcc())) {
            if (!EmptyUtils.isEmpty(beans) && beans.size() > 0) {
                List temp = Arrays.asList(beans.toArray());
                //将分段集合按照还款日期升序排列
                Collections.sort(temp, new Comparator<ParagraphBean>() {
                    public int compare(ParagraphBean p1, ParagraphBean p2) {
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(p1.getStartDate());
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(p2.getStartDate());
                        return c1.compareTo(c2);
                    }
                });
                beans.clear();
                beans.addAll(temp);
            }

            if (!EmptyUtils.isEmpty(oldRate)) {
                if (!EmptyUtils.isEmpty(beans) && beans.size() > 0) {
                    for (ParagraphBean bean : beans) {
                        FcInstrateAdjustDTO dto = new FcInstrateAdjustDTO();// 借款帐号的利率调整记录
                        dto.setAccount(send.getLoanAcc());
                        dto.setOperatorCode(user.getLoginName());
                        dto.setAdjustTime(bean.getStartDate());
                        // dto.setInstrateId();
                        dto.setStatus(CommonConstant.STATUE_CHECK);
                        dto.setChangtype(FinacialConstant.INSTRATE_CHANGT_YPE);
                        dto.setOldInstrate(oldRate);
                        if (!EmptyUtils.isEmpty(bean.getNewBaseRateData()) && "01".equals(bean.getNewBaseRateData())) {
                            dto.setNewInstrate(bean.getRate());
                            oldRate = bean.getRate();
                            dto.setInstrtType(InterestConstant.INSTRT_TYPE_NORMAL);
                            fcInstrateAdjustService.saveFcInstrateAdjust(dto);
                        }
                    }
                }
            }
        }
    }
    /**
     * 删除未还测算利息
     *
     * @param send
     */
    public void deleteProvideInterest(ProvideLoanSend send) {
        Set<String> periodSet = new HashSet<String>();
        cmsProvideInterestDao.deleteProvideInterest(send.getBillCode());// 删除未还的测算利息
    }
    /**
     * 构建利息明细（自定义）
     *
     * @param send
     */
    /*
     * public void createInstByCustomRepayType(ProvideLoanSend send) { List<CmsProvideInterest> instList = new
     * ArrayList<>(); List<RepaymentSchedule> repaymentSchedules =
     * cmsProvideInterestDao.getRepayMentScheduleByBillCode(send.getId()); Date interestStartDate = send.getSendDate();
     * for (RepaymentSchedule repaymentSchedule : repaymentSchedules) { if
     * (CommonConstant.YES.equals(repaymentSchedule.getIsRepay())) { continue; } CmsProvideInterest cmsProvideInterest =
     * new CmsProvideInterest(); cmsProvideInterest.setRepaymentScheduleId(repaymentSchedule.getId());//还款计划id
     * cmsProvideInterest.setInterestTime(new Date());//计息日期
     * cmsProvideInterest.setContractCode(send.getContractCode());//合同号
     * cmsProvideInterest.setBillCode(send.getBillCode());//主业务单据号 cmsProvideInterest.setCorpId(send.getCorpId());//单位id
     * cmsProvideInterest.setCurId(send.getCurCode());//币种id
     * cmsProvideInterest.setInterestType(send.getInterestWay());//计息方式
     * cmsProvideInterest.setRepayDate(repaymentSchedule.getPrediceRepayDate());//还款日期
     * cmsProvideInterest.setInterestStartDate(interestStartDate);//计息开始日期
     * cmsProvideInterest.setBlnInstDate(DateUtils.addDay(repaymentSchedule.getPrediceRepayDate(), -1));//计息结束日期
     * cmsProvideInterest.setDays(DateUtils.getOffsetDays(interestStartDate,cmsProvideInterest.getBlnInstDate()));//计息天数
     * cmsProvideInterest.setFloatPoint(BigDecimal.ZERO);//浮动利率
     * cmsProvideInterest.setInterestRate(send.getPlRate());//计息利率 cmsProvideInterest.setRateFax(BigDecimal.ZERO);//税率
     * cmsProvideInterest.setLoanMoney(repaymentSchedule.getSurplusMoney().add(repaymentSchedule.getRepaymentMoney()));/
     * /剩余本金 cmsProvideInterest.setRepayMoney(repaymentSchedule.getRepaymentMoney());//还款本金
     * cmsProvideInterest.setLeftMoney(repaymentSchedule.getSurplusMoney());//剩余本金
     * cmsProvideInterest.setInterest(repaymentSchedule.getRepaymentIst());//归还利息
     * cmsProvideInterest.setPayInterest(repaymentSchedule.getRepaymentIst());//支付利息
     * cmsProvideInterest.setTotalInterest(repaymentSchedule.getRepaymentIst());//全部利息
     * cmsProvideInterest.setUnPayInterest(BigDecimal.ZERO);//未支付利息
     * cmsProvideInterest.setRealInterest(repaymentSchedule.getRepaymentIst());//实际支付利息
     * cmsProvideInterest.setInterestFax(BigDecimal.ZERO);//利息税率
     * cmsProvideInterest.setInterestSign(InterestConstant.INTEREST_SIGN_YES);//计息标志
     * cmsProvideInterest.setStatus(0);//状态
     * cmsProvideInterest.setSignRepayDate(repaymentSchedule.getPrediceRepayDate());//分段时标识属于那一段时间
     * instList.add(cmsProvideInterest); interestStartDate = repaymentSchedule.getPrediceRepayDate(); }
     * cmsProvideInterestDao.batchSave(instList); }
     */
    /**
     * <br>
     * <b>功能：</b> 查询已还明细中累计未付利息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/10 0010 <br>
     */
    public BigDecimal setUnpayInterest(String sendCode) {
        List<CmsProvideInterest> list = cmsProvideInterestDao.queryPayedInterest(sendCode);
        BigDecimal unPayInterest = BigDecimal.ZERO;
        if (EmptyUtils.isEmptyList(list)) {
            return BigDecimal.ZERO;
        }
        for (CmsProvideInterest ist : list) {
            unPayInterest = unPayInterest.add(ist.getTotalInterest().subtract(ist.getRealInterest()));
        }
        if (unPayInterest.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return unPayInterest;
    }
    /**
     * 获取调整明细
     *
     * @return
     */
    private InterestWayHandler getInterestAdjustDetail(ProvideLoanSend send) {
        String curCode = send.getCurCode();
        String repayCycle = send.getRepayCycle();
        Set<String> periodSet = new HashSet<String>();
        periodSet.add(repayCycle + "," + curCode + "," + send.getReferenceRateType());
        String sendId = send.getId();
        String sendCode = send.getBillCode();
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        InterestWayHandler interestHanlder = new InterestWayHandler(send, info);
        interestHanlder.setRepayStartDate(getMaxRepayedDate(sendCode, send.getSendDate()));//取还款计息开始日期
        interestHanlder.setInterestStartDate(getPayInterestStartDate(send));
        interestHanlder.setUnPayInterest(setUnpayInterest(sendCode));//累计未付利息
        interestHanlder.setCmsRepaymentSchedule(repaymentScheduleService.getCmsRepaymentScheduleByBillCode(send.getId()));//还款计划主表数据
        interestHanlder.setScheduleList(cmsProvideInterestDao.getRepayMentScheduleByBillCode(sendId));//全部还款计划
        interestHanlder.setDeferredList(provideLoanDeferredService.getFinishDeferredListByBillCode(sendCode));//全部已完成展期的数据
        if (!StringUtils.isEmpty(send.getAccrualType())) {
            interestHanlder.setAccrualType(accrualTypeService.get(send.getAccrualType()));//设置计提方式
        }
        RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(send.getRepayType());
        if(!EmptyUtils.isEmpty(repayTypeVO)) {
            interestHanlder.setRepayType(repayTypeService.get(send.getRepayType()));//设置计息方式
        }
        interestHanlder.setIstWayList(cmsProvideInterestDao.getInterestWaysByBillCode(sendId));//计息方式调整
        //固定利率的浮动方式，也可以更换浮动方式为浮动，所以数据得准备不区分原来的浮动方式是什么
//        if ("01".equals(send.getFloatWay())) {//固定利率
        interestHanlder.setRateAdjustList(cmsProvideInterestDao.getRateAdjustsByInfoId(info.getId()));//查询利率调整
//        } else {//浮动利率
        interestHanlder.setBaseRateBeanList(cmsProvideInterestDao.getLoanRateMaintence(periodSet));//基准利率调整记录
        //TODO 待确认应该只有最后一次修改的浮动方式有效，而不是 最大的执行日是有效的
        List<IstFloatBean> floatBeans = cmsProvideInterestDao.getOrderInterestFloatsByBillCode(sendId);
        logger.info("FloatTypeList :{}", floatBeans);
        if (EmptyUtils.isNotEmpty(floatBeans)) {
            IstFloatBean lastTimesFloatBeans = floatBeans.get(0);
            //新浮动方式的基准利率，利率周期
            Set<String> periodSetForNewFloat = new HashSet<String>();
            periodSetForNewFloat.add(lastTimesFloatBeans.getRepayCycle() + "," + curCode + "," + lastTimesFloatBeans.getReferenceRateType());
            List<InstRateBean> instRateBeansForLastTimesFloat = cmsProvideInterestDao.getLoanRateMaintence(periodSetForNewFloat);
            lastTimesFloatBeans.setInstRateBeansForLastTimesFloat(instRateBeansForLastTimesFloat);
            floatBeans = Lists.newArrayList();
            floatBeans.add(lastTimesFloatBeans);
        }
        interestHanlder.setFloatTypeList(floatBeans);//浮动方式调整
//        }
        interestHanlder.setRateFloatRuleService(this.rateFloatRuleService);

        //获取已还款预计利息明细
        List<CmsProvideInterest> repaidList = cmsProvideInterestDao.getProvideInterestBySendCode(sendCode);
        interestHanlder.setRepaidList(repaidList); //先记住已还的数据
        return interestHanlder;
    }

    /**
     * <br>
     * <b>功能：</b> 获取还款计划最后一次还款的日期 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/10 0010 <br>
     *
     * @param billCode
     *            发放编号
     * @param loanDate
     *            发放日期
     */
    public Date getMaxRepayedDate(String billCode, Date loanDate) {
        if (EmptyUtils.isEmpty(billCode)) {
            return loanDate;
        }
        List<CmsProvideInterest> interestList = cmsProvideInterestDao.queryPayedInterest(billCode);
        if (EmptyUtils.isEmptyList(interestList)) {
            return loanDate;
        }
        Date repayDate = null;
        for (CmsProvideInterest interest : interestList) {
            if (Integer.valueOf(0).equals(interest.getStatus())) {// 未还
                break;
            }
            repayDate = interest.getRepayDate();// 取最大的还款日期
        }
        if (!EmptyUtils.isEmpty(repayDate)) {
            return repayDate;
        }
        return loanDate;
    }

    /**
     * <br>
     * <b>功能：</b> 获取还款计划最后一次还款的日期 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/10 0010 <br>
     *
     * @param send 发放数据
     */
    public Date getPayInterestStartDate(ProvideLoanSend send) {
        if (EmptyUtils.isEmpty(send.getBillCode())) {
            return send.getSendDate();
        }
        //获取已还款预计利息明细
        List<CmsProvideInterest> repaidList = cmsProvideInterestDao.getProvideInterestBySendCode(send.getBillCode());
        if (EmptyUtils.isEmptyList(repaidList)) {
            return send.getSendDate();
        }
        Date interestDate=send.getSendDate();
        RepayType repayType= repayTypeService.get(send.getRepayType());
        for(CmsProvideInterest interest:repaidList){
            if(interest.getPayInterest().compareTo(BigDecimal.ZERO)>0) {
                //还息的情况下
                if(interest.getRepayMoney().compareTo(BigDecimal.ZERO)>0) {
                    //还息且还本 1:配置还息且还本 还本金利息 2:配置还息且还本 还剩余利息 3:还息与还本在同一天
                    if (CmsConstant.IS_TRUE.equals(repayType.getInterestRepayType())) {
                        if (!CmsConstant.IS_TRUE.equals(repayType.getInterestRepayTypeNext())) {//非付还款本金产生的利息
                            interestDate=interest.getRepayDate();
                        }
                    }else {
                        interestDate=interest.getRepayDate();
                    }
                }else {
                    //只还息则为换全部利息
                    interestDate=interest.getRepayDate();
                }
            }
        }
        return interestDate;
    }

    /**
     * 根据发放单号查询利息预计明细
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsProvideInterest> queryInterestListByBillCode(String sendBillCode) {
        return cmsProvideInterestDao.queryInterestList(sendBillCode, "", "", "", "", "", "");
    }
    /************* 以下为旧利息测算明细 ***************/
    @Transactional(readOnly = true)
    public List<CmsProvideInterestVO> queryInterestList(String sendBillCode, String geRepayMoney, String leRepayMoney
            , String geRepayAccrual, String legRepayAccrual, String gegRepaymentDate, String legRepaymentDate) {
        List list = cmsProvideInterestDao.queryInterestList(sendBillCode, geRepayMoney, leRepayMoney, geRepayAccrual, legRepayAccrual, gegRepaymentDate, legRepaymentDate);
        List<CmsProvideInterestVO> listVo = BeanUtils.copyListProperties(list, CmsProvideInterestVO.class);

        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();

        String signRepayData = "";
        BigDecimal countRepayMoney = BigDecimal.ZERO;//合计还款金额
        BigDecimal countPayInterest = BigDecimal.ZERO;//合计实付利息
        BigDecimal countRealInterest = BigDecimal.ZERO;//合计实付金额

        int count = 0;
        for (int i = listVo.size() - 1; i >= 0; i--) {
            CmsProvideInterestVO vo = listVo.get(i);
            if (signRepayData.equals(DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd")) || "".equals(signRepayData)) {//同一个还款日期的利息明细数据
                count++;
            } else {
                listVo.get(i + 1).setCount(count);
                listVo.get(i + 1).setCountRepayMoney(countRepayMoney);
                listVo.get(i + 1).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(i + 1).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }

            countRepayMoney = countRepayMoney.add(vo.getRepayMoney());
            countPayInterest = countPayInterest.add(null != vo.getPayInterest() ? vo.getPayInterest() : BigDecimal.ZERO);
            if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                if (!EmptyUtils.isEmpty(countRealInterest)&&!EmptyUtils.isEmpty(vo.getRealInterest())){
                    countRealInterest = countRealInterest.add(vo.getRealInterest());
                }
            }

            signRepayData = DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd");

            if (i == 0) {
                listVo.get(0).setCount(count);
                listVo.get(0).setCountRepayMoney(countRepayMoney);
                listVo.get(0).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(0).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }
        }


        return listVo;
    }
    /************* 以下为旧利息测算明细 ***************/
    @Transactional(readOnly = true)
    public List<CmsProvideInterestVO> queryInterest(String sendBillCode) {
        List list = cmsProvideInterestDao.queryInterestList(sendBillCode, "", "", "", "", "", "");
        return BeanUtils.copyListProperties(list, CmsProvideInterestVO.class);
    }

    /************* 以下为旧利息测算明细 ***************/
    public List<CmsProvideInterestVO> buildInterestList(List<CmsProvideInterestVO> listVo) {
        if (EmptyUtils.isEmpty(listVo)) {
            return listVo;
        }
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();

        String signRepayData = "";
        BigDecimal countRepayMoney = BigDecimal.ZERO;//合计还款金额
        BigDecimal countPayInterest = BigDecimal.ZERO;//合计实付利息
        BigDecimal countRealInterest = BigDecimal.ZERO;//合计实付金额

        int count = 0;
        BigDecimal thisRlealPay = BigDecimal.ZERO;
        for (int i = listVo.size() - 1; i >= 0; i--) {
            CmsProvideInterestVO vo = listVo.get(i);
            // 校对还款计划的还款状态
            if(!org.springframework.util.ObjectUtils.isEmpty(vo.getRepaymentScheduleId())){
                RepaymentSchedule repaymentSchedule = scheduleService.get(vo.getRepaymentScheduleId(),true);
                if (!org.springframework.util.ObjectUtils.isEmpty(repaymentSchedule)) {
                    // 根据实际还款明细更新还款计划明细状态
                    if(vo.getSignRepayDate().equals(repaymentSchedule.getPrediceRepayDate()) && CmsConstant.REPAY_PLAN_INTEREST_0A1.contains(vo.getStatus().toString())){
                        if(!vo.getStatus().toString().equals(repaymentSchedule.getIsRepay())){
                            repaymentSchedule.setIsRepay(vo.getStatus().toString());
                            scheduleService.saveOrUpdate(repaymentSchedule);
                        }
                    }
                }
            }

            //还款勾对流水写入id
            LoanRepayVO repayVO =loanRepayService.getRepayInfoByInterestId(vo.getId());
            if(!EmptyUtils.isEmpty(repayVO) && CmsConstant.IS_TRUE.equals(repayVO.getBankFlowSign())){
                //勾对了流水
                vo.setTickDtlId(repayVO.getTickDtlId());
            }

            if (signRepayData.equals(DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd")) || "".equals(signRepayData)) {//同一个还款日期的利息明细数据
                count++;
                thisRlealPay = thisRlealPay.add(vo.getRealInterest() == null ? BigDecimal.ZERO : vo.getRealInterest());
            } else {
                listVo.get(i + 1).setCount(count);
                listVo.get(i + 1).setCountRepayMoney(countRepayMoney);
                listVo.get(i + 1).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(i + 1).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                listVo.get(i + 1).setRealInterest(thisRlealPay);
                thisRlealPay = vo.getRealInterest();
                count = 1;
            }

            countRepayMoney = countRepayMoney.add(vo.getRepayMoney());
            countPayInterest = countPayInterest.add(vo.getPayInterest());
            if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                countRealInterest = countRealInterest.add(vo.getRealInterest());
            }

            signRepayData = DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd");

            if (i == 0) {
                listVo.get(0).setCount(count);
                listVo.get(0).setCountRepayMoney(countRepayMoney);
                listVo.get(0).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(0).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }
        }
        return listVo;
    }

    /**
     * 查询未还的利息测算数据
     *
     * @param sendBillCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<CmsProvideInterestVO> queryAutoInterestList(String sendBillCode) {
        List list = cmsProvideInterestDao.ListInterestList(sendBillCode);
        List<CmsProvideInterestVO> listVo = BeanUtils.copyListProperties(list, CmsProvideInterestVO.class);

        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();

        String signRepayData = "";
        BigDecimal countRepayMoney = BigDecimal.ZERO;//合计还款金额
        BigDecimal countPayInterest = BigDecimal.ZERO;//合计实付利息
        BigDecimal countRealInterest = BigDecimal.ZERO;//合计实付金额

        int count = 0;
        for (int i = listVo.size() - 1; i >= 0; i--) {
            CmsProvideInterestVO vo = listVo.get(i);
            if (signRepayData.equals(DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd")) || "".equals(signRepayData)) {//同一个还款日期的利息明细数据
                count++;
            } else {
                listVo.get(i + 1).setCount(count);
                listVo.get(i + 1).setCountRepayMoney(countRepayMoney);
                listVo.get(i + 1).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(i + 1).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }

            countRepayMoney = countRepayMoney.add(vo.getRepayMoney());
            countPayInterest = countPayInterest.add(vo.getPayInterest());
            if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                countRealInterest = countRealInterest.add(vo.getRealInterest());
            }

            signRepayData = DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd");

            if (i == 0) {
                listVo.get(0).setCount(count);
                listVo.get(0).setCountRepayMoney(countRepayMoney);
                listVo.get(0).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {//未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(0).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }
        }
        return listVo;
    }


    public BaseDao<CmsProvideInterest, String> getEntityDao() {
        return cmsProvideInterestDao;
    }

    @Override
    @XxlJob("CmsProvideInterestService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.PROVIDE_CONFIG_NOTIFYDAYS);// 借款到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);

        notificationService.completeListByTag(NotificationConstant.CMS.PROVIDE_REPAY_MATURE);
        // 查询到期的发放数据
        List<CmsProvideInterest> repays = cmsProvideInterestDao.listMatureRepay(endDate);
        if (!EmptyUtils.isEmptyList(repays)) {
            for (CmsProvideInterest repay : repays) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("repayMoney", NumberUtil.formatNumeric(repay.getRepayMoney(), "#,##0.00"));
                map.put("payInterest", NumberUtil.formatNumeric(repay.getPayInterest(), "#,##0.00"));
                notificationService.send(repay.getId(), repay.getCorpId(),
                        NotificationConstant.CMS.PROVIDE_REPAY_MATURE, map);
                logger.info("您有一笔借款还款即将到期，编码：" + repay.getBillCode() + ",还款本金：" + repay.getRepayMoney() + ",还款利息：" + repay.getPayInterest());

                // repay.setRepaySign(CommonConstant.SEND_SIGN_YES);//更改发送标识
                // this.updateEntity(repay);
            }
        }
    }

    /**
     * 计算还款数据
     *
     * @param repayTypeId
     * @return
     */
    public List<RepaymentScheduleDtlVO> calInterest(String repayTypeId, Date sendDate, Date beginDate, Date endDate,
                                                    BigDecimal money, BigDecimal loanRate, BigDecimal rateBase) {
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
        RepayType repayType = repayTypeService.get(repayTypeId);
        if (BaseDataConstant.CAPITAL_AND_INTEREST_1.equals(repayType.getCapitalAndInterest())) {
//            利息按剩余本金计算
            repaymentScheduleDtlVOList.addAll(getEquivalentPrincipal(money, repayType, beginDate, endDate, loanRate, rateBase, sendDate));
        } else if (BaseDataConstant.CAPITAL_AND_INTEREST_2.equals(repayType.getCapitalAndInterest())) {
//            分期等额本息
            repaymentScheduleDtlVOList.addAll(getEqualPrincipalInterest(money, repayType, beginDate, endDate, loanRate, rateBase, sendDate));
        } else if (BaseDataConstant.CAPITAL_AND_INTEREST_3.equals(repayType.getCapitalAndInterest())) {
//            利息与本金无关（分别自定义）
            repaymentScheduleDtlVOList.addAll(getCustome(money, repayType, beginDate, endDate, sendDate));
        }
        correctRepaymentMoney(repaymentScheduleDtlVOList, money);
        return repaymentScheduleDtlVOList;
    }

    /**
     * 修正本金尾差，确保重新测算的还款本金合计等于本次待还本金。
     */
    private void correctRepaymentMoney(List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList, BigDecimal money) {
        if (CollectionUtils.isEmpty(repaymentScheduleDtlVOList) || money == null) {
            return;
        }
        BigDecimal surplusMoney = money;
        RepaymentScheduleDtlVO lastCapitalSchedule = null;
        for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
            BigDecimal repaymentMoney = repaymentScheduleDtlVO.getRepaymentMoney();
            if (repaymentMoney == null || BigDecimal.ZERO.compareTo(repaymentMoney) == 0) {
                repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
                continue;
            }
            lastCapitalSchedule = repaymentScheduleDtlVO;
            if (BigDecimal.ZERO.compareTo(surplusMoney) == 0) {
                repaymentScheduleDtlVO.setRepaymentMoney(BigDecimal.ZERO);
                resetCurrentRepayMoney(repaymentScheduleDtlVO);
                repaymentScheduleDtlVO.setSurplusMoney(BigDecimal.ZERO);
                continue;
            }
            if (repaymentMoney.compareTo(surplusMoney) > 0) {
                repaymentScheduleDtlVO.setRepaymentMoney(surplusMoney);
                resetCurrentRepayMoney(repaymentScheduleDtlVO);
                surplusMoney = BigDecimal.ZERO;
            } else {
                surplusMoney = surplusMoney.subtract(repaymentMoney);
            }
            repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
        }
        if (lastCapitalSchedule != null && BigDecimal.ZERO.compareTo(surplusMoney) != 0) {
            lastCapitalSchedule.setRepaymentMoney(lastCapitalSchedule.getRepaymentMoney().add(surplusMoney));
            resetCurrentRepayMoney(lastCapitalSchedule);
            lastCapitalSchedule.setSurplusMoney(BigDecimal.ZERO);
        }
        refreshSurplusMoney(repaymentScheduleDtlVOList, money);
    }

    private void resetCurrentRepayMoney(RepaymentScheduleDtlVO repaymentScheduleDtlVO) {
        BigDecimal repaymentMoney = repaymentScheduleDtlVO.getRepaymentMoney() == null ? BigDecimal.ZERO : repaymentScheduleDtlVO.getRepaymentMoney();
        BigDecimal repaymentIst = repaymentScheduleDtlVO.getRepaymentIst() == null ? BigDecimal.ZERO : repaymentScheduleDtlVO.getRepaymentIst();
        repaymentScheduleDtlVO.setCurrentRepayMoney(repaymentMoney.add(repaymentIst));
    }

    private void refreshSurplusMoney(List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList, BigDecimal money) {
        BigDecimal surplusMoney = money;
        for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
            BigDecimal repaymentMoney = repaymentScheduleDtlVO.getRepaymentMoney() == null ? BigDecimal.ZERO : repaymentScheduleDtlVO.getRepaymentMoney();
            surplusMoney = surplusMoney.subtract(repaymentMoney);
            repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
        }
    }

    /**
     * 等额本金
     *
     * @param money     本金
     * @param repayType 还款方式对象
     * @param beginDate 开始日
     * @param endDate   结束日
     * @param loanRate  利率
     * @param rateBase  利息测算基数
     * @return
     */
    public List<RepaymentScheduleDtlVO> getEquivalentPrincipal(BigDecimal money, RepayType repayType,
                                                               Date beginDate, Date endDate, BigDecimal
                                                                       loanRate, BigDecimal rateBase, Date sendDate) {
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
//        按次数
        if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyType())) {
            repaymentScheduleDtlVOList.addAll(repayByTimes(money, repayType));
        } else if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyType())) {
            //        按时间周周期
            if (null != beginDate && !BaseDataConstant.NUM.equals(repayType.getCapitalFrequency())) {
//              如果选择时间，则第一条还款数据为用户选择还款日期，后续则根据规则计算时间
                RepaymentScheduleDtlVO repaymentScheduleDtlVO1 = new RepaymentScheduleDtlVO();
                repaymentScheduleDtlVO1.setPrediceRepayDate(beginDate);
                repaymentScheduleDtlVO1.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO1.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO1.setInerestIsEdit(false);
                repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO1);
            } else {
                beginDate = sendDate;
            }
            repaymentScheduleDtlVOList.addAll(getByCycleTime(beginDate, endDate, repayType, true, false, false, false));
            if (CollectionUtils.isEmpty(repaymentScheduleDtlVOList)) {
                return repaymentScheduleDtlVOList;
            }
            BigDecimal repayMoney = money.divide(BigDecimal.valueOf(repaymentScheduleDtlVOList.size()), 2, RoundingMode.HALF_UP);
            BigDecimal ist = BigDecimal.ZERO;
            BigDecimal surplusMoney = money;
//        计息开始日期
            Date lastDate = beginDate;
            int days = 0;
            int count = 0;
            for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
                days = DateUtils.getOffsetDays(lastDate, repaymentScheduleDtlVO.getPrediceRepayDate());
                lastDate = repaymentScheduleDtlVO.getPrediceRepayDate();
                ist = surplusMoney.multiply(loanRate).multiply(BigDecimal.valueOf(days)).divide(rateBase, 4, RoundingMode.HALF_UP);
                repaymentScheduleDtlVO.setRepaymentIst(ist);
                if (count == repaymentScheduleDtlVOList.size() - 1) {
                    repaymentScheduleDtlVO.setRepaymentMoney(surplusMoney);
                } else {
                    repaymentScheduleDtlVO.setRepaymentMoney(repayMoney);
                }
                surplusMoney = surplusMoney.subtract(repaymentScheduleDtlVO.getRepaymentMoney());
                repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
                repaymentScheduleDtlVO.setCurrentRepayMoney(repaymentScheduleDtlVO.getRepaymentMoney().add(repaymentScheduleDtlVO.getRepaymentIst()));
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                count++;
            }
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 等额本息
     *
     * @param money     本金
     * @param repayType 还款方式对象
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param loanRate  年利率
     * @param rateBase  年利率基数
     * @return
     */
    public List<RepaymentScheduleDtlVO> getEqualPrincipalInterest(BigDecimal money, RepayType repayType,
                                                                  Date beginDate, Date endDate, BigDecimal
                                                                          loanRate, BigDecimal rateBase, Date sendDate) {
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
        if (null != beginDate) {
            RepaymentScheduleDtlVO repaymentScheduleDtlVO1 = new RepaymentScheduleDtlVO();
            repaymentScheduleDtlVO1.setPrediceRepayDate(beginDate);
            repaymentScheduleDtlVO1.setPrediceRepayDateIsEdit(false);
            repaymentScheduleDtlVO1.setPaymentMoneyIsEdit(false);
            repaymentScheduleDtlVO1.setInerestIsEdit(false);
            repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO1);
        } else {
            beginDate = sendDate;
        }
        repaymentScheduleDtlVOList.addAll(getByCycleTime(beginDate, endDate, repayType, true, false, true, true));
        BigDecimal rate = BigDecimal.ZERO;
        if (BaseDataConstant.FREQUENCY_NEXT_1.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(rateBase, 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_2.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(52), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_3.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_4.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(4), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_5.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate;
        }
//        分期等额本息 每期还款金额（本金+利息）
        BigDecimal repayMoentMoney = (money.multiply(rate)
                .multiply((BigDecimal.ONE.add(rate))
                        .pow(repaymentScheduleDtlVOList.size()))).divide((BigDecimal.ONE.add(rate)).pow(repaymentScheduleDtlVOList.size()).subtract(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        BigDecimal inst = BigDecimal.ZERO;
        BigDecimal surplusMoney = money;
        int n = 1;
        for (RepaymentScheduleDtlVO repaymentScheduleDtlVO : repaymentScheduleDtlVOList) {
            inst = (money.multiply(rate).subtract(repayMoentMoney)).multiply((BigDecimal.ONE.add(rate)).pow(n - 1)).add(repayMoentMoney);
            if (n == repaymentScheduleDtlVOList.size()) {
                //            利息
                repaymentScheduleDtlVO.setRepaymentIst(repayMoentMoney.subtract(surplusMoney));
//              本金
                repaymentScheduleDtlVO.setRepaymentMoney(surplusMoney);
            } else {
                //            利息
                repaymentScheduleDtlVO.setRepaymentIst(inst);
//                本金
                repaymentScheduleDtlVO.setRepaymentMoney((repayMoentMoney.subtract(inst)).setScale(2, RoundingMode.HALF_UP));
            }
//            本金+利息
            repaymentScheduleDtlVO.setCurrentRepayMoney(repayMoentMoney);
            surplusMoney = surplusMoney.subtract(repaymentScheduleDtlVO.getRepaymentMoney());
            surplusMoney = surplusMoney.setScale(2, RoundingMode.HALF_UP);
            repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
            repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
            repaymentScheduleDtlVO.setInerestIsEdit(false);
            repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
            n++;
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 3-利息与本金无关（分别自定义）
     *
     * @return
     */
    public List<RepaymentScheduleDtlVO> getCustome(BigDecimal money, RepayType repayType,
                                                   Date beginDate, Date endDate, Date sendDate) {
//        时间周期
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
        if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyType())) {
            if (null != beginDate) {
                RepaymentScheduleDtlVO repaymentScheduleDtlVO = new RepaymentScheduleDtlVO();
                repaymentScheduleDtlVO.setPrediceRepayDate(beginDate);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(false);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(true);
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVO.setRepaymentIst(BigDecimal.ZERO);
                repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO);
            } else {
                beginDate = sendDate;
            }
//        本金按时间周期
            repaymentScheduleDtlVOList.addAll(getByCycleTime(beginDate, endDate, repayType, true, false, true, false));
        } else if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayType.getCapitalFrequencyType())) {
//            按次数计算
            repaymentScheduleDtlVOList.addAll(repayByTimes(money, repayType));
        }

//        利息按时间周期计算
        if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getInterestFrequencyType())) {
            repaymentScheduleDtlVOList.addAll(getByCycleTime(beginDate, endDate, repayType, false, false, false, true));
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 按次数计算
     *
     * @param money
     * @param repayType
     * @return
     */
    public List<RepaymentScheduleDtlVO> repayByTimes(BigDecimal money, RepayType repayType) {
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
        BigDecimal surplusMoney = money;
        //            本金按次数
        if (BaseDataConstant.FREQUENCY_NEXT_1.equals(repayType.getCapitalFrequencyNext())) {
//                有比例
            String[] dtlStr = repayType.getCapitalFrequencyDetail().split(",");
            for (String s : dtlStr) {
                RepaymentScheduleDtlVO repaymentScheduleDtlVO = new RepaymentScheduleDtlVO();
                repaymentScheduleDtlVO.setRepaymentMoney(money.multiply(BigDecimal.valueOf(Double.valueOf(s))));
                repaymentScheduleDtlVO.setRepaymentIst(BigDecimal.ZERO);
                surplusMoney = surplusMoney.subtract(repaymentScheduleDtlVO.getRepaymentMoney());
                repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(false);
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO);
            }
        } else {
//                无比例
            int num = Integer.parseInt(repayType.getCapitalFrequencyNum());
            BigDecimal repayMoney = money.divide(BigDecimal.valueOf(num), 2, RoundingMode.HALF_UP);
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < num; i++) {
                RepaymentScheduleDtlVO repaymentScheduleDtlVO = new RepaymentScheduleDtlVO();
                if (i == num - 1) {
                    repaymentScheduleDtlVO.setRepaymentMoney(money.subtract(sum));
                } else {
                    sum = sum.add(repayMoney);
                    repaymentScheduleDtlVO.setRepaymentMoney(repayMoney);
                }
                surplusMoney = surplusMoney.subtract(repaymentScheduleDtlVO.getRepaymentMoney());
                repaymentScheduleDtlVO.setRepaymentIst(BigDecimal.ZERO);
                repaymentScheduleDtlVO.setSurplusMoney(surplusMoney);
                repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(true);
                repaymentScheduleDtlVO.setPaymentMoneyIsEdit(true);
                repaymentScheduleDtlVO.setInerestIsEdit(false);
                repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO);
            }
        }
        return repaymentScheduleDtlVOList;
    }

    /**
     * 获取还款日期
     *
     * @param beginDate      开始日期
     * @param endDate        结束日期
     * @param repayType      还款方式对象
     * @param isRepayCapital true 还本，false结息
     * @return 计算后的还款日期
     */
    public Date getDate(Date beginDate, Date endDate, RepayType repayType, Boolean isRepayCapital) {
        if (beginDate == null || endDate == null || repayType == null) {
            return endDate;
        }
        String frequencyNext = isRepayCapital ? repayType.getCapitalFrequencyNext() : repayType.getInterestFrequencyNext();
        Date repayDate = beginDate;
        // 最大循环保护：最多向后推120个月，防止死循环
        final int MAX_LOOP = 120;
        int loopCount;

        if (BaseDataConstant.FREQUENCY_NEXT_1.equals(frequencyNext)) {
            // 按日
            if (BaseDataConstant.NUM.equals(repayType.getCapitalFrequency())) {
                // 到期一次性支付
                repayDate = endDate;
            } else {
                String capFreqNumStr = repayType.getCapitalFrequencyNum();
                int dayNum = safeParseInt(capFreqNumStr, 0);
                repayDate = DateUtils.addDay(beginDate, dayNum);
            }
        } else if (BaseDataConstant.FREQUENCY_NEXT_2.equals(frequencyNext)) {
            // 按周
            if (isRepayCapital) {
                // 还本
                String capFreqNumStr = repayType.getCapitalFrequencyNum();
                int weekNum = safeParseInt(capFreqNumStr, 0);
                repayDate = DateUtils.addWeek(beginDate, weekNum);
            } else {
                // 结息，等额本息场景interestFrequencyNum为空，直接跳过循环
                String interestFreqNumStr = repayType.getInterestFrequencyNum();
                if (interestFreqNumStr != null && !interestFreqNumStr.trim().isEmpty()) {
                    int targetNum = Integer.parseInt(interestFreqNumStr.trim());
                    int num = 0;
                    loopCount = 0;
                    do {
                        repayDate = DateUtils.addDay(repayDate, 1);
                        if (DateUtils.getDayOfWeek(repayDate) == repayType.getInterestDay()) {
                            num += 1;
                        }
                        loopCount++;
                    } while (num < targetNum && loopCount < MAX_LOOP);
                }
            }
        } else if (BaseDataConstant.FREQUENCY_NEXT_3.equals(frequencyNext)) {
            // 按月
            String capFreqNumStr = repayType.getCapitalFrequencyNum();
            int monthNum = safeParseInt(capFreqNumStr, 0);
            repayDate = DateUtils.addMonth(beginDate, monthNum);
        } else if (BaseDataConstant.FREQUENCY_NEXT_4.equals(frequencyNext)) {
            // 按季
            loopCount = 0;
            String capFreq = repayType.getCapitalFrequency();
            if (BaseDataConstant.FREQUENCY_0.equals(capFreq)) {
                // 季初：1,4,7,10月
                do {
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    int month = DateUtils.getMonth(repayDate);
                    loopCount++;
                } while (!isQuarterStart(DateUtils.getMonth(repayDate)) && loopCount < MAX_LOOP);
            } else if (BaseDataConstant.FREQUENCY_3.equals(capFreq)) {
                // 季中：2,5,8,11月
                do {
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    int month = DateUtils.getMonth(repayDate);
                    loopCount++;
                } while (!isQuarterMid(DateUtils.getMonth(repayDate)) && loopCount < MAX_LOOP);
            } else if (BaseDataConstant.FREQUENCY_1.equals(capFreq)) {
                // 季末：3,6,9,12月
                do {
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    int month = DateUtils.getMonth(repayDate);
                    loopCount++;
                } while (!isQuarterEnd(DateUtils.getMonth(repayDate)) && loopCount < MAX_LOOP);
            }
        } else {
            // 按年（含半年）
            if (BaseDataConstant.FREQUENCY_THIRD_6.equals(repayType.getCapitalFrequencyThird())) {
                // 半年：6月、12月
                loopCount = 0;
                do {
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    loopCount++;
                } while (!isHalfYear(DateUtils.getMonth(repayDate)) && loopCount < MAX_LOOP);
            } else {
                // 按年结息
                String interestFreqNumStr = repayType.getInterestFrequencyNum();
                if (interestFreqNumStr != null && !interestFreqNumStr.trim().isEmpty()) {
                    int targetNum = Integer.parseInt(interestFreqNumStr.trim());
                    int num = 0;
                    loopCount = 0;
                    do {
                        repayDate = DateUtils.addMonth(repayDate, 1);
                        int month = DateUtils.getMonth(repayDate);
                        if (month == 12) {
                            num += 1;
                        }
                        loopCount++;
                    } while (num < targetNum && loopCount < MAX_LOOP);
                }
            }
        }

        // 结息时：按月/季/半年/年，设置结息日；按日、按周不处理
        if (!isRepayCapital && !BaseDataConstant.FREQUENCY_NEXT_1.equals(frequencyNext)
                && !BaseDataConstant.FREQUENCY_NEXT_2.equals(frequencyNext)) {
            Integer interestDay = repayType.getInterestDay();
            if (interestDay != null && interestDay != 31) {
                repayDate = DateUtils.getDay(repayDate, interestDay);
            } else {
                // 31代表月末最后一天
                repayDate = DateUtils.getLastMonthDate(repayDate);
            }
        }

        // 不能超过到期日
        if (DateUtils.dayDiffs(repayDate, endDate) > 0) {
            repayDate = endDate;
        }
        return repayDate;
    }

    /**
     * 安全字符串转int，null/空/非数字返回默认值
     * @param str 待转换字符串
     * @param defaultVal 默认值
     * @return int
     */
    private int safeParseInt(String str, int defaultVal) {
        if (str == null || str.trim().isEmpty()) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private boolean isQuarterStart(int month) {
        return month == 1 || month == 4 || month == 7 || month == 10;
    }

    private boolean isQuarterMid(int month) {
        return month == 2 || month == 5 || month == 8 || month == 11;
    }

    private boolean isQuarterEnd(int month) {
        return month == 3 || month == 6 || month == 9 || month == 12;
    }

    private boolean isHalfYear(int month) {
        return month == 6 || month == 12;
    }

    /**
     * 根据时间周期计算还款数据
     *
     * @param beginDate              开始日期
     * @param endDate                结束日期
     * @param repayType              还款方式对象
     * @param isRepayCapital         是否计算还款本金
     * @param prediceRepayDateIsEdit 是否还款日可编辑
     * @param paymentMoneyIsEdit     是否还款本金可编辑
     * @param inerestIsEdit          是否还款利息可编辑
     */
    public List<RepaymentScheduleDtlVO> getByCycleTime(Date beginDate, Date endDate, RepayType repayType,
                                                       boolean isRepayCapital, boolean prediceRepayDateIsEdit,
                                                       boolean paymentMoneyIsEdit, boolean inerestIsEdit) {
        List<RepaymentScheduleDtlVO> repaymentScheduleDtlVOList = new ArrayList<>();
        Date repayDate = beginDate;
        // 【保护1】最大期数，防止配置错误生成无限期，贷款一般不会超过360期（30年），可按需调大
        final int MAX_PERIOD = 360;
        int periodCount = 0;

        while (DateUtils.dayDiffs(repayDate, endDate) < 0 && periodCount < MAX_PERIOD) {
            RepaymentScheduleDtlVO repaymentScheduleDtlVO = new RepaymentScheduleDtlVO();
            Date nextRepayDate = getDate(repayDate, endDate, repayType, isRepayCapital);

            // 【保护2】本次计算的还款日没有往后走，终止循环，避免原地死循环
            if (DateUtils.dayDiffs(nextRepayDate, repayDate) <= 0) {
                break;
            }
            repayDate = nextRepayDate;

            repaymentScheduleDtlVO.setPrediceRepayDate(repayDate);
            repaymentScheduleDtlVO.setPrediceRepayDateIsEdit(prediceRepayDateIsEdit);
            repaymentScheduleDtlVO.setPaymentMoneyIsEdit(paymentMoneyIsEdit);
            // 如果计算的是利息日期，则本金不允许录入，且金额为0
            if (!paymentMoneyIsEdit) {
                repaymentScheduleDtlVO.setRepaymentMoney(BigDecimal.ZERO);
            }
            // 如果计算的是本金日期，则利息不允许录入，且金额为0
            repaymentScheduleDtlVO.setInerestIsEdit(inerestIsEdit);
            if (!inerestIsEdit) {
                repaymentScheduleDtlVO.setRepaymentIst(BigDecimal.ZERO);
            }
            repaymentScheduleDtlVOList.add(repaymentScheduleDtlVO);
            periodCount++;
        }
        return repaymentScheduleDtlVOList;
    }

    public CmsProvideInterestVO sumLeftMoney(String sendCode, String status) {
        return cmsProvideInterestDao.sumLeftMoney(sendCode, status);
    }

    public CmsProvideInterestVO queryInterestMaxAndMinDate(String sendCode, String status) {
        return cmsProvideInterestDao.queryInterestMaxAndMinDate(sendCode, status);
    }

    /**
     * 组装还款计划明细数据
     *
     * @param repayPlanVo   前端参数
     * @param data          组装参数
     * @param subtractMoney 剩余本金
     * @return 明细数据
     */
    private RepaymentSchedule setScheduleData(RepayPlanVO repayPlanVo, RepaymentSchedule data, BigDecimal subtractMoney) {
        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentSchedule.setBillCode(data.getBillCode());
        repaymentSchedule.setPrediceRepayDate(DateUtils.parse(repayPlanVo.getRepayDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        repaymentSchedule.setRepaymentMoney(repayPlanVo.getLoanMoney());
        repaymentSchedule.setRepaymentIst(BigDecimal.ZERO);
        repaymentSchedule.setId(UUID.randomUUID().toString().replace("-", ""));
        repaymentSchedule.setCurrentRepayMoney(repayPlanVo.getLoanMoney());
        repaymentSchedule.setSurplusMoney(subtractMoney);
        repaymentSchedule.setValid_Sign(CommonConstant.YES);
        repaymentSchedule.setIsRepay(CommonConstant.NO);
        repaymentSchedule.setRepaymentBeginDate(DateUtils.parse(repayPlanVo.getRepayDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        repaymentSchedule.setCreateBy(data.getCreateBy());
        repaymentSchedule.setCreateName(data.getCreateName());
        repaymentSchedule.setCreateDate(new Date());
        repaymentSchedule.setRelationId(data.getRelationId());
        repaymentSchedule.setInternalSign("B2");
        return repaymentSchedule;
    }

    /**
     * 等额本息计算方法
     *
     * @param resultList    未还款的还款计划明细
     * @param subtractMoney 本金-金额后的金额
     * @param repayType     还款方式
     * @param sendBillCode  发送表单据编号
     * @param loanRate      info申请录入表的放款利率
     */
    private void getEqualPrincipalInterestTest(List<RepaymentSchedule> resultList, BigDecimal subtractMoney, RepayType repayType, String sendBillCode, BigDecimal loanRate) {
        CmsProvideLoanInfo loanInfo = loanInfoService.getLoanInfoByBillCode(sendBillCode);
        BigDecimal rate = BigDecimal.ZERO;
        if (BaseDataConstant.FREQUENCY_NEXT_1.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(new BigDecimal(loanInfo.getRateBase()), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_2.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(52), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_3.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_4.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate.divide(BigDecimal.valueOf(4), 10, RoundingMode.HALF_UP);
        } else if (BaseDataConstant.FREQUENCY_NEXT_5.equals(repayType.getCapitalFrequencyNext())) {
            rate = loanRate;
        }
        //分期等额本息 每期还款金额（本金+利息）
        BigDecimal repayMoentMoney = (subtractMoney.multiply(rate)
                .multiply((BigDecimal.ONE.add(rate))
                        .pow(resultList.size()))).divide((BigDecimal.ONE.add(rate)).pow(resultList.size()).subtract(BigDecimal.ONE), 4, RoundingMode.HALF_UP);
        BigDecimal inst = BigDecimal.ZERO;
        BigDecimal surplusMoney = subtractMoney;
        int n = 1;
        for (RepaymentSchedule item : resultList) {
            inst = (subtractMoney.multiply(rate).subtract(repayMoentMoney)).multiply((BigDecimal.ONE.add(rate)).pow(n - 1)).add(repayMoentMoney);
            if (n == resultList.size()) {
                //利息
                item.setRepaymentIst(repayMoentMoney.subtract(surplusMoney));
                //本金
                item.setRepaymentMoney(surplusMoney);
            } else {
                //利息
                item.setRepaymentIst(inst);
                //本金
                item.setRepaymentMoney((repayMoentMoney.subtract(inst)).setScale(2, RoundingMode.HALF_UP));
            }
            //本金+利息
            item.setCurrentRepayMoney(repayMoentMoney);
            surplusMoney = surplusMoney.subtract(item.getRepaymentMoney());
            surplusMoney = surplusMoney.setScale(2, RoundingMode.HALF_UP);
            item.setSurplusMoney(surplusMoney);
            n++;
        }
        loanInfoService.getEntityDao().evict(loanInfo);
    }

    /**
     * 试算功能计算还款计划的金额
     *
     * @param resultList   未还款的还款计划
     * @param repayType    还款方式
     * @param repayPlanVo  查询参数
     * @param sendBillCode 发放表单据号
     * @param sumTotal     未还款计划明细总金额
     * @param loanRate     info表的放款利率
     * @return 试算完成的还款计划明细
     */
    private List<RepaymentSchedule> provideSyndicatedTrial(List<RepaymentSchedule> resultList, RepayType repayType, RepayPlanVO repayPlanVo, String sendBillCode, BigDecimal sumTotal, BigDecimal loanRate) {
        //提前还款金额
        BigDecimal repayMoney = repayPlanVo.getLoanMoney();
        //总金额-提前还款金额
        BigDecimal subtractMoney = sumTotal.subtract(repayMoney);
        BigDecimal dtlSize = new BigDecimal(resultList.size());
        RepaymentSchedule repaymentSchedule = setScheduleData(repayPlanVo, resultList.get(0), subtractMoney);
        if(BigDecimal.ZERO.compareTo(subtractMoney)<0) {//当剩余金额大于0才需要后续按方式得到计划
            if (repayType.getCapitalAndInterest().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_2)) {
                //还款方式本息关系-等额本息（仅频率一致支持）
                getEqualPrincipalInterestTest(resultList, subtractMoney, repayType, sendBillCode, loanRate);
            } else {
                //剩余本金
                BigDecimal surplusMoney = subtractMoney;
                for (RepaymentSchedule item : resultList) {
                    //利息按本金计算
                    if (repayType.getCapitalAndInterest().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_1)) {
                        //本金支付频率-按时间周期
                        if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_D)) {
                            //非本金支付频率-按时间周期-天-到期一次性支付
                            if (repayType.getCapitalFrequency().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_NUM)) {
                                //还款本金=总金额-提前还款金额。
                                item.setRepaymentMoney(sumTotal.subtract(repayMoney));
                            } else {
                                //本金=总金额-还款金额/条数
                                item.setRepaymentMoney(subtractMoney.divide(dtlSize, 2, RoundingMode.HALF_UP));
                            }
                            //按次数
                        } else if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_C)) {
                            if (repayType.getCapitalFrequencyNext().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_1)) {
                                //按比例：本金=（未还款总金额-提前还款金额）*应还款金额/未还款总金额
                                item.setRepaymentMoney(subtractMoney.multiply(item.getRepaymentMoney()).divide(sumTotal, 2, RoundingMode.HALF_UP));
                            } else if (repayType.getCapitalFrequencyNext().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_2)) {
                                //按次数:本金=总金额-还款金额/条数
                                item.setRepaymentMoney(subtractMoney.divide(dtlSize, 2, RoundingMode.HALF_UP));
                            }
                            //不规律（有计划）
                        } else if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_P)) {
                            //判断提前还款金额是否 > 0
                            if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                                //判断提前还款金额是否 > 本期金额
                                if (repayMoney.compareTo(item.getRepaymentMoney()) == 1) {
                                    repayMoney = repayMoney.subtract(item.getRepaymentMoney());
                                    //直接设置本期金额为 0因为提前还款已还
                                    item.setRepaymentMoney(BigDecimal.ZERO);
                                } else {
                                    //提前还款金额小于本期金额
                                    BigDecimal repaymentMoney = item.getRepaymentMoney();
                                    item.setRepaymentMoney(repaymentMoney.subtract(repayMoney));
                                    repayMoney = repayMoney.subtract(repaymentMoney);
                                }
                            }
                        }
                    } else if (repayType.getCapitalAndInterest().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_3)) {
                        //还款方式本息关系-利息与本金无关系（分别自定义）
                        //本金支付频率-按时间周期
                        if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_D)) {
                            //本金=总金额-还款金额/条数
                            item.setRepaymentMoney(subtractMoney.divide(dtlSize, 2, RoundingMode.HALF_UP));
                            //按次数
                        } else if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_C)) {
                            if (repayType.getCapitalFrequencyNext().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_1)) {
                                //按比例：本金=（未还款总金额-提前还款金额）*应还款金额/未还款总金额
                                item.setRepaymentMoney(subtractMoney.multiply(item.getRepaymentMoney()).divide(sumTotal, 2, RoundingMode.HALF_UP));
                            } else if (repayType.getCapitalFrequencyNext().equals(CmsConstant.REPAY_TYPE_CAPITAL_AND_INTEREST_2)) {
                                //按次数:本金=总金额-还款金额/条数
                                item.setRepaymentMoney(subtractMoney.divide(dtlSize, 2, RoundingMode.HALF_UP));
                            }
                            //不规律（有计划）
                        } else if (repayType.getCapitalFrequencyType().equals(CmsConstant.REPAY_TYPE_CAPITAL_FREQUENCY_TYPE_P)) {
                            //判断提前还款金额是否 > 0
                            if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                                //判断提前还款金额是否 > 本期金额
                                if (repayMoney.compareTo(item.getRepaymentMoney()) == 1) {
                                    repayMoney = repayMoney.subtract(item.getRepaymentMoney());
                                    //直接设置本期金额为 0因为提前还款已还
                                    item.setRepaymentMoney(BigDecimal.ZERO);
                                } else {
                                    //提前还款金额小于本期金额
                                    BigDecimal repaymentMoney = item.getRepaymentMoney();
                                    item.setRepaymentMoney(repaymentMoney.subtract(repayMoney));
                                    repayMoney = repayMoney.subtract(repaymentMoney);
                                }
                            }
                        }
                    }
                    //重新计算剩余本金
                    surplusMoney = surplusMoney.subtract(item.getRepaymentMoney());
                    item.setSurplusMoney(surplusMoney);
                }
            }
        } else {//提前还款直接还完
            resultList.clear();
        }
        resultList.add(repaymentSchedule);
        return resultList;
    }

    private void countMoneyCorrect(List<CmsProvideInterest> returnList, List<RepaymentSchedule> resultList, BigDecimal sumTotal) {
        BigDecimal resultMoney = new BigDecimal("0");
        //计算差值，解决四舍五入计算可能有多0.01或者少的问题
        for (RepaymentSchedule item : resultList) {
            //还款计划明细
            resultMoney = resultMoney.add(item.getRepaymentMoney());
        }
        BigDecimal returnMoney = new BigDecimal("0");
        for (CmsProvideInterest item : returnList) {
            //预计利息明细
            returnMoney = resultMoney.add(item.getRepayMoney());
        }
        RepaymentSchedule repaymentSchedule = resultList.get(resultList.size() - 1);
        if (sumTotal.compareTo(resultMoney) != 0) {
            //17250 < 17250.01
            repaymentSchedule.setRepaymentMoney(repaymentSchedule.getRepaymentMoney().add(repaymentSchedule.getSurplusMoney()));
            repaymentSchedule.setCurrentRepayMoney(repaymentSchedule.getCurrentRepayMoney().add(repaymentSchedule.getSurplusMoney()));
        }
        repaymentSchedule.setSurplusMoney(BigDecimal.ZERO);

        CmsProvideInterest cmsProvideInterest = returnList.get(returnList.size() - 1);
        if (sumTotal.compareTo(returnMoney) != 0) {
            //17250 < 17250.01
            cmsProvideInterest.setRepayMoney(cmsProvideInterest.getRepayMoney().add(cmsProvideInterest.getLeftMoney()));
        }
        cmsProvideInterest.setLeftMoney(BigDecimal.ZERO);
    }

    public List<CmsProvideInterestVO> queryInterestTest(RepayPlanVO repayPlanVo, HttpServletRequest request, Model model) {
        List<CmsProvideInterestTemp> cmsProvideInterestTemp = cmsProvideInterestTempDao.getListByBillCodeAndTimeMoney(repayPlanVo.getSendBillCode(),repayPlanVo.getRepayDate(),repayPlanVo.getLoanMoney());
        List<CmsProvideInterestVO> returnList = new ArrayList<>();
        if(ListUtils.isNullOrEmpty(cmsProvideInterestTemp)){
            ProvideLoanSend send = loanSendService.get(repayPlanVo.getSendId());
            //获取未还款的还款计划明细
            List<RepaymentScheduleVO> dtlList = scheduleService.getRepaymentScheduleDtlByBillCode(repayPlanVo.getSendId(), CommonConstant.NO);
            //获取还款方式
            RepayType repayType = repayTypeService.get(repayPlanVo.getRepayType());
            if (EmptyUtils.isEmpty(dtlList) || EmptyUtils.isEmpty(repayType) || dtlList.size() <= 0) {
                return new ArrayList<>();
            }
            //计算未还款明细总金额
            BigDecimal sumTotal = new BigDecimal("0");
            RepaymentSchedule bean;
            List<RepaymentSchedule> resultList = new ArrayList<>();
            for (RepaymentScheduleVO entity : dtlList) {
                if (entity.getIsRepay().equals(CommonConstant.NO)) {
                    sumTotal = sumTotal.add(entity.getRepaymentMoney());
                    bean = new RepaymentSchedule();
                    BeanUtils.copyNotNullProperties(entity, bean);
                    resultList.add(bean);
                }
            }
            cmsProvideInterestDao.flush();
            //获取必要的组装参数
            InterestWayHandler interestAdjustDetail = this.getInterestAdjustDetail(send);
            //重新计算还款计划明细
            List<RepaymentSchedule> repaymentSchedules = provideSyndicatedTrial(resultList, repayType, repayPlanVo, send.getLoanCode(), sumTotal, dtlList.get(0).getLoanRate());
            //重新设置还款计划明细
            Collections.sort(repaymentSchedules, new Comparator<RepaymentSchedule>() {
                @Override
                public int compare(RepaymentSchedule item, RepaymentSchedule entity) {
                    return item.getPrediceRepayDate().compareTo(entity.getPrediceRepayDate());
                }
            });
            interestAdjustDetail.setScheduleList(repaymentSchedules);
            //修改计划主表信息的本金
            interestAdjustDetail.getCmsRepaymentSchedule().setLoanMoney(sumTotal);
            interestAdjustDetail.getSend().setMoney(sumTotal);
            interestAdjustDetail.getAccrualType().setFrequencyType(CommonConstant.NO);
            //获取已还款预计利息明细
            List<CmsProvideInterest> repaidList = cmsProvideInterestDao.getProvideInterestBySendCode(repayPlanVo.getSendBillCode());
            if (EmptyUtils.isNotEmpty(repaidList)) {
                BigDecimal sumTotalInterest = BigDecimal.ZERO;
                BigDecimal sumPayInterest = BigDecimal.ZERO;
                for (CmsProvideInterest item : repaidList) {
                    sumTotalInterest = sumTotalInterest.add(item.getTotalInterest());
                    sumPayInterest = sumPayInterest.add(item.getPayInterest());
                }
                interestAdjustDetail.setUnPayInterest(sumTotalInterest.subtract(sumPayInterest));
            }
            interestAdjustDetail.setRepaidList(repaidList); //add by liucl 先记住已还的数据
            //生成利息预计明细
            List<CmsProvideInterest> instList = interestAdjustDetail.createInterestDetail();
            //处理四舍五入问题
            countMoneyCorrect(instList, resultList, sumTotal);
            instList.get(0).setStatus(Integer.valueOf(CommonConstant.YES));
            instList.get(0).setId(UUID.randomUUID().toString().replace("-", ""));
            repaymentSchedules.get(0).setIsRepay(CommonConstant.YES);
            model.addAttribute("repayDate", DateUtils.format(instList.get(0).getRepayDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            model.addAttribute("repayMoney", instList.get(0).getRepayMoney());
            model.addAttribute("payInterest", instList.get(0).getRealInterest());
            model.addAttribute("provideInterestId", instList.get(0).getId());
            if (EmptyUtils.isNotEmpty(repaidList)) {
                instList.addAll(repaidList);
            }
            CmsProvideInterestVO interestVo;
            for (CmsProvideInterest item : instList) {
                interestVo = new CmsProvideInterestVO();
                BeanUtils.copyNotNullProperties(item, interestVo);
                returnList.add(interestVo);
            }
            Collections.sort(returnList, new Comparator<CmsProvideInterestVO>() {
                @Override
                public int compare(CmsProvideInterestVO item, CmsProvideInterestVO entity) {
                    return item.getRepayDate().compareTo(entity.getRepayDate());
                }
            });
            loanSendService.getEntityDao().evict(send);
            request.getSession().setAttribute("repaymentScheduleDtlList", resultList);
        }else{
            List<String> ids = new ArrayList<>();
            String interestId=null;
            for (CmsProvideInterestTemp provideInterestTemp : cmsProvideInterestTemp) {
                CmsProvideInterestVO cmsProvideInterestVO = new CmsProvideInterestVO();
                BeanUtils.copyNotNullProperties(provideInterestTemp,cmsProvideInterestVO);
                ids.add(cmsProvideInterestVO.getRepaymentScheduleId());
                returnList.add(cmsProvideInterestVO);
                if(cmsProvideInterestVO.getRepayDate().compareTo(DateUtils.parse(repayPlanVo.getRepayDate(),"yyyy-MM-dd"))==0){
                    interestId = cmsProvideInterestVO.getId();
                }
            }
            List<RepaymentSchedule> resultList = new ArrayList<>();
            List<ScheduleDtlTemp> scheduleDtlTemps = dtlTempService.getListByIds(ids);
            for (ScheduleDtlTemp scheduleDtlTemp : scheduleDtlTemps) {
                RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
                BeanUtils.copyNotNullProperties(scheduleDtlTemp, repaymentSchedule);
                resultList.add(repaymentSchedule);
            }
            model.addAttribute("repayDate", repayPlanVo.getRepayDate());
            model.addAttribute("repayMoney", repayPlanVo.getLoanMoney());
            model.addAttribute("provideInterestId", interestId);
            request.getSession().setAttribute("repaymentScheduleDtlList", resultList);
        }
        return returnList;
    }
    public List<CmsProvideInterestVO> queryInterestTest(RepayPlanVO repayPlanVo, HttpServletRequest request, Map<String,Object> model) {
        List<CmsProvideInterestTemp> cmsProvideInterestTemp = cmsProvideInterestTempDao.getListByBillCodeAndTimeMoney(repayPlanVo.getSendBillCode(),repayPlanVo.getRepayDate(),repayPlanVo.getLoanMoney());
        List<CmsProvideInterestVO> returnList = new ArrayList<>();
        if(ListUtils.isNullOrEmpty(cmsProvideInterestTemp)){
            ProvideLoanSend send = loanSendService.get(repayPlanVo.getSendId());
            //获取未还款的还款计划明细
            List<RepaymentScheduleVO> dtlList = scheduleService.getRepaymentScheduleDtlByBillCode(repayPlanVo.getSendId(), CommonConstant.NO);
            //获取还款方式
            RepayType repayType = repayTypeService.get(repayPlanVo.getRepayType());
            if (EmptyUtils.isEmpty(dtlList) || EmptyUtils.isEmpty(repayType) || dtlList.size() <= 0) {
                return new ArrayList<>();
            }
            //计算未还款明细总金额
            BigDecimal sumTotal = new BigDecimal("0");
            RepaymentSchedule bean;
            List<RepaymentSchedule> resultList = new ArrayList<>();
            for (RepaymentScheduleVO entity : dtlList) {
                if (entity.getIsRepay().equals(CommonConstant.NO)) {
                    sumTotal = sumTotal.add(entity.getRepaymentMoney());
                    bean = new RepaymentSchedule();
                    BeanUtils.copyNotNullProperties(entity, bean);
                    resultList.add(bean);
                }
            }
            cmsProvideInterestDao.flush();
            //获取必要的组装参数
            InterestWayHandler interestAdjustDetail = this.getInterestAdjustDetail(send);
            //重新计算还款计划明细
            List<RepaymentSchedule> repaymentSchedules = provideSyndicatedTrial(resultList, repayType, repayPlanVo, send.getLoanCode(), sumTotal, dtlList.get(0).getLoanRate());
            //重新设置还款计划明细
            Collections.sort(repaymentSchedules, new Comparator<RepaymentSchedule>() {
                @Override
                public int compare(RepaymentSchedule item, RepaymentSchedule entity) {
                    return item.getPrediceRepayDate().compareTo(entity.getPrediceRepayDate());
                }
            });
            interestAdjustDetail.setScheduleList(repaymentSchedules);
            //修改计划主表信息的本金
            interestAdjustDetail.getCmsRepaymentSchedule().setLoanMoney(sumTotal);
            interestAdjustDetail.getSend().setMoney(sumTotal);
            interestAdjustDetail.getAccrualType().setFrequencyType(CommonConstant.NO);
            //获取已还款预计利息明细
            List<CmsProvideInterest> repaidList = cmsProvideInterestDao.getProvideInterestBySendCode(repayPlanVo.getSendBillCode());
            if (EmptyUtils.isNotEmpty(repaidList)) {
                BigDecimal sumTotalInterest = BigDecimal.ZERO;
                BigDecimal sumPayInterest = BigDecimal.ZERO;
                for (CmsProvideInterest item : repaidList) {
                    sumTotalInterest = sumTotalInterest.add(item.getTotalInterest());
                    sumPayInterest = sumPayInterest.add(item.getPayInterest());
                }
                interestAdjustDetail.setUnPayInterest(sumTotalInterest.subtract(sumPayInterest));
            }
            interestAdjustDetail.setRepaidList(repaidList); //add by liucl 先记住已还的数据
            //生成利息预计明细
            List<CmsProvideInterest> instList = interestAdjustDetail.createInterestDetail();
            //处理四舍五入问题
            countMoneyCorrect(instList, resultList, sumTotal);
            instList.get(0).setStatus(Integer.valueOf(CommonConstant.YES));
            instList.get(0).setId(UUID.randomUUID().toString().replace("-", ""));
            repaymentSchedules.get(0).setIsRepay(CommonConstant.YES);
            model.put("repayDate", DateUtils.format(instList.get(0).getRepayDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            model.put("repayMoney", instList.get(0).getRepayMoney());
            model.put("payInterest", instList.get(0).getRealInterest());
            model.put("provideInterestId", instList.get(0).getId());
            if (EmptyUtils.isNotEmpty(repaidList)) {
                instList.addAll(repaidList);
            }
            CmsProvideInterestVO interestVo;
            for (CmsProvideInterest item : instList) {
                interestVo = new CmsProvideInterestVO();
                BeanUtils.copyNotNullProperties(item, interestVo);
                returnList.add(interestVo);
            }
            Collections.sort(returnList, new Comparator<CmsProvideInterestVO>() {
                @Override
                public int compare(CmsProvideInterestVO item, CmsProvideInterestVO entity) {
                    return item.getRepayDate().compareTo(entity.getRepayDate());
                }
            });
            loanSendService.getEntityDao().evict(send);
            request.getSession().setAttribute("repaymentScheduleDtlList", resultList);
        }else{
            List<String> ids = new ArrayList<>();
            String interestId=null;
            for (CmsProvideInterestTemp provideInterestTemp : cmsProvideInterestTemp) {
                CmsProvideInterestVO cmsProvideInterestVO = new CmsProvideInterestVO();
                BeanUtils.copyNotNullProperties(provideInterestTemp,cmsProvideInterestVO);
                ids.add(cmsProvideInterestVO.getRepaymentScheduleId());
                returnList.add(cmsProvideInterestVO);
                if(cmsProvideInterestVO.getRepayDate().compareTo(DateUtils.parse(repayPlanVo.getRepayDate(),"yyyy-MM-dd"))==0){
                    interestId = cmsProvideInterestVO.getId();
                }
            }
            List<RepaymentSchedule> resultList = new ArrayList<>();
            List<ScheduleDtlTemp> scheduleDtlTemps = dtlTempService.getListByIds(ids);
            for (ScheduleDtlTemp scheduleDtlTemp : scheduleDtlTemps) {
                RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
                BeanUtils.copyNotNullProperties(scheduleDtlTemp, repaymentSchedule);
                resultList.add(repaymentSchedule);
            }
            model.put("repayDate", repayPlanVo.getRepayDate());
            model.put("repayMoney", repayPlanVo.getLoanMoney());
            model.put("provideInterestId", interestId);
            request.getSession().setAttribute("repaymentScheduleDtlList", resultList);
        }
        return returnList;
    }

    /**
     * 新增还款申请试算数据
     *
     * @param repayPlanVo 参数
     * @param list        还款计划试算明细数据
     */
    public void saveTestList(RepayPlanVO repayPlanVo, List<RepaymentSchedule> list) {
        if (EmptyUtils.isNotEmpty(repayPlanVo.getInterestVoList()) && EmptyUtils.isNotEmpty(list)) {
            List<ScheduleDtlTemp> scheduleDtlTemp = new ArrayList<>();
            ScheduleDtlTemp bean;
            for (RepaymentSchedule item : list) {
                bean = new ScheduleDtlTemp();
                BeanUtils.copyNotNullProperties(item, bean);
                scheduleDtlTemp.add(bean);
            }
            for (CmsProvideInterestTemp item : repayPlanVo.getInterestVoList()) {
                if (EmptyUtils.isEmpty(item.getRepayMoney())) {
                    item.setRepayMoney(BigDecimal.ZERO);
                }
                if (EmptyUtils.isEmpty(item.getPayInterest())) {
                    item.setPayInterest(BigDecimal.ZERO);
                }
                if (EmptyUtils.isEmpty(item.getTotalInterest())) {
                    item.setTotalInterest(BigDecimal.ZERO);
                }
                if (EmptyUtils.isEmpty(item.getRealInterest())) {
                    item.setRealInterest(BigDecimal.ZERO);
                }
                item.setChooseRepayDate(DateUtils.parseDate(repayPlanVo.getRepayDate()));
                item.setChooseRepayMoney(repayPlanVo.getRepayMoney());
                item.setInterestTime(new Date());
            }
            dtlTempService.deleteBySendBillCode(scheduleDtlTemp.get(0).getBillCode());
            tempService.deleteBySendBillCode(repayPlanVo.getInterestVoList().get(0).getBillCode());
            dtlTempService.batchSave(scheduleDtlTemp);
            tempService.batchSave(repayPlanVo.getInterestVoList());
        }
    }

    public void deleteBySendICode(String billCode) {
        cmsProvideInterestDao.deleteBySendICode(billCode);
    }
}
