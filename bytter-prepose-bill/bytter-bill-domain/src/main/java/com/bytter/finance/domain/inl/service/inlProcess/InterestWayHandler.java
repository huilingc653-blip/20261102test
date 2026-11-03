package com.bytter.finance.domain.inl.service.inlProcess;

import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.AccrualType;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.service.RateFloatRuleService;
import com.bytter.finance.domain.provide.service.process.ParagraphBean;
import com.bytter.finance.domain.provide.vo.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 借款预计利息明细生成
 *
 * @author lvwl
 */
public class InterestWayHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private CmsInlSend send;
    private CmsInlInfo info;
    /**
     * 还款方式
     */
    private RepayType repayType;

    //	还款计划主表数据
    private CmsRepaymentSchedule cmsRepaymentSchedule;
    private List<RepaymentSchedule> scheduleList;
    private List<RateAdjustBean> rateAdjustList;
    private List<InstRateBean> baseRateBeanList;
    private List<IstFloatBean> floatTypeList;
    private List<InterestWayBean> istWayList;
    private List<CmsInlDeferredVO> deferredList;//展期分段

    private List<Date> accrualList = new ArrayList<Date>();

    private int isFirst = 0;// 是否第一笔还款
    private boolean islast = false;// 是否最后一笔
    private String sendRepayType = null;
    private Date repayStartDate = null;// 更新计划前最后一次还款的日期
    private BigDecimal unPayInterest = BigDecimal.ZERO;//更新计划前累计未还利息
    private RateFloatRuleService rateFloatRuleService;
    // 分段类
    /**
     * 当存在多次修改计息方式时则会出现后面计息方式失效问题
     * 因为后面计息方式生效日期永远上一计息分段执行日期
     * 此时需要把后面的分段一起添加到paragraphList继续循环，
     * 由于要动态改变paragraphList内容所有使用CopyOnWriteArrayList
     */
    private CopyOnWriteArrayList<ParagraphBean> paragraphList;

    public InterestWayHandler() {
    }

    public InterestWayHandler(CmsInlSend send, CmsInlInfo info) {
        this.send = send;
        this.info = info;
    }

    /**
     * 创建利息明细
     */
    public List<CmsProvideInterest> createInterestDetail() {
        List<CmsProvideInterest> list = new ArrayList<CmsProvideInterest>();
        if (!EmptyUtils.isEmpty(repayType)) {
            // 利息与本金分别自定义
            if ((BaseDataConstant.CAPITAL_AND_INTEREST_3.equals(repayType.getCapitalAndInterest())
                    || BaseDataConstant.CAPITAL_AND_INTEREST_2.equals(repayType.getCapitalAndInterest()))) {
                if (!BaseDataConstant.FREQUENCY_TYPE_N.equals(repayType.getCapitalFrequencyType())) {
                    list.addAll(createInstByCustomRepayType(this.send, this.scheduleList));
                }
            } else {
                if (!BaseDataConstant.FREQUENCY_TYPE_N.equals(repayType.getCapitalFrequencyType())) {
                    // 按剩余本金或等额本息计算
                    //还款方式还利息，不需要进行计息预提分别分段
                    //1、根据参数生成分段数据   计息
                    list.addAll(this.createParagraph(InterestConstant.INTEREST_SIGN_YES));
                }
            }
        }
//		//2、根据参数生成分段数据   预提
//		list.addAll(this.createParagraph(InterestConstant.INTEREST_SIGN_NO));
        return list;
    }

    /**
     * 利息本金分别自定义
     *
     * @param send
     */
    public List<CmsProvideInterest> createInstByCustomRepayType(CmsInlSend send, List<RepaymentSchedule> repaymentSchedules) {
        List<CmsProvideInterest> instList = new ArrayList<>();
        Date interestStartDate = send.getSendDate();
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            if (CommonConstant.YES.equals(repaymentSchedule.getIsRepay())) {
                interestStartDate = repaymentSchedule.getPrediceRepayDate();
                continue;
            }
            CmsProvideInterest cmsProvideInterest = new CmsProvideInterest();
            cmsProvideInterest.setRepaymentScheduleId(repaymentSchedule.getId());//还款计划id
            cmsProvideInterest.setInterestTime(new Date());//计息日期
            cmsProvideInterest.setContractCode(send.getSendCode());//合同号
            cmsProvideInterest.setBillCode(send.getBillNo());//主业务单据号
            cmsProvideInterest.setCorpId(send.getBorrowInCorp());//单位id
            cmsProvideInterest.setCurId(send.getCurId());//币种id
            cmsProvideInterest.setInterestType(send.getInterestWay());//计息方式
            cmsProvideInterest.setRepayDate(repaymentSchedule.getPrediceRepayDate());//还款日期
            cmsProvideInterest.setInterestStartDate(interestStartDate);//计息开始日期
            cmsProvideInterest.setBlnInstDate(DateUtils.addDay(repaymentSchedule.getPrediceRepayDate(), -1));//计息结束日期
            cmsProvideInterest.setDays(DateUtils.getOffsetDays(interestStartDate, repaymentSchedule.getPrediceRepayDate()));//计息天数
            cmsProvideInterest.setFloatPoint(BigDecimal.ZERO);//浮动利率
            cmsProvideInterest.setInterestRate(send.getRate());//计息利率
            cmsProvideInterest.setRateFax(BigDecimal.ZERO);//税率
            cmsProvideInterest.setLoanMoney(repaymentSchedule.getSurplusMoney().add(repaymentSchedule.getRepaymentMoney()));//剩余本金
            cmsProvideInterest.setRepayMoney(repaymentSchedule.getRepaymentMoney());//还款本金
            cmsProvideInterest.setLeftMoney(repaymentSchedule.getSurplusMoney());//剩余本金
            cmsProvideInterest.setInterest(repaymentSchedule.getRepaymentIst());//归还利息
            cmsProvideInterest.setPayInterest(repaymentSchedule.getRepaymentIst());//支付利息
            cmsProvideInterest.setTotalInterest(repaymentSchedule.getRepaymentIst());//全部利息
            cmsProvideInterest.setUnPayInterest(BigDecimal.ZERO);//未支付利息
            cmsProvideInterest.setRealInterest(repaymentSchedule.getRepaymentIst());//实际支付利息
            cmsProvideInterest.setInterestFax(BigDecimal.ZERO);//利息税率
            cmsProvideInterest.setInterestSign(InterestConstant.INTEREST_SIGN_YES);//计息标志
            cmsProvideInterest.setStatus(0);//状态
            cmsProvideInterest.setSignRepayDate(repaymentSchedule.getPrediceRepayDate());//分段时标识属于那一段时间
            instList.add(cmsProvideInterest);
            interestStartDate = repaymentSchedule.getPrediceRepayDate();
        }
        return instList;
    }

    /**
     * 根据调整参数生成分段数据<br>
     * 1.根据还款计划分段 <br>
     * 2.根据计息方式调整分段<br>
     * 3.根据计息日期与预提日进行分段 ** 设置还款日期标记<br>
     * 4.根据其他调整 对标记了还款日期的分段进行 调整分段
     */
    private List<CmsProvideInterest> createParagraph(String interestSign) {
        /**初始化 集合*/
        paragraphList = new CopyOnWriteArrayList<ParagraphBean>();

        /**1.还款计划*/
        this.handleRepaymentSchedule(interestSign);

        /**2.计息方式*/
        //        无计息方式调整
        this.handleInterestWay();

        /**3.结息、预提分段*/
        this.handleExpiryDaily();

        /**4.利率调整*/
        this.handleRateAdjust();

        /**5.浮动方式调整*/
        this.handleFloatType();

        /**6.基准利率调整*/
        this.handleBaseRateV2();

        //浮动方式修改自定义处理
        this.makeFloatWayCustom();

        /**7.展期分段*/
        this.handleDeferredOverdue();

        this.mergeProvideInterest();//既不还息也不还本的数据合并

        return this.createCmsProvideInterest();
    }

    /**
     * 还款计划分段
     */
    private void handleRepaymentSchedule(String interestSign) {
        if (!EmptyUtils.isEmptyList(scheduleList)) {//还款计划明细表不等于空
            Date repayStartDate = getRepayStartDate();//获取最大还款开始日
            BigDecimal corpus = send.getSendMoney();//本金
            for (RepaymentSchedule rs : scheduleList) {
                if ("0".equals(rs.getIsRepay())) {//未还的还款计划
                    ParagraphBean pb = new ParagraphBean();
                    pb.setMoney(corpus);//设置本金
                    pb.setRepayMoney(rs.getRepaymentMoney());//设置还款金额

                    pb.setPlRate(send.getRate());//设置借款利率
                    pb.setDeferredRate(send.getDeferredRate());//展期利率
                    //pb.setDeferredRate();//预期利率  暂时没有需求预期分段
                    if (!"01".equals(send.getInterestWay())) {//浮动利率
                        pb.setRate(send.getBenchmarkRate());//设置基准利率
                    }
                    pb.setIsFloatWay(false);//默认不浮动
                    /**默认计息信息*/
                    if (StringUtils.isEmpty(send.getRepayType())) {
                        pb.setInterestWay(send.getInterestWay());//设置计息方式
                        pb.setExpiryDate(send.getExpiryDate());//设置计息日
                        pb.setAccruedDailyDate(send.getAccruedDailyDate());//设置预提日
                        pb.setIsStanderd("02");//01 自然，02标准
//						设置为旧版计提计息 1=旧版计提计息，2新版计提计息
                        pb.setOldOrNew("1");
                    } else {
                        pb.setRepayType(this.getRepayType());
                        if (this.repayType.getCapitalAndInterest().equals(BaseDataConstant.CAPITAL_AND_INTEREST_1)) {
                            if(!EmptyUtils.isEmpty(this.repayType.getInterestDay()) && this.repayType.getInterestDay().compareTo(35)==0){
                                //计息日同发放日
                                int day= DateUtils.getDay(send.getSendDate());
                                pb.setExpiryDate(day);
                            } else {
                                pb.setExpiryDate(this.repayType.getInterestDay());//设置计息日
                            }
                        } else if (this.repayType.getCapitalAndInterest().equals(BaseDataConstant.CAPITAL_AND_INTEREST_2)) {
//							按周
                            if (!this.repayType.getCapitalFrequencyNext().equals(BaseDataConstant.FREQUENCY_NEXT_1)) {
                                if (this.repayType.getCapitalFrequencyNext().equals(BaseDataConstant.FREQUENCY_NEXT_2)) {
                                    pb.setExpiryDate(DateUtils.getDayOfWeek(null == this.cmsRepaymentSchedule.getRepayBeginDate() ? send.getSendDate() : this.cmsRepaymentSchedule.getRepayBeginDate()));
                                } else {
                                    pb.setExpiryDate(DateUtils.getDay(null == this.cmsRepaymentSchedule.getRepayBeginDate() ? send.getSendDate() : this.cmsRepaymentSchedule.getRepayBeginDate()));
//									pb.setExpiryDate(DateUtils.getDayOfMonth(null == this.cmsRepaymentSchedule.getRepayBeginDate() ? send.getSendDate() : this.cmsRepaymentSchedule.getRepayBeginDate()));
                                }
                            }
                        }
//						设置为新版计提计息 1=旧版计提计息，2新版计提计息
                        pb.setOldOrNew("2");
                    }
                    pb.setInterestSign(interestSign);//设置计息标志
                    pb.setStartDate(repayStartDate);
                    pb.setEndDate(rs.getPrediceRepayDate());//设置结束日期
                    pb.setSignRepayDate(rs.getPrediceRepayDate());//还款日期标记
                    pb.setRepaymentScheduleId(rs.getId());
                    pb.setIsRepayCorpus(true);//归还本金标记
                    pb.setIsRepayInt(false);//归还利息
                    paragraphList.add(pb);
                }

                Date prediceRepayDate = rs.getPrediceRepayDate();//下次还款开始日
                if (DateUtils.dayDiffs(repayStartDate, prediceRepayDate) < 0) {
                    repayStartDate = prediceRepayDate;
                }
                corpus = corpus.subtract(rs.getRepaymentMoney());
            }
        }
    }

    /**
     * 计息方式分段
     */
    private void handleInterestWay() {
        if (!EmptyUtils.isEmptyList(istWayList)) {
//			List<ParagraphBean> newParageraphList = new ArrayList<ParagraphBean>();
            Iterator<InterestWayBean> iterator = istWayList.iterator();
            while (iterator.hasNext()) {
                InterestWayBean iwb = iterator.next();
                for (ParagraphBean pb : paragraphList) {
                    if (compareDate(iwb.getEffectDate(), pb.getStartDate()) <= 0) {//计息调整生效日期 <= 开始日期
                        pb.setInterestWay(iwb.getInterestWay());//设置计息方式
                        pb.setExpiryDate(iwb.getInterestDay());//设置计息日
                        pb.setAccruedDailyDate(iwb.getPredrawDay());//设置预提日
                        pb.setIsStanderd("02");//01 自然，02标准
                    } else if (compareDate(iwb.getEffectDate(), pb.getEndDate()) < 0) {//开始日期 < 计息调整生效日期  < 结束日期
                        ParagraphBean newPb = new ParagraphBean();
                        BeanUtils.copyNotNullProperties(pb, newPb);

                        pb.setEndDate(iwb.getEffectDate());//设置计息调整分段 上段的结束日期为计息调整生效日期
                        pb.setIsRepayCorpus(false);//归还本金标记
                        /**计息方式分段，但是还款日期不分段  所以注释掉*/
                        pb.setSignRepayDate(iwb.getEffectDate());//设置还款日期标记

                        newPb.setStartDate(iwb.getEffectDate());//设置计息调整分段 下段的开始日期为计息调整生效日期
                        newPb.setInterestWay(iwb.getInterestWay());//设置计息方式
                        newPb.setExpiryDate(iwb.getInterestDay());//设置计息日
                        newPb.setAccruedDailyDate(iwb.getPredrawDay());//设置预提日
                        newPb.setIsStanderd("02");//01 自然，02标准
                        paragraphList.add(newPb);
//						newParageraphList.add(newPb);
                    } else {

                    }
                }
            }
//			paragraphList.addAll(newParageraphList);//将新加分段添加到全局
        }
    }

    /**
     * 计算结息预提分段（采用新的计提计息方式）
     *
     * @param pb
     * @return
     */
    private List<ParagraphBean> handleExpiryDailyExecute(ParagraphBean pb) {
        List<ParagraphBean> list = new ArrayList<ParagraphBean>();
        Date date = send.getSendDate();//结息、预提开始日期
        Date endDate = send.getEndDate();
        if(CommonConstant.YES.equals(send.getIsDeferred())){
            endDate=send.getDeferredEndDate();
        }
        RepayType repayType = pb.getRepayType();
        AccrualType accrualType = pb.getAccrualType();
        String isThis=repayType.getInterestFrequencyThis();//是否本月本季计息
        do {
            if (InterestConstant.INTEREST_SIGN_YES.equals(pb.getInterestSign())) {
//				计算结息日
                //等额本息，预计利息明细与还款计划一致
//                {
//                    if (date.compareTo(send.getSendDate()) == 0) {
//                        date = (null == this.cmsRepaymentSchedule.getRepayBeginDate() ? send.getSendDate() : this.cmsRepaymentSchedule.getRepayBeginDate());
//                    } else {
////					等额本息
//                        date = getDate(date, endDate, repayType.getCapitalFrequencyNext(), repayType.getCapitalFrequencyNum(), repayType.getCapitalFrequency(), pb.getExpiryDate(), true);
//                    }
                if (BaseDataConstant.CAPITAL_AND_INTEREST_1.equals(repayType.getCapitalAndInterest())) {
//					等额本金
                    date = getDate(date, endDate, repayType.getInterestFrequencyNext(), repayType.getInterestFrequencyThird(),repayType.getInterestFrequencyNum(), repayType.getInterestFrequency(), pb.getExpiryDate(), false,isThis);
                    isThis= CmsConstant.IS_FALSE;
                }
            } else {
//				计算预提日
                date = getDate(date, endDate, accrualType.getFrequencyType(), null,accrualType.getFrequency(), accrualType.getFrequencyDailyDate(), pb.getExpiryDate(), false,isThis);
            }
            //开始日期 < 结息、预提日期 < 结束日期
            if (compareDate(pb.getStartDate(), date) < 0 && compareDate(date, pb.getEndDate()) < 0) {
                //结息、预提分段
                ParagraphBean newPb = new ParagraphBean();
                BeanUtils.copyNotNullProperties(pb, newPb);
                pb.setEndDate(date);//设置结息、预提分段 上段的结束日期为结息、预提日期
                pb.setIsRepayCorpus(false);//归还本金标记
                pb.setIsRepayInt(true);//归还利息
                pb.setSignRepayDate(date);//设置还款日期标记
                newPb.setStartDate(date);//设置结息、预提 下段的开始日期为结息、预提日期
                list.add(newPb);
                pb = newPb;//newPb将作为下次比较日期的主体
            } else if (compareDate(date, pb.getEndDate()) == 0) {
                pb.setIsRepayInt(true);//归还利息
            }
        } while (compareDate(date, pb.getEndDate()) < 0);
        return list;
    }

    /**
     * 结息、预提分段
     */
    private void handleExpiryDaily() {
        List<ParagraphBean> list = new ArrayList<ParagraphBean>();
        for (ParagraphBean pb : paragraphList) {
            /**
             * 1=旧版计提计息，2新版计提计息
             */
            if ("1".equals(pb.getOldOrNew())) {
                boolean isStanderd = "02".equals(pb.getIsStanderd()) ? true : false;//标准季(true) 自然季(false)

                String interestWay = pb.getInterestWay();//计息方式
                if (InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(interestWay)) {//季末结息预提
                    list.addAll(this.handleExpiryDailyExecute(pb, isStanderd, 3));//分段
                } else if (InterestConstant.PREDRAW_SIGN_SEASON_NO_ANTICIPATE.equals(interestWay)) {//季末结息不预提
                    if (InterestConstant.INTEREST_SIGN_YES.equals(pb.getInterestSign())) {//结息
                        list.addAll(this.handleExpiryDailyExecute(pb, isStanderd, 3));//分段
                    }
                } else if (InterestConstant.PREDRAW_SIGN_MONTH_INTEREST.equals(interestWay)) {//每月结息
                    if (InterestConstant.INTEREST_SIGN_YES.equals(pb.getInterestSign())) {//结息
                        list.addAll(this.handleExpiryDailyExecute(pb, isStanderd, 1));//分段
                    }
                } else if (InterestConstant.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.equals(interestWay)) {//季末不结息预提
                    if (InterestConstant.INTEREST_SIGN_NO.equals(pb.getInterestSign())) {//预提
                        list.addAll(this.handleExpiryDailyExecute(pb, isStanderd, 3));//分段
                    }
                } else if (InterestConstant.PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE.equals(interestWay)) {//季末不结息不预提

                }
            } else {
                if (CommonConstant.YES.equals(pb.getInterestSign())) {
                    list.addAll(this.handleExpiryDailyExecute(pb));
                }
            }
            isFirst++;
        }

        paragraphList.addAll(list);//将结息、预提分段添加到全局分段中
        //CopyOnWriteArrayList不支持Collections.sort()
        List<ParagraphBean> paragraphListTemp = new ArrayList<>(paragraphList);
        if (!EmptyUtils.isEmptyList(paragraphListTemp)) {
            Collections.sort(paragraphListTemp, new Comparator<ParagraphBean>() {
                public int compare(ParagraphBean o1, ParagraphBean o2) {
                    return o1.getEndDate().compareTo(o2.getEndDate());
                }
            });
        }
        paragraphList.clear();
        paragraphList.addAll(paragraphListTemp);
    }


    /**
     * 结息、预提分段执行
     */
    private List<ParagraphBean> handleExpiryDailyExecute(ParagraphBean pb, boolean isStanderd, Integer count) {
        List<ParagraphBean> list = new ArrayList<ParagraphBean>();
        Integer day = null;
        if (InterestConstant.INTEREST_SIGN_YES.equals(pb.getInterestSign())) {
            day = pb.getExpiryDate();//结息日
        } else {
            day = pb.getAccruedDailyDate();//预提日
        }
        if (EmptyUtils.isEmpty(day) || day == 0) return list;

        /**
         * 循环获取结息日期，与分段的开始日期、结束日期比较
         */
        Date date = send.getSendDate();//结息、预提开始日期
        do {
            date = this.getInterestDate(date, day, isStanderd, count);//获取下个结息、预提日期
            if (compareDate(date, pb.getEndDate()) == 0) {
                accrualList.add(date);//保存每一段的预提日
            }
            //结息分段有问题
            if (compareDate(pb.getStartDate(), date) < 0 && compareDate(date, pb.getEndDate()) < 0) {//开始日期 < 结息、预提日期 < 结束日期
                //结息、预提分段
                ParagraphBean newPb = new ParagraphBean();
                BeanUtils.copyNotNullProperties(pb, newPb);

                pb.setEndDate(date);//设置结息、预提分段 上段的结束日期为结息、预提日期
                pb.setIsRepayCorpus(false);//归还本金标记
                pb.setSignRepayDate(date);//设置还款日期标记

                newPb.setStartDate(date);//设置结息、预提 下段的开始日期为结息、预提日期

                list.add(newPb);

                pb = newPb;//newPb将作为下次比较日期的主体
            }
        } while (compareDate(date, pb.getEndDate()) < 0);//结息预提日期 < 结束日期

        return list;
    }


    /**
     * 利率调整分段
     * 只在固定利率时有效
     */
    private void handleRateAdjust() {
        if (!EmptyUtils.isEmptyList(rateAdjustList)) {
//			List<ParagraphBean> newParageraphList = new ArrayList<ParagraphBean>();
            Iterator<RateAdjustBean> iterator = rateAdjustList.iterator();
            boolean pdSort = true;
            while (iterator.hasNext()) {
                RateAdjustBean rab = iterator.next();
                for (ParagraphBean pb : paragraphList) {
                    if (compareDate(rab.getEnterDate(), pb.getStartDate()) <= 0) {//利率调整生效日期 <= 开始日期
                        pb.setPlRate(rab.getPlRate());
                        pb.setDeferredRate(rab.getDeferredRate());
                    } else if (compareDate(rab.getEnterDate(), pb.getEndDate()) < 0) {//开始日期 < 利率调整生效日期  < 结束日期
                        ParagraphBean newPb = new ParagraphBean();
                        BeanUtils.copyNotNullProperties(pb, newPb);

                        pb.setEndDate(rab.getEnterDate());//设置利率调整分段 上段的结束日期为利率调整生效日期
                        pb.setIsRepayCorpus(false);//归还本金标记
                        pb.setIsRepayInt(false);//归还利息
                        newPb.setStartDate(rab.getEnterDate());//设置利率调整分段 下段的开始日期为利率调整生效日期
                        newPb.setPlRate(rab.getPlRate());
                        newPb.setDeferredRate(rab.getDeferredRate());//展期利率
                        newPb.setOverdueRate(rab.getOverdueRate());//预期利率
                        paragraphList.add(newPb);
//						newParageraphList.add(newPb);
                        pdSort = true;//需要重新排序
                    } else {

                    }
                }
                if (pdSort) {
                    List temp = Arrays.asList(paragraphList.toArray());
                    Collections.sort(temp, new Comparator<ParagraphBean>() {
                        public int compare(ParagraphBean p1, ParagraphBean p2) {
                            Calendar c1 = Calendar.getInstance();
                            c1.setTime(p1.getStartDate());
                            Calendar c2 = Calendar.getInstance();
                            c2.setTime(p2.getStartDate());
                            return c1.compareTo(c2);
                        }
                    });
                    paragraphList.clear();
                    paragraphList.addAll(temp);
                    pdSort = false;
                }
            }
//			paragraphList.addAll(newParageraphList);//将新加分段添加到全局
        }
    }

    /**
     * 基准利率调整
     * 只在浮动利率时有效
     */
    private void handleBaseRate() {
        if (!EmptyUtils.isEmptyList(baseRateBeanList)) {
//			List<ParagraphBean> newParageraphList = new ArrayList<ParagraphBean>();
            Iterator<InstRateBean> iterator = baseRateBeanList.iterator();
            Date effectDate = send.getRateEntriesEffectDate();

            BigDecimal previous = send.getBenchmarkRate();//上一个基准利率

            boolean pdSort = true;
            while (iterator.hasNext()) {
                InstRateBean irb = iterator.next();

                //将分段集合按照还款日期升序排列
                if (pdSort) {
                    Collections.sort(paragraphList, new Comparator<ParagraphBean>() {
                        public int compare(ParagraphBean p1, ParagraphBean p2) {
                            Calendar c1 = Calendar.getInstance();
                            c1.setTime(p1.getStartDate());
                            Calendar c2 = Calendar.getInstance();
                            c2.setTime(p2.getStartDate());
                            return c1.compareTo(c2);
                        }
                    });
                    pdSort = false;
                }

                for (ParagraphBean pb : paragraphList) {
                    /**如果支持多次不同的浮动方式 以下不用修改*/
                    Date rateFloatEffectDate = this.getFloatDate(irb.getEffectDate(), pb);//获取基准利率生效之后的浮动开始日期

                    if (EmptyUtils.isEmpty(rateFloatEffectDate)) {
                        continue;
                    }

                    /**浮动*/
                    if (compareDate(pb.getEndDate(), effectDate) > 0) {//结束日期  > 浮动生效日期
                        if (compareDate(pb.getStartDate(), effectDate) <= 0) {//开始日期 <= 浮动生效日期 <结束日期
                            if (compareDate(rateFloatEffectDate, effectDate) < 0) {
                                rateFloatEffectDate = effectDate;
                            }
                        }

                        if (compareDate(rateFloatEffectDate, pb.getStartDate()) <= 0) {//基准利率调整生效日期 <= 开始日期
                            previous = irb.getNewRate();
                            pb.setRate(previous);
                        } else if (compareDate(rateFloatEffectDate, pb.getEndDate()) < 0) {//开始日期 < 基准利率调整生效日期  < 结束日期

                            ParagraphBean newPb = new ParagraphBean();
                            BeanUtils.copyNotNullProperties(pb, newPb);

                            pb.setRate(previous);
                            pb.setEndDate(rateFloatEffectDate);//设置基准利率调整分段 上段的结束日期为基准利率调整生效日期
                            pb.setIsRepayCorpus(false);//归还本金标记

                            newPb.setStartDate(rateFloatEffectDate);//设置基准利率调整分段 下段的开始日期为基准利率调整生效日期
                            newPb.setRate(irb.getNewRate());
                            newPb.setNewBaseRateData("01");//没什么卵用，只是用来标记 方便利率变化 将变化利率写入帐号利率编号表中
                            newPb.setIsFloatWay(true);//浮动

//							newParageraphList.add(newPb);
                            paragraphList.add(newPb);

                            previous = irb.getNewRate();
                            pdSort = true;//需要重新排序
                        } else {

                        }
                    }
                }

//				paragraphList.addAll(newParageraphList);//将新加分段添加到全局
//				newParageraphList.clear();//清空
            }


        }
    }

    /**
     * 基准利率调整 通过使用分割点来分割
     */
    private void handleBaseRateV2() {
        //原为固定利率且未进行浮动方式修改
        if ("01".equals(send.getFloatWay()) && EmptyUtils.isEmpty(floatTypeList)) {//固定利率
            return;
        }
        // 一个放款的所有段的计息标识是相同的
        String interestSign = paragraphList.get(0).getInterestSign();
        // 放款单的所有的浮动点，若是放款单的浮动方式进行了变更，也会包含进来
        List<RateFloatPointVO> floatPoints = getFloatPoint(send, baseRateBeanList, floatTypeList, interestSign);
        LOG.debug("---------------floatPoints:" + JsonUtils.toJSONString(floatPoints));
        BigDecimal previous = send.getBenchmarkRate();//使用放款日的利率
        boolean pdSort = true;
        for (RateFloatPointVO floatPoint : floatPoints) {
            for (int i = 0; i < paragraphList.size(); i++) {
                ParagraphBean pb = paragraphList.get(i);

                if ("01".equals(pb.getFloatWay())) {//固定利率
                    continue;
                }
                BigDecimal pointProportion = floatPoint.getPointProportion();
                BigDecimal rate = floatPoint.getRate();
                if (EmptyUtils.isEmpty(rate)) {
                    rate = previous;//使用放款日的利率 //TODO 放款单指定的利率是否是放款日之前最近的一个利率，若是放款日有利率，则利率分割点一定有利率
                }
                previous = rate;
                //生效日之后浮动，才变更利率和点数，也是必须变更利率和点数
                if (compareDate(floatPoint.getFloatPointDate(), pb.getStartDate()) <= 0) {//基准利率调整生效日期 <= 开始日期
                    pb.setRate(rate);
                    pb.setPointsText(pointProportion);
                    pb.setIsFloatWay(true);//浮动
                } else if (compareDate(floatPoint.getFloatPointDate(), pb.getEndDate()) < 0) {//开始日期 < 基准利率调整生效日期  < 结束日期
                    ParagraphBean newPb = new ParagraphBean();
                    BeanUtils.copyNotNullProperties(pb, newPb);

                    pb.setEndDate(floatPoint.getFloatPointDate());//设置基准利率调整分段 上段的结束日期为基准利率调整生效日期
                    pb.setIsRepayCorpus(false);//归还本金标记
                    pb.setIsRepayInt(false);//归还利息
//                    pb.setIsFloatWay(true);//浮动

                    newPb.setStartDate(floatPoint.getFloatPointDate());//设置基准利率调整分段 下段的开始日期为基准利率调整生效日期
                    newPb.setRate(rate);
                    newPb.setPointsText(pointProportion);
                    newPb.setIsFloatWay(true);//浮动
                    newPb.setNewBaseRateData("01");//没什么卵用，只是用来标记 方便利率变化 将变化利率写入帐号利率编号表中
                    paragraphList.add(newPb);

                    pdSort = true;//需要重新排序
                }
            }
        }
        //将分段集合按照还款日期升序排列
        if (pdSort) {
            List temp = Arrays.asList(paragraphList.toArray());
            Collections.sort(temp, new Comparator<ParagraphBean>() {
                public int compare(ParagraphBean p1, ParagraphBean p2) {
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(p1.getStartDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(p2.getStartDate());
                    return c1.compareTo(c2);
                }
            });
            paragraphList.clear();
            paragraphList.addAll(temp);
            pdSort = false;
        }
    }

    /**
     * 获取这个放款单对应的利率浮动点
     *
     * @param send             放款单
     * @param baseRateBeanList 放款单对应的利率列表
     * @param floatTypeList    放款单对应的浮动方式最新的变更
     * @param interestSign     计息标识
     * @return
     */
    public List<RateFloatPointVO> getFloatPoint(CmsInlSend send, List<InstRateBean> baseRateBeanList, List<IstFloatBean> floatTypeList, String interestSign) {
        List<RateFloatPointVO> allFloatPoints = Lists.newArrayList();
        Date baseDate = send.getSendDate();
        RateFloatRuleVO floatRule = this.rateFloatRuleService.inlbuildRateFloatRuleVO(send, baseDate, send.getFloatWay(), send.getFloatWayNext(), interestSign);
        floatRule.setInstRateBeanList(baseRateBeanList);//放款单指定的报价利率
        floatRule.setPointProportion(send.getPointsProportionText());// 使用放款单指定的生效日的pb点数，点数或比例,点数或比例从生效日之后都是固定的，生效日之前是没有点数或比例的
        Date startDate = send.getRateEntriesEffectDate();//放款单指定的浮动生效日期
        if (EmptyUtils.isNotEmpty(floatTypeList)) {
            IstFloatBean newFloatBean = floatTypeList.get(0);//最新的浮动方式
            if (!EmptyUtils.isEmpty(startDate)) {//原是浮动，修改浮动方式
                //固定转浮动，发放数据没有浮动生效日期
                if (compareDate(newFloatBean.getEffectDate(), startDate) > 0) {
                    // 新的浮动方式的执行时间 > 放款单指定的利率浮动生效时间，从 放款单指定的利率浮动生效时间 到 新的浮动方式的执行时间 时间段使用放款单指定的利率浮动方式的规则和利率报价进行分割
                    Date beforeStartDate = startDate;
                    Date beforeEndDate = newFloatBean.getEffectDate();
                    floatRule.setStartDate(beforeStartDate);
                    floatRule.setEndDate(beforeEndDate);
                    //从 放款单指定的利率浮动生效时间 到 新的浮动方式的执行时间 使用生效日的pb点数，点数或比例从生效日之后都是固定的，生效日之前是没有点数或比例的

                    List<RateFloatPointVO> beforeFloatPoints = this.rateFloatRuleService.getRateFloatPoints(floatRule);
                    allFloatPoints.addAll(beforeFloatPoints);
                }
            }
            // 处理 从 新的浮动方式的执行时间 开始 到 贷款结束时间
            startDate = newFloatBean.getEffectDate();
            // 以执行日期作为分割的基准
            baseDate = newFloatBean.getEffectDate();

            // 从 新的浮动方式的执行时间 到 放款单指定的贷款结束时间   时间段使用新的利率浮动方式的规则和新的报价利率 以 执行日期 作为基准 进行分割
            // 从 新的浮动方式的执行时间 到 使用新的浮动方式的第一次分割点 使用的是 前一个分割段的利率和PB点数
            floatRule = this.rateFloatRuleService.inlbuildRateFloatRuleVO(send, baseDate, newFloatBean.getFloatWay(), newFloatBean.getFloatWayNext(), interestSign);
            if (CmsConstant.FLOAT_WAY_01.equals(floatRule.getFloatWay())) {
                floatRule.setFixedRate(newFloatBean.getFixRate());
            }
            floatRule.setInstRateBeanList(newFloatBean.getInstRateBeansForLastTimesFloat());
            floatRule.setPointProportion(newFloatBean.getPointsText());//使用新的pb点数
        }
        floatRule.setStartDate(startDate);
        floatRule.setEndDate(send.getEndDate());// 放款单指定的贷款结束时间

        List<RateFloatPointVO> floatPoints = this.rateFloatRuleService.getRateFloatPoints(floatRule);
        allFloatPoints.addAll(floatPoints);

        return allFloatPoints;
    }

    /**
     * 浮动方式调整分段
     * 只取生效日期最大的浮动方式调整
     */
    private void handleFloatType() {
        // 为了 【利率调整生效日期，利率浮动方式修改的执行日】有浮动方式，即用发放时确定的浮动方式
        if (!"01".equals(send.getFloatWay())) {//浮动利率
            Date effectDate = send.getRateEntriesEffectDate();
            if (!EmptyUtils.isEmpty(effectDate)) {
//				List<ParagraphBean> newSendParageraphList = new ArrayList<ParagraphBean>();
                for (ParagraphBean pb : paragraphList) {
                    pb.setFloatWay(send.getFloatWay());//设置浮动方式
                    pb.setFloatWayNext(send.getFloatWayNext());//具体浮动方式
                    // ---- 2022-5-26产品提出的需求：从贷款日到生效日，也要加上PB点数 start ----------------
                    pb.setPointsText(send.getPointsProportionText());
                    pb.setIsFloatWay(true);
                    // ----2022-5-26产品提出的需求： 从贷款日到生效日，也要加上PB点数 end ----------------
                    if (compareDate(effectDate, pb.getStartDate()) <= 0) {//利率调整生效日期 <= 开始日期
                        pb.setIsFloatWay(true);
                        // 决定因素是：生效之后，利率浮动，不浮动不需要修改点数
                    } else if (compareDate(effectDate, pb.getEndDate()) < 0) {//开始日期  < 利率调整生效日期 < 结束日期
                        /**暂时不用浮动生效日期分段*/
                        //需要 用浮动生效日期分段，为了记录分割的段的点数
							/*ParagraphBean newPb = new ParagraphBean();
							BeanUtils.copyNotNullProperties(pb, newPb);

							pb.setEndDate(effectDate);//设置利率调整生效日期分段 上段的结束日期为利率调整生效日期
							pb.setIsRepayCorpus(false);//归还本金标记
							pb.setFloatWay(send.getFloatWay());//设置浮动方式
							pb.setFloatWayNext(send.getFloatWayNext());//具体浮动方式

							newPb.setStartDate(effectDate);//设置利率调整生效日期分段 下段的开始日期为利率调整生效日期
							newPb.setIsFloatWay(true);//浮动
							newPb.setFloatWay(send.getFloatWay());//设置浮动方式
							newPb.setFloatWayNext(send.getFloatWayNext());//具体浮动方式
							newPb.setPointsText( send.getPointsProportionText());

							newSendParageraphList.add(newPb);*/
                    } else {

                    }
                }
//				paragraphList.addAll(newSendParageraphList);
            }
        }
        List<ParagraphBean> newParageraphList = new ArrayList<ParagraphBean>();
        boolean pdSort = true;
        if (!EmptyUtils.isEmptyList(floatTypeList)) {
            Iterator<IstFloatBean> iterator = floatTypeList.iterator();//集合中只有一条最大生效日期的数据 //TODO 待确认应该只有最后一次修改的浮动方式有效，而不是 最大的执行日是有效的
            while (iterator.hasNext()) {
                IstFloatBean isb = iterator.next();
                for (ParagraphBean pb : paragraphList) {
//					pb.setFloatWay(isb.getFloatWay());//设置浮动方式
//					pb.setFloatWayNext(isb.getFloatWayNext());//具体浮动方式
                    if (compareDate(isb.getEffectDate(), pb.getStartDate()) <= 0) {//浮动方式率调整生效日期 <= 开始日期
                        //TODO 将上面的2行放到下面来，如下
                        pb.setFloatWayNext(isb.getFloatWayNext());//具体浮动方式
                        pb.setFloatWay(isb.getFloatWay());//设置浮动方式
                        if (isb.getFloatWay().equals("01")) {
                            pb.setRate(isb.getFixRate());
                            pb.setIsFloatWay(false);//浮动
                            pb.setPointsText(BigDecimal.ZERO);
                        } else {
                            pb.setRate(isb.getRate());//基准利率
                            pb.setIsFloatWay(true);//浮动
                            pb.setPointsText(isb.getPointsText());
                        }
                    } else if (compareDate(isb.getEffectDate(), pb.getEndDate()) < 0) {//开始日期  < 浮动方式调整生效日期  < 结束日期
                        ParagraphBean newPb = new ParagraphBean();
                        BeanUtils.copyNotNullProperties(pb, newPb);

                        pb.setEndDate(isb.getEffectDate());//设置浮动方式调整分段 上段的结束日期为浮动方式调整生效日期
                        pb.setIsRepayCorpus(false);//归还本金标记
//                        pb.setIsRepayInt(false);//归还利息
                        // TODO 用旧的
//						pb.setFloatWay(isb.getFloatWay());//设置浮动方式
//						pb.setFloatWayNext(isb.getFloatWayNext());//具体浮动方式


                        newPb.setStartDate(isb.getEffectDate());//设置浮动方式调整分段 下段的开始日期为浮动方式调整生效日期
                        if (isb.getFloatWay().equals("01")) {
                            newPb.setRate(isb.getFixRate());
                            newPb.setIsFloatWay(false);//浮动
                            newPb.setPointsText(BigDecimal.ZERO);
                        } else {
                            newPb.setRate(isb.getRate());
                            newPb.setIsFloatWay(true);//浮动
                            newPb.setPointsText(isb.getPointsText());
                        }
                        newPb.setFloatWay(isb.getFloatWay());//设置浮动方式
                        newPb.setFloatWayNext(isb.getFloatWayNext());//具体浮动方式

                        newParageraphList.add(newPb);
                        pdSort = true;//需要重新排序
                    } else {

                    }

                }
            }
        } else {//取发放表信息中的浮动生效日期

        }

        paragraphList.addAll(newParageraphList);//将新加分段添加到全局
        if (pdSort) {
            List temp = Arrays.asList(paragraphList.toArray());
            Collections.sort(temp, new Comparator<ParagraphBean>() {
                public int compare(ParagraphBean p1, ParagraphBean p2) {
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(p1.getStartDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(p2.getStartDate());
                    return c1.compareTo(c2);
                }
            });
            paragraphList.clear();
            paragraphList.addAll(temp);
            pdSort = false;
        }
    }

//	/**展期分段*/
//	private void handleDeferredOverdue(){
//		Date sendEndDate = this.send.getEndDate();//发放结束日期
//		Date deferredDate = this.send.getDeferredEndDate();//发放展期到期日期
//		if(!EmptyUtils.isEmpty(deferredDate)){//存在展期
//			List<ParagraphBean> newParageraphList = new ArrayList<ParagraphBean>();
//			for(ParagraphBean pb : paragraphList){
//				if(compareDate(sendEndDate, pb.getStartDate()) <= 0){//发放结束日期<= 开始日期
//					pb.setIsDeferred(true);
//					pb.setDeferredRate(send.getDeferredRate());
//				}else if(compareDate(sendEndDate, pb.getEndDate()) < 0){//开始日期  < 发放结束日期  < 结束日期
//					ParagraphBean newPb = new ParagraphBean();
//					BeanUtils.copyNotNullProperties(pb, newPb);
//
//					pb.setEndDate(sendEndDate);//设置利率调整生效日期分段 上段的结束日期为利率调整生效日期
//					pb.setIsRepayCorpus(false);//归还本金标记
//
//					newPb.setStartDate(sendEndDate);//设置利率调整生效日期分段 下段的开始日期为利率调整生效日期
//					newPb.setIsDeferred(true);
//					newPb.setDeferredRate(send.getDeferredRate());
//					newParageraphList.add(newPb);
//				}else{
//
//				}
//			}
//			paragraphList.addAll(newParageraphList);//将新加分段添加到全局
//		}
//	}

    /**
     * 展期分段
     */
    private void handleDeferredOverdue() {
        Date sendEndDate = this.send.getEndDate();//发放结束日期
        List<CmsInlDeferredVO> deferredVOList = this.getDeferredList();//展期完成的数据
        Iterator<CmsInlDeferredVO> iterator = deferredVOList.iterator();
        boolean pdSort = false;
        while (iterator.hasNext()) {
            CmsInlDeferredVO pdf = iterator.next();
            for (ParagraphBean pb : paragraphList) {
                if (compareDate(sendEndDate, pb.getStartDate()) <= 0) {//展期分段日期小于开始日期
                    pb.setIsDeferred(true);
                    pb.setDeferredRate(pdf.getDeferredRate());
                } else if (compareDate(sendEndDate, pb.getEndDate()) < 0) {//开始日期<展期分段日期<结束日期
                    ParagraphBean newPb = new ParagraphBean();
                    BeanUtils.copyNotNullProperties(pb, newPb);
                    pb.setEndDate(sendEndDate);//设置展期调整分段 上段的结束日期为展期调整生效日期
                    pb.setIsRepayCorpus(false);//归还本金标记
//                    pb.setIsRepayInt(false);//归还利息
                    newPb.setStartDate(sendEndDate);//设置展期调整分段 下段的开始日期为展期调整生效日期
                    newPb.setIsDeferred(true);
                    newPb.setDeferredRate(pdf.getDeferredRate());
                    paragraphList.add(newPb);
                    pdSort = true;//需要重新排序
                }
            }
            ;
            sendEndDate = pdf.getNowDeferredDate();
        }
        if (pdSort) {
            List temp = Arrays.asList(paragraphList.toArray());
            Collections.sort(temp, new Comparator<ParagraphBean>() {
                public int compare(ParagraphBean p1, ParagraphBean p2) {
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(p1.getStartDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(p2.getStartDate());
                    return c1.compareTo(c2);
                }
            });
            paragraphList.clear();
            paragraphList.addAll(temp);
            pdSort = false;
        }
    }

    /**
     * 将既不还本也不还息的数据与后一笔数据进行合并归还
     */
    private void mergeProvideInterest() {
        for (int i = 0; i < paragraphList.size(); i++) {
            if (i != paragraphList.size() - 1) {
                if (!paragraphList.get(i).getIsRepayCorpus() && !paragraphList.get(i).getIsRepayInt()) {
                    paragraphList.get(i).setSignRepayDate(paragraphList.get(i + 1).getSignRepayDate());
                }
            }
        }
    }

    /**
     * 浮动方式修改自定义处理
     */
    public void makeFloatWayCustom() {
        if (!EmptyUtils.isEmptyList(floatTypeList)) {
            for (IstFloatBean isb : floatTypeList) {
                if (!send.getFloatWay().equals("01") && !isb.getFloatWay().equals("01")) {
                    for (ParagraphBean pb : paragraphList) {
                        if (pb.getStartDate().compareTo(isb.getEffectDate()) >= 0 && pb.getEndDate().compareTo(isb.getFloatEffectDate()) <= 0) {
                            pb.setRate(isb.getRate());//基准利率
                            pb.setIsFloatWay(true);//浮动
                            pb.setPointsText(isb.getPointsText());
                        }
                    }
                }
            }
        }
    }

    /**
     * 将分段信息转成利息测算明细数据
     */
    private List<CmsProvideInterest> createCmsProvideInterest() {
        List<CmsProvideInterest> interestList = new ArrayList<CmsProvideInterest>();
        BigDecimal unPayInsTatal = BigDecimal.ZERO;//前几期未结利息
        //初始化上一笔结息日期为发放开始时间
        Date lInterestDate = getRepayStartDate();//发放日期作为还款开始日期
        //总共所需还利息合计
        BigDecimal sumPayInt = BigDecimal.ZERO;
        //所需归还利息条数
        Integer num = 0;
        ParagraphBean lastBean = null;
        if (paragraphList.size() > 0) {
            lastBean = paragraphList.get(paragraphList.size() - 1);//获取最后一笔数据
        }
        for (ParagraphBean pb : paragraphList) {
            if (lastBean != null) {
                if (pb.getEndDate().compareTo(lastBean.getEndDate()) == 0) {//判断是不是最后一笔
                    islast = true;
                }
            }
            CmsProvideInterest ist = new CmsProvideInterest();
            ist.setRepaymentScheduleId(pb.getRepaymentScheduleId());//设置还款计划ID
            ist.setSignRepayDate(pb.getSignRepayDate());
            ist.setContractCode(send.getSendCode());
            ist.setBillCode(send.getBillNo());
            ist.setCorpId(send.getBorrowInCorp());
            ist.setCurId(send.getCurId());
            ist.setInterestType("1");//正常

            ist.setRepayDate(pb.getEndDate());//还款日期
            ist.setInterestStartDate(pb.getStartDate());//起息日期
            ist.setBlnInstDate(DateUtils.addDay(pb.getEndDate(), -1));//结束日期 为还款日前一天

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(pb.getEndDate());
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(pb.getStartDate());
            ist.setDays(DateUtils.dateDiff('d', calendar1, calendar2));//计息天数
            if (ist.getDays() <= 0) {
                continue;
            }

            ist.setRate(pb.getRate());//基准利率
            ist.setFloatPoint(send.getPointsProportionText());//浮动点数

            BigDecimal interestRate = pb.getPlRate();
            if (pb.getIsDeferred()) {//展期
                interestRate = pb.getDeferredRate();
            } else if (pb.getIsFloatWay()) {
                //浮动利率
                interestRate = this.calcFloatRate(pb.getRate(), send.getPointsProportionSelect(), pb.getPointsText());
            }
//            else {//使用发放利率
//                if (null != send.getBenchmarkRate() && null != send.getPointsProportionSelect() && null != send.getPointsProportionText()) {
//                    BigDecimal floatRate = this.calcFloatRate(send.getBenchmarkRate(), send.getPointsProportionSelect(), send.getPointsProportionText());
////				浮动利率分段情况下，由于无法判断是手动修改利率还是浮动修改利率，则只能通过对比来判断
//                    if (send.getRate().compareTo(floatRate) == 0) {
////					发放利率=浮动利率，则表示没有进行过利率修改，此时使用基础利率(适用于浮动利率，且没有修改过发放利率)
//                        interestRate = pb.getRate();
//                    } else {
////					发放利率!=浮动利率，则表示进行过利率修改，此时使用发放利率(适用于浮动利率，且手动修改过利率)
//                        interestRate = send.getRate();
//                    }
//                }
//            }
            ist.setInterestRate(interestRate);//计息利率

            ist.setRateFax(send.getRate());
            if (null == ist.getRateFax()) {
                ist.setRateFax(BigDecimal.ZERO);
            }

            ist.setLoanMoney(pb.getMoney());//本金
            if (pb.getIsRepayCorpus()) {//是否归还本金
                ist.setRepayMoney(pb.getRepayMoney());//归还金额
            } else {
                ist.setRepayMoney(BigDecimal.ZERO);
            }
            BigDecimal leftMoney = ist.getLoanMoney().subtract(ist.getRepayMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
            ist.setLeftMoney(leftMoney);//剩余本金

            String rateBaseStr = info.getRateBase();
            BigDecimal rateBase = new BigDecimal(360);//利息测算基数
            try {
                rateBase = new BigDecimal(rateBaseStr);
            } catch (Exception e) {
                rateBase = new BigDecimal(360);
            }
            BigDecimal interest = BigDecimal.ZERO;//如果还款方式为等额本金不还息和其他不还息，则还本时利息为0
            if ("1".equals(pb.getOldOrNew())) {  //兼容历史数据
                if (!CmsConstant.REPAY_TYPE_05.equals(send.getRepayType()) && !CmsConstant.REPAY_TYPE_06.equals(send.getRepayType())) {
                    interest = ist.getRepayMoney().multiply(new BigDecimal(ist.getDays()))
                            .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);//避免精度丢失
                } else {
                    if (islast) {//如果最后一次还款，则还所有的利息
                        interest = ist.getRepayMoney().multiply(new BigDecimal(ist.getDays()))
                                .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);//避免精度丢失
                    }
                }
            } else {//新需求计提计息
                interest = ist.getRepayMoney().multiply(new BigDecimal(ist.getDays()))
                        .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);//避免精度丢失

            }
            ist.setInterest(interest);//归还本金利息

            BigDecimal totalInterest = ist.getLoanMoney().multiply(new BigDecimal(ist.getDays()))
                    .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            ist.setTotalInterest(totalInterest);//全部利息

            BigDecimal interestFax = totalInterest.
                    divide(new BigDecimal(100).subtract(ist.getRateFax()), 10, BigDecimal.ROUND_HALF_UP)
                    .multiply(ist.getRateFax()).setScale(2, BigDecimal.ROUND_HALF_UP);
            ist.setInterestFax(interestFax);//利息税率

            ist.setInterest(interest);//归还本金利息

            if (!pb.getIsRepayInt()) {//是否归还利息
//				ist.setPayInterest(interest);//支付利息
                ist.setPayInterest(BigDecimal.ZERO);//支付利息
                boolean isOk = false;
                for (Date accrual : accrualList) {
                    isOk = (pb.getEndDate().compareTo(accrual) == 0) ? true : false;
                    if (isOk) {
                        break;
                    }
                }
                if (isOk || islast) {//isok==true
                    ist.setUnPayInterest(BigDecimal.ZERO);//未结息
//                    ist.setRealInterest(totalInterest.add(unPayInsTatal));//实付利息
                    ist.setPayInterest(totalInterest.add(unPayInsTatal));//支付利息
                    num += 1;
                    unPayInsTatal = BigDecimal.ZERO;
                } else {
                    if (pb.getOldOrNew().equals("2") && pb.getIsRepayCorpus() && StringUtils.isNotEmpty(this.repayType.getInterestRepayType()) && this.repayType.getInterestRepayType().equals("1")) {
                        //是否归还本金,并且还本时归还利息
                        if ("1".equals(this.repayType.getInterestRepayTypeNext())) {//付还款本金产生的利息
                            Calendar calendar3 = Calendar.getInstance();
                            calendar3.setTime(ist.getRepayDate());
                            Calendar calendar4 = Calendar.getInstance();
                            calendar4.setTime(lInterestDate);
                            Integer days = DateUtils.dateDiff('d', calendar3, calendar4);//计息天数
                            //本金在距离上次还息之间产生的利息
                            BigDecimal moneyInterest = ist.getRepayMoney().multiply(new BigDecimal(days))
                                    .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                            ist.setPayInterest(moneyInterest);//支付利息
                            num += 1;
                            ist.setUnPayInterest(totalInterest.subtract(ist.getInterest()));
                            unPayInsTatal = unPayInsTatal.add(totalInterest).subtract(moneyInterest);
                        } else if ("0".equals(this.repayType.getInterestRepayTypeNext())) {//还本时同时还剩余本金产生的利息
                            ist.setPayInterest(unPayInsTatal.add(totalInterest));//支付利息
                            num += 1;
                            ist.setUnPayInterest(BigDecimal.ZERO);
                            unPayInsTatal = BigDecimal.ZERO;
                            //进行还息了则记录最新上一笔还息日期
                            lInterestDate = ist.getRepayDate();
                        }
                    } else {
                        ist.setUnPayInterest(totalInterest);
                        unPayInsTatal = unPayInsTatal.add(totalInterest);
                    }
                }
            } else {
                ist.setPayInterest(unPayInsTatal.add(totalInterest));//支付利息
                num += 1;
                ist.setUnPayInterest(BigDecimal.ZERO);
                unPayInsTatal = BigDecimal.ZERO;
                //进行还息了则记录最新上一笔还息日期
                lInterestDate = ist.getRepayDate();
            }
            sumPayInt = sumPayInt.add(ist.getPayInterest());//总要归还利息合计
            ist.setStatus(0);//未还
            ist.setInterestSign(pb.getInterestSign());
            ist.setRealInterest(BigDecimal.ZERO);//实付利息
            interestList.add(ist);
        }

        if (!EmptyUtils.isEmpty(this.repayType)) {
            if (StringUtils.isNotEmpty(this.repayType.getInterestWay()) && this.repayType.getInterestWay().equals("1")) {
                BigDecimal nvlInterest = sumPayInt.divide(new BigDecimal(num), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                //一次性还本，平均还息
                for (CmsProvideInterest cist : interestList) {
                    if (cist.getPayInterest().compareTo(BigDecimal.ZERO) > 0) {
                        cist.setPayInterest(nvlInterest);
                    }
                }
            }
        }
        return interestList;
    }

    /**
     * 计算浮动利率
     */
    protected BigDecimal calcFloatRate(BigDecimal plrate, String floatType, BigDecimal floatPoint) {
        if (floatPoint == null) {
            floatPoint = BigDecimal.ZERO;
        }
        if ("02".equals(floatType)) {//比例   利率plrate * ( 1 + ( 比例floatPoint / 100 ))
            return plrate.multiply(new BigDecimal(1).add(floatPoint.divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP))).setScale(6, BigDecimal.ROUND_HALF_UP);
        } else {//点数浮动   利率plrate + (点数floatPoint / 10000)
            return plrate.add(floatPoint.divide(new BigDecimal(10000), 10, BigDecimal.ROUND_HALF_UP)).setScale(6, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 获取日期 date 的下个结息、预提日期
     *
     * @param date
     * @param day        结息、预提日
     * @param isStanderd 自然季(false)或标准季(true)
     * @param count      不同结息方式对应的数字
     * @return
     */
    protected Date getInterestDate(Date date, Integer day, boolean isStanderd, Integer count) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar = getFirstDayCalender(date);
        int monthDay = calendar1.get(Calendar.DAY_OF_MONTH);//还款日期所在月份第几天
        if (count == 1) {//每月计息按照自然季来计算
            isStanderd = true;
        }
        //判断当月是否满足计息条件
        if (monthDay < day) {//还款日小于计息日，则判断能否计息
            if (count == 1) {
                this.setInstDateDay(calendar, day);//结息、预提的准确年月日
                isFirst++;
                return calendar.getTime();
            } else if (count == 3) {
                if (isStanderd) {//标准
                    int month = calendar.get(Calendar.MONTH);//确认是否季末
                    if ((month == 2 || month == 5 || month == 8 || month == 11)) {
                        this.setInstDateDay(calendar, day);//结息、预提的准确年月日
                        isFirst++;
                        return calendar.getTime();
                    }
                }
            }
        }
        if (isStanderd) {//标准
            int month = calendar.get(Calendar.MONTH);//传入日期所在月份减1
            do {
                //第一次还款，传入月份是结息月，结息日=还款日，还款方式非还本付息时不处理时间
                if (isFirst == 0 && (month == 2 || month == 5 || month == 8 || month == 11) && monthDay == day && !"01".equals(sendRepayType)) {
                    break;
                } else {
                    calendar.add(Calendar.MONTH, 1);//月份+1
                }
                month = calendar.get(Calendar.MONTH) + 1;//还原java月份差额
            } while (month % count != 0);//非结息月时进行日期叠加，直到找到下个结息月
        } else {//自然
            calendar.add(Calendar.MONTH, count);//月份+1
        }
        this.setInstDateDay(calendar, day);//结息、预提的准确年月日
        isFirst++;
        return calendar.getTime();
    }

    /**
     * 获取日期date的浮动生效日期
     *
     * @param date 发放日期
     * @param pb   对象分段
     * @return
     */
    private Date getFloatDate(Date date, ParagraphBean pb) {
        boolean isStanderd = "02".equals(pb.getIsStanderd()) ? true : false;//标准季(true) 自然季(false)
        Integer day = null;
        if (InterestConstant.INTEREST_SIGN_YES.equals(pb.getInterestSign())) {
            day = pb.getExpiryDate();//结息日
        } else {
            day = pb.getAccruedDailyDate();//预提日
        }
        if (EmptyUtils.isEmpty(day) || day == 0) day = 21;//默认21号

        String floatWay = pb.getFloatWay();//浮动方式
        String floatWayNext = pb.getFloatWayNext();

        if ("01".equals(floatWay)) {//固定利率
            return null;
        } else if ("02".equals(floatWay)) {//即时浮动
            return date;
        } else if ("03".equals(floatWay)) {//按月浮动
            return this.getFloatDate(date, day, floatWayNext, isStanderd, 1);
        } else if ("04".equals(floatWay)) {//按季浮动
            return this.getFloatDate(date, day, floatWayNext, isStanderd, 3);
        } else if ("05".equals(floatWay)) {//按半年浮动
            return this.getFloatDate(date, day, floatWayNext, isStanderd, 6);
        } else if ("06".equals(floatWay)) {//按年浮动
            return this.getFloatDate(date, day, floatWayNext, isStanderd, 12);
        }
        return null;
    }

    /**
     * 获取日期sendDate的浮动生效日期
     *
     * @param sendDate     发放日期
     * @param day          结息日
     * @param floatWayNext 下次浮动明细
     * @param isStanderd   自然季(false)或标准季(true)
     * @param count        不同浮动方式对应的数字
     * @return
     */
    private Date getFloatDate(Date sendDate, Integer day, String floatWayNext, boolean isStanderd, Integer count) {
        Calendar calendar = getFirstDayCalender(sendDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//月初第一天
        if (count == 1) {//每月计息按照自然季来计算
            isStanderd = false;
        }
        if (isStanderd) {//标准
            if ("1".equals(floatWayNext)) {//次《什么》第一天
                day = 1;
                int month = calendar.get(Calendar.MONTH);
                do {
                    calendar.add(Calendar.MONTH, 1);//月份+1
                    month = calendar.get(Calendar.MONTH) + 1;//还原java月份差额
                } while (month % count != 1);
            } else {
                if ("2".equals(floatWayNext)) {//次《什么》第贷款日
                    Calendar sendCalendar = Calendar.getInstance();
                    sendCalendar.setTime(this.send.getSendDate());
                    day = sendCalendar.get(Calendar.DAY_OF_MONTH);//获取贷款日
                } else {//计息日次日
                    day += 1;
                }

                calendar.add(Calendar.MONTH, count);//增加 count确保日期进入下个季度或者半年
                int month = calendar.get(Calendar.MONTH) + 1;
                while (month % count != 0) {
                    calendar.add(Calendar.MONTH, 1);//月份+1
                    month = calendar.get(Calendar.MONTH) + 1;//还原java月份差额
                }
            }
        } else {//自然
            if ("1".equals(floatWayNext)) {//次《什么》第一天
                day = 1;
            } else if ("2".equals(floatWayNext)) {//次《什么》第贷款日
                Calendar sendCalendar = Calendar.getInstance();
                sendCalendar.setTime(this.send.getSendDate());
                day = sendCalendar.get(Calendar.DAY_OF_MONTH);//获取贷款日
            } else {
                day += 1;
            }
            calendar.add(Calendar.MONTH, count);//月份+1
        }
        this.setInstDateDay(calendar, day);//浮动生效的准确年月日
        return calendar.getTime();
    }

    /**
     * 获取结息/预提日期（使用新计提计息方式）
     *
     * @param beginDate     开始日期
     * @param endDate       结束日期
     * @param frequencyNext 天/周/月/季/年
     * @param FrequencyNum  数值
     * @param Frequency     季末/半年
     * @param dayNum        结息/预提日
     * @return 返回结息/预提日期
     */
    public Date getDate(Date beginDate, Date endDate, String frequencyNext,String frequencyThird, String FrequencyNum,
                        String Frequency, Integer dayNum, Boolean isRepayCapital,String isThis) {
        Date repayDate = beginDate;
        if (BaseDataConstant.FREQUENCY_NEXT_1.equals(frequencyNext)) {
//            按日
            if (BaseDataConstant.NUM.equals(Frequency)) {
//                到期一次性支付
                repayDate = endDate;
            } else {
//                其他
                repayDate = DateUtils.addDay(beginDate, Integer.parseInt(Frequency));
            }
        } else if (BaseDataConstant.FREQUENCY_NEXT_2.equals(frequencyNext)) {
//            按周
            if (isRepayCapital) {
//                计算还本
                repayDate = DateUtils.addWeek(beginDate, Integer.parseInt(FrequencyNum));
            } else {
//                计算结息
                int num = 0;
                do {
                    repayDate = DateUtils.addDay(repayDate, 1);
                    if (DateUtils.getDayOfWeek(repayDate) == dayNum) {
                        num += 1;
                    }
                } while (num < Integer.parseInt(FrequencyNum));
            }
        } else if (BaseDataConstant.FREQUENCY_NEXT_3.equals(frequencyNext)) {
//            按月
            if(isThis.equals(CmsConstant.IS_FALSE)) {
                //本月不计息
                repayDate = DateUtils.addMonth(beginDate, Integer.parseInt(FrequencyNum));
            }
        } else if (BaseDataConstant.FREQUENCY_NEXT_4.equals(frequencyNext)) {
//            按季
            int monthNum = DateUtils.getMonth(repayDate);
            if (BaseDataConstant.FREQUENCY_0.equals(Frequency)) {
//                季初
                if(isThis.equals(CmsConstant.IS_FALSE)) {
                    //本季不计息
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    monthNum = DateUtils.getMonth(repayDate);
                }
                while (monthNum != 1 && monthNum != 4 && monthNum != 7 && monthNum != 10){
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    monthNum = DateUtils.getMonth(repayDate);
                }

            } else if(BaseDataConstant.FREQUENCY_3.equals(Frequency)) {
//                季中
                if(isThis.equals(CmsConstant.IS_FALSE)) {
                    //本季不计息
                    repayDate = DateUtils.addMonth(repayDate, 2);
                    monthNum = DateUtils.getMonth(repayDate);
                }
                while (monthNum != 2 && monthNum != 5 && monthNum != 8 && monthNum != 11){
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    monthNum = DateUtils.getMonth(repayDate);
                }

            } else if (BaseDataConstant.FREQUENCY_1.equals(Frequency)) {
//                季末
                if(isThis.equals(CmsConstant.IS_FALSE)) {
                    //本季不计息
                    repayDate = DateUtils.addMonth(repayDate, 3);
                    monthNum = DateUtils.getMonth(repayDate);
                }
                while (monthNum != 3 && monthNum != 6 && monthNum != 9 && monthNum != 12){
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    monthNum = DateUtils.getMonth(repayDate);
                }

            }
        } else {
//            按年
            if(BaseDataConstant.FREQUENCY_THIRD_6.equals(frequencyThird)){
                //半年
                int monthNum = DateUtils.getMonth(repayDate);
                if(isThis.equals(CmsConstant.IS_FALSE)) {
                    //本期不计息
                    repayDate = DateUtils.addMonth(repayDate, 6);
                    monthNum = DateUtils.getMonth(repayDate);
                }
                while (monthNum != 6 && monthNum != 12){
                    repayDate = DateUtils.addMonth(repayDate, 1);
                    monthNum = DateUtils.getMonth(repayDate);
                }
            } else {
                //年
                if (isRepayCapital) {
                    repayDate = DateUtils.addYear(beginDate, Integer.parseInt(FrequencyNum));
                } else {
                    int monthNum = 0;
                    int num = 0;
                    do {
                        repayDate = DateUtils.addMonth(repayDate, 1);
                        monthNum = DateUtils.getMonth(repayDate);
                        if (monthNum == 12) {
                            num += 1;
                        }
                    } while (num < Integer.parseInt(FrequencyNum));
                }
            }
        }
//        计息时按月，季，半年，年时则需要设置结息日，为周时按天增加不需要设置
        if (!BaseDataConstant.FREQUENCY_NEXT_1.equals(frequencyNext) &&
                !BaseDataConstant.FREQUENCY_NEXT_2.equals(frequencyNext)) {
            if (dayNum != 31) {
                repayDate = DateUtils.getDay(repayDate, dayNum);
            } else {
//            月末最后一天
                repayDate = DateUtils.getLastMonthDate(repayDate);
            }
        }
/*		if (DateUtils.dayDiffs(repayDate, endDate) > 0) {
			repayDate = endDate;
		}*/
        return repayDate;
    }

    /**
     * 设置某月中的第day天
     */
    private void setInstDateDay(Calendar calendar, Integer day) {
        String d = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
        Integer lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//月份最后一天
        if (day < lastDay && day != 0) {//结息日小于月末最后一天
            calendar.set(Calendar.DAY_OF_MONTH, day);//结息日
        } else {
            //结息日大于月末最后一天,设置最后一天为结息日
            calendar.set(Calendar.DAY_OF_MONTH, lastDay);//月末最后一天作为结息日
        }
    }


    /**
     * 获取给定日期的第一天
     */
    protected Calendar getFirstDayCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 比较两个日期大小
     *
     * @param d1
     * @param d2
     * @return d1 > d2  1 , d1 = d2  0 , d1 < d2  -1
     */
    static int compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.compareTo(c2);
    }

    public CopyOnWriteArrayList<ParagraphBean> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(CopyOnWriteArrayList<ParagraphBean> paragraphList) {
        this.paragraphList = paragraphList;
    }

    public CmsInlSend getSend() {
        return send;
    }

    public void setSend(CmsInlSend send) {
        this.send = send;
    }

    public CmsInlInfo getInfo() {
        return info;
    }

    public void setInfo(CmsInlInfo info) {
        this.info = info;
    }

    public RepayType getRepayType() {
        return repayType;
    }

    public void setRepayType(RepayType repayType) {
        this.repayType = repayType;
    }

    public RateFloatRuleService getRateFloatRuleService() {
        return rateFloatRuleService;
    }

    public void setRateFloatRuleService(RateFloatRuleService rateFloatRuleService) {
        this.rateFloatRuleService = rateFloatRuleService;
    }

    public CmsRepaymentSchedule getCmsRepaymentSchedule() {
        return cmsRepaymentSchedule;
    }

    public void setCmsRepaymentSchedule(CmsRepaymentSchedule cmsRepaymentSchedule) {
        this.cmsRepaymentSchedule = cmsRepaymentSchedule;
    }

    public List<RepaymentSchedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<RepaymentSchedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public List<RateAdjustBean> getRateAdjustList() {
        return rateAdjustList;
    }

    public void setRateAdjustList(List<RateAdjustBean> rateAdjustList) {
        this.rateAdjustList = rateAdjustList;
    }

    public List<InstRateBean> getBaseRateBeanList() {
        return baseRateBeanList;
    }

    public void setBaseRateBeanList(List<InstRateBean> baseRateBeanList) {
        this.baseRateBeanList = baseRateBeanList;
    }

    public List<IstFloatBean> getFloatTypeList() {
        return floatTypeList;
    }

    public void setFloatTypeList(List<IstFloatBean> floatTypeList) {
        this.floatTypeList = floatTypeList;
    }

    public List<InterestWayBean> getIstWayList() {
        return istWayList;
    }

    public void setIstWayList(List<InterestWayBean> istWayList) {
        this.istWayList = istWayList;
    }

    public List<CmsInlDeferredVO> getDeferredList() {
        return deferredList;
    }

    public void setDeferredList(List<CmsInlDeferredVO> deferredList) {
        this.deferredList = deferredList;
    }

    public Date getRepayStartDate() {
        return repayStartDate;
    }

    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
    }
}
