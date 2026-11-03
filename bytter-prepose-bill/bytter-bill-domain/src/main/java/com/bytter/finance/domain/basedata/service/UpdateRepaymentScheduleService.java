package com.bytter.finance.domain.basedata.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.dto.RepayPlanDTO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.enums.ProvideMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: 还款计划子表
 * @author onlineGenerator
 * @date 2017-08-01 09:58:43
 * @version 1.0
 *
 */
@Service("updateRepaymentScheduleService")
@Transactional
public class UpdateRepaymentScheduleService extends BaseService<RepaymentSchedule, String> {
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private RepayTypeService repayTypeService;
    /**
     * 保存还款计划
     *
     * @param rp
     */
    public String updateRepayPlan(RepayPlanVO rp, ICurrentUser user) throws ParseException {
        String id = "";
        if (StringUtils.isNotEmpty(rp.getId())) {
            // 先根据id号删除之前的还款计划
            deleteRepaymentSchedule(rp.getId());
            // 根据单号查询还款计划主表id
            CmsRepaymentSchedule cms = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(rp.getId());
            RepayType repayType = repayTypeService.get(rp.getRepayType());
            if (repayType.getCapitalFrequencyType() != "N") {
                if (cms != null) {
                    // 更新还款计划主表信息
                    cms.setRepayType(rp.getRepayType()); // 还款方式
                    cms.setRepayCycel(rp.getRepayCycel());// 还款周期
                    if (!EmptyUtils.isEmpty(rp.getRepayBeginDate())) {
                        cms.setRepayBeginDate(DateUtils.parseDate(rp.getRepayBeginDate(), "yyyy-MM-dd"));// 还款开始日期
                    }
                    cmsRepaymentScheduleService.saveOrUpdate(cms);
                    // 保存还款计划子表
                    id = cms.getId();
                    subSave(id, rp, user);
                } else {
                    // 新增还款计划主表
                    CmsRepaymentSchedule cs = new CmsRepaymentSchedule();
                    cs.setBillNo(rp.getId());
                    cs.setStatus(CmsConstant.IS_TRUE);
                    // 还款方式
                    cs.setRepayType(rp.getRepayType());
                    // 还款周期
                    cs.setRepayCycel(rp.getRepayCycel());
                    cs.setRepayBeginDate(DateUtils.parseDate(rp.getRepayBeginDate()));// 还款开始日期
                    // cs.setCmsType(CmsConstant.);
                    cmsRepaymentScheduleService.save(cs);
                    // 新增还款计划子表
                    id = cs.getId();
                    subSave(id, rp, user);
                }
                // 借款
                if (rp.getType().equals(CmsConstant.CREDITTYPE_0)) {
                    cmsProvideLoanSendService.updateRepayType(rp.getId(), rp.getRepayType());
                    ProvideLoanSend send = cmsProvideLoanSendService.get(rp.getId());
                    send.setRepayType(rp.getRepayType());
                    cmsProvideInterestService.adjustProvideInterest(send, user);
                }
                // 贷款
                if (rp.getType().equals(CmsConstant.CREDITTYPE_1)) {
                    cmsLoanSendService.updateRepayType(rp.getId(), rp.getRepayType());
                }
                // 委托贷款
                if (rp.getType().equals(CmsConstant.CREDITTYPE_2)) {
                    cmsPrecativeLoanSendService.updateRepayType(rp.getId(), rp.getRepayType());
                }
            } else {
                // 借款
                if (rp.getType().equals(CmsConstant.CREDITTYPE_0)) {
                    cmsProvideLoanSendService.updateRepayType(rp.getId(), rp.getRepayType());
                    ProvideLoanSend send = cmsProvideLoanSendService.get(rp.getId());
                    send.setRepayType(rp.getRepayType());
                    // 删除未还测算利息,重新生成
                    cmsProvideInterestService.deleteProvideInterest(send);
                }
            }
        }
        return id;
    }
    /**
     * 根据单号删除之前的还款计划(逻辑删除)
     */
    public void deleteRepaymentSchedule(String billCode) {
        repaymentScheduleService.deleteRepaymentSchedule(billCode);
    }
    /**
     * 保存还款计划子表
     *
     * @param id
     */
    public void subSave(String id, RepayPlanVO rp, ICurrentUser user) {
        if (EmptyUtils.isEmptyList(rp.getRepayMents())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY);
        }
        for (int i = 0; i < rp.getRepayMents().size(); i++) {
            RepayPlanDTO rs = rp.getRepayMents().get(i);
            if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(rs.getIsRepay())) {
                continue;
            }
            // 还款计划明细表
            RepaymentSchedule entity = new RepaymentSchedule();
            if (rs.getPrediceRepayDate() != null) {
                entity.setBillCode(rp.getId());// 单号
                entity.setRepayCycel(rp.getRepayCycel());// 还款周期
                entity.setPrediceRepayDate(DateUtils.str2Date(rs.getPrediceRepayDate(), DateUtils.date_sdf()));// 预计还款日
                entity.setRepaymentBeginDate(DateUtils.str2Date(rp.getRepayBeginDate(), DateUtils.date_sdf()));// 还款开始日
                entity.setRepaymentMoney(rs.getRepaymentMoney());// 还款金额
                entity.setRepaymentIst(rs.getRepaymentIst());// 利息
                entity.setCurrentRepayMoney(rs.getCurrentRepayMoney());// 本期还款金额
                entity.setSurplusMoney(rs.getSurplusMoney());// 剩余本金
                entity.setIsRepay(rs.getIsRepay());// 是否已还
                entity.setRemark(rs.getRemark());// 备注
                entity.setCreateBy(user.getLoginName());
                entity.setCreateName(user.getUsername());
                entity.setCreateDate(new Date());
                entity.setValid_Sign(CmsConstant.VALIDSIGN);// 有效标志 1为有效 -2为无效
                entity.setInternalSign(rp.getInternalSign());
                entity.setRelationId(id);
                repaymentScheduleService.saveOrUpdate(entity);
            }
        }
    }
    /**
     * 功能介绍：根据id号获取还款计划<br>
     *
     * @param
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listUpdateRepayPlan(String id) {
        List<RepaymentScheduleVO> rsList = repaymentScheduleService.getRepayMentScheduleByBillCode(id);
        if (ListUtils.isNullOrEmpty(rsList)) {
            rsList = new ArrayList<RepaymentScheduleVO>();
            RepaymentScheduleVO rs = new RepaymentScheduleVO();
            rs.setIsRepay(CmsConstant.IS_FALSE);
            rsList.add(rs);
        }
        return rsList;
    }
    /**
     * 根据单号查询信息
     *
     * @param repayPlanVO
     * @return
     */
    @Transactional(readOnly = true)
    public RepayPlanVO toRepayPlanUpdate(RepayPlanVO repayPlanVO) {
        if (CmsConstant.CREDITTYPE_0.equals(repayPlanVO.getType())) {
            cmsProvideLoanSend(repayPlanVO);
        }
        if (CmsConstant.CREDITTYPE_1.equals(repayPlanVO.getType())) {
            cmsLoanSend(repayPlanVO);
        }
        if (CmsConstant.CREDITTYPE_2.equals(repayPlanVO.getType())) {
            cmsPrecativeLoanSend(repayPlanVO);
        }
        return repayPlanVO;
    }
    /**
     * <br>
     * <b>功能：</b> 根据还款计划获取本次开始的还款日期 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/9 0009 <br>
     */
    @Transactional(readOnly = true)
    public String getMaxRepayedDate(RepayPlanVO repayPlanVO) {
        String loanDateStr = repayPlanVO.getLoanDate();// 发放日期
        String billCode = repayPlanVO.getBillCode();// 获取业务单号
        Date loanDate = DateUtils.str2Date(loanDateStr, DateUtils.date_sdf);
        Date startDate = cmsProvideInterestService.getMaxRepayedDate(billCode, loanDate);
        return DateUtils.format(startDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
    }
    /**
     * 查询借款发放数据
     *
     * @return
     */
    public RepayPlanVO cmsProvideLoanSend(RepayPlanVO repayPlanVO) {
        LoanSendVO send = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(repayPlanVO.getBillCode());// 查询发放vo
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        CmsRepaymentSchedule cmsRepaymentSchedule = repaymentScheduleService
                .getCmsRepaymentScheduleByBillCode(send.getId());
        info.getRateBase();
        // repayPlanVO.setRateTypse("Y");
        repayPlanVO.setPlanId(cmsRepaymentSchedule.getId());
        repayPlanVO.setLoanRate(send.getPlRate());// 借款利率
        repayPlanVO.setLoanDate(DateUtils.date2Str(send.getSendDate(), DateUtils.date_sdf()));// 发放日期
        repayPlanVO.setRepayDate(DateUtils.date2Str(send.getRepayDate(), DateUtils.date_sdf()));// 还款日期
        repayPlanVO.setRepayType(send.getRepayType());// 还款方式 CmsConstant.REPAY_TYPE_03之前的
        repayPlanVO.setLoanMoney(send.getMoney());// 发放金额
        repayPlanVO.setRepayDate(DateUtils.date2Str(send.getRepayDate(), DateUtils.date_sdf()));// 还款日期
        if (EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            repayPlanVO.setRepayBeginDate(DateUtils.date2Str(send.getSendDate(), DateUtils.date_sdf()));// 还款开始日期
            repayPlanVO.setRepayCycel(info.getRepayCycle());
        } else {
            repayPlanVO.setRepayBeginDate(getMaxRepayedDate(repayPlanVO));
            // repayPlanVO.setRepayBeginDate(DateUtils.date2Str(cmsRepaymentSchedule.getRepayBeginDate(),
            // DateUtils.date_sdf()));// 还款开始日期
            repayPlanVO.setRepayCycel(cmsRepaymentSchedule.getRepayCycel());
        }
        repayPlanVO.setId(send.getId());
        repayPlanVO.setRateBase(info.getRateBase());
        repayPlanVO.setDeferredRepayDate(DateUtils.date2Str(send.getDeferredDate(), DateUtils.date_sdf()));
        return repayPlanVO;
    }
    /**
     * 查询贷款发放数据
     *
     * @return
     */
    public RepayPlanVO cmsLoanSend(RepayPlanVO repayPlanVO) {
        CmsLoanSend send = cmsLoanSendService.getLoanSendByBillCode(repayPlanVO.getBillCode());
        CmsRepaymentSchedule cmsRepaymentSchedule = repaymentScheduleService
                .getCmsRepaymentScheduleByBillCode(send.getId());
        // repayPlanVO.setRateTypse("Y");
        repayPlanVO.setPlanId(cmsRepaymentSchedule.getId());
        repayPlanVO.setLoanRate(send.getLoanRate());// 放款利率
        repayPlanVO.setLoanDate(DateUtils.date2Str(send.getLoanDate(), DateUtils.date_sdf()));// 发放日期
        repayPlanVO.setRepayDate(DateUtils.date2Str(send.getRepayDate(), DateUtils.date_sdf()));// 还款日期
        repayPlanVO.setRepayType(send.getRepayType()); // 还款方式
        if (!EmptyUtils.isEmpty(cmsRepaymentSchedule)) {
            repayPlanVO.setRepayCycel(cmsRepaymentSchedule.getRepayCycel()); // 还款周期
            repayPlanVO.setRepayBeginDate(
                    DateUtils.date2Str(cmsRepaymentSchedule.getRepayBeginDate(), DateUtils.date_sdf()));// 还款开始日期
        } else {
            repayPlanVO.setRepayBeginDate(DateUtils.date2Str(send.getLoanDate(), DateUtils.date_sdf()));// 还款开始日期
        }
        repayPlanVO.setLoanMoney(send.getLoanMoney());// 发放金额
        repayPlanVO.setId(send.getId());
        repayPlanVO.setDeferredRepayDate(DateUtils.date2Str(send.getDeferredDate(), DateUtils.date_sdf()));
        return repayPlanVO;
    }
    /**
     * 查询委托贷款发放数据
     *
     * @return
     */
    public RepayPlanVO cmsPrecativeLoanSend(RepayPlanVO repayPlanVO) {
        CmsPrecativeLoanSend send = cmsPrecativeLoanSendService.get(repayPlanVO.getBillCode());
        CmsRepaymentSchedule cmsRepaymentSchedule = repaymentScheduleService
                .getCmsRepaymentScheduleByBillCode(send.getId());
        // repayPlanVO.setRateTypse("Y");
        repayPlanVO.setLoanRate(send.getRate());// 借款利率
        repayPlanVO.setLoanDate(DateUtils.date2Str(send.getSendDate(), DateUtils.date_sdf()));// 发放日期
        repayPlanVO.setRepayDate(DateUtils.date2Str(send.getDeadlineDate(), DateUtils.date_sdf()));// 还款日期
        repayPlanVO.setRepayType(send.getRepayType());// 还款方式
        if (ObjectUtil.isNotEmpty(cmsRepaymentSchedule)) {
            repayPlanVO.setPlanId(cmsRepaymentSchedule.getId());
            repayPlanVO.setRepayCycel(cmsRepaymentSchedule.getRepayCycel());// 还款周期
            repayPlanVO.setRepayBeginDate(DateUtils.date2Str(cmsRepaymentSchedule.getRepayBeginDate(), DateUtils.date_sdf()));// 还款开始日期
        } else {
            repayPlanVO.setRepayBeginDate(DateUtils.date2Str(send.getSendDate(), DateUtils.date_sdf()));// 还款开始日期
        }
        repayPlanVO.setLoanMoney(send.getMoney());// 发放金额
        repayPlanVO.setId(send.getId());
        repayPlanVO.setBillCode(send.getBillCode());
        repayPlanVO.setDeferredRepayDate(DateUtils.date2Str(send.getDeferredDate(), DateUtils.date_sdf()));
        return repayPlanVO;
    }
    @Override
    public BaseDao<RepaymentSchedule, String> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }
}
