package com.bytter.finance.domain.loan.service;

import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.CmsLoanSendOutCheckVO;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.loan.dao.CmsLoanDeferredDao;
import com.bytter.finance.domain.loan.dao.CmsLoanInfoDao;
import com.bytter.finance.domain.loan.dao.CmsLoanRepayDao;
import com.bytter.finance.domain.loan.dao.CmsLoanSendDao;
import com.bytter.finance.domain.loan.entity.*;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendTallyQueryVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款发放表
 * @date 2017-07-28 11:27:00
 */
@WorkFlow(CmsWorkFlowConstant.CMS_LOAN_SEND)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LOAN_SEND)
@Service("cmsLoanSendService")
@Transactional
public class CmsLoanSendService extends BaseService<CmsLoanSend, String>
        implements IWorkFlowListener, ITaskService, ICorpDisableVerifyService {
    @Autowired
    private CmsLoanSendDao cmsLoanSendDao;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private CmsLoanInfoDao cmsLoanInfoDao;
    @Autowired
    private CmsLoanRepayDao cmsLoanRepayDao;
    @Autowired
    private CmsLoanDeferredDao cmsLoanDeferredDao;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private RepayTypeService repayTypeService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private ConCommonService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsLoanSend entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsLoanSend entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsLoanSend entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsLoanSend entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<CmsLoanSend, String> getEntityDao() {
        return cmsLoanSendDao;
    }

    /**
     * <b>功能介绍：</b>根据billCode<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanSend getLoanSendByBillCode(String billCode) {
        return cmsLoanSendDao.getCmsLoanSendByBillCode(billCode);
    }

    /**
     * <b>功能介绍：</b>生成贷款发送票据号<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getLoanSendBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_LOANSEND_ORDER_NO, prefix);
    }

    /**
     * <b>功能介绍：</b>根据发放信息查找申请信息<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanInfoVO getLoanInfoByLoanSend(CmsLoanSend cmsLoanSend) {
        CmsLoanInfoVO cmsLoanInfoVO = cmsLoanInfoDao.getCmsLoanInfoById(cmsLoanInfoDao.findByBillCode(cmsLoanSend.getLoanCode()).getId());
        //TODO 统一处理期限与利率
        Optional.ofNullable(cmsLoanInfoVO).ifPresent(cmsLoanInfo -> {
            if (StringUtils.isNotEmpty(cmsLoanInfo.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsLoanInfo.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> cmsLoanInfoVO.setReferenceRateTypeName(rate.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(cmsLoanInfo.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsLoanInfo.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(period -> cmsLoanInfoVO.setRepayCycleName(period.getPeriodName()));
            }
        });
        return cmsLoanInfoVO;
    }

    /**
     * <b>功能介绍：</b>根据id查询发放数据<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanSendVO getCmsLoanSendById(String id) {
        CmsLoanSendVO cmsLoanSendVO = cmsLoanSendDao.getCmsLoanSendById(id);
        //TODO 统一处理期限与利率
        Optional.ofNullable(cmsLoanSendVO).ifPresent(cmsLoanSend -> {
            if (StringUtils.isNotEmpty(cmsLoanSend.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsLoanSend.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> cmsLoanSendVO.setReferenceRateTypeName(rate.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(cmsLoanSend.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsLoanSend.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(period -> cmsLoanSendVO.setRepayCycleName(period.getPeriodName()));
            }
        });
        return cmsLoanSendVO;
    }

    /**
     * <b>功能介绍：</b>根据发放billCode查询发放数据<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanSendVO getCmsLoanSendVOByBillCode(String billCode) {
        return cmsLoanSendDao.getCmsLoanSendVOByBillCode(billCode);
    }

    /**
     * <b>功能介绍：</b>根据发放编号查询还款明细<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanRepay> getCmsLoanRepayBySendCode(String sendCode, String type) {
        return cmsLoanRepayDao.getCmsLoanRepayBySendCode(sendCode, type);
    }

    /**
     * <b>功能介绍：</b>放款申请表查询列表<br>
     * <b>日期：</b>2017年7月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listCmsLoanSend(DataTablesInput input, String pageType) {
        return cmsLoanSendDao.listCmsLoanSend(input, pageType);
    }

    /**
     * <b>功能介绍：</b>贷款合同生成列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput loanContractList(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_CONTRACT_QUERY, userId);
        DataTablesOutput dataTablesOutput = cmsLoanSendDao.loanContractList(input, corpAuth);
        List<CmsLoanSendVO> list = dataTablesOutput.getData();
        for (CmsLoanSendVO vo : list) {
            if (StringUtils.isNotEmpty(vo.getCorpId())) {
                String corpName = iCorpService.getCorpName(vo.getCorpId());
                vo.setCorpName(corpName);
            }
            if (StringUtils.isNotEmpty(vo.getNetId())) {
                String netName = iCorpService.getCorpName(vo.getNetId());
                vo.setNetName(netName);
            }
            if (StringUtils.isNotEmpty(vo.getRepayType())) {
                RepayTypeVO repayType = repayTypeService.getRepayTypeInfoById(vo.getRepayType());
                if (!EmptyUtils.isEmpty(repayType)) {
                    vo.setRepayTypeName(repayType.getRepayName());
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * <b>功能介绍：</b>贷款还款申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput loanRepayList(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_REPAY_QUERY, userId);
        DataTablesOutput output = cmsLoanSendDao.loanRepayList(input, corpAuth);
        for (CmsLoanSendVO sendVO : (List<CmsLoanSendVO>) output.getData()) {
            setNewSchedule(sendVO);// 取最近一期未还的还款计划与对应还款方式
        }
        return output;
    }

    public List<CmsLoanSendVO> getAutoDataAppTables() {
        return cmsLoanSendDao.getAutoDataAppTables();
    }

    /*
     * 取最近一期未还的还款计划与还款方式
     */
    public void setNewSchedule(CmsLoanSendVO sendVO) {
        List<RepaymentScheduleVO> scheduleVOs = repaymentScheduleService.getRepayScheduleByBillCode(sendVO.getId());
        if (!EmptyUtils.isEmpty(scheduleVOs)) {
            sendVO.setRepaymentScheduleVO(scheduleVOs.get(0));
        }
        if (StringUtils.isNotEmpty(sendVO.getRepayType())) {
            RepayTypeVO repayTypeVO = repayTypeService.getRepayTypeInfoById(sendVO.getRepayType());
            if (!EmptyUtils.isEmpty(repayTypeVO)) {
                sendVO.setRepayTypeName(repayTypeVO.getRepayName());
            }
        }
    }

    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput dataRepayPlanable(DataTablesInput input) {
        DataTablesOutput output = cmsLoanSendDao.dataRepayPlanable(input);
        for (CmsLoanSendVO sendVO : (List<CmsLoanSendVO>) output.getData()) {
            setNewSchedule(sendVO);// 取最近一期未还的还款计划与对应还款方式
        }
        return output;
    }

    /**
     * <b>功能介绍：</b>发放审批列表数据<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput listSendApproveData(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LOAN_SEND,
                dataTablesInput);
        DataTablesOutput dp = cmsLoanSendDao.dataTableApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }

    /**
     * <b>功能介绍：</b>贷款展期申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput loanDeferredList(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_DEFERRED_QUERY, userId);
        DataTablesOutput result = cmsLoanSendDao.loanDeferredList(input, isMoreDeferred(), corpAuth);
        List<CmsLoanSendVO> cmsLoanSends = result.getData();
        for (CmsLoanSendVO cmsLoanSend : cmsLoanSends) {
            List<CmsLoanDeferred> deferred = cmsLoanDeferredDao.getCmsLoanDeferredByLiCode(cmsLoanSend.getBillCode());
            int num = deferred.size();
            cmsLoanSend.setDeferredNum(num);// 设置展期次数
            if (num > 0) {
                if (num >= 2) {// 设置上期贷款展期日期
                    cmsLoanSend.setLastDeferredDate(deferred.get(num - 2).getDeferredRepayDate());
                } else {
                    // cmsLoanSend.setLastDeferredDate(deferred.get(0).getMatureDate());
                }
                cmsLoanSend.setDeferredReplayDate(deferred.get(num - 1).getDeferredRepayDate());// 设置展期到期日期
            }
            if (StringUtils.isNotEmpty(cmsLoanSend.getRepayType())) {
                RepayTypeVO repayType = repayTypeService.getRepayTypeInfoById(cmsLoanSend.getRepayType());
                if (!EmptyUtils.isEmpty(repayType)) {
                    cmsLoanSend.setRepayTypeName(repayType.getRepayName());
                }
            }
        }
        result.setData(cmsLoanSends);
        return result;
    }

    /**
     * <b>功能介绍：</b>保存发放信息<br>
     * <b>日期：</b>2017年7月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void saveLoanSendInfo(CmsLoanSend entity, ICurrentUser user, String sendBillCode) {
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoDao.findByBillCode(entity.getLoanCode());
        //TODO 核算现在不通
        Date tallyDate = fcDayCloseService.getDayCloseFinalDate(cmsLoanInfo.getNetCode());// 获取网点日结日期
        if (cmsLoanSendDao.existsContractCode(entity.getContractCode())) {// 判断合同号是否已存在
            throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_CONTRACTCODE_EXISTS);
        }
        if (cmsLoanSendDao.validateSendStatus(cmsLoanInfo.getBillCode())) {// 验证当前贷款是否存在未生成合同的有效发放记录
            throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_FAILE);
        }
        if (cmsLoanSendDao.validateSendTime(cmsLoanInfo.getBillCode(), entity.getSendTime())) {// 验证当前发放次数是否正确
            throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_SENDTIME_EXISTS,
                    String.valueOf(entity.getSendTime()));
        }
        if (isMoreLoanMoney(cmsLoanInfo.getBillCode(), entity.getLoanMoney(), cmsLoanInfo.getLoanMoney())) {// 判断发放金额是否超出贷款金额
            throw new ParameterException(LoanMessageEnum.CMS_LOANSEND_MONEY_ISMORE);
        }
        // TODO 核算的暂时不动
        if (isTally()) {
            if (DateUtils.dayDiffs(entity.getLoanDate(), tallyDate) < 0) {// 发放日期已日结
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDDATE_PASS_CLOSEDATE,
                        iCorpService.get(cmsLoanInfo.getNetCode()).getName(), DateUtils.formatDate(tallyDate));
            }
        }
        cmsLoanInfo.setLoanSendOutType(entity.getLoanSendOutType());// 设置发放方式
        cmsLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_35);// 发放待审批
        cmsLoanInfo.setRepayType(entity.getRepayType());
        cmsLoanInfoDao.update(cmsLoanInfo);
        entity.setCreateBy(user.getId());
        entity.setCreateName(user.getUsername());
        entity.setBizStatus(CmsConstant.BUSINESSSTATE_32);
        CmsLoanSend send = null;
        String planId = null;
        String id = null;
        if (EmptyUtils.isNotEmpty(sendBillCode) && !sendBillCode.equals("null")) {
            send = cmsLoanSendDao.getCmsLoanSendByBillCode(sendBillCode);
            BeanUtils.copyNotNullProperties(entity, send);
            planId = send.getPlanId();
            id = send.getId();
            update(send);
        } else {
            if (entity.getCreateDate() == null) {
                entity.setCreateDate(new Date());
            }
            super.saveEntity(entity);
            planId = entity.getPlanId();
            id = entity.getId();
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_SEND, cmsLoanInfo.getId(), user.getId());
        notificationService.complete(NotificationConstant.CMS.LOAN_SEND, id, user.getId());
        if (EmptyUtils.isNotEmpty(planId)) {
            repaymentScheduleDao.updateRepayMentBill(planId, CmsConstant.LOAN_SEND_BILL_CODE_PREFIX, id);// 更新还款计划的ID和类型
        } else {
            throw new ParameterException(LoanMessageEnum.CMS_LOANAPPLY_PLAN_ISNULL);
        }
        if (EmptyUtils.isNotEmpty(sendBillCode) && !sendBillCode.equals("null")) {
            //TODO 审批先不走
            loanSendApply(send, user);// 启动审批
            msService.insertProcessMsg(send.getBillCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_SEND_SUBMIT, user);
        } else {
            //TODO 审批先不走
            loanSendApply(entity, user);// 启动审批
            msService.insertProcessMsg(entity.getBillCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_SEND_SUBMIT, user);
        }
    }

    /**
     * <b>功能介绍：</b>启动发放审批流<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void loanSendApply(CmsLoanSend send, ICurrentUser user) {
        getStartHandler().handler(CmsWorkFlowConstant.CMS_LOAN_SEND, send.getId(), send, send.getCorpId(), user.getId());
    }

    /**
     * <b>功能介绍：</b>查询放款金额是否超出贷款总金额<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean isMoreLoanMoney(String loanCode, BigDecimal sendMoney, BigDecimal loanMoney) {
        BigDecimal repayedMoney = cmsLoanSendDao.getSendedMoney(loanCode);
        if (repayedMoney.add(sendMoney).compareTo(loanMoney) > 0) {
            return true;
        }
        return false;
    }

    /**
     * <b>功能介绍：</b>根据贷款申请编号获取放款次数<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public Integer getCmsLoanSendCountByloanCode(String loanCode) {
        return cmsLoanSendDao.getCmsLoanSendCountByloanCode(loanCode);
    }

    /**
     * <b>功能介绍：</b>获取在途金额<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getInwayMoney(String loanCode) {
        return cmsLoanRepayDao.getInwayMoney(loanCode);
    }

    @Transactional(readOnly = true)
    public BigDecimal getMobileInwayMoney(String sendCode) {
        return cmsLoanRepayDao.getMobileInwayMoney(sendCode);
    }

    /**
     * <b>功能介绍：</b>获取贷款已还款金额<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getRepayedMoney(String loanCode) {
        return cmsLoanRepayDao.getRepayedMoney(loanCode);
    }

    /**
     * <b>功能介绍：</b>本次发放已还款金额<br>
     * <b>日期：</b>2017年10月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getSendRepayedMoney(String sendCode) {
        return cmsLoanRepayDao.getSendRepayedMoney(sendCode);
    }

    /**
     * 审批开始回调
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }

    /**
     * 审批处理回调
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsLoanSend send = this.get(businessKey, true);
        Integer status = result ? CmsConstant.APPROVED : CmsConstant.DELETED;// 更新发放单据状态
        String bizStatus = result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37;
        send.setStatus(status);
        send.setBizStatus(bizStatus);
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoDao.findByBillCode(send.getLoanCode());
        if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(send.getLoanSendOutType())) {// 一次性发放
            cmsLoanInfo.setBizStatus(result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37);// 合同待生成
        } else {
            cmsLoanInfo.setBizStatus(result ? CmsConstant.BUSINESSSTATE_32 : CmsConstant.BUSINESSSTATE_37);// 放款中
        }
        cmsLoanInfoDao.update(cmsLoanInfo);
        this.saveOrUpdate(send);
        if (result) {
            // 自动生成合同
            ConfigDTO config = cmsLoanInfoService.getagAutoHandle();// 自动生成合同
            if (config.getValue().equals("1")) {
                CmsContract cmsContract = new CmsContract();
                cmsContract.setContractCode(send.getContractCode());
                cmsContract.setCorpId(send.getCorpId());
                cmsContract.setLiCode(send.getBillCode());
                cmsLoanInfoService.autoHandleContract(cmsContract, user);
            } else {
                notificationService.send(businessKey, send.getCorpId(), NotificationConstant.CMS.LOAN_CONTRACT, send);
            }
            // 如果对接了合同管理生成履约信息
            this.createSegmentInfo(cmsLoanInfo, send);
            msService.insertProcessMsg(send.getBillCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_SEND_CHECKOK, user);
        } else {
            msService.insertProcessMsg(send.getBillCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_SEND_CHECKBACK, user);
        }
    }

    /**
     * 更据id修改还款方式
     */
    public void updateRepayType(String id, String repayType) {
        cmsLoanInfoDao.updateRepayType(id, repayType);
    }

    /**
     * <b>功能介绍：</b>是否多次展期<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    public String isMoreDeferred() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.INNERLOANDFNUM).toString();
    }

    /**
     * <b>功能介绍：</b>查询内部贷款发放到期<br>
     * <b>日期：</b>2017年11月16日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param endDate
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanSendOutCheckVO> getLoanSendOutInfoByEndDate(String netId, Date endDate) {
        return cmsLoanSendDao.getLoanSendOutInfoByEndDate(netId, endDate);
    }

    /**
     * <b>功能介绍：</b>定时任务，发放到期提醒<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param arguments
     * @version 1.0
     */
    @XxlJob("CmsLoanSendService")
    public void run(String arguments) {
        String jobParam = XxlJobHelper.getJobParam();
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, jobParam);
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        List<CmsLoanSend> sends = cmsLoanSendDao.getSendExpirationWarnData(endDate);
        if (!EmptyUtils.isEmptyList(sends)) {
            for (CmsLoanSend send : sends) {
                CmsLoanSendVO sendVo = new CmsLoanSendVO();
                BeanUtils.copyProperties(send, sendVo);
                CorpDTO corpDTO = corpService.get(sendVo.getCorpId());
                if (corpDTO != null && corpDTO.getName() != null) {
                    sendVo.setCorpName(corpDTO.getName());
                }
                sendVo.setRepayDateStr(DateUtil.formatDate(send.getRepayDate(), "yyyy-MM-dd"));
                notificationService.send(send.getId(), send.getCorpId(), NotificationConstant.CMS.LOAN_MATURE, sendVo);
                send.setSendSign(CommonConstant.SEND_SIGN_YES);
                this.update(send);
            }
        }
    }

    public DataTablesOutput getLoanTallyList(DataTablesInput input, String opType, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds("fcl:loan:tally:apply", userId);
        DataTablesOutput dataTablesOutput = cmsLoanSendDao.getLoanTallyList(input, opType, userId, corpAuth);
        List<CmsLoanSendTallyQueryVO> list = dataTablesOutput.getData();
        ArrayList<CmsLoanSendTallyQueryVO> cmsLoanSendTallyQueryVOs = new ArrayList<>();
        for (Object datum : list) {
            CmsLoanSendTallyQueryVO vo = (CmsLoanSendTallyQueryVO) datum;
            String netName = corpService.getCorpName(vo.getNetCode());
            String corpName = corpService.getCorpName(vo.getCorpId());
            vo.setNetName(netName);
            vo.setCorpName(corpName);
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(vo.getCurCode());
            vo.setCurName(currencyDTO.getCurName());
            cmsLoanSendTallyQueryVOs.add(vo);
        }
        dataTablesOutput.setData(cmsLoanSendTallyQueryVOs);
        return dataTablesOutput;
    }

    /**
     * <b>功能介绍：</b>是否记账<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean isTally() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ACCOUNTING).toInteger()
                .intValue() == 1;
    }

    /**
     * 查询发放待记账的数据
     *
     * @return
     */
    public List<CmsLoanSendDTO> querySendTallyData() {
        return cmsLoanSendDao.querySendTallyData();
    }

    /**
     * @return java.lang.String
     * @Description 查询信贷参数履约环节
     * @Param []
     * @Date 2019/11/13 19:31
     * @Author fusb
     */
    @Transactional(readOnly = true)
    public String conSegment() {
        return configService.getConfig(ConfigConstant.CMS, "loan_relate_con_segment").toFormatString();
    }

    /**
     * @return void
     * @Description 如果对接合同管理生成履约信息
     * @Param [loanInfo, send, user]
     * @Date 2019/11/15 10:52
     * @Author fusb
     */
    public void createSegmentInfo(CmsLoanInfo loanInfo, CmsLoanSend send) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(send.getUpdateBy())) {
            user = userService.getByUserId(send.getUpdateBy());
        } else {
            user = userService.getByUserId(send.getCreateBy());
        }
        // 如果对接了合同生成履约信息
        if (StringUtils.isNotEmpty(loanInfo.getConId()) && this.conSegment().contains(CmsConstant.SEGMENT_LOAN)) {
            ConInfoRecordDTO dto = new ConInfoRecordDTO();
            ConInfoDTO conDTO = conCommonService.getConInfo("", loanInfo.getId());
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                // 如果合同终止不产生履约信息和终止信息
                dto.setConId(conDTO.getId());
                dto.setOuterId(send.getId());
                dto.setOpMsg(CmsConstant.INNER_LOAN_SEND);
                dto.setOpDate(send.getLoanDate());
                dto.setOpNo(send.getBillCode());
                dto.setOpMoney(send.getLoanMoney());
                conCommonService.performanceConOp(dto, user);
            }
        }
    }

    /**
     * 单位禁用校验-待记账
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsLoanSendDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
}
