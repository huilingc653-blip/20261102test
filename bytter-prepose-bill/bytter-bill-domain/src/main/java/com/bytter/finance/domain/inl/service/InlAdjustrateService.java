package com.bytter.finance.domain.inl.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlAdjustrateDao;
import com.bytter.finance.domain.inl.dao.InlDeferredDao;
import com.bytter.finance.domain.inl.entity.CmsInlAdjustrate;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlAdjustrateVO;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.domain.provide.vo.RateAdjustBean;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
@WorkFlow(InlWorkFlowConstant.CMS_INL_ADJUSTRATE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_ADJUSTRATE)
@Service("inlAdjustrateService")
public class InlAdjustrateService extends BaseService<CmsInlAdjustrate, String> implements IWorkFlowListener {
    @Autowired
    private InlAdjustrateDao inlAdjustrateDao;
    @Autowired
    private InlDeferredDao inlDeferredDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private InlSendService inlSendService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /**
     * 申请列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_SEND_QUERY, user.getId());
        DataTablesOutput output = inlAdjustrateDao.dataTable(input, corpIds);
        // 设置最新得展期到期日期
        setEndDate(output);
        return output;
    }
    /**
     * 设置到期日期
     *
     * @param output
     */
    public void setEndDate(DataTablesOutput output) {
        List<CmsInlAdjustrateVO> cmsInlAdjustrateVOS = output.getData();
        Map<String, Object> params = new HashMap<>();
        for (CmsInlAdjustrateVO cmsInlAdjustrateVO : cmsInlAdjustrateVOS) {
            params.put("inlSendId", cmsInlAdjustrateVO.getInlSendId());
            List<CmsInlDeferredVO> cmsInlDeferredVOS1 = inlDeferredDao.getCmsInlDeferredVOBySendId(params);
            if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS1)) {
                cmsInlAdjustrateVO.setDeferredDate(cmsInlDeferredVOS1.get(0).getNowDeferredDate());
            }
        }
        output.setData(cmsInlAdjustrateVOS);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_ADJUSTRATE, input);
        DataTablesOutput output = inlAdjustrateDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList());
        // 设置最新得展期到期日期
        setEndDate(output);
        return workFlowBusiness.merge(output);
    }
    /**
     * 保存数据
     *
     * @param cmsInlAdjustrateVO
     * @param user
     */
    public void save(CmsInlAdjustrateVO cmsInlAdjustrateVO, ICurrentUser user) {
        CmsInlAdjustrate cmsInlAdjustrate;
        if (StringUtils.isNotEmpty(cmsInlAdjustrateVO.getId())) {
            cmsInlAdjustrate = inlAdjustrateDao.get(cmsInlAdjustrateVO.getId());
            BeanUtils.copyNotNullProperties(cmsInlAdjustrateVO, cmsInlAdjustrate);
            cmsInlAdjustrate.setUpdateBy(user.getId());
            cmsInlAdjustrate.setUpdateDate(new Date());
        } else {
            cmsInlAdjustrate = new CmsInlAdjustrate();
            BeanUtils.copyNotNullProperties(cmsInlAdjustrateVO, cmsInlAdjustrate);
            cmsInlAdjustrate.setCreateBy(user.getId());
            cmsInlAdjustrate.setCreateDate(new Date());
            cmsInlAdjustrate.setBillNo(InlUtil.getInlAdjuestrateNo());
        }
        inlAdjustrateDao.saveOrUpdate(cmsInlAdjustrate);
        if (InlConstant.COMMIT_APPROVE == cmsInlAdjustrate.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_ADJUSTRATE, cmsInlAdjustrate.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_ADJUSTRATE, cmsInlAdjustrate.getId(), cmsInlAdjustrate, cmsInlAdjustrate.getCorpId(), user.getId());
        }
    }
    @Override
    public BaseDao<CmsInlAdjustrate, String> getEntityDao() {
        return inlAdjustrateDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        CmsInlSend cmsInlSend = inlSendService.get(cmsInlAdjustrate.getInlSendId(), true);
        if (isPass) {
            cmsInlAdjustrate.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlAdjustrate.getBillNo(), InlConstant.CMS_INL_ADJUSTRATE_NO, CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
            if (null != cmsInlAdjustrate.getAdjustDeferredRate()) {
                cmsInlSend.setDeferredRate(cmsInlAdjustrate.getAdjustDeferredRate());
            }
            //附件id
            if(StringUtils.isNotBlank(cmsInlAdjustrate.getAttachmentId())){
                cmsInlSend.setAttachmentId(cmsInlAdjustrate.getAttachmentId());
            }
            inlSendService.updateEntity(cmsInlSend);
        } else {
            cmsInlAdjustrate.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlAdjustrate.getId(), cmsInlAdjustrate.getCorpId(), NotificationConstant.CMS.INL_ADJUSTRATE, cmsInlAdjustrate);
            msService.insertProcessMsg(cmsInlAdjustrate.getBillNo(), InlConstant.CMS_INL_ADJUSTRATE_NO, CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        // updateBudget(dfDirectFinanceInfo, user);
        inlAdjustrateDao.update(cmsInlAdjustrate);
        inlAdjustrateDao.flush();
        if (isPass) {
            // 调整利息明细
            inlSendService.adjustProvideInterest(cmsInlSend, user);
            // 更新还款计划
            // updateReplayPlan(cmsInlAdjustrate.getInlSendId(), user);
        }
    }
    /**
     * 更新还款计划
     *
     * @param sendId
     * @param user
     */
    public void updateReplayPlan(String sendId, ICurrentUser user) {
        inlAdjustrateDao.flush();
        inlSendService.getEntityDao().flush();
        CmsInlSend cmsInlSend = inlSendService.get(sendId);
        CmsInlAdjustrate cmsInlAdjustrate = listCmsInlAdjustrate(sendId);
        CmsRepaymentSchedule cmsRepaymentSchedule = repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(cmsInlSend.getId());
        List<RepaymentSchedule> repaymentSchedules = repaymentScheduleDao.listRepaymentScheduleByBillNo(cmsInlSend.getId());
        List<RepaymentSchedule> repaymentSchedulesNew = new ArrayList<>();
        // 已还数据则不做改变
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            if (CommonConstant.YES.equals(repaymentSchedule.getIsRepay())) {
                repaymentSchedulesNew.add(repaymentSchedule);
            }
        }
        if (ListUtils.isNotNullOrEmpty(repaymentSchedulesNew)) {
            repaymentSchedulesNew.addAll(getListRepayPlan(repaymentSchedulesNew.get(repaymentSchedulesNew.size() - 1).getPrediceRepayDate(), cmsInlSend, cmsRepaymentSchedule, user));
        } else {
            repaymentSchedulesNew.addAll(getListRepayPlan(cmsRepaymentSchedule.getRepayBeginDate(), cmsInlSend, cmsRepaymentSchedule, user));
        }
        if (CmsConstant.REPAY_TYPE_02.equals(cmsInlSend.getRepayType())) {
            // 分期等额本金
            calRepay(cmsInlAdjustrate, cmsInlSend, repaymentSchedulesNew);
        } else if (CmsConstant.REPAY_TYPE_04.equals(cmsInlSend.getRepayType())) {
            // 分期等额本息
            calRepay2(cmsRepaymentSchedule, cmsInlAdjustrate, cmsInlSend, repaymentSchedulesNew, user);
        } else if (CmsConstant.REPAY_TYPE_01.equals(cmsInlSend.getRepayType())) {
            // 还本付息
            RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
            if (CommonConstant.YES.equals(cmsInlSend.getIsDeferred())) {
                repaymentSchedule.setPrediceRepayDate(cmsInlSend.getDeferredEndDate());
            } else {
                repaymentSchedule.setPrediceRepayDate(cmsInlSend.getEndDate());
            }
            repaymentSchedulesNew.add(repaymentSchedule);
            calRepay3(cmsInlAdjustrate, cmsInlSend, repaymentSchedulesNew);
        } else if (CmsConstant.REPAY_TYPE_03.equals(cmsInlSend.getRepayType())) {
            // 其他
        }
        if (ListUtils.isNotNullOrEmpty(repaymentSchedulesNew)) {
            // 删除旧还款计划
            repaymentScheduleDao.deleteRepaymentSchedule(cmsInlSend.getId());
            // 保存新还款计划
            repaymentScheduleDao.batchSave(repaymentSchedulesNew);
            repaymentScheduleDao.flush();
            // 调整利息明细
            inlSendService.adjustProvideInterest(cmsInlSend, user);
        }
    }
    /**
     * 生成还本明细（分期等额本金）
     *
     * @param cmsInlAdjustrate
     * @param cmsInlSend
     * @param repaymentSchedules
     * @return
     */
    public List<RepaymentSchedule> calRepay(CmsInlAdjustrate cmsInlAdjustrate, CmsInlSend cmsInlSend, List<RepaymentSchedule> repaymentSchedules) {
        if (ListUtils.isNullOrEmpty(repaymentSchedules)) {
            return null;
        }
        BigDecimal repayMoney = cmsInlSend.getSendMoney().divide(BigDecimal.valueOf(repaymentSchedules.size()), 4, RoundingMode.HALF_UP);
        // 利息计算
        calInterest(cmsInlAdjustrate, cmsInlSend, repaymentSchedules, repayMoney, null);
        BigDecimal surplusMoney = cmsInlSend.getSendMoney();// 剩余本金
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            if (CommonConstant.YES.equals(repaymentSchedule)) {
                continue;
            }
            /**
             * 当前应还本金+利息
             */
            repaymentSchedule.setCurrentRepayMoney(repayMoney.add(repaymentSchedule.getRepaymentIst()));
            /**
             * 当前应还本金
             */
            repaymentSchedule.setRepaymentMoney(repayMoney);
            /**
             * 剩余本金
             */
            repaymentSchedule.setSurplusMoney(surplusMoney);
            surplusMoney = surplusMoney.subtract(repayMoney);
        }
        return repaymentSchedules;
    }
    /**
     * 计算利息
     *
     * @param cmsInlSend            发放对象
     * @param repaymentSchedules
     * @param repayMoney            还本金额
     * @param repayMoneyAndInterest 本金利息合计
     */
    public void calInterest(CmsInlAdjustrate cmsInlAdjustrate, CmsInlSend cmsInlSend, List<RepaymentSchedule> repaymentSchedules, BigDecimal repayMoney, BigDecimal repayMoneyAndInterest) {
        BigDecimal surplusMoney = cmsInlSend.getSendMoney();
        BigDecimal interest = BigDecimal.ZERO;
        Date lastDate = repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(cmsInlSend.getId()).getRepayBeginDate();
        // 初始化还款开始日
        BigDecimal deferredRate = (null == cmsInlSend.getDeferredRate() ? cmsInlSend.getRate() : cmsInlSend.getDeferredRate());
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            if (CommonConstant.YES.equals(repaymentSchedule.getIsRepay())) {
                continue;
            }
            // 进行过利率调整
            if (null != cmsInlAdjustrate) {
                // 判断到期日是否大于上一个还款日，小于当前还款日，如果是则利息分段计算
                if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(cmsInlSend.getEndDate(), lastDate) > 0) {
                    // 正常计算利息 分段计算 上次计息日<执行日期<到期日期<当前还款日期
                    BigDecimal interest1 = calInterest2(cmsInlAdjustrate, lastDate, cmsInlSend, surplusMoney, cmsInlSend.getEndDate());
                    // 展期利息
                    Integer days2 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
                    BigDecimal avgRat = deferredRate.divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
                    BigDecimal interest2 = surplusMoney.multiply(avgRat).multiply(BigDecimal.valueOf((days2)));
                    interest = interest1.add(interest2);
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) <= 0) {
                    // 正常计算利息 分段计算
                    interest = calInterest2(cmsInlAdjustrate, lastDate, cmsInlSend, surplusMoney, repaymentSchedule.getPrediceRepayDate());
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(lastDate, cmsInlSend.getEndDate()) > 0) {
                    // 展期 计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    interest = surplusMoney.multiply(deferredRate).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * days), 4, RoundingMode.HALF_UP);
                }
            } else {// 未进行过利率调整
                // 判断到期日是否大于上一个还款日，小于当前还款日，如果是则利息分段计算
                if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(cmsInlSend.getEndDate(), lastDate) > 0) {
                    // 正常计算利息
                    Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlSend.getEndDate());
                    BigDecimal avgRat = cmsInlSend.getRate().divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
                    BigDecimal interest1 = surplusMoney.multiply(avgRat).multiply(BigDecimal.valueOf((days1)));
                    // 展期利息
                    Integer days2 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
                    avgRat = deferredRate.divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
                    BigDecimal interest2 = surplusMoney.multiply(avgRat).multiply(BigDecimal.valueOf((days2)));
                    interest = interest1.add(interest2);
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) <= 0) {
                    // 正常计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    interest = surplusMoney.multiply(cmsInlSend.getRate()).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * (days)), 4, RoundingMode.HALF_UP);
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(lastDate, cmsInlSend.getEndDate()) > 0) {
                    // 展期 计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    interest = surplusMoney.multiply(deferredRate).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * (days)), 4, RoundingMode.HALF_UP);
                }
            }
            repaymentSchedule.setRepaymentIst(interest);
            if (null != repayMoney) {
                surplusMoney = surplusMoney.subtract(repayMoney);
            } else {
                surplusMoney = surplusMoney.subtract(repayMoneyAndInterest.subtract(interest));
            }
            lastDate = repaymentSchedule.getPrediceRepayDate();
        }
    }
    public BigDecimal calInterest2(CmsInlAdjustrate cmsInlAdjustrate, Date lastDate, CmsInlSend cmsInlSend, BigDecimal surplusMoney, Date prediceRepayDate) {
        BigDecimal interest = BigDecimal.ZERO;
        if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), lastDate) >= 0 && DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) < 0) {
            // 上次还款日期<执行日<本次还款日期
            // 正常计算利息 分段，上段
            Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlAdjustrate.getExecDate());
            BigDecimal interest1 = surplusMoney.multiply(cmsInlSend.getRate()).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * days1), 4, RoundingMode.HALF_UP);
            // 正常计算利息 分段，下段
            Integer days2 = DateUtils.getOffsetDays(cmsInlAdjustrate.getExecDate(), prediceRepayDate);
            BigDecimal interest2 = surplusMoney.multiply(cmsInlAdjustrate.getAdjustRate()).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * days2), 4, RoundingMode.HALF_UP);
            interest = interest1.add(interest2);
        } else if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) < 0) {
            // 执行日>当前还款日期
            Integer days = DateUtils.getOffsetDays(lastDate, prediceRepayDate);
            interest = surplusMoney.multiply(cmsInlSend.getRate()).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * days), 4, RoundingMode.HALF_UP);
        } else if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) > 0) {
            // 执行日<当前还款日期
            Integer days = DateUtils.getOffsetDays(lastDate, prediceRepayDate);
            interest = surplusMoney.multiply(cmsInlAdjustrate.getAdjustRate()).divide(BigDecimal.valueOf((long) Integer.valueOf(cmsInlSend.getRateBase()) * days), 4, RoundingMode.HALF_UP);
        }
        return interest;
    }
    /**
     * 分期等额本息
     *
     * @param cmsRepaymentSchedule
     * @param cmsInlAdjustrate
     * @param cmsInlSend
     * @param repaymentSchedules
     * @param user
     * @return
     */
    public List<RepaymentSchedule> calRepay2(CmsRepaymentSchedule cmsRepaymentSchedule, CmsInlAdjustrate cmsInlAdjustrate, CmsInlSend cmsInlSend, List<RepaymentSchedule> repaymentSchedules, ICurrentUser user) {
        if (ListUtils.isNullOrEmpty(repaymentSchedules)) {
            return null;
        }
        BigDecimal qs = BigDecimal.ZERO;
        BigDecimal f1 = BigDecimal.ZERO;
        BigDecimal f2 = BigDecimal.ONE;
        BigDecimal sumf = BigDecimal.ZERO;
        int rateBase = Integer.valueOf(cmsInlSend.getRateBase());
        Date lastDate = repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(cmsInlSend.getId()).getRepayBeginDate();
        // 初始化还款开始日
        BigDecimal deferredRate = (null == cmsInlSend.getDeferredRate() ? cmsInlSend.getRate() : cmsInlSend.getDeferredRate());
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            // 是否调整过利率
            if (null != cmsInlAdjustrate) {
                // 判断到期日是否大于上一个还款日，小于当前还款日，如果是则利息分段计算
                if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(cmsInlSend.getEndDate(), lastDate) > 0) {
                    if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), lastDate) >= 0 && DateUtils.dayDiffs(cmsInlSend.getEndDate(), cmsInlAdjustrate.getExecDate()) > 0) {
                        // 正常
                        Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlAdjustrate.getExecDate());
                        // 调整
                        Integer days2 = DateUtils.getOffsetDays(cmsInlAdjustrate.getExecDate(), cmsInlSend.getEndDate());
                        // 展期
                        Integer days3 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
                        // qs=1+天数1*发放利率/计息基数+天数2*展期利率/计息基数==1+(天数1*发放利率+天数2*调整利率+天数3*展期利率)/计息基数
                        BigDecimal tmp1 = cmsInlSend.getRate().multiply(BigDecimal.valueOf(days1));
                        BigDecimal tmp2 = cmsInlAdjustrate.getAdjustRate().multiply(BigDecimal.valueOf(days2));
                        BigDecimal tmp3 = deferredRate.multiply(BigDecimal.valueOf(days3));
                        qs = BigDecimal.ONE.add((tmp1.add(tmp2).add(tmp3)).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                    } else {
                        // 正常
                        Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlSend.getEndDate());
                        // 展期
                        Integer days2 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
                        // qs=1+天数1*发放利率/计息基数+天数2*展期利率/计息基数==1+(天数1*发放利率+天数2*展期利率)/计息基数
                        qs = BigDecimal.ONE.add((BigDecimal.valueOf(days1).multiply(cmsInlSend.getRate()).add(BigDecimal.valueOf(days2).multiply(deferredRate))).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                    }
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) <= 0) {
                    // qs=1+天数*发放利率/计息基数
                    qs = calQs(cmsInlAdjustrate, lastDate, cmsInlSend, repaymentSchedule.getPrediceRepayDate());
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(lastDate, cmsInlSend.getEndDate()) > 0) {
                    // 展期 计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    // qs=1+天数*展期利率/计息基数
                    qs = BigDecimal.ONE.add(BigDecimal.valueOf(days).multiply(deferredRate).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                }
            } else {
                // 判断到期日是否大于上一个还款日，小于当前还款日，如果是则利息分段计算
                if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(cmsInlSend.getEndDate(), lastDate) > 0) {
                    // 正常
                    Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlSend.getEndDate());
                    // 展期
                    Integer days2 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
                    // qs=1+天数1*发放利率/计息基数+天数2*展期利率/计息基数
                    qs = BigDecimal.ONE.add((BigDecimal.valueOf(days1).multiply(cmsInlSend.getRate()).add(BigDecimal.valueOf(days2).multiply(deferredRate))).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) <= 0) {
                    // 正常计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    // qs=1+天数*发放利率/计息基数
                    qs = BigDecimal.ONE.add(BigDecimal.valueOf(days).multiply(cmsInlSend.getRate()).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                } else if (DateUtils.dayDiffs(repaymentSchedule.getPrediceRepayDate(), cmsInlSend.getEndDate()) > 0 && DateUtils.dayDiffs(lastDate, cmsInlSend.getEndDate()) > 0) {
                    // 展期 计算利息
                    Integer days = DateUtils.getOffsetDays(lastDate, repaymentSchedule.getPrediceRepayDate());
                    // qs=1+天数*展期利率/计息基数
                    qs = BigDecimal.ONE.add(BigDecimal.valueOf(days).multiply(deferredRate).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
                }
            }
            f1 = BigDecimal.ONE.divide(qs, 10, RoundingMode.HALF_UP);
            f2 = f1.multiply(f2);
            sumf = sumf.add(f2);
            lastDate = repaymentSchedule.getPrediceRepayDate();
        }
        // 利息和本金
        BigDecimal repayMoneyAndInterest = cmsInlSend.getSendMoney().divide(sumf, 2, RoundingMode.HALF_UP);
        // 利息计算
        calInterest(cmsInlAdjustrate, cmsInlSend, repaymentSchedules, null, repayMoneyAndInterest);
        BigDecimal surplusMoney = cmsInlSend.getSendMoney();// 剩余本金
        for (RepaymentSchedule repaymentSchedule : repaymentSchedules) {
            /**
             * 当前应还本金+利息
             */
            repaymentSchedule.setCurrentRepayMoney(repayMoneyAndInterest);
            /**
             * 当前应还本金
             */
            repaymentSchedule.setRepaymentMoney(repayMoneyAndInterest.subtract(repaymentSchedule.getRepaymentIst()));
            /**
             * 剩余本金
             */
            repaymentSchedule.setSurplusMoney(surplusMoney);
            surplusMoney = surplusMoney.subtract(repaymentSchedule.getRepaymentMoney());
        }
        return repaymentSchedules;
    }
    public BigDecimal calQs(CmsInlAdjustrate cmsInlAdjustrate, Date lastDate, CmsInlSend cmsInlSend, Date prediceRepayDate) {
        int rateBase = Integer.valueOf(cmsInlSend.getRateBase());
        BigDecimal qs = BigDecimal.ZERO;
        if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), lastDate) >= 0 && DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) < 0) {
            // 上次还款日期<执行日<本次还款日期
            // 正常计算利息 分段，上段
            Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlAdjustrate.getExecDate());
            // 正常计算利息 分段，下段
            Integer days2 = DateUtils.getOffsetDays(cmsInlAdjustrate.getExecDate(), prediceRepayDate);
            qs = BigDecimal.ONE.add((BigDecimal.valueOf(days1).multiply(cmsInlSend.getRate()).add(BigDecimal.valueOf(days2).multiply(cmsInlAdjustrate.getAdjustRate()))).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
        } else if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) < 0) {
            // 执行日>当前还款日期
            Integer days = DateUtils.getOffsetDays(lastDate, prediceRepayDate);
            qs = BigDecimal.ONE.add(BigDecimal.valueOf(days).multiply(cmsInlSend.getRate()).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
        } else if (DateUtils.dayDiffs(cmsInlAdjustrate.getExecDate(), prediceRepayDate) > 0) {
            // 执行日<当前还款日期
            Integer days = DateUtils.getOffsetDays(lastDate, prediceRepayDate);
            qs = BigDecimal.ONE.add(BigDecimal.valueOf(days).multiply(cmsInlAdjustrate.getAdjustRate()).divide(BigDecimal.valueOf(rateBase), 4, RoundingMode.HALF_UP));
        }
        return qs;
    }
    /**
     * 还本付息
     *
     * @param cmsInlAdjustrate
     * @param cmsInlSend
     * @param repaymentSchedules
     * @return
     */
    public List<RepaymentSchedule> calRepay3(CmsInlAdjustrate cmsInlAdjustrate, CmsInlSend cmsInlSend, List<RepaymentSchedule> repaymentSchedules) {
        RepaymentSchedule repaymentSchedule = repaymentSchedules.get(0);
        Date lastDate = repaymentSchedule.getPrediceRepayDate();
        BigDecimal avgRat = BigDecimal.ONE;
        BigDecimal interest = BigDecimal.ZERO;
        // 展期利率
        BigDecimal deferredRate = (null == cmsInlSend.getDeferredRate() ? cmsInlSend.getRate() : cmsInlSend.getDeferredRate());
        if (null != cmsInlAdjustrate) {// 利率调整
            // 正常计算利息分段,调整前
            Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlAdjustrate.getExecDate());
            avgRat = cmsInlSend.getRate().divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
            BigDecimal interest1 = cmsInlSend.getSendMoney().multiply(avgRat).multiply(BigDecimal.valueOf((days1)));
            // 正常计算利息分段,调整后
            Integer days2 = DateUtils.getOffsetDays(cmsInlAdjustrate.getExecDate(), cmsInlSend.getEndDate());
            avgRat = cmsInlAdjustrate.getAdjustRate().divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
            BigDecimal interest2 = cmsInlSend.getSendMoney().multiply(avgRat).multiply(BigDecimal.valueOf((days2)));
            interest = interest1.add(interest2);
        } else {// 未进行利率调整
            // 正常计算利息分段,调整前
            Integer days1 = DateUtils.getOffsetDays(lastDate, cmsInlSend.getEndDate());
            avgRat = cmsInlSend.getRate().divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
            interest = cmsInlSend.getSendMoney().multiply(avgRat).multiply(BigDecimal.valueOf((days1)));
        }
        if (CommonConstant.YES.equals(cmsInlSend.getIsDeferred())) {
            // 展期利息
            Integer days3 = DateUtils.getOffsetDays(cmsInlSend.getEndDate(), repaymentSchedule.getPrediceRepayDate());
            avgRat = deferredRate.divide(BigDecimal.valueOf(Integer.valueOf(cmsInlSend.getRateBase())), 4, RoundingMode.HALF_UP);
            interest = interest.add(cmsInlSend.getSendMoney().multiply(avgRat).multiply(BigDecimal.valueOf((days3))));
        }
        repaymentSchedule.setRepaymentIst(interest);
        repaymentSchedule.setRepaymentMoney(cmsInlSend.getSendMoney());
        repaymentSchedule.setCurrentRepayMoney(interest.add(cmsInlSend.getSendMoney()));
        repaymentSchedule.setSurplusMoney(BigDecimal.ZERO);
        repaymentSchedules.clear();
        repaymentSchedules.add(repaymentSchedule);
        return repaymentSchedules;
    }
    /**
     * @param startDate  还款开始日期
     * @param cmsInlSend
     * @return
     */
    public List<RepaymentSchedule> getListRepayPlan(Date startDate, CmsInlSend cmsInlSend, CmsRepaymentSchedule cmsRepaymentSchedule, ICurrentUser user) {
        List<RepaymentSchedule> repaymentSchedules = new ArrayList<>();
        Date deferredDate = cmsInlSend.getEndDate();
        if (CommonConstant.YES.equals(cmsInlSend.getIsDeferred())) {
            deferredDate = cmsInlSend.getDeferredEndDate();
        }
        while (DateUtils.dayDiffs(deferredDate, startDate) > 0) {
            RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
            /**
             * 还款周期 0=按月 1=按季 2=半年
             */
            if ("0".equals(cmsRepaymentSchedule.getRepayCycel())) {
                startDate = DateUtils.addMonth(startDate, 1);
            } else if ("1".equals(cmsRepaymentSchedule.getRepayCycel())) {
                startDate = DateUtils.addMonth(startDate, 3);
            } else if ("2".equals(cmsRepaymentSchedule.getRepayCycel())) {
                startDate = DateUtils.addMonth(startDate, 6);
            }
            // 设置本次还款日期
            repaymentSchedule.setPrediceRepayDate(startDate);
            InlUtil.setRepaymentSchedule(user);
            repaymentSchedules.add(repaymentSchedule);
        }
        if (ListUtils.isNotNullOrEmpty(repaymentSchedules)) {
            repaymentSchedules.get(repaymentSchedules.size() - 1).setPrediceRepayDate(deferredDate);
        }
        return repaymentSchedules;
    }
    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateDao.get(id);
        cmsInlAdjustrate.setStatus(InlConstant.DELETE);
        cmsInlAdjustrate.setBizStatus(InlConstant.DELETE);
        inlAdjustrateDao.update(cmsInlAdjustrate);
    }
    /**
     * 查询审批通过的利率调整记录
     *
     * @return
     */
    public List<CmsInlAdjustrateVO> getCmsInlAdjustrateVOBySendId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("inlSendId", id);
        return inlAdjustrateDao.getCmsInlAdjustrateVOBySendId(params);
    }
    /**
     * 查询审批通过的利率调整记录
     *
     * @param id
     * @return
     */
    public List<RateAdjustBean> getRateAdjustBeanVOBySendId(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("inlSendId", id);
        return inlAdjustrateDao.getRateAdjustBeanVOBySendId(params);
    }
    /**
     * 查询最新审批通过的利率调整记录
     *
     * @param sendId
     * @return
     */
    public CmsInlAdjustrate listCmsInlAdjustrate(String sendId) {
        List<CmsInlAdjustrate> cmsInlAdjustrates = inlAdjustrateDao.listCmsInlAdjustrate(sendId);
        if (ListUtils.isNotNullOrEmpty(cmsInlAdjustrates)) {
            return cmsInlAdjustrates.get(0);
        }
        return null;
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
    }
}
